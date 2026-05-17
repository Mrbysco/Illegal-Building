package com.mrbysco.illegalbuilding.datagen.client;

import com.mrbysco.illegalbuilding.Reference;
import com.mrbysco.illegalbuilding.registry.IllegalRegistry;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class IllegalModelProvider extends ModelProvider {
	public static final ModelTemplate OFFSET_CUBE_ALL = ModelTemplates.create("illegalbuilding:cube_offset_all", TextureSlot.ALL);
	public static final ModelTemplate UPSIDEDOWN_CROSS = ModelTemplates.create("illegalbuilding:upsidedown_cross", TextureSlot.CROSS);

	public IllegalModelProvider(PackOutput output) {
		super(output, Reference.MOD_ID);
	}

	@Override
	protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
		createOffset(blockModels, IllegalRegistry.OFFSET_STONE.get(), Blocks.STONE);

		createLog(blockModels, IllegalRegistry.IMPOSSIBLE_OAK_LOG.get(), mcLocation("block/oak_log_top"));
		createLog(blockModels, IllegalRegistry.IMPOSSIBLE_SPRUCE_LOG.get(), mcLocation("block/spruce_log_top"));
		createLog(blockModels, IllegalRegistry.IMPOSSIBLE_BIRCH_LOG.get(), mcLocation("block/birch_log_top"));
		createLog(blockModels, IllegalRegistry.IMPOSSIBLE_JUNGLE_LOG.get(), mcLocation("block/jungle_log_top"));
		createLog(blockModels, IllegalRegistry.IMPOSSIBLE_ACACIA_LOG.get(), mcLocation("block/acacia_log_top"));
		createLog(blockModels, IllegalRegistry.IMPOSSIBLE_DARK_OAK_LOG.get(), mcLocation("block/dark_oak_log_top"));

		createAll(blockModels, IllegalRegistry.IMPOSSIBLE_SAND.get(), Blocks.SAND);
		createAll(blockModels, IllegalRegistry.IMPOSSIBLE_RED_SAND.get(), Blocks.RED_SAND);

		createUpsideDownCross(blockModels, IllegalRegistry.IMPOSSIBLE_SUGAR_CANE.get(), Blocks.SUGAR_CANE);
		itemModels.itemModelOutput.accept(
				IllegalRegistry.IMPOSSIBLE_SUGAR_CANE.asItem(),
				ItemModelUtils.plainModel(
						ModelTemplates.FLAT_ITEM.create(
								IllegalRegistry.IMPOSSIBLE_SUGAR_CANE.asItem(),
								TextureMapping.layer0(Items.SUGAR_CANE),
								itemModels.modelOutput
						)
				)
		);

		createExisting(blockModels, IllegalRegistry.IMPOSSIBLE_CACTUS.get(), modLocation("block/impossible_cactus"));
	}

	private void createLog(BlockModelGenerators blockModels, Block block, Identifier texture) {
		TextureMapping texturemapping = TextureMapping.cube(new Material(texture));
		Identifier model = ModelTemplates.CUBE_ALL.create(block, texturemapping, blockModels.modelOutput);
		MultiVariantGenerator multiVariant = MultiVariantGenerator.dispatch(block, BlockModelGenerators.plainVariant(model));

		blockModels.blockStateOutput
				.accept(multiVariant);
	}

	private void createExisting(BlockModelGenerators blockModels, Block block, Identifier model) {
		MultiVariantGenerator multiVariant = MultiVariantGenerator.dispatch(block, BlockModelGenerators.plainVariant(model));

		blockModels.blockStateOutput
				.accept(multiVariant);
	}

	private void createAll(BlockModelGenerators blockModels, Block block, Block textureBlock) {
		TextureMapping texturemapping = TextureMapping.cube(textureBlock);
		Identifier model = ModelTemplates.CUBE_ALL.create(block, texturemapping, blockModels.modelOutput);
		MultiVariantGenerator multiVariant = MultiVariantGenerator.dispatch(block, BlockModelGenerators.plainVariant(model));

		blockModels.blockStateOutput
				.accept(multiVariant);
	}

	private void createOffset(BlockModelGenerators blockModels, Block block, Block textureBlock) {
		TextureMapping texturemapping = TextureMapping.cube(textureBlock);
		Identifier model = OFFSET_CUBE_ALL.create(block, texturemapping, blockModels.modelOutput);
		MultiVariantGenerator multiVariant = MultiVariantGenerator.dispatch(block, BlockModelGenerators.plainVariant(model))
				.with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING);

		blockModels.blockStateOutput
				.accept(multiVariant);
	}

	private void createUpsideDownCross(BlockModelGenerators blockModels, Block block, Block textureBlock) {
		TextureMapping texturemapping = TextureMapping.cross(textureBlock);
		Identifier model = UPSIDEDOWN_CROSS.create(block, texturemapping, blockModels.modelOutput);
		MultiVariantGenerator multiVariant = MultiVariantGenerator.dispatch(block, BlockModelGenerators.plainVariant(model));

		blockModels.blockStateOutput
				.accept(multiVariant);
	}
}
