package com.teammetallurgy.aquaculture.integration.jei;

import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.integration.jei.recipes.FilletKnifeRecipeMaker;
import com.teammetallurgy.aquaculture.misc.AquaConfig;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

@JeiPlugin
public class JEIIntegration implements IModPlugin {

    @Override
    @Nonnull
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, "jei_support");
    }

    @Override
    public void registerRecipes(@Nonnull IRecipeRegistration registration) {
        if (AquaConfig.BASIC_OPTIONS.showFilletRecipesInJEI.get()) {
            registration.addRecipes(RecipeTypes.CRAFTING, FilletKnifeRecipeMaker.createFilletKnifeRecipes());
        }
    }
}