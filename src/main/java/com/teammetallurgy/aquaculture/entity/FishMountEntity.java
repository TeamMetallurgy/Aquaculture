package com.teammetallurgy.aquaculture.entity;

import com.teammetallurgy.aquaculture.api.AquacultureAPI;
import com.teammetallurgy.aquaculture.init.AquaSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class FishMountEntity extends HangingEntity implements IEntityAdditionalSpawnData {
    private static final Logger PRIVATE_LOGGER = LogManager.getLogger();
    private static final EntityDataAccessor<ItemStack> ITEM = SynchedEntityData.defineId(FishMountEntity.class, EntityDataSerializers.ITEM_STACK);
    private float itemDropChance = 1.0F;
    public Entity entity;

    public FishMountEntity(EntityType<? extends FishMountEntity> type, Level world) {
        super(type, world);
    }

    public FishMountEntity(EntityType<? extends FishMountEntity> type, Level world, BlockPos blockPos, Direction direction) {
        super(type, world, blockPos);
        this.setDirection(direction);
    }

    public FishMountEntity(PlayMessages.SpawnEntity spawnPacket, Level world) {
        this((EntityType<? extends FishMountEntity>) ForgeRegistries.ENTITY_TYPES.getValue(spawnPacket.getAdditionalData().readResourceLocation()), world);
    }

    @Override
    protected float getEyeHeight(Pose pose, EntityDimensions size) {
        return 0.0F;
    }

    @Override
    protected void defineSynchedData() {
        this.getEntityData().define(ITEM, ItemStack.EMPTY);
    }

    @Override
    public void setDirection(@Nonnull Direction direction) {
        Validate.notNull(direction);
        this.direction = direction;
        if (direction.getAxis().isHorizontal()) {
            this.setXRot(0.0F);
            this.setYRot((float) (this.direction.get2DDataValue() * 90));
        } else {
            this.setXRot((float) (-90 * direction.getAxisDirection().getStep()));
            this.setYRot(0.0F);
        }
        this.xRotO = this.getXRot();
        this.yRotO = this.getYRot();
        this.recalculateBoundingBox();
    }

    @Override
    public boolean survives() {
        if (!this.level.noCollision(this)) {
            return false;
        } else {
            BlockState state = this.level.getBlockState(this.pos.relative(this.direction.getOpposite()));
            return (state.getMaterial().isSolid() || this.direction.getAxis().isHorizontal() && DiodeBlock.isDiode(state)) && this.level.getEntities(this, this.getBoundingBox(), HANGING_ENTITY).isEmpty();
        }
    }

    @Override
    protected void recalculateBoundingBox() {
        if (this.direction != null) {
            double d0 = 0.46875D;
            double posX = (double) this.pos.getX() + 0.5D - (double) this.direction.getStepX() * d0;
            double posY = (double) this.pos.getY() + 0.5D - (double) this.direction.getStepY() * d0;
            double posZ = (double) this.pos.getZ() + 0.5D - (double) this.direction.getStepZ() * d0;
            this.setPosRaw(posX, posY, posZ);
            double x1 = this.getWidth() / 32.0D;
            double x2 = this.getWidth() / 32.0D;
            double y1 = this.getHeight() / 32.0D;
            double y2 = this.getHeight() / 32.0D;
            double z1 = this.getWidth() / 32.0D;
            double z2 = this.getWidth() / 32.0D;
            switch (this.direction.getAxis()) {
                case X -> {
                    x1 = (this.direction.getStepX() < 0 ? 3.0D : 1.0D) / 32.0D;
                    x2 = (this.direction.getStepX() > 0 ? 3.0D : 1.0D) / 32.0D;
                }
                case Y -> {
                    y1 = (this.direction.getStepY() < 0 ? 3.0D : 1.0D) / 32.0D;
                    y2 = (this.direction.getStepY() > 0 ? 3.0D : 1.0D) / 32.0D;
                    z1 = 8.0D / 32.0D;
                    z2 = 8.0D / 32.0D;
                }
                case Z -> {
                    z1 = (this.direction.getStepZ() < 0 ? 3.0D : 1.0D) / 32.0D;
                    z2 = (this.direction.getStepZ() > 0 ? 3.0D : 1.0D) / 32.0D;
                }
            }
            this.setBoundingBox(new AABB(posX - x1, posY - y1, posZ - z1, posX + x2, posY + y2, posZ + z2));
        }
    }

    @Override
    public void kill() {
        this.setDisplayedItem(ItemStack.EMPTY);
        super.kill();
    }

    @Override
    public boolean hurt(@Nonnull DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if (!source.isExplosion() && !this.getDisplayedItem().isEmpty()) {
            if (!this.level.isClientSide) {
                this.dropItemOrSelf(source.getEntity(), false);
                this.playSound(AquaSounds.FISH_MOUNT_REMOVED.get(), 1.0F, 1.0F);
            }
            return true;
        } else {
            return super.hurt(source, amount);
        }
    }

    @Override
    public int getWidth() {
        return 12;
    }

    @Override
    public int getHeight() {
        return 8;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean shouldRenderAtSqrDistance(double distance) {
        double d0 = 16.0D;
        d0 = d0 * 64.0D * getViewScale();
        return distance < d0 * d0;
    }

    @Override
    public void dropItem(@Nullable Entity brokenEntity) {
        this.playSound(AquaSounds.FISH_MOUNT_BROKEN.get(), 1.0F, 1.0F);
        this.dropItemOrSelf(brokenEntity, true);
    }

    @Override
    public void playPlacementSound() {
        this.playSound(AquaSounds.FISH_MOUNT_PLACED.get(), 1.0F, 1.0F);
    }

    private void dropItemOrSelf(@Nullable Entity entity, boolean shouldDropSelf) {
        if (!this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            if (entity == null) {
                this.setDisplayedItem(ItemStack.EMPTY);
            }

        } else {
            ItemStack displayStack = this.getDisplayedItem();
            this.setDisplayedItem(ItemStack.EMPTY);
            if (entity instanceof Player player) {
                if (player.getAbilities().instabuild) {
                    this.setDisplayedItem(ItemStack.EMPTY);
                    return;
                }
            }

            if (shouldDropSelf) {
                this.spawnAtLocation(this.getItem());
            }

            if (!displayStack.isEmpty()) {
                displayStack = displayStack.copy();
                if (this.random.nextFloat() < this.itemDropChance) {
                    this.spawnAtLocation(displayStack);
                }
            }
        }
    }

    private Item getItem() {
        ResourceLocation location = ForgeRegistries.ENTITY_TYPES.getKey(this.getType());
        if (ForgeRegistries.ITEMS.containsKey(location) && location != null) {
            return ForgeRegistries.ITEMS.getValue(location);
        }
        return Items.AIR;
    }

    @Nonnull
    public ItemStack getDisplayedItem() {
        return this.getEntityData().get(ITEM);
    }

    public void setDisplayedItem(@Nonnull ItemStack stack) {
        this.setDisplayedItemWithUpdate(stack, true);
    }

    public void setDisplayedItemWithUpdate(@Nonnull ItemStack stack, boolean shouldUpdate) {
        if (!stack.isEmpty()) {
            stack = stack.copy();
            stack.setCount(1);
        }

        this.getEntityData().set(ITEM, stack);
        if (!stack.isEmpty()) {
            this.playSound(AquaSounds.FISH_MOUNT_ADD_ITEM.get(), 1.0F, 1.0F);
        }

        if (shouldUpdate && this.pos != null) {
            this.level.updateNeighbourForOutputSignal(this.pos, Blocks.AIR);
        }
    }

    @Override
    public void setItemSlot(@Nonnull EquipmentSlot slot, @Nonnull ItemStack stack) {
        if (slot == EquipmentSlot.MAINHAND) {
            this.setDisplayedItem(stack);
        }
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (key.equals(ITEM)) {
            ItemStack displayStack = this.getDisplayedItem();
            if (displayStack != null && !displayStack.isEmpty()) {
                EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(ForgeRegistries.ITEMS.getKey(displayStack.getItem()));
                if (entityType != null && entityType != EntityType.PIG) {
                    this.entity = entityType.create(this.level);
                }
            } else {
                this.entity = null;
            }
        }
    }

    @Override
    public void addAdditionalSaveData(@Nonnull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (!this.getDisplayedItem().isEmpty()) {
            compound.put("Item", this.getDisplayedItem().save(new CompoundTag()));
            compound.putFloat("ItemDropChance", this.itemDropChance);
        }
        compound.putByte("Facing", (byte) this.direction.get3DDataValue());
    }

    @Override
    public void readAdditionalSaveData(@Nonnull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        CompoundTag nbt = compound.getCompound("Item");
        if (nbt != null && !nbt.isEmpty()) {
            ItemStack nbtStack = ItemStack.of(nbt);
            if (nbtStack.isEmpty()) {
                PRIVATE_LOGGER.warn("Unable to load item from: {}", nbt);
            }

            ItemStack displayStack = this.getDisplayedItem();
            if (!displayStack.isEmpty() && !ItemStack.matches(nbtStack, displayStack)) {
                this.setDisplayedItem(ItemStack.EMPTY);
            }

            this.setDisplayedItemWithUpdate(nbtStack, false);
            if (compound.contains("ItemDropChance", 99)) {
                this.itemDropChance = compound.getFloat("ItemDropChance");
            }
        }
        this.setDirection(Direction.from3DDataValue(compound.getByte("Facing")));
    }

    @Override
    @Nonnull
    public InteractionResult interact(Player player, @Nonnull InteractionHand hand) {
        ItemStack heldStack = player.getItemInHand(hand);
        if (!this.level.isClientSide) {
            if (this.getDisplayedItem().isEmpty()) {
                Item heldItem = heldStack.getItem();
                EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(ForgeRegistries.ITEMS.getKey(heldItem));
                if (entityType != EntityType.PIG && AquacultureAPI.FISH_DATA.getFish().contains(heldItem)) {
                    this.setDisplayedItem(heldStack);
                    if (!player.getAbilities().instabuild) {
                        heldStack.shrink(1);
                    }
                }
            }
            return InteractionResult.CONSUME;
        }
        return super.interact(player, hand);
    }

    @Override
    @Nonnull
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public ItemStack getPickedResult(HitResult target) {
        return !this.getDisplayedItem().isEmpty() ? this.getDisplayedItem() : new ItemStack(this.getItem());
    }

    @Override
    public void recreateFromPacket(@Nonnull ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        this.setDirection(Direction.from3DDataValue(packet.getData()));
    }

    @Override
    protected void setRot(float yaw, float pitch) {
        super.setRot(yaw, pitch);
        if (pitch == 0) {
            this.setDirection(Direction.fromYRot(yaw));
        } else {
            this.setDirection(pitch < 0 ? Direction.UP : Direction.DOWN);
        }
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(this.getType())));
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
    }
}