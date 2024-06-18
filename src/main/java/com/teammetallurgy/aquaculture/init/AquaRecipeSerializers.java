package com.teammetallurgy.aquaculture.init;

import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.item.crafting.FishFilletRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AquaRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> IRECIPE_SERIALIZERS_DEFERRED = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Aquaculture.MOD_ID);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<FishFilletRecipe>> FISH_FILLET_SERIALIZER = registerRecipeSerializer("crafting_special_fish_fillet", new SimpleCraftingRecipeSerializer<>(FishFilletRecipe::new));

    public static <T extends Recipe<?>> DeferredHolder<RecipeSerializer<?>, RecipeSerializer<T>> registerRecipeSerializer(String name, RecipeSerializer<T> serializer) {
        return IRECIPE_SERIALIZERS_DEFERRED.register(name, () -> serializer);
    }
}
