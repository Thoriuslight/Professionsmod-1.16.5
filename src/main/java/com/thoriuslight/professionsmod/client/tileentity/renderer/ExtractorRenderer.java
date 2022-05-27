package com.thoriuslight.professionsmod.client.tileentity.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.thoriuslight.professionsmod.tileentity.ExtractorTileEntity;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;

public class ExtractorRenderer extends TileEntityRenderer<ExtractorTileEntity>{
	private static final ResourceLocation FLUID_TEXTURE = new ResourceLocation("textures/block/water_still.png");
	private static final RenderType RENDER_TYPE = RenderType.entitySolid(FLUID_TEXTURE);
	//private BlockRendererDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
	public ExtractorRenderer(TileEntityRendererDispatcher p_i226006_1_) {
		super(p_i226006_1_);
	}

	@Override
	public void render(ExtractorTileEntity tile, float p_225616_2_, MatrixStack matrix, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		//render model
		//BlockState state = tile.getBlockState();
        //World world = tile.getLevel();
        //BlockPos pos = tile.getBlockPos();
        //IModelData model = blockRenderer.getBlockModel(state).getModelData(world, pos, state, ModelDataManager.getModelData(world, pos));
       // blockRenderer.getModelRenderer().tesselateBlock(world, this.blockRenderer.getBlockModel(state), state, pos, matrix, bufferIn.getBuffer(RenderTypeLookup.getRenderType(state, false)), false, new Random(), state.getSeed(pos), combinedOverlayIn);
        //blockRenderer.renderBlock(state, matrix, bufferIn, combinedLightIn, combinedOverlayIn, model);
        //render fluid
        int fluid = tile.getFluidAmount();
        if(fluid > 0) {
        	long i = tile.getLevel().getGameTime();
	    	int aT = (((int)(i % 64))/2);
			IVertexBuilder vertexBuilder = bufferIn.getBuffer(RENDER_TYPE);
			renderFluid(vertexBuilder, matrix, combinedOverlayIn, combinedLightIn, ((float)fluid)/tile.getCapacity(), aT);
        }
	}
	public void renderFluid(IVertexBuilder vertexBuilder, MatrixStack matrix, int combinedOverlayIn, int combinedLightIn, float level, int animationTick) {
		renderTopFace(vertexBuilder, matrix.last().pose(), combinedOverlayIn, combinedLightIn, level, animationTick);
		matrix.translate(0.5D, 0.0D, 0.5D);
		float y = 0.8125f + 0.625f * level;
		float v1 = 0.f + 0.03125f * animationTick;
		float v2 = 0.01953125f * level + 0.03125f * animationTick;
		renderSideFace(vertexBuilder, matrix.last().pose(), combinedOverlayIn, combinedLightIn, y, v1, v2, animationTick);
		for(int i = 0; i < 3; ++i) {
			matrix.mulPose(Vector3f.YP.rotationDegrees(90.f));
			renderSideFace(vertexBuilder, matrix.last().pose(), combinedOverlayIn, combinedLightIn, y, v1, v2, animationTick);
		}
	}
	public void renderTopFace(IVertexBuilder vertexBuilder, Matrix4f matrix, int combinedOverlayIn, int combinedLightIn, float level, int animationTick) {
		float y = 0.8125f + 0.625f * level;
		float v1 = 0.f + 0.03125f * animationTick;
		float v2 = 0.015625f + 0.03125f * animationTick;
		vertexBuilder.vertex(matrix, 0.255f, y, 0.255f).color(0.016F, 0.102F, 0.0F, 1.0F).uv(0.0f, v2).overlayCoords(combinedOverlayIn).uv2(combinedLightIn).normal(0, 1, 0).endVertex();
		vertexBuilder.vertex(matrix, 0.255f, y, 0.745f).color(0.016F, 0.102F, 0.0F, 1.0F).uv(0.5f, v2).overlayCoords(combinedOverlayIn).uv2(combinedLightIn).normal(0, 1, 0).endVertex();
		vertexBuilder.vertex(matrix, 0.745f, y, 0.745f).color(0.016F, 0.102F, 0.0F, 1.0F).uv(0.5f, v1).overlayCoords(combinedOverlayIn).uv2(combinedLightIn).normal(0, 1, 0).endVertex();
		vertexBuilder.vertex(matrix, 0.745f, y, 0.255f).color(0.016F, 0.102F, 0.0F, 1.0F).uv(0.0f, v1).overlayCoords(combinedOverlayIn).uv2(combinedLightIn).normal(0, 1, 0).endVertex();
	}
	public void renderSideFace(IVertexBuilder vertexBuilder, Matrix4f matrix, int combinedOverlayIn, int combinedLightIn, float y, float v1, float v2, int animationTick) {
		vertexBuilder.vertex(matrix, -0.245f, 0.8125f, 0.245f).color(0.016F, 0.102F, 0.0F, 1.0F).uv(0.0f, v2).overlayCoords(combinedOverlayIn).uv2(combinedLightIn).normal(0, 0, 1).endVertex();
		vertexBuilder.vertex(matrix, 0.245f, 0.8125f, 0.245f).color(0.016F, 0.102F, 0.0F, 1.0F).uv(0.5f, v2).overlayCoords(combinedOverlayIn).uv2(combinedLightIn).normal(0, 0, 1).endVertex();
		vertexBuilder.vertex(matrix, 0.245f, y, 0.245f).color(0.016F, 0.102F, 0.0F, 1.0F).uv(0.5f, v1).overlayCoords(combinedOverlayIn).uv2(combinedLightIn).normal(0, 0, 1).endVertex();
		vertexBuilder.vertex(matrix, -0.245f, y, 0.245f).color(0.016F, 0.102F, 0.0F, 1.0F).uv(0.0f, v1).overlayCoords(combinedOverlayIn).uv2(combinedLightIn).normal(0, 0, 1).endVertex();
	}
}
