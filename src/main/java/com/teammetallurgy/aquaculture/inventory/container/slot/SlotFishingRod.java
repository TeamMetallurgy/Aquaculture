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

        ItemContainerContents contents = this.getItem().get(DataComponents.CONTAINER);

        if (contents == null) {
            System.out.println("SlotFishingRod, contents is EMPTY");
            this.rodHandler = AquaFishingRodItem.FishingRodEquipmentHandler.EMPTY;
        } else {
            System.out.println("SlotFishingRod, contents is: " + contents.nonEmptyItems());
            this.rodHandler = new AquaFishingRodItem.FishingRodEquipmentHandler(this.getStackCopy());
        }
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        return stack.getItem() instanceof AquaFishingRodItem;
    }

    @Override
    @Nonnull
    protected ItemStack getStackCopy() {
        return super.getStackCopy();
    }

    @Override
    protected void setStackCopy(@Nonnull ItemStack stack) {
        super.setStackCopy(stack);

        this.getStackCopy().set(DataComponents.CONTAINER, ItemContainerContents.fromItems(this.rodHandler.getItems()));
    }
}