package com.mrbysco.illegalbuilding.handler;

import com.mrbysco.illegalbuilding.blocks.ImpossibleSandBlock;
import com.mrbysco.illegalbuilding.registry.IllegalRegistry;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RightClickHandler {
	@SubscribeEvent
	public void onRightClick(RightClickBlock event) {
		World world = event.getWorld();
		BlockPos pos = event.getPos();
		BlockState state = world.getBlockState(pos);
		ItemStack stack = event.getItemStack();
		if(state.getBlock() instanceof ImpossibleSandBlock) {
			BlockRayTraceResult hitVec = event.getHitVec();
			BlockPos downPos = pos.down();
			if(hitVec.getFace() == Direction.DOWN) {
				if(world.getBlockState(downPos).getMaterial().isReplaceable()) {
					PlayerEntity player = event.getPlayer();
					if (stack.getItem() == Items.SUGAR_CANE) {
						BlockState sugarCane = IllegalRegistry.IMPOSSIBLE_SUGAR_CANE.get().getDefaultState();
						if(sugarCane.isValidPosition(world, downPos)) {
							world.setBlockState(downPos, sugarCane, 11);
							sugarCane.getBlock().onBlockPlacedBy(world, downPos, sugarCane, player, new ItemStack(IllegalRegistry.IMPOSSIBLE_SUGAR_CANE_ITEM.get()));
							if (player instanceof ServerPlayerEntity) {
								CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity)player, downPos, stack);
							}
							SoundType soundtype = sugarCane.getSoundType(world, pos, player);
							world.playSound(player, downPos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);

							if (player == null || !player.abilities.isCreativeMode) {
								stack.shrink(1);
							}
						}
					}
					if (stack.getItem() == Items.CACTUS) {
						BlockState cactusState = IllegalRegistry.IMPOSSIBLE_CACTUS.get().getDefaultState();
						if(cactusState.isValidPosition(world, downPos)) {
							world.setBlockState(downPos, cactusState, 11);
							cactusState.getBlock().onBlockPlacedBy(world, downPos, cactusState, player, new ItemStack(IllegalRegistry.IMPOSSIBLE_SUGAR_CANE_ITEM.get()));
							if (player instanceof ServerPlayerEntity) {
								CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity)player, downPos, stack);
							}
							SoundType soundtype = cactusState.getSoundType(world, pos, player);
							world.playSound(player, downPos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);

							if (player == null || !player.abilities.isCreativeMode) {
								stack.shrink(1);
							}
						}
					}
				}
			}
		}
	}
}
