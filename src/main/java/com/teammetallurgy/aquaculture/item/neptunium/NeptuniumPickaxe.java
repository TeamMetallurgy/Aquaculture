package com.teammetallurgy.aquaculture.item.neptunium;

import com.teammetallurgy.aquaculture.init.AquaDataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public class NeptuniumPickaxe extends Item {

    public NeptuniumPickaxe(ToolMaterial toolMaterial, float attackDamage, float attackSpeed, Item.Properties properties) {
        super(properties.pickaxe(toolMaterial, attackDamage, attackSpeed));
    }

    @Override
    public void inventoryTick(@Nonnull ItemStack stack, @Nonnull ServerLevel level, @Nonnull Entity entity, @Nonnull EquipmentSlot slot) {
        if (entity instanceof Player player && stack.getItem() == this) {
            stack.set(AquaDataComponents.IN_WATER, player.isEyeInFluid(FluidTags.WATER));
        }
    }

    @Override
    public float getDestroySpeed(@Nonnull ItemStack stack, @Nonnull BlockState state) {
        float defaultSpeed = super.getDestroySpeed(stack, state);
        Boolean inWater = stack.get(AquaDataComponents.IN_WATER);
        boolean isInWater = stack.has(AquaDataComponents.IN_WATER) && inWater != null && inWater;
        return isInWater ? (defaultSpeed * 5.0F) * 5.0F : defaultSpeed;
    }
}