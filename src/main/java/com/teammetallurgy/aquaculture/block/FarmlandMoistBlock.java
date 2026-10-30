package com.teammetallurgy.aquaculture.block;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmlandBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class FarmlandMoistBlock extends FarmlandBlock {

    public FarmlandMoistBlock(BlockBehaviour.Properties properties) {
        super(Blocks.DIRT, properties.mapColor(MapColor.DIRT).strength(0.6F).sound(SoundType.GRAVEL));
        this.registerDefaultState(this.stateDefinition.any().setValue(MOISTURE, 7));
    }
}