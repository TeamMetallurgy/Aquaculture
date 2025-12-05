package com.teammetallurgy.aquaculture.inventory.container.slot;

import com.teammetallurgy.aquaculture.init.AquaDataComponents;
import com.teammetallurgy.aquaculture.item.AquaFishingRodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

import javax.annotation.Nonnull;

public class SlotFishingRod extends ResourceHandlerSlot { //TODO Test
    public ItemStacksResourceHandler rodHandler;

    public SlotFishingRod(ResourceHandler<ItemResource> itemHandler, IndexModifier<ItemResource> slotModifier, int index, int xPosition, int yPosition) {
        super(itemHandler, slotModifier, index, xPosition, yPosition);
        this.initCapability();
    }

    @Override
    @Nonnull
    public ResourceHandler<ItemResource> getResourceHandler() {
        return this.rodHandler;
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        return stack.getItem() instanceof AquaFishingRodItem;
    }

    public void initCapability() { //Save changes to the rod
        ItemStack stack = getItem();
        this.rodHandler = (ItemStacksResourceHandler) stack.getCapability(Capabilities.Item.ITEM, ItemAccess.forStack(stack));
        if (rodHandler == null) {
            this.rodHandler = AquaFishingRodItem.FishingRodEquipmentHandler.EMPTY;
        } else {
            ItemContainerContents rodInventory = stack.get(AquaDataComponents.ROD_INVENTORY);
            if (!stack.isEmpty() && rodInventory!= null && stack.has(AquaDataComponents.ROD_INVENTORY)) {
                for (int slot = 0; slot < rodInventory.getSlots(); slot++) {
                    ItemStack slotStack = rodInventory.getStackInSlot(slot);
                    this.rodHandler.set(slot, ItemResource.of(slotStack), slotStack.getCount()); //Reload
                }
            }
        }
    }
}