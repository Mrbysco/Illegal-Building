package com.mrbysco.illegalbuilding.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.illegalbuilding.entity.ImpossibleFallingBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.FallingBlockRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

public class ImpossibleFallingBlockRenderer extends EntityRenderer<ImpossibleFallingBlockEntity, FallingBlockRenderState> {
	private final BlockRenderDispatcher dispatcher;

	public ImpossibleFallingBlockRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.5F;
		this.dispatcher = context.getBlockRenderDispatcher();
	}

	@Override
	public void render(FallingBlockRenderState renderState, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		BlockState blockstate = renderState.blockState;
		if (blockstate.getRenderShape() == RenderShape.MODEL) {
			poseStack.pushPose();
			poseStack.translate(-0.5, 0.0, -0.5);
			var model = this.dispatcher.getBlockModel(blockstate);
			for (var renderType : model.getRenderTypes(blockstate, RandomSource.create(blockstate.getSeed(renderState.startBlockPos)), net.neoforged.neoforge.client.model.data.ModelData.EMPTY))
				this.dispatcher
						.getModelRenderer()
						.tesselateBlock(
								renderState,
								this.dispatcher.getBlockModel(blockstate),
								blockstate,
								renderState.blockPos,
								poseStack,
								bufferSource.getBuffer(net.neoforged.neoforge.client.RenderTypeHelper.getMovingBlockRenderType(renderType)),
								false,
								RandomSource.create(),
								blockstate.getSeed(renderState.startBlockPos),
								OverlayTexture.NO_OVERLAY,
								net.neoforged.neoforge.client.model.data.ModelData.EMPTY,
								renderType
						);
			poseStack.popPose();
			super.render(renderState, poseStack, bufferSource, packedLight);
		}
	}

	@Override
	public FallingBlockRenderState createRenderState() {
		return new FallingBlockRenderState();
	}

	@Override
	public void extractRenderState(ImpossibleFallingBlockEntity fallingBlockEntity, FallingBlockRenderState renderState, float partialTick) {
		super.extractRenderState(fallingBlockEntity, renderState, partialTick);
		BlockPos blockpos = BlockPos.containing(fallingBlockEntity.getX(), fallingBlockEntity.getBoundingBox().maxY, fallingBlockEntity.getZ());
		renderState.startBlockPos = fallingBlockEntity.getStartPos();
		renderState.blockPos = blockpos;
		renderState.blockState = fallingBlockEntity.getBlockState();
		renderState.biome = fallingBlockEntity.level().getBiome(blockpos);
		renderState.level = fallingBlockEntity.level();
	}
}
