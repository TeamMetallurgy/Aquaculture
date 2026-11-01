package com.teammetallurgy.aquaculture.misc;

import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.init.AquaContextIntProviders;
import com.teammetallurgy.aquaculture.init.AquaItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Compostable;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;

@EventBusSubscriber(modid = Aquaculture.MOD_ID)
public class Compostables {

    @SubscribeEvent
    public static void addCompostables(ModifyDefaultComponentsEvent event) {
        compostable(event, AquaItems.MINNOW.get(), AquaContextIntProviders.COMPOSTABLE_CHANCE_5);
        compostable(event, AquaItems.PINK_SALMON.get(), AquaContextIntProviders.COMPOSTABLE_CHANCE_5);
        compostable(event, AquaItems.PIRANHA.get(), AquaContextIntProviders.COMPOSTABLE_CHANCE_5);
        compostable(event, AquaItems.SYNODONTIS.get(), AquaContextIntProviders.COMPOSTABLE_CHANCE_5);
        compostable(event, Items.TROPICAL_FISH, AquaContextIntProviders.COMPOSTABLE_CHANCE_5);
        compostable(event, Items.SALMON, AquaContextIntProviders.COMPOSTABLE_CHANCE_5);
        compostable(event, AquaItems.BLUEGILL.get(), AquaContextIntProviders.COMPOSTABLE_CHANCE_5);
        compostable(event, AquaItems.ATLANTIC_HERRING.get(), AquaContextIntProviders.COMPOSTABLE_CHANCE_5);
        compostable(event, AquaItems.BROWN_SHROOMA.get(), AquaContextIntProviders.COMPOSTABLE_CHANCE_5);
        compostable(event, Items.PUFFERFISH, AquaContextIntProviders.COMPOSTABLE_CHANCE_5);
        compostable(event, AquaItems.BROWN_TROUT.get(), AquaContextIntProviders.COMPOSTABLE_CHANCE_5);
        compostable(event, AquaItems.PERCH.get(), AquaContextIntProviders.COMPOSTABLE_CHANCE_5);
        compostable(event, AquaItems.SMALLMOUTH_BASS.get(), AquaContextIntProviders.COMPOSTABLE_CHANCE_5);
        compostable(event, AquaItems.GOLDFISH.get(), AquaContextIntProviders.COMPOSTABLE_CHANCE_5);
        compostable(event, AquaItems.BOULTI.get(), AquaContextIntProviders.COMPOSTABLE_CHANCE_5);
        compostable(event, AquaItems.RED_SHROOMA.get(), AquaContextIntProviders.COMPOSTABLE_CHANCE_5);
        compostable(event, AquaItems.BLACKFISH.get(), AquaContextIntProviders.COMPOSTABLE_CHANCE_5);
        compostable(event, AquaItems.LARGEMOUTH_BASS.get(), AquaContextIntProviders.COMPOSTABLE_CHANCE_10);
        compostable(event, AquaItems.RAINBOW_TROUT.get(), AquaContextIntProviders.COMPOSTABLE_CHANCE_25);
        compostable(event, AquaItems.GAR.get(), AquaContextIntProviders.COMPOSTABLE_CHANCE_25);
        compostable(event, AquaItems.MUSKELLUNGE.get(), AquaContextIntProviders.COMPOSTABLE_CHANCE_25);
        compostable(event, AquaItems.CARP.get(), AquaContextIntProviders.COMPOSTABLE_CHANCE_25);
        compostable(event, AquaItems.POLLOCK.get(), AquaContextIntProviders.COMPOSTABLE_CHANCE_25);
        compostable(event, AquaItems.JELLYFISH.get(), AquaContextIntProviders.COMPOSTABLE_CHANCE_25);
        compostable(event, AquaItems.TAMBAQUI.get(), AquaContextIntProviders.COMPOSTABLE_CHANCE_25);
        compostable(event, AquaItems.BAYAD.get(), AquaContextIntProviders.COMPOSTABLE_CHANCE_25);
        compostable(event, AquaItems.RED_GROUPER.get(), AquaContextIntProviders.COMPOSTABLE_CHANCE_25);
        compostable(event, AquaItems.ALGAE.get(), ContextIntProviders.COMPOSTABLE_LOW);
        compostable(event, AquaItems.CATFISH.get(), ContextIntProviders.COMPOSTABLE_LOW_MEDIUM);
        compostable(event, AquaItems.PACIFIC_HALIBUT.get(), ContextIntProviders.COMPOSTABLE_LOW_MEDIUM);
        compostable(event, Items.COD, ContextIntProviders.COMPOSTABLE_LOW_MEDIUM);
        compostable(event, AquaItems.ARAPAIMA.get(), ContextIntProviders.COMPOSTABLE_LOW_MEDIUM);
        compostable(event, AquaItems.CAPITAINE.get(), ContextIntProviders.COMPOSTABLE_LOW_MEDIUM);
        compostable(event, AquaItems.TUNA.get(), ContextIntProviders.COMPOSTABLE_LOW_MEDIUM);
        compostable(event, AquaItems.ATLANTIC_COD.get(), ContextIntProviders.COMPOSTABLE_LOW_MEDIUM);
        compostable(event, AquaItems.ATLANTIC_HALIBUT.get(), ContextIntProviders.COMPOSTABLE_MEDIUM);
    }

    private static void compostable(ModifyDefaultComponentsEvent event, ItemLike item, ResourceKey<ContextIntProvider> layers) {
        event.modify(item, (components, context, i) -> components.set(DataComponents.COMPOSTABLE, new Compostable(layers)));
    }
}
