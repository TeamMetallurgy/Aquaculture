package com.teammetallurgy.aquaculture.init;

import com.mojang.serialization.Codec;
import com.teammetallurgy.aquaculture.Aquaculture;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nonnull;
import java.util.function.UnaryOperator;

public class AquaDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPE_DEFERRED = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, Aquaculture.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Float>> FISH_WEIGHT = register("fish_weight", data -> data.persistent(ExtraCodecs.POSITIVE_FLOAT).networkSynchronized(ByteBufCodecs.FLOAT));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> FISH_SIZE = register("fish_size", data -> data.persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> IN_WATER = register("in_water", data -> data.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));

    public static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(@Nonnull String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        return DATA_COMPONENT_TYPE_DEFERRED.register(name, () -> builder.apply(DataComponentType.builder()).build());
    }
}