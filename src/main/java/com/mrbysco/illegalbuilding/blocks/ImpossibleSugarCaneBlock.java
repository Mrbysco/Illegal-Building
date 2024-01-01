package com.mrbysco.illegalbuilding.blocks;

import com.mrbysco.illegalbuilding.registry.IllegalRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.common.IPlantable;
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
				if (CommonHooks.onCropsGrowPre(level, pos, state, true)) {
					if (j == 15) {
						level.setBlockAndUpdate(pos.below(), this.defaultBlockState());
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
		BlockState soil = level.getBlockState(pos.above());
		if (soil.canSustainPlant(level, pos.above(), Direction.DOWN, this)) return true;
		BlockState blockstate = level.getBlockState(pos.above());
		if (blockstate.getBlock() == this) {
			return true;
		} else {
			if (blockstate.is(Blocks.GRASS_BLOCK) || blockstate.is(Blocks.DIRT) || blockstate.is(Blocks.COARSE_DIRT) ||
					blockstate.is(Blocks.PODZOL) || blockstate.getBlock() == IllegalRegistry.IMPOSSIBLE_SAND.get() ||
					blockstate.getBlock() == IllegalRegistry.IMPOSSIBLE_RED_SAND.get()) {
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
	public boolean canSustainPlant(BlockState state, BlockGetter blockGetter, BlockPos pos, Direction facing, IPlantable plantable) {
		BlockState plant = plantable.getPlant(blockGetter, pos.relative(facing));
		if (plant.getBlock() == this)
			return true;

		return super.canSustainPlant(state, blockGetter, pos, facing, plantable);
	}
}
