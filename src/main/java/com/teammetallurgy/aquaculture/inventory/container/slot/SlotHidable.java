package com.teammetallurgy.aquaculture.inventory.container.slot;

import com.teammetallurgy.aquaculture.init.AquaDataComponents;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class SlotHidable extends SlotItemHandler {
    private final SlotFishingRod fishingRod;

    public SlotHidable(SlotFishingRod fishingRod, int index, int xPosition, int yPosition) {
        super(fishingRod.rodHandler, index, xPosition, yPosition);
        this.fishingRod = fishingRod;
    }

    @Override
    @Nonnull
    public IItemHandler getItemHandler() {
        return this.fishingRod.rodHandler;
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        return this.fishingRod.hasItem();
    }

    @Override
    public boolean mayPickup(Player player) {
        return this.fishingRod.hasItem() && super.mayPickup(player);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isActive() {
        return this.fishingRod.hasItem();
    }

    @Override
    public void setChanged() { //Save changes to the rod
        ItemStack stack = this.fishingRod.getItem();
        if (!stack.isEmpty()) {

            NonNullList<ItemStack> list = NonNullList.create();
            for (int slot = 0; slot < getItemHandler().getSlots(); slot++) {
                list.add(getItemHandler().getStackInSlot(slot));
            }

            stack.set(AquaDataComponents.ROD_INVENTORY, ItemContainerContents.fromItems(list));
        }
    }
}