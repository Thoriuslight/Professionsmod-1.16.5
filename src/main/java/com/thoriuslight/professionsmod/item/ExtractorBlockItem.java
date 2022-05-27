package com.thoriuslight.professionsmod.item;

import javax.annotation.Nullable;

import com.thoriuslight.professionsmod.block.ExtractorBlock;
import com.thoriuslight.professionsmod.block.OvenBrickBlock;
import com.thoriuslight.professionsmod.init.BlockInit;
import com.thoriuslight.professionsmod.init.SkillInit;
import com.thoriuslight.professionsmod.profession.capabilities.CapabilityProfession;
import com.thoriuslight.professionsmod.profession.capabilities.IProfession;
import com.thoriuslight.professionsmod.profession.capabilities.ProfessionTypes.profession;
import com.thoriuslight.professionsmod.tileentity.OvenControllerTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class ExtractorBlockItem extends BlockItem{

	public ExtractorBlockItem(Block p_i48527_1_, Properties p_i48527_2_) {
		super(p_i48527_1_, p_i48527_2_);
	}
	@Override
	@Nullable
	protected BlockState getPlacementState(BlockItemUseContext context) {
		/*IProfession iProfession = context.getPlayer().getCapability(CapabilityProfession.PROFESSION, null).orElseThrow(IllegalStateException::new);
		if(iProfession.getProfession() == profession.ALCHEMIST && iProfession.getSkillTalent(SkillInit.EXTRACTOR.getId())) {
			BlockState blockstate = this.getBlock().getStateForPlacement(context);
			if(blockstate != null) {
				if(this.canPlace(context, blockstate)) {
					BlockPos blockpos = context.getClickedPos();
				    for(Direction direction : Direction.Plane.HORIZONTAL) {
				    	BlockPos pos = blockpos.relative(direction);
				    	BlockState ovenBlock = context.getLevel().getBlockState(pos);
				    	if(ovenBlock.getBlock() == BlockInit.OVEN_BRICK_BLOCK.get()) {
				    		TileEntity tile = context.getLevel().getBlockEntity(OvenBrickBlock.getControllerPos(pos, ovenBlock));
				    		if(tile != null) {
				    			if(((OvenControllerTileEntity)tile).addExtractor(blockpos)) {
				    				return ExtractorBlock.setDrainFacing(context.getLevel(), blockpos, blockstate, direction);
				    			}
				    		}
				    	}
				    }
				}
			}
		}
	    return null;*/
		//temp
		return BlockInit.EXTRACTOR_CONTROLLER_BLOCK.get().getStateForPlacement(context);
		//
	}
}
