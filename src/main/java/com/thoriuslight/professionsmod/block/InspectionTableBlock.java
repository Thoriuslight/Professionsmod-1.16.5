package com.thoriuslight.professionsmod.block;

import java.util.stream.Stream;

import com.thoriuslight.professionsmod.inventory.container.InspectionContainerProvider;
import com.thoriuslight.professionsmod.profession.capabilities.CapabilityProfession;
import com.thoriuslight.professionsmod.profession.capabilities.IProfession;
import com.thoriuslight.professionsmod.profession.capabilities.ProfessionTypes.profession;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class InspectionTableBlock extends Block{
	public static final DirectionProperty FACING = HorizontalBlock.FACING;
	public static final VoxelShape SHAPE = Stream.of(
			Block.box(13, 0, 13, 15, 12, 15),
			Block.box(13, 0, 1, 15, 12, 3),
			Block.box(1, 0, 1, 3, 12, 3),
			Block.box(1, 0, 13, 3, 12, 15),
			Block.box(0, 12, 0, 16, 14, 16)
			).reduce((v1, v2) -> {return VoxelShapes.join(v1, v2, IBooleanFunction.OR);}).get();
	
	public InspectionTableBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}
	
	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		IProfession iProfession = player.getCapability(CapabilityProfession.PROFESSION, null).orElseThrow(IllegalStateException::new);
		if (worldIn.isClientSide) {
			if(iProfession.getProfession() == profession.SMITH) {
				if(iProfession.getSkillTalent(1))
					return ActionResultType.SUCCESS;
			}
			return ActionResultType.PASS;
		} else {
			if(iProfession.getProfession() == profession.SMITH) {
				if(iProfession.getSkillTalent(1)) {
					NetworkHooks.openGui((ServerPlayerEntity) player, new InspectionContainerProvider(pos), pos);
    				return ActionResultType.SUCCESS;
				}
            }
			return ActionResultType.PASS;
		}
	}
	
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Deprecated
	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}
	@Deprecated
	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
	}
	
	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
}
