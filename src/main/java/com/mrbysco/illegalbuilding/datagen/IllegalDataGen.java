package com.mrbysco.illegalbuilding.datagen;

import com.mrbysco.illegalbuilding.datagen.server.IllegalBlockTagProvider;
import com.mrbysco.illegalbuilding.datagen.server.IllegalItemTagProvider;
import com.mrbysco.illegalbuilding.datagen.server.IllegalLootProvider;
import com.mrbysco.illegalbuilding.datagen.server.IllegalRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class IllegalDataGen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
		ExistingFileHelper helper = event.getExistingFileHelper();

		if (event.includeServer()) {
			generator.addProvider(event.includeServer(), new IllegalRecipeProvider(packOutput, lookupProvider));
			generator.addProvider(event.includeServer(), new IllegalLootProvider(packOutput, lookupProvider));

			IllegalBlockTagProvider blockTags = new IllegalBlockTagProvider(packOutput, lookupProvider, helper);
			generator.addProvider(event.includeServer(), blockTags);
			generator.addProvider(event.includeServer(), new IllegalItemTagProvider(packOutput, lookupProvider, blockTags, helper));
		}
	}
}
