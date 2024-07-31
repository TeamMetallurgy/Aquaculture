package com.teammetallurgy.aquaculture.integration.jei.recipes;

import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.api.AquacultureAPI;
import com.teammetallurgy.aquaculture.init.AquaItems;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

import java.util.ArrayList;
import java.util.List;

public class FilletKnifeRecipeMaker {

    public static List<RecipeHolder<CraftingRecipe>> createFilletKnifeRecipes() {
        List<RecipeHolder<CraftingRecipe>> recipes = new ArrayList<>();

        for (Item fish : AquacultureAPI.FISH_DATA.getFish()) {
            TagKey<Item> filletKnifeTag = AquacultureAPI.Tags.KNIFE;
            NonNullList<Ingredient> input = NonNullList.of(Ingredient.EMPTY, Ingredient.of(filletKnifeTag), Ingredient.of(fish));
            if (AquacultureAPI.FISH_DATA.hasFilletAmount(fish)) {
                ItemStack output = new ItemStack(AquaItems.FISH_FILLET.get(), AquacultureAPI.FISH_DATA.getFilletAmount(fish));
                ResourceLocation itemID = BuiltInRegistries.ITEM.getKey(fish);
                if (itemID != null) {
                    ResourceLocation id = ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, "fish_fillet." + itemID.getPath());
                    ShapelessRecipe recipe = new ShapelessRecipe("aquaculture.fish_fillet", CraftingBookCategory.MISC, output, input);
                    recipes.add(new RecipeHolder<>(id, recipe));
                }
            }
        }
        return recipes;
    }
}