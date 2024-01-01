package com.mrbysco.illegalbuilding.registry;

import com.mrbysco.illegalbuilding.Reference;
import com.mrbysco.illegalbuilding.blocks.ImpossibleCactusBlock;
import com.mrbysco.illegalbuilding.blocks.ImpossibleSandBlock;
import com.mrbysco.illegalbuilding.blocks.ImpossibleSugarCaneBlock;
import com.mrbysco.illegalbuilding.blocks.OffsetBlock;
import com.mrbysco.illegalbuilding.entity.ImpossibleFallingBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Supplier;

public class IllegalRegistry {
	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Reference.MOD_ID);
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Reference.MOD_ID);
	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Reference.MOD_ID);
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, Reference.MOD_ID);

	public static final Supplier<EntityType<ImpossibleFallingBlockEntity>> IMPOSSIBLE_FALLING_BLOCK = ENTITY_TYPES.register("impossible_falling_block",
			() -> EntityType.Builder.<ImpossibleFallingBlockEntity>of(ImpossibleFallingBlockEntity::new, MobCategory.MISC)
					.sized(0.98F, 0.98F).clientTrackingRange(10).updateInterval(20)
					.setCustomClientFactory(ImpossibleFallingBlockEntity::new).build("impossible_falling_block"));

	public static final DeferredBlock<OffsetBlock> OFFSET_STONE = BLOCKS.register("offset_stone", () -> new OffsetBlock(Block.Properties.ofFullCopy(Blocks.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F).noOcclusion().isRedstoneConductor(OffsetBlock::isntSolid)));

	public static final DeferredBlock<Block> IMPOSSIBLE_OAK_LOG = BLOCKS.register("impossible_oak_log", () -> new Block(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F)));
	public static final DeferredBlock<Block> IMPOSSIBLE_SPRUCE_LOG = BLOCKS.register("impossible_spruce_log", () -> new Block(Block.Properties.ofFullCopy(Blocks.SPRUCE_PLANKS).strength(2.0F, 3.0F)));
	public static final DeferredBlock<Block> IMPOSSIBLE_BIRCH_LOG = BLOCKS.register("impossible_birch_log", () -> new Block(Block.Properties.ofFullCopy(Blocks.BIRCH_PLANKS).strength(2.0F, 3.0F)));
	public static final DeferredBlock<Block> IMPOSSIBLE_JUNGLE_LOG = BLOCKS.register("impossible_jungle_log", () -> new Block(Block.Properties.ofFullCopy(Blocks.JUNGLE_PLANKS).strength(2.0F, 3.0F)));
	public static final DeferredBlock<Block> IMPOSSIBLE_ACACIA_LOG = BLOCKS.register("impossible_acacia_log", () -> new Block(Block.Properties.ofFullCopy(Blocks.ACACIA_PLANKS).strength(2.0F, 3.0F)));
	public static final DeferredBlock<Block> IMPOSSIBLE_DARK_OAK_LOG = BLOCKS.register("impossible_dark_oak_log", () -> new Block(Block.Properties.ofFullCopy(Blocks.DARK_OAK_PLANKS).strength(2.0F, 3.0F)));

	public static final DeferredBlock<ImpossibleSandBlock> IMPOSSIBLE_SAND = BLOCKS.register("impossible_sand", () -> new ImpossibleSandBlock(14406560, Block.Properties.ofFullCopy(Blocks.SAND).strength(0.5F).sound(SoundType.SAND)));
	public static final DeferredBlock<ImpossibleSandBlock> IMPOSSIBLE_RED_SAND = BLOCKS.register("impossible_red_sand", () -> new ImpossibleSandBlock(11098145, Block.Properties.ofFullCopy(Blocks.RED_SAND).strength(0.5F).sound(SoundType.SAND)));

	public static final DeferredBlock<ImpossibleSugarCaneBlock> IMPOSSIBLE_SUGAR_CANE = BLOCKS.register("impossible_sugar_cane", () -> new ImpossibleSugarCaneBlock(Block.Properties.ofFullCopy(Blocks.SUGAR_CANE).noCollission().randomTicks().instabreak().sound(SoundType.GRASS)));
	public static final DeferredBlock<ImpossibleCactusBlock> IMPOSSIBLE_CACTUS = BLOCKS.register("impossible_cactus", () -> new ImpossibleCactusBlock(Block.Properties.ofFullCopy(Blocks.CACTUS).randomTicks().strength(0.4F).sound(SoundType.WOOL)));

	public static final DeferredItem<BlockItem> OFFSET_STONE_ITEM = ITEMS.registerSimpleBlockItem(OFFSET_STONE);
	public static final DeferredItem<BlockItem> IMPOSSIBLE_OAK_LOG_ITEM = ITEMS.registerSimpleBlockItem(IMPOSSIBLE_OAK_LOG);
	public static final DeferredItem<BlockItem> IMPOSSIBLE_SPRUCE_LOG_ITEM = ITEMS.registerSimpleBlockItem(IMPOSSIBLE_SPRUCE_LOG);
	public static final DeferredItem<BlockItem> IMPOSSIBLE_BIRCH_LOG_ITEM = ITEMS.registerSimpleBlockItem(IMPOSSIBLE_BIRCH_LOG);
	public static final DeferredItem<BlockItem> IMPOSSIBLE_JUNGLE_LOG_ITEM = ITEMS.registerSimpleBlockItem(IMPOSSIBLE_JUNGLE_LOG);
	public static final DeferredItem<BlockItem> IMPOSSIBLE_ACACIA_LOG_ITEM = ITEMS.registerSimpleBlockItem(IMPOSSIBLE_ACACIA_LOG);
	public static final DeferredItem<BlockItem> IMPOSSIBLE_DARK_OAK_LOG_ITEM = ITEMS.registerSimpleBlockItem(IMPOSSIBLE_DARK_OAK_LOG);
	public static final DeferredItem<BlockItem> IMPOSSIBLE_SAND_ITEM = ITEMS.registerSimpleBlockItem(IMPOSSIBLE_SAND);
	public static final DeferredItem<BlockItem> IMPOSSIBLE_RED_SAND_ITEM = ITEMS.registerSimpleBlockItem(IMPOSSIBLE_RED_SAND);
	public static final DeferredItem<BlockItem> IMPOSSIBLE_SUGAR_CANE_ITEM = ITEMS.registerSimpleBlockItem(IMPOSSIBLE_SUGAR_CANE);
	public static final DeferredItem<BlockItem> IMPOSSIBLE_CACTUS_ITEM = ITEMS.registerSimpleBlockItem(IMPOSSIBLE_CACTUS);

	public static final Supplier<CreativeModeTab> ILLEGAL_TAB = CREATIVE_MODE_TABS.register("tab", () -> CreativeModeTab.builder()
			.icon(() -> new ItemStack(IllegalRegistry.OFFSET_STONE.get()))
			.withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
			.title(Component.translatable("itemGroup.illegalbuilding"))
			.displayItems((displayParameters, output) -> {
				List<ItemStack> stacks = IllegalRegistry.ITEMS.getEntries().stream().map(reg -> new ItemStack(reg.get())).toList();
				output.acceptAll(stacks);
			}).build());
}
