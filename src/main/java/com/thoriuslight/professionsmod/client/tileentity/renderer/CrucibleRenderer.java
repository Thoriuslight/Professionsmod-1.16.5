package com.thoriuslight.professionsmod.client.tileentity.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.thoriuslight.professionsmod.ProfessionsMod;
import com.thoriuslight.professionsmod.tileentity.CrucibleTileEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;

public class CrucibleRenderer extends TileEntityRenderer<CrucibleTileEntity>{
	private static final ResourceLocation MOLTEN_METAL_TEXTURE = new ResourceLocation("textures/block/lava_still.png");
	private static final RenderType RENDER_TYPE = RenderType.entitySolid(MOLTEN_METAL_TEXTURE);
	private static final ResourceLocation OVERLAY_TEXTURE = new ResourceLocation(ProfessionsMod.MODID, "textures/models/crucible_block_heat.png");
	private static final RenderType OVERLAY_RENDER_TYPE = RenderType.entityCutout(OVERLAY_TEXTURE);
	private static final ResourceLocation LAVA_FLOW_TEXTURE = new ResourceLocation(ProfessionsMod.MODID, "textures/blocks/lava_flow_animation.png");
	private static final RenderType FLOW_RENDER_TYPE = RenderType.entityCutout(LAVA_FLOW_TEXTURE);
	public CrucibleRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	@Override
	public void render(CrucibleTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		//Molten metal
		float fluid = tileEntityIn.getDynamicFluidAmount(partialTicks);
		if(fluid > 0) {
			float f = 0.6875f + fluid * 0.00727272727273f;
			Matrix4f matrix4f = matrixStackIn.last().pose();
		    Vector3f vector3f = new Vector3f(0.f, 1.f, 0.f);
		    vector3f.transform(matrixStackIn.last().normal());
			IVertexBuilder vertexBuilder = bufferIn.getBuffer(RENDER_TYPE);
			this.renderFace(tileEntityIn, f, tileEntityIn.getAnimationFrame(), combinedOverlayIn, combinedLightIn, matrix4f, vertexBuilder, vector3f);
		}
		//Oxygen
		int oxygen = tileEntityIn.getOxygen();
		boolean hasFuel = tileEntityIn.hasFuel();
		if(oxygen > 0 && hasFuel) {
			matrixStackIn.pushPose();
			matrixStackIn.translate(0.5D, 0.5D, 0.5D);
		    matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(tileEntityIn.getBlockDirection()));
			Matrix4f matrix4f = matrixStackIn.last().pose();
		    Vector3f vector3f = new Vector3f(1.f, 0.f, 0.f);
		    vector3f.transform(matrixStackIn.last().normal());
			IVertexBuilder vertexBuilder = bufferIn.getBuffer(OVERLAY_RENDER_TYPE);
			renderOverlay(matrix4f, vertexBuilder, combinedOverlayIn, combinedLightIn, vector3f.x(), vector3f.y(), vector3f.z(), (float) (oxygen/201 * 0.5f));
			matrixStackIn.popPose();
		}
	    //Item
		ItemStack item = tileEntityIn.getItem();
		if(!item.isEmpty()) {
			matrixStackIn.pushPose();
			matrixStackIn.scale(0.5f,  0.5f, 0.5f);
			double height = 2.0D;
			if(item.getItem() instanceof BlockItem)
				height = 1.75D;
			//Animation
			height += fluid * (0.0044444444444444444444444444444444444444444444444D + 0.0022222222222222222222222222222222222222222222222D * MathHelper.sin((tileEntityIn.getAnimationFrame() + partialTicks) * 0.08433805781449109365000384921556f));
			matrixStackIn.translate(1.0D, height, 1.0D);
			if(item.getItem() instanceof BlockItem)
				matrixStackIn.scale(1.5f,  1.5f, 1.5f);
			else
				matrixStackIn.mulPose(Vector3f.YP.rotationDegrees((tileEntityIn.getAnimationFrame() + partialTicks) * 4.8322147651006711409395973154362f));
			//Rendering
			renderItem(item, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
			matrixStackIn.popPose();
		}	
		//Casting
		if(tileEntityIn.getCooldown() != 0) {
			matrixStackIn.pushPose();
			matrixStackIn.translate(0.5D, 0.5D, 0.5D);
		    matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(tileEntityIn.getDirection()));
			Matrix4f matrix4f = matrixStackIn.last().pose();
		    Vector3f vector3f = new Vector3f(0.f, 0.f, -1.f);
		    vector3f.transform(matrixStackIn.last().normal());
			IVertexBuilder vertexBuilder = bufferIn.getBuffer(FLOW_RENDER_TYPE);
			renderDoubleFace(matrix4f, vertexBuilder, combinedOverlayIn, combinedLightIn, vector3f.x(), vector3f.y(), vector3f.z(), 60 - tileEntityIn.getCooldown());
			matrixStackIn.popPose();
		}
	}
	private void renderFace(CrucibleTileEntity tileEntityIn, float height, int animationTicks, int combinedOverlayIn, int combinedLightIn, Matrix4f matrix, IVertexBuilder iVertexBuilder, Vector3f vector3f) {
		animationTicks /= 2;	//Every 2 frames
		if(animationTicks > 19) {
			animationTicks = 38 - animationTicks;
		}
		float v1 = 0.25f/20.f + 1.f/20.f*animationTicks;
		float v2 = 0.75f/20.f + 1.f/20.f*animationTicks;
		this.renderFace(matrix, iVertexBuilder, height, v1, v2, combinedOverlayIn, combinedLightIn, vector3f.x(), vector3f.y(), vector3f.z());
	}
	private void renderFace(Matrix4f matrix, IVertexBuilder vertexBuilder, float height, float v1, float v2, int combinedOverlayIn, int combinedLightIn, float nX, float nY, float nZ) {
		vertexBuilder.vertex(matrix, 0.25F, height, 0.75F).color(1.0f, 1.0f, 1.0f, 1.0F).uv(0.25f, v2).overlayCoords(combinedOverlayIn).uv2(combinedLightIn).normal(nX, nY, nZ).endVertex();
		vertexBuilder.vertex(matrix, 0.75F, height, 0.75F).color(1.0f, 1.0f, 1.0f, 1.0F).uv(0.75f, v2).overlayCoords(combinedOverlayIn).uv2(combinedLightIn).normal(nX, nY, nZ).endVertex();
		vertexBuilder.vertex(matrix, 0.75F, height, 0.25F).color(1.0f, 1.0f, 1.0f, 1.0F).uv(0.75f, v1).overlayCoords(combinedOverlayIn).uv2(combinedLightIn).normal(nX, nY, nZ).endVertex();
		vertexBuilder.vertex(matrix, 0.25F, height, 0.25F).color(1.0f, 1.0f, 1.0f, 1.0F).uv(0.25f, v1).overlayCoords(combinedOverlayIn).uv2(combinedLightIn).normal(nX, nY, nZ).endVertex();
	}
	private void renderItem(ItemStack stack, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		Minecraft.getInstance().getItemRenderer().renderStatic(stack, TransformType.FIXED, combinedLightIn, combinedOverlayIn/*OverlayTexture.NO_OVERLAY*/, matrixStackIn, bufferIn);
	}

	private void renderDoubleFace(Matrix4f matrix, IVertexBuilder vertexBuilder, int combinedOverlayIn, int combinedLightIn, float nX, float nY, float nZ, int animationTicks) {
		animationTicks /= 2;
		float f = 0.971875f - 1.f/320.f * animationTicks;
		float f2 = f - 0.028125f;
		vertex(matrix, vertexBuilder, -0.09375f, 	0.0625f, -0.7f, 0.f, 		f2, combinedOverlayIn, combinedLightIn, nX, nY, nZ); 	//Top Left
		vertex(matrix, vertexBuilder, 0.09375f, 	0.0625f, -0.7f, 0.1875f, 	f2, combinedOverlayIn, combinedLightIn, nX, nY, nZ);	//Top Right
		vertex(matrix, vertexBuilder, 0.09375f, 	-0.5f, -0.7f, 	0.1875f, 	f, combinedOverlayIn, combinedLightIn, nX, nY, nZ); 	//Bottom Right
		vertex(matrix, vertexBuilder, -0.09375f, 	-0.5f, -0.7f, 	0.f, 		f, combinedOverlayIn, combinedLightIn, nX, nY, nZ); 	//Bottom Left
	}
	private void renderOverlay(Matrix4f matrix, IVertexBuilder vertexBuilder, int combinedOverlayIn, int combinedLightIn, float nX, float nY, float nZ, float mode) {
		vertex(matrix, vertexBuilder, -0.51f, 	-0.5f, -0.5f, 	0.0f + mode, 	1.0f, combinedOverlayIn, combinedLightIn, nX, nY, nZ); 	//1
		vertex(matrix, vertexBuilder, -0.51f, 	-0.5f, 0.5f, 	0.5f + mode, 	1.0f, combinedOverlayIn, combinedLightIn, nX, nY, nZ);	//2
		vertex(matrix, vertexBuilder, -0.51f, 	0.5f, 0.5f, 	0.5f + mode, 	0.0f, combinedOverlayIn, combinedLightIn, nX, nY, nZ); 	//3
		vertex(matrix, vertexBuilder, -0.51f, 	0.5f, -0.5f, 	0.0f + mode, 	0.0f, combinedOverlayIn, combinedLightIn, nX, nY, nZ); 	//4
	}
	private void vertex(Matrix4f matrix, IVertexBuilder vertexBuilder, float x, float y, float z, float u, float v, int combinedOverlayIn, int combinedLightIn, float nX, float nY, float nZ) {
		vertexBuilder.vertex(matrix, x, y, z).color(1.f, 1.f, 1.f, 1.f).uv(u, v).overlayCoords(combinedOverlayIn).uv2(combinedLightIn).normal(nX, nY, nZ).endVertex();
	}
}
