package com.mrbysco.illegalbuilding.blocks;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrbysco.illegalbuilding.entity.ImpossibleFallingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ColorRGBA;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ColoredFallingBlock;
import net.minecraft.world.level.block.state.BlockState;

public class ImpossibleColoredFallingBlock extends ColoredFallingBlock {
	public static final MapCodec<ImpossibleColoredFallingBlock> CODEC = RecordCodecBuilder.mapCodec(
			instance -> instance.group(ColorRGBA.CODEC.fieldOf("falling_dust_color")
							.forGetter(fallingBlock -> fallingBlock.dustColor), propertiesCodec())
					.apply(instance, ImpossibleColoredFallingBlock::new)
	);

	public MapCodec<ImpossibleColoredFallingBlock> codec() {
		return CODEC;
	}

	public ImpossibleColoredFallingBlock(ColorRGBA dustColor, Block.Properties builder) {
		super(dustColor, builder);
	}


	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (isFree(level.getBlockState(pos.above())) && pos.getY() <= level.getMaxY()) {
			ImpossibleFallingBlockEntity fallingblockentity = ImpossibleFallingBlockEntity.fall(level, pos, state);
			this.onImpossibleFalling(fallingblockentity);
		}
	}

	protected void onImpossibleFalling(ImpossibleFallingBlockEntity fallingEntity) {

	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
		if (rand.nextInt(16) == 0) {
			BlockPos blockpos = pos.below();
			if (isFree(level.getBlockState(blockpos))) {
				ParticleUtils.spawnParticleBelow(level, pos, rand, new BlockParticleOption(ParticleTypes.FALLING_DUST, state));
			}
		}
	}
}
