package com.thoriuslight.professionsmod.client.tileentity.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.thoriuslight.professionsmod.tileentity.StoneAnvilTileEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class StoneAnvilRenderer extends TileEntityRenderer<StoneAnvilTileEntity>{

	public StoneAnvilRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	@Override
	public void render(StoneAnvilTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		ItemStack item = tileEntityIn.getItem();
		if(!item.isEmpty()) {
			matrixStackIn.pushPose();
			if(item.getItem() instanceof BlockItem) {
				matrixStackIn.translate(0.5D, 1.25D, 0.5D);
			}
			else {
				matrixStackIn.translate(0.5D, 1.03125D, 0.5D);
			}
			matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(90.f));
	        int lightTop = WorldRenderer.getLightColor(tileEntityIn.getLevel(), tileEntityIn.getBlockPos().offset(0,1,0));
			renderItem(item, matrixStackIn, bufferIn, lightTop, combinedOverlayIn);
			matrixStackIn.popPose();
		}
	}


	private void renderItem(ItemStack stack, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		Minecraft.getInstance().getItemRenderer().renderStatic(stack, TransformType.FIXED, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
	}
}
