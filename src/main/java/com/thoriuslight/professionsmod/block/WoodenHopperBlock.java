package com.thoriuslight.professionsmod.block;

import com.thoriuslight.professionsmod.init.ModTileEntityTypes;
import com.thoriuslight.professionsmod.tileentity.WoodenHopperTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

@SuppressWarnings("unused")
public class WoodenHopperBlock extends ContainerBlock{
	private static final VoxelShape SHAPE = VoxelShapes.join(Block.box(2, 0, 2, 14, 12, 14), Block.box(0, 12, 0, 16, 16, 16), IBooleanFunction.OR);
	//private static final VoxelShape DOWN_RAYTRACE_SHAPE = IHopper.INSIDE_BOWL_SHAPE;
	
	public WoodenHopperBlock(Properties properties) {
		super(properties);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}
	
	//@Override
	//public VoxelShape getRaytraceShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
	//	return DOWN_RAYTRACE_SHAPE;
	//}
	
	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
	
	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (worldIn.isClientSide) {
			return ActionResultType.SUCCESS;
		} else {
			TileEntity tileentity = worldIn.getBlockEntity(pos);
		    if (tileentity instanceof WoodenHopperTileEntity) {
		    	NetworkHooks.openGui((ServerPlayerEntity)player, (WoodenHopperTileEntity)tileentity, pos);
		    }
		    return ActionResultType.SUCCESS;
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if(state.getBlock() != newState.getBlock()) {
	         TileEntity tileentity = worldIn.getBlockEntity(pos);
	         if (tileentity instanceof WoodenHopperTileEntity) {
	             InventoryHelper.dropContents(worldIn, pos, (WoodenHopperTileEntity)tileentity);
	             //worldIn.updateComparatorOutputLevel(pos, this);
	          }
	          super.onRemove(state, worldIn, pos, newState, isMoving);
		}
	}

	@Override
	public TileEntity newBlockEntity(IBlockReader worldIn) {
		//return ModTileEntityTypes.WOODEN_HOPPER.get().create();
		return null;
	}
	
	@Override
	public BlockRenderType getRenderShape(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	/*@Override
	public boolean hasComparatorInputOverride(BlockState state) {
		return true;
	}
	
	@Override
	public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
		return Container.calcRedstone(worldIn.getTileEntity(pos));
	}
	
	@Override
	public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
		return false;
	}*/
}
