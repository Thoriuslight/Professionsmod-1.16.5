package com.thoriuslight.professionsmod.init;

import com.thoriuslight.professionsmod.ProfessionsMod;
import com.thoriuslight.professionsmod.event.loot.DungeonModifier;
import com.thoriuslight.professionsmod.event.loot.MobDropModifier;

import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = ProfessionsMod.MODID, bus = Bus.MOD)
public class LootModifierSerializerInit {
	@SubscribeEvent
	public static void registerLootModifierSerializers(final RegistryEvent.Register< GlobalLootModifierSerializer<?>> event) {
		IForgeRegistry<GlobalLootModifierSerializer<?>> registry = event.getRegistry();
		register(registry, new DungeonModifier.Serializer(), "arsenic_gen");
		register(registry, new MobDropModifier.Serializer(), "creeper_arsenic");
	}
	private static void register(IForgeRegistry<GlobalLootModifierSerializer<?>> registry, GlobalLootModifierSerializer<?> serializer, String name) {
		registry.register(serializer.setRegistryName(ProfessionsMod.MODID, name));
	}
}
