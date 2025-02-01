package com.teammetallurgy.aquaculture.item;

import com.teammetallurgy.aquaculture.init.AquaSounds;
import com.teammetallurgy.aquaculture.misc.AquaConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public class ItemMessageInABottle extends Item {

    public ItemMessageInABottle(Properties properties) {
        super(properties);
    }

    @Override
    @Nonnull
    public InteractionResult use(Level world, Player player, @Nonnull InteractionHand hand) {
        ItemStack heldStack = player.getItemInHand(hand);
        world.playSound(null, player.getX(), player.getY(), player.getZ(), AquaSounds.BOTTLE_OPEN.get(), SoundSource.PLAYERS, 0.5F, 0.4F / (world.random.nextFloat() * 0.4F + 0.8F));

        if (player instanceof ServerPlayer) {
            player.displayClientMessage(Component.translatable("aquaculture.message" + world.random.nextInt(AquaConfig.BASIC_OPTIONS.messageInABottleAmount.get() + 1)), true);
        }
        heldStack.shrink(1);

        return super.use(world, player, hand);
    }
}