package com.thoriuslight.professionsmod.block;

import java.util.Random;
import java.util.stream.Stream;

import com.thoriuslight.professionsmod.init.ItemInit;
import com.thoriuslight.professionsmod.init.ItemInit.ModItemTier;
import com.thoriuslight.professionsmod.init.ModTileEntityTypes;
import com.thoriuslight.professionsmod.init.SkillInit;
import com.thoriuslight.professionsmod.item.HammerItem;
import com.thoriuslight.professionsmod.item.IMeltable;
import com.thoriuslight.professionsmod.profession.capabilities.CapabilityProfession;
import com.thoriuslight.professionsmod.profession.capabilities.IProfession;
import com.thoriuslight.professionsmod.profession.capabilities.ProfessionTypes.profession;
import com.thoriuslight.professionsmod.tileentity.CastingBasinTileEntity;
import com.thoriuslight.professionsmod.tileentity.CrucibleTileEntity;
import com.thoriuslight.professionsmod.util.MetalItems;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.RedstoneTorchBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CrucibleBlock extends ContainerBlock{
	public static final DirectionProperty FACING = HorizontalBlock.FACING;
	public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;
	public static final DirectionProperty DRAIN_FACING = DirectionProperty.create("drain_facing", Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP);
	private static final VoxelShape SHAPE = Stream.of(
			Block.box(0, 0, 0, 16, 16, 16),
			Block.box(4, 10, 4, 12, 16, 12)
			).reduce((v1, v2) -> {return VoxelShapes.join(v1, v2, IBooleanFunction.ONLY_FIRST);}).get();
	
	public CrucibleBlock(Properties builder) {
		super(builder.lightLevel(state -> {return state.getValue(LIT) ? 5 : 0;}));
	    this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, Boolean.valueOf(false)).setValue(DRAIN_FACING, Direction.UP));
	}
	//Interaction
	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		IProfession iProfession = player.getCapability(CapabilityProfession.PROFESSION, null).orElseThrow(IllegalStateException::new);
		if(iProfession.getProfession() == profession.SMITH && iProfession.getSkillTalent(SkillInit.CRUCIBLE.getId())) {
			TileEntity tileentity = worldIn.getBlockEntity(pos);
			if(tileentity instanceof CrucibleTileEntity) {
				CrucibleTileEntity crucible = (CrucibleTileEntity)tileentity;
				ItemStack stack = player.getMainHandItem();
				Item heldItem = stack.getItem();
				if(heldItem != ItemInit.BLOWPIPE.get() && (!(heldItem instanceof HammerItem) || hit.getDirection().equals(Direction.UP))){
					int burnTime = net.minecraftforge.common.ForgeHooks.getBurnTime(stack, null);
					//Add fuel
					if(burnTime > 0) {
						if(crucible.canAddFuel()) {
							worldIn.playSound((PlayerEntity)null, pos, SoundEvents.SNOW_PLACE, SoundCategory.BLOCKS, 0.5F, 0.4F / (RANDOM.nextFloat() * 0.4F + 0.8F));
							if (worldIn.isClientSide) {
								return ActionResultType.SUCCESS;
							} else {
								if(!state.getValue(LIT)) {
									BlockState blockstate1 = state.setValue(LIT, Boolean.valueOf(true));
									worldIn.setBlock(pos, blockstate1, 2);
								}
								stack.shrink(1);
								crucible.addFuel(burnTime);
								return ActionResultType.SUCCESS;
							}
						}
						else return ActionResultType.FAIL;
					}
					//Cast if drain side was clicked
					else if(hit.getDirection().equals(state.getValue(DRAIN_FACING)) && state.getValue(DRAIN_FACING) != Direction.UP) {
						BlockPos newPos = BlockPos.of(BlockPos.offset(pos.asLong(), hit.getDirection()));
						BlockState blockstate2 = worldIn.getBlockState(newPos);
						if(blockstate2.getBlock() instanceof CastingBasinBlock) {
							if(!worldIn.isClientSide) {
								TileEntity tile = worldIn.getBlockEntity(newPos);
								if(tile instanceof CastingBasinTileEntity) {
									if(!((CastingBasinTileEntity)tile).hasItem()) {
										crucible.cast(newPos, blockstate2);
									}
								}
							}
							return ActionResultType.SUCCESS;
						}
					}
					//Add material
					else if(!crucible.hasItem()) {
						boolean isMeltable = heldItem instanceof IMeltable;
						if(MetalItems.isItemMeltable(heldItem) || (isMeltable && iProfession.getSkillTalent(SkillInit.RECYCLING.getId()))) {
							if(crucible.isItemValid(stack, isMeltable)) {
								if (!worldIn.isClientSide) {
									int i = 1;
									if(!isMeltable) {
										i = Math.min(MetalItems.getMetal(stack.getItem()).getStackSize(), stack.getCount());
									}
									ItemStack newStack = stack.copy();
									newStack.setCount(i);
									crucible.setItem(newStack, isMeltable);
									stack.shrink(i);
								}
								return ActionResultType.SUCCESS;
							}
						}
						else if(iProfession.getSkillTalent(SkillInit.ARSENICAL_BRONZE.getId())) {
							if(heldItem == ItemInit.ARSENIC.get()) {
								if (crucible.getCooldown() == 0 && crucible.checkFluid(ModItemTier.COPPER)) {
									if (!worldIn.isClientSide) {
										crucible.addArsenic();
										stack.shrink(1);
									}
									return ActionResultType.SUCCESS;
								}
							}
						}
					}
					//Remove material
					else if (worldIn.isClientSide) {
						return ActionResultType.SUCCESS;
					} else {
						ItemStack item = crucible.getItem();
						crucible.setItem(ItemStack.EMPTY, false);
						if (!player.inventory.add(item)) {
							player.drop(item, false);
						}
						return ActionResultType.SUCCESS;
					}
				}
			}
		}
		return ActionResultType.PASS;
	}
	@SuppressWarnings("deprecation")
	@Override
	public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if(state.getBlock() != newState.getBlock()) {
			TileEntity tileentity = worldIn.getBlockEntity(pos);
			if(tileentity instanceof CrucibleTileEntity) {
		        InventoryHelper.dropItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), ((CrucibleTileEntity)tileentity).getItem().copy());
			}
			super.onRemove(state, worldIn, pos, newState, isMoving);
		}
	}
	//Create a drain for the casting basin
	public ActionResultType setDrain(World world, BlockPos pos, BlockState state, Direction direction) {
		if(!direction.equals(state.getValue(FACING)) && !direction.equals(Direction.UP) && !direction.equals(Direction.DOWN)) {
			TileEntity tileentity = world.getBlockEntity(pos);
			if(tileentity instanceof CrucibleTileEntity) {
				CrucibleTileEntity crucible = (CrucibleTileEntity)tileentity;
				if(crucible.getCooldown() == 0) {
					if (world.isClientSide) {
						return ActionResultType.SUCCESS;
					}
					else {
						if(direction.equals(state.getValue(DRAIN_FACING))) {
							BlockState blockstate1 = state.setValue(DRAIN_FACING, Direction.UP);
							world.setBlock(pos, blockstate1, 2);
						}
						else {
							BlockState blockstate1 = state.setValue(DRAIN_FACING, direction);
							world.setBlock(pos, blockstate1, 2);
						}
						return ActionResultType.SUCCESS;
					}			
				}
			}
		}
		return ActionResultType.PASS;
	}
	public static Direction getDrainFacing(BlockState state) {
		return state.getValue(DRAIN_FACING);
	}
	public static Direction getFacing(BlockState state) {
		return state.getValue(FACING);
	}
	//TileEntity
	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return ModTileEntityTypes.CRUCIBLE.get().create();
	}
	@Override
	public TileEntity newBlockEntity(IBlockReader worldIn) {
		return ModTileEntityTypes.CRUCIBLE.get().create();
	}
	//Properties
	public static void extinguish(BlockState state, World worldIn, BlockPos pos) {
		BlockState blockstate1 = state.setValue(LIT, Boolean.valueOf(false));
		worldIn.setBlock(pos, blockstate1, 2);
	}
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}
	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING))).setValue(DRAIN_FACING, rot.rotate(state.getValue(DRAIN_FACING)));
	}
	@SuppressWarnings("deprecation")
	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		
		return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
	}
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(FACING, LIT, DRAIN_FACING);
	}
	//Rendering
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}
	@Override
	public BlockRenderType getRenderShape(BlockState state) {
		return BlockRenderType.MODEL;
	}
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		if (stateIn.getValue(LIT)) {
			double d0 = (double)pos.getX() + 0.5D;
			double d1 = (double)pos.getY();
			double d2 = (double)pos.getZ() + 0.5D;
			if (rand.nextDouble() < 0.1D) {
				worldIn.playLocalSound(d0, d1, d2, SoundEvents.FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
			}
			Direction direction = stateIn.getValue(FACING);
			Direction.Axis direction$axis = direction.getAxis();
			double d3 = 0.52D;
			double d4 = rand.nextDouble() * 0.4D - 0.2D;
	        double d5 = direction$axis == Direction.Axis.X ? (double)direction.getStepX() * d3 : d4;
	        double d6 = rand.nextDouble() * 4.0D / 16.0D + 0.2D;
	        double d7 = direction$axis == Direction.Axis.Z ? (double)direction.getStepZ() * d3 : d4;
	        worldIn.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
	        worldIn.addParticle(ParticleTypes.FLAME, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
		}
	}
}
