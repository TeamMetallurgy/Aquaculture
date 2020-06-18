package com.teammetallurgy.aquaculture.item.neptunium;

import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.entity.WaterArrowEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;

import javax.annotation.Nonnull;

public class NeptuniumBow extends BowItem {

    public NeptuniumBow() {
        super(new Item.Properties().group(Aquaculture.GROUP).maxStackSize(1).maxDamage(2500));
    }

    @Override
    @Nonnull
    public AbstractArrowEntity customeArrow(@Nonnull AbstractArrowEntity arrowEntity) {
        if (arrowEntity.getType() == EntityType.ARROW) {
            return new WaterArrowEntity(arrowEntity.world, arrowEntity.world.getPlayerByUuid(arrowEntity.shootingEntity));
        } else {
            return super.customeArrow(arrowEntity);
        }
    }
}