package com.mrbysco.illegalbuilding.blocks;

import com.mrbysco.illegalbuilding.registry.IllegalRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.TriState;

public class ImpossibleSandBlock extends ImpossibleFallingBlock {
	private final int dustColor;

	public ImpossibleSandBlock(int color, Block.Properties properties) {
		super(properties);
		this.dustColor = color;
	}

	@Override
	public int getDustColor(BlockState state, BlockGetter getter, BlockPos pos) {
		return this.dustColor;
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