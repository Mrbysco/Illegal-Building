package com.mrbysco.illegalbuilding.datagen.server;

import com.mrbysco.illegalbuilding.Reference;
import com.mrbysco.illegalbuilding.registry.IllegalRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class IllegalItemTagProvider extends ItemTagsProvider {

	public IllegalItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
	                              TagsProvider<Block> blockTagProvider) {
		super(output, lookupProvider, blockTagProvider.contentsGetter(), Reference.MOD_ID);
	}

	@Override
	public void addTags(HolderLookup.Provider provider) {
		this.tag(Reference.IMPOSSIBLE_SAND_ITEM).add(
				IllegalRegistry.IMPOSSIBLE_SAND.asItem(), IllegalRegistry.IMPOSSIBLE_RED_SAND.asItem()
		);
	}
}
