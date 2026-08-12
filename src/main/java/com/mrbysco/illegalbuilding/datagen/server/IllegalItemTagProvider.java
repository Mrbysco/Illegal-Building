package com.mrbysco.illegalbuilding.datagen.server;

import com.mrbysco.illegalbuilding.Reference;
import com.mrbysco.illegalbuilding.registry.IllegalRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

public class IllegalItemTagProvider extends ItemTagsProvider {

	public IllegalItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, lookupProvider, Reference.MOD_ID);
	}

	@Override
	public void addTags(HolderLookup.Provider provider) {
		this.tag(Reference.IMPOSSIBLE_SAND_ITEM).add(
				IllegalRegistry.IMPOSSIBLE_SAND_ITEM.getKey(), IllegalRegistry.IMPOSSIBLE_RED_SAND_ITEM.getKey()
		);
	}
}
