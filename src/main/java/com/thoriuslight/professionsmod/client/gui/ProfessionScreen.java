package com.thoriuslight.professionsmod.client.gui;

import java.util.concurrent.atomic.AtomicInteger;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.thoriuslight.professionsmod.profession.P_Consts;
import com.thoriuslight.professionsmod.profession.capabilities.CapabilityProfession;
import com.thoriuslight.professionsmod.profession.capabilities.IProfession;
import com.thoriuslight.professionsmod.profession.capabilities.ProfessionTypes.profession;
import com.thoriuslight.professionsmod.profession.skill.SkillTree;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;

public class ProfessionScreen extends Screen{
	private static final ResourceLocation WINDOW = new ResourceLocation("textures/gui/advancements/window.png");
	private final SkillTree skills;
	private int skill;
	private AtomicInteger points;
	public ProfessionScreen(Minecraft minecraft, profession prof) {
		super(new TranslationTextComponent(prof.getName()));
		skills = new SkillTree(minecraft);
		IProfession iProfession = minecraft.player.getCapability(CapabilityProfession.PROFESSION, null).orElseThrow(IllegalStateException::new);
		skill = iProfession.getSkill();
		points = new AtomicInteger(iProfession.getSkillPoints());
	}
	
	@Override
	public void render(MatrixStack matrixStack, int p_render_1_, int p_render_2_, float p_render_3_) {
		int i = (this.width - 252) / 2;
		int j = (this.height - 140) / 2;
		this.renderBackground(matrixStack);
		this.renderInside(matrixStack, i, j);
		this.renderWindow(matrixStack, i, j);
		this.renderTooltip(matrixStack, p_render_1_, p_render_2_, i, j);
	}
	   
	@SuppressWarnings("deprecation")
	private void renderTooltip(MatrixStack matrixStack, int p_render_1_, int p_render_2_, int i, int j) {
	      RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
	      RenderSystem.pushMatrix();
	      RenderSystem.enableDepthTest();
	      RenderSystem.translatef((float)(i + 9), (float)(j + 18), 400.0F);
	      skills.drawToolTips(matrixStack, p_render_1_ - i - 9, p_render_2_ - j - 18, i, j);
	      RenderSystem.disableDepthTest();
	      RenderSystem.popMatrix();
	}

	@SuppressWarnings("deprecation")
	private void renderInside(MatrixStack matrixStack, int xC, int yC) {
		RenderSystem.pushMatrix();
		RenderSystem.translatef((float)(xC + 9), (float)(yC + 18), 0.0F);
		skills.drawContents(matrixStack);
		RenderSystem.popMatrix();
		RenderSystem.depthFunc(515);
		RenderSystem.disableDepthTest();
	}
	   
	@SuppressWarnings("deprecation")
	private void renderWindow(MatrixStack matrixStack, int xC, int yC){
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.enableBlend();
		this.minecraft.getTextureManager().bind(WINDOW);
		this.blit(matrixStack, xC, yC, 0, 0, 252, 140);
		RenderSystem.disableBlend();
	    this.font.draw(matrixStack, this.title, (float)(xC + 8), (float)(yC + 6), 4210752);
	    this.font.draw(matrixStack, I18n.get("Skill: " + String.valueOf(skill) + "/" + P_Consts.MAX_SKILL), (float)(xC + 90), (float)(yC + 6), 4210752);
	    this.font.draw(matrixStack, I18n.get("Points: ") + String.valueOf(points), (float)(xC + 180), (float)(yC + 6), 4210752);
	}
	   
	@Override
	public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
		//Left click
		if (p_mouseClicked_5_ == 0) {
			//translate mouse position
			int mX = MathHelper.floor(p_mouseClicked_1_) - (this.width - 252) / 2 - 9;
			int mY = MathHelper.floor(p_mouseClicked_3_) - (this.height - 140) / 2 - 18;
			skills.mouseClicked(mX, mY, points, this);
		}
		return super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
	}
}
