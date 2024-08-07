package com.teammetallurgy.aquaculture.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;

import javax.annotation.Nonnull;

public class FarmlandMoistBlock extends FarmBlock {
    public static final MapCodec<FarmBlock> CODEC = simpleCodec(p -> new FarmlandMoistBlock());

    public FarmlandMoistBlock() {
        super(Block.Properties.of().mapColor(MapColor.DIRT).strength(0.6F).sound(SoundType.GRAVEL));
        this.registerDefaultState(this.stateDefinition.any().setValue(MOISTURE, 7));
    }

    @Override
    @Nonnull
    public MapCodec<FarmBlock> codec() {
        return CODEC;
    }
}