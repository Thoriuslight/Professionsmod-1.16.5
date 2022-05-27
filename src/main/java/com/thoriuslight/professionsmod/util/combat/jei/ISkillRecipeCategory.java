package com.thoriuslight.professionsmod.util.combat.jei;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.thoriuslight.professionsmod.ProfessionsMod;

import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;

public interface ISkillRecipeCategory<T> extends IRecipeCategory<T> {
	static final ResourceLocation WIDGETS = new ResourceLocation(ProfessionsMod.MODID, "textures/gui/skill_background.png");
	public static void blit(MatrixStack matrixStack, int x1, int y1, int z, float uvx1, float uvy1, int offset_x, int offset_y, int p_238464_8_, int p_238464_9_) {
		innerBlit(matrixStack, x1, x1 + offset_x, y1, y1 + offset_y, z, offset_x, offset_y, uvx1, uvy1, p_238464_9_, p_238464_8_);
	}
	static void innerBlit(MatrixStack matrixStack, int x1, int x2, int y1, int y2, int z, int offset_x, int offset_y, float uvx1, float uvy1, int p_238469_10_, int p_238469_11_) {
		innerBlit(matrixStack.last().pose(), x1, x2, y1, y2, z, (uvx1+0.0F)/(float)p_238469_10_, (uvx1+(float)offset_x)/(float)p_238469_10_, (uvy1 + 0.0F) / (float)p_238469_11_, (uvy1 + (float)offset_y) / (float)p_238469_11_);
	}
	@SuppressWarnings("deprecation")
	static void innerBlit(Matrix4f matrix, int x1, int x2, int y1, int y2, int z, float uvx1, float uvx2, float uvy1, float uvy2) {
		BufferBuilder bufferbuilder = Tessellator.getInstance().getBuilder();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.vertex(matrix, (float)x1, (float)y2, (float)z).uv(uvx1, uvy2).endVertex();
		bufferbuilder.vertex(matrix, (float)x2, (float)y2, (float)z).uv(uvx2, uvy2).endVertex();
		bufferbuilder.vertex(matrix, (float)x2, (float)y1, (float)z).uv(uvx2, uvy1).endVertex();
		bufferbuilder.vertex(matrix, (float)x1, (float)y1, (float)z).uv(uvx1, uvy1).endVertex();
		bufferbuilder.end();
		RenderSystem.enableAlphaTest();
		WorldVertexBufferUploader.end(bufferbuilder);
	}
	public static int Log2(int v) {
	    int r = 0xFFFF - v >> 31 & 0x10;
	    v >>= r;
	    int shift = 0xFF - v >> 31 & 0x8;
	    v >>= shift; 
	    r |= shift;
	    shift = 0xF - v >> 31 & 0x4;
	    v >>= shift;
	    r |= shift;
	    shift = 0x3 - v >> 31 & 0x2;
	    v >>= shift;
	    r |= shift;
	    r |= (v >> 1);
	    return r;
	}
}
