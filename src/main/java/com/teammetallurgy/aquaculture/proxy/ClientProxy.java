package com.teammetallurgy.aquaculture.proxy;

import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.client.CustomFishingHookRenderFactory;
import com.teammetallurgy.aquaculture.handlers.AquacultureEntitySpawnHandler;
import com.teammetallurgy.aquaculture.handlers.EntityCustomFishHook;
import com.teammetallurgy.aquaculture.items.AquacultureItems;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerParticles() {
    }

    @Override
    public void registerModelRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(EntityCustomFishHook.class, new CustomFishingHookRenderFactory());
    }

    @Override
    public void registerItemModels() {
        // Fishing Rods
        ModelLoader.setCustomModelResourceLocation(AquacultureItems.woodenFishingRod, 0, new ModelResourceLocation(Aquaculture.MOD_ID + ":fishing_rod", "inventory"));
        ModelLoader.setCustomModelResourceLocation(AquacultureItems.woodenFishingRod, 1, new ModelResourceLocation(Aquaculture.MOD_ID + ":fishing_rod_cast", "inventory"));
        ModelLoader.setCustomModelResourceLocation(AquacultureItems.adminFishingRod, 0, new ModelResourceLocation(Aquaculture.MOD_ID + ":admin_fishing_rod", "inventory"));
        ModelLoader.setCustomModelResourceLocation(AquacultureItems.adminFishingRod, 1, new ModelResourceLocation(Aquaculture.MOD_ID + ":admin_fishing_rod_cast", "inventory"));
        ModelLoader.setCustomModelResourceLocation(AquacultureItems.ironFishingRod, 0, new ModelResourceLocation(Aquaculture.MOD_ID + ":iron_fishing_rod", "inventory"));
        ModelLoader.setCustomModelResourceLocation(AquacultureItems.ironFishingRod, 1, new ModelResourceLocation(Aquaculture.MOD_ID + ":iron_fishing_rod_cast", "inventory"));
        ModelLoader.setCustomModelResourceLocation(AquacultureItems.goldFishingRod, 0, new ModelResourceLocation(Aquaculture.MOD_ID + ":gold_fishing_rod", "inventory"));
        ModelLoader.setCustomModelResourceLocation(AquacultureItems.goldFishingRod, 1, new ModelResourceLocation(Aquaculture.MOD_ID + ":gold_fishing_rod_cast", "inventory"));
        ModelLoader.setCustomModelResourceLocation(AquacultureItems.diamondFishingRod, 0, new ModelResourceLocation(Aquaculture.MOD_ID + ":diamond_fishing_rod", "inventory"));
        ModelLoader.setCustomModelResourceLocation(AquacultureItems.diamondFishingRod, 1, new ModelResourceLocation(Aquaculture.MOD_ID + ":diamond_fishing_rod_cast", "inventory"));

        // Food
        String[] food = { "seaweed", "algae", "whale_steak_raw", "fish_fillet_raw", "fish_fillet_cooked", "whale_steak_cooked", "whale_burger", "frog_legs_raw", "frog_legs_cooked",
                "turtle_soup", "sushi" };

        for (int i = 0; i < food.length; i++) {
            ModelLoader.setCustomModelResourceLocation(AquacultureItems.metaFoodItem, i, new ModelResourceLocation(Aquaculture.MOD_ID + ":loot/" + food[i], "inventory"));
        }

        // Loot
        String[] loot = { "driftwood", "neptunium_ingot", "tin_can", "message_in_a_bottle", "box", "lockbox", "treasure_chest", "neptunes_bounty" };

        for (int i = 0; i < loot.length; i++) {
            ModelLoader.setCustomModelResourceLocation(AquacultureItems.metaLootItem, i, new ModelResourceLocation(Aquaculture.MOD_ID + ":loot/" + loot[i], "inventory"));
        }

        // Tools
        ModelLoader.setCustomModelResourceLocation(AquacultureItems.neptuniumPickaxe, 0, new ModelResourceLocation(Aquaculture.MOD_ID + ":neptunium_pickaxe", "inventory"));
        ModelLoader.setCustomModelResourceLocation(AquacultureItems.neptuniumShovel, 0, new ModelResourceLocation(Aquaculture.MOD_ID + ":neptunium_shovel", "inventory"));
        ModelLoader.setCustomModelResourceLocation(AquacultureItems.neptuniumAxe, 0, new ModelResourceLocation(Aquaculture.MOD_ID + ":neptunium_axe", "inventory"));
        ModelLoader.setCustomModelResourceLocation(AquacultureItems.neptuniumHoe, 0, new ModelResourceLocation(Aquaculture.MOD_ID + ":neptunium_hoe", "inventory"));
        ModelLoader.setCustomModelResourceLocation(AquacultureItems.neptuniumSword, 0, new ModelResourceLocation(Aquaculture.MOD_ID + ":neptunium_sword", "inventory"));

        // Armour
        ModelLoader.setCustomModelResourceLocation(AquacultureItems.neptuniumHelmet, 0, new ModelResourceLocation(Aquaculture.MOD_ID + ":neptunium_helmet", "inventory"));
        ModelLoader.setCustomModelResourceLocation(AquacultureItems.neptuniumPlate, 0, new ModelResourceLocation(Aquaculture.MOD_ID + ":neptunium_chestplate", "inventory"));
        ModelLoader.setCustomModelResourceLocation(AquacultureItems.neptuniumLegs, 0, new ModelResourceLocation(Aquaculture.MOD_ID + ":neptunium_leggings", "inventory"));
        ModelLoader.setCustomModelResourceLocation(AquacultureItems.neptuniumBoots, 0, new ModelResourceLocation(Aquaculture.MOD_ID + ":neptunium_boots", "inventory"));

        // Fish
        String[] fish = { "bluegill", "perch", "gar", "bass", "muskellunge", "trout_brown", "catfish", "carp", "blowfish", "grouper_red", "salmon", "tuna", "swordfish",
                "shark", "whale", "squid", "jellyfish", "frog", "turtle", "leech", "pirahna", "electric_eel", "tambaqui", "arapaima", "cod", "pollock", "herring",
                "halibut", "salmon_pink", "trout_rainbow", "blackfish", "capitaine", "boulti", "bagrid", "syndontis", "shrooma_red", "shrooma_brown", "goldfish",
                "fish_bones" };

        for (int i = 0; i < fish.length; i++) {
            ModelLoader.setCustomModelResourceLocation(AquacultureItems.fish, i, new ModelResourceLocation(Aquaculture.MOD_ID + ":fish/" + fish[i], "inventory"));
        }
    }

    @Override
    public void registerEntities() {
        super.registerEntities();

        AquacultureEntitySpawnHandler.init();
    }
}
