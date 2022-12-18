package com.mrbysco.illegalbuilding.datagen;

import com.mrbysco.illegalbuilding.registry.IllegalRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class IllegalDataGen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		ExistingFileHelper helper = event.getExistingFileHelper();

		if (event.includeServer()) {
			generator.addProvider(event.includeServer(), new IllegalLoot(packOutput));
		}
	}

	private static class IllegalLoot extends LootTableProvider {
		public IllegalLoot(PackOutput packOutput) {
			super(packOutput, Set.of(), List.of(
					new SubProviderEntry(IllegalBlocks::new, LootContextParamSets.BLOCK)
			));
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
				return (Iterable<Block>) IllegalRegistry.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
			}
		}

		@Override
		protected void validate(Map<ResourceLocation, LootTable> map, @Nonnull ValidationContext validationContext) {
			map.forEach((name, table) -> LootTables.validate(validationContext, name, table));
		}
	}
}
