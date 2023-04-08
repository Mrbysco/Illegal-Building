package com.mrbysco.illegalbuilding.blocks;

import com.mrbysco.illegalbuilding.registry.IllegalRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.common.IPlantable;

import java.util.Random;

public class ImpossibleSugarCaneBlock extends SugarCaneBlock {
	public ImpossibleSugarCaneBlock(Block.Properties builder) {
		super(builder);
	}

	@Override
	public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, Random random) {
		if (worldIn.isEmptyBlock(pos.below())) {
			int i;
			for (i = 1; worldIn.getBlockState(pos.above(i)).is(this); ++i) {
			}

			if (i < 3) {
				int j = state.getValue(AGE);
				if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, true)) {
					if (j == 15) {
						worldIn.setBlockAndUpdate(pos.below(), this.defaultBlockState());
						worldIn.setBlock(pos, state.setValue(AGE, Integer.valueOf(0)), 4);
					} else {
						worldIn.setBlock(pos, state.setValue(AGE, Integer.valueOf(j + 1)), 4);
					}
				}
			}
		}
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
		BlockState soil = worldIn.getBlockState(pos.above());
		if (soil.canSustainPlant(worldIn, pos.above(), Direction.DOWN, this)) return true;
		BlockState blockstate = worldIn.getBlockState(pos.above());
		if (blockstate.getBlock() == this) {
			return true;
		} else {
			if (blockstate.is(Blocks.GRASS_BLOCK) || blockstate.is(Blocks.DIRT) || blockstate.is(Blocks.COARSE_DIRT) ||
					blockstate.is(Blocks.PODZOL) || blockstate.getBlock() == IllegalRegistry.IMPOSSIBLE_SAND.get() ||
					blockstate.getBlock() == IllegalRegistry.IMPOSSIBLE_RED_SAND.get()) {
				BlockPos blockpos = pos.above();

				for (Direction direction : Direction.Plane.HORIZONTAL) {
					BlockState blockstate1 = worldIn.getBlockState(blockpos.relative(direction));
					FluidState fluidstate = worldIn.getFluidState(blockpos.relative(direction));
					if (fluidstate.is(FluidTags.WATER) || blockstate1.is(Blocks.FROSTED_ICE)) {
						return true;
					}
				}
			}

			return false;
		}
	}

	@Override
	public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, IPlantable plantable) {
		BlockState plant = plantable.getPlant(world, pos.relative(facing));
		if (plant.getBlock() == this)
			return true;

		return super.canSustainPlant(state, world, pos, facing, plantable);
	}
}
