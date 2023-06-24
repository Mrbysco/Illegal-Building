package com.mrbysco.illegalbuilding;

import com.mojang.logging.LogUtils;
import com.mrbysco.illegalbuilding.client.ClientHandler;
import com.mrbysco.illegalbuilding.handler.RightClickHandler;
import com.mrbysco.illegalbuilding.registry.IllegalRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Reference.MOD_ID)
public class IllegalBuilding {
	public static final Logger LOGGER = LogUtils.getLogger();

	public IllegalBuilding() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

		IllegalRegistry.ENTITY_TYPES.register(eventBus);
		IllegalRegistry.BLOCKS.register(eventBus);
		IllegalRegistry.ITEMS.register(eventBus);
		IllegalRegistry.CREATIVE_MODE_TABS.register(eventBus);

		MinecraftForge.EVENT_BUS.register(new RightClickHandler());

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			eventBus.addListener(ClientHandler::registerEntityRenders);
			eventBus.addListener(ClientHandler::registerBlockColors);
		});
	}
}