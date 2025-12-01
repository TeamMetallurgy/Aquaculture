package com.teammetallurgy.aquaculture.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

import com.teammetallurgy.aquaculture.api.bait.IBaitItem;

public class BaitItem extends Item implements IBaitItem {
    private final int lureSpeedModifier;
    private final int durability;

    public BaitItem(int durability, int lureSpeedModifier) {
        super(new Item.Properties().setNoRepair());
        this.lureSpeedModifier = lureSpeedModifier;
        this.durability = durability;
    }

    @Override
    public int getLureSpeedModifier() {
        return this.lureSpeedModifier;
    }

    @Override
    public boolean canBeDepleted() {
        return this.durability > 0;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return this.durability;
    }

    @Override
    public int getBarWidth(@Nonnull ItemStack stack) {
        return Math.round(13.0F - (float) stack.getDamageValue() * 13.0F / (float) this.durability);
    }

    @Override
    public int getBarColor(@Nonnull ItemStack stack) {
        return 16761035;
    }

    @Override
    public boolean isEnchantable(@Nonnull ItemStack stack) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }
}