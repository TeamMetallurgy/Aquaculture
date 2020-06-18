package com.teammetallurgy.aquaculture.init;

import com.google.common.collect.Lists;
import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.entity.AquaFishingBobberEntity;
import com.teammetallurgy.aquaculture.entity.TurtleLandEntity;
import com.teammetallurgy.aquaculture.entity.WaterArrowEntity;
import com.teammetallurgy.aquaculture.misc.AquaConfig;
import com.teammetallurgy.aquaculture.misc.BiomeDictionaryHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.teammetallurgy.aquaculture.misc.AquaConfig.Helper;

@Mod.EventBusSubscriber(modid = Aquaculture.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(Aquaculture.MOD_ID)
public class AquaEntities {
    private static List<EntityType<?>> entities = Lists.newArrayList();
    private static List<EntityType<?>> mobs = Lists.newArrayList();
    public static final EntityType<AquaFishingBobberEntity> BOBBER = register("bobber", EntityType.Builder.<AquaFishingBobberEntity>create(EntityClassification.MISC)
            .disableSerialization()
            .disableSummoning()
            .size(0.25F, 0.25F)
            .setTrackingRange(4)
            .setUpdateInterval(5)
            .setCustomClientFactory(AquaFishingBobberEntity::new));
    public static final EntityType<WaterArrowEntity> WATER_ARROW = register("water_arrow", EntityType.Builder.<WaterArrowEntity>create(WaterArrowEntity::new, EntityClassification.MISC)
            .size(0.5F, 0.5F)
            .setCustomClientFactory(WaterArrowEntity::new));
    public static final EntityType<TurtleLandEntity> BOX_TURTLE = registerMob("box_turtle", 1, 2, 7, BiomeDictionary.Type.SWAMP, null, 0x7F8439, 0x5D612A,
            EntityType.Builder.create(TurtleLandEntity::new, EntityClassification.CREATURE)
            .size(0.5F, 0.25F));
    public static final EntityType<TurtleLandEntity> ARRAU_TURTLE = registerMob("arrau_turtle", 1, 2, 4, BiomeDictionary.Type.JUNGLE, null, 0x71857A, 0x4F6258,
            EntityType.Builder.create(TurtleLandEntity::new, EntityClassification.CREATURE)
            .size(0.5F, 0.25F));
    public static final EntityType<TurtleLandEntity> STARSHELL_TURTLE = registerMob("starshell_turtle", 1, 2, 5, BiomeDictionaryHelper.TWILIGHT, null, 0xDCE2E5, 0x464645,
            EntityType.Builder.create(TurtleLandEntity::new, EntityClassification.CREATURE)
            .size(0.5F, 0.25F));

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
        for (EntityType<?> entity : entities) {
            event.getRegistry().register(entity);
        }
    }

    private static <T extends Entity> EntityType<T> registerMob(String name, int min, int max, int weight, BiomeDictionary.Type include, @Nullable BiomeDictionary.Type exclude, int eggPrimary, int eggSecondary, EntityType.Builder<T> builder) {
        return registerMob(name, min, max, weight, eggPrimary, eggSecondary, Collections.singletonList(String.valueOf(include)), Collections.singletonList(String.valueOf(exclude == null ? "" : exclude)), builder);
    }

    private static <T extends Entity> EntityType<T> registerMob(String name, int min, int max, int weight, int eggPrimary, int eggSecondary, List<? extends String> include, List<? extends String> exclude, EntityType.Builder<T> builder) {
        EntityType<T> entityType = register(name, builder);
        Item spawnEgg = new SpawnEggItem(entityType, eggPrimary, eggSecondary, (new Item.Properties()).group(ItemGroup.MISC));
        AquaItems.register(spawnEgg, name + "_spawn_egg");
        new AquaConfig.Spawn(AquaConfig.BUILDER, name, min, max, weight, include, exclude);
        mobs.add(entityType);
        return entityType;
    }

    public static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
        ResourceLocation location = new ResourceLocation(Aquaculture.MOD_ID, name);
        EntityType<T> entityType = builder.build(location.toString());
        entityType.setRegistryName(location);
        entities.add(entityType);
        return entityType;
    }

    public static void setSpawnPlacement() {
        EntitySpawnPlacementRegistry.register(BOX_TURTLE, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, TurtleLandEntity::canAnimalSpawn);
        EntitySpawnPlacementRegistry.register(ARRAU_TURTLE, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, TurtleLandEntity::canAnimalSpawn);
        EntitySpawnPlacementRegistry.register(STARSHELL_TURTLE, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, TurtleLandEntity::canAnimalSpawn);
    }

    public static void addEntitySpawns() {
        for (EntityType<?> entityType : mobs) {
            String name = Objects.requireNonNull(entityType.getRegistryName()).getPath();
            String subCategory = Helper.getSubConfig(AquaConfig.Spawn.SPAWN_OPTIONS, name);
            BiomeDictionaryHelper.addSpawn(entityType, Helper.get(subCategory, "min"), Helper.get(subCategory, "max"), Helper.get(subCategory, "weight"), Helper.get(subCategory, "include"), Helper.get(subCategory, "exclude"));
        }
    }
}