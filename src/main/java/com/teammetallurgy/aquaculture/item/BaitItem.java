package com.teammetallurgy.aquaculture.item;

import com.teammetallurgy.aquaculture.api.bait.IBaitItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class BaitItem extends Item implements IBaitItem {
    private final int lureSpeedModifier;

    public BaitItem(int durability, int lureSpeedModifier) {
        super(new Item.Properties().durability(durability).setNoRepair());
        this.lureSpeedModifier = lureSpeedModifier;
    }

    @Override
    public int getLureSpeedModifier() {
        return this.lureSpeedModifier;
    }

    @Override
    public int getMaxStackSize(@Nonnull ItemStack stack) {
        return 64;
    }

    @Override
    public int getBarColor(@Nonnull ItemStack stack) {
        return 16761035;
    }
}