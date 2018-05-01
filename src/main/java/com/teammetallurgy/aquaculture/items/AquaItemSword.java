package com.teammetallurgy.aquaculture.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;

import javax.annotation.Nonnull;

public class AquaItemSword extends ItemSword {

    public AquaItemSword(ToolMaterial toolMaterial) {
        super(toolMaterial);
    }

    @Override
    @Nonnull
    public Item setUnlocalizedName(@Nonnull String name) {
        super.setUnlocalizedName(name);
        return this;
    }
}