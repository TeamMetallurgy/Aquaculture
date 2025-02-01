package com.teammetallurgy.aquaculture.init;

import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.entity.AquaFishEntity;
import com.teammetallurgy.aquaculture.entity.FishMountEntity;
import com.teammetallurgy.aquaculture.entity.FishType;
import com.teammetallurgy.aquaculture.item.AquaFishBucket;
import com.teammetallurgy.aquaculture.item.FishMountItem;
import com.teammetallurgy.aquaculture.loot.BiomeTagCheck;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.RegisterEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

@EventBusSubscriber(modid = Aquaculture.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class FishRegistry {
    public static List<DeferredHolder<EntityType<?>, EntityType<AquaFishEntity>>> fishEntities = new ArrayList<>();
    public static List<DeferredHolder<EntityType<?>, EntityType<FishMountEntity>>> fishMounts = new ArrayList<>();

    public static DeferredItem<Item> registerFishMount(@Nonnull String name) {
        DeferredHolder<EntityType<?>, EntityType<FishMountEntity>> fishMount = AquaEntities.ENTITY_DEFERRED.register(name, () -> EntityType.Builder.<FishMountEntity>of(FishMountEntity::new, MobCategory.MISC)
                .sized(0.5F, 0.5F)
                .eyeHeight(0.0F)
                .build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, name))));
        DeferredItem<Item> fishMountItem = AquaItems.registerWithTab(p -> new FishMountItem(fishMount, p), name);
        fishMounts.add(fishMount);
        return fishMountItem;
    }

    /**
     * Same as {@link #register(Function, String, FishType)}, but with default size
     */
    public static DeferredItem<Item> register(Function<Item.Properties, ? extends Item> function, @Nonnull String name) {
        return register(function, name, FishType.MEDIUM);
    }

    /**
     * Registers the fish item, fish entity and fish bucket
     *
     * @param function The fish initializer
     * @param name        The fish name
     * @return The fish Item that was registered
     */
    public static DeferredItem<Item> register(Function<Item.Properties, ? extends Item> function, @Nonnull String name, FishType fishType) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, name);
        DeferredHolder<EntityType<?>, EntityType<AquaFishEntity>> fish = AquaEntities.ENTITY_DEFERRED.register(name, () -> EntityType.Builder.<AquaFishEntity>of((f, w) -> new AquaFishEntity(f, w, fishType), MobCategory.WATER_AMBIENT).sized(fishType.getWidth(), fishType.getHeight()).build(ResourceKey.create(Registries.ENTITY_TYPE, id)));
        fishEntities.add(fish);

        //Registers fish buckets
        DeferredItem<Item> bucket = AquaItems.register(p -> new AquaFishBucket(fish.value(), p.stacksTo(1)), name + "_bucket");
        AquaItems.ITEMS_FOR_TAB_LIST.add(bucket);

        return AquaItems.registerWithTab(function, name);
    }

    @SubscribeEvent
    public static void registerFishies(RegisterEvent event) {
        if (!event.getRegistryKey().equals(Registries.LOOT_CONDITION_TYPE)) return;
        event.register(Registries.LOOT_CONDITION_TYPE, ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, "biome_tag_check"), () -> BiomeTagCheck.BIOME_TAG_CHECK);
    }
}