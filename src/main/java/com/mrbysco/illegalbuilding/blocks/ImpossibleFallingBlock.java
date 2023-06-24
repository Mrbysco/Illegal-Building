package com.mrbysco.illegalbuilding.blocks;

import com.mrbysco.illegalbuilding.entity.ImpossibleFallingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
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
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
		if (level.isEmptyBlock(pos.above()) || isFree(level.getBlockState(pos.above())) && pos.getY() <= 256) {
			ImpossibleFallingBlockEntity impossibleFallingBlockEntity = new ImpossibleFallingBlockEntity(level, (double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, level.getBlockState(pos));
			this.onStartFalling(impossibleFallingBlockEntity);
			level.addFreshEntity(impossibleFallingBlockEntity);
		}
	}

	protected void onStartFalling(ImpossibleFallingBlockEntity fallingEntity) {
	}

	public void onEndFalling(Level level, BlockPos pos, BlockState fallingState, BlockState hitState, ImpossibleFallingBlockEntity fallingBlock) {
	}

	public void onBroken(Level level, BlockPos pos, ImpossibleFallingBlockEntity fallingBlock) {
	}

	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, Level level, BlockPos pos, Random rand) {
		if (rand.nextInt(16) == 0) {
			BlockPos blockpos = pos.above();
			if (level.isEmptyBlock(blockpos) || isFree(level.getBlockState(blockpos))) {
				double d0 = (double) pos.getX() + rand.nextDouble();
				double d1 = (double) pos.getY() - 0.05D;
				double d2 = (double) pos.getZ() + rand.nextDouble();
				level.addParticle(new BlockParticleOption(ParticleTypes.FALLING_DUST, stateIn), d0, d1, d2, 0.0D, 0.0D, 0.0D);
			}
		}
	}
}
