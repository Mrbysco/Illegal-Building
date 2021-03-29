package com.mrbysco.illegalbuilding.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class OffsetBlock extends DirectionalBlock {
    protected static final VoxelShape OFFSET_NORTH_AABB = Block.makeCuboidShape(0.0D, 0.0D, -8.0D, 16.0D, 16.0D, 8.0D);
    protected static final VoxelShape OFFSET_SOUTH_AABB = Block.makeCuboidShape(0.0D, 0.0D, 24.0D, 16.0D, 16.0D, 8.0D);
    protected static final VoxelShape OFFSET_WEST_AABB = Block.makeCuboidShape(-8.0D, 0.0D, 0.0D, 8.0D, 16.0D, 16.0D);
    protected static final VoxelShape OFFSET_EAST_AABB = Block.makeCuboidShape(24.0D, 0.0D, 0.0D, 8.0D, 16.0D, 16.0D);
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

    public OffsetBlock(Block.Properties builder) {
        super(builder);
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));
    }

    public static boolean isntSolid(BlockState state, IBlockReader reader, BlockPos pos) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        Direction facing = (Direction)state.get(FACING);

        switch (facing){
            default:
                return OFFSET_NORTH_AABB;
            case SOUTH:
                return OFFSET_SOUTH_AABB;
            case WEST:
                return OFFSET_WEST_AABB;
            case EAST:
                return OFFSET_EAST_AABB;
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return true;
    }
}
