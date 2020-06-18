package com.teammetallurgy.aquaculture.block;

import com.teammetallurgy.aquaculture.api.AquacultureAPI;
import com.teammetallurgy.aquaculture.init.AquaItems;
import com.teammetallurgy.aquaculture.loot.FishWeightHandler;
import com.teammetallurgy.aquaculture.misc.AquaConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ComposterBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class WormFarmBlock extends ComposterBlock {

    public WormFarmBlock() {
        super(Block.Properties.create(Material.WOOD).hardnessAndResistance(0.6F).sound(SoundType.WOOD));
    }

    public static void addCompostables() {
        registerCompostable(AquaItems.ALGAE.asItem(), 0.3F);
        if (AquaConfig.BASIC_OPTIONS.compostableFish.get()) {
            for (Item fish : AquacultureAPI.FISH_DATA.getFish()) {
                double weight = AquacultureAPI.FISH_DATA.getMinWeight(fish);
                ItemStack fishStack = new ItemStack(fish);
                if (fishStack.getTag() != null && fishStack.getTag().contains("fishWeight")) {
                    weight = fishStack.getTag().getDouble("fishWeight");
                }
                float chance = MathHelper.clamp((FishWeightHandler.getFilletAmountFromWeight(weight) * 0.25F), 0.05F, 0.65F);
                registerCompostable(fish, chance);
            }
        }
    }

    public static void registerCompostable(Item item, float chance) {
        if (!CHANCES.containsKey(item) && CHANCES.size() < 256) {
            CHANCES.put(item, chance);
        }
    }

    @Override
    @Nonnull
    public ActionResultType onBlockActivated(BlockState state, @Nonnull World world, @Nonnull BlockPos pos, PlayerEntity player, @Nonnull Hand hand, BlockRayTraceResult raytrace) {
        int level = state.get(LEVEL);
        ItemStack heldStack = player.getHeldItem(hand);

        if (CHANCES.containsKey(heldStack.getItem())) {
            if (level < 8 && !world.isRemote) {
                boolean addItem = WormFarmBlock.addItem(state, world, pos, heldStack);
                world.playEvent(1500, pos, addItem ? 1 : 0);
                if (!player.abilities.isCreativeMode) {
                    heldStack.shrink(1);
                }
            }
            return ActionResultType.SUCCESS;
        } else if (level > 0) {
            if (!world.isRemote) {
                double x = (double) (world.rand.nextFloat() * 0.7F) + 0.15000000596046448D;
                double y = (double) (world.rand.nextFloat() * 0.7F) + 0.06000000238418579D + 0.6D;
                double z = (double) (world.rand.nextFloat() * 0.7F) + 0.15000000596046448D;
                ItemEntity itemEntity = new ItemEntity(world, (double) pos.getX() + x, (double) pos.getY() + y, (double) pos.getZ() + z, new ItemStack(AquaItems.WORM));
                itemEntity.setDefaultPickupDelay();
                world.addEntity(itemEntity);
            }
            world.setBlockState(pos, state.with(LEVEL, state.get(LEVEL) - 1), 3);
            world.playSound(null, pos, SoundEvents.BLOCK_COMPOSTER_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
            return ActionResultType.SUCCESS;
        } else {
            return ActionResultType.FAIL;
        }
    }

    @Override
    public void tick(BlockState state, @Nonnull ServerWorld world, @Nonnull BlockPos pos, Random random) {
    }
    
    private static boolean addItem(BlockState state, IWorld world, BlockPos pos, @Nonnull ItemStack stack) {
        int level = state.get(LEVEL);
        float chance = CHANCES.getFloat(stack.getItem());
        if ((level != 0 || chance <= 0.0F) && world.getRandom().nextDouble() >= (double) chance) {
            return false;
        } else {
            int levelAdd = level + 1;
            world.setBlockState(pos, state.with(LEVEL, levelAdd), 3);
            if (levelAdd == 7) {
                world.getPendingBlockTicks().scheduleTick(pos, state.getBlock(), 20);
            }
            return true;
        }
    }

    @Override
    @Nonnull
    public ISidedInventory createInventory(BlockState state, @Nonnull IWorld world, @Nonnull BlockPos pos) {
        int level = state.get(LEVEL);
        if (level == 8) {
            return new FullInventory(state, world, pos, new ItemStack(AquaItems.WORM));
        } else {
            return level < 7 ? new PartialInventory(state, world, pos) : new EmptyInventory();
        }
    }

    static class PartialInventory extends Inventory implements ISidedInventory {
        private final BlockState state;
        private final IWorld world;
        private final BlockPos pos;
        private boolean inserted;

        PartialInventory(BlockState state, IWorld world, BlockPos pos) {
            super(1);
            this.state = state;
            this.world = world;
            this.pos = pos;
        }

        @Override
        public int getInventoryStackLimit() {
            return 1;
        }

        @Override
        @Nonnull
        public int[] getSlotsForFace(@Nonnull Direction direction) {
            return direction == Direction.UP ? new int[]{0} : new int[0];
        }

        @Override
        public boolean canInsertItem(int index, @Nonnull ItemStack stack, @Nullable Direction direction) {
            return !this.inserted && direction == Direction.UP && ComposterBlock.CHANCES.containsKey(stack.getItem());
        }

        @Override
        public boolean canExtractItem(int index, @Nonnull ItemStack stack, @Nonnull Direction direction) {
            return false;
        }

        @Override
        public void markDirty() {
            ItemStack stack = this.getStackInSlot(0);
            if (!stack.isEmpty()) {
                this.inserted = true;
                WormFarmBlock.addItem(this.state, this.world, this.pos, stack);
                this.removeStackFromSlot(0);
            }

        }
    }

    static class FullInventory extends Inventory implements ISidedInventory {
        private final BlockState state;
        private final IWorld world;
        private final BlockPos pos;
        private boolean extracted;

        FullInventory(BlockState state, IWorld world, BlockPos pos, @Nonnull ItemStack stack) {
            super(stack);
            this.state = state;
            this.world = world;
            this.pos = pos;
        }

        @Override
        public int getInventoryStackLimit() {
            return 1;
        }

        @Override
        @Nonnull
        public int[] getSlotsForFace(@Nonnull Direction direction) {
            return direction == Direction.DOWN ? new int[]{0} : new int[0];
        }

        @Override
        public boolean canInsertItem(int index, @Nonnull ItemStack stack, @Nullable Direction direction) {
            return false;
        }

        @Override
        public boolean canExtractItem(int index, @Nonnull ItemStack stack, @Nonnull Direction direction) {
            return !this.extracted && direction == Direction.DOWN && stack.getItem() == AquaItems.WORM;
        }

        @Override
        public void markDirty() {
            this.world.setBlockState(this.pos, this.state.with(LEVEL, 0), 3);
            this.extracted = true;
        }
    }

    static class EmptyInventory extends Inventory implements ISidedInventory {

        EmptyInventory() {
            super(0);
        }

        @Override
        @Nonnull
        public int[] getSlotsForFace(@Nonnull Direction direction) {
            return new int[0];
        }

        @Override
        public boolean canInsertItem(int index, @Nonnull ItemStack stack, @Nullable Direction direction) {
            return false;
        }

        @Override
        public boolean canExtractItem(int index, @Nonnull ItemStack stack, @Nonnull Direction direction) {
            return false;
        }
    }
}