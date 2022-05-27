package com.thoriuslight.professionsmod.event;

import com.thoriuslight.professionsmod.ProfessionsMod;
import com.thoriuslight.professionsmod.network.ModPacketHandler;
import com.thoriuslight.professionsmod.network.PacketSyncProfCap;
import com.thoriuslight.professionsmod.profession.capabilities.CapabilityProfession;
import com.thoriuslight.professionsmod.profession.capabilities.Profession;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.network.PacketDistributor;

public @Mod.EventBusSubscriber(modid = ProfessionsMod.MODID, bus = Bus.FORGE)
class RespawnEvent {
	@SubscribeEvent
	public static void PlayerRespawnEvent(PlayerEvent.PlayerRespawnEvent event) {
		if(event.getPlayer().isEffectiveAi()) {
			event.getPlayer().getCapability(CapabilityProfession.PROFESSION, null).ifPresent(iProfession -> {
            	ModPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> {return (ServerPlayerEntity) event.getPlayer();}), new PacketSyncProfCap((Profession) iProfession));
			});
		}
	}
}
