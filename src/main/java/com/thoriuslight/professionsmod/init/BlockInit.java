package com.thoriuslight.professionsmod.init;

import com.thoriuslight.professionsmod.ProfessionsMod;
import com.thoriuslight.professionsmod.block.AlchemistCraftingTableBlock;
import com.thoriuslight.professionsmod.block.CastingBasinBlock;
import com.thoriuslight.professionsmod.block.CrucibleBlock;
import com.thoriuslight.professionsmod.block.ExtractorBlock;
import com.thoriuslight.professionsmod.block.ExtractorBlockUpper;
import com.thoriuslight.professionsmod.block.ForgeBlock;
import com.thoriuslight.professionsmod.block.GlassJarBlock;
import com.thoriuslight.professionsmod.block.InspectionTableBlock;
import com.thoriuslight.professionsmod.block.OvenBrickBlock;
import com.thoriuslight.professionsmod.block.OvenControllerBlock;
import com.thoriuslight.professionsmod.block.SmithCraftingTableBlock;
import com.thoriuslight.professionsmod.block.StoneAnvilBlock;
import com.thoriuslight.professionsmod.block.WoodenHopperBlock;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockInit {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ProfessionsMod.MODID);
	//Common Blocks
	public static final RegistryObject<Block> COPPER_ORE = BLOCKS.register("copper_ore", () -> new Block(Block.Properties.of(Material.STONE).strength(2.5f, 3.f).sound(SoundType.STONE).harvestLevel(1).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> SILVER_ORE = BLOCKS.register("silver_ore", () -> new Block(Block.Properties.of(Material.STONE).strength(2.5f, 3.f).sound(SoundType.STONE).harvestLevel(1).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> INSULATION_BRICK_BLOCK = BLOCKS.register("insulation_brick_block", () -> new Block(Block.Properties.of(Material.STONE).strength(3.f, 4.f).sound(SoundType.STONE).harvestLevel(1).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops()));
	//Smithing Blocks
	public static final RegistryObject<Block> SMITHCRAFTINGTABLE_BLOCK = BLOCKS.register("smithcraftingtable_block", () -> new SmithCraftingTableBlock((Block.Properties.of(Material.STONE).strength(3.f, 3.f).sound(SoundType.STONE).harvestLevel(1).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops())));
	public static final RegistryObject<Block> STONEANVIL_BLOCK = BLOCKS.register("stoneanvil_block", () -> new StoneAnvilBlock((Block.Properties.of(Material.STONE).strength(3.f, 6.f).sound(SoundType.STONE).harvestLevel(1).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops())));
	public static final RegistryObject<Block> CASTINGBASIN_BLOCK = BLOCKS.register("castingbasin_block", () -> new CastingBasinBlock(Block.Properties.of(Material.STONE).strength(1.5f, 2.f).sound(SoundType.STONE).harvestLevel(0).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> STONECASTINGBASIN_BLOCK = BLOCKS.register("stonecastingbasin_block", () -> new CastingBasinBlock(Block.Properties.of(Material.STONE).strength(1.5f, 2.f).sound(SoundType.STONE).harvestLevel(0).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> INSPECTIONTABLE_BLOCK = BLOCKS.register("inspectiontable_block", () -> new InspectionTableBlock(Block.Properties.of(Material.STONE).strength(2.0f, 2.f).sound(SoundType.STONE).harvestLevel(0).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> FORGE_BLOCK = BLOCKS.register("forge_block", () -> new ForgeBlock((Block.Properties.of(Material.STONE).strength(3.f, 3.f).sound(SoundType.STONE).harvestLevel(1).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops())));
	public static final RegistryObject<Block> CRUCIBLE_BLOCK = BLOCKS.register("crucible_block", () -> new CrucibleBlock((Block.Properties.of(Material.STONE).strength(3.f, 3.f).sound(SoundType.STONE).harvestLevel(1).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops())));
	//Engineering Blocks
	public static final RegistryObject<Block> WOODENHOPPER_BLOCK = BLOCKS.register("woodenhopper_block", () -> new WoodenHopperBlock(Block.Properties.of(Material.WOOD).strength(2.0f, 3.f).sound(SoundType.WOOD).harvestLevel(0).harvestTool(ToolType.AXE)));
	//Alchemy Blocks
	public static final RegistryObject<Block> ALCHEMISTCRAFTINGTABLE_BLOCK = BLOCKS.register("alchemistcraftingtable_block", () -> new AlchemistCraftingTableBlock((Block.Properties.of(Material.STONE).strength(3.f, 3.f).sound(SoundType.STONE).harvestLevel(1).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops())));
	public static final RegistryObject<Block> GLASS_JAR = BLOCKS.register("glass_jar", () -> new GlassJarBlock(Block.Properties.of(Material.GLASS).strength(1.0f, 3.f).sound(SoundType.GLASS)));
	public static final RegistryObject<Block> OVEN_BRICK_BLOCK = BLOCKS.register("oven_brick_block", () -> new OvenBrickBlock(Block.Properties.of(Material.STONE).strength(3.f, 4.f).sound(SoundType.STONE).harvestLevel(1).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> OVEN_CONTROLLER_BLOCK = BLOCKS.register("oven_controller_block", () -> new OvenControllerBlock(Block.Properties.of(Material.STONE).strength(3.f, 4.f).sound(SoundType.STONE).harvestLevel(1).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops()));
	public static final RegistryObject<Block> EXTRACTOR_CONTROLLER_BLOCK = BLOCKS.register("extractor_controller_block", () -> new ExtractorBlock(Block.Properties.of(Material.STONE).strength(3.f, 4.f).sound(SoundType.STONE).harvestLevel(1).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops().noOcclusion()));
	public static final RegistryObject<Block> EXTRACTOR_UPPER_BLOCK = BLOCKS.register("extractor_upper_block", () -> new ExtractorBlockUpper(Block.Properties.of(Material.STONE).strength(3.f, 4.f).sound(SoundType.STONE).harvestLevel(1).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops().noOcclusion()));

}
