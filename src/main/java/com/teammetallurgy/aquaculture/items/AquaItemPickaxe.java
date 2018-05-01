package com.teammetallurgy.aquaculture.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;

import javax.annotation.Nonnull;

public class AquaItemPickaxe extends ItemPickaxe {

    public AquaItemPickaxe(ToolMaterial toolMaterial) {
        super(toolMaterial);
    }

    @Override
    @Nonnull
    public Item setUnlocalizedName(@Nonnull String name) {
        super.setUnlocalizedName(name);
        return this;
    }
}