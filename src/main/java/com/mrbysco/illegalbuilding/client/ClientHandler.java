package com.mrbysco.illegalbuilding.client;

import com.mrbysco.illegalbuilding.client.renderer.ImpossibleFallingBlockRenderer;
import com.mrbysco.illegalbuilding.registry.IllegalRegistry;
import net.minecraft.client.color.block.BlockTintSources;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

import java.util.List;

@EventBusSubscriber(Dist.CLIENT)
public class ClientHandler {

	@SubscribeEvent
	public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(IllegalRegistry.IMPOSSIBLE_FALLING_BLOCK.get(), ImpossibleFallingBlockRenderer::new);
	}

	@SubscribeEvent
	public static void registerBlockColors(RegisterColorHandlersEvent.BlockTintSources event) {
		event.register(List.of(BlockTintSources.sugarCane()), IllegalRegistry.IMPOSSIBLE_SUGAR_CANE.get());
	}
}