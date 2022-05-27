package com.thoriuslight.professionsmod.block;

import java.util.stream.Stream;

import com.thoriuslight.professionsmod.init.BlockInit;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.PushReaction;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

public class ExtractorBlockUpper extends Block{
	private static final VoxelShape SHAPE = Stream.of(
			Block.box(3, 0, 3, 13, 8, 13),
			Block.box(4, 8, 4, 12, 12, 12)
			).reduce((v1, v2) -> {return VoxelShapes.join(v1, v2, IBooleanFunction.OR);}).get();
	public ExtractorBlockUpper(Properties p_i48440_1_) {
		super(p_i48440_1_);
	}
	public BlockRenderType getRenderShape(BlockState p_149645_1_) {
		return BlockRenderType.INVISIBLE;
	}
	@Override
	public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
		return ExtractorBlockUpper.SHAPE;
	}
	@SuppressWarnings("deprecation")
	@Override
	public BlockState updateShape(BlockState p_196271_1_, Direction p_196271_2_, BlockState p_196271_3_, IWorld p_196271_4_, BlockPos p_196271_5_, BlockPos p_196271_6_) {
		if (p_196271_2_.getAxis() != Direction.Axis.Y || !(p_196271_2_ == Direction.DOWN) || p_196271_3_.is(BlockInit.EXTRACTOR_CONTROLLER_BLOCK.get())) {
			return true && p_196271_2_ == Direction.UP && !p_196271_1_.canSurvive(p_196271_4_, p_196271_5_) ? Blocks.AIR.defaultBlockState() : super.updateShape(p_196271_1_, p_196271_2_, p_196271_3_, p_196271_4_, p_196271_5_, p_196271_6_);
		} else {
			return Blocks.AIR.defaultBlockState();
		}
	}
	@Override
	public PushReaction getPistonPushReaction(BlockState p_149656_1_) {
		return PushReaction.IGNORE;
	}
}
