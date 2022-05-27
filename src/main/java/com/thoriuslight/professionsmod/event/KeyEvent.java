package com.thoriuslight.professionsmod.event;

import com.thoriuslight.professionsmod.ProfessionsMod;
import com.thoriuslight.professionsmod.client.gui.ProfessionsMenuScreen;
import com.thoriuslight.professionsmod.client.gui.ProfessionScreen;
import com.thoriuslight.professionsmod.init.KeyBindInit;
import com.thoriuslight.professionsmod.profession.capabilities.CapabilityProfession;
import com.thoriuslight.professionsmod.profession.capabilities.IProfession;
import com.thoriuslight.professionsmod.profession.capabilities.ProfessionTypes.profession;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

public @Mod.EventBusSubscriber(modid = ProfessionsMod.MODID, bus = Bus.FORGE, value = Dist.CLIENT)
class KeyEvent {
	@SubscribeEvent
	public static void KeyInputEvent(KeyInputEvent event) {
		if(KeyBindInit.PROMENU.consumeClick()) {
			IProfession iProfession = Minecraft.getInstance().player.getCapability(CapabilityProfession.PROFESSION, null).orElseThrow(IllegalStateException::new);
			profession prof = iProfession.getProfession();
			if(prof  != profession.NOTHING) {
				Minecraft.getInstance().setScreen(new ProfessionScreen(Minecraft.getInstance(), prof));
			} 
			else {
				Minecraft.getInstance().setScreen(new ProfessionsMenuScreen());
			}
		}
	}
}
