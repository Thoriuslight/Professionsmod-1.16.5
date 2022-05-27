package com.thoriuslight.professionsmod.block;

import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.thoriuslight.professionsmod.init.BlockInit;
import com.thoriuslight.professionsmod.init.ModTileEntityTypes;
import com.thoriuslight.professionsmod.profession.capabilities.CapabilityProfession;
import com.thoriuslight.professionsmod.profession.capabilities.IProfession;
import com.thoriuslight.professionsmod.profession.capabilities.ProfessionTypes.profession;
import com.thoriuslight.professionsmod.tileentity.ExtractorTileEntity;
import com.thoriuslight.professionsmod.tileentity.OvenControllerTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
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
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class ExtractorBlock extends ContainerBlock{
	public static final DirectionProperty FACING = HorizontalBlock.FACING;
	public static final DirectionProperty DRAIN_FACING = DirectionProperty.create("drain_facing", Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP);
	private static final VoxelShape SHAPE = Stream.of(
			Block.box(2, 0, 2, 14, 12, 14),
			Block.box(3, 12, 3, 13, 16, 13)
			).reduce((v1, v2) -> {return VoxelShapes.join(v1, v2, IBooleanFunction.OR);}).get();
	public ExtractorBlock(Properties p_i48440_1_) {
		super(p_i48440_1_);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(DRAIN_FACING, Direction.UP));
	}
	@Override
	public ActionResultType use(BlockState p_225533_1_, World p_225533_2_, BlockPos pos, PlayerEntity player, Hand p_225533_5_, BlockRayTraceResult p_225533_6_) {
		IProfession iProfession = player.getCapability(CapabilityProfession.PROFESSION, null).orElseThrow(IllegalStateException::new);
		if(iProfession.getProfession() == profession.ALCHEMIST) {
			if (p_225533_2_.isClientSide) {
				return ActionResultType.SUCCESS;
			} else {
				TileEntity tileentity = p_225533_2_.getBlockEntity(pos);
				if (tileentity instanceof ExtractorTileEntity) {
			    	NetworkHooks.openGui((ServerPlayerEntity)player, (ExtractorTileEntity)tileentity, pos);
			    }
				return ActionResultType.CONSUME;
			}
		}
		return ActionResultType.PASS;
	}
	@Override
	public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
		return ExtractorBlock.SHAPE;
	}
	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext p_196258_1_) {
		BlockPos blockpos = p_196258_1_.getClickedPos();
	    if (blockpos.getY() < 255 && p_196258_1_.getLevel().getBlockState(blockpos.above()).canBeReplaced(p_196258_1_)) {
	        return this.defaultBlockState().setValue(FACING, p_196258_1_.getHorizontalDirection());
	    } else {
	    	return null;
	    }
	}
	public static BlockState setDrainFacing(World world, BlockPos pos, BlockState state, Direction direction) {
		BlockState blockstate = state.setValue(DRAIN_FACING, direction);
		return blockstate;
	}
	
	@Override
	public void setPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity p_180633_4_, ItemStack p_180633_5_) {
		world.setBlock(pos.above(), BlockInit.EXTRACTOR_UPPER_BLOCK.get().defaultBlockState(), 3);
	}
	@Override
	public BlockState rotate(BlockState p_185499_1_, Rotation p_185499_2_) {
		return p_185499_1_.setValue(FACING, p_185499_2_.rotate(p_185499_1_.getValue(FACING)));
	}
	@SuppressWarnings("deprecation")
	@Override
	public BlockState mirror(BlockState p_185471_1_, Mirror p_185471_2_) {
		return p_185471_2_ == Mirror.NONE ? p_185471_1_ : p_185471_1_.rotate(p_185471_2_.getRotation(p_185471_1_.getValue(FACING)));
	}
	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> p_206840_1_) {
		p_206840_1_.add(FACING).add(DRAIN_FACING);
	}
	@SuppressWarnings("deprecation")
	@Override
	public BlockState updateShape(BlockState p_196271_1_, Direction p_196271_2_, BlockState p_196271_3_, IWorld p_196271_4_, BlockPos p_196271_5_, BlockPos p_196271_6_) {
		if (p_196271_2_.getAxis() != Direction.Axis.Y || !(p_196271_2_ == Direction.UP) || p_196271_3_.is(BlockInit.EXTRACTOR_UPPER_BLOCK.get())) {
			return true && p_196271_2_ == Direction.DOWN && !p_196271_1_.canSurvive(p_196271_4_, p_196271_5_) ? Blocks.AIR.defaultBlockState() : super.updateShape(p_196271_1_, p_196271_2_, p_196271_3_, p_196271_4_, p_196271_5_, p_196271_6_);
		} else {
			return Blocks.AIR.defaultBlockState();
		}
	}
	@Override
	public TileEntity newBlockEntity(IBlockReader p_196283_1_) {
		return ModTileEntityTypes.EXTRACTOR.get().create();
	}
	@Override
	public BlockRenderType getRenderShape(BlockState state) {
		return BlockRenderType.MODEL;
	}
	@Override
	public PushReaction getPistonPushReaction(BlockState p_149656_1_) {
		return PushReaction.IGNORE;
	}
	@SuppressWarnings("deprecation")
	@Override
	public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
		if(state.getBlock() != newState.getBlock()) {
			for(Direction direction : Direction.Plane.HORIZONTAL) {
				BlockState state1 = world.getBlockState(pos.relative(direction));
				if(state1.getBlock() == BlockInit.OVEN_BRICK_BLOCK.get()) {
		    		TileEntity tile = world.getBlockEntity(OvenBrickBlock.getControllerPos(pos.relative(direction), state1));
		    		if(tile != null) {
		    			((OvenControllerTileEntity)tile).removeExtractor(pos);
		    		}
				};
			}
			TileEntity tileentity = world.getBlockEntity(pos);
			if(tileentity instanceof IInventory) {
		        InventoryHelper.dropContents(world, pos, (IInventory)tileentity);
			}
			super.onRemove(state, world, pos, newState, isMoving);
		}
	}
}
