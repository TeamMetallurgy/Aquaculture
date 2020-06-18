package com.teammetallurgy.aquaculture.item;

import com.teammetallurgy.aquaculture.Aquaculture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameterSets;

import javax.annotation.Nonnull;
import java.util.List;

public class LootBoxItem extends Item {
    private final ResourceLocation lootTable;

    public LootBoxItem(ResourceLocation lootTable) {
        super(new Item.Properties().group(Aquaculture.GROUP));
        this.lootTable = lootTable;
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
        ItemStack heldStack = player.getHeldItem(hand);
        if (world.isRemote || this.lootTable == null) return new ActionResult<>(ActionResultType.FAIL, heldStack);

        if (world instanceof ServerWorld) {
            ServerWorld worldServer = (ServerWorld) world;
            LootContext.Builder builder = new LootContext.Builder(worldServer);
            List<ItemStack> loot = worldServer.getServer().getLootTableManager().getLootTableFromLocation(this.lootTable).generate(builder.build(LootParameterSets.EMPTY));
            if (!loot.isEmpty()) {
                ItemStack lootStack = loot.get(0);
                player.sendStatusMessage(new TranslationTextComponent("aquaculture.loot.open", lootStack.getDisplayName()).applyTextStyle(TextFormatting.YELLOW), true);
                this.giveItem(player, lootStack);
                heldStack.shrink(1);
                return new ActionResult<>(ActionResultType.SUCCESS, heldStack);
            }
        }

        return super.onItemRightClick(world, player, hand);
    }

    /*
     * Gives the specified ItemStack to the player
     */
    private void giveItem(PlayerEntity player, @Nonnull ItemStack stack) {
        if (!player.inventory.addItemStackToInventory(stack)) {
            player.dropItem(stack, false);
        } else if (player instanceof ServerPlayerEntity) {
            ((ServerPlayerEntity) player).sendContainerToPlayer(player.container);
        }
    }
}