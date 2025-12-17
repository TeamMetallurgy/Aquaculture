package com.teammetallurgy.aquaculture.misc;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.TagValueOutput;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemUtil;

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

    public static void dropInventory(Level world, BlockPos pos, ItemStacksResourceHandler handler) {
        for (int slot = 0; slot < handler.size(); ++slot) {
            ItemStack slotStack = ItemUtil.getStack(handler, slot);
            Containers.dropItemStack(world, pos.getX(), pos.getY(), pos.getZ(), slotStack);
        }
    }

    public static InteractionHand getUsedHand(@Nonnull ItemStack stackMainHand, Class<? extends Item> clazz) {
        return clazz.isAssignableFrom(stackMainHand.getItem().getClass()) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
    }

    public static void saveToItem(ItemStack stack, HolderLookup.Provider provider, BlockEntity blockEntity) {
        try (ProblemReporter.ScopedCollector scopedCollector = new ProblemReporter.ScopedCollector(blockEntity.problemPath(), LogUtils.getLogger())) {
            TagValueOutput tagValueOutput = TagValueOutput.createWithContext(scopedCollector, provider);
            blockEntity.saveCustomOnly(tagValueOutput);
            blockEntity.removeComponentsFromTag(tagValueOutput);
            BlockItem.setBlockEntityData(stack, blockEntity.getType(), tagValueOutput);
            stack.applyComponents(blockEntity.collectComponents());
        }
    }
}