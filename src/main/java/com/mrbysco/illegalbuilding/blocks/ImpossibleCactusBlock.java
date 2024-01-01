package com.mrbysco.illegalbuilding.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.IPlantable;
import net.neoforged.neoforge.common.CommonHooks;

public class ImpossibleCactusBlock extends CactusBlock {
	public ImpossibleCactusBlock(Properties builder) {
		super(builder);
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
		if (!level.isAreaLoaded(pos, 1))
			return; // Forge: prevent growing cactus from loading unloaded chunks with block update
		if (!state.canSurvive(level, pos)) {
			level.destroyBlock(pos, true);
		}
	}

	@Override
	public void randomTick(BlockState state, ServerLevel serverLevel, BlockPos pos, RandomSource random) {
		BlockPos blockpos = pos.below();
		if (serverLevel.isEmptyBlock(blockpos)) {
			int i;
			for (i = 1; serverLevel.getBlockState(pos.above(i)).is(this); ++i) {
			}

			if (i < 3) {
				int j = state.getValue(AGE);
				if (CommonHooks.onCropsGrowPre(serverLevel, blockpos, state, true)) {
					if (j == 15) {
						serverLevel.setBlockAndUpdate(blockpos, this.defaultBlockState());
						BlockState blockstate = state.setValue(AGE, Integer.valueOf(0));
						serverLevel.setBlock(pos, blockstate, 4);
						blockstate.neighborChanged(serverLevel, blockpos, this, pos, false);
					} else {
						serverLevel.setBlock(pos, state.setValue(AGE, Integer.valueOf(j + 1)), 4);
					}
					CommonHooks.onCropsGrowPost(serverLevel, pos, state);
				}
			}
		}
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		for (Direction direction : Direction.Plane.HORIZONTAL) {
			BlockState blockstate = level.getBlockState(pos.relative(direction));
			if (blockstate.isSolid() || level.getFluidState(pos.relative(direction)).is(FluidTags.LAVA)) {
				return false;
			}
		}

		BlockState soil = level.getBlockState(pos.above());
		return soil.canSustainPlant(level, pos, Direction.UP, this) &&
				!level.getBlockState(pos.above()).liquid();
	}

	@Override
	public boolean canSustainPlant(BlockState state, BlockGetter blockGetter, BlockPos pos, Direction facing, IPlantable plantable) {
		BlockState plant = plantable.getPlant(blockGetter, pos.relative(facing));
		if (plant.getBlock() == this)
			return true;

		return super.canSustainPlant(state, blockGetter, pos, facing, plantable);
	}
}
