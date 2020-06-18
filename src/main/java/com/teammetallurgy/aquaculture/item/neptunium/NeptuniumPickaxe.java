package com.teammetallurgy.aquaculture.item.neptunium;

import com.teammetallurgy.aquaculture.Aquaculture;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class NeptuniumPickaxe extends PickaxeItem {

    public NeptuniumPickaxe(IItemTier tier, int attackDamage, float attackSpeed) {
        super(tier, attackDamage, attackSpeed, new Item.Properties().group(Aquaculture.GROUP));
    }

    @Override
    public void inventoryTick(@Nonnull ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (entity instanceof PlayerEntity && stack.getItem() == this) {
            PlayerEntity player = (PlayerEntity) entity;
            stack.getOrCreateTag().putBoolean("inWater", player.areEyesInFluid(FluidTags.WATER));
        }
    }

    @Override
    public float getDestroySpeed(@Nonnull ItemStack stack, BlockState state) {
        float defaultSpeed = super.getDestroySpeed(stack, state);
        boolean isInWater = stack.hasTag() && stack.getTag() != null && stack.getTag().getBoolean("inWater");
        return isInWater ? (defaultSpeed * 5.0F) * 5.0F : defaultSpeed;
    }
}