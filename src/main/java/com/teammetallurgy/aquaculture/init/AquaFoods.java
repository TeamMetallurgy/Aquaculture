package com.teammetallurgy.aquaculture.init;

import net.minecraft.world.food.FoodProperties;

public class AquaFoods {
    public static final FoodProperties ALGAE = new FoodProperties.Builder().nutrition(1).saturationModifier(0.0F).alwaysEdible().build();
    public static final FoodProperties FISH_RAW = new FoodProperties.Builder().nutrition(1).saturationModifier(0.2F).build();
    public static final FoodProperties FISH_FILLET = new FoodProperties.Builder().nutrition(3).saturationModifier(0.6F).build();
    public static final FoodProperties SUSHI = new FoodProperties.Builder().nutrition(4).saturationModifier(0.8F).build();
}