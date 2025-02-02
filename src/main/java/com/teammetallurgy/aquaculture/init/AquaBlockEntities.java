package com.teammetallurgy.aquaculture.init;

import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.block.blockentity.NeptunesBountyBlockEntity;
import com.teammetallurgy.aquaculture.block.blockentity.TackleBoxBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class AquaBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_DEFERRED = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Aquaculture.MOD_ID);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<NeptunesBountyBlockEntity>> NEPTUNES_BOUNTY = register("neptunes_bounty", () -> new BlockEntityType<>(NeptunesBountyBlockEntity::new, AquaBlocks.NEPTUNES_BOUNTY.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TackleBoxBlockEntity>> TACKLE_BOX = register("tackle_box", () -> new BlockEntityType<>(TackleBoxBlockEntity::new, AquaBlocks.TACKLE_BOX.get()));

    public static <T extends BlockEntity> DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> register(@Nonnull String name, @Nonnull Supplier<BlockEntityType<T>> initializer) {
        return BLOCK_ENTITY_DEFERRED.register(name, initializer);
    }
}