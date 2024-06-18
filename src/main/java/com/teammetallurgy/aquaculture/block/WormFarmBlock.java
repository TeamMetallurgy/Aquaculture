package com.teammetallurgy.aquaculture.block;

import com.mojang.serialization.MapCodec;
import com.teammetallurgy.aquaculture.init.AquaItems;
import com.teammetallurgy.aquaculture.init.AquaSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class WormFarmBlock extends ComposterBlock {
    public static final MapCodec<ComposterBlock> CODEC = simpleCodec(p -> new WormFarmBlock());

    public WormFarmBlock() {
        super(Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(0.6F).sound(SoundType.WOOD));
    }

    @Override
    @Nonnull
    public MapCodec<ComposterBlock> codec() {
        return CODEC;
    }

    @Override
    @Nonnull
    public ItemInteractionResult useItemOn(@Nonnull ItemStack stack, BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult blockHitResult) {
        int stateLevel = state.getValue(LEVEL);
        ItemStack heldStack = player.getItemInHand(hand);

        if (heldStack.getItemHolder().getData(NeoForgeDataMaps.COMPOSTABLES) != null) {
            if (stateLevel < 8 && !level.isClientSide) {
                boolean addItem = WormFarmBlock.addItem(state, level, pos, heldStack);
                level.levelEvent(1500, pos, addItem ? 1 : 0);
                if (!player.getAbilities().instabuild) {
                    heldStack.shrink(1);
                }
            }
            return ItemInteractionResult.SUCCESS;
        } else if (stateLevel > 0) {
            if (!level.isClientSide) {
                double x = (double) (level.random.nextFloat() * 0.7F) + 0.15000000596046448D;
                double y = (double) (level.random.nextFloat() * 0.7F) + 0.06000000238418579D + 0.6D;
                double z = (double) (level.random.nextFloat() * 0.7F) + 0.15000000596046448D;
                ItemEntity itemEntity = new ItemEntity(level, (double) pos.getX() + x, (double) pos.getY() + y, (double) pos.getZ() + z, new ItemStack(AquaItems.WORM.get()));
                itemEntity.setDefaultPickUpDelay();
                level.addFreshEntity(itemEntity);
            }
            level.setBlock(pos, state.setValue(LEVEL, state.getValue(LEVEL) - 1), 3);
            level.playSound(null, pos, AquaSounds.WORM_FARM_EMPTY.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
            return ItemInteractionResult.SUCCESS;
        } else {
            return ItemInteractionResult.FAIL;
        }
    }

    @Override
    public void tick(@Nonnull BlockState state, @Nonnull ServerLevel level, @Nonnull BlockPos pos, @Nonnull RandomSource random) {
    }
    
    private static boolean addItem(BlockState state, LevelAccessor level, BlockPos pos, @Nonnull ItemStack stack) {
        int stateLevel = state.getValue(LEVEL);
        float chance = ComposterBlock.getValue(stack);
        if ((stateLevel != 0 || chance <= 0.0F) && level.getRandom().nextDouble() >= (double) chance) {
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
            return !this.inserted && direction == Direction.UP && ComposterBlock.getValue(stack) > 0;
        }

        @Override
        public boolean canTakeItemThroughFace(int index, @Nonnull ItemStack stack, @Nonnull Direction direction) {
            return false;
        }

        @Override
        public void setChanged() {
            ItemStack stack = this.getItem(0);
            if (!stack.isEmpty()) {
                this.inserted = true;
                WormFarmBlock.addItem(this.state, this.levelAccessor, this.pos, stack);
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
