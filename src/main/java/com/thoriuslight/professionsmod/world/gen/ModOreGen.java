package com.thoriuslight.professionsmod.world.gen;

import com.thoriuslight.professionsmod.init.BlockInit;

import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;

public class ModOreGen {
	public static void generateOreEvent(final BiomeLoadingEvent event) {
		if(!(event.getCategory().equals(Biome.Category.NETHER) || event.getCategory().equals(Biome.Category.THEEND))) {
			//Copper
			if(event.getCategory().equals(Biome.Category.EXTREME_HILLS)) {
				generateOre(event.getGeneration(), OreFeatureConfig.FillerBlockType.NATURAL_STONE, BlockInit.COPPER_ORE.get().defaultBlockState(), 10, 30, 0, 128);
			}
			else {
				generateOre(event.getGeneration(), OreFeatureConfig.FillerBlockType.NATURAL_STONE, BlockInit.COPPER_ORE.get().defaultBlockState(), 8, 16, 0, 128);
			}
			//Silver
			if(event.getCategory().equals(Biome.Category.TAIGA)) {
				generateOre(event.getGeneration(), OreFeatureConfig.FillerBlockType.NATURAL_STONE, BlockInit.SILVER_ORE.get().defaultBlockState(), 10, 10, 0, 64);
			}
			else {
				generateOre(event.getGeneration(), OreFeatureConfig.FillerBlockType.NATURAL_STONE, BlockInit.SILVER_ORE.get().defaultBlockState(), 5, 7, 0, 48);
			}
		}
	}
	private static void generateOre(BiomeGenerationSettingsBuilder settings, RuleTest filler, BlockState state, int veinSize, int count, int minH, int maxH) {
		settings.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Feature.ORE.configured(new OreFeatureConfig(filler, state, veinSize))
				.decorated(Placement.RANGE.configured(new TopSolidRangeConfig(minH, 0, maxH))).squared().count(count));
	}
}
