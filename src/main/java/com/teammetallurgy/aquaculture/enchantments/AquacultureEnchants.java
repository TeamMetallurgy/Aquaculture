package com.teammetallurgy.aquaculture.enchantments;

import com.teammetallurgy.aquaculture.items.AquacultureItems;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.function.Predicate;

/**
 * @author Freyja
 */
public class AquacultureEnchants {

    public static EnumEnchantmentType enumFishingPole;

    public static EnchantmentFishingPole appealing;
    public static EnchantmentFishingPole magnetic;
    public static EnchantmentFishingPole longcast;
    public static EnchantmentFishingPole shortcast;
    // public static EnchantmentFishingPole fastcast;
    public static EnchantmentFishingPole doubleHook;
    public static EnchantmentFishingPole barbedHook;
    public static EnchantmentFishingPole heavyLine;

    public static void init() {

        Predicate<Item> isAquacultureRod = item ->
                item == AquacultureItems.adminFishingRod ||
                item == AquacultureItems.diamondFishingRod ||
                item == AquacultureItems.goldFishingRod ||
                item == AquacultureItems.ironFishingRod ||
                item == AquacultureItems.woodenFishingRod;
        enumFishingPole = EnumHelper.addEnchantmentType("fishingPole", isAquacultureRod::test);

        appealing = new EnchantmentAppealing(Enchantment.Rarity.RARE);
        appealing.setRegistryName("appealing");
        ForgeRegistries.ENCHANTMENTS.register(appealing);

        magnetic = new EnchantmentMagnetic(Enchantment.Rarity.RARE);
        magnetic.setRegistryName("magnetic");
        ForgeRegistries.ENCHANTMENTS.register(magnetic);

        longcast = new EnchantmentLongCast(Enchantment.Rarity.COMMON);
        longcast.setRegistryName("long_cast");
        ForgeRegistries.ENCHANTMENTS.register(longcast);

        shortcast = new EnchantmentShortCast(Enchantment.Rarity.COMMON);
        shortcast.setRegistryName("short_cast");
        ForgeRegistries.ENCHANTMENTS.register(shortcast);

        // fastcast = new EnchantmentFastcast(Config.fastcastID, 7);

        doubleHook = new EnchantmentDoubleHook(Enchantment.Rarity.VERY_RARE);
        doubleHook.setRegistryName("double_hook");
        ForgeRegistries.ENCHANTMENTS.register(doubleHook);

        barbedHook = new EnchantmentBardedHook(Enchantment.Rarity.VERY_RARE);
        barbedHook.setRegistryName("barbed_hook");
        ForgeRegistries.ENCHANTMENTS.register(barbedHook);

        heavyLine = new EnchantmentHeavyLine(Enchantment.Rarity.UNCOMMON);
        heavyLine.setRegistryName("heavy_line");
        ForgeRegistries.ENCHANTMENTS.register(heavyLine);
    }
}