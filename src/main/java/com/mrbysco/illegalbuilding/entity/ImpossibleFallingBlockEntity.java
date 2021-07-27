package com.mrbysco.illegalbuilding.entity;

import com.mrbysco.illegalbuilding.blocks.ImpossibleFallingBlock;
import com.mrbysco.illegalbuilding.registry.IllegalRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ConcretePowderBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.DirectionalPlaceContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.IPacket;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class ImpossibleFallingBlockEntity extends FallingBlockEntity {
    public boolean onRoof;

    public ImpossibleFallingBlockEntity(World worldIn, double x, double y, double z, BlockState fallingBlockState) {
        super(IllegalRegistry.IMPOSSIBLE_FALLING_BLOCK.get(), worldIn);
        this.blockState = fallingBlockState;
        this.blocksBuilding = true;
        this.setPos(x, y + (double)((1.0F - this.getBbHeight()) / 2.0F), z);
        this.setDeltaMovement(Vector3d.ZERO);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.setStartPos(this.blockPosition());
    }

    public ImpossibleFallingBlockEntity(EntityType<? extends FallingBlockEntity> p_i50218_1_, World world) {
        super(p_i50218_1_, world);
    }

    public ImpossibleFallingBlockEntity(FMLPlayMessages.SpawnEntity spawnEntity, World worldIn) {
        this(IllegalRegistry.IMPOSSIBLE_FALLING_BLOCK.get(), worldIn);
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick() {
        if (this.blockState.isAir()) {
            this.remove();
        } else {
            Block block = this.blockState.getBlock();
            if (this.time++ == 0) {
                BlockPos blockpos = this.blockPosition();
                if (this.level.getBlockState(blockpos).is(block)) {
                    this.level.removeBlock(blockpos, false);
                } else if (!this.level.isClientSide) {
                    this.remove();
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
                    BlockRayTraceResult blockraytraceresult = this.level.clip(new RayTraceContext(new Vector3d(this.xo, this.yo, this.zo), this.position(), RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.SOURCE_ONLY, this));
                    if (blockraytraceresult.getType() != RayTraceResult.Type.MISS && this.level.getFluidState(blockraytraceresult.getBlockPos()).is(FluidTags.WATER)) {
                        blockpos1 = blockraytraceresult.getBlockPos();
                        flag1 = true;
                    }
                }

                if (!this.onRoof && !flag1) {
                    if (!this.level.isClientSide && (this.time > 100 && (blockpos1.getY() < 1 || blockpos1.getY() > 256) || this.time > 600)) {
                        if (this.dropItem && this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                            this.spawnAtLocation(block);
                        }

                        this.remove();
                    }
                } else {
                    BlockState blockstate = this.level.getBlockState(blockpos1);
                    this.setDeltaMovement(this.getDeltaMovement().multiply(0.7D, -0.5D, 0.7D));
                    if (!blockstate.is(Blocks.MOVING_PISTON)) {
                        this.remove();
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

                                    if (this.blockData != null && this.blockState.hasTileEntity()) {
                                        TileEntity tileentity = this.level.getBlockEntity(blockpos1);
                                        if (tileentity != null) {
                                            CompoundNBT compoundnbt = tileentity.save(new CompoundNBT());

                                            for(String s : this.blockData.getAllKeys()) {
                                                INBT inbt = this.blockData.get(s);
                                                if (!"x".equals(s) && !"y".equals(s) && !"z".equals(s)) {
                                                    compoundnbt.put(s, inbt.copy());
                                                }
                                            }

                                            tileentity.load(this.blockState, compoundnbt);
                                            tileentity.setChanged();
                                        }
                                    }
                                } else if (this.dropItem && this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                                    this.spawnAtLocation(block);
                                }
                            } else if (this.dropItem && this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                                this.spawnAtLocation(block);
                            }
                        } else if (block instanceof ImpossibleFallingBlock) {
                            ((ImpossibleFallingBlock)block).onBroken(this.level, blockpos1, this);
                        }
                    }
                }
            }

            this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));
        }
    }


    @Override
    public void move(MoverType typeIn, Vector3d pos) {
        super.move(typeIn, pos);
        if (!this.noPhysics) {
            this.onRoof = this.verticalCollision && pos.y > 0.0D;

            int x = MathHelper.floor(this.getX());
            int y = MathHelper.floor(this.getY() + (double)0.2F);
            int z = MathHelper.floor(this.getZ());
            BlockPos blockpos = new BlockPos(x, y, z);
            BlockState blockstate = this.level.getBlockState(blockpos);
            if (blockstate.isAir(this.level, blockpos)) {
                BlockPos blockpos1 = blockpos.below();
                BlockState blockstate1 = this.level.getBlockState(blockpos1);
                Block block1 = blockstate1.getBlock();
                if (block1.is(BlockTags.FENCES) || block1.is(BlockTags.WALLS) || block1.is(BlockTags.FENCE_GATES)) {
                    blockstate = blockstate1;
                    blockpos = blockpos1;
                }
            }

            this.checkFallDamage(y, this.verticalCollision, blockstate, blockpos);
        }
    }

    @Override
    public CompoundNBT saveWithoutId(CompoundNBT compound) {
        compound = super.saveWithoutId(compound);
        compound.putBoolean("OnRoof", this.onRoof);
        return compound;
    }

    @Override
    public void load(CompoundNBT compound) {
        super.load(compound);
        this.onRoof = compound.getBoolean("OnRoof");
    }
}