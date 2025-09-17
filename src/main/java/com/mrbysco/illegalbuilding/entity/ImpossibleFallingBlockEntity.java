package com.mrbysco.illegalbuilding.entity;

import com.mrbysco.illegalbuilding.IllegalBuilding;
import com.mrbysco.illegalbuilding.blocks.ImpossibleColoredFallingBlock;
import com.mrbysco.illegalbuilding.registry.IllegalRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
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
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class ImpossibleFallingBlockEntity extends FallingBlockEntity {
	public boolean onRoof;

	public ImpossibleFallingBlockEntity(Level level, double x, double y, double z, BlockState fallingBlockState) {
		super(IllegalRegistry.IMPOSSIBLE_FALLING_BLOCK.get(), level);
		this.blockState = fallingBlockState;
		this.blocksBuilding = true;
		this.setPos(x, y + (double) ((1.0F - this.getBbHeight()) / 2.0F), z);
		this.setDeltaMovement(Vec3.ZERO);
		this.xo = x;
		this.yo = y;
		this.zo = z;
		this.setStartPos(this.blockPosition());
	}

	public ImpossibleFallingBlockEntity(EntityType<? extends FallingBlockEntity> entityType, Level level) {
		super(entityType, level);
	}

	public static ImpossibleFallingBlockEntity fall(Level level, BlockPos pos, BlockState blockState) {
		ImpossibleFallingBlockEntity impossibleFallingBlockEntity = new ImpossibleFallingBlockEntity(
				level,
				pos.getX() + 0.5,
				pos.getY(),
				pos.getZ() + 0.5,
				blockState.hasProperty(BlockStateProperties.WATERLOGGED) ? blockState.setValue(BlockStateProperties.WATERLOGGED, false) : blockState
		);
		level.setBlock(pos, blockState.getFluidState().createLegacyBlock(), 3);
		level.addFreshEntity(impossibleFallingBlockEntity);
		return impossibleFallingBlockEntity;
	}

	@Override
	protected void applyGravity() {
		double gravity = this.getGravity();
		if (gravity != 0.0) {
			this.setDeltaMovement(this.getDeltaMovement().add(0.0, gravity, 0.0));
		}
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void tick() {
		if (this.blockState.isAir()) {
			this.discard();
		} else {
			Block block = this.blockState.getBlock();
			this.time++;
			this.applyGravity();
			this.move(MoverType.SELF, this.getDeltaMovement());
			this.applyEffectsFromBlocks();
			this.handlePortal();
			if (this.level() instanceof ServerLevel serverlevel && (this.isAlive() || this.forceTickAfterTeleportToDuplicate)) {
				BlockPos pos = this.blockPosition();
				boolean flag = this.blockState.getBlock() instanceof ConcretePowderBlock;
				boolean flag1 = flag && this.blockState.canBeHydrated(this.level(), pos, this.level().getFluidState(pos), pos);
				double d0 = this.getDeltaMovement().lengthSqr();
				if (flag && d0 > 1.0) {
					BlockHitResult blockhitresult = this.level()
							.clip(
									new ClipContext(
											new Vec3(this.xo, this.yo, this.zo), this.position(), ClipContext.Block.COLLIDER, ClipContext.Fluid.SOURCE_ONLY, this
									)
							);
					if (blockhitresult.getType() != HitResult.Type.MISS && this.blockState.canBeHydrated(this.level(), pos, this.level().getFluidState(blockhitresult.getBlockPos()), blockhitresult.getBlockPos())) {
						pos = blockhitresult.getBlockPos();
						flag1 = true;
					}
				}

				if (!this.onRoof && !flag1) {
					if (this.time > 100 && (pos.getY() <= this.level().getMinY() || pos.getY() > this.level().getMaxY()) || this.time > 600) {
						if (this.dropItem && serverlevel.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
							this.spawnAtLocation(serverlevel, block);
						}

						this.discard();
					}
				} else {
					BlockState blockstate = this.level().getBlockState(pos);
					this.setDeltaMovement(this.getDeltaMovement().multiply(0.7D, -0.5D, 0.7D));
					if (!blockstate.is(Blocks.MOVING_PISTON)) {
						this.discard();
						if (!this.cancelDrop) {
							boolean flag2 = blockstate.canBeReplaced(new DirectionalPlaceContext(this.level(), pos, Direction.DOWN, ItemStack.EMPTY, Direction.UP));
							boolean flag3 = ImpossibleColoredFallingBlock.isFree(this.level().getBlockState(pos.above())) && (!flag || !flag1);
							if (flag3) {
								this.onRoof = false;
							}
							boolean flag4 = this.blockState.canSurvive(this.level(), pos) && !flag3;
							if (flag2 && flag4) {
								if (this.blockState.hasProperty(BlockStateProperties.WATERLOGGED) && this.level().getFluidState(pos).getType() == Fluids.WATER) {
									this.blockState = this.blockState.setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(true));
								}

								if (this.level().setBlock(pos, this.blockState, 3)) {
									if (block instanceof Fallable) {
										((Fallable) block).onLand(this.level(), pos, this.blockState, blockstate, this);
									}

									if (this.blockData != null && this.blockState.hasBlockEntity()) {
										BlockEntity blockEntity = this.level().getBlockEntity(pos);
										if (blockEntity != null) {
											CompoundTag compoundTag = blockEntity.saveWithoutMetadata(this.registryAccess());

											for (String s : this.blockData.keySet()) {
												Tag tag = this.blockData.get(s);
												if (!"x".equals(s) && !"y".equals(s) && !"z".equals(s)) {
													compoundTag.put(s, tag.copy());
												}
											}

											try {
												blockEntity.loadWithComponents(compoundTag, this.registryAccess());
											} catch (Exception var16) {
												IllegalBuilding.LOGGER.error("Failed to load block entity from impossible falling block", var16);
											}

											blockEntity.setChanged();
										}
									}
								} else if (this.dropItem && serverlevel.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
									this.discard();
									this.callOnBrokenAfterFall(block, pos);
									this.spawnAtLocation(serverlevel, block);
								}
							} else {
								this.discard();
								if (this.dropItem && serverlevel.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
									this.callOnBrokenAfterFall(block, pos);
									this.spawnAtLocation(serverlevel, block);
								}
							}
						} else {
							this.discard();
							this.callOnBrokenAfterFall(block, pos);
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
			int y = Mth.floor(this.getY() + (double) 0.2F);
			int z = Mth.floor(this.getZ());
			BlockPos blockpos = new BlockPos(x, y, z);
			BlockState blockstate = this.level().getBlockState(blockpos);
			if (blockstate.isAir()) {
				BlockPos belowPos = blockpos.below();
				BlockState belowState = this.level().getBlockState(belowPos);
				if (belowState.is(BlockTags.FENCES) || belowState.is(BlockTags.WALLS) || belowState.is(BlockTags.FENCE_GATES)) {
					blockstate = belowState;
					blockpos = belowPos;
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
		this.onRoof = compound.getBooleanOr("OnRoof", false);
	}
}