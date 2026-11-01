package com.teammetallurgy.aquaculture.block;

import com.teammetallurgy.aquaculture.init.AquaItems;
import com.teammetallurgy.aquaculture.init.AquaSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Compostable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class WormFarmBlock extends ComposterBlock {

    public WormFarmBlock(Block.Properties properties) {
        super(properties.mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(0.6F).sound(SoundType.WOOD));
    }


    @Override
    @Nonnull
    public InteractionResult useItemOn(@Nonnull ItemStack stack, BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult blockHitResult) {
        int fillLevel = state.getValue(LEVEL);

        if (stack.has(DataComponents.COMPOSTABLE)) {
            if (fillLevel < 8 && level instanceof ServerLevel serverLevel) {
                boolean addItem = WormFarmBlock.addItem(state, serverLevel, pos, stack);
                level.levelEvent(1500, pos, addItem ? 1 : 0);
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            }
            return InteractionResult .SUCCESS;
        } else if (fillLevel > 0) {
            if (!level.isClientSide()) {
                double x = (double) (level.getRandom().nextFloat() * 0.7F) + 0.15000000596046448D;
                double y = (double) (level.getRandom().nextFloat() * 0.7F) + 0.06000000238418579D + 0.6D;
                double z = (double) (level.getRandom().nextFloat() * 0.7F) + 0.15000000596046448D;
                ItemEntity itemEntity = new ItemEntity(level, (double) pos.getX() + x, (double) pos.getY() + y, (double) pos.getZ() + z, new ItemStack(AquaItems.WORM.get()));
                itemEntity.setDefaultPickUpDelay();
                level.addFreshEntity(itemEntity);
            }
            level.setBlock(pos, state.setValue(LEVEL, state.getValue(LEVEL) - 1), 3);
            level.playSound(null, pos, AquaSounds.WORM_FARM_EMPTY.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
            return InteractionResult .SUCCESS;
        } else {
            return InteractionResult .FAIL;
        }
    }

    @Override
    public void tick(@Nonnull BlockState state, @Nonnull ServerLevel level, @Nonnull BlockPos pos, @Nonnull RandomSource random) {
    }
    
    private static boolean addItem(BlockState state, ServerLevel level, BlockPos pos, @Nonnull ItemStack stack) {
        Compostable compostable = stack.get(DataComponents.COMPOSTABLE);
        if (compostable == null) {
            return false;
        }
        int stateLevel = state.getValue(LEVEL);
        LootContext lootContext = new LootContext.Builder(
                new LootParams.Builder(level)
                        .withParameter(LootContextParams.BLOCK_STATE, state)
                        .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                        .withOptionalParameter(LootContextParams.INTERACTING_ENTITY, null)
                        .create(LootContextParamSets.BLOCK_INTERACT)
        ).create(Optional.empty());
        if (stateLevel != 0 && compostable.layers().get(lootContext, 0) <= 0) {
            return false;
        } else {
            int levelAdd = stateLevel + 1;
            level.setBlock(pos, state.setValue(LEVEL, levelAdd), 3);
            if (levelAdd == 7) {
                level.scheduleTick(pos, state.getBlock(), 20);
            }
            return true;
        }
    }

    @Override
    @Nonnull
    public WorldlyContainer getContainer(BlockState state, @Nonnull LevelAccessor levelAccessor, @Nonnull BlockPos pos) {
        int level = state.getValue(LEVEL);
        if (level == 8) {
            return new FullInventory(state, levelAccessor, pos, new ItemStack(AquaItems.WORM.get()));
        } else {
            return level < 7 ? new PartialInventory(state, levelAccessor, pos) : new EmptyInventory();
        }
    }

    static class PartialInventory extends SimpleContainer implements WorldlyContainer {
        private final BlockState state;
        private final LevelAccessor levelAccessor;
        private final BlockPos pos;
        private boolean inserted;

        PartialInventory(BlockState state, LevelAccessor levelAccessor, BlockPos pos) {
            super(1);
            this.state = state;
            this.levelAccessor = levelAccessor;
            this.pos = pos;
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }

        @Override
        @Nonnull
        public int[] getSlotsForFace(@Nonnull Direction direction) {
            return direction == Direction.UP ? new int[]{0} : new int[0];
        }

        @Override
        public boolean canPlaceItemThroughFace(int index, @Nonnull ItemStack stack, @Nullable Direction direction) {
            return !this.inserted && direction == Direction.UP && stack.has(DataComponents.COMPOSTABLE);
        }

        @Override
        public boolean canTakeItemThroughFace(int index, @Nonnull ItemStack stack, @Nonnull Direction direction) {
            return false;
        }

        @Override
        public void setChanged() {
            ItemStack stack = this.getItem(0);
            if (!stack.isEmpty() && this.levelAccessor instanceof ServerLevel serverLevel) {
                this.inserted = true;
                WormFarmBlock.addItem(this.state, serverLevel, this.pos, stack);
                this.removeItemNoUpdate(0);
            }

        }
    }

    static class FullInventory extends SimpleContainer implements WorldlyContainer {
        private final BlockState state;
        private final LevelAccessor world;
        private final BlockPos pos;
        private boolean extracted;

        FullInventory(BlockState state, LevelAccessor world, BlockPos pos, @Nonnull ItemStack stack) {
            super(stack);
            this.state = state;
            this.world = world;
            this.pos = pos;
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }

        @Override
        @Nonnull
        public int[] getSlotsForFace(@Nonnull Direction direction) {
            return direction == Direction.DOWN ? new int[]{0} : new int[0];
        }

        @Override
        public boolean canPlaceItemThroughFace(int index, @Nonnull ItemStack stack, @Nullable Direction direction) {
            return false;
        }

        @Override
        public boolean canTakeItemThroughFace(int index, @Nonnull ItemStack stack, @Nonnull Direction direction) {
            return !this.extracted && direction == Direction.DOWN && stack.getItem() == AquaItems.WORM.get();
        }

        @Override
        public void setChanged() {
            this.world.setBlock(this.pos, this.state.setValue(LEVEL, 0), 3);
            this.extracted = true;
        }
    }

    static class EmptyInventory extends SimpleContainer implements WorldlyContainer {

        EmptyInventory() {
            super(0);
        }

        @Override
        @Nonnull
        public int[] getSlotsForFace(@Nonnull Direction direction) {
            return new int[0];
        }

        @Override
        public boolean canPlaceItemThroughFace(int index, @Nonnull ItemStack stack, @Nullable Direction direction) {
            return false;
        }

        @Override
        public boolean canTakeItemThroughFace(int index, @Nonnull ItemStack stack, @Nonnull Direction direction) {
            return false;
        }
    }
}
