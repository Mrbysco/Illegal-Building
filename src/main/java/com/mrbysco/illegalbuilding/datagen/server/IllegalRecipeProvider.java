package com.mrbysco.illegalbuilding.datagen.server;

import com.mrbysco.illegalbuilding.registry.IllegalRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.concurrent.CompletableFuture;

public class IllegalRecipeProvider extends RecipeProvider {
	public IllegalRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
		super(provider, recipeOutput);
	}

	@Override
	protected void buildRecipes() {
		createLogRecipe(output, IllegalRegistry.IMPOSSIBLE_OAK_LOG_ITEM, Items.OAK_LOG);
		createLogRecipe(output, IllegalRegistry.IMPOSSIBLE_SPRUCE_LOG_ITEM, Items.SPRUCE_LOG);
		createLogRecipe(output, IllegalRegistry.IMPOSSIBLE_BIRCH_LOG_ITEM, Items.BIRCH_LOG);
		createLogRecipe(output, IllegalRegistry.IMPOSSIBLE_JUNGLE_LOG_ITEM, Items.JUNGLE_LOG);
		createLogRecipe(output, IllegalRegistry.IMPOSSIBLE_ACACIA_LOG_ITEM, Items.ACACIA_LOG);
		createLogRecipe(output, IllegalRegistry.IMPOSSIBLE_CHERRY_LOG_ITEM, Items.CHERRY_LOG);
		createLogRecipe(output, IllegalRegistry.IMPOSSIBLE_DARK_OAK_LOG_ITEM, Items.DARK_OAK_LOG);
		createLogRecipe(output, IllegalRegistry.IMPOSSIBLE_PALE_OAK_LOG_ITEM, Items.PALE_OAK_LOG);
		createLogRecipe(output, IllegalRegistry.IMPOSSIBLE_MANGROVE_LOG_ITEM, Items.MANGROVE_LOG);

		shapeless(RecipeCategory.MISC, IllegalRegistry.IMPOSSIBLE_SAND_ITEM.get())
				.requires(Items.SAND)
				.unlockedBy("has_item", has(Items.SAND))
				.save(output);
		shapeless(RecipeCategory.MISC, IllegalRegistry.IMPOSSIBLE_RED_SAND_ITEM.get())
				.requires(Items.RED_SAND)
				.unlockedBy("has_item", has(Items.RED_SAND))
				.save(output);

		shaped(RecipeCategory.BUILDING_BLOCKS, IllegalRegistry.OFFSET_STONE.asItem(), 9)
				.pattern("###")
				.pattern("###")
				.pattern("###")
				.define('#', Items.STONE)
				.unlockedBy("has_item", has(Items.STONE))
				.save(output);
	}

	private void createLogRecipe(RecipeOutput output, DeferredItem<?> logItem, ItemLike log) {
		shaped(RecipeCategory.BUILDING_BLOCKS, logItem.asItem(), 4)
				.pattern("##")
				.pattern("##")
				.define('#', log)
				.unlockedBy("has_item", has(log))
				.save(output);
	}

	public static class Runner extends RecipeProvider.Runner {
		public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
			super(output, completableFuture);
		}

		@Override
		protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
			return new IllegalRecipeProvider(provider, recipeOutput);
		}

		@Override
		public String getName() {
			return "Illegal Building Recipes";
		}
	}
}
