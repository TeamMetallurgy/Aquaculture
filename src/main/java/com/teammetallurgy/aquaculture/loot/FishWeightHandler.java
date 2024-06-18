package com.teammetallurgy.aquaculture.loot;

import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.api.AquacultureAPI;
import com.teammetallurgy.aquaculture.api.fish.FishData;
import com.teammetallurgy.aquaculture.init.AquaDataComponents;
import com.teammetallurgy.aquaculture.misc.AquaConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.StringUtils;
import net.neoforged.neoforge.event.entity.player.ItemFishedEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;

import static com.teammetallurgy.aquaculture.init.AquaItems.*;

@EventBusSubscriber(modid = Aquaculture.MOD_ID)
public class FishWeightHandler {

    @SubscribeEvent
    public static void onItemFished(ItemFishedEvent event) {
        if (!event.getDrops().isEmpty()) {
            ItemStack fish = event.getDrops().get(0);
            if (AquaConfig.BASIC_OPTIONS.randomWeight.get()) {
                if (AquacultureAPI.FISH_DATA.hasWeight(fish.getItem())) {
                    FishData fishWeight = AquacultureAPI.FISH_DATA;
                    assignRandomWeight(fish, fishWeight.getMinWeight(fish.getItem()), fishWeight.getMaxWeight(fish.getItem()));
                } else if (fish.is(ItemTags.FISHES)) { //Adds weight to any fish that does not have one specified
                    assignRandomWeight(fish, 0.1, 100);
                }
            }
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (!stack.isEmpty() && stack != null && stack.has(AquaDataComponents.FISH_WEIGHT)) { //Keep stack null check, in case of other mods is doing bad things
            Float fishWeight = stack.get(AquaDataComponents.FISH_WEIGHT);
            if (stack.has(AquaDataComponents.FISH_SIZE)) {
                MutableComponent fishWeightString = Component.translatable("aquaculture.fishWeight." + StringUtils.toLowerCase(stack.get(AquaDataComponents.FISH_SIZE)));
                event.getToolTip().add(fishWeightString.withStyle(fishWeightString.getStyle().withItalic(true).withColor(ChatFormatting.AQUA)));
            }
            if (fishWeight != null) {
                double weight = fishWeight;
                String lb = weight == 1.0D ? " lb" : " lbs";

                DecimalFormat df = new DecimalFormat("#,###.##");
                BigDecimal bd = new BigDecimal(weight);
                bd = bd.round(new MathContext(3));
                if (bd.doubleValue() > 999) {
                    MutableComponent doubleWeight = Component.translatable("aquaculture.fishWeight.weight", df.format((int) bd.doubleValue()) + lb);
                    event.getToolTip().add(doubleWeight.withStyle(doubleWeight.getStyle().withItalic(true).withColor(ChatFormatting.GRAY)));
                } else {
                    MutableComponent decimalWeight = Component.translatable("aquaculture.fishWeight.weight", bd + lb);
                    event.getToolTip().add(decimalWeight.withStyle(decimalWeight.getStyle().withItalic(true).withColor(ChatFormatting.GRAY)));
                }
            }
        }
    }

    private static void assignRandomWeight(ItemStack fish, double min, double max) {
        if (fish.isEmpty()) {
            return;
        }
        float weight = (float) (min + Math.random() * (max - min));

        if (!fish.has(AquaDataComponents.FISH_WEIGHT)) {

            fish.set(AquaDataComponents.FISH_WEIGHT, weight);
            if (weight <= max * 0.10F) {
                fish.set(AquaDataComponents.FISH_SIZE, "juvenile");
            } else if (weight > max * 0.10F && weight <= max * 0.20F) {
                fish.set(AquaDataComponents.FISH_SIZE, "small");
            } else if (weight >= max * 0.80F && weight < max * 0.90F) {
                fish.set(AquaDataComponents.FISH_SIZE, "large");
            } else if (weight >= max * 0.90F) {
                fish.set(AquaDataComponents.FISH_SIZE, "massive");
            }
        }
    }

    public static void registerFishData() {
        AquacultureAPI.FISH_DATA.add(ATLANTIC_COD.get(), 10, 211, 6);
        AquacultureAPI.FISH_DATA.add(BLACKFISH.get(), 1, 28, 2);
        AquacultureAPI.FISH_DATA.add(PACIFIC_HALIBUT.get(), 25, 550, 12);
        AquacultureAPI.FISH_DATA.add(ATLANTIC_HALIBUT.get(), 50, 710, 14);
        AquacultureAPI.FISH_DATA.add(ATLANTIC_HERRING.get(), 0.5, 2.4, 1);
        AquacultureAPI.FISH_DATA.add(PINK_SALMON.get(), 1.5, 15, 2);
        AquacultureAPI.FISH_DATA.add(POLLOCK.get(), 3, 46, 2);
        AquacultureAPI.FISH_DATA.add(RAINBOW_TROUT.get(), 2, 27, 2);
        AquacultureAPI.FISH_DATA.add(BAYAD.get(), 5, 145, 4);
        AquacultureAPI.FISH_DATA.add(BOULTI.get(), 1, 9.5, 1);
        AquacultureAPI.FISH_DATA.add(CAPITAINE.get(), 20, 440, 10);
        AquacultureAPI.FISH_DATA.add(SYNODONTIS.get(), 0.5, 2.5, 1);
        AquacultureAPI.FISH_DATA.add(SMALLMOUTH_BASS.get(), 1, 12, 2);
        AquacultureAPI.FISH_DATA.add(BLUEGILL.get(), 0.8, 4.5, 1);
        AquacultureAPI.FISH_DATA.add(BROWN_TROUT.get(), 1.5, 44, 2);
        AquacultureAPI.FISH_DATA.add(CARP.get(), 2, 40, 2);
        AquacultureAPI.FISH_DATA.add(CATFISH.get(), 10, 220, 6);
        AquacultureAPI.FISH_DATA.add(GAR.get(), 8, 100, 4);
        AquacultureAPI.FISH_DATA.add(MINNOW.get(), 0.2, 1.5, 0);
        AquacultureAPI.FISH_DATA.add(MUSKELLUNGE.get(), 5, 70, 3);
        AquacultureAPI.FISH_DATA.add(PERCH.get(), 0.5, 6, 1);
        AquacultureAPI.FISH_DATA.add(ARAPAIMA.get(), 20, 440, 10);
        AquacultureAPI.FISH_DATA.add(PIRANHA.get(), 0.5, 7.7, 1);
        AquacultureAPI.FISH_DATA.add(TAMBAQUI.get(), 7, 97, 3);
        AquacultureAPI.FISH_DATA.add(BROWN_SHROOMA.get(), 1, 5, 0);
        AquacultureAPI.FISH_DATA.add(RED_SHROOMA.get(), 1, 5, 0);
        AquacultureAPI.FISH_DATA.add(JELLYFISH.get(), 5, 400, 0);
        AquacultureAPI.FISH_DATA.add(RED_GROUPER.get(), 4, 50, 3);
        AquacultureAPI.FISH_DATA.add(TUNA.get(), 30, 1508, 10);
        AquacultureAPI.FISH_DATA.add(GOLDFISH.get(), 0.05, 5, 0);

        //Vanilla
        AquacultureAPI.FISH_DATA.add(Items.COD, 12, 211, 4);
        AquacultureAPI.FISH_DATA.add(Items.SALMON, 0.6, 15, 2);
        AquacultureAPI.FISH_DATA.add(Items.TROPICAL_FISH, 0.01, 1, 0);
        AquacultureAPI.FISH_DATA.add(Items.PUFFERFISH, 1, 25, 1);
    }
}