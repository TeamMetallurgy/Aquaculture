package com.teammetallurgy.aquaculture.misc;

import com.electronwill.nightconfig.core.file.FileConfig;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.util.List;

public class AquaConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final BasicOptions BASIC_OPTIONS = new BasicOptions(BUILDER);
    public static final NeptuniumOptions NEPTUNIUM_OPTIONS = new NeptuniumOptions(BUILDER);

    public static class BasicOptions {
        static final String BASIC_OPTIONS = "basic options";
        public ForgeConfigSpec.BooleanValue enableFishSpawning;
        public ForgeConfigSpec.BooleanValue randomWeight;
        public ForgeConfigSpec.BooleanValue compostableFish;
        public ForgeConfigSpec.BooleanValue debugMode;

        BasicOptions(ForgeConfigSpec.Builder builder) {
            builder.push(BASIC_OPTIONS);
            enableFishSpawning = builder.comment("Enable fish mob spawning? Weight & biomes can be modified in the Aquaculture fish loot table").define("Enable fish spawning?", true);
            randomWeight = builder.define("Enable weight for fish? Useful for fishing competitions", false);
            compostableFish = builder.define("Should fish be added as compostables for the composter/worm farm? (Based on fish, or weight if enabled)", true);
            debugMode = builder.define("Enable debug mode? (Enables additional logging)", false);
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

    public static class Spawn {
        public static final String SPAWN_OPTIONS = "spawn options";
        public final ForgeConfigSpec.IntValue min;
        public final ForgeConfigSpec.IntValue max;
        public final ForgeConfigSpec.IntValue weight;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> include;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> exclude;

        public Spawn(ForgeConfigSpec.Builder builder, String name, int min, int max, int weight, List<? extends String> include, List<? extends String> exclude) {
            builder.push(SPAWN_OPTIONS);
            builder.push(name);
            this.min = builder.defineInRange("min", min, 0, 64);
            this.max = builder.defineInRange("max", max, 0, 64);
            this.weight = builder.defineInRange("weight", weight, 0, 100);
            this.include = builder.defineList("include", include, o -> BiomeDictionary.Type.getAll().contains(BiomeDictionaryHelper.getType(String.valueOf(o))));
            this.exclude = builder.defineList("exclude", exclude, o -> BiomeDictionary.Type.getAll().contains(BiomeDictionaryHelper.getType(String.valueOf(o))));
            builder.pop(2);
        }
    }

    public static ForgeConfigSpec spec = BUILDER.build();

    public static class Helper {
        private static final FileConfig CONFIG_FILE = FileConfig.of(new File(FMLPaths.CONFIGDIR.get().toFile(), "aquaculture-common.toml"));

        public static <T> T get(String category, String subCategory, String value) {
            return get(category + "." + subCategory, value);
        }

        public static <T> T get(String category, String value) {
            CONFIG_FILE.load();
            return CONFIG_FILE.get(category + "." + value);
        }

        public static String getSubConfig(String category, String subCategory) {
            return category + "." + subCategory;
        }
    }
}