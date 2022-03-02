package com.mrbysco.illegalbuilding.datagen;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mrbysco.illegalbuilding.registry.IllegalRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

import javax.annotation.Nonnull;
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
		protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
			return ImmutableList.of(Pair.of(FarmingBlocks::new, LootContextParamSets.BLOCK));
		}

		private static class FarmingBlocks extends BlockLoot {

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
		protected void validate(Map<ResourceLocation, LootTable> map, @Nonnull ValidationContext validationContext) {
			map.forEach((name, table) -> LootTables.validate(validationContext, name, table));
		}
	}
}
