package com.mrbysco.illegalbuilding.registry;

import com.mrbysco.illegalbuilding.Reference;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class IllegalTabs {

	private static CreativeModeTab ILLEGAL_TAB;

	@SubscribeEvent
	public void registerCreativeTabs(final CreativeModeTabEvent.Register event) {
		ILLEGAL_TAB = event.registerCreativeModeTab(new ResourceLocation(Reference.MOD_ID, "tab"), builder ->
				builder.icon(() -> new ItemStack(IllegalRegistry.OFFSET_STONE.get()))
						.title(Component.translatable("itemGroup.illegalbuilding"))
						.displayItems((displayParameters, output) -> {
							List<ItemStack> stacks = IllegalRegistry.ITEMS.getEntries().stream().map(reg -> new ItemStack(reg.get())).toList();
							output.acceptAll(stacks);
						}));
	}
}
