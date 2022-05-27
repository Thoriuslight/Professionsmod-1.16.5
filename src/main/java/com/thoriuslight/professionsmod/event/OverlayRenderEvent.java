package com.thoriuslight.professionsmod.event;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.thoriuslight.professionsmod.ProfessionsMod;
import com.thoriuslight.professionsmod.init.SkillInit;
import com.thoriuslight.professionsmod.profession.capabilities.CapabilityProfession;
import com.thoriuslight.professionsmod.profession.capabilities.IProfession;
import com.thoriuslight.professionsmod.profession.capabilities.ProfessionTypes.profession;
import com.thoriuslight.professionsmod.tileentity.AbstractHeaterTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.util.NonNullConsumer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

public @Mod.EventBusSubscriber(modid = ProfessionsMod.MODID, bus = Bus.FORGE)
class OverlayRenderEvent {
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void render(RenderGameOverlayEvent.Post event) {
		if (event.getType() == ElementType.TEXT) {
			Minecraft minecraft = Minecraft.getInstance();
			minecraft.player.getCapability(CapabilityProfession.PROFESSION, null).ifPresent(new NonNullConsumer<IProfession>() {
				@Override
	            public void accept(@Nonnull IProfession iProfession) {
					if(iProfession.getProfession() == profession.SMITH) {
						Entity entity = minecraft.getCameraEntity();
						RayTraceResult rayTraceBlock = entity.pick(20.0D, 0.0F, false);
						if (rayTraceBlock.getType() == RayTraceResult.Type.BLOCK) {
							BlockPos blockpos = ((BlockRayTraceResult)rayTraceBlock).getBlockPos();
							TileEntity tileEntity = minecraft.level.getBlockEntity(blockpos);
							if(tileEntity instanceof AbstractHeaterTileEntity) {
								MatrixStack matrixStack = event.getMatrixStack();
								if(iProfession.getSkillTalent(SkillInit.TEMP_READING_I.getId())) {
					         		int temp = (((AbstractHeaterTileEntity)tileEntity).getMaxTemperature()/100 - 273)/500 * 500;
					       			int x = minecraft.getWindow().getGuiScaledWidth()/2 + minecraft.getWindow().getGuiScaledWidth()/20;
					        		int y = minecraft.getWindow().getGuiScaledHeight()/2 - minecraft.font.lineHeight;
					         		minecraft.font.draw(matrixStack, "Max: " + temp + " °C", x, y, 0xFFFFFFFF);
					         	}
								if(iProfession.getSkillTalent(SkillInit.TEMP_INSPECTION_I.getId())) {
					         		int temp = (((AbstractHeaterTileEntity)tileEntity).getTemperature() - 273)/100 * 100;;
					       			int x = minecraft.getWindow().getGuiScaledWidth()/2 + minecraft.getWindow().getGuiScaledWidth()/20;
					        		int y = minecraft.getWindow().getGuiScaledHeight()/2 + minecraft.font.lineHeight;
					         		minecraft.font.draw(matrixStack, temp + " °C", x, y, 0xFFFFFFFF);
					         	}
					       	}
						}
					}
				}
			});
		}
	}
}
