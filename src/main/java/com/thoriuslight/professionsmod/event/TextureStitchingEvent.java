package com.thoriuslight.professionsmod.event;

import com.thoriuslight.professionsmod.ProfessionsMod;

import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

public @Mod.EventBusSubscriber(modid = ProfessionsMod.MODID, bus = Bus.MOD,  value = Dist.CLIENT)
class TextureStitchingEvent {
    @SuppressWarnings("deprecation")
	@SubscribeEvent
    public static void onTextureStitchEvent(TextureStitchEvent.Pre event) {
        if (event.getMap().location() == AtlasTexture.LOCATION_BLOCKS) {
            event.addSprite(new ResourceLocation(ProfessionsMod.MODID, "models/oven"));
            event.addSprite(new ResourceLocation(ProfessionsMod.MODID, "models/oven_lit"));
        }
    }

}
