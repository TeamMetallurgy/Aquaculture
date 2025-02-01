package com.teammetallurgy.aquaculture.item.neptunium;

import com.teammetallurgy.aquaculture.entity.SpectralWaterArrowEntity;
import com.teammetallurgy.aquaculture.entity.WaterArrowEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class NeptuniumBow extends BowItem {

    public NeptuniumBow(Properties properties) {
        super(properties.stacksTo(1).durability(2500));
    }

    @Override
    @Nonnull
    public AbstractArrow customArrow(@Nonnull AbstractArrow arrowEntity, @Nonnull ItemStack projectileStack, @Nonnull ItemStack weaponStack) {
        Entity shooter = arrowEntity.getOwner();

        if (arrowEntity.getType() == EntityType.ARROW) {
            if (shooter instanceof LivingEntity) {
                return new WaterArrowEntity(arrowEntity.level(), (LivingEntity) shooter, projectileStack.copyWithCount(1), weaponStack);
            }
        }
        if (arrowEntity.getType() == EntityType.SPECTRAL_ARROW) {
            if (shooter instanceof LivingEntity) {
                return new SpectralWaterArrowEntity(arrowEntity.level(), (LivingEntity) shooter, projectileStack.copyWithCount(1), weaponStack);
            }
        }
        return super.customArrow(arrowEntity, projectileStack, weaponStack);
    }
}