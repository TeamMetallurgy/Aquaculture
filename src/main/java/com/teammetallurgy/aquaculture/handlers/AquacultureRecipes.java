package com.teammetallurgy.aquaculture.handlers;

import com.teammetallurgy.aquaculture.items.AquacultureItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class AquacultureRecipes {

    public void addRecipes() {
        AquacultureItems.fish.addFilletRecipes();

        GameRegistry.addSmelting(AquacultureItems.fishFillet.getItemStack(), AquacultureItems.cookedFillet.getItemStack(), 0.3f);
        GameRegistry.addSmelting(AquacultureItems.whaleSteak.getItemStack(), AquacultureItems.cookedWhaleSteak.getItemStack(), 0.3f);
        GameRegistry.addSmelting(AquacultureItems.tinCan.getItemStack(), new ItemStack(Items.IRON_INGOT), 0.7f);
        GameRegistry.addSmelting(AquacultureItems.frogLegs.getItemStack(), AquacultureItems.cookedFrogLegs.getItemStack(), 0.3F);
    }
}
