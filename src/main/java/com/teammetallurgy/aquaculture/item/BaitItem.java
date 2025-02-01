package com.teammetallurgy.aquaculture.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class BaitItem extends Item {
    private final int lureSpeedModifier;

    public BaitItem(int durability, int lureSpeedModifier, Properties properties) {
        super(properties.durability(durability).setNoCombineRepair());
        this.lureSpeedModifier = lureSpeedModifier;
    }

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