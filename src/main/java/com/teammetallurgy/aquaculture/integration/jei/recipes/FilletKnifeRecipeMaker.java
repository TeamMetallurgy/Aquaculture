package com.teammetallurgy.aquaculture.integration.jei.recipes;

import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.api.AquacultureAPI;
import com.teammetallurgy.aquaculture.init.AquaItems;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
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
            HolderGetter<Item> itemHolderGetter = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.ITEM); //TODO Test?? Requires JEI update to test
            NonNullList<Ingredient> input = NonNullList.of(Ingredient.of(), Ingredient.of(itemHolderGetter.getOrThrow(filletKnifeTag)), Ingredient.of(fish));
            if (AquacultureAPI.FISH_DATA.hasFilletAmount(fish)) {
                ItemStack output = new ItemStack(AquaItems.FISH_FILLET.get(), AquacultureAPI.FISH_DATA.getFilletAmount(fish));
                ResourceLocation itemID = BuiltInRegistries.ITEM.getKey(fish);
                if (itemID != null) {
                    ResourceLocation id = ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, "fish_fillet." + itemID.getPath());
                    ResourceKey<Recipe<?>> key = ResourceKey.create(Registries.RECIPE, id);
                    ShapelessRecipe recipe = new ShapelessRecipe("aquaculture.fish_fillet", CraftingBookCategory.MISC, output, input);
                    recipes.add(new RecipeHolder<>(key, recipe));
                }
            }
        }
        return recipes;
    }
}