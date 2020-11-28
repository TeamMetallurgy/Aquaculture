package com.teammetallurgy.aquaculture.api.fishing;

import com.teammetallurgy.aquaculture.Aquaculture;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Aquaculture.MOD_ID) //To get the static registering working
public class Hooks {
    public static final SoundEvent BOBBER_NOTE = (SoundEvent)(new SoundEvent(new ResourceLocation("aquaculture", "bobber_note_catch"))).setRegistryName(new ResourceLocation("aquaculture", "bobber_note_catch"));
    public static final Hook EMPTY = new Hook.HookBuilder().build();
    //Water
    public static final Hook IRON = new Hook.HookBuilder("iron").setDurabilityChance(0.20).setColor(TextFormatting.GRAY).build();
    public static final Hook GOLD = new Hook.HookBuilder("gold").setColor(TextFormatting.GOLD).setLuckModifier(1).build();
    public static final Hook DIAMOND = new Hook.HookBuilder("diamond").setColor(TextFormatting.AQUA).setDurabilityChance(0.50).build();
    public static final Hook LIGHT = new Hook.HookBuilder("light").setColor(TextFormatting.ITALIC).setWeight(new Vector3d(1.5D, 1.0D, 1.5D)).build();
    public static final Hook HEAVY = new Hook.HookBuilder("heavy").setColor(TextFormatting.BOLD).setWeight(new Vector3d(0.6D, 0.15D, 0.6D)).build();
    public static final Hook DOUBLE = new Hook.HookBuilder("double").setColor(TextFormatting.DARK_GRAY).setDoubleCatchChance(0.10).build();
    public static final Hook REDSTONE = new Hook.HookBuilder("redstone").setColor(TextFormatting.RED).setCatchableWindow(35, 70).build();
    public static final Hook NOTE = new Hook.HookBuilder("note").setColor(TextFormatting.DARK_RED).setCatchSound(BOBBER_NOTE).build();
    public static final Hook NETHER_STAR = new Hook.HookBuilder("nether_star").setColor(TextFormatting.BLACK)/*.setFluid(FluidTags.LAVA)*/.setFluid(FluidTags.WATER).setDurabilityChance(0.50).setLuckModifier(1).build();
    //Lava
    //public static final Hook OBSIDIAN = new Hook.HookBuilder("obsidian").setColor(TextFormatting.DARK_PURPLE).setFluid(FluidTags.LAVA).build();
    //public static final Hook DOUBLE_OBSIDIAN = new Hook.HookBuilder("double_obsidian").setFluid(FluidTags.LAVA).setDoubleCatchChance(0.15).build();
    //public static final Hook GLOWSTONE = new Hook.HookBuilder("glowstone").setColor(TextFormatting.YELLOW).setFluid(FluidTags.LAVA).setLuckModifier(1).build();
    //public static final Hook QUARTZ = new Hook.HookBuilder("quartz").setFluid(FluidTags.LAVA).setDurabilityChance(0.30).build();
    //public static final Hook SOUL_SAND = new Hook.HookBuilder("soul_sand").setColor(TextFormatting.DARK_GRAY).setFluid(FluidTags.LAVA).setCatchableWindow(40, 70).build();
    //public static final Hook OBSIDIAN_NOTE = new Hook.HookBuilder("obsidian_note").setColor(TextFormatting.LIGHT_PURPLE).setFluid(FluidTags.LAVA).setCatchSound(SoundEvents.BLOCK_LAVA_EXTINGUISH).build();
}
