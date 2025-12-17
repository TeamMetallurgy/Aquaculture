package com.teammetallurgy.aquaculture.entity;

import com.teammetallurgy.aquaculture.init.AquaEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.SpectralArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SpectralWaterArrowEntity extends SpectralArrow {

    public SpectralWaterArrowEntity(EntityType<? extends SpectralArrow> arrow, Level level) {
        super(arrow, level);
    }

    public SpectralWaterArrowEntity(Level level, LivingEntity livingEntity, ItemStack stack, @Nullable ItemStack weapon) {
        super(level, livingEntity, stack, weapon);
    }

    @Override
    protected float getWaterInertia() {
        return 1.0F;
    }

    @Override
    @Nonnull
    public EntityType<?> getType() {
        return AquaEntities.SPECTRAL_WATER_ARROW.get();
    }
}
