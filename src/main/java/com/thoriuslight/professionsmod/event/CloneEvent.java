package com.thoriuslight.professionsmod.event;

import com.thoriuslight.professionsmod.ProfessionsMod;
import com.thoriuslight.professionsmod.profession.capabilities.CapabilityProfession;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

public @Mod.EventBusSubscriber(modid = ProfessionsMod.MODID, bus = Bus.FORGE)
class CloneEvent {
	@SubscribeEvent
	public static void playerClonedEvent(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
        	event.getOriginal().getCapability(CapabilityProfession.PROFESSION, null).ifPresent(iprofession -> {
        		ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        		player.getCapability(CapabilityProfession.PROFESSION, null).ifPresent(prof -> {
        			prof.setProfession(iprofession.getProfession());
        			prof.setSkill(iprofession.getSkill());
        			prof.setSkillTree(iprofession.getSkillTree());
        		});
        	});
        }
	}
}
