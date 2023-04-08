package com.mrbysco.illegalbuilding.client;

import com.mrbysco.illegalbuilding.client.renderer.ImpossibleFallingBlockRenderer;
import com.mrbysco.illegalbuilding.registry.IllegalRegistry;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientHandler {
	public static void onClientSetup(final FMLClientSetupEvent event) {
		ItemBlockRenderTypes.setRenderLayer(IllegalRegistry.IMPOSSIBLE_SUGAR_CANE.get(), RenderType.cutout());
		ItemBlockRenderTypes.setRenderLayer(IllegalRegistry.IMPOSSIBLE_CACTUS.get(), RenderType.cutout());
	}

	public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(IllegalRegistry.IMPOSSIBLE_FALLING_BLOCK.get(), ImpossibleFallingBlockRenderer::new);
	}


	public static void registerBlockColors(ColorHandlerEvent.Item event) {
		BlockColors blockColors = event.getBlockColors();

		blockColors.register((state, blockDisplayReader, pos, tintIndex) -> blockDisplayReader != null && pos != null ?
				BiomeColors.getAverageGrassColor(blockDisplayReader, pos) : -1, IllegalRegistry.IMPOSSIBLE_SUGAR_CANE.get());
	}
}