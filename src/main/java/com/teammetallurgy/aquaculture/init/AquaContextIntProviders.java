package com.teammetallurgy.aquaculture.init;

import com.teammetallurgy.aquaculture.Aquaculture;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;

/*
 * Keys for the providers in data/aquaculture/context_int_provider
 */
public class AquaContextIntProviders {
    public static final ResourceKey<ContextIntProvider> COOKING_TIME_WOODEN_FILLET_KNIFE = createKey("cooking/time_wooden_fillet_knife");
    public static final ResourceKey<ContextIntProvider> COOKING_TIME_DRIFTWOOD = createKey("cooking/time_driftwood");
    public static final ResourceKey<ContextIntProvider> COMPOSTABLE_CHANCE_5 = createKey("compostable/chance_5");
    public static final ResourceKey<ContextIntProvider> COMPOSTABLE_CHANCE_10 = createKey("compostable/chance_10");
    public static final ResourceKey<ContextIntProvider> COMPOSTABLE_CHANCE_25 = createKey("compostable/chance_25");

    private static ResourceKey<ContextIntProvider> createKey(String path) {
        return ResourceKey.create(Registries.CONTEXT_INT_PROVIDER, Identifier.fromNamespaceAndPath(Aquaculture.MOD_ID, path));
    }
}
