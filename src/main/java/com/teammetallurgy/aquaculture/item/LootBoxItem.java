package com.teammetallurgy.aquaculture.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import javax.annotation.Nonnull;
import java.util.List;

public class LootBoxItem extends Item {
    private final ResourceKey<LootTable> lootTable;

    public LootBoxItem(ResourceKey<LootTable> lootTable, Properties properties) {
        super(properties);
        this.lootTable = lootTable;
    }

    @Override
    @Nonnull
    public InteractionResult use(Level world, Player player, @Nonnull InteractionHand hand) {
        ItemStack heldStack = player.getItemInHand(hand);
        if (world.isClientSide() || this.lootTable == null) return InteractionResult.FAIL;

        if (world instanceof ServerLevel serverLevel) {
            LootParams.Builder builder = new LootParams.Builder(serverLevel);
            List<ItemStack> loot = serverLevel.getServer().reloadableRegistries().getLootTable(this.lootTable).getRandomItems(builder.create(LootContextParamSets.EMPTY));
            if (!loot.isEmpty()) {
                ItemStack lootStack = loot.getFirst();
                player.displayClientMessage(Component.translatable("aquaculture.loot.open", lootStack.getHoverName()).withStyle(ChatFormatting.YELLOW), true);
                this.giveItem(player, lootStack);
                heldStack.shrink(1);
                return InteractionResult.SUCCESS.heldItemTransformedTo(heldStack);
            }
        }

        return super.use(world, player, hand);
    }

    /*
     * Gives the specified ItemStack to the player
     */
    private void giveItem(Player player, @Nonnull ItemStack stack) {
        if (!player.getInventory().add(stack)) {
            player.drop(stack, false);
        } else if (player instanceof ServerPlayer) {
            player.inventoryMenu.sendAllDataToRemote();
        }
    }
}