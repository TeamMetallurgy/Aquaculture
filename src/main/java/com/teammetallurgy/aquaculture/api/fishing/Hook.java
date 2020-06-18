package com.teammetallurgy.aquaculture.api.fishing;

import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.init.AquaItems;
import com.teammetallurgy.aquaculture.item.HookItem;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Hook {
    public static final ConcurrentHashMap<String, Item> HOOKS = new ConcurrentHashMap<>();
    private final String name;
    private final String modID;
    private final Item hookItem;
    private final ResourceLocation texture;
    private final TextFormatting color;
    private final int minCatchable;
    private final int maxCatchable;
    private final Vec3d weight;
    private final double durabilityChance;
    private final int luckModifier;
    private final double doubleCatchChance;
    private final SoundEvent catchSound;
    private final List<Tag<Fluid>> fluids;

    private Hook(String name, String modID, TextFormatting color, int minCatchable, int maxCatchable, Vec3d weight, double durabilityChance, int luckModifier, double doubleCatchChance, SoundEvent catchSound, List<Tag<Fluid>> fluids) {
        this.name = name;
        this.modID = modID;
        this.color = color;
        this.minCatchable = minCatchable;
        this.maxCatchable = maxCatchable;
        this.weight = weight;
        this.durabilityChance = durabilityChance;
        this.fluids = fluids;
        this.luckModifier = luckModifier;
        this.doubleCatchChance = doubleCatchChance;
        this.catchSound = catchSound;
        this.texture = new ResourceLocation(modID, "textures/entity/rod/hook/" + name + "_hook" + ".png");
        if (name != null) {
            this.hookItem = AquaItems.register(new HookItem(this), new ResourceLocation(modID, name + "_hook"));
            HOOKS.put(name, hookItem);
        } else {
            hookItem = Items.AIR;
        }
    }

    public String getName() {
        return name;
    }

    public String getModID() {
        return modID;
    }

    public Item getItem() {
        return hookItem;
    }

    public ResourceLocation getTexture() {
        return texture;
    }

    public TextFormatting getColor() {
        return color;
    }

    public int getMinCatchable() {
        return minCatchable;
    }

    public int getMaxCatchable() {
        return maxCatchable;
    }

    public Vec3d getWeight() {
        return weight;
    }

    public double getDurabilityChance() {
        return durabilityChance;
    }

    public int getLuckModifier() {
        return luckModifier;
    }

    public double getDoubleCatchChance() {
        return doubleCatchChance;
    }

    public SoundEvent getCatchSound() {
        return catchSound;
    }

    public List<Tag<Fluid>> getFluids() {
        return fluids;
    }

    public static class HookBuilder {
        private String name;
        private String modID = Aquaculture.MOD_ID;
        private TextFormatting color = TextFormatting.WHITE;
        private int minCatchable;
        private int maxCatchable;
        private Vec3d weightModifier;
        private double durabilityChance;
        private int luckModifier;
        private double doubleCatchChance;
        private SoundEvent catchSound;
        private List<Tag<Fluid>> fluids = new ArrayList<>();

        HookBuilder() {
        }

        public HookBuilder(String name) {
            this.name = name;
        }

        public HookBuilder setModID(String modID) {
            this.modID = modID;
            return this;
        }

        public HookBuilder setColor(TextFormatting color) {
            this.color = color;
            return this;
        }

        /*
         * Sets the amount of time the fish/loot is catchable within.
         * If not set, it defaults back to the vanilla values, which is min: 20, max: 40
         */
        public HookBuilder setCatchableWindow(int min, int max) {
            this.minCatchable = min;
            this.maxCatchable = max;
            return this;
        }

        public HookBuilder setWeight(Vec3d weightModifier) {
            this.weightModifier = weightModifier;
            return this;
        }

        /*
         * Sets the percentage chance that the rod will not take damage when using this rod
         */
        public HookBuilder setDurabilityChance(double durabilityChance) {
            this.durabilityChance = durabilityChance;
            return this;
        }

        public HookBuilder setLuckModifier(int luckModifier) {
            this.luckModifier = luckModifier;
            return this;
        }

        public HookBuilder setDoubleCatchChance(double doubleCatchChance) {
            this.doubleCatchChance = doubleCatchChance;
            return this;
        }

        public HookBuilder setCatchSound(SoundEvent catchSound) {
            this.catchSound = catchSound;
            return this;
        }

        public HookBuilder setFluid(Tag<Fluid> fluid) {
            this.fluids.add(fluid);
            return this;
        }

        public Hook build() {
            if (this.fluids.isEmpty()) {
                this.fluids.add(FluidTags.WATER);
            }
            return new Hook(this.name, this.modID, this.color, this.minCatchable, this.maxCatchable, this.weightModifier, this.durabilityChance, this.luckModifier, this.doubleCatchChance, this.catchSound, this.fluids);
        }
    }
}