package com.mrbysco.illegalbuilding.registry;

import com.mrbysco.illegalbuilding.Reference;
import com.mrbysco.illegalbuilding.blocks.ImpossibleCactusBlock;
import com.mrbysco.illegalbuilding.blocks.ImpossibleSandBlock;
import com.mrbysco.illegalbuilding.blocks.ImpossibleSugarCaneBlock;
import com.mrbysco.illegalbuilding.blocks.OffsetBlock;
import com.mrbysco.illegalbuilding.entity.ImpossibleFallingBlockEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class IllegalRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Reference.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MOD_ID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Reference.MOD_ID);

    public static final RegistryObject<EntityType<ImpossibleFallingBlockEntity>> IMPOSSIBLE_FALLING_BLOCK = ENTITIES.register("impossible_falling_block",
            () -> register("impossible_falling_block", EntityType.Builder.<ImpossibleFallingBlockEntity>of(ImpossibleFallingBlockEntity::new, MobCategory.MISC)
                    .sized(0.98F, 0.98F).clientTrackingRange(10).updateInterval(20)
                    .setCustomClientFactory(ImpossibleFallingBlockEntity::new)));

    public static final RegistryObject<Block> OFFSET_STONE = BLOCKS.register("offset_stone", () -> new OffsetBlock(Block.Properties.of(Material.STONE, MaterialColor.STONE).requiresCorrectToolForDrops().strength(1.5F, 6.0F).noOcclusion().isRedstoneConductor(OffsetBlock::isntSolid)));

    public static final RegistryObject<Block> IMPOSSIBLE_OAK_LOG = BLOCKS.register("impossible_oak_log", () -> new Block(Block.Properties.of(Material.WOOD, MaterialColor.PODZOL).strength(2.0F, 3.0F)));
    public static final RegistryObject<Block> IMPOSSIBLE_SPRUCE_LOG = BLOCKS.register("impossible_spruce_log", () -> new Block(Block.Properties.of(Material.WOOD, MaterialColor.COLOR_BROWN).strength(2.0F, 3.0F)));
    public static final RegistryObject<Block> IMPOSSIBLE_BIRCH_LOG = BLOCKS.register("impossible_birch_log", () -> new Block(Block.Properties.of(Material.WOOD, MaterialColor.QUARTZ).strength(2.0F, 3.0F)));
    public static final RegistryObject<Block> IMPOSSIBLE_JUNGLE_LOG = BLOCKS.register("impossible_jungle_log", () -> new Block(Block.Properties.of(Material.WOOD, MaterialColor.PODZOL).strength(2.0F, 3.0F)));
    public static final RegistryObject<Block> IMPOSSIBLE_ACACIA_LOG = BLOCKS.register("impossible_acacia_log", () -> new Block(Block.Properties.of(Material.WOOD, MaterialColor.STONE).strength(2.0F, 3.0F)));
    public static final RegistryObject<Block> IMPOSSIBLE_DARK_OAK_LOG = BLOCKS.register("impossible_dark_oak_log", () -> new Block(Block.Properties.of(Material.WOOD, MaterialColor.COLOR_BROWN).strength(2.0F, 3.0F)));

    public static final RegistryObject<Block> IMPOSSIBLE_SAND = BLOCKS.register("impossible_sand", () -> new ImpossibleSandBlock(14406560, Block.Properties.of(Material.SAND, MaterialColor.SAND).strength(0.5F).sound(SoundType.SAND)));
    public static final RegistryObject<Block> IMPOSSIBLE_RED_SAND = BLOCKS.register("impossible_red_sand", () -> new ImpossibleSandBlock(11098145, Block.Properties.of(Material.SAND, MaterialColor.COLOR_ORANGE).strength(0.5F).sound(SoundType.SAND)));

    public static final RegistryObject<Block> IMPOSSIBLE_SUGAR_CANE = BLOCKS.register("impossible_sugar_cane", () -> new ImpossibleSugarCaneBlock(Block.Properties.of(Material.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS)));
    public static final RegistryObject<Block> IMPOSSIBLE_CACTUS = BLOCKS.register("impossible_cactus", () -> new ImpossibleCactusBlock(Block.Properties.of(Material.CACTUS).randomTicks().strength(0.4F).sound(SoundType.WOOL)));

    public static final RegistryObject<Item> OFFSET_STONE_ITEM = ITEMS.register("offset_stone", () -> new BlockItem(OFFSET_STONE.get(), new Item.Properties().tab(IllegalTabs.ILLEGAL_TAB)));
    public static final RegistryObject<Item> IMPOSSIBLE_OAK_LOG_ITEM = ITEMS.register("impossible_oak_log", () -> new BlockItem(IMPOSSIBLE_OAK_LOG.get(), new Item.Properties().tab(IllegalTabs.ILLEGAL_TAB)));
    public static final RegistryObject<Item> IMPOSSIBLE_SPRUCE_LOG_ITEM = ITEMS.register("impossible_spruce_log", () -> new BlockItem(IMPOSSIBLE_SPRUCE_LOG.get(), new Item.Properties().tab(IllegalTabs.ILLEGAL_TAB)));
    public static final RegistryObject<Item> IMPOSSIBLE_BIRCH_LOG_ITEM = ITEMS.register("impossible_birch_log", () -> new BlockItem(IMPOSSIBLE_BIRCH_LOG.get(), new Item.Properties().tab(IllegalTabs.ILLEGAL_TAB)));
    public static final RegistryObject<Item> IMPOSSIBLE_JUNGLE_LOG_ITEM = ITEMS.register("impossible_jungle_log", () -> new BlockItem(IMPOSSIBLE_JUNGLE_LOG.get(), new Item.Properties().tab(IllegalTabs.ILLEGAL_TAB)));
    public static final RegistryObject<Item> IMPOSSIBLE_ACACIA_LOG_ITEM = ITEMS.register("impossible_acacia_log", () -> new BlockItem(IMPOSSIBLE_ACACIA_LOG.get(), new Item.Properties().tab(IllegalTabs.ILLEGAL_TAB)));
    public static final RegistryObject<Item> IMPOSSIBLE_DARK_OAK_LOG_ITEM = ITEMS.register("impossible_dark_oak_log", () -> new BlockItem(IMPOSSIBLE_DARK_OAK_LOG.get(), new Item.Properties().tab(IllegalTabs.ILLEGAL_TAB)));
    public static final RegistryObject<Item> IMPOSSIBLE_SAND_ITEM = ITEMS.register("impossible_sand", () -> new BlockItem(IMPOSSIBLE_SAND.get(), new Item.Properties().tab(IllegalTabs.ILLEGAL_TAB)));
    public static final RegistryObject<Item> IMPOSSIBLE_RED_SAND_ITEM = ITEMS.register("impossible_red_sand", () -> new BlockItem(IMPOSSIBLE_RED_SAND.get(), new Item.Properties().tab(IllegalTabs.ILLEGAL_TAB)));
    public static final RegistryObject<Item> IMPOSSIBLE_SUGAR_CANE_ITEM = ITEMS.register("impossible_sugar_cane", () -> new BlockItem(IMPOSSIBLE_SUGAR_CANE.get(), new Item.Properties().tab(IllegalTabs.ILLEGAL_TAB)));
    public static final RegistryObject<Item> IMPOSSIBLE_CACTUS_ITEM = ITEMS.register("impossible_cactus", () -> new BlockItem(IMPOSSIBLE_CACTUS.get(), new Item.Properties().tab(IllegalTabs.ILLEGAL_TAB)));

    public static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> builder) {
        return builder.build(id);
    }
}
