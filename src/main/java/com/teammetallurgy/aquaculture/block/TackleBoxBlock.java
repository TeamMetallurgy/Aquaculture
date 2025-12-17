package com.teammetallurgy.aquaculture.block;

import com.mojang.serialization.MapCodec;
import com.teammetallurgy.aquaculture.block.blockentity.TackleBoxBlockEntity;
import com.teammetallurgy.aquaculture.init.AquaBlockEntities;
import com.teammetallurgy.aquaculture.misc.StackHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.item.ItemAccessItemHandler;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TackleBoxBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
    public static final MapCodec<TackleBoxBlock> CODEC = simpleCodec(TackleBoxBlock::new);
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final VoxelShape NORTH_SOUTH = Block.box(0.8D, 0.0D, 3.9D, 15.2D, 9.0D, 12.2D);
    private static final VoxelShape EAST_WEST = Block.box(3.9D, 0.0D, 0.8D, 12.2D, 9.0D, 15.2D);

    public TackleBoxBlock(BlockBehaviour.Properties properties) {
        super(properties.mapColor(MapColor.METAL).strength(4.0F, 5.0F).sound(SoundType.METAL));
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
    }

    @Override
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new TackleBoxBlockEntity(pos, state);
    }

    @Override
    @Nonnull
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    @Nonnull
    public RenderShape getRenderShape(@Nonnull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    @Nonnull
    public VoxelShape getShape(BlockState state, @Nonnull BlockGetter blockReader, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        switch (state.getValue(FACING)) {
            case NORTH:
            case SOUTH:
                return NORTH_SOUTH;
            case EAST:
            case WEST:
                return EAST_WEST;
        }
        return super.getShape(state, blockReader, pos, context);
    }

    @Override
    @Nonnull
    public InteractionResult useWithoutItem(@Nonnull BlockState state, Level level, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull BlockHitResult blockHitResult) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        } else {
            MenuProvider container = this.getMenuProvider(state, level, pos);
            if (container != null && player instanceof ServerPlayer serverPlayer) {
                if (player.isShiftKeyDown()) {
                    BlockEntity blockEntity = level.getBlockEntity(pos);
                    if (blockEntity != null) {
                        ItemStack giveStack = new ItemStack(this);
                        StackHelper.saveToItem(giveStack, player.level().registryAccess(), blockEntity);
                        StackHelper.giveItem(serverPlayer, giveStack);
                        level.removeBlock(pos, false);
                        level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.6F, 0.8F);
                    }
                } else {
                    serverPlayer.openMenu(container, pos);
                }
            }
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction placerFacing = context.getHorizontalDirection().getOpposite();
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        return this.defaultBlockState().setValue(FACING, placerFacing).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    @Override
    public void setPlacedBy(@Nonnull Level world, @Nonnull BlockPos pos, @Nonnull BlockState state, LivingEntity placer, @Nonnull ItemStack stack) {
        if (stack.has(DataComponents.CUSTOM_NAME)) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof TackleBoxBlockEntity) {
                ((TackleBoxBlockEntity) blockEntity).setCustomName(stack.getHoverName());
            }
        }
    }

    @Override
    @Nonnull
    public BlockState updateShape(BlockState state, @Nonnull LevelReader level, @Nonnull ScheduledTickAccess scheduledTickAccess, @Nonnull BlockPos pos, @Nonnull Direction direction, @Nonnull BlockPos neighborPos, @Nonnull BlockState neighborState, @Nonnull RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            scheduledTickAccess.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, level, scheduledTickAccess, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    @Nonnull
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public boolean hasAnalogOutputSignal(@Nonnull BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(@Nonnull BlockState state, Level level, @Nonnull BlockPos pos, Direction direction) {
        ItemAccessItemHandler handler = (ItemAccessItemHandler) level.getCapability(Capabilities.Item.BLOCK, pos, null);
        if (handler != null) {
            return ResourceHandlerUtil.getRedstoneSignalFromResourceHandler(handler);
        }
        return 0;
    }

    @Override
    @Nonnull
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    @Nonnull
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    @Override
    protected boolean isPathfindable(@Nonnull BlockState state, @Nonnull PathComputationType pathComputationType) {
        return false;
    }

    @Override
    protected void affectNeighborsAfterRemoval(@Nonnull BlockState state, @Nonnull ServerLevel serverLevel, @Nonnull BlockPos pos, boolean isMoving) {
        serverLevel.updateNeighbourForOutputSignal(pos, this);
    }

    @Override
    public void playerDestroy(@Nonnull Level level, Player player, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nullable BlockEntity blockEntity, @Nonnull ItemStack stack) {
        player.awardStat(Stats.BLOCK_MINED.get(this));
        player.causeFoodExhaustion(0.005F);
    }

    @Override
    public boolean onDestroyedByPlayer(@Nonnull BlockState state, Level level, @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull ItemStack toolStack, boolean willHarvest, @Nonnull FluidState fluid) {
        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity instanceof TackleBoxBlockEntity) {
            ItemStack tackleBox = new ItemStack(this);
            StackHelper.saveToItem(tackleBox, player.level().registryAccess(), blockEntity);
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), tackleBox);
        }
        return super.onDestroyedByPlayer(state, level, pos, player, toolStack, willHarvest, fluid);
    }

    @Override
    @Nonnull
    public ItemStack getCloneItemStack(@Nonnull LevelReader level, @Nonnull BlockPos pos, @Nonnull BlockState state, boolean includeData, @Nonnull Player player) {
        ItemStack cloneItemStack = super.getCloneItemStack(level, pos, state, includeData, player);
        level.getBlockEntity(pos, AquaBlockEntities.TACKLE_BOX.get()).ifPresent((blockEntity) -> {
            StackHelper.saveToItem(cloneItemStack, player.level().registryAccess(), blockEntity);
        });
        return cloneItemStack;
    }

    @Override
    public void onBlockExploded(@Nonnull BlockState state, ServerLevel level, @Nonnull BlockPos pos, @Nonnull Explosion explosion) {
        if (!level.isClientSide()) {
            ItemStacksResourceHandler handler = (ItemStacksResourceHandler) level.getCapability(Capabilities.Item.BLOCK, pos, null);
            if (handler != null) {
                StackHelper.dropInventory(level, pos, handler);
            }
        }
        super.onBlockExploded(state, level, pos, explosion);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @Nonnull BlockState state, @Nonnull BlockEntityType<T> blockEntityType) {
        return level.isClientSide() ? createTickerHelper(blockEntityType, AquaBlockEntities.TACKLE_BOX.get(), TackleBoxBlockEntity::lidAnimateTick) : null;
    }

    @Override
    public void tick(@Nonnull BlockState state, ServerLevel serverLevel, @Nonnull BlockPos pos, @Nonnull RandomSource rand) {
        BlockEntity blockEntity = serverLevel.getBlockEntity(pos);
        if (blockEntity instanceof TackleBoxBlockEntity) {
            ((TackleBoxBlockEntity) blockEntity).recheckOpen();
        }
    }
}