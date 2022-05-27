package com.thoriuslight.professionsmod.block;

import com.thoriuslight.professionsmod.init.BlockInit;
import com.thoriuslight.professionsmod.init.SkillInit;
import com.thoriuslight.professionsmod.profession.capabilities.CapabilityProfession;
import com.thoriuslight.professionsmod.profession.capabilities.IProfession;
import com.thoriuslight.professionsmod.profession.capabilities.ProfessionTypes.profession;
import com.thoriuslight.professionsmod.tileentity.OvenControllerTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class OvenBrickBlock extends Block{
	public static final IntegerProperty POS = IntegerProperty.create("pos", 0, 26);
	public OvenBrickBlock(Properties properties) {
		super(properties.noOcclusion());
		this.registerDefaultState(this.stateDefinition.any().setValue(POS, Integer.valueOf(13)));
	}
	
	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		IProfession iProfession = player.getCapability(CapabilityProfession.PROFESSION, null).orElseThrow(IllegalStateException::new);
		if(iProfession.getProfession() == profession.ALCHEMIST) {
			if(iProfession.getSkillTalent(SkillInit.OVENS.getId())) {
				if (worldIn.isClientSide) {
					return ActionResultType.SUCCESS;
				} else {
					BlockPos tilePos = OvenBrickBlock.getControllerPos(pos, state);
					TileEntity tileentity = worldIn.getBlockEntity(tilePos);
					if (tileentity instanceof OvenControllerTileEntity) {
				    	NetworkHooks.openGui((ServerPlayerEntity)player, (OvenControllerTileEntity)tileentity, tilePos);
					}
					return ActionResultType.SUCCESS;
				}
			}
		}
		return ActionResultType.PASS;
	}
	
	public static void setController(BlockPos pos, World world, BlockState state, int i) {
		world.setBlock(pos, state.setValue(POS, Integer.valueOf(i)), 2);
	}
	public static BlockPos getControllerPos(BlockPos pos, BlockState state) {
		int loc = state.getValue(POS);
		int z = loc / 9 - 1;
		int y = loc % 9 / 3 - 1;
		int x = loc % 3 - 1;
		return pos.offset(-x, -y ,-z);
	}
	@Override
	public BlockRenderType getRenderShape(BlockState state) {
		return BlockRenderType.INVISIBLE;
	}
	
	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(POS);
	}
	
	/*@Override
	@OnlyIn(Dist.CLIENT)
	public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return 1.0F;
	}*/
	
	@Override
	public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
		return true;
	}
	
	@Override
	public ItemStack getCloneItemStack(IBlockReader worldIn, BlockPos pos, BlockState state) {
		return new ItemStack(BlockInit.INSULATION_BRICK_BLOCK.get());
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if(!(newState.getBlock().equals(BlockInit.INSULATION_BRICK_BLOCK.get()) || newState.getBlock().equals(this))) {
			if(!worldIn.isClientSide) {
				BlockPos location = OvenBrickBlock.getControllerPos(pos, state);
				TileEntity tile = worldIn.getBlockEntity(location);
				if(tile instanceof OvenControllerTileEntity) {
					((OvenControllerTileEntity) tile).destroyMultiblock(location, worldIn, pos);
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
}
