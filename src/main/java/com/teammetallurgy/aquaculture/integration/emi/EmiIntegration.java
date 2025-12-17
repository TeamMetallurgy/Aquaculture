//package com.teammetallurgy.aquaculture.integration.emi;
//
//import com.teammetallurgy.aquaculture.api.AquacultureAPI;
//import com.teammetallurgy.aquaculture.init.AquaItems;
//import com.teammetallurgy.aquaculture.misc.AquaConfig;
//import dev.emi.emi.api.EmiEntrypoint;
//import dev.emi.emi.api.EmiPlugin;
//import dev.emi.emi.api.EmiRegistry;
//import dev.emi.emi.api.recipe.EmiCraftingRecipe;
//import dev.emi.emi.api.stack.EmiIngredient;
//import dev.emi.emi.api.stack.EmiStack;
//import net.minecraft.core.NonNullList;
//import net.minecraft.core.registries.BuiltInRegistries;
//import net.minecraft.resources.Identifier;
//import net.minecraft.tags.TagKey;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.crafting.Ingredient;
//
//@EmiEntrypoint
//public class EmiIntegration implements EmiPlugin {
//
//    @Override
//    public void register(EmiRegistry registry) {
//        if (AquaConfig.BASIC_OPTIONS.showFilletRecipesInJEI.get()) {
//            for (Item fish : AquacultureAPI.FISH_DATA.getFish()) {
//                TagKey<Item> filletKnifeTag = AquacultureAPI.Tags.KNIFE;
//                NonNullList<EmiIngredient> input = NonNullList.of(EmiIngredient.of(Ingredient.EMPTY), EmiIngredient.of(Ingredient.of(filletKnifeTag)), EmiIngredient.of(Ingredient.of(fish)));
//                if (AquacultureAPI.FISH_DATA.hasFilletAmount(fish)) {
//                    EmiStack output = EmiStack.of(new ItemStack(AquaItems.FISH_FILLET.get(), AquacultureAPI.FISH_DATA.getFilletAmount(fish)));
//                    registry.addRecipe(new EmiCraftingRecipe(input, output, Identifier.parse("/aquaculture.fish_fillet_" + BuiltInRegistries.ITEM.getKey(fish).getPath())));
//                }
//            }
//        }
//    }
//}