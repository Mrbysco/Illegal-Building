package com.mrbysco.illegalbuilding.datagen;

import com.mrbysco.illegalbuilding.registry.IllegalRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.WritableRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class IllegalDataGen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

		if (event.includeServer()) {
			generator.addProvider(event.includeServer(), new IllegalLoot(packOutput, lookupProvider));
		}
	}

	private static class IllegalLoot extends LootTableProvider {
		public IllegalLoot(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
			super(packOutput, Set.of(), List.of(
					new SubProviderEntry(IllegalBlocks::new, LootContextParamSets.BLOCK)
			), lookupProvider);
		}

		private static class IllegalBlocks extends BlockLootSubProvider {

			protected IllegalBlocks() {
				super(Set.of(), FeatureFlags.REGISTRY.allFlags());
			}

			@Override
			protected void generate() {
				this.dropSelf(IllegalRegistry.OFFSET_STONE.get());

				this.dropSelf(IllegalRegistry.IMPOSSIBLE_OAK_LOG.get());
				this.dropSelf(IllegalRegistry.IMPOSSIBLE_SPRUCE_LOG.get());
				this.dropSelf(IllegalRegistry.IMPOSSIBLE_BIRCH_LOG.get());
				this.dropSelf(IllegalRegistry.IMPOSSIBLE_JUNGLE_LOG.get());
				this.dropSelf(IllegalRegistry.IMPOSSIBLE_ACACIA_LOG.get());
				this.dropSelf(IllegalRegistry.IMPOSSIBLE_DARK_OAK_LOG.get());

				this.dropSelf(IllegalRegistry.IMPOSSIBLE_SAND.get());
				this.dropSelf(IllegalRegistry.IMPOSSIBLE_RED_SAND.get());

				this.dropSelf(IllegalRegistry.IMPOSSIBLE_SUGAR_CANE.get());
				this.dropSelf(IllegalRegistry.IMPOSSIBLE_CACTUS.get());
			}

			@Override
			protected Iterable<Block> getKnownBlocks() {
				return (Iterable<Block>) IllegalRegistry.BLOCKS.getEntries().stream().map(holder -> (Block) holder.get())::iterator;
			}
		}

		@Override
		protected void validate(WritableRegistry<LootTable> writableregistry, ValidationContext validationcontext, ProblemReporter.Collector problemreporter$collector) {
			super.validate(writableregistry, validationcontext, problemreporter$collector);
		}
	}
}
