package com.thoriuslight.professionsmod.util;

import com.thoriuslight.professionsmod.ProfessionsMod;
import com.thoriuslight.professionsmod.client.gui.WoodenHopperScreen;
import com.thoriuslight.professionsmod.client.gui.screen.inventory.AlchemistCraftingScreen;
import com.thoriuslight.professionsmod.client.gui.screen.inventory.ExtractorScreen;
import com.thoriuslight.professionsmod.client.gui.screen.inventory.InspectionTableScreen;
import com.thoriuslight.professionsmod.client.gui.screen.inventory.OvenScreen;
import com.thoriuslight.professionsmod.client.gui.screen.inventory.SmithCraftingScreen;
import com.thoriuslight.professionsmod.client.tileentity.renderer.CastingBasinRenderer;
import com.thoriuslight.professionsmod.client.tileentity.renderer.CrucibleRenderer;
import com.thoriuslight.professionsmod.client.tileentity.renderer.ExtractorRenderer;
import com.thoriuslight.professionsmod.client.tileentity.renderer.ForgeRenderer;
import com.thoriuslight.professionsmod.client.tileentity.renderer.OvenRenderer;
import com.thoriuslight.professionsmod.client.tileentity.renderer.StoneAnvilRenderer;
import com.thoriuslight.professionsmod.init.BlockInit;
import com.thoriuslight.professionsmod.init.FluidInit;
import com.thoriuslight.professionsmod.init.ItemInit;
import com.thoriuslight.professionsmod.init.KeyBindInit;
import com.thoriuslight.professionsmod.init.ModContainerTypes;
import com.thoriuslight.professionsmod.init.ModTileEntityTypes;
import com.thoriuslight.professionsmod.init.SkillInit;
import com.thoriuslight.professionsmod.item.Mortar;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = ProfessionsMod.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber {
	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {
		ScreenManager.register(ModContainerTypes.WOODEN_HOPPER.get(), WoodenHopperScreen::new);
		ScreenManager.register(ModContainerTypes.SMITH_CRAFTING.get(), SmithCraftingScreen::new);
		ScreenManager.register(ModContainerTypes.ALCHEMIST_CRAFTING.get(), AlchemistCraftingScreen::new);
		ScreenManager.register(ModContainerTypes.INSPECTION_TABLE.get(), InspectionTableScreen::new);
		ScreenManager.register(ModContainerTypes.OVEN.get(), OvenScreen::new);
		ScreenManager.register(ModContainerTypes.EXTRACTOR.get(), ExtractorScreen::new);
		ClientRegistry.bindTileEntityRenderer(ModTileEntityTypes.STONEANVIL.get(), StoneAnvilRenderer::new);
		ClientRegistry.bindTileEntityRenderer(ModTileEntityTypes.FORGE.get(), ForgeRenderer::new);
		ClientRegistry.bindTileEntityRenderer(ModTileEntityTypes.CRUCIBLE.get(), CrucibleRenderer::new);
		ClientRegistry.bindTileEntityRenderer(ModTileEntityTypes.CASTINGBASIN.get(), CastingBasinRenderer::new);
		ClientRegistry.bindTileEntityRenderer(ModTileEntityTypes.OVEN_CONTROLLER.get(), OvenRenderer::new);
		ClientRegistry.bindTileEntityRenderer(ModTileEntityTypes.EXTRACTOR.get(), ExtractorRenderer::new);
	    RenderType rendertype1 = RenderType.cutout();
    	RenderTypeLookup.setRenderLayer(BlockInit.FORGE_BLOCK.get(), rendertype1);
        RenderType rendertype2 = RenderType.translucent();
    	RenderTypeLookup.setRenderLayer(BlockInit.EXTRACTOR_CONTROLLER_BLOCK.get(), rendertype2);
    	RenderTypeLookup.setRenderLayer(BlockInit.GLASS_JAR.get(), rendertype2);
    	RenderTypeLookup.setRenderLayer(FluidInit.PLANT_OIL_BLOCK.get(), rendertype2);
    	RenderTypeLookup.setRenderLayer(FluidInit.PLANT_OIL_FLOWING.get(), rendertype2);
    	RenderTypeLookup.setRenderLayer(FluidInit.PLANT_OIL_FLUID.get(), rendertype2);
    	KeyBindInit.initKeyBinding();
    	SkillInit.initSkills();
    	event.enqueueWork(() -> {
    	    ItemModelsProperties.register(ItemInit.WOODEN_MORTARANDPESTLE.get(), 
    	    new ResourceLocation(ProfessionsMod.MODID, "state"), (stack, world, living) -> {
				if(!Mortar.getState(stack).isEmpty()) {
					return 1.f;
				}
				return 0.f;
    	    });
    	});
    	event.enqueueWork(() -> {
    	    ItemModelsProperties.register(ItemInit.MORTARANDPESTLE.get(), 
    	    new ResourceLocation(ProfessionsMod.MODID, "state"), (stack, world, living) -> {
				if(!Mortar.getState(stack).isEmpty()) {
					return 1.f;
				}
				return 0.f;
    	    });
    	});
    	event.enqueueWork(() -> {
    	    ItemModelsProperties.register(ItemInit.COPPER_MORTARANDPESTLE.get(), 
    	    new ResourceLocation(ProfessionsMod.MODID, "state"), (stack, world, living) -> {
				if(!Mortar.getState(stack).isEmpty()) {
					return 1.f;
				}
				return 0.f;
    	    });
    	});
    	event.enqueueWork(() -> {
    	    ItemModelsProperties.register(ItemInit.SILVER_MORTARANDPESTLE.get(), 
    	    new ResourceLocation(ProfessionsMod.MODID, "state"), (stack, world, living) -> {
				if(!Mortar.getState(stack).isEmpty()) {
					return 1.f;
				}
				return 0.f;
    	    });
    	});
    	event.enqueueWork(() -> {
    	    ItemModelsProperties.register(ItemInit.GOLDEN_MORTARANDPESTLE.get(), 
    	    new ResourceLocation(ProfessionsMod.MODID, "state"), (stack, world, living) -> {
				if(!Mortar.getState(stack).isEmpty()) {
					return 1.f;
				}
				return 0.f;
    	    });
    	});
    	event.enqueueWork(() -> {
    	    ItemModelsProperties.register(ItemInit.ARSENICAL_BRONZE_MORTARANDPESTLE.get(), 
    	    new ResourceLocation(ProfessionsMod.MODID, "state"), (stack, world, living) -> {
				if(!Mortar.getState(stack).isEmpty()) {
					return 1.f;
				}
				return 0.f;
    	    });
    	});
	}
}
