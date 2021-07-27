package com.mrbysco.illegalbuilding.client;

import com.mrbysco.illegalbuilding.client.renderer.ImpossibleFallingBlockRenderer;
import com.mrbysco.illegalbuilding.registry.IllegalRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientHandler {
    public static void onClientSetup(final FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(IllegalRegistry.IMPOSSIBLE_FALLING_BLOCK.get(), ImpossibleFallingBlockRenderer::new);

        RenderTypeLookup.setRenderLayer(IllegalRegistry.IMPOSSIBLE_SUGAR_CANE.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(IllegalRegistry.IMPOSSIBLE_CACTUS.get(), RenderType.cutout());
    }

    public static void registerBlockColors(ColorHandlerEvent.Item event) {
        BlockColors blockColors = event.getBlockColors();

        blockColors.register((state, blockDisplayReader, pos, tintIndex) -> blockDisplayReader != null && pos != null ?
                BiomeColors.getAverageGrassColor(blockDisplayReader, pos) : -1, IllegalRegistry.IMPOSSIBLE_SUGAR_CANE.get());
    }
}