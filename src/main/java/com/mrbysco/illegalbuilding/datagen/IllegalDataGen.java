package com.mrbysco.illegalbuilding.datagen;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mrbysco.illegalbuilding.registry.IllegalRegistry;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.loot.LootParameterSet;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTable.Builder;
import net.minecraft.loot.LootTableManager;
import net.minecraft.loot.ValidationTracker;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.mrbysco.illegalbuilding.registry.IllegalRegistry.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class IllegalDataGen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper helper = event.getExistingFileHelper();

		if (event.includeServer()) {
			generator.addProvider(new IllegalLoot(generator));
		}
	}

	private static class IllegalLoot extends LootTableProvider {
		public IllegalLoot(DataGenerator gen) {
			super(gen);
		}

		@Override
		protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, Builder>>>, LootParameterSet>> getTables() {
			return ImmutableList.of(Pair.of(FarmingBlocks::new, LootParameterSets.BLOCK));
		}

		private static class FarmingBlocks extends BlockLootTables {

			@Override
			protected void addTables() {
				this.dropSelf(OFFSET_STONE.get());

				this.dropSelf(IMPOSSIBLE_OAK_LOG.get());
				this.dropSelf(IMPOSSIBLE_SPRUCE_LOG.get());
				this.dropSelf(IMPOSSIBLE_BIRCH_LOG.get());
				this.dropSelf(IMPOSSIBLE_JUNGLE_LOG.get());
				this.dropSelf(IMPOSSIBLE_ACACIA_LOG.get());
				this.dropSelf(IMPOSSIBLE_DARK_OAK_LOG.get());

				this.dropSelf(IMPOSSIBLE_SAND.get());
				this.dropSelf(IMPOSSIBLE_RED_SAND.get());

				this.dropSelf(IMPOSSIBLE_SUGAR_CANE.get());
				this.dropSelf(IMPOSSIBLE_CACTUS.get());
			}

			@Override
			protected Iterable<Block> getKnownBlocks() {
				return (Iterable<Block>) IllegalRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
			}
		}

		@Override
		protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationtracker) {
			map.forEach((name, table) -> LootTableManager.validate(validationtracker, name, table));
		}
	}
}
