package com.teammetallurgy.aquaculture.enchantments;

import com.teammetallurgy.aquaculture.items.ItemAdminFishingRod;
import com.teammetallurgy.aquaculture.items.ItemAquacultureFishingRod;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * @author Freyja
 */
public abstract class EnchantmentFishingPole extends Enchantment {

    public EnchantmentFishingPole(Rarity rarity) {
        super(rarity, AquacultureEnchants.enumFishingPole, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND });
    }

    @Override
    public boolean canApply(@Nonnull ItemStack stack) {
        return ((stack.getItem() instanceof ItemAquacultureFishingRod) || (stack.getItem() instanceof ItemAdminFishingRod));
    }

    @Override
    public boolean canApplyAtEnchantingTable(@Nonnull ItemStack stack) {
        return canApply(stack);
    }
}