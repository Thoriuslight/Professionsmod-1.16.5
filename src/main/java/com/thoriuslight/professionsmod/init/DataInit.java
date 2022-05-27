package com.thoriuslight.professionsmod.init;

import java.util.EnumMap;

import com.thoriuslight.professionsmod.init.ItemInit.ModItemTier;
import com.thoriuslight.professionsmod.state.properties.Tool;
import com.thoriuslight.professionsmod.util.MetalItems;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class DataInit {
	private static EnumMap<Tool, EnumMap<ModItemTier, Item>> map = new EnumMap<>(Tool.class);
	private static EnumMap<ModItemTier, Item> pickaxe = new EnumMap<>(ModItemTier.class);
	private static EnumMap<ModItemTier, Item> shovel = new EnumMap<>(ModItemTier.class);
	private static EnumMap<ModItemTier, Item> axe = new EnumMap<>(ModItemTier.class);
	private static EnumMap<ModItemTier, Item> knife = new EnumMap<>(ModItemTier.class);
	private static EnumMap<ModItemTier, Item> hoe = new EnumMap<>(ModItemTier.class);
	private static EnumMap<ModItemTier, Item> hammer = new EnumMap<>(ModItemTier.class);
	private static EnumMap<ModItemTier, Item> mortar = new EnumMap<>(ModItemTier.class);
	private static EnumMap<ModItemTier, Item> ingot = new EnumMap<>(ModItemTier.class);
	private static EnumMap<ModItemTier, Item> nugget = new EnumMap<>(ModItemTier.class);
	private static EnumMap<ModItemTier, Item> rings = new EnumMap<>(ModItemTier.class);
	
	public static void initData() {
		//Casting initialization
		pickaxe.put(ModItemTier.COPPER, ItemInit.COPPER_PICKAXEHEAD.get());
		pickaxe.put(ModItemTier.SILVER, ItemInit.SILVER_PICKAXEHEAD.get());
		pickaxe.put(ModItemTier.GOLD, ItemInit.GOLDEN_PICKAXEHEAD.get());
		pickaxe.put(ModItemTier.ARSENICAL_BRONZE, ItemInit.ARSENICAL_BRONZE_PICKAXEHEAD.get());
		shovel.put(ModItemTier.COPPER, ItemInit.COPPER_SHOVELHEAD.get());
		shovel.put(ModItemTier.SILVER, ItemInit.SILVER_SHOVELHEAD.get());
		shovel.put(ModItemTier.GOLD, ItemInit.GOLDEN_SHOVELHEAD.get());
		shovel.put(ModItemTier.ARSENICAL_BRONZE, ItemInit.ARSENICAL_BRONZE_SHOVELHEAD.get());
		axe.put(ModItemTier.COPPER, ItemInit.COPPER_AXEHEAD.get());
		axe.put(ModItemTier.SILVER, ItemInit.SILVER_AXEHEAD.get());
		axe.put(ModItemTier.GOLD, ItemInit.GOLDEN_AXEHEAD.get());
		axe.put(ModItemTier.ARSENICAL_BRONZE, ItemInit.ARSENICAL_BRONZE_AXEHEAD.get());
		knife.put(ModItemTier.COPPER, ItemInit.COPPER_KNIFEHEAD.get());
		knife.put(ModItemTier.SILVER, ItemInit.SILVER_KNIFEHEAD.get());
		knife.put(ModItemTier.GOLD, ItemInit.GOLDEN_KNIFEHEAD.get());
		knife.put(ModItemTier.ARSENICAL_BRONZE, ItemInit.ARSENICAL_BRONZE_KNIFEHEAD.get());
		hoe.put(ModItemTier.COPPER, ItemInit.COPPER_HOEHEAD.get());
		hoe.put(ModItemTier.SILVER, ItemInit.SILVER_HOEHEAD.get());
		hoe.put(ModItemTier.GOLD, ItemInit.GOLDEN_HOEHEAD.get());
		hoe.put(ModItemTier.ARSENICAL_BRONZE, ItemInit.ARSENICAL_BRONZE_HAMMERHEAD.get());
		hammer.put(ModItemTier.COPPER, ItemInit.COPPER_HAMMERHEAD.get());
		hammer.put(ModItemTier.SILVER, ItemInit.SILVER_HAMMERHEAD.get());
		hammer.put(ModItemTier.GOLD, ItemInit.GOLDEN_HAMMERHEAD.get());
		hammer.put(ModItemTier.ARSENICAL_BRONZE, ItemInit.ARSENICAL_BRONZE_HAMMERHEAD.get());
		mortar.put(ModItemTier.COPPER, ItemInit.COPPER_MORTARANDPESTLE.get());
		mortar.put(ModItemTier.SILVER, ItemInit.SILVER_MORTARANDPESTLE.get());
		mortar.put(ModItemTier.GOLD, ItemInit.GOLDEN_MORTARANDPESTLE.get());
		mortar.put(ModItemTier.ARSENICAL_BRONZE, ItemInit.ARSENICAL_BRONZE_MORTARANDPESTLE.get());
		ingot.put(ModItemTier.COPPER, ItemInit.COPPER_INGOT.get());
		ingot.put(ModItemTier.SILVER, ItemInit.SILVER_INGOT.get());
		ingot.put(ModItemTier.GOLD, Items.GOLD_INGOT);
		ingot.put(ModItemTier.ARSENICAL_BRONZE, ItemInit.ARSENICAL_BRONZE_INGOT.get());
		nugget.put(ModItemTier.COPPER, ItemInit.COPPER_NUGGET.get());
		nugget.put(ModItemTier.SILVER, ItemInit.SILVER_NUGGET.get());
		nugget.put(ModItemTier.GOLD, Items.GOLD_NUGGET);
		nugget.put(ModItemTier.ARSENICAL_BRONZE, ItemInit.ARSENICAL_BRONZE_NUGGET.get());
		rings.put(ModItemTier.COPPER, ItemInit.COPPER_RINGS.get());
		rings.put(ModItemTier.SILVER, ItemInit.SILVER_RINGS.get());
		rings.put(ModItemTier.GOLD, ItemInit.GOLDEN_RINGS.get());
		rings.put(ModItemTier.ARSENICAL_BRONZE, ItemInit.ARSENICAL_BRONZE_RINGS.get());
		store(pickaxe, Tool.PICKAXE);
		store(shovel, Tool.SHOVEL);
		store(axe, Tool.AXE);
		store(knife, Tool.KNIFE);
		store(hoe, Tool.HOE);
		store(hammer, Tool.HAMMER);
		store(mortar, Tool.MORTAR);
		store(ingot, Tool.INGOT);
		store(nugget, Tool.NUGGET);
		store(rings, Tool.RINGS);
		//Material initialization
		MetalItems.InitMetals();
	}
	private static void store(EnumMap<ModItemTier, Item> enumMap, Tool type) {
		map.put(type, enumMap);
		Object[] itemList = enumMap.values().toArray();
		Object[] metalList = enumMap.keySet().toArray();
		for(int i = 0; i < enumMap.size(); ++i) {
			MetalItems.add((Item) itemList[i], type, (ModItemTier) metalList[i]);
		}
	}
	public static Item getToolHead(Tool tool, ModItemTier material) {
		return map.get(tool).get(material);
	}
	
	public static void initClientData() {
		
	}
}
