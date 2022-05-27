package com.thoriuslight.professionsmod.event;

import javax.annotation.Nonnull;

import com.thoriuslight.professionsmod.ProfessionsMod;
import com.thoriuslight.professionsmod.network.ModPacketHandler;
import com.thoriuslight.professionsmod.network.PacketSyncProfCap;
import com.thoriuslight.professionsmod.profession.capabilities.CapabilityProfession;
import com.thoriuslight.professionsmod.profession.capabilities.IProfession;
import com.thoriuslight.professionsmod.profession.capabilities.Profession;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.util.NonNullConsumer;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.network.PacketDistributor;

public @Mod.EventBusSubscriber(modid = ProfessionsMod.MODID, bus = Bus.FORGE)
class PlayerLoginEvent {
	@SubscribeEvent
	public static void loginEvent(PlayerLoggedInEvent event) {
		if(event.getPlayer().isEffectiveAi()) {
		event.getPlayer().getCapability(CapabilityProfession.PROFESSION, null).ifPresent(new NonNullConsumer<IProfession>() {
			@Override
            public void accept(@Nonnull IProfession iProfession) {
            	ModPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> {return (ServerPlayerEntity) event.getPlayer();}), new PacketSyncProfCap((Profession) iProfession));
            }
        });
		}
	}
}
