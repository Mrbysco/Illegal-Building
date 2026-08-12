package com.mrbysco.illegalbuilding.datagen;

import com.mrbysco.illegalbuilding.datagen.client.IllegalLanguageProvider;
import com.mrbysco.illegalbuilding.datagen.client.IllegalModelProvider;
import com.mrbysco.illegalbuilding.datagen.server.IllegalBlockTagProvider;
import com.mrbysco.illegalbuilding.datagen.server.IllegalItemTagProvider;
import com.mrbysco.illegalbuilding.datagen.server.IllegalLootProvider;
import com.mrbysco.illegalbuilding.datagen.server.IllegalRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber
public class IllegalDataGen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent.Client event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

		generator.addProvider(true, new IllegalRecipeProvider.Runner(packOutput, lookupProvider));
		generator.addProvider(true, new IllegalLootProvider(packOutput, lookupProvider));

		generator.addProvider(true, new IllegalBlockTagProvider(packOutput, lookupProvider));
		generator.addProvider(true, new IllegalItemTagProvider(packOutput, lookupProvider));

		generator.addProvider(true, new IllegalLanguageProvider(packOutput));
		generator.addProvider(true, new IllegalModelProvider(packOutput));
	}
}
