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
        if (worldIn.isAirBlock(pos.down())) {
            int i;
            for(i = 1; worldIn.getBlockState(pos.up(i)).matchesBlock(this); ++i) {
            }

            if (i < 3) {
                int j = state.get(AGE);
                if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, true)) {
                    if (j == 15) {
                        worldIn.setBlockState(pos.down(), this.getDefaultState());
                        worldIn.setBlockState(pos, state.with(AGE, Integer.valueOf(0)), 4);
                    } else {
                        worldIn.setBlockState(pos, state.with(AGE, Integer.valueOf(j + 1)), 4);
                    }
                }
            }
        }
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        BlockState soil = worldIn.getBlockState(pos.up());
        if (soil.canSustainPlant(worldIn, pos.up(), Direction.DOWN, this)) return true;
        BlockState blockstate = worldIn.getBlockState(pos.up());
        if (blockstate.getBlock() == this) {
            return true;
        } else {
            if (blockstate.matchesBlock(Blocks.GRASS_BLOCK) || blockstate.matchesBlock(Blocks.DIRT) || blockstate.matchesBlock(Blocks.COARSE_DIRT) ||
                    blockstate.matchesBlock(Blocks.PODZOL) || blockstate.getBlock() == IllegalRegistry.IMPOSSIBLE_SAND.get() ||
                    blockstate.getBlock() == IllegalRegistry.IMPOSSIBLE_RED_SAND.get()) {
                BlockPos blockpos = pos.up();

                for(Direction direction : Direction.Plane.HORIZONTAL) {
                    BlockState blockstate1 = worldIn.getBlockState(blockpos.offset(direction));
                    FluidState fluidstate = worldIn.getFluidState(blockpos.offset(direction));
                    if (fluidstate.isTagged(FluidTags.WATER) || blockstate1.matchesBlock(Blocks.FROSTED_ICE)) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    @Override
    public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing, IPlantable plantable) {
        BlockState plant = plantable.getPlant(world, pos.offset(facing));
        if (plant.getBlock() == this)
            return true;

        return super.canSustainPlant(state, world, pos, facing, plantable);
    }
}
