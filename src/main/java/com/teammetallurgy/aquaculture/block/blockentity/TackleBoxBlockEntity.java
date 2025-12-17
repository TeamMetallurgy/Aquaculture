package com.teammetallurgy.aquaculture.block.blockentity;

import com.teammetallurgy.aquaculture.api.AquacultureAPI;
import com.teammetallurgy.aquaculture.api.bait.IBaitItem;
import com.teammetallurgy.aquaculture.init.AquaBlockEntities;
import com.teammetallurgy.aquaculture.init.AquaSounds;
import com.teammetallurgy.aquaculture.inventory.container.TackleBoxContainer;
import com.teammetallurgy.aquaculture.item.AquaFishingRodItem;
import com.teammetallurgy.aquaculture.item.HookItem;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.ContainerUser;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.ChestLidController;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TackleBoxBlockEntity extends IItemHandlerBEBase implements MenuProvider, LidBlockEntity {
    private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
        @Override
        protected void onOpen(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState state) {
            TackleBoxBlockEntity.playSound(level, pos, state, AquaSounds.TACKLE_BOX_OPEN.get());
        }

        @Override
        protected void onClose(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState state) {
            TackleBoxBlockEntity.playSound(level, pos, state, AquaSounds.TACKLE_BOX_CLOSE.get());
        }

        @Override
        protected void openerCountChanged(@Nonnull Level level, @Nonnull BlockPos pos, @Nonnull BlockState state, int i, int i1) {
            TackleBoxBlockEntity.this.signalOpenCount(level, pos, state, i, i1);
        }

        @Override
        public boolean isOwnContainer(Player player) {
            return player.containerMenu instanceof TackleBoxContainer;
        }
    };
    private final ChestLidController lidController = new ChestLidController();

    public TackleBoxBlockEntity(BlockPos pos, BlockState state) {
        super(AquaBlockEntities.TACKLE_BOX.get(), pos, state);
    }

    @Override
    @Nonnull
    protected ItemStacksResourceHandler createItemHandler() {
        return new ItemStacksResourceHandler(17) {
            @Override
            protected void onContentsChanged(int index, @Nonnull ItemStack previousContents) {
                super.onContentsChanged(index, previousContents);
                TackleBoxBlockEntity.this.setChanged();
            }

            @Override
            public boolean isValid(int slot, @Nonnull ItemResource resource) {
                ItemStack stack = resource.toStack();
                if (slot == 0) {
                 return stack.getItem() instanceof AquaFishingRodItem;
                } else {
                    return canBePutInTackleBox(stack);
                }
            }
        };
    }

    public static boolean canBePutInTackleBox(@Nonnull ItemStack stack) {
        Item item = stack.getItem();

        return stack.is(AquacultureAPI.Tags.TACKLE_BOX) || item instanceof HookItem || item instanceof IBaitItem ||
                stack.is(AquacultureAPI.Tags.FISHING_LINE) || stack.is(AquacultureAPI.Tags.BOBBER);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int windowID, @Nonnull Inventory playerInventory, @Nonnull Player player) {
        return new TackleBoxContainer(windowID, this.worldPosition, playerInventory);
    }

    public static void lidAnimateTick(Level level, BlockPos pos, BlockState state, TackleBoxBlockEntity tackleBox) {
        tackleBox.lidController.tickLid();
    }

    protected void signalOpenCount(Level level, BlockPos pos, BlockState state, int i, int i1) {
        Block block = state.getBlock();
        level.blockEvent(pos, block, 1, i1);
    }

    static void playSound(Level level, BlockPos pos, BlockState state, SoundEvent sound) {
        if (level != null) {
            level.playSound(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, sound, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
        }
    }

    @Override
    public boolean triggerEvent(int p_59114_, int p_59115_) {
        if (p_59114_ == 1) {
            this.lidController.shouldBeOpen(p_59115_ > 0);
            return true;
        } else {
            return super.triggerEvent(p_59114_, p_59115_);
        }
    }

    public void startOpen(ContainerUser user) {
        if (!this.remove && !user.getLivingEntity().isSpectator()) {
            this.openersCounter.incrementOpeners(user.getLivingEntity(), this.getLevel(), this.getBlockPos(), this.getBlockState(), user.getContainerInteractionRange());
        }
    }

    public void stopOpen(ContainerUser user) {
        if (!this.remove && !user.getLivingEntity().isSpectator()) {
            this.openersCounter.decrementOpeners(user.getLivingEntity(), this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }

    public float getOpenNess(float partialTicks) {
        return this.lidController.getOpenness(partialTicks);
    }

    public void recheckOpen() {
        if (!this.remove) {
            this.openersCounter.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
        }
    }
}