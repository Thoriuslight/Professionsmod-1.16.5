package com.thoriuslight.professionsmod.client.tileentity.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.thoriuslight.professionsmod.tileentity.CastingBasinTileEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;

public class CastingBasinRenderer  extends TileEntityRenderer<CastingBasinTileEntity> {

	public CastingBasinRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	@Override
	public void render(CastingBasinTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
	    //Item
		ItemStack item = tileEntityIn.getItem();
		if(!item.isEmpty()) {
			matrixStackIn.pushPose();
			matrixStackIn.translate(0.5D, 0.21875D + 0.08D, 0.5D);
			matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(90.f));
			matrixStackIn.scale(0.75f,  0.75f, 0.75f);
			//Rendering
			renderItem(item, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
			matrixStackIn.popPose();
		}	
	}
	private void renderItem(ItemStack stack, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		Minecraft.getInstance().getItemRenderer().renderStatic(stack, TransformType.FIXED, combinedLightIn, combinedOverlayIn/*OverlayTexture.NO_OVERLAY*/, matrixStackIn, bufferIn);
	}
}
