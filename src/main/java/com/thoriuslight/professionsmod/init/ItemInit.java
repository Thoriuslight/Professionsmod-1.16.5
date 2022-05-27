package com.thoriuslight.professionsmod.init;

import java.util.function.Supplier;

import com.thoriuslight.professionsmod.ProfessionsMod;
import com.thoriuslight.professionsmod.ProfessionsMod.AlchemyItemGroup;
import com.thoriuslight.professionsmod.ProfessionsMod.BlacksmithItemGroup;
import com.thoriuslight.professionsmod.ProfessionsMod.ProfCommonItemGroup;
import com.thoriuslight.professionsmod.item.Antidote;
import com.thoriuslight.professionsmod.item.BlowPipeItem;
import com.thoriuslight.professionsmod.item.BowlItem;
import com.thoriuslight.professionsmod.item.BreathGum;
import com.thoriuslight.professionsmod.item.BrokenTool;
import com.thoriuslight.professionsmod.item.ButtedChainArmorItem;
import com.thoriuslight.professionsmod.item.ColdWorkedAxe;
import com.thoriuslight.professionsmod.item.ColdWorkedHoe;
import com.thoriuslight.professionsmod.item.ColdWorkedKnife;
import com.thoriuslight.professionsmod.item.ColdWorkedPickaxe;
import com.thoriuslight.professionsmod.item.ColdWorkedShovel;
import com.thoriuslight.professionsmod.item.EnergySnack;
import com.thoriuslight.professionsmod.item.HammerItem;
import com.thoriuslight.professionsmod.item.HealingSalve;
import com.thoriuslight.professionsmod.item.MetalHammerItem;
import com.thoriuslight.professionsmod.item.MetalMortar;
import com.thoriuslight.professionsmod.item.MoltenMetalItem;
import com.thoriuslight.professionsmod.item.Mortar;
import com.thoriuslight.professionsmod.item.Sourdough;
import com.thoriuslight.professionsmod.item.Stimulant;
import com.thoriuslight.professionsmod.item.WeakMetalChainItem;
import com.thoriuslight.professionsmod.item.WoodenHammerItem;
import com.thoriuslight.professionsmod.state.properties.Tool;

import net.minecraft.block.Blocks;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BucketItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.item.ItemTier;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemInit {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ProfessionsMod.MODID);
	public static final DeferredRegister<Item> VANILLA_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "minecraft");
	//Smithing Tools
	public static final RegistryObject<Item> STONEHAMMER_ITEM = ITEMS.register("stonehammer_item", () -> new HammerItem(new Item.Properties().tab(BlacksmithItemGroup.instance).defaultDurability(131), ItemTier.STONE, 10));
	public static final RegistryObject<Item> UNFIRED_BLOWPIPE = ITEMS.register("unfired_blowpipe", () -> new Item(new Item.Properties().stacksTo(1).tab(BlacksmithItemGroup.instance)));
	public static final RegistryObject<Item> BLOWPIPE = ITEMS.register("blowpipe", () -> new BlowPipeItem(new Item.Properties().stacksTo(1).tab(BlacksmithItemGroup.instance)));
	//General Items
	public static final RegistryObject<Item> CLAY_MIXTURE = ITEMS.register("clay_mixture", () -> new Item(new Item.Properties().tab(ProfCommonItemGroup.instance)));
	public static final RegistryObject<Item> INSULATION_BRICK = ITEMS.register("insulation_brick", () -> new Item(new Item.Properties().tab(ProfCommonItemGroup.instance)));
	public static final RegistryObject<Item> SIMPLE_MECHANISM = ITEMS.register("simple_mechanism", () -> new Item(new Item.Properties().tab(ProfCommonItemGroup.instance)));
	public static final RegistryObject<Item> BOWL = VANILLA_ITEMS.register("bowl", () -> new BowlItem((new Item.Properties()).tab(ItemGroup.TAB_MATERIALS)));
	public static final RegistryObject<Item> WATER_BOWL = ITEMS.register("water_bowl", () -> new Item(new Item.Properties().craftRemainder(Items.BOWL).tab(ProfCommonItemGroup.instance)));
	public static final RegistryObject<Item> WOODEN_HAMMER = ITEMS.register("woodenhammer_item", () -> new WoodenHammerItem(new Item.Properties().stacksTo(1).tab(ProfCommonItemGroup.instance)));
	//General Tools
	public static final RegistryObject<Item> COPPER_PICKAXE = ITEMS.register("copper_pickaxe", () -> new ColdWorkedPickaxe(ModItemTier.COPPER, 1, -2.8f, new Item.Properties().tab(BlacksmithItemGroup.instance)));
	public static final RegistryObject<Item> COPPER_SHOVEL = ITEMS.register("copper_shovel", () -> new ColdWorkedShovel(ModItemTier.COPPER, 1.5f, -3.0f, new Item.Properties().tab(BlacksmithItemGroup.instance)));
	public static final RegistryObject<Item> COPPER_AXE = ITEMS.register("copper_axe", () -> new ColdWorkedAxe(ModItemTier.COPPER, 6.5f, -3.15f, new Item.Properties().tab(BlacksmithItemGroup.instance)));
	public static final RegistryObject<Item> COPPER_KNIFE = ITEMS.register("copper_knife", () -> new ColdWorkedKnife(ModItemTier.COPPER, 1.5f, -2.00f, new Item.Properties().tab(BlacksmithItemGroup.instance)));
	public static final RegistryObject<Item> COPPER_HOE = ITEMS.register("copper_hoe", () -> new ColdWorkedHoe(ModItemTier.COPPER, -1.50f, new Item.Properties().tab(BlacksmithItemGroup.instance)));
	public static final RegistryObject<Item> COPPER_BUTTED_CHAIN_HELMET = ITEMS.register("copper_butted_chain_helmet", () -> new ButtedChainArmorItem(ModArmorMaterial.COPPER, EquipmentSlotType.HEAD, new Item.Properties().tab(BlacksmithItemGroup.instance)));
	public static final RegistryObject<Item> COPPER_BUTTED_CHAIN_CHESTPLATE = ITEMS.register("copper_butted_chain_chestplate", () -> new ButtedChainArmorItem(ModArmorMaterial.COPPER, EquipmentSlotType.CHEST, new Item.Properties().tab(BlacksmithItemGroup.instance)));
	public static final RegistryObject<Item> COPPER_BUTTED_CHAIN_LEGGINGS = ITEMS.register("copper_butted_chain_leggings", () -> new ButtedChainArmorItem(ModArmorMaterial.COPPER, EquipmentSlotType.LEGS, new Item.Properties().tab(BlacksmithItemGroup.instance)));
	public static final RegistryObject<Item> COPPER_BUTTED_CHAIN_BOOTS = ITEMS.register("copper_butted_chain_boots", () -> new ButtedChainArmorItem(ModArmorMaterial.COPPER, EquipmentSlotType.FEET, new Item.Properties().tab(BlacksmithItemGroup.instance)));
	public static final RegistryObject<Item> COPPER_HAMMER = ITEMS.register("copper_hammer", () -> new MetalHammerItem(new Item.Properties().tab(BlacksmithItemGroup.instance).defaultDurability(400), ModItemTier.COPPER, 13));
	public static final RegistryObject<Item> COPPER_MORTARANDPESTLE = ITEMS.register("copper_mortarandpestle", () -> new MetalMortar(new Item.Properties().stacksTo(1).tab(AlchemyItemGroup.instance).durability(400), 50, 2, ModItemTier.COPPER));

	public static final RegistryObject<Item> SILVER_PICKAXE = ITEMS.register("silver_pickaxe", () -> new ColdWorkedPickaxe(ModItemTier.SILVER, 1, -2.8f, new Item.Properties().tab(BlacksmithItemGroup.instance)));
	public static final RegistryObject<Item> SILVER_SHOVEL = ITEMS.register("silver_shovel", () -> new ColdWorkedShovel(ModItemTier.SILVER, 1.5f, -3.0f, new Item.Properties().tab(BlacksmithItemGroup.instance)));
	public static final RegistryObject<Item> SILVER_AXE = ITEMS.register("silver_axe", () -> new ColdWorkedAxe(ModItemTier.SILVER, 7.0f, -3.2f, new Item.Properties().tab(BlacksmithItemGroup.instance)));
	public static final RegistryObject<Item> SILVER_KNIFE = ITEMS.register("silver_knife", () -> new ColdWorkedKnife(ModItemTier.SILVER, 1.5f, -2.00f, new Item.Properties().tab(BlacksmithItemGroup.instance)));
	public static final RegistryObject<Item> SILVER_HOE = ITEMS.register("silver_hoe", () -> new ColdWorkedHoe(ModItemTier.SILVER, -2.00f, new Item.Properties().tab(BlacksmithItemGroup.instance)));
	public static final RegistryObject<Item> SILVER_BUTTED_CHAIN_HELMET = ITEMS.register("silver_butted_chain_helmet", () -> new ButtedChainArmorItem(ModArmorMaterial.SILVER, EquipmentSlotType.HEAD, new Item.Properties().tab(BlacksmithItemGroup.instance)));
	public static final RegistryObject<Item> SILVER_BUTTED_CHAIN_CHESTPLATE = ITEMS.register("silver_butted_chain_chestplate", () -> new ButtedChainArmorItem(ModArmorMaterial.SILVER, EquipmentSlotType.CHEST, new Item.Properties().tab(BlacksmithItemGroup.instance)));
	public static final RegistryObject<Item> SILVER_BUTTED_CHAIN_LEGGINGS = ITEMS.register("silver_butted_chain_leggings", () -> new ButtedChainArmorItem(ModArmorMaterial.SILVER, EquipmentSlotType.LEGS, new Item.Properties().tab(BlacksmithItemGroup.instance)));
	public static final RegistryObject<Item> SILVER_BUTTED_CHAIN_BOOTS = ITEMS.register("silver_butted_chain_boots", () -> new ButtedChainArmorItem(ModArmorMaterial.SILVER, EquipmentSlotType.FEET, new Item.Properties().tab(BlacksmithItemGroup.instance)));
	public static final RegistryObject<Item> SILVER_HAMMER = ITEMS.register("silver_hammer", () -> new MetalHammerItem(new Item.Properties().tab(BlacksmithItemGroup.instance).defaultDurability(300), ModItemTier.SILVER, 10));
	public static final RegistryObject<Item> SILVER_MORTARANDPESTLE = ITEMS.register("silver_mortarandpestle", () -> new MetalMortar(new Item.Properties().stacksTo(1).tab(AlchemyItemGroup.instance).durability(300), 60, 2, ModItemTier.SILVER));

	public static final RegistryObject<Item> GOLDEN_PICKAXE = ITEMS.register("golden_pickaxe", () -> new ColdWorkedPickaxe(ModItemTier.GOLD, 1, -2.8f, new Item.Properties().tab(BlacksmithItemGroup.instance)));
	public static final RegistryObject<Item> GOLDEN_SHOVEL = ITEMS.register("golden_shovel", () -> new ColdWorkedShovel(ModItemTier.GOLD, 1.5f, -3.0f, new Item.Properties().tab(BlacksmithItemGroup.instance)));
	public static final RegistryObject<Item> GOLDEN_AXE = ITEMS.register("golden_axe", () -> new ColdWorkedAxe(ModItemTier.GOLD, 6.0f, -3.2f, new Item.Properties().tab(BlacksmithItemGroup.instance)));
	public static final RegistryObject<Item> GOLDEN_KNIFE = ITEMS.register("golden_knife", () -> new ColdWorkedKnife(ModItemTier.GOLD, 1.5f, -2.00f, new Item.Properties().tab(BlacksmithItemGroup.instance)));
	public static final RegistryObject<Item> GOLDEN_HOE = ITEMS.register("golden_hoe", () -> new ColdWorkedHoe(ModItemTier.GOLD, -2.50f, new Item.Properties().tab(BlacksmithItemGroup.instance)));
	public static final RegistryObject<Item> GOLDEN_BUTTED_CHAIN_HELMET = ITEMS.register("golden_butted_chain_helmet", () -> new ButtedChainArmorItem(ModArmorMaterial.GOLD, EquipmentSlotType.HEAD, new Item.Properties().tab(BlacksmithItemGroup.instance)));
	public static final RegistryObject<Item> GOLDEN_BUTTED_CHAIN_CHESTPLATE = ITEMS.register("golden_butted_chain_chestplate", () -> new ButtedChainArmorItem(ModArmorMaterial.GOLD, EquipmentSlotType.CHEST, new Item.Properties().tab(BlacksmithItemGroup.instance)));
	public static final RegistryObject<Item> GOLDEN_BUTTED_CHAIN_LEGGINGS = ITEMS.register("golden_butted_chain_leggings", () -> new ButtedChainArmorItem(ModArmorMaterial.GOLD, EquipmentSlotType.LEGS, new Item.Properties().tab(BlacksmithItemGroup.instance)));
	public static final RegistryObject<Item> GOLDEN_BUTTED_CHAIN_BOOTS = ITEMS.register("golden_butted_chain_boots", () -> new ButtedChainArmorItem(ModArmorMaterial.GOLD, EquipmentSlotType.FEET, new Item.Properties().tab(BlacksmithItemGroup.instance)));
	public static final RegistryObject<Item> GOLDEN_HAMMER = ITEMS.register("golden_hammer", () -> new MetalHammerItem(new Item.Properties().tab(BlacksmithItemGroup.instance).defaultDurability(200), ModItemTier.GOLD, 8));
	public static final RegistryObject<Item> GOLDEN_MORTARANDPESTLE = ITEMS.register("golden_mortarandpestle", () -> new MetalMortar(new Item.Properties().stacksTo(1).tab(AlchemyItemGroup.instance).durability(200), 70, 2, ModItemTier.GOLD));
	
	public static final RegistryObject<Item> ARSENICAL_BRONZE_PICKAXE = ITEMS.register("arsenical_bronze_pickaxe", () -> new ColdWorkedPickaxe(ModItemTier.ARSENICAL_BRONZE, 1, -2.8f, new Item.Properties().tab(BlacksmithItemGroup.instance)));
	public static final RegistryObject<Item> ARSENICAL_BRONZE_SHOVEL = ITEMS.register("arsenical_bronze_shovel", () -> new ColdWorkedShovel(ModItemTier.ARSENICAL_BRONZE, 1.5f, -3.0f, new Item.Properties().tab(BlacksmithItemGroup.instance)));
	public static final RegistryObject<Item> ARSENICAL_BRONZE_AXE = ITEMS.register("arsenical_bronze_axe", () -> new ColdWorkedAxe(ModItemTier.ARSENICAL_BRONZE, 6.0f, -3.10f, new Item.Properties().tab(BlacksmithItemGroup.instance)));
	public static final RegistryObject<Item> ARSENICAL_BRONZE_KNIFE = ITEMS.register("arsenical_bronze_knife", () -> new ColdWorkedKnife(ModItemTier.ARSENICAL_BRONZE, 1.5f, -2.00f, new Item.Properties().tab(BlacksmithItemGroup.instance)));
	public static final RegistryObject<Item> ARSENICAL_BRONZE_HOE = ITEMS.register("arsenical_bronze_hoe", () -> new ColdWorkedHoe(ModItemTier.ARSENICAL_BRONZE, -1.50f, new Item.Properties().tab(BlacksmithItemGroup.instance)));
	public static final RegistryObject<Item> ARSENICAL_BRONZE_BUTTED_CHAIN_HELMET = ITEMS.register("arsenical_bronze_butted_chain_helmet", () -> new ButtedChainArmorItem(ModArmorMaterial.ARSENICAL_BRONZE, EquipmentSlotType.HEAD, new Item.Properties().tab(BlacksmithItemGroup.instance)));
	public static final RegistryObject<Item> ARSENICAL_BRONZE_BUTTED_CHAIN_CHESTPLATE = ITEMS.register("arsenical_bronze_butted_chain_chestplate", () -> new ButtedChainArmorItem(ModArmorMaterial.ARSENICAL_BRONZE, EquipmentSlotType.CHEST, new Item.Properties().tab(BlacksmithItemGroup.instance)));
	public static final RegistryObject<Item> ARSENICAL_BRONZE_BUTTED_CHAIN_LEGGINGS = ITEMS.register("arsenical_bronze_butted_chain_leggings", () -> new ButtedChainArmorItem(ModArmorMaterial.ARSENICAL_BRONZE, EquipmentSlotType.LEGS, new Item.Properties().tab(BlacksmithItemGroup.instance)));
	public static final RegistryObject<Item> ARSENICAL_BRONZE_BUTTED_CHAIN_BOOTS = ITEMS.register("arsenical_bronze_butted_chain_boots", () -> new ButtedChainArmorItem(ModArmorMaterial.ARSENICAL_BRONZE, EquipmentSlotType.FEET, new Item.Properties().tab(BlacksmithItemGroup.instance)));
	public static final RegistryObject<Item> ARSENICAL_BRONZE_HAMMER = ITEMS.register("arsenical_bronze_hammer", () -> new MetalHammerItem(new Item.Properties().tab(BlacksmithItemGroup.instance).defaultDurability(500), ModItemTier.ARSENICAL_BRONZE, 15));
	public static final RegistryObject<Item> ARSENICAL_BRONZE_MORTARANDPESTLE = ITEMS.register("arsenical_bronze_mortarandpestle", () -> new MetalMortar(new Item.Properties().stacksTo(1).tab(AlchemyItemGroup.instance).durability(500), 40, 2, ModItemTier.ARSENICAL_BRONZE));
	//Copper
	public static final RegistryObject<Item> COPPER_CHUNK = ITEMS.register("copper_chunk", () -> new Item(new Item.Properties().tab(BlacksmithItemGroup.instance).stacksTo(16)));
	public static final RegistryObject<Item> COPPER_ORE_DUST = ITEMS.register("copper_ore_dust", () -> new Item(new Item.Properties().tab(BlacksmithItemGroup.instance).stacksTo(32)));
	public static final RegistryObject<Item> ROASTED_COPPER_ORE = ITEMS.register("roasted_copper_ore", () -> new Item(new Item.Properties().tab(BlacksmithItemGroup.instance).stacksTo(24)));
	public static final RegistryObject<Item> COPPER_NUGGET = ITEMS.register("copper_nugget", () -> new Item(new Item.Properties().tab(BlacksmithItemGroup.instance)));
	public static final RegistryObject<Item> COPPER_INGOT = ITEMS.register("copper_ingot", () -> new Item(new Item.Properties().tab(BlacksmithItemGroup.instance)));
	public static final RegistryObject<Item> BUCKETFILLED_COPPER = ITEMS.register("bucketfilled_copper", () -> new Item(new Item.Properties().tab(BlacksmithItemGroup.instance).stacksTo(1)));
	public static final RegistryObject<Item> BUCKETFILLED_MOLTENCOPPER = ITEMS.register("bucketfilled_moltencopper", () -> new MoltenMetalItem(new Item.Properties().tab(BlacksmithItemGroup.instance).stacksTo(1), ModItemTier.COPPER));
	public static final RegistryObject<Item> COPPER_PICKAXEHEAD = ITEMS.register("copper_pickaxehead", () -> new Item(new Item.Properties().tab(BlacksmithItemGroup.instance).stacksTo(1)));
	public static final RegistryObject<Item> COPPER_SHOVELHEAD = ITEMS.register("copper_shovelhead", () -> new Item(new Item.Properties().tab(BlacksmithItemGroup.instance).stacksTo(1)));
	public static final RegistryObject<Item> COPPER_AXEHEAD = ITEMS.register("copper_axehead", () -> new Item(new Item.Properties().tab(BlacksmithItemGroup.instance).stacksTo(1)));
	public static final RegistryObject<Item> COPPER_KNIFEHEAD = ITEMS.register("copper_knifehead", () -> new Item(new Item.Properties().tab(BlacksmithItemGroup.instance).stacksTo(1)));
	public static final RegistryObject<Item> COPPER_HOEHEAD = ITEMS.register("copper_hoehead", () -> new Item(new Item.Properties().tab(BlacksmithItemGroup.instance).stacksTo(1)));
	public static final RegistryObject<Item> COPPER_HAMMERHEAD = ITEMS.register("copper_hammerhead", () -> new Item(new Item.Properties().tab(BlacksmithItemGroup.instance).stacksTo(1)));
	public static final RegistryObject<Item> COPPER_CHAIN = ITEMS.register("copper_chain", () -> new WeakMetalChainItem(new Item.Properties().tab(BlacksmithItemGroup.instance).stacksTo(24)));
	public static final RegistryObject<Item> COPPER_RINGS = ITEMS.register("copper_rings", () -> new Item(new Item.Properties().tab(BlacksmithItemGroup.instance).stacksTo(24)));
	//Silver
	public static final RegistryObject<Item> SILVER_CHUNK = ITEMS.register("silver_chunk", () -> new Item(new Item.Properties().tab(BlacksmithItemGroup.instance).stacksTo(16)));
	public static final RegistryObject<Item> SILVER_ORE_DUST = ITEMS.register("silver_ore_dust", () -> new Item(new Item.Properties().tab(BlacksmithItemGroup.instance).stacksTo(32)));
	public static final RegistryObject<Item> ROASTED_SILVER_ORE = ITEMS.register("roasted_silver_ore", () -> new Item(new Item.Properties().tab(BlacksmithItemGroup.instance).stacksTo(24)));
	public static final RegistryObject<Item> SILVER_NUGGET = ITEMS.register("silver_nugget", () -> new Item(new Item.Properties().tab(BlacksmithItemGroup.instance)));
	public static final RegistryObject<Item> SILVER_INGOT = ITEMS.register("silver_ingot", () -> new Item(new Item.Properties().tab(BlacksmithItemGroup.instance)));
	public static final RegistryObject<Item> BUCKETFILLED_SILVER = ITEMS.register("bucketfilled_silver", () -> new Item(new Item.Properties().tab(BlacksmithItemGroup.instance).stacksTo(1)));
	public static final RegistryObject<Item> BUCKETFILLED_MOLTENSILVER = ITEMS.register("bucketfilled_moltensilver", () -> new MoltenMetalItem(new Item.Properties().tab(BlacksmithItemGroup.instance).stacksTo(1), ModItemTier.SILVER));
	public static final RegistryObject<Item> SILVER_PICKAXEHEAD = ITEMS.register("silver_pickaxehead", () -> new Item(new Item.Properties().tab(BlacksmithItemGroup.instance).stacksTo(1)));
	public static final RegistryObject<Item> SILVER_SHOVELHEAD = ITEMS.register("silver_shovelhead", () -> new Item(new Item.Properties().tab(BlacksmithItemGroup.instance).stacksTo(1)));
	public static final RegistryObject<Item> SILVER_AXEHEAD = ITEMS.register("silver_axehead", () -> new Item(new Item.Properties().tab(BlacksmithItemGroup.instance).stacksTo(1)));
	public static final RegistryObject<Item> SILVER_KNIFEHEAD = ITEMS.register("silver_knifehead", () -> new Item(new Item.Properties().tab(BlacksmithItemGroup.instance).stacksTo(1)));
	public static final RegistryObject<Item> SILVER_HOEHEAD = ITEMS.register("silver_hoehead", () -> new Item(new Item.Properties().tab(BlacksmithItemGroup.instance).stacksTo(1)));
	public static final RegistryObject<Item> SILVER_HAMMERHEAD = ITEMS.register("silver_hammerhead", () -> new Item(new Item.Properties().tab(BlacksmithItemGroup.instance).stacksTo(1)));
	public static final RegistryObject<Item> SILVER_CHAIN = ITEMS.register("silver_chain", () -> new WeakMetalChainItem(new Item.Properties().tab(BlacksmithItemGroup.instance).stacksTo(24)));
	public static final RegistryObject<Item> SILVER_RINGS = ITEMS.register("silver_rings", () -> new Item(new Item.Properties().tab(BlacksmithItemGroup.instance).stacksTo(24)));
	//Gold
	public static final RegistryObject<Item> GOLD_ORE_ITEM = VANILLA_ITEMS.register("gold_ore", () -> new BlockItem(Blocks.GOLD_ORE, new Item.Properties().stacksTo(1).tab(ItemGroup.TAB_BUILDING_BLOCKS)));
	public static final RegistryObject<Item> GOLDEN_CHUNK = ITEMS.register("golden_chunk", () -> new Item(new Item.Properties().tab(BlacksmithItemGroup.instance).stacksTo(16)));
	public static final RegistryObject<Item> GOLD_ORE_DUST = ITEMS.register("gold_ore_dust", () -> new Item(new Item.Properties().tab(BlacksmithItemGroup.instance).stacksTo(36)));
	public static final RegistryObject<Item> ROASTED_GOLD_ORE = ITEMS.register("roasted_gold_ore", () -> new Item(new Item.Properties().tab(BlacksmithItemGroup.instance).stacksTo(24)));
	public static final RegistryObject<Item> BUCKETFILLED_GOLD = ITEMS.register("bucketfilled_gold", () -> new Item(new Item.Properties().tab(BlacksmithItemGroup.instance).stacksTo(1)));
	public static final RegistryObject<Item> BUCKETFILLED_MOLTENGOLD = ITEMS.register("bucketfilled_moltengold", () -> new MoltenMetalItem(new Item.Properties().tab(BlacksmithItemGroup.instance).stacksTo(1), ModItemTier.GOLD));
	public static final RegistryObject<Item> GOLDEN_PICKAXEHEAD = ITEMS.register("golden_pickaxehead", () -> new Item(new Item.Properties().tab(BlacksmithItemGroup.instance).stacksTo(1)));
	public static final RegistryObject<Item> GOLDEN_SHOVELHEAD = ITEMS.register("golden_shovelhead", () -> new Item(new Item.Properties().tab(BlacksmithItemGroup.instance).stacksTo(1)));
	public static final RegistryObject<Item> GOLDEN_AXEHEAD = ITEMS.register("golden_axehead", () -> new Item(new Item.Properties().tab(BlacksmithItemGroup.instance).stacksTo(1)));
	public static final RegistryObject<Item> GOLDEN_KNIFEHEAD = ITEMS.register("golden_knifehead", () -> new Item(new Item.Properties().tab(BlacksmithItemGroup.instance).stacksTo(1)));
	public static final RegistryObject<Item> GOLDEN_HOEHEAD = ITEMS.register("golden_hoehead", () -> new Item(new Item.Properties().tab(BlacksmithItemGroup.instance).stacksTo(1)));
	public static final RegistryObject<Item> GOLDEN_HAMMERHEAD = ITEMS.register("golden_hammerhead", () -> new Item(new Item.Properties().tab(BlacksmithItemGroup.instance).stacksTo(1)));
	public static final RegistryObject<Item> GOLDEN_CHAIN = ITEMS.register("golden_chain", () -> new WeakMetalChainItem(new Item.Properties().tab(BlacksmithItemGroup.instance).stacksTo(24)));
	public static final RegistryObject<Item> GOLDEN_RINGS = ITEMS.register("golden_rings", () -> new Item(new Item.Properties().tab(BlacksmithItemGroup.instance).stacksTo(24)));
	//Arsenic bronze
	public static final RegistryObject<Item> ARSENIC = ITEMS.register("arsenic", () -> new Item(new Item.Properties().tab(AlchemyItemGroup.instance)));
	public static final RegistryObject<Item> ARSENICAL_BRONZE_NUGGET = ITEMS.register("arsenical_bronze_nugget", () -> new Item(new Item.Properties().tab(BlacksmithItemGroup.instance)));
	public static final RegistryObject<Item> ARSENICAL_BRONZE_INGOT = ITEMS.register("arsenical_bronze_ingot", () -> new Item(new Item.Properties().tab(BlacksmithItemGroup.instance)));
	public static final RegistryObject<Item> ARSENICAL_BRONZE_PICKAXEHEAD = ITEMS.register("arsenical_bronze_pickaxehead", () -> new Item(new Item.Properties().tab(BlacksmithItemGroup.instance).stacksTo(1)));
	public static final RegistryObject<Item> ARSENICAL_BRONZE_SHOVELHEAD = ITEMS.register("arsenical_bronze_shovelhead", () -> new Item(new Item.Properties().tab(BlacksmithItemGroup.instance).stacksTo(1)));
	public static final RegistryObject<Item> ARSENICAL_BRONZE_AXEHEAD = ITEMS.register("arsenical_bronze_axehead", () -> new Item(new Item.Properties().tab(BlacksmithItemGroup.instance).stacksTo(1)));
	public static final RegistryObject<Item> ARSENICAL_BRONZE_KNIFEHEAD = ITEMS.register("arsenical_bronze_knifehead", () -> new Item(new Item.Properties().tab(BlacksmithItemGroup.instance).stacksTo(1)));
	public static final RegistryObject<Item> ARSENICAL_BRONZE_HOEHEAD = ITEMS.register("arsenical_bronze_hoehead", () -> new Item(new Item.Properties().tab(BlacksmithItemGroup.instance).stacksTo(1)));
	public static final RegistryObject<Item> ARSENICAL_BRONZE_HAMMERHEAD = ITEMS.register("arsenical_bronze_hammerhead", () -> new Item(new Item.Properties().tab(BlacksmithItemGroup.instance).stacksTo(1)));
	public static final RegistryObject<Item> ARSENICAL_BRONZE_CHAIN = ITEMS.register("arsenical_bronze_chain", () -> new WeakMetalChainItem(new Item.Properties().tab(BlacksmithItemGroup.instance).stacksTo(24)));
	public static final RegistryObject<Item> ARSENICAL_BRONZE_RINGS = ITEMS.register("arsenical_bronze_rings", () -> new Item(new Item.Properties().tab(BlacksmithItemGroup.instance).stacksTo(24)));
	
	//Broken tools
	public static final RegistryObject<Item> BROKEN_PICKAXE = ITEMS.register("broken_pickaxe", () -> new BrokenTool(new Item.Properties().tab(BlacksmithItemGroup.instance), Tool.PICKAXE));
	public static final RegistryObject<Item> BROKEN_AXE = ITEMS.register("broken_axe", () -> new BrokenTool(new Item.Properties().tab(BlacksmithItemGroup.instance), Tool.AXE));
	public static final RegistryObject<Item> BROKEN_SHOVEL = ITEMS.register("broken_shovel", () -> new BrokenTool(new Item.Properties().tab(BlacksmithItemGroup.instance), Tool.SHOVEL));
	public static final RegistryObject<Item> BROKEN_HOE = ITEMS.register("broken_hoe", () -> new BrokenTool(new Item.Properties().tab(BlacksmithItemGroup.instance), Tool.HOE));
	public static final RegistryObject<Item> BROKEN_KNIFE = ITEMS.register("broken_knife", () -> new BrokenTool(new Item.Properties().tab(BlacksmithItemGroup.instance), Tool.KNIFE));
	public static final RegistryObject<Item> BROKEN_HAMMER = ITEMS.register("broken_hammer", () -> new BrokenTool(new Item.Properties().tab(BlacksmithItemGroup.instance), Tool.HAMMER));
	public static final RegistryObject<Item> BROKEN_MORTAR = ITEMS.register("broken_mortar", () -> new BrokenTool(new Item.Properties().tab(BlacksmithItemGroup.instance), Tool.MORTAR, 0));
	
	//Alchemy
	public static final RegistryObject<Item> MORTARANDPESTLE = ITEMS.register("mortarandpestle", () -> new Mortar(new Item.Properties().stacksTo(1).tab(AlchemyItemGroup.instance).durability(131), 50, 1));
	public static final RegistryObject<Item> WOODEN_MORTARANDPESTLE = ITEMS.register("wooden_mortarandpestle", () -> new Mortar(new Item.Properties().stacksTo(1).tab(AlchemyItemGroup.instance).durability(59), 75, 1));
	public static final RegistryObject<Item> FLOUR = ITEMS.register("flour", () -> new Item(new Item.Properties().tab(AlchemyItemGroup.instance)));
	public static final RegistryObject<Item> SOURDOUGH = ITEMS.register("sourdough", () -> new Sourdough(new Item.Properties().stacksTo(1).durability(25).tab(AlchemyItemGroup.instance)));
	public static final RegistryObject<Item> DOUGH = ITEMS.register("dough", () -> new Item(new Item.Properties().tab(AlchemyItemGroup.instance)));
	public static final RegistryObject<Item> SALVE_MIXTURE = ITEMS.register("salve_mix", () -> new Item(new Item.Properties().tab(AlchemyItemGroup.instance)));
	public static final RegistryObject<Item> SALVE_CREAM = ITEMS.register("salve_cream", () -> new Item(new Item.Properties().tab(AlchemyItemGroup.instance)));
	public static final RegistryObject<Item> SALVE_BOWL = ITEMS.register("salve_bowl", () -> new HealingSalve(new Item.Properties().tab(AlchemyItemGroup.instance), Items.BOWL));
	public static final RegistryObject<Item> SALVE_PAPER = ITEMS.register("salve_paper", () -> new HealingSalve(new Item.Properties().tab(AlchemyItemGroup.instance), null));
	public static final RegistryObject<Item> ANTIDOTE_MIXTURE = ITEMS.register("antidote_mix", () -> new Item(new Item.Properties().tab(AlchemyItemGroup.instance)));
	public static final RegistryObject<Item> ANTIDOTE = ITEMS.register("antidote", () -> new Item(new Item.Properties().tab(AlchemyItemGroup.instance)));
	public static final RegistryObject<Item> ANTIDOTE_GLASS = ITEMS.register("antidote_glass", () -> new Antidote(new Item.Properties().tab(AlchemyItemGroup.instance)));
	public static final RegistryObject<Item> STIMULANT_MIXTURE = ITEMS.register("stimulant_mix", () -> new Item(new Item.Properties().tab(AlchemyItemGroup.instance)));
	public static final RegistryObject<Item> STIMULANT_CREAM = ITEMS.register("stimulant_cream", () -> new Item(new Item.Properties().tab(AlchemyItemGroup.instance)));
	public static final RegistryObject<Item> STIMULANT_BOWL = ITEMS.register("stimulant_bowl", () -> new Stimulant(new Item.Properties().tab(AlchemyItemGroup.instance), Items.BOWL));
	public static final RegistryObject<Item> STIMULANT_PAPER = ITEMS.register("stimulant_paper", () -> new Stimulant(new Item.Properties().tab(AlchemyItemGroup.instance), null));
	public static final RegistryObject<Item> BREATH_MIXTURE = ITEMS.register("breath_mix", () -> new Item(new Item.Properties().tab(AlchemyItemGroup.instance)));
	public static final RegistryObject<Item> BREATH_GUM = ITEMS.register("breath_gum", () -> new BreathGum(new Item.Properties().tab(AlchemyItemGroup.instance)));
	public static final RegistryObject<Item> ENERGY_MIXTURE = ITEMS.register("energy_mix", () -> new Item(new Item.Properties().tab(AlchemyItemGroup.instance)));
	public static final RegistryObject<Item> ENERGY_CREAM = ITEMS.register("energy_cream", () -> new Item(new Item.Properties().tab(AlchemyItemGroup.instance)));
	public static final RegistryObject<Item> ENERGY_BOWL = ITEMS.register("energy_bowl", () -> new EnergySnack(new Item.Properties().tab(AlchemyItemGroup.instance), Items.BOWL));
	public static final RegistryObject<Item> ENERGY_PAPER = ITEMS.register("energy_paper", () -> new EnergySnack(new Item.Properties().tab(AlchemyItemGroup.instance), null));
	public static final RegistryObject<Item> CREOSOTE_GLASS = ITEMS.register("creosote_glass", () -> new Item(new Item.Properties().tab(AlchemyItemGroup.instance)));
	public static final RegistryObject<Item> CREOSOTE_BUCKET = ITEMS.register("creosote_bucket", () -> new BucketItem(() -> FluidInit.CREOSOTE_FLUID.get(),new Item.Properties().stacksTo(1).tab(AlchemyItemGroup.instance)));
	public static final RegistryObject<Item> PLANT_OIL_GLASS = ITEMS.register("plant_oil_glass", () -> new Item(new Item.Properties().tab(AlchemyItemGroup.instance)));
	public static final RegistryObject<Item> PLANT_OIL_BUCKET = ITEMS.register("plant_oil_bucket", () -> new BucketItem(() -> FluidInit.PLANT_OIL_FLUID.get(),new Item.Properties().stacksTo(1).tab(AlchemyItemGroup.instance)));
	//public static final RegistryObject<Item> SANDWICH = ITEMS.register("sandwich", () -> new Item(new Item.Properties().food(new Food.Builder().hunger(6).saturation(8.f).build()).group(AlchemyItemGroup.instance)));
	//public static final RegistryObject<Item> ROASTED_CHICKEN_DISH = ITEMS.register("roasted_chicken_dish", () -> new Item(new Item.Properties().containerItem(Items.BOWL).food(new Food.Builder().hunger(12).saturation(10.f).build()).group(AlchemyItemGroup.instance)));
	//public static final RegistryObject<Item> HONEY_ROASTED_BEETROOT_CARROT = ITEMS.register("honey_roasted_beetroot_carrot", () -> new Item(new Item.Properties().containerItem(Items.BOWL).food(new Food.Builder().hunger(8).saturation(14.f).build()).group(AlchemyItemGroup.instance)));
	
	public enum ModItemTier implements IItemTier {
		COPPER(1, 150, 5.0f, 1.5f, 18, 10, () -> {
		      return Ingredient.of(ItemInit.COPPER_NUGGET.get());
		}, 1085, 1.0f, 0xff7f2b),
		SILVER(1, 100, 4.5f, 1.0f, 20, 8, () -> {
		      return Ingredient.of(ItemInit.SILVER_NUGGET.get());
		}, 962, 1.4f, 0xd2f3ff),
		GOLD(0, 75, 4.0f, 0.5f, 24, 6, () -> {
		      return Ingredient.of(Items.GOLD_NUGGET);
		}, 1064, 1.4f, 0xf8b500),
		ARSENICAL_BRONZE(2, 384, 6.0f, 2.0f, 15, 20, () -> {
		      return Ingredient.of(ItemInit.ARSENICAL_BRONZE_NUGGET.get());
		}, 950, 1.0f, 0xf5f5f5);
		private final int harvestLevel;
		private final int maxUses;
		private final float efficiency;
		private final float attackDamage;
		private final int enchantability;
		private final LazyValue<Ingredient> repairMaterial;
		private final int hardness;
		private final int meltingPoint;
		private final int colour;

		private ModItemTier(int harvestLevelIn, int maxUsesIn, float efficiencyIn, float attackDamageIn, int enchantabilityIn, int hardness, Supplier<Ingredient> repairMaterialIn, int meltingPoint, float heatingSpeed, int colour) {
			this.harvestLevel = harvestLevelIn;
			this.maxUses = maxUsesIn;
			this.efficiency = efficiencyIn;
			this.attackDamage = attackDamageIn;
			this.enchantability = enchantabilityIn;
			this.repairMaterial = new LazyValue<>(repairMaterialIn);
			this.hardness = hardness;
			this.meltingPoint = (meltingPoint + 273) * 100;
			this.colour = colour;
		}
		@Override
		public int getUses() {
			return this.maxUses;
		}
		@Override
		public float getSpeed() {
			return this.efficiency;
		}
		@Override
		public float getAttackDamageBonus() {
			return this.attackDamage;
		}
		@Override
		public int getLevel() {
			return this.harvestLevel;
		}
		@Override
		public int getEnchantmentValue() {
			return this.enchantability;
		}
		public int getHardness() {
			return this.hardness;
		}
		@Override
		public Ingredient getRepairIngredient() {
			return this.repairMaterial.get();
		}
		public int getMeltingPoint() {
			return this.meltingPoint;
		}
		public int getColour() {
			return this.colour;
		}
	}
	public enum ModArmorMaterial implements IArmorMaterial{
		COPPER(ProfessionsMod.MODID + ":copper_chainmail", 5, new int[]{1, 2, 3, 1}, SoundEvents.ARMOR_EQUIP_CHAIN, 0.f, () -> {
		      return Ingredient.of(ItemInit.COPPER_CHAIN.get());
		}),
		SILVER(ProfessionsMod.MODID + ":silver_chainmail", 4, new int[]{1, 2, 2, 1}, SoundEvents.ARMOR_EQUIP_CHAIN, 0.f, () -> {
		      return Ingredient.of(ItemInit.SILVER_CHAIN.get());
		}),
		GOLD(ProfessionsMod.MODID + ":gold_chainmail", 3, new int[]{1, 1, 2, 1}, SoundEvents.ARMOR_EQUIP_CHAIN, 0.f, () -> {
		      return Ingredient.of(ItemInit.GOLDEN_CHAIN.get());
		}),
		ARSENICAL_BRONZE(ProfessionsMod.MODID + ":arsenical_bronze_chainmail", 7, new int[]{1, 3, 3, 2}, SoundEvents.ARMOR_EQUIP_CHAIN, 0.f, () -> {
		      return Ingredient.of(ItemInit.ARSENICAL_BRONZE_CHAIN.get());
		});
		private static final int[] MAX_DAMAGE_ARRAY = {13, 15, 16, 11};
		private final String name;
		private final int maxDamageFactor;
		private final int[] damageReductionAmountArray;
		private final SoundEvent soundEvent;
		private final float toughness;
		private final LazyValue<Ingredient> ingredient;
		
		private ModArmorMaterial(String name, int maxDamageFactor, int[] damageReductionAmountIn, SoundEvent soundEventIn, float toughness, Supplier<Ingredient> ingredient) {
			this.name = name;
			this.maxDamageFactor = maxDamageFactor;
			this.damageReductionAmountArray = damageReductionAmountIn;
			this.soundEvent = soundEventIn;
			this.toughness = toughness;
			this.ingredient = new LazyValue<>(ingredient);
		}
		@Override
		public int getDurabilityForSlot(EquipmentSlotType slotIn) {
			return ModArmorMaterial.MAX_DAMAGE_ARRAY[slotIn.getIndex()] * this.maxDamageFactor;
		}
		@Override
		public int getDefenseForSlot(EquipmentSlotType slotIn) {
			return this.damageReductionAmountArray[slotIn.getIndex()];
		}
		@Override
		public int getEnchantmentValue() {
			return 0;
		}
		@Override
		public SoundEvent getEquipSound() {
			return this.soundEvent;
		}
		@Override
		public Ingredient getRepairIngredient() {
			return this.ingredient.get();
		}
		@Override
		@OnlyIn(Dist.CLIENT)
		public String getName() {
			return this.name;
		}
		@Override
		public float getToughness() {
			return this.toughness;
		}
		@Override
		public float getKnockbackResistance() {
			return 0;
		}
	}
}
