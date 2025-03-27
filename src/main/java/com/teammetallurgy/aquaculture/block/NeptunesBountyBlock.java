package com.teammetallurgy.aquaculture.block;

import com.mojang.serialization.MapCodec;
import com.teammetallurgy.aquaculture.block.blockentity.NeptunesBountyBlockEntity;
import com.teammetallurgy.aquaculture.init.AquaBlockEntities;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class NeptunesBountyBlock extends ChestBlock {
    public static final MapCodec<NeptunesBountyBlock> CODEC = simpleCodec((p) -> new NeptunesBountyBlock(AquaBlockEntities.NEPTUNES_BOUNTY::get, p));

    public NeptunesBountyBlock(Supplier<BlockEntityType<? extends ChestBlockEntity>> block, BlockBehaviour.Properties properties) {
        super(block, properties.mapColor(MapColor.METAL).strength(3.5F, 8.0F).sound(SoundType.METAL));
    }

    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new NeptunesBountyBlockEntity(pos, state);
    }

    @Override
    @Nonnull
    public MapCodec<? extends NeptunesBountyBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        return this.defaultBlockState().setValue(TYPE, ChestType.SINGLE).setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    @Override
    @Nonnull
    public BlockState updateShape(BlockState state, @Nonnull LevelReader level, @Nonnull ScheduledTickAccess scheduledTickAccess, @Nonnull BlockPos pos, @Nonnull Direction direction, @Nonnull BlockPos neighborPos, @Nonnull BlockState neighborState, @Nonnull RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            scheduledTickAccess.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return state;
    }

    /*@Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@Nonnull ItemStack stack, @Nonnull Item.TooltipContext context, TooltipDisplay display, @Nonnull Consumer<Component> tooltip, @Nonnull TooltipFlag flag) {
        if (stack.has(DataComponents.CONTAINER_LOOT)) {
            tooltip.accept(Component.literal("???????").withStyle(ChatFormatting.ITALIC)); //TODO
        }
    }*/
}