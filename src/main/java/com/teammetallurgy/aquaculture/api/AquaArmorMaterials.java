package com.teammetallurgy.aquaculture.api;

import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.init.AquaItems;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class AquaArmorMaterials {
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIAL_DEFERRED = DeferredRegister.create(Registries.ARMOR_MATERIAL, Aquaculture.MOD_ID);

    public static Holder<ArmorMaterial> NEPTUNIUM = register("neptunium", Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
        map.put(ArmorItem.Type.BOOTS, 3);
        map.put(ArmorItem.Type.LEGGINGS, 6);
        map.put(ArmorItem.Type.CHESTPLATE, 8);
        map.put(ArmorItem.Type.HELMET, 3);
        map.put(ArmorItem.Type.HELMET, 11);
    }), 14, SoundEvents.ARMOR_EQUIP_DIAMOND, 2.0F, 0.0F, () -> Ingredient.of(AquaItems.NEPTUNIUM_INGOT.get()));

    public static Holder<ArmorMaterial> register(String name,
            EnumMap<ArmorItem.Type, Integer> defense,
            int enchantmentValue,
            Holder<SoundEvent> equipSound,
            float toughness,
            float knockbackResistance,
            Supplier<Ingredient> repairIngredient
    ) {
        EnumMap<ArmorItem.Type, Integer> armorTypeMap = new EnumMap<>(ArmorItem.Type.class);
        ResourceLocation location = ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, name);

        for (ArmorItem.Type armoritem$type : ArmorItem.Type.values()) {
            armorTypeMap.put(armoritem$type, defense.get(armoritem$type));
        }

        List<ArmorMaterial.Layer> list = List.of(new ArmorMaterial.Layer(location));

        return ARMOR_MATERIAL_DEFERRED.register(name, () -> new ArmorMaterial(armorTypeMap, enchantmentValue, equipSound, repairIngredient, list, toughness, knockbackResistance));
    }
}