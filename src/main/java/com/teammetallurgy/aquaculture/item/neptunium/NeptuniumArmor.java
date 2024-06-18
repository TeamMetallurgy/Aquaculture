package com.teammetallurgy.aquaculture.item.neptunium;

import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.init.AquaItems;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import javax.annotation.Nonnull;

@EventBusSubscriber(modid = Aquaculture.MOD_ID)
public class NeptuniumArmor extends ArmorItem {
    protected static final ResourceLocation NEPTUNIUM_BOOTS_SWIM_SPEED = ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, "neptunium_boots_swim_speed");
    private static final AttributeModifier INCREASED_SWIM_SPEED = new AttributeModifier(NEPTUNIUM_BOOTS_SWIM_SPEED, 0.5D, AttributeModifier.Operation.ADD_VALUE);
    private String texture;

    public NeptuniumArmor(Holder<ArmorMaterial> armorMaterial, ArmorItem.Type type) {
        super(armorMaterial, type, new Item.Properties());
    }

    @Override
    public void inventoryTick(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull Entity entity, int slot, boolean b) {
        super.inventoryTick(stack, level, entity, slot, b);
        if (entity instanceof Player player) {
            if (player.isEyeInFluidType(NeoForgeMod.WATER_TYPE.value())) {
                if (this.getType() == Type.HELMET) {
                    player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 20, 0, false, false, false));
                } else if (this.getType() == Type.CHESTPLATE) {
                    player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 20, 0, false, false, false));
                } else if (this.getType() == Type.LEGGINGS) {
                    if (!player.isCrouching() && !player.jumping && !player.isSwimming()) {
                        player.setDeltaMovement(Vec3.ZERO);
                    }
                }
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

    public Item setArmorTexture(String string) {
        this.texture = string;
        return this;
    }

    @Override
    public ResourceLocation getArmorTexture(@Nonnull ItemStack stack, @Nonnull Entity entity, @Nonnull EquipmentSlot slot, @Nonnull ArmorMaterial.Layer layer, boolean isInnerModel) {
        return ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, "textures/armor/" + this.texture + ".png");
    }
}