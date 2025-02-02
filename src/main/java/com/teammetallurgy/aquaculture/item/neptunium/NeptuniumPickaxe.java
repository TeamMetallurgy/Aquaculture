package com.teammetallurgy.aquaculture.item.neptunium;

import com.teammetallurgy.aquaculture.init.AquaDataComponents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public class NeptuniumPickaxe extends PickaxeItem {

    public NeptuniumPickaxe(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Properties properties) {
        super(toolMaterial, attackDamage, attackSpeed, properties);
    }

    @Override
    public void inventoryTick(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull Entity entity, int itemSlot, boolean isSelected) {
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