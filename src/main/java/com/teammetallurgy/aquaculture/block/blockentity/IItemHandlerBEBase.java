package com.teammetallurgy.aquaculture.block.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.world.Nameable;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.Optional;

public abstract class IItemHandlerBEBase extends BlockEntity implements Nameable {
    private Component customName;
    public final IItemHandler handler = createItemHandler();

    public IItemHandlerBEBase(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(blockEntityType, pos, state);
    }

    @Nonnull
    protected abstract IItemHandler createItemHandler();

    @Override
    protected void loadAdditional(@Nonnull CompoundTag tag, @Nonnull HolderLookup.Provider provider) {
        Optional<CompoundTag> invTag = tag.getCompound("inv");
        invTag.ifPresent(compoundTag -> ((INBTSerializable<CompoundTag>) handler).deserializeNBT(provider, compoundTag));

        this.customName = parseCustomNameSafe(tag.get("CustomName"), provider);

        super.loadAdditional(tag, provider);
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag tag, @Nonnull HolderLookup.Provider provider) {
        CompoundTag compound = ((INBTSerializable<CompoundTag>) handler).serializeNBT(provider);
        if (compound != null) {
            tag.put("inv", compound);
        }

        if (this.hasCustomName()) {
            tag.store("CustomName", ComponentSerialization.CODEC, provider.createSerializationContext(NbtOps.INSTANCE), this.customName);
        }

        super.saveAdditional(tag, provider);
    }

    @Override
    @Nonnull
    public Component getName() {
        return this.customName != null ? this.customName : Component.translatable(this.getBlockState().getBlock().getDescriptionId());
    }

    @Override
    @Nonnull
    public Component getDisplayName() {
        return getName();
    }

    public void setCustomName(Component name) {
        this.customName = name;
    }
}