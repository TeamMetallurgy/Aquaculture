package com.teammetallurgy.aquaculture.inventory.container;

import com.teammetallurgy.aquaculture.api.AquacultureAPI;
import com.teammetallurgy.aquaculture.api.bait.IBaitItem;
import com.teammetallurgy.aquaculture.item.HookItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/*
 * Largely based on ItemContainerWrapper from ToolBelt. Thanks to @Gigaherz for letting me copy it!
 */
public class FishingRodContainerWrapper implements Container {
    private final ItemStack stack;
    private final int actualSlots;
    private ItemContainerContents inv;
    private ItemStacksResourceHandler tackleBox;

    public FishingRodContainerWrapper(@Nullable ItemContainerContents container, int actualSlots, ItemStack stack, ItemStacksResourceHandler tackleBox) {
        this.stack = stack;
        this.inv = container;
        this.actualSlots = actualSlots;
        this.tackleBox = tackleBox;

        System.out.println("Wrapper stack: " + stack);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean canPlaceItem(int slot, @Nonnull ItemStack stack) {
        return switch (slot) {
            case 0 -> stack.getItem() instanceof HookItem;
            case 1 -> stack.getItem() instanceof IBaitItem;
            case 2 -> stack.is(AquacultureAPI.Tags.FISHING_LINE);
            case 3 -> stack.is(AquacultureAPI.Tags.BOBBER);
            default -> false;
        };
    }

    @Override
    public int getContainerSize() {
        return this.actualSlots;
    }

    @Override
    public boolean isEmpty() {
        if (this.inv == null) return true;
        for (int i = 0; i < this.inv.getSlots(); i++) {
            if (!this.inv.getStackInSlot(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    @Nonnull
    public ItemStack getItem(int slot) {
        if (this.inv == null) return ItemStack.EMPTY;
        return slot < this.inv.getSlots() ? this.inv.getStackInSlot(slot) : ItemStack.EMPTY;
    }

    @Override
    @Nonnull
    public ItemStack removeItem(int slot, int count) {
        if (this.inv == null) return ItemStack.EMPTY;

        ItemStack existing = getItem(slot);

        if (count >= existing.getCount()) {
            setItem(slot, ItemStack.EMPTY);
        } else {
            var remaining = existing.copy();
            existing = existing.split(count);
            setItem(slot, remaining);
        }
        return existing;
    }

    @Override
    @Nonnull
    public ItemStack removeItemNoUpdate(int slot) {
        if (this.inv == null) return ItemStack.EMPTY;

        ItemStack existing = getItem(slot);
        setItem(slot, ItemStack.EMPTY);
        return existing;
    }

    @Override
    public void setItem(int slot, @Nonnull ItemStack newStack) {
        List<ItemStack> newItems = null;
        boolean didWork = false;
        for (int i = 0; i < this.actualSlots; i++) {
            var stack = getItem(i);
            if (i == slot) {
                if (newItems == null) {
                    newItems = new ArrayList<>(this.actualSlots);
                }
                newItems.add(newStack);
                didWork = true;
            } else {
                if (newItems == null) {
                    newItems = new ArrayList<>(this.actualSlots);
                }
                newItems.add(stack);
            }
        }

        if (didWork) {
            this.inv = ItemContainerContents.fromItems(newItems);
            this.stack.set(DataComponents.CONTAINER, this.inv);
            if (!this.stack.isEmpty()) {
                this.tackleBox.set(0, ItemResource.of(this.stack), 1);
            }
        }
    }

    @Override
    public void setChanged() {

    }

    @Override
    public boolean stillValid(@Nonnull Player player) {
        return this.inv != null;
    }

    @Override
    public void clearContent() {
        this.stack.remove(DataComponents.CONTAINER);
        this.tackleBox.set(0, ItemResource.EMPTY, 0);
    }
}
