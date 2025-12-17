package com.teammetallurgy.aquaculture.inventory.container.slot;

import com.teammetallurgy.aquaculture.inventory.container.FishingRodContainerWrapper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class SlotHidable extends Slot {
    private final SlotFishingRod fishingRodSlot;

    public SlotHidable(SlotFishingRod fishingRodSlot, FishingRodContainerWrapper wrapper, int index, int xPosition, int yPosition) {
        super(wrapper, index, xPosition, yPosition);
        this.fishingRodSlot = fishingRodSlot;
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
    public int getMaxStackSize() {
        return 1;
    }
}