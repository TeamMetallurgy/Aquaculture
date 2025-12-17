package com.teammetallurgy.aquaculture.api.fishing;

import com.teammetallurgy.aquaculture.Aquaculture;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredItem;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

public class Hook {
    public static final HashMap<String, DeferredItem<Item>> HOOKS = new HashMap<>();
    private final String name;
    private final String modID;
    private final Identifier texture;
    private final ChatFormatting color;
    private final int minCatchable;
    private final int maxCatchable;
    private final Vec3 weight;
    private final double durabilityChance;
    private final int luckModifier;
    private final double doubleCatchChance;
    private final Supplier<SoundEvent> catchSound;
    private final List<TagKey<Fluid>> fluids;

    private Hook(String name, String modID, ChatFormatting color, int minCatchable, int maxCatchable, Vec3 weight, double durabilityChance, int luckModifier, double doubleCatchChance, Supplier<SoundEvent> catchSound, List<TagKey<Fluid>> fluids) {
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
        this.texture = Identifier.fromNamespaceAndPath(modID, "textures/entity/rod/hook/" + name + "_hook" + ".png");
    }

    public String getName() {
        return this.name;
    }

    @Nonnull
    public Item getItem() {
        DeferredItem<Item> hookItem = HOOKS.get(this.getName());
        return hookItem != null ? hookItem.get() : Items.AIR;
    }

    public Identifier getTexture() {
        return this.texture;
    }

    public ChatFormatting getColor() {
        return this.color;
    }

    public int getMinCatchable() {
        return this.minCatchable;
    }

    public int getMaxCatchable() {
        return this.maxCatchable;
    }

    public Vec3 getWeight() {
        return this.weight;
    }

    public double getDurabilityChance() {
        return this.durabilityChance;
    }

    public int getLuckModifier() {
        return this.luckModifier;
    }

    public double getDoubleCatchChance() {
        return this.doubleCatchChance;
    }

    public SoundEvent getCatchSound() {
        return this.catchSound != null ? this.catchSound.get() : null;
    }

    public List<TagKey<Fluid>> getFluids() {
        return this.fluids;
    }

    public static class HookBuilder {
        private String name;
        private String modID = Aquaculture.MOD_ID;
        private ChatFormatting color = ChatFormatting.WHITE;
        private int minCatchable;
        private int maxCatchable;
        private Vec3 weightModifier;
        private double durabilityChance;
        private int luckModifier;
        private double doubleCatchChance;
        private Supplier<SoundEvent> catchSound;
        private final List<TagKey<Fluid>> fluids = new ArrayList<>();

        HookBuilder() {
        }

        public HookBuilder(String name) {
            this.name = name;
        }

        public HookBuilder setModID(String modID) {
            this.modID = modID;
            return this;
        }

        public HookBuilder setColor(ChatFormatting color) {
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

        public HookBuilder setWeight(Vec3 weightModifier) {
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

        public HookBuilder setCatchSound(Supplier<SoundEvent> catchSound) {
            this.catchSound = catchSound;
            return this;
        }

        public HookBuilder setFluid(TagKey<Fluid> fluid) {
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