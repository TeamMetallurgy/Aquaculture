package com.teammetallurgy.aquaculture.inventory.container;

import com.teammetallurgy.aquaculture.api.AquacultureAPI;
import com.teammetallurgy.aquaculture.api.bait.IBaitItem;
import com.teammetallurgy.aquaculture.init.AquaDataComponents;
import com.teammetallurgy.aquaculture.inventory.container.slot.SlotFishingRod;
import com.teammetallurgy.aquaculture.item.HookItem;
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
    private final SlotFishingRod rodSlot;
    private final int actualSlots;
    private ItemStacksResourceHandler tackleBox;

    public FishingRodContainerWrapper(SlotFishingRod rodSlot, int actualSlots, ItemStacksResourceHandler tackleBox) {
        this.rodSlot = rodSlot;
        this.actualSlots = actualSlots;
        this.tackleBox = tackleBox;
    }

    private ItemStack rod() {
        return this.rodSlot.getRod();
    }

    @Nullable
    private ItemContainerContents inv() {
        ItemStack rod = rod();
        return rod.isEmpty() ? null : rod.get(AquaDataComponents.ROD_INVENTORY);
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
        ItemContainerContents inv = this.inv();
        if (inv == null) return true;
        for (int i = 0; i < inv.getSlots(); i++) {
            if (!inv.getStackInSlot(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    @Nonnull
    public ItemStack getItem(int slot) {
        ItemContainerContents inv = this.inv();
        if (inv == null) return ItemStack.EMPTY;
        return slot < inv.getSlots() ? inv.getStackInSlot(slot) : ItemStack.EMPTY;
    }

    @Override
    @Nonnull
    public ItemStack removeItem(int slot, int count) {
        if (this.inv() == null) return ItemStack.EMPTY;

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
        if (this.inv() == null) return ItemStack.EMPTY;

        ItemStack existing = getItem(slot);
        setItem(slot, ItemStack.EMPTY);
        return existing;
    }

    @Override
    public void setItem(int slot, @Nonnull ItemStack newStack) {
        ItemStack rod = rod();
        if (rod.isEmpty()) return;

        List<ItemStack> items = new ArrayList<>(this.actualSlots);
        ItemContainerContents inv = inv();

        for (int i = 0; i < this.actualSlots; i++) {
            ItemStack existing = (inv != null && i < inv.getSlots()) ? inv.getStackInSlot(i) : ItemStack.EMPTY;
            items.add(i == slot ? newStack : existing);
        }

        ItemContainerContents newInv = ItemContainerContents.fromItems(items);
        rod.set(AquaDataComponents.ROD_INVENTORY, newInv);

        this.tackleBox.set(0, ItemResource.of(rod), 1);
    }

    @Override
    public void setChanged() {

    }

    @Override
    public boolean stillValid(@Nonnull Player player) {
        return this.inv() != null;
    }

    @Override
    public void clearContent() {
        this.rod().remove(AquaDataComponents.ROD_INVENTORY);
        this.tackleBox.set(0, ItemResource.EMPTY, 0);
    }
}