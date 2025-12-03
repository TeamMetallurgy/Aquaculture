package com.teammetallurgy.aquaculture.entity;

import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.entity.ai.goal.FollowTypeSchoolLeaderGoal;
import com.teammetallurgy.aquaculture.init.AquaSounds;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.FollowFlockLeaderGoal;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import javax.annotation.Nonnull;
import java.util.Objects;

@EventBusSubscriber(modid = Aquaculture.MOD_ID)
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

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) return;
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        Entity target = event.getTarget();
        ItemStack handStack = event.getItemStack();

        if (!(handStack.getItem() instanceof BucketItem bucketItem)) return;


        if (bucketItem.content.isSame(Fluids.WATER) && target instanceof AquaFishEntity) {
            CriteriaTriggers.FILLED_BUCKET.trigger(serverPlayer, new ItemStack(Items.COD_BUCKET)); //Triggers Tactical Fishing advancement for AQ fish. Needs to be triggered for a vanilla fish bucket to work.
        }
    }
}