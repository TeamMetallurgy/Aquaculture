package com.teammetallurgy.aquaculture.handlers;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class AquacultureTab extends CreativeTabs {
    private ItemStack tabItem;

    public AquacultureTab(String label) {
        super(label);
    }

    public void setItemStack(@Nonnull ItemStack itemStack) {
        tabItem = itemStack;
    }

    @Override
    @Nonnull
    public ItemStack getTabIconItem() {
        return tabItem;
    }
}