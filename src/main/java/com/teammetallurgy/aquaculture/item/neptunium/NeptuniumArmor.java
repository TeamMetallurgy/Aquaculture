package com.teammetallurgy.aquaculture.item.neptunium;

import com.google.common.collect.ImmutableList;
import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.api.AquaArmorMaterials;
import com.teammetallurgy.aquaculture.init.AquaItems;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.neoforge.common.NeoForgeMod;

import javax.annotation.Nonnull;

public class NeptuniumArmor extends Item {
    protected static final Identifier NEPTUNIUM_BOOTS_SWIM_SPEED = Identifier.fromNamespaceAndPath(Aquaculture.MOD_ID, "neptunium_boots_swim_speed");
    private static final AttributeModifier INCREASED_SWIM_SPEED = new AttributeModifier(NEPTUNIUM_BOOTS_SWIM_SPEED, 1.5D, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);

    public NeptuniumArmor(ArmorMaterial armorMaterial, ArmorType type, Item.Properties properties) {
        super(properties.humanoidArmor(armorMaterial, type).durability(type.getDurability(35)));
    }

    public NeptuniumArmor(ArmorMaterial armorMaterial, ArmorType type, Item.Properties properties, ItemAttributeModifiers attributes) {
        super(properties.humanoidArmor(armorMaterial, type).durability(type.getDurability(35)).attributes(attributes));
    }

    @Override
    public void inventoryTick(@Nonnull ItemStack stack, @Nonnull ServerLevel level, @Nonnull Entity entity, EquipmentSlot slot) {
        super.inventoryTick(stack, level, entity, slot);
        if (entity instanceof Player player) {
            if (player.isEyeInFluidType(NeoForgeMod.WATER_TYPE.value())) {
                if (player.getItemBySlot(EquipmentSlot.HEAD).getItem() == AquaItems.NEPTUNIUM_HELMET.get()) {
                    player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 20, 0, false, false, false));
                }
                if (player.getItemBySlot(EquipmentSlot.CHEST).getItem() == AquaItems.NEPTUNIUM_CHESTPLATE.get()) {
                    player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 20, 0, false, false, false));
                }
                if (player.getItemBySlot(EquipmentSlot.LEGS).getItem() == AquaItems.NEPTUNIUM_LEGGINGS.get()) {
                    player.setNoGravity(!player.isCrouching() && !player.jumping && !player.isSwimming() && !player.isSpectator());
                } else {
                    player.setNoGravity(false);
                }
            } else {
                player.setNoGravity(false);
            }
        }
    }

    public static ItemAttributeModifiers createBootAttributes() {
        ItemAttributeModifiers attributeModifiers = AquaArmorMaterials.NEPTUNIUM.createAttributes(ArmorType.BOOTS);

        ImmutableList.Builder<ItemAttributeModifiers.Entry> builder = ImmutableList.builderWithExpectedSize(attributeModifiers.modifiers().size() + 1);
        for (ItemAttributeModifiers.Entry entry : attributeModifiers.modifiers()) {
            builder.add(entry);
        }

        builder.add(new ItemAttributeModifiers.Entry(NeoForgeMod.SWIM_SPEED, INCREASED_SWIM_SPEED, EquipmentSlotGroup.FEET));

        return new ItemAttributeModifiers(builder.build());
    }
}