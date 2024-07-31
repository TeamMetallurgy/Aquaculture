package com.teammetallurgy.aquaculture.api;

import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.api.fish.FishData;
import com.teammetallurgy.aquaculture.init.FishRegistry;
import com.teammetallurgy.aquaculture.item.BaitItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;

public class AquacultureAPI {
    /**
     * Reference to Aquaculture's materials
     **/
    public static AquaMats MATS = new AquaMats();

    /**
     * Reference to setting weight for fish
     **/
    public static FishData FISH_DATA = new FishData();

    public static BaitItem createBait(int durability, int lureSpeedModifier) {
        return new BaitItem(durability, lureSpeedModifier);
    }

    public static RegistryObject<Item> registerFishMount(@Nonnull String name) {
        return FishRegistry.registerFishMount(name);
    }

    public static class Tags {
        public static final TagKey<Item> FILLET_KNIFE = tag("forge", "tools/knives");
        public static final TagKey<Item> FISHING_LINE = tag(Aquaculture.MOD_ID, "fishing_line");
        public static final TagKey<Item> BOBBER = tag(Aquaculture.MOD_ID, "bobber");
        public static final TagKey<Item> TACKLE_BOX = tag(Aquaculture.MOD_ID, "tackle_box");
        public static final TagKey<Item> TURTLE_EDIBLE = tag(Aquaculture.MOD_ID, "turtle_edible");
        public static final TagKey<Item> TOOLTIP = tag(Aquaculture.MOD_ID, "tooltip");

        public static final TagKey<Biome> IS_TWILIGHT = biomeTag("forge","is_twilight");
        public static final TagKey<Biome> EMPTY = biomeTag("aquaculture","is_twilight");

        public static TagKey<Item> tag(String modID, String name) {
            return ItemTags.create(new ResourceLocation(modID, name));
        }

        public static TagKey<Biome> biomeTag(String modID, String name) {
            return TagKey.create(Registries.BIOME, new ResourceLocation(modID, name));
        }

        public static void init() {
        }
    }
}
