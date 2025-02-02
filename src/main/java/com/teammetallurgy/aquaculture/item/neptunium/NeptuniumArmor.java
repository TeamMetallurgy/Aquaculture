package com.teammetallurgy.aquaculture.item.neptunium;

import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.init.AquaItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import javax.annotation.Nonnull;

@EventBusSubscriber(modid = Aquaculture.MOD_ID)
public class NeptuniumArmor extends ArmorItem {
    protected static final ResourceLocation NEPTUNIUM_BOOTS_SWIM_SPEED = ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, "neptunium_boots_swim_speed");
    private static final AttributeModifier INCREASED_SWIM_SPEED = new AttributeModifier(NEPTUNIUM_BOOTS_SWIM_SPEED, 0.5D, AttributeModifier.Operation.ADD_VALUE);

    public NeptuniumArmor(ArmorMaterial armorMaterial, ArmorType type, Properties properties) {
        super(armorMaterial, type, properties.durability(type.getDurability(35)));
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

    @SubscribeEvent
    public static void onLivingTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();

        if (!player.level().isClientSide) {
            AttributeInstance swimSpeed = player.getAttribute(NeoForgeMod.SWIM_SPEED);
            if (swimSpeed != null) {
                if (player.isInWater() && player.getItemBySlot(EquipmentSlot.FEET).getItem() == AquaItems.NEPTUNIUM_BOOTS.get()) {
                    if (!swimSpeed.hasModifier(NEPTUNIUM_BOOTS_SWIM_SPEED)) {
                        swimSpeed.addPermanentModifier(INCREASED_SWIM_SPEED);
                    }
                } else {
                    if (swimSpeed.hasModifier(NEPTUNIUM_BOOTS_SWIM_SPEED)) {
                        swimSpeed.removeModifier(INCREASED_SWIM_SPEED);
                    }
                }
            }
        }
    }
}