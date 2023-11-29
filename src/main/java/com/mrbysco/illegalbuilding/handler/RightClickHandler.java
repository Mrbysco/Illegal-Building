package com.mrbysco.illegalbuilding.handler;

import com.mrbysco.illegalbuilding.blocks.ImpossibleSandBlock;
import com.mrbysco.illegalbuilding.registry.IllegalRegistry;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.neoforged.bus.api.SubscribeEvent;

public class RightClickHandler {
	@SubscribeEvent
	public void onRightClick(RightClickBlock event) {
		Level level = event.getLevel();
		BlockPos pos = event.getPos();
		BlockState state = level.getBlockState(pos);
		ItemStack stack = event.getItemStack();
		if (state.getBlock() instanceof ImpossibleSandBlock) {
			BlockHitResult hitVec = event.getHitVec();
			BlockPos downPos = pos.below();
			if (hitVec.getDirection() == Direction.DOWN && level.getBlockState(downPos).canBeReplaced()) {
				Player player = event.getEntity();
				if (stack.getItem() == Items.SUGAR_CANE) {
					BlockState sugarCane = IllegalRegistry.IMPOSSIBLE_SUGAR_CANE.get().defaultBlockState();
					if (sugarCane.canSurvive(level, downPos)) {
						level.setBlock(downPos, sugarCane, 11);
						sugarCane.getBlock().setPlacedBy(level, downPos, sugarCane, player, new ItemStack(IllegalRegistry.IMPOSSIBLE_SUGAR_CANE.get()));
						if (player instanceof ServerPlayer) {
							CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, downPos, stack);
						}
						SoundType soundtype = sugarCane.getSoundType(level, pos, player);
						level.playSound(player, downPos, soundtype.getPlaceSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);

						if (player == null || !player.getAbilities().instabuild) {
							stack.shrink(1);
						}
					}
				}
				if (stack.getItem() == Items.CACTUS) {
					BlockState cactusState = IllegalRegistry.IMPOSSIBLE_CACTUS.get().defaultBlockState();
					if (cactusState.canSurvive(level, downPos)) {
						level.setBlock(downPos, cactusState, 11);
						cactusState.getBlock().setPlacedBy(level, downPos, cactusState, player, new ItemStack(IllegalRegistry.IMPOSSIBLE_CACTUS.get()));
						if (player instanceof ServerPlayer) {
							CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, downPos, stack);
						}
						SoundType soundtype = cactusState.getSoundType(level, pos, player);
						level.playSound(player, downPos, soundtype.getPlaceSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);

						if (player == null || !player.getAbilities().instabuild) {
							stack.shrink(1);
						}
					}
				}
			}
		}
	}
}
