package com.teammetallurgy.aquaculture.block.tileentity;

import com.teammetallurgy.aquaculture.block.TackleBoxBlock;
import com.teammetallurgy.aquaculture.init.AquaBlocks;
import com.teammetallurgy.aquaculture.inventory.container.TackleBoxContainer;
import com.teammetallurgy.aquaculture.misc.AquacultureSounds;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TackleBoxTileEntity extends IItemHandlerTEBase implements INamedContainerProvider, ITickableTileEntity {
    protected float lidAngle;
    protected float prevLidAngle;
    protected int numPlayersUsing;
    private int ticksSinceSync;

    public TackleBoxTileEntity() {
        super(AquaBlocks.AquaTileEntities.TACKLE_BOX);
    }

    @Override
    @Nonnull
    protected IItemHandler createItemHandler() {
        return new ItemStackHandler(17);
    }

    @Nullable
    @Override
    public Container createMenu(int windowID, @Nonnull PlayerInventory playerInventory, @Nonnull PlayerEntity player) {
        return new TackleBoxContainer(windowID, pos, playerInventory);
    }

    @Override
    public void tick() {
        int x = this.pos.getX();
        int y = this.pos.getY();
        int z = this.pos.getZ();
        ++this.ticksSinceSync;
        this.numPlayersUsing = this.getNumbersOfPlayersUsing(this.world, this.ticksSinceSync, x, y, z, this.numPlayersUsing);
        this.prevLidAngle = this.lidAngle;
        if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F) {
            this.playSound(AquacultureSounds.TACKLE_BOX_OPEN);
        }

        if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F) {
            float angle = this.lidAngle;
            if (this.numPlayersUsing > 0) {
                this.lidAngle += 0.1F;
            } else {
                this.lidAngle -= 0.1F;
            }

            if (this.lidAngle > 1.0F) {
                this.lidAngle = 1.0F;
            }

            if (this.lidAngle < 0.5F && angle >= 0.5F) {
                this.playSound(AquacultureSounds.TACKLE_BOX_CLOSE);
            }

            if (this.lidAngle < 0.0F) {
                this.lidAngle = 0.0F;
            }
        }
    }

    private int getNumbersOfPlayersUsing(World world, int ticksSinceSync, int x, int y, int z, int numPlayersUsing) {
        if (!world.isRemote && numPlayersUsing != 0 && (ticksSinceSync + x + y + z) % 200 == 0) {
            numPlayersUsing = getPlayers(world, x, y, z);
        }
        return numPlayersUsing;
    }

    private int getPlayers(World world, int x, int y, int z) {
        int players = 0;
        for (PlayerEntity playerentity : world.getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB((float) x - 5.0F, (float) y - 5.0F, (float) z - 5.0F, (float) (x + 1) + 5.0F, (float) (y + 1) + 5.0F, (float) (z + 1) + 5.0F))) {
            if (playerentity.openContainer instanceof TackleBoxContainer) {
                ++players;
            }
        }
        return players;
    }

    @Override
    public boolean receiveClientEvent(int id, int type) {
        if (id == 1) {
            this.numPlayersUsing = type;
            return true;
        } else {
            return super.receiveClientEvent(id, type);
        }
    }

    private void playSound(SoundEvent sound) {
        if (world != null) {
            this.world.playSound(null, this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D, sound, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
        }
    }

    public float getLidAngle(float partialTicks) {
        return MathHelper.lerp(partialTicks, this.prevLidAngle, this.lidAngle);
    }

    public void openInventory(PlayerEntity player) {
        if (!this.world.isRemote) {
            if (!player.isSpectator()) {
                if (this.numPlayersUsing < 0) {
                    this.numPlayersUsing = 0;
                }

                ++this.numPlayersUsing;
                this.onOpenOrClose();
            }
        }
    }

    public void closeInventory(PlayerEntity player) {
        if (!this.world.isRemote) {
            if (!player.isSpectator()) {
                --this.numPlayersUsing;
                this.onOpenOrClose();
            }
        }
    }

    private void onOpenOrClose() {
        Block block = getBlockState().getBlock();
        if (block instanceof TackleBoxBlock) {
            world.addBlockEvent(pos, block, 1, numPlayersUsing);
            world.notifyNeighborsOfStateChange(pos, block);
        }
    }
}
