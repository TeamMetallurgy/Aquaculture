package com.teammetallurgy.aquaculture.item;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

public class FishItem extends SimpleItem {
    public static final FoodProperties SMALL_FISH_RAW = (new FoodProperties.Builder()).nutrition(1).saturationMod(0.1F).build();
    public static final FoodProperties MEDIUM_FISH_RAW = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.1F).build();
    public static final FoodProperties LARGE_FISH_RAW = (new FoodProperties.Builder()).nutrition(3).saturationMod(0.2F).build();

    public FishItem() {
        this(MEDIUM_FISH_RAW);
    }

    public FishItem(FoodProperties foodProperties) {
        super(new Item.Properties().food(foodProperties));
    }
}