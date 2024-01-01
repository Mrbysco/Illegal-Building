package com.mrbysco.illegalbuilding.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.illegalbuilding.entity.ImpossibleFallingBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;

public class ImpossibleFallingBlockRenderer extends EntityRenderer<ImpossibleFallingBlockEntity> {
	private final BlockRenderDispatcher dispatcher;

	public ImpossibleFallingBlockRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.5F;
		this.dispatcher = context.getBlockRenderDispatcher();
	}

	public void render(ImpossibleFallingBlockEntity impossibleFallingBlockEntity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn) {
		BlockState blockstate = impossibleFallingBlockEntity.getBlockState();
		if (blockstate.getRenderShape() == RenderShape.MODEL) {
			Level level = impossibleFallingBlockEntity.level();
			if (blockstate != level.getBlockState(impossibleFallingBlockEntity.blockPosition()) && blockstate.getRenderShape() != RenderShape.INVISIBLE) {
				poseStack.pushPose();
				BlockPos blockpos = BlockPos.containing(impossibleFallingBlockEntity.getX(), impossibleFallingBlockEntity.getBoundingBox().maxY, impossibleFallingBlockEntity.getZ());
				poseStack.translate(-0.5D, 0.0D, -0.5D);
				var model = this.dispatcher.getBlockModel(blockstate);
				for (var renderType : model.getRenderTypes(blockstate, RandomSource.create(blockstate.getSeed(impossibleFallingBlockEntity.getStartPos())), ModelData.EMPTY))
					this.dispatcher.getModelRenderer().tesselateBlock(level, model, blockstate, blockpos, poseStack, bufferSource.getBuffer(renderType), false, RandomSource.create(), blockstate.getSeed(impossibleFallingBlockEntity.getStartPos()), OverlayTexture.NO_OVERLAY, ModelData.EMPTY, renderType);
				poseStack.popPose();
				super.render(impossibleFallingBlockEntity, entityYaw, partialTicks, poseStack, bufferSource, packedLightIn);
			}
		}
	}

	/**
	 * Returns the location of an entity's texture.
	 */
	public ResourceLocation getTextureLocation(ImpossibleFallingBlockEntity entity) {
		return InventoryMenu.BLOCK_ATLAS;
	}
}
