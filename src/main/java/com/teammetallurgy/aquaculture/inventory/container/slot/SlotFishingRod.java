package com.teammetallurgy.aquaculture.inventory.container.slot;

import com.teammetallurgy.aquaculture.item.AquaFishingRodItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

import javax.annotation.Nonnull;

public class SlotFishingRod extends ResourceHandlerSlot {
    public AquaFishingRodItem.FishingRodEquipmentHandler rodHandler;

    public SlotFishingRod(ResourceHandler<ItemResource> itemHandler, IndexModifier<ItemResource> slotModifier, int index, int xPosition, int yPosition) {
        super(itemHandler, slotModifier, index, xPosition, yPosition);
        this.onChange();
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        return stack.getItem() instanceof AquaFishingRodItem;
    }

    public void onChange() {
        super.setChanged();

        ItemStack stack = this.getItem();
        System.out.println("FISHING ROD SLOT HAS DATA COMPONENT?: " + stack.has(DataComponents.CONTAINER));


        if (this.rodHandler == null) {
            this.rodHandler = new AquaFishingRodItem.FishingRodEquipmentHandler(stack);
            System.out.println("RodHandler null");
        } else {
            ItemContainerContents rodInventory = stack.get(DataComponents.CONTAINER);
            if (!stack.isEmpty() && rodInventory!= null && stack.has(DataComponents.CONTAINER)) {
                for (int slot = 0; slot < rodInventory.getSlots(); slot++) {
                    ItemStack slotStack = rodInventory.getStackInSlot(slot);
                    System.out.println("RODHANDLER SET");
                    this.rodHandler.setItem(slot, slotStack);
                }
            }
        }
    }
}