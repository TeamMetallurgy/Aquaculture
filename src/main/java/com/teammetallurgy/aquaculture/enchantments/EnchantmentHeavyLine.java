package com.teammetallurgy.aquaculture.enchantments;

import net.minecraft.enchantment.Enchantment;

public class EnchantmentHeavyLine extends EnchantmentFishingPole {

    public EnchantmentHeavyLine(Enchantment.Rarity rarity) {
        super(rarity);
        setName("aquacultureHeavyLine");
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 5 + 20 * (enchantmentLevel - 1);
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return super.getMinEnchantability(enchantmentLevel) + 50;
    }
}
