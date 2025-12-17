package com.teammetallurgy.aquaculture.api;

import com.teammetallurgy.aquaculture.Aquaculture;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

import java.util.EnumMap;

public class AquaArmorMaterials {
    private static final ResourceKey<EquipmentAsset> NEPTUNIUM_ASSET = createId("neptunium");

    public static final ArmorMaterial NEPTUNIUM = new ArmorMaterial(15, Util.make(new EnumMap<>(ArmorType.class), map -> {
        map.put(ArmorType.BOOTS, 3);
        map.put(ArmorType.LEGGINGS, 6);
        map.put(ArmorType.CHESTPLATE, 8);
        map.put(ArmorType.HELMET, 3);
    }), 14, SoundEvents.ARMOR_EQUIP_DIAMOND, 2.0F, 0.0F, AquacultureAPI.Tags.REPAIRS_NEPTUNIUM, NEPTUNIUM_ASSET);

    public static ResourceKey<EquipmentAsset> createId(String name) {
        return ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(Aquaculture.MOD_ID, name));
    }
}