package com.thoriuslight.professionsmod.init;

import com.thoriuslight.professionsmod.ProfessionsMod;
import com.thoriuslight.professionsmod.world.storage.loot.conditions.Dungeon;
import com.thoriuslight.professionsmod.world.storage.loot.conditions.EntityKilled;

import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class ModLootConditionTypes {
	public static final LootConditionType BLOCK_INVENTORY_LOOT = register("dungeon", new Dungeon.Serializer());
	public static final LootConditionType ENTITY_KILLED = register("entity_killed", new EntityKilled.Serializer());
	public static void init() {}

	private static LootConditionType register(final String name, final ILootSerializer<? extends ILootCondition> serializer) {
		return Registry.register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation(ProfessionsMod.MODID, name), new LootConditionType(serializer));
	}
}
