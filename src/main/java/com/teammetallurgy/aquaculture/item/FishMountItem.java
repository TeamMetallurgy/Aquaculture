package com.teammetallurgy.aquaculture.item;

import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.entity.FishMountEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HangingEntityItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class FishMountItem extends HangingEntityItem {
    private final Supplier<EntityType<FishMountEntity>> fishMount;

    public FishMountItem(Supplier<EntityType<FishMountEntity>> entityType) {
        super(null, new Item.Properties().tab(Aquaculture.GROUP));
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
            Level world = context.getLevel();
            FishMountEntity fishMountEntity = new FishMountEntity(this.fishMount.get(), world, offset, direction);

            CompoundTag tag = useStack.getTag();
            if (tag != null) {
                EntityType.updateCustomEntityTag(world, player, fishMountEntity, tag);
            }

            if (fishMountEntity.survives()) {
                if (!world.isClientSide) {
                    fishMountEntity.playPlacementSound();
                    world.addFreshEntity(fishMountEntity);
                }
                useStack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    protected boolean mayPlace(@Nonnull Player player, @Nonnull Direction direction, @Nonnull ItemStack stack, @Nonnull BlockPos pos) {
        return !player.level.isOutsideBuildHeight(pos) && player.mayUseItemAt(pos, direction, stack) && (direction != Direction.UP && direction != Direction.DOWN);
    }
}