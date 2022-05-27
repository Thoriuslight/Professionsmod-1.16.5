package com.thoriuslight.professionsmod.profession.skill;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.thoriuslight.professionsmod.client.gui.SkillGui;
import com.thoriuslight.professionsmod.client.gui.SkillGuideScreen;
import com.thoriuslight.professionsmod.profession.capabilities.CapabilityProfession;
import com.thoriuslight.professionsmod.profession.capabilities.IProfession;
import com.thoriuslight.professionsmod.profession.capabilities.ProfessionTypes.profession;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class SkillTree extends AbstractGui{
	private final Minecraft minecraft;
	private int scrollX;
	private int scrollY;
	private int minX = Integer.MAX_VALUE;
	private int minY = Integer.MAX_VALUE;
	private int maxX = Integer.MIN_VALUE;
	private int maxY = Integer.MIN_VALUE;
	private boolean centered;
	private static final ResourceLocation WIDGETS = new ResourceLocation("textures/gui/advancements/widgets.png");
    private SkillContainer skills;
    private final profession Prof;
    private float fade;
    private final Map<Skill, SkillGui> skillGuis = Maps.newLinkedHashMap();
	
	public SkillTree(Minecraft minecraft){
		this.minecraft = minecraft;
		if (!this.centered) {
			this.scrollX = MathHelper.floor((double)(117 - (this.maxX + this.minX) / 2));
			this.scrollY = MathHelper.floor((double)(56 - (this.maxY + this.minY) / 2));
			this.centered = true;
		}
		IProfession iProfession = this.minecraft.player.getCapability(CapabilityProfession.PROFESSION, null).orElseThrow(IllegalStateException::new);
		skills = new SkillContainer(iProfession.getSkillTree());
		this.Prof = iProfession.getProfession();
        for(Skill skill : Prof.getSkillList().getSkills()) {
        	skillGuis.put(skill, new SkillGui(minecraft, skill, 252));
        }
	}
	   
	@SuppressWarnings("deprecation")
	public void drawContents(MatrixStack matrixStack) {
        RenderSystem.pushMatrix();
        RenderSystem.enableDepthTest();
        RenderSystem.translatef(0.0F, 0.0F, 950.0F);
        RenderSystem.colorMask(false, false, false, false);
        fill(matrixStack, 4680, 2260, -4680, -2260, -16777216);
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.translatef(0.0F, 0.0F, -950.0F);
        RenderSystem.depthFunc(518);
        fill(matrixStack, 234, 113, 0, 0, -16777216);
        RenderSystem.depthFunc(515);
        ResourceLocation resourcelocation = new ResourceLocation("textures/block/dirt.png");
        this.minecraft.getTextureManager().bind(resourcelocation);
        int k = scrollX % 16;
        int l = scrollY % 16;
        for(int i1 = -1; i1 <= 15; ++i1) {
            for(int j1 = -1; j1 <= 8; ++j1) {
               blit(matrixStack, k + 16 * i1, l + 16 * j1, 0.0F, 0.0F, 16, 16, 16, 16);
            }
        }
        //draw skills
        int id = 0;
	    //RenderSystem.color4f(0.5F, 0.5F, 0.5F, 1.0F);
	    //RenderSystem.enableBlend();
        for(Skill skill : Prof.getSkillList().getSkills()) {
            if(skills.getSkill(skill.getId()))
            	id = 0;
            else
            	id = 1;
            this.minecraft.getTextureManager().bind(WIDGETS);
        	this.blit(matrixStack, scrollX - 9 + skill.getPosX(), scrollY - 12 + skill.getPosY(), 0, 128 + id * 26, 26, 26);
        	this.minecraft.getItemRenderer().renderAndDecorateItem((LivingEntity)null, skill.getIcon(), scrollX - 4  + skill.getPosX(), scrollY - 7  + skill.getPosY());
        }
	    //RenderSystem.disableBlend();
        
        RenderSystem.depthFunc(518);
        RenderSystem.translatef(0.0F, 0.0F, -950.0F);
        RenderSystem.colorMask(false, false, false, false);
        fill(matrixStack, 4680, 2260, -4680, -2260, -16777216);
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.translatef(0.0F, 0.0F, 950.0F);
        RenderSystem.depthFunc(515);
        RenderSystem.popMatrix();
	}
	
	@SuppressWarnings("deprecation")
	public void drawToolTips(MatrixStack matrixStack, int p_192991_1_, int p_192991_2_, int p_192991_3_, int p_192991_4_) {
	      RenderSystem.pushMatrix();
	      RenderSystem.translatef(0.0F, 0.0F, 200.0F);
	      fill(matrixStack, 0, 0, 234, 113, MathHelper.floor(this.fade * 255.0F) << 24);
	      boolean flag = false;
	      int i = MathHelper.floor(this.scrollX);
	      int j = MathHelper.floor(this.scrollY);
	      if (p_192991_1_ > 0 && p_192991_1_ < 234 && p_192991_2_ > 0 && p_192991_2_ < 113) {
	          for(Skill skill : Prof.getSkillList().getSkills()) {
	        	  if(skill.isMouseOver(i, j, p_192991_1_, p_192991_2_)) {
		        	  flag = true;
		        	  this.skillGuis.get(skill).drawHover(matrixStack, i, j, this.fade, p_192991_3_, p_192991_4_, skills.getSkill(skill.getId()));
	        	  }
	          }
	      }
	      RenderSystem.popMatrix();
	      if (flag) {
	          this.fade = MathHelper.clamp(this.fade + 0.02F, 0.0F, 0.3F);
	       } else {
	          this.fade = MathHelper.clamp(this.fade - 0.04F, 0.0F, 1.0F);
	       }
	}
	

	
	public void mouseClicked(int mouse_x, int mouse_y, AtomicInteger points, Screen screen){
		for(Skill skill : Prof.getSkillList().getSkills()) {
			int x_coord = scrollX - 12 + 3 + skill.getPosX();
			int y_coord = scrollY - 12 + skill.getPosY();
			if(mouse_x > x_coord && mouse_x < x_coord + 24 && mouse_y > y_coord && mouse_y < y_coord + 24) {
				IProfession iProfession = this.minecraft.player.getCapability(CapabilityProfession.PROFESSION, null).orElseThrow(IllegalStateException::new);
				if(iProfession.setSkillTalent(skill.getId(), true, this.minecraft.player)) {
					points.set(iProfession.getSkillPoints());
					this.skills.setSkill(skill.getId(), true);
				}
				else if(skills.getSkill(skill.getId())){
					Minecraft.getInstance().setScreen(new SkillGuideScreen(screen, skill));
				}
			}
		}
	}
}
