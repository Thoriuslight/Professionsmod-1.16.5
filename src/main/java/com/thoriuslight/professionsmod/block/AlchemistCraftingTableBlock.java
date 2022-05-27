package com.thoriuslight.professionsmod.block;

import com.thoriuslight.professionsmod.inventory.container.AlchemyContainerProvider;
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

public class AlchemistCraftingTableBlock extends Block{
	public AlchemistCraftingTableBlock(Properties properties) {
		super(properties);
	}
	
	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		IProfession iProfession = player.getCapability(CapabilityProfession.PROFESSION, null).orElseThrow(IllegalStateException::new);
		if (worldIn.isClientSide) {
			if(iProfession.getProfession() == profession.ALCHEMIST) {
				return ActionResultType.SUCCESS;
			}
			return ActionResultType.PASS;
		} else {
			if(iProfession.getProfession() == profession.ALCHEMIST) {
                NetworkHooks.openGui((ServerPlayerEntity) player, new AlchemyContainerProvider(pos), pos);
    			return ActionResultType.SUCCESS;
            }
			return ActionResultType.PASS;
		}
	}
}
