package com.teammetallurgy.aquaculture.item;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

public class FishItem extends SimpleItem {
    public static final FoodProperties SMALL_FISH_RAW = (new FoodProperties.Builder()).nutrition(1).saturationModifier(0.1F).build();
    public static final FoodProperties MEDIUM_FISH_RAW = (new FoodProperties.Builder()).nutrition(2).saturationModifier(0.1F).build();
    public static final FoodProperties LARGE_FISH_RAW = (new FoodProperties.Builder()).nutrition(3).saturationModifier(0.2F).build();

    public FishItem(Properties properties) {
        this(properties, MEDIUM_FISH_RAW);
    }

    public FishItem(Properties properties, FoodProperties foodProperties) {
        super(properties.food(foodProperties));
    }
}