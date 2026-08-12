package com.mrbysco.illegalbuilding.datagen.server;

import com.mrbysco.illegalbuilding.Reference;
import com.mrbysco.illegalbuilding.registry.IllegalRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import java.util.concurrent.CompletableFuture;

public class IllegalBlockTagProvider extends BlockTagsProvider {
	public IllegalBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, lookupProvider, Reference.MOD_ID);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(Reference.IMPOSSIBLE_SAND).add(
				IllegalRegistry.IMPOSSIBLE_SAND.get(), IllegalRegistry.IMPOSSIBLE_RED_SAND.get()
		);

		this.tag(BlockTags.MINEABLE_WITH_AXE).add(
				IllegalRegistry.IMPOSSIBLE_OAK_LOG.get(),
				IllegalRegistry.IMPOSSIBLE_SPRUCE_LOG.get(),
				IllegalRegistry.IMPOSSIBLE_BIRCH_LOG.get(),
				IllegalRegistry.IMPOSSIBLE_JUNGLE_LOG.get(),
				IllegalRegistry.IMPOSSIBLE_ACACIA_LOG.get(),
				IllegalRegistry.IMPOSSIBLE_CHERRY_LOG.get(),
				IllegalRegistry.IMPOSSIBLE_DARK_OAK_LOG.get(),
				IllegalRegistry.IMPOSSIBLE_PALE_OAK_LOG.get(),
				IllegalRegistry.IMPOSSIBLE_MANGROVE_LOG.get()
		);

		this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
				IllegalRegistry.OFFSET_STONE.get()
		);

		this.tag(BlockTags.MINEABLE_WITH_SHOVEL).add(
				IllegalRegistry.IMPOSSIBLE_SAND.get(),
				IllegalRegistry.IMPOSSIBLE_RED_SAND.get()
		);
	}
}
