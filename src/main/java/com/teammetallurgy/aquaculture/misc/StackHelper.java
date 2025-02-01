package com.teammetallurgy.aquaculture.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class StackHelper {

    /*
     * Gives the specified ItemStack to the player
     */
    public static void giveItem(Player player, @Nonnull ItemStack stack) {
        if (!player.getInventory().add(stack)) {
            player.drop(stack, false);
        } else if (player instanceof ServerPlayer) {
            player.inventoryMenu.sendAllDataToRemote();
        }
    }

    public static void dropInventory(Level world, BlockPos pos, IItemHandler handler) {
        for (int slot = 0; slot < handler.getSlots(); ++slot) {
            Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), handler.getStackInSlot(slot));
        }
    }

    public static InteractionHand getUsedHand(@Nonnull ItemStack stackMainHand, Class<? extends Item> clazz) {
        return clazz.isAssignableFrom(stackMainHand.getItem().getClass()) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
    }

    public static void saveToItem(ItemStack stack, HolderLookup.Provider p_323484_, BlockEntity blockEntity) {
        CompoundTag compoundtag = blockEntity.saveCustomOnly(p_323484_);
        blockEntity.removeComponentsFromTag(compoundtag);
        BlockItem.setBlockEntityData(stack, blockEntity.getType(), compoundtag);
        stack.applyComponents(blockEntity.collectComponents());
    }
}