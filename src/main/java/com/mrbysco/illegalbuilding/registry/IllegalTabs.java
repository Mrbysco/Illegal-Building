package com.mrbysco.illegalbuilding.registry;

import com.mrbysco.illegalbuilding.Reference;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class IllegalTabs {
    public static final ItemGroup ILLEGAL_TAB = new ItemGroup(Reference.MOD_ID) {
        @OnlyIn(Dist.CLIENT)
        public ItemStack createIcon() {
            return new ItemStack(IllegalRegistry.OFFSET_STONE.get());
        }
    };
}
