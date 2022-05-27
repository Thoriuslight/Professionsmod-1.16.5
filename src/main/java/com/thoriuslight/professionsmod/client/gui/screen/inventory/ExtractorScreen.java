package com.thoriuslight.professionsmod.client.gui.screen.inventory;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.thoriuslight.professionsmod.ProfessionsMod;
import com.thoriuslight.professionsmod.inventory.container.ExtractorContainer;

import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ExtractorScreen extends ContainerScreen<ExtractorContainer>{
	private static final ResourceLocation EXTRACTOR_TEXTURES = new ResourceLocation(ProfessionsMod.MODID, "textures/gui/extractor.png");
	private static final ResourceLocation FLUID_TEXTURE = new ResourceLocation(ProfessionsMod.MODID, "textures/gui/fluid.png");
	public ExtractorScreen(ExtractorContainer container, PlayerInventory p_i51105_2_, ITextComponent p_i51105_3_) {
		super(container, p_i51105_2_, p_i51105_3_);
	}
	@Override
	protected void init() {
		super.init();
		this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
	}
	@Override
	public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
		this.renderBackground(p_230430_1_);
		super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
		this.renderTooltip(p_230430_1_, p_230430_2_, p_230430_3_);
	}
	@SuppressWarnings("deprecation")
	@Override
	protected void renderBg(MatrixStack matrixStack, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bind(EXTRACTOR_TEXTURES);
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
		int fluidAmount = this.menu.getFluidAmount();
		if(fluidAmount != 0) {
			this.minecraft.getTextureManager().bind(FLUID_TEXTURE);
			RenderSystem.pushMatrix();
			RenderSystem.color4f(0.016F, 0.102F, 0.0F, 1.0F);
			RenderSystem.disableBlend();
			int level = (int) ((49.f * fluidAmount)/4000);
		    AbstractGui.blit(matrixStack, i + 16, j + 68 - level, 0, 0, 35, level, 16, 16);
		    RenderSystem.popMatrix();
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.minecraft.getTextureManager().bind(EXTRACTOR_TEXTURES);
		}
	    this.blit(matrixStack, i + 16, j + 19, 176, 0, 35, 46);
	}
}
