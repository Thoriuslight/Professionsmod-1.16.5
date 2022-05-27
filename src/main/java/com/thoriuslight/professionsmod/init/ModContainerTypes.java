package com.thoriuslight.professionsmod.init;

import com.thoriuslight.professionsmod.ProfessionsMod;
import com.thoriuslight.professionsmod.inventory.container.WoodenHopperContainer;
import com.thoriuslight.professionsmod.inventory.container.InspectionTableContainer;
import com.thoriuslight.professionsmod.inventory.container.OvenContainer;
import com.thoriuslight.professionsmod.inventory.container.SmithCraftingContainer;
import com.thoriuslight.professionsmod.inventory.container.AlchemistCraftingContainer;
import com.thoriuslight.professionsmod.inventory.container.ExtractorContainer;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainerTypes {
	public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, ProfessionsMod.MODID);
	
	public static final RegistryObject<ContainerType<WoodenHopperContainer>> WOODEN_HOPPER = CONTAINER_TYPES.register("wooden_hopper", () -> IForgeContainerType.create(WoodenHopperContainer::new));
	public static final RegistryObject<ContainerType<SmithCraftingContainer>> SMITH_CRAFTING = CONTAINER_TYPES.register("smith_crafting", () -> IForgeContainerType.create(SmithCraftingContainer::new));
	public static final RegistryObject<ContainerType<AlchemistCraftingContainer>> ALCHEMIST_CRAFTING = CONTAINER_TYPES.register("alchemist_crafting", () -> IForgeContainerType.create(AlchemistCraftingContainer::new));
	public static final RegistryObject<ContainerType<InspectionTableContainer>> INSPECTION_TABLE = CONTAINER_TYPES.register("inspection_table", () -> IForgeContainerType.create(InspectionTableContainer::new));
	public static final RegistryObject<ContainerType<OvenContainer>> OVEN = CONTAINER_TYPES.register("oven", () -> IForgeContainerType.create(OvenContainer::new));
	public static final RegistryObject<ContainerType<ExtractorContainer>> EXTRACTOR = CONTAINER_TYPES.register("extractor", () -> IForgeContainerType.create(ExtractorContainer::new));
}
