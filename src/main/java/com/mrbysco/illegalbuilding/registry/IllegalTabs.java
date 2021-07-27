package com.mrbysco.illegalbuilding.registry;

import com.mrbysco.illegalbuilding.Reference;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class IllegalTabs {
    public static final CreativeModeTab ILLEGAL_TAB = new CreativeModeTab(Reference.MOD_ID) {
        @OnlyIn(Dist.CLIENT)
        public ItemStack makeIcon() {
            return new ItemStack(IllegalRegistry.OFFSET_STONE.get());
        }
    };
}
