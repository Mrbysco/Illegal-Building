package com.mrbysco.illegalbuilding.blocks;

import com.mrbysco.illegalbuilding.registry.IllegalRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ColorRGBA;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TriState;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.sounds.AmbientDesertBlockSoundsPlayer;
import net.minecraft.world.level.block.state.BlockState;

public class ImpossibleSandBlock extends ImpossibleColoredFallingBlock {

	public ImpossibleSandBlock(ColorRGBA color, Block.Properties properties) {
		super(color, properties);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
		AmbientDesertBlockSoundsPlayer.playAmbientSandSounds(level, pos, rand);
	}

	@Override
	public TriState canSustainPlant(BlockState state, BlockGetter level, BlockPos soilPosition, Direction facing, BlockState plant) {
		if (plant.getBlock() == IllegalRegistry.IMPOSSIBLE_CACTUS.get() ||
				plant.getBlock() == IllegalRegistry.IMPOSSIBLE_SUGAR_CANE.get()) {
			return TriState.TRUE;
		}
		return super.canSustainPlant(state, level, soilPosition, facing, plant);
	}
}