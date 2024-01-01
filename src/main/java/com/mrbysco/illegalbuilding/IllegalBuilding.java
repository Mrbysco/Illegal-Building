package com.mrbysco.illegalbuilding;

import com.mojang.logging.LogUtils;
import com.mrbysco.illegalbuilding.client.ClientHandler;
import com.mrbysco.illegalbuilding.handler.RightClickHandler;
import com.mrbysco.illegalbuilding.registry.IllegalRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(Reference.MOD_ID)
public class IllegalBuilding {
	public static final Logger LOGGER = LogUtils.getLogger();

	public IllegalBuilding(IEventBus eventBus) {
		IllegalRegistry.ENTITY_TYPES.register(eventBus);
		IllegalRegistry.BLOCKS.register(eventBus);
		IllegalRegistry.ITEMS.register(eventBus);
		IllegalRegistry.CREATIVE_MODE_TABS.register(eventBus);

		NeoForge.EVENT_BUS.register(new RightClickHandler());

		if (FMLEnvironment.dist.isClient()) {
			eventBus.addListener(ClientHandler::registerEntityRenders);
			eventBus.addListener(ClientHandler::registerBlockColors);
		}
	}
}