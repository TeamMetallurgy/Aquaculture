package com.teammetallurgy.aquaculture.init;

import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.entity.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@EventBusSubscriber(modid = Aquaculture.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class AquaEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_DEFERRED = DeferredRegister.create(Registries.ENTITY_TYPE, Aquaculture.MOD_ID);
    public static final DeferredHolder<EntityType<?>, EntityType<AquaFishingBobberEntity>> BOBBER = register("bobber", () -> EntityType.Builder.<AquaFishingBobberEntity>of(AquaFishingBobberEntity::new, MobCategory.MISC)
            .noSave()
            .noSummon()
            .sized(0.25F, 0.25F)
            .setTrackingRange(4)
            .setUpdateInterval(5));
    public static final DeferredHolder<EntityType<?>, EntityType<WaterArrowEntity>> WATER_ARROW = register("water_arrow", () -> EntityType.Builder.<WaterArrowEntity>of(WaterArrowEntity::new, MobCategory.MISC)
            .sized(0.5F, 0.5F));
    public static final DeferredHolder<EntityType<?>, EntityType<SpectralWaterArrowEntity>> SPECTRAL_WATER_ARROW = register("spectral_water_arrow", () -> EntityType.Builder.<SpectralWaterArrowEntity>of(SpectralWaterArrowEntity::new, MobCategory.MISC)
            .sized(0.5F, 0.5F));
    public static final DeferredHolder<EntityType<?>, EntityType<TurtleLandEntity>> BOX_TURTLE = registerMob("box_turtle", 0x7F8439, 0x5D612A,
            () -> EntityType.Builder.of(TurtleLandEntity::new, MobCategory.CREATURE)
                    .eyeHeight(0.1875F)
                    .sized(0.5F, 0.25F));
    public static final DeferredHolder<EntityType<?>, EntityType<TurtleLandEntity>> ARRAU_TURTLE = registerMob("arrau_turtle", 0x71857A, 0x4F6258,
            () -> EntityType.Builder.of(TurtleLandEntity::new, MobCategory.CREATURE)
                    .eyeHeight(0.1875F)
                    .sized(0.5F, 0.25F));
    public static final DeferredHolder<EntityType<?>, EntityType<TurtleLandEntity>> STARSHELL_TURTLE = registerMob("starshell_turtle", 0xDCE2E5, 0x464645,
            () -> EntityType.Builder.of(TurtleLandEntity::new, MobCategory.CREATURE)
                    .eyeHeight(0.1875F)
                    .sized(0.5F, 0.25F));

    private static <T extends Mob> DeferredHolder<EntityType<?>, EntityType<T>> registerMob(String name, int eggPrimary, int eggSecondary, Supplier<EntityType.Builder<T>> builder) {
        DeferredHolder<EntityType<?>, EntityType<T>> entityType = register(name, builder);
        DeferredItem<Item> spawnEggItem = AquaItems.register(() -> new DeferredSpawnEggItem(entityType, eggPrimary, eggSecondary, new Item.Properties()), name + "_spawn_egg");
        AquaItems.SPAWN_EGGS.add(spawnEggItem);
        return entityType;
    }

    public static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> register(String name, Supplier<EntityType.Builder<T>> builder) {
        ResourceLocation location = ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, name);
        return ENTITY_DEFERRED.register(name, () -> builder.get().build(location.toString()));
    }

    @SubscribeEvent
    public static void setSpawnPlacement(RegisterSpawnPlacementsEvent event) {
        event.register(BOX_TURTLE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TurtleLandEntity::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(ARRAU_TURTLE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TurtleLandEntity::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        event.register(STARSHELL_TURTLE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TurtleLandEntity::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);

        for (DeferredHolder<EntityType<?>, EntityType<AquaFishEntity>> entityType : FishRegistry.fishEntities) {
            event.register(entityType.get(), SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AquaFishEntity::checkSurfaceWaterAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.AND);
        }
    }

    @SubscribeEvent
    public static void addEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(BOX_TURTLE.get(), TurtleLandEntity.createAttributes().build());
        event.put(ARRAU_TURTLE.get(), TurtleLandEntity.createAttributes().build());
        event.put(STARSHELL_TURTLE.get(), TurtleLandEntity.createAttributes().build());

        for (DeferredHolder<EntityType<?>, EntityType<AquaFishEntity>> entityType : FishRegistry.fishEntities) {
            event.put(entityType.get(), AquaFishEntity.createAttributes().build());
        }
    }
}