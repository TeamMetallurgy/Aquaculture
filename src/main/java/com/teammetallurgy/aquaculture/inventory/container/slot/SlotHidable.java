package com.teammetallurgy.aquaculture.inventory.container.slot;

import com.teammetallurgy.aquaculture.item.AquaFishingRodItem;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

import javax.annotation.Nonnull;

public class SlotHidable extends Slot {
    private final SlotFishingRod fishingRodSlot;

    public SlotHidable(SlotFishingRod fishingRodSlot, int index, int xPosition, int yPosition) {
        super(fishingRodSlot.rodHandler, index, xPosition, yPosition);
        this.fishingRodSlot = fishingRodSlot;
        this.setChanged();
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        return this.fishingRodSlot.hasItem();
    }

    @Override
    public boolean mayPickup(@Nonnull Player player) {
        return this.fishingRodSlot.hasItem() && super.mayPickup(player);
    }

    @Override
    public boolean isActive() {
        return this.fishingRodSlot.hasItem();
    }

    @Override
    public void setChanged() {
        super.setChanged();
        ItemStack fishingRod = this.fishingRodSlot.getItem();
        if (!fishingRod.isEmpty()) {
            System.out.println("Not empty");
            NonNullList<ItemStack> list = NonNullList.create();
            for (int slot = 0; slot < this.container.getContainerSize(); slot++) {
                System.out.println("Add item to list: " + this.container.getItem(slot));
                list.add(this.container.getItem(slot));
            }
            System.out.println("SlotHideble - saveChanges - stack.set to: " + list + " for: " + fishingRod.getItem().getDescriptionId());
            System.out.println("SlotHideable - fishing rod has data component: " + fishingRod.has(DataComponents.CONTAINER));

            fishingRod.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(list));
        }
    }
}