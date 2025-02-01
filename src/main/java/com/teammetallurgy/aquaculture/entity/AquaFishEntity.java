package com.teammetallurgy.aquaculture.entity;

import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.entity.ai.goal.FollowTypeSchoolLeaderGoal;
import com.teammetallurgy.aquaculture.init.AquaSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.FollowFlockLeaderGoal;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import java.util.Objects;

public class AquaFishEntity extends AbstractSchoolingFish {
    private final FishType fishType;

    public AquaFishEntity(EntityType<? extends AbstractSchoolingFish> entityType, Level world, FishType fishType) {
        super(entityType, world);
        this.fishType = fishType;
    }

    public FishType getFishType() {
        return this.fishType;
    }

    public ResourceLocation byName() {
        return BuiltInRegistries.ENTITY_TYPE.getKey(this.getType());
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        this.goalSelector.availableGoals.forEach(prioritizedGoal -> { //Removes vanilla schooling goal
            if (prioritizedGoal.getGoal().getClass() == FollowFlockLeaderGoal.class) {
                this.goalSelector.removeGoal(prioritizedGoal.getGoal());
            }
        });
        this.goalSelector.addGoal(5, new FollowTypeSchoolLeaderGoal(this));
    }

    @Override
    public ItemStack getPickResult() {
        return this.getBucketItemStack();
    }

    @Override
    @Nonnull
    public ItemStack getBucketItemStack() {
        return new ItemStack(BuiltInRegistries.ITEM.getValue(ResourceLocation.parse(BuiltInRegistries.ENTITY_TYPE.getKey(this.getType()) + "_bucket")));
    }

    @Override
    @Nonnull
    protected SoundEvent getFlopSound() {
        if (this.getFishType() == FishType.JELLYFISH) {
            return AquaSounds.JELLYFISH_FLOP.get();
        }
        return AquaSounds.FISH_FLOP.get();
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return AquaSounds.FISH_AMBIENT.get();
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return AquaSounds.FISH_DEATH.get();
    }

    @Override
    protected SoundEvent getHurtSound(@Nonnull DamageSource damageSource) {
        return AquaSounds.FISH_HURT.get();
    }

    @Override
    public void playerTouch(@Nonnull Player player) {
        super.playerTouch(player);
        if (Objects.equals(BuiltInRegistries.ENTITY_TYPE.getKey(this.getType()), ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, "jellyfish"))) {
            if (this.isAlive() && player.level() instanceof ServerLevel level) {
                if (this.distanceToSqr(player) < 1.0D && player.hurtServer(level, this.damageSources().mobAttack(this), 0.5F)) {
                    this.playSound(AquaSounds.JELLYFISH_COLLIDE.get(), 0.5F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                }
            }
        }
    }

    @Override
    public void stopFollowing() {
        if (this.leader != null) {
            super.stopFollowing();
        }
    }

    private static boolean isSourceBlock(LevelAccessor world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return state.getBlock() instanceof LiquidBlock && world.getBlockState(pos).is(Blocks.WATER) && state.getValue(LiquidBlock.LEVEL) == 0;
    }
}
