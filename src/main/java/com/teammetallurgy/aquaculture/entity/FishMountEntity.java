package com.teammetallurgy.aquaculture.entity;

import com.teammetallurgy.aquaculture.api.AquacultureAPI;
import com.teammetallurgy.aquaculture.init.AquaSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class FishMountEntity extends HangingEntity implements IEntityWithComplexSpawn {
    private static final EntityDataAccessor<ItemStack> DATA_ITEM = SynchedEntityData.defineId(FishMountEntity.class, EntityDataSerializers.ITEM_STACK);
    private float itemDropChance = 1.0F;
    public Entity entity;

    public FishMountEntity(EntityType<? extends FishMountEntity> type, Level world) {
        super(type, world);
    }

    public FishMountEntity(EntityType<? extends FishMountEntity> type, Level world, BlockPos blockPos, Direction direction) {
        super(type, world, blockPos);
        this.setDirection(direction);
    }

    public Identifier byName() {
        return BuiltInRegistries.ENTITY_TYPE.getKey(this.getType());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ITEM, ItemStack.EMPTY);
    }

    @Override
    public void setDirection(@Nonnull Direction direction) {
        Validate.notNull(direction);
        super.setDirectionRaw(direction);
        if (direction.getAxis().isHorizontal()) {
            this.setXRot(0.0F);
            this.setYRot((float) (direction.get2DDataValue() * 90));
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
        if (!this.level().noCollision(this)) {
            return false;
        } else {
            BlockState blockstate = this.level().getBlockState(this.pos.relative(this.getDirection().getOpposite()));
            return blockstate.isSolid() || this.getDirection().getAxis().isHorizontal() && DiodeBlock.isDiode(blockstate) ? this.canCoexist(true) : false;
        }
    }

    @Override
    @Nonnull
    protected AABB calculateBoundingBox(@Nonnull BlockPos pos, @Nonnull Direction direction) {
        Vec3 vec3 = Vec3.atCenterOf(pos).relative(direction, -0.46875);
        Direction.Axis axis = direction.getAxis();
        double x = axis == Direction.Axis.X ? 0.0625 : 0.75;
        double y = axis == Direction.Axis.Y ? 0.0625 : 0.5;
        double z = axis == Direction.Axis.Z ? 0.0625 : 0.75;

        return AABB.ofSize(vec3, x, y, z);
    }

    @Override
    public void kill(@Nonnull ServerLevel level) {
        this.setDisplayedItem(ItemStack.EMPTY);
        super.kill(level);
    }

    @Override
    public boolean hurtClient(DamageSource damageSource) {
        return !this.isInvulnerableToBase(damageSource);
    }

    @Override
    public boolean hurtServer(@Nonnull ServerLevel level, @Nonnull DamageSource source, float amount) {
        if (this.isInvulnerableToBase(source)) {
            return false;
        } else if (!source.is(DamageTypeTags.IS_EXPLOSION) && !this.getItem().isEmpty()) {
                this.dropItemOrSelf(level, source.getEntity(), false);
                this.gameEvent(GameEvent.BLOCK_CHANGE, source.getEntity());
                this.playSound(AquaSounds.FISH_MOUNT_REMOVED.get(), 1.0F, 1.0F);
            return true;
        } else {
            return super.hurtServer(level, source, amount);
        }
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        double d0 = 16.0D;
        d0 = d0 * 64.0D * getViewScale();
        return distance < d0 * d0;
    }

    @Override
    public void dropItem(@Nonnull ServerLevel level, @Nullable Entity brokenEntity) {
        this.playSound(AquaSounds.FISH_MOUNT_BROKEN.get(), 1.0F, 1.0F);
        this.dropItemOrSelf(level, brokenEntity, true);
        this.gameEvent(GameEvent.BLOCK_CHANGE, entity);
    }

    @Override
    public void playPlacementSound() {
        this.playSound(AquaSounds.FISH_MOUNT_PLACED.get(), 1.0F, 1.0F);
    }

    private void dropItemOrSelf(ServerLevel level, @Nullable Entity entity, boolean shouldDropSelf) {
        ItemStack displayedStack = this.getItem();
        this.setDisplayedItem(ItemStack.EMPTY);
        if (!level.getGameRules().get(GameRules.ENTITY_DROPS)) {
            if (entity == null) {
                this.setDisplayedItem(ItemStack.EMPTY);
            }
        } else {
            if (entity instanceof Player player && player.hasInfiniteMaterials()) {
                this.setDisplayedItem(ItemStack.EMPTY);
                return;
            }

            if (shouldDropSelf) {
                this.spawnAtLocation(level, this.getItemVariant());
            }

            if (!displayedStack.isEmpty()) {
                displayedStack = displayedStack.copy();
                if (this.random.nextFloat() < this.itemDropChance) {
                    this.spawnAtLocation(level, displayedStack);
                }
            }
        }
    }

    private Item getItemVariant() {
        Identifier location = BuiltInRegistries.ENTITY_TYPE.getKey(this.getType());
        if (BuiltInRegistries.ITEM.containsKey(location) && location != null) {
            return BuiltInRegistries.ITEM.getValue(location);
        }
        return Items.AIR;
    }

    @Nonnull
    public ItemStack getItem() {
        return this.getEntityData().get(DATA_ITEM);
    }

    public void setDisplayedItem(@Nonnull ItemStack stack) {
        this.setDisplayedItemWithUpdate(stack, true);
    }

    public void setDisplayedItemWithUpdate(@Nonnull ItemStack stack, boolean shouldUpdate) {
        if (!stack.isEmpty()) {
            stack = stack.copyWithCount(1);
        }

        this.getEntityData().set(DATA_ITEM, stack);
        if (!stack.isEmpty()) {
            this.playSound(AquaSounds.FISH_MOUNT_ADD_ITEM.get(), 1.0F, 1.0F);
        }

        if (shouldUpdate && this.pos != null) {
            this.level().updateNeighbourForOutputSignal(this.pos, Blocks.AIR);
        }
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (key.equals(DATA_ITEM)) {
            ItemStack displayStack = this.getItem();
            if (displayStack != null && !displayStack.isEmpty()) {
                EntityType<?> entityType = BuiltInRegistries.ENTITY_TYPE.getValue(BuiltInRegistries.ITEM.getKey(displayStack.getItem()));
                if (entityType != null && entityType != EntityType.PIG) {
                    this.entity = entityType.create(this.level(), EntitySpawnReason.TRIGGERED);
                }
            } else {
                this.entity = null;
            }
        }
    }

    @Override
    public void addAdditionalSaveData(@Nonnull ValueOutput output) {
        super.addAdditionalSaveData(output);
        ItemStack stack = this.getItem();
        if (!stack.isEmpty()) {
            output.store("Item", ItemStack.CODEC, stack);
        }

        output.putFloat("ItemDropChance", this.itemDropChance);
        output.store("Facing", Direction.LEGACY_ID_CODEC, this.getDirection());
    }

    @Override
    public void readAdditionalSaveData(@Nonnull ValueInput input) {
        super.readAdditionalSaveData(input);

        ItemStack stack = input.read("Item", ItemStack.CODEC).orElse(ItemStack.EMPTY);
        ItemStack displayStack = this.getItem();
        if (!displayStack.isEmpty() && !ItemStack.matches(stack, displayStack)) {
            this.setDisplayedItem(displayStack);
        }

        this.setDisplayedItemWithUpdate(stack, false);
        this.itemDropChance = input.getFloatOr("ItemDropChance", 1.0F);
        this.setDirection(input.read("Facing", Direction.LEGACY_ID_CODEC).orElse(Direction.DOWN));
    }

    @Override
    @Nonnull
    public InteractionResult interact(Player player, @Nonnull InteractionHand hand) {
        ItemStack heldStack = player.getItemInHand(hand);
        if (!this.level().isClientSide()) {
            if (this.getItem().isEmpty()) {
                Item heldItem = heldStack.getItem();
                EntityType<?> entityType = BuiltInRegistries.ENTITY_TYPE.getValue(BuiltInRegistries.ITEM.getKey(heldItem));
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
    public Packet<ClientGamePacketListener> getAddEntityPacket(@Nonnull ServerEntity serverEntity) {
        return new ClientboundAddEntityPacket(this, this.getDirection().get3DDataValue(), this.getPos());
    }

    @Override
    public void recreateFromPacket(@Nonnull ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        this.setDirection(Direction.from3DDataValue(packet.getData()));
    }

    @Override
    public ItemStack getPickResult() {
        return !this.getItem().isEmpty() ? this.getItem() : new ItemStack(this.getItemVariant());
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
    public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
        buffer.writeIdentifier(Objects.requireNonNull(BuiltInRegistries.ENTITY_TYPE.getKey(this.getType())));
    }

    @Override
    public void readSpawnData(@Nonnull RegistryFriendlyByteBuf additionalData) {
    }
}