package com.teammetallurgy.aquaculture.inventory.container.slot;

import com.teammetallurgy.aquaculture.item.AquaFishingRodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

import javax.annotation.Nonnull;

public class SlotFishingRod extends ResourceHandlerSlot {

    public SlotFishingRod(ResourceHandler<ItemResource> itemHandler, IndexModifier<ItemResource> slotModifier, int index, int xPosition, int yPosition) {
        super(itemHandler, slotModifier, index, xPosition, yPosition);
        this.setChanged();
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        ItemContainerContents
        return stack.getItem() instanceof AquaFishingRodItem;
    }

    public ItemStack getRod() {
        return this.getItem();
    }
}