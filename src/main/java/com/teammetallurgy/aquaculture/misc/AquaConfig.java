package com.teammetallurgy.aquaculture.misc;

import com.electronwill.nightconfig.core.file.FileConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;

public class AquaConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final BasicOptions BASIC_OPTIONS = new BasicOptions(BUILDER);
    public static final NeptuniumOptions NEPTUNIUM_OPTIONS = new NeptuniumOptions(BUILDER);

    public static class BasicOptions {
        static final String BASIC_OPTIONS = "basic options";
        public ForgeConfigSpec.BooleanValue randomWeight;
        public ForgeConfigSpec.BooleanValue compostableFish;
        public ForgeConfigSpec.BooleanValue aqFishToBreedCats;
        public ForgeConfigSpec.IntValue messageInABottleAmount;
        public ForgeConfigSpec.BooleanValue debugMode;
        public ForgeConfigSpec.BooleanValue showFilletRecipesInJEI;
        public ForgeConfigSpec.IntValue fishSpawnLevelModifier;

        BasicOptions(ForgeConfigSpec.Builder builder) {
            builder.push(BASIC_OPTIONS);
            randomWeight = builder.define("Enable weight for fish? Useful for fishing competitions", false);
            compostableFish = builder.define("Should fish be added as compostables for the composter/worm farm? (Based on fish, or weight if enabled)", true);
            aqFishToBreedCats = builder.define("Should Aquaculture fish be able to be used to breed cats & ocelots?", true);
            messageInABottleAmount = builder.defineInRange("Amount of Message In A Bottle messages. Used to add additional custom messages", 29, 0, 255);
            debugMode = builder.define("Enable debug mode? (Enables additional logging)", false);
            showFilletRecipesInJEI = builder.define("Show Fillet recipes in JEI?", true);
            fishSpawnLevelModifier = builder.defineInRange("How many blocks below sea level Aquaculture fish can spawn", 13, 0, 63);
            builder.pop();
        }
    }

    public static class NeptuniumOptions {
        static final String NEPTUNIUM_OPTIONS = "neptunium options";
        public ForgeConfigSpec.BooleanValue enableNeptuniumItems;
        public ForgeConfigSpec.BooleanValue enableNeptuniumArmor;
        public ForgeConfigSpec.BooleanValue addNeptunesBountyToLoot;

        NeptuniumOptions(ForgeConfigSpec.Builder builder) {
            builder.push(NEPTUNIUM_OPTIONS);
            enableNeptuniumItems = builder.define("Enable recipes for Neptunium items?", true);
            enableNeptuniumArmor = builder.define("Enable recipes for Neptunium armor?", true);
            addNeptunesBountyToLoot = builder.comment("Should Neptune's bounty be added as fishing loot? Very rare.").define("Add Neptune's Bounty as loot?", true);
            builder.pop();
        }
    }

    public static ForgeConfigSpec spec = BUILDER.build();

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