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
    protected static final VoxelShape OFFSET_NORTH_AABB = Block.box(0.0D, 0.0D, -8.0D, 16.0D, 16.0D, 8.0D);
    protected static final VoxelShape OFFSET_SOUTH_AABB = Block.box(0.0D, 0.0D, 8D, 16.0D, 16.0D, 24.0D);
    protected static final VoxelShape OFFSET_WEST_AABB = Block.box(-8.0D, 0.0D, 0.0D, 8.0D, 16.0D, 16.0D);
    protected static final VoxelShape OFFSET_EAST_AABB = Block.box(8.0D, 0.0D, 0.0D, 24.0D, 16.0D, 16.0D);
    public static final DirectionProperty FACING = HorizontalBlock.FACING;

    public OffsetBlock(Block.Properties builder) {
        super(builder);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    public static boolean isntSolid(BlockState state, IBlockReader reader, BlockPos pos) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        Direction facing = (Direction)state.getValue(FACING);

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
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return true;
    }
}
