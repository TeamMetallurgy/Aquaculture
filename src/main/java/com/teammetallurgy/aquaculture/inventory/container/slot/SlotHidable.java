package com.teammetallurgy.aquaculture.inventory.container.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class SlotHidable extends SlotItemHandler {
    private final SlotFishingRod fishingRod;

    public SlotHidable(SlotFishingRod fishingRod, int index, int xPosition, int yPosition) {
        super(fishingRod.rodHandler, index, xPosition, yPosition);
        this.fishingRod = fishingRod;
    }

    @Override
    public IItemHandler getItemHandler() {
        return this.fishingRod.rodHandler;
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        return this.fishingRod.getHasStack();
    }

    @Override
    public boolean canTakeStack(PlayerEntity player) {
        return this.fishingRod.getHasStack() && super.canTakeStack(player);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isEnabled() {
        return this.fishingRod.getHasStack();
    }

    @Override
    public void onSlotChanged() { //Save changes to the rod
        ItemStack stack = this.fishingRod.getStack();
        if (!stack.isEmpty()) {
            CompoundNBT tag = stack.getOrCreateTag();
            tag.put("Inventory", ((ItemStackHandler) getItemHandler()).serializeNBT());
            stack.setTag(tag);
        }
    }
}