package com.teammetallurgy.aquaculture.entity;

import com.teammetallurgy.aquaculture.api.AquacultureAPI;
import com.teammetallurgy.aquaculture.misc.AquacultureSounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RedstoneDiodeBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.item.HangingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FishMountEntity extends HangingEntity implements IEntityAdditionalSpawnData {
    private static final Logger PRIVATE_LOGGER = LogManager.getLogger();
    private static final DataParameter<ItemStack> ITEM = EntityDataManager.createKey(FishMountEntity.class, DataSerializers.ITEMSTACK);
    private float itemDropChance = 1.0F;
    public Entity entity;

    public FishMountEntity(EntityType<? extends FishMountEntity> type, World world) {
        super(type, world);
    }

    public FishMountEntity(EntityType<? extends FishMountEntity> type, World world, BlockPos blockPos, Direction direction) {
        super(type, world, blockPos);
        this.updateFacingWithBoundingBox(direction);
    }

    public FishMountEntity(FMLPlayMessages.SpawnEntity spawnPacket, World world) {
        this((EntityType<? extends FishMountEntity>) ForgeRegistries.ENTITIES.getValue(spawnPacket.getAdditionalData().readResourceLocation()), world);
    }

    @Override
    protected float getEyeHeight(Pose pose, EntitySize size) {
        return 0.0F;
    }

    @Override
    protected void registerData() {
        this.getDataManager().register(ITEM, ItemStack.EMPTY);
    }

    @Override
    protected void updateFacingWithBoundingBox(Direction direction) {
        Validate.notNull(direction);
        this.facingDirection = direction;
        if (direction.getAxis().isHorizontal()) {
            this.rotationPitch = 0.0F;
            this.rotationYaw = (float) (this.facingDirection.getHorizontalIndex() * 90);
        } else {
            this.rotationPitch = (float) (-90 * direction.getAxisDirection().getOffset());
            this.rotationYaw = 0.0F;
        }
        this.prevRotationPitch = this.rotationPitch;
        this.prevRotationYaw = this.rotationYaw;
        this.updateBoundingBox();
    }

    @Override
    public boolean onValidSurface() {
        if (!this.world.hasNoCollisions(this)) {
            return false;
        } else {
            BlockState state = this.world.getBlockState(this.hangingPosition.offset(this.facingDirection.getOpposite()));
            return (state.getMaterial().isSolid() || this.facingDirection.getAxis().isHorizontal() && RedstoneDiodeBlock.isDiode(state)) && this.world.getEntitiesInAABBexcluding(this, this.getBoundingBox(), IS_HANGING_ENTITY).isEmpty();
        }
    }

    @Override
    protected void updateBoundingBox() {
        if (this.facingDirection != null) {
            double d0 = 0.46875D;
            double posX = (double) this.hangingPosition.getX() + 0.5D - (double) this.facingDirection.getXOffset() * d0;
            double posY = (double) this.hangingPosition.getY() + 0.5D - (double) this.facingDirection.getYOffset() * d0;
            double posZ = (double) this.hangingPosition.getZ() + 0.5D - (double) this.facingDirection.getZOffset() * d0;
            this.setRawPosition(posX, posY, posZ);
            double x1 = this.getWidthPixels() / 32.0D;
            double x2 = this.getWidthPixels() / 32.0D;
            double y1 = this.getHeightPixels() / 32.0D;
            double y2 = this.getHeightPixels() / 32.0D;
            double z1 = this.getWidthPixels() / 32.0D;
            double z2 = this.getWidthPixels() / 32.0D;
            switch (this.facingDirection.getAxis()) {
                case X:
                    x1 = (this.facingDirection.getXOffset() < 0 ? 3.0D : 1.0D) / 32.0D;
                    x2 = (this.facingDirection.getXOffset() > 0 ? 3.0D : 1.0D) / 32.0D;
                    break;
                case Y:
                    y1 = (this.facingDirection.getYOffset() < 0 ? 3.0D : 1.0D) / 32.0D;
                    y2 = (this.facingDirection.getYOffset() > 0 ? 3.0D : 1.0D) / 32.0D;
                    z1 = 8.0D / 32.0D;
                    z2 = 8.0D / 32.0D;
                    break;
                case Z:
                    z1 = (this.facingDirection.getZOffset() < 0 ? 3.0D : 1.0D) / 32.0D;
                    z2 = (this.facingDirection.getZOffset() > 0 ? 3.0D : 1.0D) / 32.0D;
            }
            this.setBoundingBox(new AxisAlignedBB(posX - x1, posY - y1, posZ - z1, posX + x2, posY + y2, posZ + z2));
        }
    }

    @Override
    public void onKillCommand() {
        this.setDisplayedItem(ItemStack.EMPTY);
        super.onKillCommand();
    }

    @Override
    public boolean attackEntityFrom(@Nonnull DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if (!source.isExplosion() && !this.getDisplayedItem().isEmpty()) {
            if (!this.world.isRemote) {
                this.dropItemOrSelf(source.getTrueSource(), false);
                this.playSound(AquacultureSounds.fishMountRemoved, 1.0F, 1.0F);
            }
            return true;
        } else {
            return super.attackEntityFrom(source, amount);
        }
    }

    @Override
    public int getWidthPixels() {
        return 12;
    }

    @Override
    public int getHeightPixels() {
        return 8;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isInRangeToRenderDist(double distance) {
        double d0 = 16.0D;
        d0 = d0 * 64.0D * getRenderDistanceWeight();
        return distance < d0 * d0;
    }

    @Override
    public void onBroken(@Nullable Entity brokenEntity) {
        this.playSound(AquacultureSounds.fismMountBroken, 1.0F, 1.0F);
        this.dropItemOrSelf(brokenEntity, true);
    }

    @Override
    public void playPlaceSound() {
        this.playSound(AquacultureSounds.fishMountPlaced, 1.0F, 1.0F);
    }

    private void dropItemOrSelf(@Nullable Entity entity, boolean shouldDropSelf) {
        if (!this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
            if (entity == null) {
                this.setDisplayedItem(ItemStack.EMPTY);
            }

        } else {
            ItemStack displayStack = this.getDisplayedItem();
            this.setDisplayedItem(ItemStack.EMPTY);
            if (entity instanceof PlayerEntity) {
                PlayerEntity playerentity = (PlayerEntity) entity;
                if (playerentity.abilities.isCreativeMode) {
                    this.setDisplayedItem(ItemStack.EMPTY);
                    return;
                }
            }

            if (shouldDropSelf) {
                this.entityDropItem(this.getItem());
            }

            if (!displayStack.isEmpty()) {
                displayStack = displayStack.copy();
                if (this.rand.nextFloat() < this.itemDropChance) {
                    this.entityDropItem(displayStack);
                }
            }
        }
    }

    private Item getItem() {
        ResourceLocation location = this.getType().getRegistryName();
        if (ForgeRegistries.ITEMS.containsKey(location) && location != null) {
            return ForgeRegistries.ITEMS.getValue(location);
        }
        return Items.AIR;
    }

    @Nonnull
    public ItemStack getDisplayedItem() {
        return this.getDataManager().get(ITEM);
    }

    public void setDisplayedItem(@Nonnull ItemStack stack) {
        this.setDisplayedItemWithUpdate(stack, true);
    }

    public void setDisplayedItemWithUpdate(@Nonnull ItemStack stack, boolean shouldUpdate) {
        if (!stack.isEmpty()) {
            stack = stack.copy();
            stack.setCount(1);
        }

        this.getDataManager().set(ITEM, stack);
        if (!stack.isEmpty()) {
            this.playSound(AquacultureSounds.fishMountFishPlaced, 1.0F, 1.0F);
        }

        if (shouldUpdate && this.hangingPosition != null) {
            this.world.updateComparatorOutputLevel(this.hangingPosition, Blocks.AIR);
        }
    }

    @Override
    public boolean replaceItemInInventory(int inventorySlot, @Nonnull ItemStack stack) {
        if (inventorySlot == 0) {
            this.setDisplayedItem(stack);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key) {
        if (key.equals(ITEM)) {
            ItemStack displayStack = this.getDisplayedItem();
            if (displayStack != null && !displayStack.isEmpty()) {
                EntityType entityType = ForgeRegistries.ENTITIES.getValue(displayStack.getItem().getRegistryName());
                if (entityType != null && entityType != EntityType.PIG) {
                    this.entity = entityType.create(this.world);
                }
            } else {
                this.entity = null;
            }
        }
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        if (!this.getDisplayedItem().isEmpty()) {
            compound.put("Item", this.getDisplayedItem().write(new CompoundNBT()));
            compound.putFloat("ItemDropChance", this.itemDropChance);
        }
        compound.putByte("Facing", (byte) this.facingDirection.getIndex());
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        CompoundNBT nbt = compound.getCompound("Item");
        if (nbt != null && !nbt.isEmpty()) {
            ItemStack nbtStack = ItemStack.read(nbt);
            if (nbtStack.isEmpty()) {
                PRIVATE_LOGGER.warn("Unable to load item from: {}", nbt);
            }

            ItemStack displayStack = this.getDisplayedItem();
            if (!displayStack.isEmpty() && !ItemStack.areItemStacksEqual(nbtStack, displayStack)) {
                this.setDisplayedItem(ItemStack.EMPTY);
            }

            this.setDisplayedItemWithUpdate(nbtStack, false);
            if (compound.contains("ItemDropChance", 99)) {
                this.itemDropChance = compound.getFloat("ItemDropChance");
            }
        }
        this.updateFacingWithBoundingBox(Direction.byIndex(compound.getByte("Facing")));
    }

    @Override
    @Nonnull
    public ActionResultType processInitialInteract(PlayerEntity player, @Nonnull Hand hand) {
        ItemStack heldStack = player.getHeldItem(hand);
        if (!this.world.isRemote) {
            if (this.getDisplayedItem().isEmpty()) {
                Item heldItem = heldStack.getItem();
                EntityType<?> entityType = ForgeRegistries.ENTITIES.getValue(heldItem.getRegistryName());
                if (entityType != EntityType.PIG && AquacultureAPI.FISH_DATA.getFish().contains(heldItem)) {
                    this.setDisplayedItem(heldStack);
                    if (!player.abilities.isCreativeMode) {
                        heldStack.shrink(1);
                    }
                }
            }
            return ActionResultType.CONSUME;
        }
        return super.processInitialInteract(player, hand);
    }

    @Override
    @Nonnull
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return !this.getDisplayedItem().isEmpty() ? this.getDisplayedItem() : new ItemStack(this.getItem());
    }

    @Override
    protected void setRotation(float yaw, float pitch) {
        super.setRotation(yaw, pitch);
        if (pitch == 0) {
            this.updateFacingWithBoundingBox(Direction.fromAngle(yaw));
        } else {
            this.updateFacingWithBoundingBox(pitch < 0 ? Direction.UP : Direction.DOWN);
        }
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        buffer.writeResourceLocation(this.getType().getRegistryName());
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
    }
}
