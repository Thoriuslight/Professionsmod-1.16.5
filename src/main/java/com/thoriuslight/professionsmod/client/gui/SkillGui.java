package com.thoriuslight.professionsmod.client.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.thoriuslight.professionsmod.profession.skill.Skill;

import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.CharacterManager;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.LanguageMap;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextComponentUtils;

@SuppressWarnings("unused")
public class SkillGui extends AbstractGui{
	private static final ResourceLocation WIDGETS = new ResourceLocation("textures/gui/advancements/widgets.png");
	private static final int[] TEST_SPLIT_OFFSETS = new int[]{0, 10, -10, 25, -25};
	private IReorderingProcessor title;
	private List<IReorderingProcessor> description;
	private List<IReorderingProcessor> requirement;
	private final Minecraft minecraft;
	private final Skill skill;
	private static final Pattern PATTERN = Pattern.compile("(.+) \\S+");
	private final int x;
	private final int y;
	private final int width;
	private final int parentScreenWidth;
	
	public SkillGui(Minecraft minecraft, Skill skill, int parentScreenWidth) {
		this.minecraft = minecraft;
		this.skill = skill;
	    this.title = LanguageMap.getInstance().getVisualOrder(minecraft.font.substrByWidth(skill.getDisplayName(), 163));
	    this.x = MathHelper.floor(skill.getPosX() - 12);
	    this.y = MathHelper.floor(skill.getPosY() - 12);
	    int i = skill.getRequiredPoints();
	    int j = String.valueOf(i).length();
	    int k = minecraft.font.width("  ") + minecraft.font.width("0") * j + minecraft.font.width("P");
	    int l = 29 + minecraft.font.width(this.title) + k;
		this.description = LanguageMap.getInstance().getVisualOrder(this.findOptimalLines(TextComponentUtils.mergeStyles(skill.getDescription().copy(), Style.EMPTY.withColor((FrameType.TASK).getChatColor())), l));//this.findOptimalLines(new TextComponent(s), l);
		this.requirement = new ArrayList<>();
		if(skill.getRequirement().size() != 0) {
			this.requirement.add(LanguageMap.getInstance().getVisualOrder(minecraft.font.substrByWidth(new StringTextComponent("requirement:"), 163)));
			for(Skill sk : skill.getRequirement()) {
				this.requirement.add(LanguageMap.getInstance().getVisualOrder(minecraft.font.substrByWidth(sk.getDisplayName(), 163)));
			}
			for(IReorderingProcessor ireorderingprocessor : this.requirement) {
	    		l = Math.max(l, minecraft.font.width(ireorderingprocessor));
	 		}
		}
		for(IReorderingProcessor ireorderingprocessor : this.description) {
    		l = Math.max(l, minecraft.font.width(ireorderingprocessor));
 		}
 		this.width = l + 3 + 5;
		this.parentScreenWidth = parentScreenWidth;
	}
	private static float getMaxWidth(CharacterManager p_238693_0_, List<ITextProperties> p_238693_1_) {
		return (float)p_238693_1_.stream().mapToDouble(p_238693_0_::stringWidth).max().orElse(0.0D);
	}
	private List<ITextProperties> findOptimalLines(ITextComponent text, int num) {
		CharacterManager charactermanager = this.minecraft.font.getSplitter();
	    List<ITextProperties> list = null;
	    float f = Float.MAX_VALUE;
	    for(int i : TEST_SPLIT_OFFSETS) {
	    	List<ITextProperties> list1 = charactermanager.splitLines(text, num - i, Style.EMPTY);
	        float f1 = Math.abs(getMaxWidth(charactermanager, list1) - (float)num);
	        if (f1 <= 10.0F) {
	        	return list1;
	        }
	        if (f1 < f) {
	        	f = f1;
	            list = list1;
	       }
	    }
	    return list;
	}
	@SuppressWarnings("deprecation")
	public void drawHover(MatrixStack matrixStack, int p_191821_1_, int p_191821_2_, float fade, int p_191821_4_, int p_191821_5_, boolean isMastered) {
		boolean flag = p_191821_4_ + p_191821_1_ + this.x + this.width + 26 >= this.parentScreenWidth;
		int lowerSize = this.description.size() * 9;
	    if(!isMastered) {
	    	lowerSize += this.requirement.size() * 9;
	    }
		boolean flag1 = 113 - p_191821_2_ - this.y - 26 <= 6 + lowerSize;
	    int j = MathHelper.floor((float)this.width) - 2;
	    int k = this.width - j;
	    this.minecraft.getTextureManager().bind(WIDGETS);
	    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
	    RenderSystem.enableBlend();
	    int l = p_191821_2_ + this.y;
	    int i1;
	    if (flag) {
	    	i1 = p_191821_1_ + this.x - this.width + 26 + 6;
	    } else {
	    	i1 = p_191821_1_ + this.x;
	    }
	    int j1 = 32 + lowerSize;
	    //Text background
	    if (!this.description.isEmpty()) {
	    	if (flag1) {
	    		this.render9Sprite(matrixStack, i1, l + 26 - j1, this.width, j1, 10, 200, 26, 0, 52);
	    	} else {
	    		this.render9Sprite(matrixStack, i1, l, this.width, j1, 10, 200, 26, 0, 52);
	        }
	    }
	    int activated = 1;
	    if(isMastered) {
	    	activated = 0;
	    }
	    //Title background
	    this.blit(matrixStack, i1, l, 0, activated * 26, j, 26);
	    this.blit(matrixStack, i1 + j, l, 200 - k, activated * 26, k, 26);
	    //Item background
	    this.blit(matrixStack, p_191821_1_ + this.x + 3, p_191821_2_ + this.y, 0, 128 + activated * 26, 26, 26);
	    //Texts
		String s = String.valueOf(this.skill.getRequiredPoints()) + "P";
		int i = this.minecraft.font.width(s);
	    if (flag) {	//fits

	    	this.minecraft.font.drawShadow(matrixStack, this.title, (float)(i1 + 5), (float)(p_191821_2_ + this.y + 9), -1);
	        if (s != null) {
	        	this.minecraft.font.drawShadow(matrixStack, s, (float)(p_191821_1_ + this.x - i), (float)(p_191821_2_ + this.y + 9), -1);
	        }
	    } else {	//doesn't fit
	          this.minecraft.font.drawShadow(matrixStack, this.title, (float)(p_191821_1_ + this.x + 32), (float)(p_191821_2_ + this.y + 9), -1);
	          if (s != null) {
	             this.minecraft.font.drawShadow(matrixStack, s, (float)(p_191821_1_ + this.x + this.width - i - 5), (float)(p_191821_2_ + this.y + 9), -1);
	          }
	       }
	      
	      if (flag1) {
	          for(int k1 = 0; k1 < this.description.size(); ++k1) {
	             this.minecraft.font.draw(matrixStack, this.description.get(k1), (float)(i1 + 5), (float)(l + 26 - j1 + 7 + k1 * 9), -5592406);
	          }
	          if(!isMastered)
	          for(int k1 = 0; k1 < this.requirement.size(); ++k1) {
		             this.minecraft.font.draw(matrixStack, this.requirement.get(k1), (float)(i1 + 5), (float)(l + 26 - j1 + 7 + this.description.size() * 9 + k1 * 9), -32512);
		      }
	       } else {
	          for(int l1 = 0; l1 < this.description.size(); ++l1) {
	             this.minecraft.font.draw(matrixStack, this.description.get(l1), (float)(i1 + 5), (float)(p_191821_2_ + this.y + 9 + 17 + l1 * 9), -5592406);
	          }
	          if(!isMastered)
	          for(int l1 = 0; l1 < this.requirement.size(); ++l1) {
		             this.minecraft.font.draw(matrixStack, this.requirement.get(l1), (float)(i1 + 5), (float)(p_191821_2_ + this.y + 9 + 17 + this.description.size() * 9 + l1 * 9), -32512);
		          }
	       }
	      
	      
	      this.minecraft.getItemRenderer().renderAndDecorateItem((LivingEntity)null, this.skill.getIcon(), p_191821_1_ + this.x + 8, p_191821_2_ + this.y + 5);
	}
	
	   protected void render9Sprite(MatrixStack matrixStack, int p_192994_1_, int p_192994_2_, int p_192994_3_, int p_192994_4_, int p_192994_5_, int p_192994_6_, int p_192994_7_, int p_192994_8_, int p_192994_9_) {
		      this.blit(matrixStack, p_192994_1_, p_192994_2_, p_192994_8_, p_192994_9_, p_192994_5_, p_192994_5_);
		      this.renderRepeating(matrixStack, p_192994_1_ + p_192994_5_, p_192994_2_, p_192994_3_ - p_192994_5_ - p_192994_5_, p_192994_5_, p_192994_8_ + p_192994_5_, p_192994_9_, p_192994_6_ - p_192994_5_ - p_192994_5_, p_192994_7_);
		      this.blit(matrixStack, p_192994_1_ + p_192994_3_ - p_192994_5_, p_192994_2_, p_192994_8_ + p_192994_6_ - p_192994_5_, p_192994_9_, p_192994_5_, p_192994_5_);
		      this.blit(matrixStack, p_192994_1_, p_192994_2_ + p_192994_4_ - p_192994_5_, p_192994_8_, p_192994_9_ + p_192994_7_ - p_192994_5_, p_192994_5_, p_192994_5_);
		      this.renderRepeating(matrixStack, p_192994_1_ + p_192994_5_, p_192994_2_ + p_192994_4_ - p_192994_5_, p_192994_3_ - p_192994_5_ - p_192994_5_, p_192994_5_, p_192994_8_ + p_192994_5_, p_192994_9_ + p_192994_7_ - p_192994_5_, p_192994_6_ - p_192994_5_ - p_192994_5_, p_192994_7_);
		      this.blit(matrixStack, p_192994_1_ + p_192994_3_ - p_192994_5_, p_192994_2_ + p_192994_4_ - p_192994_5_, p_192994_8_ + p_192994_6_ - p_192994_5_, p_192994_9_ + p_192994_7_ - p_192994_5_, p_192994_5_, p_192994_5_);
		      this.renderRepeating(matrixStack, p_192994_1_, p_192994_2_ + p_192994_5_, p_192994_5_, p_192994_4_ - p_192994_5_ - p_192994_5_, p_192994_8_, p_192994_9_ + p_192994_5_, p_192994_6_, p_192994_7_ - p_192994_5_ - p_192994_5_);
		      this.renderRepeating(matrixStack, p_192994_1_ + p_192994_5_, p_192994_2_ + p_192994_5_, p_192994_3_ - p_192994_5_ - p_192994_5_, p_192994_4_ - p_192994_5_ - p_192994_5_, p_192994_8_ + p_192994_5_, p_192994_9_ + p_192994_5_, p_192994_6_ - p_192994_5_ - p_192994_5_, p_192994_7_ - p_192994_5_ - p_192994_5_);
		      this.renderRepeating(matrixStack, p_192994_1_ + p_192994_3_ - p_192994_5_, p_192994_2_ + p_192994_5_, p_192994_5_, p_192994_4_ - p_192994_5_ - p_192994_5_, p_192994_8_ + p_192994_6_ - p_192994_5_, p_192994_9_ + p_192994_5_, p_192994_6_, p_192994_7_ - p_192994_5_ - p_192994_5_);
		   }

		   protected void renderRepeating(MatrixStack matrixStack, int p_192993_1_, int p_192993_2_, int p_192993_3_, int p_192993_4_, int p_192993_5_, int p_192993_6_, int p_192993_7_, int p_192993_8_) {
		      for(int i = 0; i < p_192993_3_; i += p_192993_7_) {
		         int j = p_192993_1_ + i;
		         int k = Math.min(p_192993_7_, p_192993_3_ - i);

		         for(int l = 0; l < p_192993_4_; l += p_192993_8_) {
		            int i1 = p_192993_2_ + l;
		            int j1 = Math.min(p_192993_8_, p_192993_4_ - l);
		            this.blit(matrixStack, j, i1, p_192993_5_, p_192993_6_, k, j1);
		         }
		      }

		   }
}
