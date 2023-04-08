package com.mrbysco.illegalbuilding.blocks;

import com.mrbysco.illegalbuilding.registry.IllegalRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.IPlantable;

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
	public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, IPlantable plantable) {
		BlockState plant = plantable.getPlant(world, pos.relative(facing));
		return plant.getBlock() == IllegalRegistry.IMPOSSIBLE_CACTUS.get() || plant.getBlock() == IllegalRegistry.IMPOSSIBLE_SUGAR_CANE.get();
	}
}