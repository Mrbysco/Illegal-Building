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
        this.fallTile = fallingBlockState;
        this.preventEntitySpawning = true;
        this.setPosition(x, y + (double)((1.0F - this.getHeight()) / 2.0F), z);
        this.setMotion(Vector3d.ZERO);
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
        this.setOrigin(this.getPosition());
    }

    public ImpossibleFallingBlockEntity(EntityType<? extends FallingBlockEntity> p_i50218_1_, World world) {
        super(p_i50218_1_, world);
    }

    public ImpossibleFallingBlockEntity(FMLPlayMessages.SpawnEntity spawnEntity, World worldIn) {
        this(IllegalRegistry.IMPOSSIBLE_FALLING_BLOCK.get(), worldIn);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick() {
        if (this.fallTile.isAir()) {
            this.remove();
        } else {
            Block block = this.fallTile.getBlock();
            if (this.fallTime++ == 0) {
                BlockPos blockpos = this.getPosition();
                if (this.world.getBlockState(blockpos).isIn(block)) {
                    this.world.removeBlock(blockpos, false);
                } else if (!this.world.isRemote) {
                    this.remove();
                    return;
                }
            }

            if (!this.hasNoGravity()) {
                this.setMotion(this.getMotion().add(0.0D, 0.04D, 0.0D));
            }

            this.move(MoverType.SELF, this.getMotion());
            if (!this.world.isRemote) {
                BlockPos blockpos1 = this.getPosition();
                boolean flag = this.fallTile.getBlock() instanceof ConcretePowderBlock;
                boolean flag1 = flag && this.world.getFluidState(blockpos1).isTagged(FluidTags.WATER);
                double d0 = this.getMotion().lengthSquared();
                if (flag && d0 > 1.0D) {
                    BlockRayTraceResult blockraytraceresult = this.world.rayTraceBlocks(new RayTraceContext(new Vector3d(this.prevPosX, this.prevPosY, this.prevPosZ), this.getPositionVec(), RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.SOURCE_ONLY, this));
                    if (blockraytraceresult.getType() != RayTraceResult.Type.MISS && this.world.getFluidState(blockraytraceresult.getPos()).isTagged(FluidTags.WATER)) {
                        blockpos1 = blockraytraceresult.getPos();
                        flag1 = true;
                    }
                }

                if (!this.onRoof && !flag1) {
                    if (!this.world.isRemote && (this.fallTime > 100 && (blockpos1.getY() < 1 || blockpos1.getY() > 256) || this.fallTime > 600)) {
                        if (this.shouldDropItem && this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
                            this.entityDropItem(block);
                        }

                        this.remove();
                    }
                } else {
                    BlockState blockstate = this.world.getBlockState(blockpos1);
                    this.setMotion(this.getMotion().mul(0.7D, -0.5D, 0.7D));
                    if (!blockstate.isIn(Blocks.MOVING_PISTON)) {
                        this.remove();
                        if (!this.dontSetBlock) {
                            boolean flag2 = blockstate.isReplaceable(new DirectionalPlaceContext(this.world, blockpos1, Direction.DOWN, ItemStack.EMPTY, Direction.UP));
                            boolean flag3 = ImpossibleFallingBlock.canFallThrough(this.world.getBlockState(blockpos1.up())) && (!flag || !flag1);
                            if(flag3) {
                                this.onRoof = false;
                            }
                            boolean flag4 = this.fallTile.isValidPosition(this.world, blockpos1) && !flag3;
                            if (flag2 && flag4) {
                                if (this.fallTile.hasProperty(BlockStateProperties.WATERLOGGED) && this.world.getFluidState(blockpos1).getFluid() == Fluids.WATER) {
                                    this.fallTile = this.fallTile.with(BlockStateProperties.WATERLOGGED, Boolean.valueOf(true));
                                }

                                if (this.world.setBlockState(blockpos1, this.fallTile, 3)) {
                                    if (block instanceof ImpossibleFallingBlock) {
                                        ((ImpossibleFallingBlock)block).onEndFalling(this.world, blockpos1, this.fallTile, blockstate, this);
                                    }

                                    if (this.tileEntityData != null && this.fallTile.hasTileEntity()) {
                                        TileEntity tileentity = this.world.getTileEntity(blockpos1);
                                        if (tileentity != null) {
                                            CompoundNBT compoundnbt = tileentity.write(new CompoundNBT());

                                            for(String s : this.tileEntityData.keySet()) {
                                                INBT inbt = this.tileEntityData.get(s);
                                                if (!"x".equals(s) && !"y".equals(s) && !"z".equals(s)) {
                                                    compoundnbt.put(s, inbt.copy());
                                                }
                                            }

                                            tileentity.read(this.fallTile, compoundnbt);
                                            tileentity.markDirty();
                                        }
                                    }
                                } else if (this.shouldDropItem && this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
                                    this.entityDropItem(block);
                                }
                            } else if (this.shouldDropItem && this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
                                this.entityDropItem(block);
                            }
                        } else if (block instanceof ImpossibleFallingBlock) {
                            ((ImpossibleFallingBlock)block).onBroken(this.world, blockpos1, this);
                        }
                    }
                }
            }

            this.setMotion(this.getMotion().scale(0.98D));
        }
    }


    @Override
    public void move(MoverType typeIn, Vector3d pos) {
        super.move(typeIn, pos);
        if (!this.noClip) {
            this.onRoof = this.collidedVertically && pos.y > 0.0D;

            int x = MathHelper.floor(this.getPosX());
            int y = MathHelper.floor(this.getPosY() + (double)0.2F);
            int z = MathHelper.floor(this.getPosZ());
            BlockPos blockpos = new BlockPos(x, y, z);
            BlockState blockstate = this.world.getBlockState(blockpos);
            if (blockstate.isAir(this.world, blockpos)) {
                BlockPos blockpos1 = blockpos.down();
                BlockState blockstate1 = this.world.getBlockState(blockpos1);
                Block block1 = blockstate1.getBlock();
                if (block1.isIn(BlockTags.FENCES) || block1.isIn(BlockTags.WALLS) || block1.isIn(BlockTags.FENCE_GATES)) {
                    blockstate = blockstate1;
                    blockpos = blockpos1;
                }
            }

            this.updateFallState(y, this.collidedVertically, blockstate, blockpos);
        }
    }

    @Override
    public CompoundNBT writeWithoutTypeId(CompoundNBT compound) {
        compound = super.writeWithoutTypeId(compound);
        compound.putBoolean("OnRoof", this.onRoof);
        return compound;
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        this.onRoof = compound.getBoolean("OnRoof");
    }
}