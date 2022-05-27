package com.thoriuslight.professionsmod.client.gui.screen.inventory;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.thoriuslight.professionsmod.inventory.container.OvenContainer;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class OvenScreen extends ContainerScreen<OvenContainer>{
	private static final ResourceLocation FURNACE_GUI_TEXTURES = new ResourceLocation("textures/gui/container/furnace.png");
	
	public OvenScreen(OvenContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
	}
@Override
public void render(MatrixStack matrixStack, int p_render_1_, int p_render_2_, float p_render_3_) {
    this.renderBackground(matrixStack);
	super.render(matrixStack, p_render_1_, p_render_2_, p_render_3_);
    this.renderTooltip(matrixStack, p_render_1_, p_render_2_);
}
	@SuppressWarnings("deprecation")
	@Override
	protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bind(FURNACE_GUI_TEXTURES);
		int i = this.leftPos;
		int j = this.topPos;
		this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
	    if (((OvenContainer)this.menu).isBurning()) {
	    	int k = ((OvenContainer)this.menu).getBurnLeftScaled();
	    	this.blit(matrixStack, i + 56, j + 36 + 12 - k, 176, 12 - k, 14, k + 1);
	    }
	    int l = ((OvenContainer)this.menu).getCookProgressionScaled();
	    this.blit(matrixStack, i + 79, j + 34, 176, 14, l + 1, 16);
	}

}
