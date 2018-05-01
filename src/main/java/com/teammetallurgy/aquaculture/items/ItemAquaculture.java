package com.teammetallurgy.aquaculture.items;

import net.minecraft.item.Item;

import javax.annotation.Nonnull;

public class ItemAquaculture extends Item {

    public ItemAquaculture() {
        super();
    }

    @Override
    @Nonnull
    public Item setUnlocalizedName(@Nonnull String name) {
        super.setUnlocalizedName(name);
        return this;
    }
}