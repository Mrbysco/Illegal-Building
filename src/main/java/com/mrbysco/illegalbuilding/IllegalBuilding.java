package com.mrbysco.illegalbuilding;

import com.mrbysco.illegalbuilding.client.ClientHandler;
import com.mrbysco.illegalbuilding.registry.IllegalRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Reference.MOD_ID)
public class IllegalBuilding {
    public static final Logger LOGGER = LogManager.getLogger();

    public IllegalBuilding() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        IllegalRegistry.ENTITIES.register(eventBus);
        IllegalRegistry.BLOCKS.register(eventBus);
        IllegalRegistry.ITEMS.register(eventBus);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            eventBus.addListener(ClientHandler::registerRenders);
            eventBus.addListener(ClientHandler::registerBlockColors);
        });
    }
}