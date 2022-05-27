package com.thoriuslight.professionsmod.util;

import java.util.Map;

import com.google.common.collect.Maps;
import com.thoriuslight.professionsmod.init.BlockInit;
import com.thoriuslight.professionsmod.init.ItemInit;
import com.thoriuslight.professionsmod.init.ItemInit.ModItemTier;
import com.thoriuslight.professionsmod.state.properties.Tool;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class MetalItems {
	private static Map<Item, MetalItem> map = Maps.newLinkedHashMap();
	
	public static void InitMetals(){
		map.put(BlockInit.COPPER_ORE.get().asItem(), new MetalItem(Tool.ORE, ModItemTier.COPPER));
		map.put(BlockInit.SILVER_ORE.get().asItem(), new MetalItem(Tool.ORE, ModItemTier.SILVER));
		map.put(ItemInit.GOLD_ORE_ITEM.get(), new MetalItem(Tool.ORE, ModItemTier.GOLD));
		map.put(ItemInit.COPPER_CHUNK.get(), new MetalItem(Tool.CHUNK, ModItemTier.COPPER));
		map.put(ItemInit.SILVER_CHUNK.get(), new MetalItem(Tool.CHUNK, ModItemTier.SILVER));
		map.put(ItemInit.GOLDEN_CHUNK.get(), new MetalItem(Tool.CHUNK, ModItemTier.GOLD));
		map.put(ItemInit.COPPER_ORE_DUST.get(), new MetalItem(Tool.ORE_DUST, ModItemTier.COPPER));
		map.put(ItemInit.SILVER_ORE_DUST.get(), new MetalItem(Tool.ORE_DUST, ModItemTier.SILVER));
		map.put(ItemInit.GOLD_ORE_DUST.get(), new MetalItem(Tool.ORE_DUST, ModItemTier.GOLD));
		map.put(ItemInit.ROASTED_COPPER_ORE.get(), new MetalItem(Tool.ROASTED_ORE, ModItemTier.COPPER));
		map.put(ItemInit.ROASTED_SILVER_ORE.get(), new MetalItem(Tool.ROASTED_ORE, ModItemTier.SILVER));
		map.put(ItemInit.ROASTED_GOLD_ORE.get(), new MetalItem(Tool.ROASTED_ORE, ModItemTier.GOLD));
		map.put(ItemInit.COPPER_NUGGET.get(), new MetalItem(Tool.NUGGET, ModItemTier.COPPER));
		map.put(ItemInit.SILVER_NUGGET.get(), new MetalItem(Tool.NUGGET, ModItemTier.SILVER));
		map.put(Items.GOLD_NUGGET, new MetalItem(Tool.NUGGET, ModItemTier.GOLD));
		map.put(ItemInit.ARSENICAL_BRONZE_NUGGET.get(), new MetalItem(Tool.NUGGET, ModItemTier.ARSENICAL_BRONZE));
	}
	public static void add(Item item, Tool type, ModItemTier metal) {
		map.put(item, new MetalItem(type, metal));
	}
	public static boolean isItemMeltable(Item item) {
		return map.containsKey(item);
	}
	public static MetalItem getMetal(Item item) {
		return map.get(item);
	}
}
