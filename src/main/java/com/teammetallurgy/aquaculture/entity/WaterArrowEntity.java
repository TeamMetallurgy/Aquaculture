package com.teammetallurgy.aquaculture.entity;

import com.teammetallurgy.aquaculture.init.AquaEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class WaterArrowEntity extends Arrow {

    public WaterArrowEntity(EntityType<? extends Arrow> arrow, Level level) {
        super(arrow, level);
    }

    public WaterArrowEntity(Level level, LivingEntity livingEntity, ItemStack stack, @Nullable ItemStack weapon) {
        super(level, livingEntity, stack, weapon);
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