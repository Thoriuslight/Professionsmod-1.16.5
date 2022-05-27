package com.thoriuslight.professionsmod.client.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.thoriuslight.professionsmod.ProfessionsMod;
import com.thoriuslight.professionsmod.profession.skill.Skill;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.screen.IngameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.LanguageMap;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

@SuppressWarnings("unused")
public class SkillGuideScreen extends Screen{
	private static final ResourceLocation WINDOW = new ResourceLocation(ProfessionsMod.MODID, "textures/gui/skillguide.png");
	private static final ResourceLocation BUTTON = new ResourceLocation("textures/gui/server_selection.png");
	private static final int TEXT_FIELD_WIDTH = 120;
	private static final int TEXT_FIELD_HEIGHT = 115;
	protected final Screen parentScreen;
	protected final Skill skill;
	private List<IReorderingProcessor> text;
	private double scrollAmount;
	private double scrollHeight;
	protected boolean canScroll = false;
	protected boolean scrolling = false;
	public SkillGuideScreen(Screen parentScreen, Skill skill) {
		super(new TranslationTextComponent("screen.skillguide.title"));
		this.parentScreen = parentScreen;
		this.skill = skill;
		this.scrollAmount = 0.D;
		this.scrollHeight = 0.D;
		if(skill.getDisplayName() instanceof TranslationTextComponent) {
			String location = "skills/en_us/" + ((TranslationTextComponent)skill.getDisplayName()).getKey().replace("skill.professionsmod.", "") + ".txt";
			ResourceLocation resource = new ResourceLocation(ProfessionsMod.MODID, location);
			InputStream in = null;
			try {
				in = Minecraft.getInstance().getResourceManager().getResource(resource).getInputStream();
			} catch (IOException e) {}
			if(in != null) {
				text = new ArrayList<IReorderingProcessor>();
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				List<String> s = reader.lines().collect(Collectors.toList());
		        for(int i=0; i < s.size(); ++i) {
		        	text.addAll(Minecraft.getInstance().font.split(new StringTextComponent(s.get(i)), TEXT_FIELD_WIDTH-5));
		        }
			}
			if(this.text != null) {
				if(this.text.size() * (Minecraft.getInstance().font.lineHeight + 2) > TEXT_FIELD_HEIGHT) {
					this.scrollHeight = (double)TEXT_FIELD_HEIGHT * 50.D/(this.text.size() * (Minecraft.getInstance().font.lineHeight + 2));
					this.canScroll = true;
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void render(MatrixStack matrixStack, int p_render_1_, int p_render_2_, float p_render_3_) {
		int i = (this.width - 176) / 2;
		int j = (this.height - 144) / 2;
		if(this.scrolling) {
			int mY = p_render_2_ - j;
			this.scrollAmount = Math.max(0, Math.min(mY - 18 - 7, 105));
		}
		//Rendering
		this.renderBackground(matrixStack);
		this.renderInside(matrixStack, p_render_1_, p_render_2_, i, j);
		this.renderWindow(matrixStack, i, j);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.enableBlend();
		this.minecraft.getTextureManager().bind(BUTTON);
		int k = 0;
		if(p_render_1_ > i + 5 && p_render_1_ < i + 19 && p_render_2_ > j + 5 && p_render_2_ < j + 27)
			k = 1;
		this.blit(matrixStack, i + 5, j, 32, k*32, 32, 32);
		//Scroll
		this.minecraft.getTextureManager().bind(WINDOW);
		int f = 0;
		if(!this.canScroll)
			f = 1;
		this.blit(matrixStack, i + 156, j + 18 + (int) Math.round(this.scrollAmount), 176 + f*12, 0, 12, 15);
		RenderSystem.disableBlend();
		super.render(matrixStack, p_render_1_, p_render_2_, p_render_3_);
	}
	@SuppressWarnings("deprecation")
	private void renderInside(MatrixStack matrixStack, int p_191936_1_, int p_191936_2_, int p_191936_3_, int p_191936_4_) {
        RenderSystem.pushMatrix();
        RenderSystem.translatef((float)(p_191936_3_ - 92), (float)(p_191936_4_ - 36), 0.0F);
        
        RenderSystem.pushMatrix();
        RenderSystem.enableDepthTest();
        RenderSystem.translatef(0.0F, 0.0F, 950.0F);
        RenderSystem.colorMask(false, false, false, false);
        fill(matrixStack, 4680, 2260, -4680, -2260, -16777216);
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.translatef(0.0F, 0.0F, -950.0F);
        RenderSystem.depthFunc(518);
    	fill(matrixStack, 121, 40, 241, 172, -3750202);
        RenderSystem.depthFunc(515);

        if(this.text != null) {
            int d = MathHelper.floor((float)(this.text.size() * (Minecraft.getInstance().font.lineHeight + 2) - TEXT_FIELD_HEIGHT)/105.f * (float)this.scrollAmount);
        	for(int i=0; i < this.text.size(); ++i)
        		this.font.draw(matrixStack, this.text.get(i), 125, 58 + (i) * (this.font.lineHeight +2) -d, 4210752);
        }
		RenderSystem.depthFunc(518);
		RenderSystem.translatef(0.0F, 0.0F, -950.0F);
		RenderSystem.colorMask(false, false, false, false);
		fill(matrixStack, 4680, 2260, -4680, -2260, -16777216);
		RenderSystem.colorMask(true, true, true, true);
		RenderSystem.translatef(0.0F, 0.0F, 950.0F);
		RenderSystem.depthFunc(515);
		RenderSystem.popMatrix();
	      
        RenderSystem.popMatrix();
        RenderSystem.depthFunc(515);
        RenderSystem.disableDepthTest();
	}
	@SuppressWarnings("deprecation")
	private void renderWindow(MatrixStack matrixStack, int xC, int yC){
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		//draw background
		RenderSystem.enableBlend();
		this.minecraft.getTextureManager().bind(WINDOW);
		this.blit(matrixStack, xC, yC, 0, 0, 176, 144);
		RenderSystem.disableBlend();
		//draw skill name
		ITextComponent skillName = this.skill.getDisplayName();
		int skillWidth = this.font.width(skillName)/2;
	    this.font.draw(matrixStack, skillName, (float)(xC + 88 - skillWidth), (float)(yC + 6), 4210752);
	}
	
	@Override
	public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
		//Left click
		if (p_mouseClicked_5_ == 0) {
			//translate mouse position
			int mX = MathHelper.floor(p_mouseClicked_1_) - (this.width - 176) / 2;
			int mY = MathHelper.floor(p_mouseClicked_3_) - (this.height - 144) / 2;
			//Back
			if(mX > 5 && mX < 19 && mY > 5 && mY < 27) {
	            this.playDownSound(Minecraft.getInstance().getSoundManager());
	            this.minecraft.setScreen(this.parentScreen);
			}
			//Scroll
			if(this.canScroll) {
				if(mX > 156 && mX < 168 && mY > 18 && mY < 139) {
					this.playDownSound(Minecraft.getInstance().getSoundManager());
					this.scrollAmount = Math.max(0, Math.min(mY - 18 - 7, 105));
					this.scrolling = true;
				}
			}
		}
		return super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
	}
	@Override
	public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) {
		if(this.canScroll) {
			if (p_mouseReleased_5_ == 0 && this.scrolling) {
				scrolling = false;
			}
		}
		return super.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
	}
	@Override
	public boolean mouseScrolled(double p_231043_1_, double p_231043_3_, double p_231043_5_) {
		if(canScroll)
		this.setScrollAmount(this.getScrollAmount() - p_231043_5_ * scrollHeight / 2.0D);
		return true;
	}
	public double getScrollAmount() {
		return this.scrollAmount;
	}
	public void setScrollAmount(double p_230932_1_) {
		this.scrollAmount = MathHelper.clamp(p_230932_1_, 0.0D, (double)this.getMaxScroll());
	}

	public int getMaxScroll() {
		return 105;
	}
	public void playDownSound(SoundHandler p_playDownSound_1_) {
		p_playDownSound_1_.play(SimpleSound.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
	}
    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parentScreen);
    }
}
