package com.thoriuslight.professionsmod.init;

import com.thoriuslight.professionsmod.ProfessionsMod;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class FluidInit {
    public static final ResourceLocation WATER_STILL = new ResourceLocation("block/water_still");
    public static final ResourceLocation WATER_FLOWING = new ResourceLocation("block/water_flow");
    public static final ResourceLocation WATER_OVERLAY = new ResourceLocation("block/water_overlay");
    
	public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, ProfessionsMod.MODID);
	public static final RegistryObject<FlowingFluid> CREOSOTE_FLUID = FLUIDS.register("creosote_fluid", () -> new ForgeFlowingFluid.Source(FluidInit.CREOSOTE_PROPERTIES));
	public static final RegistryObject<FlowingFluid> PLANT_OIL_FLUID = FLUIDS.register("plant_oil_fluid", () -> new ForgeFlowingFluid.Source(FluidInit.PLANT_OIL_PROPERTIES));
	public static final RegistryObject<FlowingFluid> CREOSOTE_FLOWING = FLUIDS.register("creosote_flowing", () -> new ForgeFlowingFluid.Flowing(FluidInit.CREOSOTE_PROPERTIES));
	public static final RegistryObject<FlowingFluid> PLANT_OIL_FLOWING = FLUIDS.register("plant_oil_flowing", () -> new ForgeFlowingFluid.Flowing(FluidInit.PLANT_OIL_PROPERTIES));
	
	public static final ForgeFlowingFluid.Properties CREOSOTE_PROPERTIES = new ForgeFlowingFluid.Properties(
			() -> CREOSOTE_FLUID.get(), () -> CREOSOTE_FLOWING.get(), FluidAttributes.builder(WATER_STILL, WATER_FLOWING).overlay(WATER_OVERLAY)
            .translationKey("block.professionsmod.creosote")
            .color(0xFF041A00)
            .sound(SoundEvents.BUCKET_FILL, SoundEvents.BUCKET_EMPTY))
			.slopeFindDistance(1).levelDecreasePerBlock(4)
			.block(() -> FluidInit.CREOSOTE_BLOCK.get()).bucket(() -> ItemInit.CREOSOTE_BUCKET.get());
	public static final ForgeFlowingFluid.Properties PLANT_OIL_PROPERTIES = new ForgeFlowingFluid.Properties(
			() -> PLANT_OIL_FLUID.get(), () -> PLANT_OIL_FLOWING.get(), FluidAttributes.builder(WATER_STILL, WATER_FLOWING).overlay(WATER_OVERLAY)
			.density(1000).luminosity(0).viscosity(1000)
            .translationKey("block.professionsmod.plant_oil")
            .color(0xFFbcc200)
            .sound(SoundEvents.BUCKET_FILL, SoundEvents.BUCKET_EMPTY))
			.slopeFindDistance(2)
			.block(() -> FluidInit.PLANT_OIL_BLOCK.get()).bucket(() -> ItemInit.PLANT_OIL_BUCKET.get());
	
	public static final RegistryObject<FlowingFluidBlock> CREOSOTE_BLOCK = BlockInit.BLOCKS.register("creosote", () -> new FlowingFluidBlock(() -> FluidInit.CREOSOTE_FLUID.get(), 
			AbstractBlock.Properties.of(Material.WATER).strength(100.f).noDrops()));
	public static final RegistryObject<FlowingFluidBlock> PLANT_OIL_BLOCK = BlockInit.BLOCKS.register("plant_oil", () -> new FlowingFluidBlock(() -> FluidInit.PLANT_OIL_FLUID.get(), 
			AbstractBlock.Properties.of(Material.WATER).strength(100.f).noDrops()));
}
/*
Creosote:
specific energy = 9,3 kWh/kg
density 		= 1,09 kg/l
energy density 	= 10,137 kWh/l
Plant Oil:
specific energy = 10,5 kWh/kg
density 		= 0,92 kg/l
energy density 	= 9,66 kWh/l
*/