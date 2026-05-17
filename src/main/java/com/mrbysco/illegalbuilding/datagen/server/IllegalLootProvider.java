package com.mrbysco.illegalbuilding.datagen.server;

import com.mrbysco.illegalbuilding.registry.IllegalRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class IllegalLootProvider extends LootTableProvider {
	public IllegalLootProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(packOutput, Set.of(), List.of(
				new SubProviderEntry(IllegalBlocks::new, LootContextParamSets.BLOCK)
		), lookupProvider);
	}

	private static class IllegalBlocks extends BlockLootSubProvider {

		protected IllegalBlocks(HolderLookup.Provider provider) {
			super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
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
}
