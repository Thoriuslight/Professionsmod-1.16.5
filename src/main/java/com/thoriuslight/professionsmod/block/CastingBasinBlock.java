package com.thoriuslight.professionsmod.block;

import java.util.stream.Stream;

import com.thoriuslight.professionsmod.client.gui.CastingBasinScreen;
import com.thoriuslight.professionsmod.init.ItemInit;
import com.thoriuslight.professionsmod.init.ItemInit.ModItemTier;
import com.thoriuslight.professionsmod.init.ModTileEntityTypes;
import com.thoriuslight.professionsmod.init.SkillInit;
import com.thoriuslight.professionsmod.item.MoltenMetalItem;
import com.thoriuslight.professionsmod.profession.capabilities.CapabilityProfession;
import com.thoriuslight.professionsmod.profession.capabilities.IProfession;
import com.thoriuslight.professionsmod.state.properties.Tool;
import com.thoriuslight.professionsmod.tileentity.CastingBasinTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class CastingBasinBlock extends Block{
	public static final IntegerProperty FILL = IntegerProperty.create("fill", 0, 2);
	public static final BooleanProperty LOCKED = BooleanProperty.create("locked");
	
	private static final VoxelShape SHAPE_N = Stream.of(
			Block.box(2, 2, 14, 14, 6, 16),
			Block.box(2, 2, 0, 14, 6, 2),
			Block.box(14, 2, 0, 16, 6, 16),
			Block.box(0, 2, 0, 2, 6, 16),
			Block.box(0, 0, 0, 16, 2, 16)
			).reduce((v1, v2) -> {return VoxelShapes.join(v1, v2, IBooleanFunction.OR);}).get();
	private static final VoxelShape SHAPE_NFILL = Stream.of(
			Block.box(2, 2, 14, 14, 6, 16),
			Block.box(2, 2, 0, 14, 6, 2),
			Block.box(14, 2, 0, 16, 6, 16),
			Block.box(0, 2, 0, 2, 6, 16),
			Block.box(0, 0, 0, 16, 2, 16),
			Block.box(2, 2, 2, 14, 5, 14)
			).reduce((v1, v2) -> {return VoxelShapes.join(v1, v2, IBooleanFunction.OR);}).get();
	
	public CastingBasinBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FILL, Integer.valueOf(0)).setValue(LOCKED, Boolean.FALSE));
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return state.getValue(FILL) > 0 ? SHAPE_NFILL : SHAPE_N;
	}
	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return ModTileEntityTypes.CASTINGBASIN.get().create();
	}
	
	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		TileEntity tileentity = worldIn.getBlockEntity(pos);
		if(tileentity instanceof CastingBasinTileEntity) {
			IProfession iProfession = player.getCapability(CapabilityProfession.PROFESSION, null).orElseThrow(IllegalStateException::new);
			CastingBasinTileEntity castingBasin = (CastingBasinTileEntity)tileentity;
			if(state.getValue(LOCKED)) {
				if(castingBasin.isLocked()) {
					return ActionResultType.PASS;
				}
				else {
					this.unlock(state, worldIn, pos);
				}
			}
			//Get stored item
			if(castingBasin.hasItem()) {
				if(!worldIn.isClientSide) {
					ItemStack itemstack = castingBasin.getItem().copy();
					BlockState state2 = state.setValue(FILL, Integer.valueOf(0));
		    		worldIn.setBlock(pos, state2, 2);
					if (!player.inventory.add(itemstack)) {
						this.dropItem(worldIn, pos, itemstack);
					}
				}
				worldIn.playSound((PlayerEntity)null,  player.getX(), player.getY() + 0.5, player.getZ(), SoundEvents.ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, (RANDOM.nextFloat() - RANDOM.nextFloat()) * 1.4F + 2.0F);
				castingBasin.setItem(ItemStack.EMPTY);
				return ActionResultType.SUCCESS;
			}
			//Refill mould
			ItemStack stack = player.getMainHandItem();
			if((stack.getItem() == Items.CLAY_BALL || stack.getItem() == ItemInit.DOUGH.get()) && state.getValue(FILL) == 0) {
				if (worldIn.isClientSide()) {
					return ActionResultType.SUCCESS;
				} else {
					worldIn.playSound((PlayerEntity)null, pos, SoundEvents.SNOW_PLACE, SoundCategory.BLOCKS, 0.5F, 0.4F / (RANDOM.nextFloat() * 0.4F + 0.8F));
					int property = 0;
					if(stack.getItem() == Items.CLAY_BALL) {
						property = 1;
					}
					else {
						property = 2;
					}
					BlockState blockstate1 = state.setValue(FILL, Integer.valueOf(property));
					Block.pushEntitiesUp(state, blockstate1, worldIn, pos);
	            	worldIn.setBlock(pos, blockstate1, 2);
	            	player.getMainHandItem().shrink(1);
	            	return ActionResultType.SUCCESS;
				}
			} 
			//Add metal
			else if(player.getMainHandItem().getItem() instanceof MoltenMetalItem && state.getValue(FILL) > 0){

				if (worldIn.isClientSide) {			//Client side
					if(iProfession.getSkillTalent(SkillInit.BASIC_SMITHING.getId())){
						if(castingBasin.canAddMetal(((MoltenMetalItem)player.getMainHandItem().getItem()).getMetal())){
							return ActionResultType.SUCCESS;
						}
					}
					return ActionResultType.PASS;
				} 
				else {							//Server side
					if(iProfession.getSkillTalent(SkillInit.BASIC_SMITHING.getId())){
						if(castingBasin.addMetal(((MoltenMetalItem)player.getMainHandItem().getItem()).getMetal())) {
							worldIn.playSound((PlayerEntity)null, pos, SoundEvents.BUCKET_EMPTY_LAVA, SoundCategory.BLOCKS, 0.5F, 0.4F / (RANDOM.nextFloat() * 0.4F + 0.8F));
							player.getMainHandItem().shrink(1);
							if (!player.inventory.add(new ItemStack(Items.FLOWER_POT))) {
								player.drop(new ItemStack(Items.FLOWER_POT), false);
							}
							return ActionResultType.SUCCESS;
						}
						return ActionResultType.PASS;	
					}
				}
			}
			//Set tool
			else if(player.getMainHandItem().isEmpty()) {
				if (worldIn.isClientSide) {
					Minecraft.getInstance().setScreen(new CastingBasinScreen(pos, iProfession));
				}
				return ActionResultType.SUCCESS;
			}
		}
		return ActionResultType.PASS;
	}
	
	@Override
	public void playerWillDestroy(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        TileEntity tileentity = worldIn.getBlockEntity(pos);
        if(tileentity instanceof CastingBasinTileEntity) {
        	if(!worldIn.isClientSide) {
        		ItemStack itemstack1 = ((CastingBasinTileEntity)tileentity).getItem().copy();
        		worldIn.levelEvent(1010, pos, 0);
        		double d0 = (double)(worldIn.random.nextFloat() * 0.1F) + (double)0.5F;
        		double d1 = (double)(worldIn.random.nextFloat() * 0.3F) + (double)0.50000002F;
        		double d2 = (double)(worldIn.random.nextFloat() * 0.1F) + (double)0.5F;
        		ItemEntity itementity = new ItemEntity(worldIn, (double)pos.getX() + d0, (double)pos.getY() + d1, (double)pos.getZ() + d2, itemstack1);
        		itementity.setPickUpDelay(5);
        		itementity.setDeltaMovement(worldIn.random.nextDouble() * 0.1D - 0.05D, 0.2D, worldIn.random.nextDouble() * 0.1D - 0.05D);
        		worldIn.addFreshEntity(itementity);
        	}
        }
		super.playerWillDestroy(worldIn, pos, state, player);
	}
	public static int getNuggetAmount(World world, BlockPos pos, BlockState state, ModItemTier metal) {
        TileEntity tileentity = world.getBlockEntity(pos);
		if(state.getValue(FILL) > 0) {
        if(tileentity instanceof CastingBasinTileEntity) {
        	CastingBasinTileEntity castingBasin = (CastingBasinTileEntity)tileentity;
        	if(castingBasin.canAddMetal(metal)) {
        		return ((CastingBasinTileEntity)tileentity).getRequiredNuggets();
        	}
        }
		}
		return 0;
	}
	public boolean cast(World world, BlockPos pos, BlockState state, ModItemTier metal) {
		if(!world.isClientSide) {
			if(state.getValue(FILL) > 0) {
	            TileEntity tileentity = world.getBlockEntity(pos);
	            if(tileentity instanceof CastingBasinTileEntity) {
	            	CastingBasinTileEntity castingBasin = (CastingBasinTileEntity)tileentity;
	            	if(castingBasin.canAddMetal(metal)) {
	            		world.playSound((PlayerEntity)null, pos, SoundEvents.BUCKET_EMPTY_LAVA, SoundCategory.BLOCKS, 0.5F, 0.4F / (RANDOM.nextFloat() * 0.4F + 0.8F));
	            		BlockState state2 = state.setValue(LOCKED, Boolean.FALSE);
	            		world.setBlock(pos, state2, 2);
	            		castingBasin.cast(metal);
	            		return true;
	            	}
	            }
			}
		}
		return false;
	}
	public void dropItem(World worldIn, BlockPos pos, ItemStack itemstack) {
		if (!itemstack.isEmpty()) {
			BlockState state = worldIn.getBlockState(pos).setValue(FILL, Integer.valueOf(0)).setValue(LOCKED, Boolean.FALSE);
    		worldIn.setBlock(pos, state, 2);
    		ItemStack itemstack1 = itemstack.copy();
    		worldIn.levelEvent(1010, pos, 0);
    		double d0 = (double)(worldIn.random.nextFloat() * 0.1F) + (double)0.5F;
    		double d1 = (double)(worldIn.random.nextFloat() * 0.3F) + (double)0.50000002F;
    		double d2 = (double)(worldIn.random.nextFloat() * 0.1F) + (double)0.5F;
    		ItemEntity itementity = new ItemEntity(worldIn, (double)pos.getX() + d0, (double)pos.getY() + d1, (double)pos.getZ() + d2, itemstack1);
    		itementity.setPickUpDelay(5);
    		itementity.setDeltaMovement(worldIn.random.nextDouble() * 0.1D - 0.05D, 0.2D, worldIn.random.nextDouble() * 0.1D - 0.05D);
    		worldIn.addFreshEntity(itementity);
		}      
	}
	public boolean lock(BlockState state, World world, BlockPos pos, long lockerPos) {
        TileEntity tileentity = world.getBlockEntity(pos);
        if(tileentity instanceof CastingBasinTileEntity) {
        	CastingBasinTileEntity castingBasin = (CastingBasinTileEntity) tileentity;
        	if(state.getValue(LOCKED)) {
        		if(castingBasin.isLocked())
        			return false;
        	} else {
        		BlockState blockstate1 = state.setValue(LOCKED, Boolean.TRUE);
        		world.setBlock(pos, blockstate1, 2);
        	}
        	castingBasin.setLocker(lockerPos);
        	return true;
        }
        return false;
	}
	public void unlock(BlockState state, World world, BlockPos pos) {
		BlockState blockstate1 = state.setValue(LOCKED, Boolean.FALSE);
    	world.setBlock(pos, blockstate1, 2);
	}
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.defaultBlockState().setValue(FILL, Integer.valueOf(0)).setValue(LOCKED, Boolean.FALSE);
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(FILL).add(LOCKED);
	}
	public static void setTool(Tool tool, World world, BlockPos pos) {
		if (!world.isClientSide) {
            TileEntity tileentity = world.getBlockEntity(pos);
            if(tileentity instanceof CastingBasinTileEntity) {
            	((CastingBasinTileEntity)tileentity).setTool(tool);;
            }
		}
	}
}
