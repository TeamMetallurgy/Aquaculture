package com.teammetallurgy.aquaculture.item.neptunium;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;

import javax.annotation.Nonnull;
import java.util.UUID;

public class NeptuniumArmor extends ArmorItem {
    private static final AttributeModifier INCREASED_SWIM_SPEED = new AttributeModifier(UUID.fromString("d820cadc-2d19-421c-b19f-4c1f5b84a418"), "Neptunium Boots swim speed boost", 0.5D, AttributeModifier.Operation.ADDITION);
    private String texture;

    public NeptuniumArmor(ArmorMaterial armorMaterial, ArmorItem.Type type) {
        super(armorMaterial, type, new Item.Properties());
    }

    @Override
    public void onArmorTick(@Nonnull ItemStack stack, Level world, Player player) {
        if (player.isEyeInFluidType(ForgeMod.WATER_TYPE.get())) {
            if (this.type == Type.HELMET) {
                player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 20, 0, false, false, false));
            } else if (this.type == Type.CHESTPLATE) {
                player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 20, 0, false, false, false));
            } else if (this.type == Type.LEGGINGS) {
                if (!player.isCrouching() && !player.jumping && !player.isSwimming()) {
                    player.setDeltaMovement(Vec3.ZERO);
                }
            }
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> attributeBuilder = ImmutableMultimap.builder();
        attributeBuilder.putAll(super.getDefaultAttributeModifiers(slot));
        attributeBuilder.put(ForgeMod.SWIM_SPEED.get(), INCREASED_SWIM_SPEED);
        return slot == EquipmentSlot.FEET ? attributeBuilder.build() : super.getDefaultAttributeModifiers(slot);
    }

    public Item setArmorTexture(String string) {
        this.texture = string;
        return this;
    }

    @Override
    public String getArmorTexture(@Nonnull ItemStack stack, Entity entity, EquipmentSlot slot, String layer) {
        return "aquaculture:textures/armor/" + this.texture + ".png";
    }
}