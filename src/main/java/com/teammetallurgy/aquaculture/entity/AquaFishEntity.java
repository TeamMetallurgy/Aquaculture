package com.teammetallurgy.aquaculture.entity;

import com.teammetallurgy.aquaculture.entity.ai.goal.FollowTypeSchoolLeaderGoal;
import com.teammetallurgy.aquaculture.init.AquaItems;
import com.teammetallurgy.aquaculture.misc.AquacultureSounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.FollowSchoolLeaderGoal;
import net.minecraft.entity.passive.fish.AbstractFishEntity;
import net.minecraft.entity.passive.fish.AbstractGroupFishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class AquaFishEntity extends AbstractGroupFishEntity {
    public static HashMap<EntityType<AquaFishEntity>, Item> BUCKETS = new HashMap<>();
    public static HashMap<EntityType<AquaFishEntity>, FishType> TYPES = new HashMap<>();

    public AquaFishEntity(EntityType<? extends AbstractGroupFishEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        this.goalSelector.goals.forEach(prioritizedGoal -> { //Removes vanilla schooling goal
            if (prioritizedGoal.getGoal().getClass() == FollowSchoolLeaderGoal.class) {
                this.goalSelector.removeGoal(prioritizedGoal.getGoal());
            }
        });
        this.goalSelector.addGoal(5, new FollowTypeSchoolLeaderGoal(this));
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return this.getFishBucket();
    }

    @Override
    @Nonnull
    protected ItemStack getFishBucket() {
        return new ItemStack(BUCKETS.get(this.getType()));
    }

    @Override
    @Nonnull
    protected SoundEvent getFlopSound() {
        if (AquaFishEntity.TYPES.get(this.getType()) == FishType.JELLYFISH) {
            return AquacultureSounds.jellyfishFlop;
        }
        return AquacultureSounds.fishFlop;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AquacultureSounds.fishAmbient;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AquacultureSounds.fishDeath;
    }

    @Override
    protected SoundEvent getHurtSound(@Nonnull DamageSource damageSource) {
        return AquacultureSounds.fishHurt;
    }

    @Override
    @Nonnull
    public EntitySize getSize(@Nonnull Pose pose) {
        return super.getSize(pose);
    }

    @Override
    public void onCollideWithPlayer(@Nonnull PlayerEntity player) {
        super.onCollideWithPlayer(player);
        if (Objects.equals(this.getType().getRegistryName(), AquaItems.JELLYFISH.getRegistryName())) {
            if (this.isAlive()) {
                if (this.getDistanceSq(player) < 1.0D && player.attackEntityFrom(DamageSource.causeMobDamage(this), 0.5F)) {
                    this.playSound(AquacultureSounds.fishCollide, 0.5F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
                    this.applyEnchantments(this, player);
                }
            }
        }
    }

    @Override
    public void leaveGroup() {
        if (this.groupLeader != null) {
            super.leaveGroup();
        }
    }

    public static boolean canSpawnHere(EntityType<? extends AbstractFishEntity> fish, IWorld world, SpawnReason spawnReason, BlockPos pos, Random random) {
        boolean isAllNeighborsSource = isSourceBlock(world, pos.north()) && isSourceBlock(world, pos.south()) && isSourceBlock(world, pos.west()) && isSourceBlock(world, pos.east());
        return isSourceBlock(world, pos) && isAllNeighborsSource;
    }

    private static boolean isSourceBlock(IWorld world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return state.getBlock() instanceof FlowingFluidBlock && world.getFluidState(pos).isTagged(FluidTags.WATER) && state.get(FlowingFluidBlock.LEVEL) == 0;
    }
}
