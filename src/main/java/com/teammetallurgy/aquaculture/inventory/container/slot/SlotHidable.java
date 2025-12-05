package com.teammetallurgy.aquaculture.inventory.container.slot;

import com.teammetallurgy.aquaculture.init.AquaDataComponents;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

import javax.annotation.Nonnull;

public class SlotHidable extends ResourceHandlerSlot { //TODO Test
    private final SlotFishingRod fishingRod;

    public SlotHidable(SlotFishingRod fishingRod, IndexModifier<ItemResource> slotModifier, int index, int xPosition, int yPosition) {
        super(fishingRod.rodHandler, slotModifier, index, xPosition, yPosition);
        this.fishingRod = fishingRod;
        this.saveChanges(); //Todo Test if calling this here works. Was setChanged before, but is final now
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
    public boolean isActive() {
        return this.fishingRod.hasItem();
    }

    public void saveChanges() { //Save changes to the rod
        ItemStack stack = this.fishingRod.getItem();
        if (!stack.isEmpty()) {

            NonNullList<ItemStack> list = NonNullList.create();
            ItemStacksResourceHandler stacksResourceHandler = (ItemStacksResourceHandler) this.getResourceHandler();
            for (int slot = 0; slot < stacksResourceHandler.size(); slot++) {
                list.add(stacksResourceHandler.getResource(slot).toStack());
            }

            stack.set(AquaDataComponents.ROD_INVENTORY, ItemContainerContents.fromItems(list));
        }
    }
}