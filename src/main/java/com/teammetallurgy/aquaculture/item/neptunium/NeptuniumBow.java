package com.teammetallurgy.aquaculture.item.neptunium;

import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.entity.SpectralWaterArrowEntity;
import com.teammetallurgy.aquaculture.entity.WaterArrowEntity;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.TypeCache;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import net.bytebuddy.matcher.ElementMatchers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.Item;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NeptuniumBow extends BowItem {
    private static TypeCache<Class<?>> TYPE_CACHE = new TypeCache<>();

    public NeptuniumBow() {
        super(new Item.Properties().group(Aquaculture.GROUP).maxStackSize(1).maxDamage(2500));
    }

    @Override
    @Nonnull
    public AbstractArrowEntity customArrow(@Nonnull AbstractArrowEntity arrowEntity) {
        if (arrowEntity.getType() == EntityType.ARROW) {
            Entity shooter = arrowEntity.func_234616_v_();
            if (shooter instanceof LivingEntity) {
                return new WaterArrowEntity(arrowEntity.world, (LivingEntity) shooter);
            }
        }
        if (arrowEntity.getType() == EntityType.SPECTRAL_ARROW) {
            Entity shooter = arrowEntity.func_234616_v_();
            if (shooter instanceof LivingEntity) {
                return new SpectralWaterArrowEntity(arrowEntity.world, (LivingEntity) shooter);
            }
        }

        // pretty ugly hack to make modded arrows work using byte-buddy
        // arrows stop travelling smoothly through water after a chunk reload
        Class<?> arrowClass = arrowEntity.getClass();
        Class<?> waterArrowClass = TYPE_CACHE.findOrInsert(getClass().getClassLoader(), arrowClass, () -> {
            DynamicType.Unloaded<?> unloadedType =  new ByteBuddy()
                    .subclass(arrowClass)
                    .method(ElementMatchers.named("getWaterDrag")
                            .or(ElementMatchers.named("func_203044_p")))
                    .intercept(FixedValue.value(1.0f))
                    .make();

            return unloadedType.load(getClass().getClassLoader()).getLoaded();
        });

        try {
            Constructor<?> constructor = waterArrowClass.getConstructor(EntityType.class, World.class);
            AbstractArrowEntity instance = (AbstractArrowEntity) constructor
                    .newInstance(arrowEntity.getType(), arrowEntity.getEntityWorld());
            instance.deserializeNBT(arrowEntity.serializeNBT());
            return super.customArrow(instance);
        } catch (NoSuchMethodException |
                 InvocationTargetException |
                 InstantiationException |
                 IllegalAccessException e) {
            e.printStackTrace();
        }
        return super.customArrow(arrowEntity);
    }
}
