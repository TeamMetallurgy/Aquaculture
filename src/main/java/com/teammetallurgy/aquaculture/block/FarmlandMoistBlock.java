package com.teammetallurgy.aquaculture.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.common.SpecialPlantable;
import net.neoforged.neoforge.common.util.TriState;

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

    @Override
    @Nonnull
    public TriState canSustainPlant(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos soilPosition, @Nonnull Direction facing, @Nonnull BlockState plant) {
        return TriState.TRUE;
    }
}