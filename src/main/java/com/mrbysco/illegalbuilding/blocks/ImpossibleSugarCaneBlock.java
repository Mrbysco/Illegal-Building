package com.mrbysco.illegalbuilding.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TriState;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.common.CommonHooks;

public class ImpossibleSugarCaneBlock extends SugarCaneBlock {
	public ImpossibleSugarCaneBlock(Block.Properties builder) {
		super(builder);
	}

	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (level.isEmptyBlock(pos.below())) {
			int i;
			for (i = 1; level.getBlockState(pos.above(i)).is(this); ++i) {
			}

			if (i < 3) {
				int j = state.getValue(AGE);
				if (CommonHooks.canCropGrow(level, pos, state, true)) {
					if (j == 15) {
						level.setBlockAndUpdate(pos.below(), this.defaultBlockState());
						net.neoforged.neoforge.common.CommonHooks.fireCropGrowPost(level, pos, this.defaultBlockState());
						level.setBlock(pos, state.setValue(AGE, Integer.valueOf(0)), 4);
					} else {
						level.setBlock(pos, state.setValue(AGE, Integer.valueOf(j + 1)), 4);
					}
				}
			}
		}
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		BlockState blockstate = level.getBlockState(pos.above());
		if (blockstate.getBlock() == this) {
			return true;
		} else {
			if (blockstate.canSustainPlant(level, pos, Direction.UP, state).isTrue()) {
				BlockPos blockpos = pos.above();

				for (Direction direction : Direction.Plane.HORIZONTAL) {
					BlockState blockstate1 = level.getBlockState(blockpos.relative(direction));
					FluidState fluidstate = level.getFluidState(blockpos.relative(direction));
					if (fluidstate.is(FluidTags.WATER) || blockstate1.is(Blocks.FROSTED_ICE)) {
						return true;
					}
				}
			}

			return false;
		}
	}

	@Override
	public TriState canSustainPlant(BlockState state, BlockGetter level, BlockPos soilPosition, Direction facing, BlockState plant) {
		if (plant.getBlock() == this)
			return TriState.TRUE;
		return super.canSustainPlant(state, level, soilPosition, facing, plant);
	}
}
