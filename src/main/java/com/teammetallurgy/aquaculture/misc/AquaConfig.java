package com.teammetallurgy.aquaculture.misc;

import com.electronwill.nightconfig.core.file.FileConfig;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.io.File;

public class AquaConfig {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final BasicOptions BASIC_OPTIONS = new BasicOptions(BUILDER);
    public static final NeptuniumOptions NEPTUNIUM_OPTIONS = new NeptuniumOptions(BUILDER);

    public static class BasicOptions {
        static final String BASIC_OPTIONS = "basic_options";
        public ModConfigSpec.BooleanValue randomWeight;
        public ModConfigSpec.IntValue messageInABottleAmount;
        public ModConfigSpec.BooleanValue debugMode;
        public ModConfigSpec.BooleanValue showFilletRecipesInJEI;
        public ModConfigSpec.IntValue fishSpawnLevelModifier;

        BasicOptions(ModConfigSpec.Builder builder) {
            builder.push(BASIC_OPTIONS);
            randomWeight = builder.define("Enable weight for fish? Useful for fishing competitions", false);
            messageInABottleAmount = builder.defineInRange("Amount of Message In A Bottle messages - Used to add additional custom messages", 29, 0, 255);
            debugMode = builder.define("Enable debug mode? (Enables additional logging)", false);
            showFilletRecipesInJEI = builder.define("Show Fillet recipes in JEI?", true);
            fishSpawnLevelModifier = builder.defineInRange("How many blocks below sea level Aquaculture fish can spawn", 13, 0, 63);
            builder.pop();
        }
    }

    public static class NeptuniumOptions {
        static final String NEPTUNIUM_OPTIONS = "neptunium_options";
        public ModConfigSpec.BooleanValue enableNeptuniumItems;
        public ModConfigSpec.BooleanValue enableNeptuniumArmor;
        public ModConfigSpec.BooleanValue addNeptunesBountyToLoot;
        public ModConfigSpec.IntValue neptuniumLootRarity;


        NeptuniumOptions(ModConfigSpec.Builder builder) {
            builder.push(NEPTUNIUM_OPTIONS);
            enableNeptuniumItems = builder.define("Enable recipes for Neptunium items?", true);
            enableNeptuniumArmor = builder.define("Enable recipes for Neptunium armor?", true);
            addNeptunesBountyToLoot = builder.comment("Should Neptune's bounty be added as fishing loot? Very rare.").define("Add Neptune's Bounty as loot?", true);
            neptuniumLootRarity = builder.defineInRange("Neptunium loot table weight - For reference; Fish loot table weight: 85, Junk loot table weight: 10", 1, 1, 100);
            builder.pop();
        }
    }

    public static ModConfigSpec spec = BUILDER.build();

    public static class Helper {
        private static final FileConfig CONFIG_FILE = FileConfig.of(new File(FMLPaths.CONFIGDIR.get().toFile(), "aquaculture-common.toml"));

        public static <T> T get(String category, String subCategory, String value) {
            return get(category + "." + subCategory, value);
        }

        public static <T> T get(String category, String value) {
            T configEntry = CONFIG_FILE.get(category + "." + value);
            if (configEntry == null) {
                CONFIG_FILE.load();
                return CONFIG_FILE.get(category + "." + value);
            } else {
                return configEntry;
            }
        }

        public static String getSubConfig(String category, String subCategory) {
            return category + "." + subCategory;
        }
    }
}