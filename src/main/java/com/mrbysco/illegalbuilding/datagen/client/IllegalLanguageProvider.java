package com.mrbysco.illegalbuilding.datagen.client;

import com.mrbysco.illegalbuilding.Reference;
import com.mrbysco.illegalbuilding.registry.IllegalRegistry;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class IllegalLanguageProvider extends LanguageProvider {
	public IllegalLanguageProvider(PackOutput output) {
		super(output, Reference.MOD_ID, "en_us");
	}

	@Override
	protected void addTranslations() {
		add("itemGroup.illegalbuilding", "Illegal Building");

		addBlock(IllegalRegistry.OFFSET_STONE, "Offset Stone");

		addBlock(IllegalRegistry.IMPOSSIBLE_OAK_LOG, "Illegal Oak Log");
		addBlock(IllegalRegistry.IMPOSSIBLE_SPRUCE_LOG, "Illegal Spruce Log");
		addBlock(IllegalRegistry.IMPOSSIBLE_BIRCH_LOG, "Illegal Birch Log");
		addBlock(IllegalRegistry.IMPOSSIBLE_JUNGLE_LOG, "Illegal Jungle Log");
		addBlock(IllegalRegistry.IMPOSSIBLE_ACACIA_LOG, "Illegal Acacia Log");
		addBlock(IllegalRegistry.IMPOSSIBLE_CHERRY_LOG, "Illegal Cherry Log");
		addBlock(IllegalRegistry.IMPOSSIBLE_DARK_OAK_LOG, "Illegal Dark Oak Log");
		addBlock(IllegalRegistry.IMPOSSIBLE_PALE_OAK_LOG, "Illegal Pale Oak Log");
		addBlock(IllegalRegistry.IMPOSSIBLE_MANGROVE_LOG, "Illegal Mangrove Log");

		addBlock(IllegalRegistry.IMPOSSIBLE_SAND, "Illegal Sand");
		addBlock(IllegalRegistry.IMPOSSIBLE_RED_SAND, "Illegal Red Sand");
		addBlock(IllegalRegistry.IMPOSSIBLE_SUGAR_CANE, "Illegal Sugar Cane");
		addBlock(IllegalRegistry.IMPOSSIBLE_CACTUS, "Illegal Cactus");
	}
}
