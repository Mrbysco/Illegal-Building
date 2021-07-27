package com.mrbysco.illegalbuilding.blocks;

import com.mrbysco.illegalbuilding.registry.IllegalRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.fluid.FluidState;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.IPlantable;

import java.util.Random;

public class ImpossibleSugarCaneBlock extends SugarCaneBlock {
    public ImpossibleSugarCaneBlock(Block.Properties builder) {
        super(builder);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        if (worldIn.isEmptyBlock(pos.below())) {
            int i;
            for(i = 1; worldIn.getBlockState(pos.above(i)).is(this); ++i) {
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
    public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos) {
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

                for(Direction direction : Direction.Plane.HORIZONTAL) {
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
    public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing, IPlantable plantable) {
        BlockState plant = plantable.getPlant(world, pos.relative(facing));
        if (plant.getBlock() == this)
            return true;

        return super.canSustainPlant(state, world, pos, facing, plantable);
    }
}
