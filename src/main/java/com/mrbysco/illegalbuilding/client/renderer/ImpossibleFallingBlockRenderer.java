package com.mrbysco.illegalbuilding.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.illegalbuilding.entity.ImpossibleFallingBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

public class ImpossibleFallingBlockRenderer extends EntityRenderer<ImpossibleFallingBlockEntity> {
    public ImpossibleFallingBlockRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
        this.shadowRadius = 0.5F;
    }

    public void render(ImpossibleFallingBlockEntity entityIn, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn) {
        BlockState blockstate = entityIn.getBlockState();
        if (blockstate.getRenderShape() == RenderShape.MODEL) {
            Level level = entityIn.getLevel();
            if (blockstate != level.getBlockState(entityIn.blockPosition()) && blockstate.getRenderShape() != RenderShape.INVISIBLE) {
                poseStack.pushPose();
                BlockPos blockpos = new BlockPos(entityIn.getX(), entityIn.getBoundingBox().maxY, entityIn.getZ());
                poseStack.translate(-0.5D, 0.0D, -0.5D);
                BlockRenderDispatcher blockrenderdispatcher = Minecraft.getInstance().getBlockRenderer();
                for (net.minecraft.client.renderer.RenderType type : net.minecraft.client.renderer.RenderType.chunkBufferLayers()) {
                    if (ItemBlockRenderTypes.canRenderInLayer(blockstate, type)) {
                        net.minecraftforge.client.ForgeHooksClient.setRenderType(type);
                        blockrenderdispatcher.getModelRenderer().tesselateBlock(level, blockrenderdispatcher.getBlockModel(blockstate), blockstate, blockpos, poseStack, bufferSource.getBuffer(type), false, new Random(), blockstate.getSeed(entityIn.getStartPos()), OverlayTexture.NO_OVERLAY);
                    }
                }
                net.minecraftforge.client.ForgeHooksClient.setRenderType(null);
                poseStack.popPose();
                super.render(entityIn, entityYaw, partialTicks, poseStack, bufferSource, packedLightIn);
            }
        }
    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getTextureLocation(ImpossibleFallingBlockEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
