package com.teammetallurgy.aquaculture.item;

import com.teammetallurgy.aquaculture.entity.FishMountEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HangingEntityItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class FishMountItem extends HangingEntityItem {
    private final Supplier<EntityType<FishMountEntity>> fishMount;

    public FishMountItem(Supplier<EntityType<FishMountEntity>> entityType, Properties properties) {
        super(null, properties);
        this.fishMount = entityType;
    }

    @Override
    @Nonnull
    public InteractionResult useOn(UseOnContext context) {
        BlockPos pos = context.getClickedPos();
        Direction direction = context.getClickedFace();
        BlockPos offset = pos.relative(direction);
        Player player = context.getPlayer();
        ItemStack useStack = context.getItemInHand();
        if (player != null && !this.mayPlace(player, direction, useStack, offset)) {
            return InteractionResult.FAIL;
        } else {
            Level level = context.getLevel();
            FishMountEntity fishMountEntity = new FishMountEntity(this.fishMount.get(), level, offset, direction);

            EntityType.<FishMountEntity>createDefaultStackConfig(level, useStack, player).accept(fishMountEntity);

            if (fishMountEntity.survives()) {
                if (!level.isClientSide()) {
                    fishMountEntity.playPlacementSound();
                    level.gameEvent(player, GameEvent.ENTITY_PLACE, fishMountEntity.position());
                    level.addFreshEntity(fishMountEntity);
                }
                useStack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    protected boolean mayPlace(@Nonnull Player player, @Nonnull Direction direction, @Nonnull ItemStack stack, @Nonnull BlockPos pos) {
        return !player.level().isOutsideBuildHeight(pos) && player.mayUseItemAt(pos, direction, stack) && (direction != Direction.UP && direction != Direction.DOWN);
    }
}