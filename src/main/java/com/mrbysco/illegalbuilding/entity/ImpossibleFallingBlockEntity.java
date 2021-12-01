package com.mrbysco.illegalbuilding.entity;

import com.mrbysco.illegalbuilding.blocks.ImpossibleFallingBlock;
import com.mrbysco.illegalbuilding.registry.IllegalRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ConcretePowderBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages.SpawnEntity;

public class ImpossibleFallingBlockEntity extends FallingBlockEntity {
    public boolean onRoof;

    public ImpossibleFallingBlockEntity(Level worldIn, double x, double y, double z, BlockState fallingBlockState) {
        super(IllegalRegistry.IMPOSSIBLE_FALLING_BLOCK.get(), worldIn);
        this.blockState = fallingBlockState;
        this.blocksBuilding = true;
        this.setPos(x, y + (double)((1.0F - this.getBbHeight()) / 2.0F), z);
        this.setDeltaMovement(Vec3.ZERO);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.setStartPos(this.blockPosition());
    }

    public ImpossibleFallingBlockEntity(EntityType<? extends FallingBlockEntity> p_i50218_1_, Level world) {
        super(p_i50218_1_, world);
    }

    public ImpossibleFallingBlockEntity(SpawnEntity spawnEntity, Level worldIn) {
        this(IllegalRegistry.IMPOSSIBLE_FALLING_BLOCK.get(), worldIn);
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick() {
        if (this.blockState.isAir()) {
            this.discard();
        } else {
            Block block = this.blockState.getBlock();
            if (this.time++ == 0) {
                BlockPos blockpos = this.blockPosition();
                if (this.level.getBlockState(blockpos).is(block)) {
                    this.level.removeBlock(blockpos, false);
                } else if (!this.level.isClientSide) {
                    this.discard();
                    return;
                }
            }

            if (!this.isNoGravity()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, 0.04D, 0.0D));
            }

            this.move(MoverType.SELF, this.getDeltaMovement());
            if (!this.level.isClientSide) {
                BlockPos blockpos1 = this.blockPosition();
                boolean flag = this.blockState.getBlock() instanceof ConcretePowderBlock;
                boolean flag1 = flag && this.level.getFluidState(blockpos1).is(FluidTags.WATER);
                double d0 = this.getDeltaMovement().lengthSqr();
                if (flag && d0 > 1.0D) {
                    BlockHitResult blockraytraceresult = this.level.clip(new ClipContext(new Vec3(this.xo, this.yo, this.zo), this.position(), ClipContext.Block.COLLIDER, ClipContext.Fluid.SOURCE_ONLY, this));
                    if (blockraytraceresult.getType() != HitResult.Type.MISS && this.level.getFluidState(blockraytraceresult.getBlockPos()).is(FluidTags.WATER)) {
                        blockpos1 = blockraytraceresult.getBlockPos();
                        flag1 = true;
                    }
                }

                if (!this.onRoof && !flag1) {
                    if (!this.level.isClientSide && (this.time > 100 && (blockpos1.getY() < 1 || blockpos1.getY() > 256) || this.time > 600)) {
                        if (this.dropItem && this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                            this.spawnAtLocation(block);
                        }

                        this.discard();
                    }
                } else {
                    BlockState blockstate = this.level.getBlockState(blockpos1);
                    this.setDeltaMovement(this.getDeltaMovement().multiply(0.7D, -0.5D, 0.7D));
                    if (!blockstate.is(Blocks.MOVING_PISTON)) {
                        this.discard();
                        if (!this.cancelDrop) {
                            boolean flag2 = blockstate.canBeReplaced(new DirectionalPlaceContext(this.level, blockpos1, Direction.DOWN, ItemStack.EMPTY, Direction.UP));
                            boolean flag3 = ImpossibleFallingBlock.isFree(this.level.getBlockState(blockpos1.above())) && (!flag || !flag1);
                            if(flag3) {
                                this.onRoof = false;
                            }
                            boolean flag4 = this.blockState.canSurvive(this.level, blockpos1) && !flag3;
                            if (flag2 && flag4) {
                                if (this.blockState.hasProperty(BlockStateProperties.WATERLOGGED) && this.level.getFluidState(blockpos1).getType() == Fluids.WATER) {
                                    this.blockState = this.blockState.setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(true));
                                }

                                if (this.level.setBlock(blockpos1, this.blockState, 3)) {
                                    if (block instanceof ImpossibleFallingBlock) {
                                        ((ImpossibleFallingBlock)block).onEndFalling(this.level, blockpos1, this.blockState, blockstate, this);
                                    }

                                    if (this.blockData != null && this.blockState.hasBlockEntity()) {
                                        BlockEntity tileentity = this.level.getBlockEntity(blockpos1);
                                        if (tileentity != null) {
                                            CompoundTag compoundnbt = tileentity.save(new CompoundTag());

                                            for(String s : this.blockData.getAllKeys()) {
                                                Tag inbt = this.blockData.get(s);
                                                if (!"x".equals(s) && !"y".equals(s) && !"z".equals(s)) {
                                                    compoundnbt.put(s, inbt.copy());
                                                }
                                            }

                                            try {
                                                tileentity.load(compoundnbt);
                                            } catch (Exception var16) {
                                                LOGGER.error("Failed to load block entity from impossible falling block", var16);
                                            }

                                            tileentity.setChanged();
                                        }
                                    }
                                } else if (this.dropItem && this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                                    this.discard();
                                    this.callOnBrokenAfterFall(block, blockpos1);
                                    this.spawnAtLocation(block);
                                }
                            } else if (this.dropItem && this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                                this.discard();
                                this.callOnBrokenAfterFall(block, blockpos1);
                                this.spawnAtLocation(block);
                            }
                        } else if (block instanceof ImpossibleFallingBlock) {
                            this.discard();
                            this.callOnBrokenAfterFall(block, blockpos1);
                            ((ImpossibleFallingBlock)block).onBroken(this.level, blockpos1, this);
                        }
                    }
                }
            }

            this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));
        }
    }


    @Override
    public void move(MoverType typeIn, Vec3 pos) {
        super.move(typeIn, pos);
        if (!this.noPhysics) {
            this.onRoof = this.verticalCollision && pos.y > 0.0D;

            int x = Mth.floor(this.getX());
            int y = Mth.floor(this.getY() + (double)0.2F);
            int z = Mth.floor(this.getZ());
            BlockPos blockpos = new BlockPos(x, y, z);
            BlockState blockstate = this.level.getBlockState(blockpos);
            if (blockstate.isAir()) {
                BlockPos blockpos1 = blockpos.below();
                BlockState blockstate1 = this.level.getBlockState(blockpos1);
                if (blockstate1.is(BlockTags.FENCES) || blockstate1.is(BlockTags.WALLS) || blockstate1.is(BlockTags.FENCE_GATES)) {
                    blockstate = blockstate1;
                    blockpos = blockpos1;
                }
            }

            this.checkFallDamage(y, this.verticalCollision, blockstate, blockpos);
        }
    }

    @Override
    public CompoundTag saveWithoutId(CompoundTag compound) {
        compound = super.saveWithoutId(compound);
        compound.putBoolean("OnRoof", this.onRoof);
        return compound;
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        this.onRoof = compound.getBoolean("OnRoof");
    }
}