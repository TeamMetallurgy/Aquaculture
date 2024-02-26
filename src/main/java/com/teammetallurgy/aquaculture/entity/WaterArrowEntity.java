package com.teammetallurgy.aquaculture.entity;

import com.teammetallurgy.aquaculture.init.AquaEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public class WaterArrowEntity extends Arrow {

    public WaterArrowEntity(EntityType<? extends Arrow> arrow, Level level) {
        super(arrow, level);
    }

    public WaterArrowEntity(Level level, LivingEntity livingEntity, ItemStack stack) {
        super(level, livingEntity, stack);
    }

    @Override
    protected float getWaterInertia() {
        return 1.0F;
    }

    @Override
    @Nonnull
    public EntityType<?> getType() {
        return AquaEntities.WATER_ARROW.get();
    }
}