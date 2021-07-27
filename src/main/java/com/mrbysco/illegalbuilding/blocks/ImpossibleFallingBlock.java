package com.mrbysco.illegalbuilding.blocks;

import com.mrbysco.illegalbuilding.entity.ImpossibleFallingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class ImpossibleFallingBlock extends FallingBlock {

    public ImpossibleFallingBlock(Block.Properties builder) {
        super(builder);
    }

    @Override
    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, Random rand) {
        if (worldIn.isEmptyBlock(pos.above()) || isFree(worldIn.getBlockState(pos.above())) && pos.getY() <= 256) {
            ImpossibleFallingBlockEntity fallingblockentity = new ImpossibleFallingBlockEntity(worldIn, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, worldIn.getBlockState(pos));
            this.onStartFalling(fallingblockentity);
            worldIn.addFreshEntity(fallingblockentity);
        }
    }

    protected void onStartFalling(ImpossibleFallingBlockEntity fallingEntity) {
    }

    public void onEndFalling(Level worldIn, BlockPos pos, BlockState fallingState, BlockState hitState, ImpossibleFallingBlockEntity fallingBlock) {
    }

    public void onBroken(Level worldIn, BlockPos pos, ImpossibleFallingBlockEntity fallingBlock) {
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, Random rand) {
        if (rand.nextInt(16) == 0) {
            BlockPos blockpos = pos.above();
            if (worldIn.isEmptyBlock(blockpos) || isFree(worldIn.getBlockState(blockpos))) {
                double d0 = (double)pos.getX() + rand.nextDouble();
                double d1 = (double)pos.getY() - 0.05D;
                double d2 = (double)pos.getZ() + rand.nextDouble();
                worldIn.addParticle(new BlockParticleOption(ParticleTypes.FALLING_DUST, stateIn), d0, d1, d2, 0.0D, 0.0D, 0.0D);
            }
        }
    }
}
