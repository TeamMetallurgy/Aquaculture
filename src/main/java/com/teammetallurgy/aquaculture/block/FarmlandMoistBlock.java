package com.teammetallurgy.aquaculture.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.FarmlandBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import javax.annotation.Nonnull;

public class FarmlandMoistBlock extends FarmlandBlock {
    public static final MapCodec<FarmlandBlock> CODEC = simpleCodec(FarmlandMoistBlock::new);

    public FarmlandMoistBlock(BlockBehaviour.Properties properties) {
        super(properties.mapColor(MapColor.DIRT).strength(0.6F).sound(SoundType.GRAVEL));
        this.registerDefaultState(this.stateDefinition.any().setValue(MOISTURE, 7));
    }

    @Override
    @Nonnull
    public MapCodec<FarmlandBlock> codec() {
        return CODEC;
    }
}