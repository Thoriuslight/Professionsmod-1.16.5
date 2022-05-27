package com.thoriuslight.professionsmod.client.tileentity.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.thoriuslight.professionsmod.tileentity.ForgeTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ForgeRenderer extends TileEntityRenderer<ForgeTileEntity>{
	public ForgeRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	@Override
	public void render(ForgeTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		ItemStack item = tileEntityIn.getItem();
		if(!item.isEmpty()) {
			matrixStackIn.pushPose();
			matrixStackIn.scale(1.f,  1.f, 1.f);
			matrixStackIn.translate(0.5D, 0.46875D, 0.5D);
			matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(90.f));
			renderItem(item, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
			matrixStackIn.popPose();
		}
	}


	private void renderItem(ItemStack stack, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		Minecraft.getInstance().getItemRenderer().renderStatic(stack, TransformType.FIXED, combinedLightIn, combinedOverlayIn/*OverlayTexture.NO_OVERLAY*/, matrixStackIn, bufferIn);
	}
}
