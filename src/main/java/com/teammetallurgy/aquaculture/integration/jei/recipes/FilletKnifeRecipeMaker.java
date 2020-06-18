package com.teammetallurgy.aquaculture.integration.jei.recipes;

import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.api.AquacultureAPI;
import com.teammetallurgy.aquaculture.init.AquaItems;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.tags.Tag;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class FilletKnifeRecipeMaker {

    public static List<ShapelessRecipe> createFilletKnifeRecipes() {
        List<ShapelessRecipe> recipes = new ArrayList<>();

        for (Item fish : AquacultureAPI.FISH_DATA.getFish()) {
            Tag<Item> filletKnifeTag = AquacultureAPI.Tags.FILLET_KNIFE;
            Ingredient filletKnifes = Ingredient.fromItems(AquaItems.WOODEN_FILLET_KNIFE, AquaItems.STONE_FILLET_KNIFE, AquaItems.IRON_FILLET_KNIFE, AquaItems.GOLD_FILLET_KNIFE, AquaItems.DIAMOND_FILLET_KNIFE, AquaItems.NEPTINIUM_FILLET_KNIFE);
            NonNullList<Ingredient> input = NonNullList.from(Ingredient.EMPTY, filletKnifeTag.getAllElements().isEmpty() ? filletKnifes : Ingredient.fromTag(filletKnifeTag), Ingredient.fromItems(fish));
            if (AquacultureAPI.FISH_DATA.hasFilletAmount(fish)) {
                ItemStack output = new ItemStack(AquaItems.FISH_FILLET, AquacultureAPI.FISH_DATA.getFilletAmount(fish));
                if (fish.getRegistryName() != null) {
                    ResourceLocation id = new ResourceLocation(Aquaculture.MOD_ID, "fish_fillet." + fish.getRegistryName().getPath());
                    recipes.add(new ShapelessRecipe(id, VanillaRecipeCategoryUid.CRAFTING.getPath(), output, input));
                }
            }
        }
        return recipes;
    }
}