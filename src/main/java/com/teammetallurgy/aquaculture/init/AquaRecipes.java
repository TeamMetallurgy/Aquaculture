package com.teammetallurgy.aquaculture.init;

import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.item.crafting.BrewingNBT;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

import static net.minecraft.potion.PotionUtils.addPotionToItemStack;

@Mod.EventBusSubscriber(modid = Aquaculture.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AquaRecipes {

    @SubscribeEvent
    public static void registerBrewingRecipes(RegistryEvent.Register<IRecipeSerializer<?>> event) {
        addBrewingRecipeWithSubPotions(AquaItems.JELLYFISH, Potions.POISON);
        addBrewingRecipeWithSubPotions(AquaItems.LEECH, Potions.HEALING);
    }

    private static void addBrewingRecipeWithSubPotions(Item item, Potion potionType) {
        addBrewingRecipeWithSubPotions(new ItemStack(item), potionType);
    }

    private static void addBrewingRecipeWithSubPotions(@Nonnull ItemStack input, Potion potionType) {
        addRecipe(addPotionToItemStack(new ItemStack(Items.POTION), Potions.WATER), input, addPotionToItemStack(new ItemStack(Items.POTION), Potions.MUNDANE));
        addRecipe(addPotionToItemStack(new ItemStack(Items.POTION), Potions.AWKWARD), input, addPotionToItemStack(new ItemStack(Items.POTION), potionType));
        addRecipe(addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), Potions.AWKWARD), input, addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), potionType));
        addRecipe(addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), Potions.WATER), input, addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), Potions.MUNDANE));
        addRecipe(addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), Potions.WATER), input, addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), Potions.MUNDANE));
        addRecipe(addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), Potions.AWKWARD), input, addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), potionType));
    }

    private static void addRecipe(@Nonnull ItemStack input, @Nonnull ItemStack ingredient, @Nonnull ItemStack output) {
        BrewingRecipeRegistry.addRecipe(new BrewingNBT(Ingredient.fromStacks(input), Ingredient.fromStacks(ingredient), output));
    }
}