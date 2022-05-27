package com.thoriuslight.professionsmod.block;

import java.util.Random;
import com.thoriuslight.professionsmod.init.ModTileEntityTypes;
import com.thoriuslight.professionsmod.item.IHeatable;
import com.thoriuslight.professionsmod.profession.capabilities.CapabilityProfession;
import com.thoriuslight.professionsmod.profession.capabilities.IProfession;
import com.thoriuslight.professionsmod.profession.capabilities.ProfessionTypes.profession;
import com.thoriuslight.professionsmod.tileentity.ForgeTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ForgeBlock extends ContainerBlock{
	public static final BooleanProperty BURNING = BooleanProperty.create("burning");
	private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 7, 16);
	
	public ForgeBlock(Properties builder) {
		super(builder.lightLevel(state -> {return state.getValue(BURNING) ? 7 : 0;}));
		this.registerDefaultState(this.stateDefinition.any().setValue(BURNING, Boolean.valueOf(false)));
	}
	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return ModTileEntityTypes.FORGE.get().create();
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}
	@Override
	public TileEntity newBlockEntity(IBlockReader worldIn) {
		return ModTileEntityTypes.FORGE.get().create();
	}
	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		IProfession iProfession = player.getCapability(CapabilityProfession.PROFESSION, null).orElseThrow(IllegalStateException::new);
		if(iProfession.getProfession() == profession.SMITH) {
			TileEntity tileentity = worldIn.getBlockEntity(pos);
			if(tileentity instanceof ForgeTileEntity) {
				ForgeTileEntity forge = (ForgeTileEntity)tileentity;
				ItemStack stack = player.getMainHandItem();
				int burnTime = net.minecraftforge.common.ForgeHooks.getBurnTime(stack, null);
				if(burnTime > 0) {
					if(forge.canAddFuel()) {
						if (worldIn.isClientSide) {
							return ActionResultType.SUCCESS;
						} else {
							worldIn.playSound((PlayerEntity)null, pos, SoundEvents.SNOW_PLACE, SoundCategory.BLOCKS, 0.5F, 0.4F / (RANDOM.nextFloat() * 0.4F + 0.8F));
							if(!state.getValue(BURNING)) {
								BlockState blockstate1 = state.setValue(BURNING, Boolean.valueOf(true));
								worldIn.setBlock(pos, blockstate1, 2);
							}
							stack.shrink(1);
							forge.addFuel(burnTime);
							return ActionResultType.SUCCESS;
						}
					}
				}
				if(!forge.hasItem()) {
					if(stack.getItem() instanceof IHeatable ) {
						if (worldIn.isClientSide) {
							return ActionResultType.SUCCESS;
						} else {
							forge.setItem(stack.copy());
							stack.shrink(1);
							return ActionResultType.SUCCESS;
						}
					}
				}
				else {
					if (worldIn.isClientSide) {
						return ActionResultType.SUCCESS;
					} else {
						ItemStack item = forge.getItem();
						forge.setItem(ItemStack.EMPTY);
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
	public static void extinguish(BlockState state, World worldIn, BlockPos pos) {
		BlockState blockstate1 = state.setValue(BURNING, Boolean.valueOf(false));
		worldIn.setBlock(pos, blockstate1, 2);
	}
	@Override
	public void playerWillDestroy(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		TileEntity tileentity = worldIn.getBlockEntity(pos);
		if(tileentity instanceof ForgeTileEntity) {
			if(!worldIn.isClientSide) {
				ItemStack stack = ((ForgeTileEntity)tileentity).getItem();
				if (!stack.isEmpty()) {
		               worldIn.levelEvent(1010, pos, 0);
		               double d0 = (double)(worldIn.random.nextFloat() * 0.7F) + (double)0.15F;
		               double d1 = (double)(worldIn.random.nextFloat() * 0.7F) + (double)0.060000002F + 0.6D;
		               double d2 = (double)(worldIn.random.nextFloat() * 0.7F) + (double)0.15F;
		               ItemStack itemstack1 = stack.copy();
		               ItemEntity itementity = new ItemEntity(worldIn, (double)pos.getX() + d0, (double)pos.getY() + d1, (double)pos.getZ() + d2, itemstack1);
		               itementity.setDefaultPickUpDelay();
		               worldIn.addFreshEntity(itementity);
				}
			}
		}
		super.playerWillDestroy(worldIn, pos, state, player);
	}
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.defaultBlockState().setValue(BURNING, Boolean.valueOf(false));
	}
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(BURNING);
	}
	@Override
	public BlockRenderType getRenderShape(BlockState state) {
		return BlockRenderType.MODEL;
	}
	@Override
	public void entityInside(BlockState state, World p_196262_2_, BlockPos p_196262_3_, Entity entity) {
		if(state.getValue(BURNING)) {
			entity.hurt(DamageSource.IN_FIRE, 1.0F);
		}
	}
	@OnlyIn(Dist.CLIENT)
	@Override
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		if (stateIn.getValue(BURNING)) {
			double d0 = (double)pos.getX() + 0.5D;
			double d1 = (double)pos.getY();
			double d2 = (double)pos.getZ() + 0.5D;
			if (rand.nextDouble() < 0.1D) {
				worldIn.playLocalSound(d0, d1, d2, SoundEvents.FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
			}
		}
	}
}
