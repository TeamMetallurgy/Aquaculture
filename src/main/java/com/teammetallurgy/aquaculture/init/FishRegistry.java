package com.teammetallurgy.aquaculture.init;

import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.entity.AquaFishEntity;
import com.teammetallurgy.aquaculture.entity.FishMountEntity;
import com.teammetallurgy.aquaculture.entity.FishType;
import com.teammetallurgy.aquaculture.item.AquaFishBucket;
import com.teammetallurgy.aquaculture.item.FishMountItem;
import com.teammetallurgy.aquaculture.misc.StackHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Aquaculture.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FishRegistry {
    public static List<RegistryObject<EntityType<AquaFishEntity>>> fishEntities = new ArrayList<>();
    public static List<RegistryObject<EntityType<FishMountEntity>>> fishMounts = new ArrayList<>();

    public static RegistryObject<Item> registerFishMount(@Nonnull String name) {
        RegistryObject<EntityType<FishMountEntity>> fishMount = AquaEntities.ENTITY_DEFERRED.register(name, () -> EntityType.Builder.<FishMountEntity>of(FishMountEntity::new, MobCategory.MISC)
                .sized(0.5F, 0.5F)
                .setCustomClientFactory(FishMountEntity::new).build(Aquaculture.MOD_ID + ":" + name));
        RegistryObject<Item> fishMountItem = AquaItems.registerWithTab(() -> new FishMountItem(fishMount), name);
        fishMounts.add(fishMount);
        return fishMountItem;
    }

    /**
     * Same as {@link #register(Supplier, String, FishType)}, but with default size
     */
    public static RegistryObject<Item> register(@Nonnull Supplier<Item> initializer, @Nonnull String name) {
        return register(initializer, name, FishType.MEDIUM);
    }

    /**
     * Registers the fish item, fish entity and fish bucket
     *
     * @param initializer The fish initializer
     * @param name        The fish name
     * @return The fish Item that was registered
     */
    public static RegistryObject<Item> register(@Nonnull Supplier<Item> initializer, @Nonnull String name, FishType fishType) {
        RegistryObject<EntityType<AquaFishEntity>> fish = AquaEntities.ENTITY_DEFERRED.register(name, () -> EntityType.Builder.<AquaFishEntity>of((f, w) -> new AquaFishEntity(f, w, fishType), MobCategory.WATER_AMBIENT).sized(fishType.getWidth(), fishType.getHeight()).build(Aquaculture.MOD_ID + ":" + name));
        fishEntities.add(fish);

        //Registers fish buckets
        RegistryObject<Item> bucket = AquaItems.ITEM_DEFERRED.register(name + "_bucket", () -> new AquaFishBucket(fish, new Item.Properties().stacksTo(1)));
        AquaItems.ITEMS_FOR_TAB_LIST.add(bucket);

        return AquaItems.registerWithTab(initializer, name);
    }

    @SubscribeEvent
    public static void addFishEntity0Attributes(EntityAttributeCreationEvent event) {
        for (RegistryObject<EntityType<AquaFishEntity>> entityType : fishEntities) {
            event.put(entityType.get(), AbstractFish.createAttributes().build());
        }
    }

    public static void addCatBreeding() {
        try {
            Ingredient catBreedingItems = Cat.TEMPT_INGREDIENT;
            Ingredient ocelotBreedingItems = Ocelot.TEMPT_INGREDIENT;
            List<ItemStack> aquaFish = new ArrayList<>();
            fishEntities.forEach(f -> aquaFish.add(new ItemStack(ForgeRegistries.ITEMS.getValue(ForgeRegistries.ENTITY_TYPES.getKey(f.get())))));
            aquaFish.removeIf(p -> p.getItem().equals(AquaItems.JELLYFISH.get()));

            Cat.TEMPT_INGREDIENT = StackHelper.mergeIngredient(catBreedingItems, StackHelper.ingredientFromStackList(aquaFish));
            Ocelot.TEMPT_INGREDIENT = StackHelper.mergeIngredient(ocelotBreedingItems, StackHelper.ingredientFromStackList(aquaFish));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}