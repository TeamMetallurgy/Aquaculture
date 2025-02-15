package com.teammetallurgy.aquaculture.item;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nonnull;

public class AquaFishBucket extends MobBucketItem {
    private final EntityType<?> fishType;

    public AquaFishBucket(EntityType<? extends Mob> entityType, Properties properties) {
        super(entityType, Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, properties);
        this.fishType = entityType;
    }

    @Override
    @Nonnull
    public InteractionResult use(Level world, Player player, @Nonnull InteractionHand hand) {
        ItemStack heldStack = player.getItemInHand(hand);
        if (world.isClientSide) {
            return InteractionResult.PASS;
        } else {
            BlockHitResult raytrace = getPlayerPOVHitResult(world, player, ClipContext.Fluid.SOURCE_ONLY);
            if (raytrace.getType() != HitResult.Type.BLOCK) {
                return InteractionResult.PASS;
            } else {
                BlockPos pos = raytrace.getBlockPos();
                if (!(world.getBlockState(pos).getBlock() instanceof LiquidBlock)) {
                    return super.use(world, player, hand);
                } else if (world.mayInteract(player, pos) && player.mayUseItemAt(pos, raytrace.getDirection(), heldStack)) {
                    if (world instanceof ServerLevel) {
                        Entity fishEntity = this.fishType.spawn((ServerLevel) world, heldStack, null, pos, EntitySpawnReason.BUCKET, true, false);
                        if (fishEntity != null) {
                            ((AbstractFish) fishEntity).setFromBucket(true);
                        }
                    }
                    player.awardStat(Stats.ITEM_USED.get(this));
                    return InteractionResult.SUCCESS.heldItemTransformedTo(MobBucketItem.getEmptySuccessItem(heldStack, player));
                } else {
                    return InteractionResult.FAIL;
                }
            }
        }
    }
}