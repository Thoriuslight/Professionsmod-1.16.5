package com.thoriuslight.professionsmod.client.gui.screen.inventory;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.thoriuslight.professionsmod.ProfessionsMod;
import com.thoriuslight.professionsmod.inventory.container.InspectionTableContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class InspectionTableScreen extends ContainerScreen<InspectionTableContainer> {
	private static final ResourceLocation INSPECTION_TABLE_TEXTURES = new ResourceLocation(ProfessionsMod.MODID, "textures/gui/inspection_table.png");
	public InspectionTableScreen(InspectionTableContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
	}
	
	@Override
	public void render(MatrixStack matrixStack, int p_render_1_, int p_render_2_, float p_render_3_) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, p_render_1_, p_render_2_, p_render_3_);
		this.renderTooltip(matrixStack, p_render_1_, p_render_2_);
	}
	
	@Override
	protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {
		String  durability;
		String damage;
		ItemStack stack = this.menu.getSlot(0).getItem();
		if(stack.getMaxDamage() > 1) {
			int i = stack.getMaxDamage();
			durability = String.valueOf(i);
			damage = String.valueOf(i - stack.getDamageValue());
		} else {
			durability = "-";
			damage = "-";
		}
	    this.font.draw(matrixStack, I18n.get("screen.inspection_table.durability") + " " + damage + "/" + durability, (float)(45), (float)(20), 4210752);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bind(INSPECTION_TABLE_TEXTURES);
		int i = this.leftPos;
		int j = (this.height - this.imageHeight) / 2;
		this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
	}

}
