package com.teammetallurgy.aquaculture.entity;

import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.api.fishing.Hook;
import com.teammetallurgy.aquaculture.api.fishing.Hooks;
import com.teammetallurgy.aquaculture.init.AquaEntities;
import com.teammetallurgy.aquaculture.init.AquaItems;
import com.teammetallurgy.aquaculture.init.AquaLootTables;
import com.teammetallurgy.aquaculture.item.AquaFishingRodItem;
import com.teammetallurgy.aquaculture.item.HookItem;
import com.teammetallurgy.aquaculture.misc.AquaConfig;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class AquaFishingBobberEntity extends FishingBobberEntity implements IEntityAdditionalSpawnData {
    private final Hook hook;
    private final ItemStack fishingLine;
    private final ItemStack bobber;
    private final ItemStack fishingRod;
    private final int luck;

    public AquaFishingBobberEntity(FMLPlayMessages.SpawnEntity spawnPacket, World world) {
        super(world.getPlayerByUuid(spawnPacket.getAdditionalData().readUniqueId()), world, 0, 0);
        PacketBuffer buf = spawnPacket.getAdditionalData();
        this.luck = buf.readInt();
        HookItem hookItem = ((HookItem) Hook.HOOKS.get(buf.readString()));
        if (hookItem != null && hookItem != Items.AIR) {
            this.hook = hookItem.getHookType();
        } else {
            this.hook = Hooks.EMPTY;
        }
        this.fishingLine = buf.readItemStack();
        this.bobber = buf.readItemStack();
        this.fishingRod = buf.readItemStack();
    }

    public AquaFishingBobberEntity(PlayerEntity player, World world, int luck, int lureSpeed, @Nonnull Hook hook, @Nonnull ItemStack fishingLine, @Nonnull ItemStack bobber, @Nonnull ItemStack rod) {
        super(player, world, luck, lureSpeed);
        this.luck = luck;
        this.angler.fishingBobber = this;
        this.hook = hook;
        this.fishingLine = fishingLine;
        this.bobber = bobber;
        this.fishingRod = rod;
        if (this.hasHook() && hook.getWeight() != null) {
            this.setMotion(this.getMotion().mul(hook.getWeight()));
        }
    }

    @Nonnull
    public Hook getHook() {
        return this.hook;
    }

    public boolean hasHook() {
        return this.hook != Hooks.EMPTY;
    }

    @Nonnull
    public ItemStack getBobber() {
        return this.bobber;
    }

    public boolean hasBobber() {
        return !this.getBobber().isEmpty();
    }

    @Nonnull
    public ItemStack getFishingLine() {
        return this.fishingLine;
    }

    @Override
    @Nonnull
    public EntityType<?> getType() {
        return AquaEntities.BOBBER;
    }

    @Override
    @Nonnull
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public int handleHookRetraction(@Nonnull ItemStack stack) {
        boolean isAdminRod = AquaConfig.BASIC_OPTIONS.debugMode.get() && stack.getItem() == AquaItems.NEPTUNIUM_FISHING_ROD;
        if (!this.world.isRemote && this.angler != null) {
            int rodDamage = 0;
            ItemFishedEvent event = null;
            if (this.caughtEntity != null && !isAdminRod) {
                this.bringInHookedEntity();
                CriteriaTriggers.FISHING_ROD_HOOKED.trigger((ServerPlayerEntity) this.angler, stack, this, Collections.emptyList());
                this.world.setEntityState(this, (byte) 31);
                rodDamage = this.caughtEntity instanceof ItemEntity ? 3 : 5;
            } else if ((this.ticksCatchable > 0 || isAdminRod) && this.world instanceof ServerWorld) {
                ServerWorld serverWorld = (ServerWorld) this.world;
                LootContext.Builder builder = new LootContext.Builder(serverWorld).withParameter(LootParameters.POSITION, new BlockPos(this)).withParameter(LootParameters.TOOL, stack).withRandom(this.rand).withLuck((float) this.luck + this.angler.getLuck());
                builder.withParameter(LootParameters.KILLER_ENTITY, this.angler).withParameter(LootParameters.THIS_ENTITY, this);

                List<ItemStack> lootEntries = getLoot(builder, serverWorld);
                if (lootEntries.isEmpty()) {
                    if (!this.world.isAirBlock(new BlockPos(this))) {
                        Aquaculture.LOG.error("Loot was empty in Biome: " + this.world.getBiome(new BlockPos(this)) + ". Please report on Github");
                    }
                }
                if (!lootEntries.isEmpty()) {
                    event = new ItemFishedEvent(lootEntries, this.inGround ? 2 : 1, this);
                    MinecraftForge.EVENT_BUS.post(event);
                    if (event.isCanceled()) {
                        this.remove();
                        return event.getRodDamage();
                    }
                    CriteriaTriggers.FISHING_ROD_HOOKED.trigger((ServerPlayerEntity) this.angler, stack, this, lootEntries);

                    spawnLoot(lootEntries);
                    if (this.hasHook() && this.hook.getDoubleCatchChance() > 0) {
                        if (this.rand.nextDouble() <= this.hook.getDoubleCatchChance()) {
                            List<ItemStack> doubleLoot = getLoot(builder, serverWorld);
                            if (!doubleLoot.isEmpty()) {
                                MinecraftForge.EVENT_BUS.post(new ItemFishedEvent(doubleLoot, 0, this));
                                spawnLoot(doubleLoot);
                                this.playSound(SoundEvents.ENTITY_FISHING_BOBBER_SPLASH, 0.25F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
                            }
                        }
                    }

                    //Bait
                    if (!this.angler.isCreative()) {
                        ItemStackHandler rodHandler = AquaFishingRodItem.getHandler(this.fishingRod);
                        ItemStack bait = rodHandler.getStackInSlot(1);
                        if (!bait.isEmpty()) {
                            if (bait.attemptDamageItem(1, this.world.rand, null)) {
                                bait.shrink(1);
                                this.playSound(SoundEvents.ITEM_TRIDENT_HIT_GROUND, 0.7F, 0.2F);
                            }
                            rodHandler.setStackInSlot(1, bait);
                        }
                    }
                    rodDamage = 1;
                }
            }
            if (this.inGround) {
                rodDamage = 2;
            }
            this.remove();
            return event == null ? rodDamage : event.getRodDamage();
        } else {
            return 0;
        }
    }

    private List<ItemStack> getLoot(LootContext.Builder builder, ServerWorld serverWorld) {
        ResourceLocation lootTableLocation;
        if (this.isLavaHookInLava(this, this.world, new BlockPos(this))) {
            if (serverWorld.getWorld().getDimension().isNether()) {
                lootTableLocation = AquaLootTables.NETHER_FISHING;
            } else {
                lootTableLocation = AquaLootTables.LAVA_FISHING;
            }
        } else {
            lootTableLocation = LootTables.GAMEPLAY_FISHING;
        }
        LootTable lootTable = serverWorld.getServer().getLootTableManager().getLootTableFromLocation(lootTableLocation);
        return lootTable.generate(builder.build(LootParameterSets.FISHING));
    }

    private void spawnLoot(List<ItemStack> lootEntries) {
        for (ItemStack loot : lootEntries) {
            ItemEntity lootEntity = new ItemEntity(this.world, this.getPosX(), this.getPosY(), this.getPosZ(), loot) {
                @Override
                public boolean canRenderOnFire() {
                    return false;
                }

                @Override
                protected void setOnFireFromLava() {
                }

                @Override
                public boolean isInvulnerableTo(@Nonnull DamageSource source) {
                    BlockPos spawnPos = new BlockPos(AquaFishingBobberEntity.this.getPosX(), AquaFishingBobberEntity.this.getPosY(), AquaFishingBobberEntity.this.getPosZ());
                    return AquaFishingBobberEntity.this.isLavaHookInLava(AquaFishingBobberEntity.this, this.world, spawnPos) || super.isInvulnerableTo(source);
                }
            };
            double x = this.angler.getPosX() - this.getPosX();
            double y = this.angler.getPosY() - this.getPosY();
            double z = this.angler.getPosZ() - this.getPosZ();
            lootEntity.setMotion(x * 0.1D, (y * 0.1D + Math.sqrt(Math.sqrt(x * x + y * y + z * z)) * 0.08D) + (this.hasHook() && this.isLavaHookInLava(this, this.world, new BlockPos(x, y, z)) ? 0.2D : 0.0D), z * 0.1D);
            this.world.addEntity(lootEntity);
            this.angler.world.addEntity(new ExperienceOrbEntity(this.angler.world, this.angler.getPosX(), this.angler.getPosY() + 0.5D, this.angler.getPosZ() + 0.5D, this.rand.nextInt(6) + 1));
            if (loot.getItem().isIn(ItemTags.FISHES)) {
                this.angler.addStat(Stats.FISH_CAUGHT, 1);
            }
        }
    }

    public boolean isLavaHookInLava(AquaFishingBobberEntity bobber, World world, BlockPos pos) {
        return bobber.hasHook() && bobber.hook.getFluids().contains(FluidTags.LAVA) && world.getFluidState(pos).isTagged(FluidTags.LAVA);
    }

    @Override
    public void tick() {
        if (this.hasHook() && this.hook.getFluids().contains(FluidTags.LAVA)) {
            if (this.hook.getFluids().contains(FluidTags.WATER) && world.getFluidState(new BlockPos(this)).isTagged(FluidTags.WATER)) {
                super.tick();
            } else {
                this.lavaFishingTick();
            }
        } else {
            super.tick();
        }
    }

    private void lavaFishingTick() {
        super.baseTick();
        if (this.angler == null) {
            this.remove();
        } else if (this.world.isRemote || !this.shouldStopFishing()) {
            if (this.inGround) {
                ++this.ticksInGround;
                if (this.ticksInGround >= 1200) {
                    this.remove();
                    return;
                }
            }

            float f = 0.0F;
            BlockPos bobberPos = new BlockPos(this);
            IFluidState fluidState = this.world.getFluidState(bobberPos);
            if (fluidState.isTagged(FluidTags.LAVA)) {
                f = fluidState.getActualHeight(this.world, bobberPos);
            }

            if (this.currentState == State.FLYING) {
                if (this.caughtEntity != null) {
                    this.setMotion(Vec3d.ZERO);
                    this.currentState = State.HOOKED_IN_ENTITY;
                    return;
                }

                if (f > 0.0F) {
                    this.setMotion(this.getMotion().mul(0.3D, 0.2D, 0.3D));
                    this.currentState = State.BOBBING;
                    return;
                }

                if (!this.world.isRemote) {
                    this.checkCollision();
                }

                if (!this.inGround && !this.onGround && !this.collidedHorizontally) {
                    ++this.ticksInAir;
                } else {
                    this.ticksInAir = 0;
                    this.setMotion(Vec3d.ZERO);
                }
            } else {
                if (this.currentState == State.HOOKED_IN_ENTITY) {
                    if (this.caughtEntity != null) {
                        if (this.caughtEntity.removed) {
                            this.caughtEntity = null;
                            this.currentState = State.FLYING;
                        } else {
                            this.setPosition(this.caughtEntity.getPosX(), this.caughtEntity.getPosYHeight(0.8D), this.caughtEntity.getPosZ());
                        }
                    }
                    return;
                }

                if (this.currentState == State.BOBBING) {
                    Vec3d motion = this.getMotion();
                    double y = this.getPosY() + motion.y - (double) bobberPos.getY() - (double) f;
                    if (Math.abs(y) < 0.01D) {
                        y += Math.signum(y) * 0.1D;
                    }
                    this.setMotion(motion.x * 0.9D, motion.y - y * (double) this.rand.nextFloat() * 0.2D, motion.z * 0.9D);
                    if (!this.world.isRemote && f > 0.0F) {
                        this.catchingFish(bobberPos);
                    }
                }
            }
            if (!fluidState.isTagged(FluidTags.LAVA)) {
                this.setMotion(this.getMotion().add(0.0D, -0.03D, 0.0D));
            }

            this.move(MoverType.SELF, this.getMotion());
            this.updateRotation();
            this.setMotion(this.getMotion().scale(0.92D));
            this.recenterBoundingBox();
        }
    }

    @Override
    protected void catchingFish(BlockPos pos) { //Copied from vanilla. Only changed marked things
        ServerWorld serverworld = (ServerWorld) this.world;
        int delay = 1;
        BlockPos posUp = pos.up();
        if (this.rand.nextFloat() < 0.25F && this.world.isRainingAt(posUp)) {
            ++delay;
        }

        if (this.rand.nextFloat() < 0.5F && !this.world.canSeeSky(posUp)) {
            --delay;
        }

        if (this.ticksCatchable > 0) {
            --this.ticksCatchable;
            if (this.ticksCatchable <= 0) {
                this.ticksCaughtDelay = 0;
                this.ticksCatchableDelay = 0;
            } else {
                this.setMotion(this.getMotion().add(0.0D, -0.2D * (double) this.rand.nextFloat() * (double) this.rand.nextFloat(), 0.0D));
            }
        } else {
            float angle;
            float sin;
            float cos;
            double x;
            double y;
            double z;
            IFluidState fluidState; //Replaced BlockState checks with fluidState checks
            if (this.ticksCatchableDelay > 0) {
                this.ticksCatchableDelay -= delay;
                if (this.ticksCatchableDelay > 0) {
                    this.fishApproachAngle = (float) ((double) this.fishApproachAngle + this.rand.nextGaussian() * 4.0D);
                    angle = this.fishApproachAngle * 0.017453292F;
                    sin = MathHelper.sin(angle);
                    cos = MathHelper.cos(angle);
                    x = this.getPosX() + (double) (sin * (float) this.ticksCatchableDelay * 0.1F);
                    y = ((float) MathHelper.floor(this.getBoundingBox().minY) + 1.0F);
                    z = this.getPosZ() + (double) (cos * (float) this.ticksCatchableDelay * 0.1F);
                    fluidState = serverworld.getFluidState(new BlockPos(x, y - 1.0D, z));
                    float zOffset = sin * 0.04F;
                    float xOffset = cos * 0.04F;
                    if (fluidState.isTagged(FluidTags.WATER)) {
                        if (this.rand.nextFloat() < 0.15F) {
                            serverworld.spawnParticle(ParticleTypes.BUBBLE, x, y - 0.10000000149011612D, z, 1, sin, 0.1D, cos, 0.0D);
                        }
                        serverworld.spawnParticle(ParticleTypes.FISHING, x, y, z, 0, xOffset, 0.01D, -zOffset, 1.0D);
                        serverworld.spawnParticle(ParticleTypes.FISHING, x, y, z, 0, -xOffset, 0.01D, zOffset, 1.0D);
                    }
                    if (fluidState.isTagged(FluidTags.LAVA)) {
                        if (this.rand.nextFloat() < 0.15F) {
                            serverworld.spawnParticle(ParticleTypes.LAVA, x, y - 0.10000000149011612D, z, 1, sin, 0.1D, cos, 0.0D);
                        }
                        serverworld.spawnParticle(ParticleTypes.SMOKE, x, y, z, 0, xOffset, 0.01D, -zOffset, 1.0D);
                        serverworld.spawnParticle(ParticleTypes.SMOKE, x, y, z, 0, -xOffset, 0.01D, zOffset, 1.0D);
                    }
                    if (this.hasHook() && this.hook.getCatchSound() != null) {
                        this.world.playSound(null, this.getAngler() != null ? this.getAngler().getPosition() : this.getPosition(), this.hook.getCatchSound(), this.getSoundCategory(), 0.1F, 0.1F);
                    }
                } else {
                    Vec3d motion = this.getMotion();
                    this.setMotion(motion.x, (-0.4F * MathHelper.nextFloat(this.rand, 0.6F, 1.0F)), motion.z);
                    double boundingBox = this.getBoundingBox().minY + 0.5D;
                    if (serverworld.getFluidState(new BlockPos(this)).isTagged(FluidTags.WATER)) { //Water check added
                        this.playSound(SoundEvents.ENTITY_FISHING_BOBBER_SPLASH, 0.25F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
                        serverworld.spawnParticle(ParticleTypes.BUBBLE, this.getPosX(), boundingBox, this.getPosZ(), (int) (1.0F + this.getWidth() * 20.0F), this.getWidth(), 0.0D, this.getWidth(), 0.20000000298023224D);
                        serverworld.spawnParticle(ParticleTypes.FISHING, this.getPosX(), boundingBox, this.getPosZ(), (int) (1.0F + this.getWidth() * 20.0F), this.getWidth(), 0.0D, this.getWidth(), 0.20000000298023224D);
                    }

                    //Lava sound added
                    if (serverworld.getFluidState(new BlockPos(this)).isTagged(FluidTags.LAVA)) {
                        this.playSound(SoundEvents.BLOCK_LAVA_EXTINGUISH, 1.00F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
                        serverworld.spawnParticle(ParticleTypes.LAVA, this.getPosX(), boundingBox, this.getPosZ(), (int) (1.0F + this.getWidth() * 20.0F), this.getWidth(), 0.0D, this.getWidth(), 0.20000000298023224D);
                    }
                    if (this.hasHook() && this.hook.getMaxCatchable() > 0) {
                        this.ticksCatchable = MathHelper.nextInt(this.rand, this.hook.getMinCatchable(), this.hook.getMaxCatchable());
                    } else {
                        this.ticksCatchable = MathHelper.nextInt(this.rand, 20, 40);
                    }
                }
            } else if (this.ticksCaughtDelay > 0) {
                this.ticksCaughtDelay -= delay;
                angle = 0.15F;
                if (this.ticksCaughtDelay < 20) {
                    angle = (float) ((double) angle + (double) (20 - this.ticksCaughtDelay) * 0.05D);
                } else if (this.ticksCaughtDelay < 40) {
                    angle = (float) ((double) angle + (double) (40 - this.ticksCaughtDelay) * 0.02D);
                } else if (this.ticksCaughtDelay < 60) {
                    angle = (float) ((double) angle + (double) (60 - this.ticksCaughtDelay) * 0.01D);
                }

                if (this.rand.nextFloat() < angle) {
                    sin = MathHelper.nextFloat(this.rand, 0.0F, 360.0F) * 0.017453292F;
                    cos = MathHelper.nextFloat(this.rand, 25.0F, 60.0F);
                    x = this.getPosX() + (double) (MathHelper.sin(sin) * cos * 0.1F);
                    y = ((float) MathHelper.floor(this.getBoundingBox().minY) + 1.0F);
                    z = this.getPosZ() + (double) (MathHelper.cos(sin) * cos * 0.1F);
                    fluidState = serverworld.getFluidState(new BlockPos(x, y - 1.0D, z));
                    if (fluidState.isTagged(FluidTags.WATER)) {
                        serverworld.spawnParticle(ParticleTypes.SPLASH, x, y, z, 2 + this.rand.nextInt(2), 0.10000000149011612D, 0.0D, 0.10000000149011612D, 0.0D);
                    }
                }

                if (this.ticksCaughtDelay <= 0) {
                    this.fishApproachAngle = MathHelper.nextFloat(this.rand, 0.0F, 360.0F);
                    this.ticksCatchableDelay = MathHelper.nextInt(this.rand, 20, 80);
                }
            } else {
                this.ticksCaughtDelay = MathHelper.nextInt(this.rand, 100, 600);
                this.ticksCaughtDelay -= this.lureSpeed * 20 * 5;
            }
        }
    }

    @Override
    protected void setOnFireFromLava() {
        if (!this.hasHook() || this.hasHook() && !this.hook.getFluids().contains(FluidTags.LAVA)) {
            super.setOnFireFromLava();
        }
    }

    @Override
    public boolean canRenderOnFire() {
        return (this.hasHook() && !this.hook.getFluids().contains(FluidTags.LAVA)) && super.canRenderOnFire();
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        buffer.writeUniqueId(this.getAngler().getUniqueID());
        buffer.writeInt(this.luck);
        buffer.writeString(this.hook.getName() == null ? "" : this.hook.getName());
        buffer.writeItemStack(this.fishingLine);
        buffer.writeItemStack(this.bobber);
        buffer.writeItemStack(this.fishingRod);
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
    }
}