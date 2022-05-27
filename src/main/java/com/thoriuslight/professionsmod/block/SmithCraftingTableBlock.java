package com.thoriuslight.professionsmod.block;

import com.thoriuslight.professionsmod.inventory.container.SmithContainerProvider;
import com.thoriuslight.professionsmod.profession.capabilities.CapabilityProfession;
import com.thoriuslight.professionsmod.profession.capabilities.IProfession;
import com.thoriuslight.professionsmod.profession.capabilities.ProfessionTypes.profession;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class SmithCraftingTableBlock extends Block{
	public SmithCraftingTableBlock(Properties properties) {
		super(properties);
	}
	
	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		IProfession iProfession = player.getCapability(CapabilityProfession.PROFESSION, null).orElseThrow(IllegalStateException::new);
		if (worldIn.isClientSide) {
			if(iProfession.getProfession() == profession.SMITH) {
				return ActionResultType.SUCCESS;
				//player.sendStatusMessage(new StringTextComponent("Skill: "+iProfession.getSkill()), false);
			}
			return ActionResultType.PASS;
		} else {
			if(iProfession.getProfession() == profession.SMITH) {
                NetworkHooks.openGui((ServerPlayerEntity) player, new SmithContainerProvider(pos), pos);
    			return ActionResultType.SUCCESS;
            }
			return ActionResultType.PASS;
		}
	}
}
