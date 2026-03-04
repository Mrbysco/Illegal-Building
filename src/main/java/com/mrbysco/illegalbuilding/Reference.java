package com.mrbysco.illegalbuilding;

import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class Reference {
	public static final String MOD_ID = "illegalbuilding";
	public static final String MOD_PREFIX = MOD_ID + ":";

	public static Identifier modLoc(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	public static final TagKey<Block> IMPOSSIBLE_SAND = BlockTags.create(modLoc("impossible_sand"));
	public static final TagKey<Item> IMPOSSIBLE_SAND_ITEM = ItemTags.create(modLoc("impossible_sand"));
}
