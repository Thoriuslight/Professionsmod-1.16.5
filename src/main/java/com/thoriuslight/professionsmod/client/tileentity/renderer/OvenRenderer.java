package com.thoriuslight.professionsmod.client.tileentity.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.thoriuslight.professionsmod.ProfessionsMod;
import com.thoriuslight.professionsmod.block.OvenControllerBlock;
import com.thoriuslight.professionsmod.tileentity.OvenControllerTileEntity;

import net.minecraft.block.BlockState;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class OvenRenderer extends TileEntityRenderer<OvenControllerTileEntity>{
	@SuppressWarnings("deprecation")
	public static final RenderMaterial OVEN_TEXTURE = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS, new ResourceLocation(ProfessionsMod.MODID, "models/oven"));
	@SuppressWarnings("deprecation")
	public static final RenderMaterial OVEN_LIT_TEXTURE = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS, new ResourceLocation(ProfessionsMod.MODID, "models/oven_lit"));
	private final ModelRenderer modelRenderer = new ModelRenderer(160, 80, 0, 0);
	
	public OvenRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
		modelRenderer.addBox(-8.f, -16.f, -16.f, 32.f, 32.f, 48.f);
		modelRenderer.addBox(-16.f, -16.f, -8.f, 8.f, 32.f, 32.f);
		modelRenderer.addBox(24.f, -16.f, -8.f, 8.f, 32.f, 32.f);
		modelRenderer.addBox(-8.f, 16.f, -8.f, 32.f, 16.f, 32.f);
	}

	@Override
	public void render(OvenControllerTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		BlockState state = tileEntityIn.getBlockState();
        int i = WorldRenderer.getLightColor(tileEntityIn.getLevel(), tileEntityIn.getBlockPos().offset(0,1,0));
		matrixStackIn.pushPose();
		matrixStackIn.translate(0.5D, 0.0D, 0.5D);
		matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(tileEntityIn.getDirection()));
		matrixStackIn.translate(-0.5D, 0.0D, -0.5D);
	    IVertexBuilder ivertexbuilder;
		if(OvenControllerBlock.isLit(state)) {
		    ivertexbuilder = OVEN_LIT_TEXTURE.buffer(bufferIn, RenderType::entitySolid);
		} 
		else {
		    ivertexbuilder = OVEN_TEXTURE.buffer(bufferIn, RenderType::entitySolid);
		}
		this.modelRenderer.render(matrixStackIn, ivertexbuilder, i, combinedOverlayIn);
		matrixStackIn.popPose();
	}
}
