package com.teammetallurgy.aquaculture.inventory.container.slot;

import com.teammetallurgy.aquaculture.item.AquaFishingRodItem;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

import javax.annotation.Nonnull;

public class SlotHidable extends ResourceHandlerSlot {
    private final SlotFishingRod fishingRodSlot;

    public SlotHidable(IndexModifier<ItemResource> slotModifier, SlotFishingRod fishingRodSlot, int index, int xPosition, int yPosition) {
        super(fishingRodSlot.getResourceHandler(), slotModifier, index, xPosition, yPosition);
        this.fishingRodSlot = fishingRodSlot;
        this.onChange();
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

    public void onChange() {
        super.setChanged();
        ItemStack fishingRod = this.fishingRodSlot.getItem();
        AquaFishingRodItem.FishingRodEquipmentHandler handler = this.fishingRodSlot.rodHandler;
        if (!fishingRod.isEmpty()) {
            NonNullList<ItemStack> list = NonNullList.create();
            for (int slot = 0; slot < handler.getContainerSize(); slot++) {
                list.add(handler.getItem(slot));
            }
            System.out.println("SlotHideble - saveChanges - stack.set to: " + list + " for: " + fishingRod.getItem().getDescriptionId());
            System.out.println("SlotHideable - fishing rod has data component: " + fishingRod.has(DataComponents.CONTAINER));

            fishingRod.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(list));
        }
    }
}