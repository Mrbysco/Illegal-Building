package com.mrbysco.illegalbuilding.client;

import com.mrbysco.illegalbuilding.client.renderer.ImpossibleFallingBlockRenderer;
import com.mrbysco.illegalbuilding.registry.IllegalRegistry;
import net.minecraft.client.renderer.BiomeColors;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

public class ClientHandler {

	public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(IllegalRegistry.IMPOSSIBLE_FALLING_BLOCK.get(), ImpossibleFallingBlockRenderer::new);
	}


	public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
		event.register((state, blockDisplayReader, pos, tintIndex) -> blockDisplayReader != null && pos != null ?
				BiomeColors.getAverageGrassColor(blockDisplayReader, pos) : -1, IllegalRegistry.IMPOSSIBLE_SUGAR_CANE.get());
	}
}