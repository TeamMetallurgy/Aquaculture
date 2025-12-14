package com.teammetallurgy.aquaculture.item.neptunium;

import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.init.AquaItems;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForgeMod;

import javax.annotation.Nonnull;

public class NeptuniumArmor extends ArmorItem {
    protected static final ResourceLocation NEPTUNIUM_BOOTS_SWIM_SPEED = ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, "neptunium_boots_swim_speed");
    private static final AttributeModifier INCREASED_SWIM_SPEED = new AttributeModifier(NEPTUNIUM_BOOTS_SWIM_SPEED, 0.5D, AttributeModifier.Operation.ADD_VALUE);
    private String texture;

    public NeptuniumArmor(Holder<ArmorMaterial> armorMaterial, ArmorItem.Type type) {
        super(armorMaterial, type, new Item.Properties().durability(type.getDurability(35)));
    }

    @Override
    public void inventoryTick(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull Entity entity, int slot, boolean b) {
        super.inventoryTick(stack, level, entity, slot, b);
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

    public Item setArmorTexture(String string) {
        this.texture = string;
        return this;
    }

    @Override
    public ResourceLocation getArmorTexture(@Nonnull ItemStack stack, @Nonnull Entity entity, @Nonnull EquipmentSlot slot, @Nonnull ArmorMaterial.Layer layer, boolean isInnerModel) {
        return ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, "textures/armor/" + this.texture + ".png");
    }

    @Override
    @Nonnull
    public ItemAttributeModifiers getDefaultAttributeModifiers(@Nonnull ItemStack stack) {
        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
        builder.add(NeoForgeMod.SWIM_SPEED, INCREASED_SWIM_SPEED, EquipmentSlotGroup.FEET);
        return builder.build();
    }
}