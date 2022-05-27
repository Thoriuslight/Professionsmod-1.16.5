package com.thoriuslight.professionsmod.client;

import com.thoriuslight.professionsmod.ProfessionsMod;
import com.thoriuslight.professionsmod.init.ItemInit;
import com.thoriuslight.professionsmod.item.BrokenTool;

import net.minecraft.client.renderer.color.ItemColors;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = ProfessionsMod.MODID, bus = Bus.MOD)
public class ColourHandler {
	@SubscribeEvent
	public static void registerItemColourHandlers(final ColorHandlerEvent.Item event) {
		final ItemColors itemColours = event.getItemColors();
	    itemColours.register(BrokenTool::getItemColour, ItemInit.BROKEN_PICKAXE.get());
	    itemColours.register(BrokenTool::getItemColour, ItemInit.BROKEN_AXE.get());
	    itemColours.register(BrokenTool::getItemColour, ItemInit.BROKEN_SHOVEL.get());
	    itemColours.register(BrokenTool::getItemColour, ItemInit.BROKEN_HOE.get());
	    itemColours.register(BrokenTool::getItemColour, ItemInit.BROKEN_KNIFE.get());
	    itemColours.register(BrokenTool::getItemColour, ItemInit.BROKEN_HAMMER.get());
	    itemColours.register(BrokenTool::getItemColour, ItemInit.BROKEN_MORTAR.get());
	}
}
