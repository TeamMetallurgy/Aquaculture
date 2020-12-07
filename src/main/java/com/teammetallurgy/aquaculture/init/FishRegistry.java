package com.teammetallurgy.aquaculture.init;

import com.google.common.collect.Lists;
import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.entity.AquaFishEntity;
import com.teammetallurgy.aquaculture.entity.FishMountEntity;
import com.teammetallurgy.aquaculture.entity.FishType;
import com.teammetallurgy.aquaculture.item.FishMountItem;
import com.teammetallurgy.aquaculture.misc.StackHelper;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.passive.fish.AbstractFishEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;
import sun.misc.Unsafe;

import javax.annotation.Nonnull;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Aquaculture.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FishRegistry {
    public static List<EntityType<AquaFishEntity>> fishEntities = Lists.newArrayList();
    public static List<EntityType<FishMountEntity>> fishMounts = Lists.newArrayList();

    public static Item registerFishMount(@Nonnull String name) {
        EntityType.Builder<FishMountEntity> fishMountBuilder = EntityType.Builder.<FishMountEntity>create(FishMountEntity::new, EntityClassification.MISC)
                .size(0.5F, 0.5F)
                .setCustomClientFactory(FishMountEntity::new);
        EntityType<FishMountEntity> fishMount = AquaEntities.register(name, fishMountBuilder);
        FishMountItem fishMountItem = new FishMountItem(fishMount);
        AquaItems.register(fishMountItem, name);
        fishMounts.add(fishMount);
        return fishMountItem;
    }

    /**
     * Same as {@link #register(Item, String, FishType)}, but with default size
     */
    public static Item register(@Nonnull Item fishItem, @Nonnull String name) {
        return register(fishItem, name, FishType.MEDIUM);
    }

    /**
     * Registers the fish item, fish entity and fish bucket
     *
     * @param fishItem The fish item to be registered
     * @param name     The fish name
     * @return The fish Item that was registered
     */
    public static Item register(@Nonnull Item fishItem, @Nonnull String name, FishType fishSize) {
        AquaItems.register(fishItem, name);
        EntityType<AquaFishEntity> fish = EntityType.Builder.create(AquaFishEntity::new, EntityClassification.WATER_AMBIENT).size(fishSize.getWidth(), fishSize.getHeight()).build("minecraft:cod"); //TODO Change when Forge allow for custom datafixers
        registerFishEntity(name, fish);
        AquaFishEntity.TYPES.put(fish, fishSize);
        return fishItem;
    }

    public static EntityType<AquaFishEntity> registerFishEntity(String name, EntityType<AquaFishEntity> entityType) {
        ResourceLocation location = new ResourceLocation(Aquaculture.MOD_ID, name);
        entityType.setRegistryName(location);
        fishEntities.add(entityType);
        return entityType;
    }

    @SubscribeEvent
    public static void registerFishies(RegistryEvent.Register<EntityType<?>> event) {
        for (EntityType<AquaFishEntity> entityType : fishEntities) {
            event.getRegistry().register(entityType);
            EntitySpawnPlacementRegistry.register(entityType, EntitySpawnPlacementRegistry.PlacementType.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AquaFishEntity::canSpawnHere);
            GlobalEntityTypeAttributes.put(entityType, AbstractFishEntity.func_234176_m_().create());
        }
    }

    public static void addCatBreeding() {
        try {
            Field catItems = ObfuscationReflectionHelper.findField(CatEntity.class, "field_213426_bE");
            Field ocelotItems = ObfuscationReflectionHelper.findField(OcelotEntity.class, "field_195402_bB");
            List<ItemStack> aquaFish = new ArrayList<>();
            fishEntities.forEach(f -> aquaFish.add(new ItemStack(ForgeRegistries.ITEMS.getValue(f.getRegistryName()))));
            aquaFish.removeIf(p -> p.getItem().equals(AquaItems.JELLYFISH));

            setFinalStatic(catItems, StackHelper.mergeIngredient(CatEntity.BREEDING_ITEMS, StackHelper.ingredientFromStackList(aquaFish)));
            setFinalStatic(ocelotItems, StackHelper.mergeIngredient(OcelotEntity.BREEDING_ITEMS, StackHelper.ingredientFromStackList(aquaFish)));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    static MethodHandles.Lookup implLookup;

    static {
        try {
            Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            Unsafe unsafe = (Unsafe) unsafeField.get(null);
            Field implLookupField = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
            implLookup = (MethodHandles.Lookup) unsafe.getObject(unsafe.staticFieldBase(implLookupField), unsafe.staticFieldOffset(implLookupField));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    static void setFinalStatic(Field field, Object newValue) throws Throwable {
        implLookup.findStaticSetter(field.getDeclaringClass(), field.getName(), field.getType()).invoke(newValue);
    }
}