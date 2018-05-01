package com.teammetallurgy.aquaculture.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLootBonus;

/**
 * @author Freyja
 */
public class EnchantmentBardedHook extends EnchantmentFishingPole {

    public EnchantmentBardedHook(Enchantment.Rarity rarity) {
        super(rarity);
        setName("aquacultureBarbedHook");
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public boolean canApplyTogether(Enchantment enchantment) {
        return !((enchantment instanceof EnchantmentDoubleHook) || (enchantment instanceof EnchantmentBardedHook) || (enchantment instanceof EnchantmentLootBonus));
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 15;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return super.getMinEnchantability(enchantmentLevel) + 50;
    }
}
