package com.teammetallurgy.aquaculture.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

import javax.annotation.Nonnull;

public class FarmlandMoistBlock extends FarmlandBlock {

    public FarmlandMoistBlock() {
        super(Block.Properties.create(Material.EARTH).hardnessAndResistance(0.6F).sound(SoundType.GROUND));
        this.setDefaultState(this.stateContainer.getBaseState().with(MOISTURE, 7));
    }

    @Override
    public boolean canSustainPlant(@Nonnull BlockState state, @Nonnull IBlockReader world, BlockPos pos, @Nonnull Direction facing, IPlantable plantable) {
        PlantType type = plantable.getPlantType(world, pos.offset(facing));

        return type == PlantType.Crop || type == PlantType.Plains;
    }
}