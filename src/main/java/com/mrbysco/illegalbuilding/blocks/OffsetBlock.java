package com.mrbysco.illegalbuilding.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class OffsetBlock extends DirectionalBlock {
	public static final MapCodec<OffsetBlock> CODEC = simpleCodec(OffsetBlock::new);

	protected static final VoxelShape OFFSET_NORTH_AABB = createBox(0.0D, 0.0D, -8.0D, 16.0D, 16.0D, 8.0D);
	protected static final VoxelShape OFFSET_SOUTH_AABB = createBox(0.0D, 0.0D, 8D, 16.0D, 16.0D, 24.0D);
	protected static final VoxelShape OFFSET_WEST_AABB = createBox(-8.0D, 0.0D, 0.0D, 8.0D, 16.0D, 16.0D);
	protected static final VoxelShape OFFSET_EAST_AABB = createBox(8.0D, 0.0D, 0.0D, 24.0D, 16.0D, 16.0D);
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

	/**
	 * Required as Shapes.box() doesn't allow the values I gave it :)
	 */
	private static VoxelShape createBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
		return Shapes.create(minX / 16.0D, minY / 16.0D, minZ / 16.0D, maxX / 16.0D, maxY / 16.0D, maxZ / 16.0D);
	}


	public MapCodec<OffsetBlock> codec() {
		return CODEC;
	}

	public OffsetBlock(Block.Properties builder) {
		super(builder);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	public static boolean isntSolid(BlockState state, BlockGetter reader, BlockPos pos) {
		return true;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		Direction facing = state.getValue(FACING);

		return switch (facing) {
			default -> OFFSET_NORTH_AABB;
			case SOUTH -> OFFSET_SOUTH_AABB;
			case WEST -> OFFSET_WEST_AABB;
			case EAST -> OFFSET_EAST_AABB;
		};
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
		return true;
	}
}
