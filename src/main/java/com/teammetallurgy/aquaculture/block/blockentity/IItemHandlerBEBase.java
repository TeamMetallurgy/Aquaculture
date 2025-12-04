package com.teammetallurgy.aquaculture.block.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.world.Nameable;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import net.neoforged.neoforge.items.IItemHandler;

import javax.annotation.Nonnull;

public abstract class IItemHandlerBEBase extends BlockEntity implements Nameable {
    private Component customName;
    public final IItemHandler handler = createItemHandler();

    public IItemHandlerBEBase(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(blockEntityType, pos, state);
    }

    @Nonnull
    protected abstract IItemHandler createItemHandler();

    @Override
    protected void loadAdditional(@Nonnull ValueInput input) {
        super.loadAdditional(input);
        input.child("inv").ifPresent(child -> ((ValueIOSerializable) handler).deserialize(child));
        this.customName = parseCustomNameSafe(input, "CustomName");
    }

    @Override
    protected void saveAdditional(@Nonnull ValueOutput output) {
        super.saveAdditional(output);
        output.putChild("inv", ((ValueIOSerializable) handler));
        output.storeNullable("CustomName", ComponentSerialization.CODEC, this.customName);
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