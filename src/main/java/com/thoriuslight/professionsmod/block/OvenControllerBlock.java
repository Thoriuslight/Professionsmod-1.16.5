package com.thoriuslight.professionsmod.block;

import java.util.Random;

import com.thoriuslight.professionsmod.init.BlockInit;
import com.thoriuslight.professionsmod.init.ModTileEntityTypes;
import com.thoriuslight.professionsmod.tileentity.OvenControllerTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.RedstoneTorchBlock;
import net.minecraft.block.material.PushReaction;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class OvenControllerBlock extends Block{
	public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;
	public static final DirectionProperty FACING = HorizontalBlock.FACING;
	public OvenControllerBlock(Properties properties) {
		super(properties.lightLevel(state -> {return state.getValue(LIT) ? 15 : 0;}));
		this.registerDefaultState(this.stateDefinition.any().setValue(LIT, Boolean.valueOf(false)).setValue(FACING, Direction.NORTH));
	}
	public static boolean isLit(BlockState state) {
		return state.getValue(LIT);
	}
	public BlockState getDirectionalState(Direction direction) {
		return this.defaultBlockState().setValue(FACING, direction);
	}
	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return ModTileEntityTypes.OVEN_CONTROLLER.get().create();
	}
	@Override
	public BlockRenderType getRenderShape(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}
	@SuppressWarnings("deprecation")
	@Override
	public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if(!(newState.getBlock().equals(BlockInit.INSULATION_BRICK_BLOCK.get()) || newState.getBlock().equals(this))) {
			if(!worldIn.isClientSide) {
				TileEntity tile = worldIn.getBlockEntity(pos);
				if(tile instanceof OvenControllerTileEntity) {
					((OvenControllerTileEntity) tile).destroyMultiblock(pos, worldIn, pos);
				}
			}
		}
		super.onRemove(state, worldIn, pos, newState, isMoving);
	}
	/*@Override
	public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
		return false;
	}*/
	@Override
	public PushReaction getPistonPushReaction(BlockState p_149656_1_) {
		return PushReaction.IGNORE;
	}
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(LIT, FACING);
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
			double d3 = 1.52D;
			double d4 = rand.nextDouble() * 0.6D - 0.3D;
	        double d5 = direction$axis == Direction.Axis.X ? (double)direction.getStepX() * d3 : d4;
	        double d6 = rand.nextDouble() * 6.0D / 16.0D - 1.0D;
	        double d7 = direction$axis == Direction.Axis.Z ? (double)direction.getStepZ() * d3 : d4;
	        worldIn.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
	        worldIn.addParticle(ParticleTypes.FLAME, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
		}
	}
}
