package com.mrbysco.illegalbuilding.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.illegalbuilding.entity.ImpossibleFallingBlockEntity;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.FallingBlockRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

public class ImpossibleFallingBlockRenderer extends EntityRenderer<ImpossibleFallingBlockEntity, FallingBlockRenderState> {

	public ImpossibleFallingBlockRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.5F;
	}

	@Override
	public void submit(FallingBlockRenderState renderState, PoseStack poseStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
		BlockState blockstate = renderState.movingBlockRenderState.blockState;
		if (blockstate.getRenderShape() == RenderShape.MODEL) {
			poseStack.pushPose();
			poseStack.translate(-0.5, 0.0, -0.5);
			nodeCollector.submitMovingBlock(poseStack, renderState.movingBlockRenderState, renderState.outlineColor);
			poseStack.popPose();
			super.submit(renderState, poseStack, nodeCollector, cameraRenderState);
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
		renderState.movingBlockRenderState.randomSeedPos = fallingBlockEntity.getStartPos();
		renderState.movingBlockRenderState.blockPos = blockpos;
		renderState.movingBlockRenderState.blockState = fallingBlockEntity.getBlockState();
		if (fallingBlockEntity.level() instanceof ClientLevel clientLevel) {
			renderState.movingBlockRenderState.biome = clientLevel.getBiome(blockpos);
			renderState.movingBlockRenderState.cardinalLighting = clientLevel.cardinalLighting();
			renderState.movingBlockRenderState.lightEngine = clientLevel.getLightEngine();
		}
	}
}
