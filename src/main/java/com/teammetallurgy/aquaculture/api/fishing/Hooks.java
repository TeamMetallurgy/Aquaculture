package com.teammetallurgy.aquaculture.api.fishing;

import com.teammetallurgy.aquaculture.init.AquaSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.ModList;

public class Hooks {
    public static final boolean IS_AQ2LAVA_LOADED = ModList.get().isLoaded("aq2lava");

    public static final Hook EMPTY = new Hook.HookBuilder().build();
    //Water
    public static final Hook IRON = new Hook.HookBuilder("iron").setDurabilityChance(0.20).setColor(ChatFormatting.GRAY).build();
    public static final Hook GOLD = new Hook.HookBuilder("gold").setColor(ChatFormatting.GOLD).setLuckModifier(1).build();
    public static final Hook DIAMOND = new Hook.HookBuilder("diamond").setColor(ChatFormatting.AQUA).setDurabilityChance(0.50).build();
    public static final Hook LIGHT = new Hook.HookBuilder("light").setColor(ChatFormatting.ITALIC).setWeight(new Vec3(1.5D, 1.0D, 1.5D)).build();
    public static final Hook HEAVY = new Hook.HookBuilder("heavy").setColor(ChatFormatting.BOLD).setWeight(new Vec3(0.6D, 0.15D, 0.6D)).build();
    public static final Hook DOUBLE = new Hook.HookBuilder("double").setColor(ChatFormatting.DARK_GRAY).setDoubleCatchChance(0.10).build();
    public static final Hook REDSTONE = new Hook.HookBuilder("redstone").setColor(ChatFormatting.RED).setCatchableWindow(35, 70).build();
    public static final Hook NOTE = new Hook.HookBuilder("note").setColor(ChatFormatting.DARK_RED).setCatchSound(AquaSounds.BOBBER_NOTE).build();
    public static final Hook NETHER_STAR = new Hook.HookBuilder("nether_star").setColor(ChatFormatting.BLACK).setOptionalFluid(FluidTags.LAVA, IS_AQ2LAVA_LOADED).setFluid(FluidTags.WATER).setDurabilityChance(0.50).setLuckModifier(1).build();

    public static void load() {}
}