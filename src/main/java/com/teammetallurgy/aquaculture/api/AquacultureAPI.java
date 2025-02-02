package com.teammetallurgy.aquaculture.api;

import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.api.fish.FishData;
import com.teammetallurgy.aquaculture.api.fishing.Hook;
import com.teammetallurgy.aquaculture.init.AquaItems;
import com.teammetallurgy.aquaculture.init.FishRegistry;
import com.teammetallurgy.aquaculture.item.BaitItem;
import com.teammetallurgy.aquaculture.item.HookItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.registries.DeferredItem;

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

    public static BaitItem createBait(int durability, int lureSpeedModifier, Item.Properties properties) {
        return new BaitItem(durability, lureSpeedModifier, properties);
    }

    public static DeferredItem<Item> registerFishMount(@Nonnull String name) {
        return FishRegistry.registerFishMount(name);
    }

    public static DeferredItem<Item> registerHook(Hook hook) {
        DeferredItem<Item> hookItem = AquaItems.registerWithTab(p -> new HookItem(hook, p), hook.getName() + "_hook");
        Hook.HOOKS.put(hook.getName(), hookItem);
        return hookItem;
    }

    public static class Tags {
        public static final TagKey<Item> KNIFE = tag("c", "tools/knife");
        public static final TagKey<Item> FISHING_LINE = tag(Aquaculture.MOD_ID, "fishing_line");
        public static final TagKey<Item> BOBBER = tag(Aquaculture.MOD_ID, "bobber");
        public static final TagKey<Item> TACKLE_BOX = tag(Aquaculture.MOD_ID, "tackle_box");
        public static final TagKey<Item> TURTLE_EDIBLE = tag(Aquaculture.MOD_ID, "turtle_edible");
        public static final TagKey<Item> TOOLTIP = tag(Aquaculture.MOD_ID, "tooltip");
        public static final TagKey<Item> REPAIRS_NEPTUNIUM = tag(Aquaculture.MOD_ID, "repairs_neptunium");

        public static final TagKey<Biome> IS_TWILIGHT = biomeTag("c","is_twilight");
        public static final TagKey<Biome> EMPTY = biomeTag("aquaculture","empty");

        public static TagKey<Item> tag(String modID, String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(modID, name));
        }

        public static TagKey<Biome> biomeTag(String modID, String name) {
            return TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(modID, name));
        }

        public static void init() {
        }
    }
}
