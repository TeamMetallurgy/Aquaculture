package com.teammetallurgy.aquaculture.entity;

import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.api.fishing.Hook;
import com.teammetallurgy.aquaculture.api.fishing.Hooks;
import com.teammetallurgy.aquaculture.init.AquaEntities;
import com.teammetallurgy.aquaculture.init.AquaItems;
import com.teammetallurgy.aquaculture.init.AquaLootTables;
import com.teammetallurgy.aquaculture.init.AquaSounds;
import com.teammetallurgy.aquaculture.item.AquaFishingRodItem;
import com.teammetallurgy.aquaculture.item.HookItem;
import com.teammetallurgy.aquaculture.misc.AquaConfig;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AquaFishingBobberEntity extends FishingHook implements IEntityAdditionalSpawnData {
    private final Random lavaTickRand = new Random();
    private final Hook hook;
    private final ItemStack fishingLine;
    private final ItemStack bobber;
    private final ItemStack fishingRod;
    private final int luck;

    public AquaFishingBobberEntity(PlayMessages.SpawnEntity spawnPacket, Level world) {
        super(world.getPlayerByUUID(spawnPacket.getAdditionalData().readUUID()), world, 0, 0);
        FriendlyByteBuf buf = spawnPacket.getAdditionalData();
        this.luck = buf.readInt();
        String hookName = buf.readUtf();
        if (hookName.isEmpty() || hookName == null) {
            this.hook = Hooks.EMPTY;
        } else {
            Item hookItem = Hook.HOOKS.get(hookName).get();
            this.hook = ((HookItem) hookItem).getHookType();
        }
        this.fishingLine = buf.readItem();
        this.bobber = buf.readItem();
        this.fishingRod = buf.readItem();
    }

    public AquaFishingBobberEntity(Player player, Level world, int luck, int lureSpeed, @Nonnull Hook hook, @Nonnull ItemStack fishingLine, @Nonnull ItemStack bobber, @Nonnull ItemStack rod) {
        super(player, world, luck, lureSpeed);
        this.luck = luck;
        player.fishing = this;
        this.hook = hook;
        this.fishingLine = fishingLine;
        this.bobber = bobber;
        this.fishingRod = rod;
        if (this.hasHook() && hook.getWeight() != null) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(hook.getWeight()));
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
        return AquaEntities.BOBBER.get();
    }

    @Override
    @Nonnull
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public int retrieve(@Nonnull ItemStack stack) {
        boolean isAdminRod = AquaConfig.BASIC_OPTIONS.debugMode.get() && stack.getItem() == AquaItems.NEPTUNIUM_FISHING_ROD.get();
        Player angler = this.getPlayerOwner();
        Level level = this.level();
        if (!level.isClientSide && angler != null && !this.shouldStopFishing(angler)) {
            int rodDamage = 0;
            ItemFishedEvent event = null;
            if (this.hookedIn != null && !isAdminRod) {
                this.pullEntity(this.hookedIn);
                CriteriaTriggers.FISHING_ROD_HOOKED.trigger((ServerPlayer) angler, stack, this, Collections.emptyList());
                level.broadcastEntityEvent(this, (byte) 31);
                rodDamage = this.hookedIn instanceof ItemEntity ? 3 : 5;
            } else if ((this.nibble > 0 || isAdminRod) && level instanceof ServerLevel serverLevel)  {
                LootParams lootParams = (new LootParams.Builder((ServerLevel)this.level())).withParameter(LootContextParams.ORIGIN, this.position()).withParameter(LootContextParams.TOOL, stack).withParameter(LootContextParams.THIS_ENTITY, this).withParameter(LootContextParams.KILLER_ENTITY, this.getOwner()).withParameter(LootContextParams.THIS_ENTITY, this).withLuck((float)this.luck + angler.getLuck()).create(LootContextParamSets.FISHING);

                List<ItemStack> lootEntries = getLoot(lootParams, serverLevel);
                if (lootEntries.isEmpty()) {
                    if (level.dimension() == Level.END) {
                        lootEntries.add(new ItemStack(AquaItems.FISH_BONES.get()));
                    } else {
                        if (!level.isEmptyBlock(this.blockPosition()) && (level.getFluidState(this.blockPosition()).isSource())) {
                            lootEntries.add(new ItemStack(Items.COD)); //Last resort fallback, for edge-cases
                            ResourceLocation biomeFromRegistry = level.registryAccess().registryOrThrow(Registries.BIOME).getKey(level.getBiome(this.blockPosition()).value());
                            if (biomeFromRegistry != null) {
                                Aquaculture.LOG.error("Loot was empty in Biome: " + biomeFromRegistry + ". Please report on Github");
                            }
                        }
                    }
                }
                if (!lootEntries.isEmpty()) {
                    event = new ItemFishedEvent(lootEntries, this.onGround() ? 2 : 1, this);
                    MinecraftForge.EVENT_BUS.post(event);
                    if (event.isCanceled()) {
                        this.discard();
                        return event.getRodDamage();
                    }
                    CriteriaTriggers.FISHING_ROD_HOOKED.trigger((ServerPlayer) angler, stack, this, lootEntries);

                    this.spawnLoot(angler, lootEntries);
                    if (this.hasHook() && this.hook.getDoubleCatchChance() > 0) {
                        if (this.random.nextDouble() <= this.hook.getDoubleCatchChance()) {
                            List<ItemStack> doubleLoot = getLoot(lootParams, serverLevel);
                            if (!doubleLoot.isEmpty()) {
                                MinecraftForge.EVENT_BUS.post(new ItemFishedEvent(doubleLoot, 0, this));
                                this.spawnLoot(angler, doubleLoot);
                                this.playSound(SoundEvents.FISHING_BOBBER_SPLASH, 0.25F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
                            }
                        }
                    }

                    //Bait
                    if (!angler.isCreative()) {
                        ItemStackHandler rodHandler = AquaFishingRodItem.getHandler(this.fishingRod);
                        ItemStack bait = rodHandler.getStackInSlot(1);
                        if (!bait.isEmpty()) {
                            if (bait.getItem().canBeDepleted()) {
                                bait.hurt(1, level.random, null);
                            }
                            bait.shrink(1);
                            this.playSound(AquaSounds.BOBBER_BAIT_BREAK.get(), 0.7F, 0.2F);
                            rodHandler.setStackInSlot(1, bait);
                        }
                    }
                    rodDamage = 1;
                }
            }
            if (this.onGround()) {
                rodDamage = 2;
            }
            this.discard();
            return event == null ? rodDamage : event.getRodDamage();
        } else {
            return 0;
        }
    }

    @Override
    protected boolean shouldStopFishing(Player player) {
        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();
        boolean isMainHandRod = mainHand.canPerformAction(ToolActions.FISHING_ROD_CAST);
        boolean isOffHandRod = offHand.canPerformAction(ToolActions.FISHING_ROD_CAST);
        if (!player.isRemoved() && player.isAlive() && (isMainHandRod || isOffHandRod) && !(this.distanceToSqr(player) > 1024.0D)) {
            return false;
        } else {
            this.discard();
            return true;
        }
    }

    private List<ItemStack> getLoot(LootParams lootParams, ServerLevel serverLevel) {
        ResourceLocation lootTableLocation;
        if (this.isLavaHookInLava(this, serverLevel, this.blockPosition())) {
            if (serverLevel.getLevel().dimensionType().hasCeiling()) {
                lootTableLocation = AquaLootTables.NETHER_FISHING;
            } else {
                lootTableLocation = AquaLootTables.LAVA_FISHING;
            }
        } else {
            lootTableLocation = BuiltInLootTables.FISHING;
        }
        LootTable lootTable = serverLevel.getServer().getLootData().getLootTable(lootTableLocation);
        return lootTable.getRandomItems(lootParams);
    }

    private void spawnLoot(Player angler, List<ItemStack> lootEntries) {
        Level level = this.level();
        for (ItemStack loot : lootEntries) {
            ItemEntity lootEntity = new ItemEntity(level, this.getX(), this.getY(), this.getZ(), loot) {
                @Override
                public boolean displayFireAnimation() {
                    return false;
                }

                @Override
                public void lavaHurt() {
                }

                @Override
                public boolean isInvulnerableTo(@Nonnull DamageSource source) {
                    BlockPos spawnPos = new BlockPos((int) AquaFishingBobberEntity.this.getX(), (int) AquaFishingBobberEntity.this.getY(), (int) AquaFishingBobberEntity.this.getZ());
                    return AquaFishingBobberEntity.this.isLavaHookInLava(AquaFishingBobberEntity.this, level, spawnPos) || super.isInvulnerableTo(source);
                }
            };
            double x = angler.getX() - this.getX();
            double y = angler.getY() - this.getY();
            double z = angler.getZ() - this.getZ();
            lootEntity.setDeltaMovement(x * 0.1D, (y * 0.1D + Math.sqrt(Math.sqrt(x * x + y * y + z * z)) * 0.08D) + (this.hasHook() && this.isLavaHookInLava(this, this.level(), new BlockPos((int) x, (int) y, (int) z)) ? 0.2D : 0.0D), z * 0.1D);
            level.addFreshEntity(lootEntity);
            angler.level().addFreshEntity(new ExperienceOrb(angler.level(), angler.getX(), angler.getY() + 0.5D, angler.getZ() + 0.5D, this.random.nextInt(6) + 1));
            if (loot.is(ItemTags.FISHES)) {
                angler.awardStat(Stats.FISH_CAUGHT, 1);
            }
        }
    }

    public boolean isLavaHookInLava(AquaFishingBobberEntity bobber, Level world, BlockPos pos) {
        return bobber.hasHook() && bobber.hook.getFluids().contains(FluidTags.LAVA) && world.getFluidState(pos).is(FluidTags.LAVA);
    }

    @Override
    public void tick() {
        if (this.hasHook() && this.hook.getFluids().contains(FluidTags.LAVA)) {
            if (this.hook.getFluids().contains(FluidTags.WATER) && this.level().getFluidState(this.blockPosition()).is(FluidTags.WATER)) {
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
        this.lavaTickRand.setSeed(this.getUUID().getLeastSignificantBits() ^ this.level().getGameTime());
        Player angler = this.getPlayerOwner();
        if (angler == null) {
            this.discard();
        } else if (this.level().isClientSide || !this.shouldStopFishing(angler)) {
            if (this.onGround()) {
                ++this.life;
                if (this.life >= 1200) {
                    this.discard();
                    return;
                }
            } else {
                this.life = 0;
            }

            float f = 0.0F;
            BlockPos bobberPos = this.blockPosition();
            FluidState fluidState = this.level().getFluidState(bobberPos);
            if (fluidState.is(FluidTags.LAVA)) {
                f = fluidState.getHeight(this.level(), bobberPos);
            }

            boolean isMainHandRod = f > 0.0F;
            if (this.currentState == FishHookState.FLYING) {
                if (this.hookedIn != null) {
                    this.setDeltaMovement(Vec3.ZERO);
                    this.currentState = FishHookState.HOOKED_IN_ENTITY;
                    return;
                }

                if (isMainHandRod) {
                    this.setDeltaMovement(this.getDeltaMovement().multiply(0.3D, 0.2D, 0.3D));
                    this.currentState = FishHookState.BOBBING;
                    return;
                }

                this.checkCollision();
            } else {
                if (this.currentState == FishHookState.HOOKED_IN_ENTITY) {
                    if (this.hookedIn != null) {
                        if (!this.hookedIn.isRemoved() && this.hookedIn.level().dimension() == this.level().dimension()) {
                            this.setPos(this.hookedIn.getX(), this.hookedIn.getY(0.8D), this.hookedIn.getZ());
                        } else {
                            this.setHookedEntity(null);
                            this.currentState = FishingHook.FishHookState.FLYING;
                        }
                    }
                    return;
                }

                if (this.currentState == FishHookState.BOBBING) {
                    Vec3 motion = this.getDeltaMovement();
                    double y = this.getY() + motion.y - (double) bobberPos.getY() - (double) f;
                    if (Math.abs(y) < 0.01D) {
                        y += Math.signum(y) * 0.1D;
                    }

                    this.setDeltaMovement(motion.x * 0.9D, motion.y - y * (double) this.random.nextFloat() * 0.2D, motion.z * 0.9D);
                    if (this.nibble <= 0 && this.timeUntilHooked <= 0) {
                        this.openWater = true;
                    } else {
                        this.openWater = this.openWater && this.outOfWaterTime < 10 && this.calculateOpenWater(bobberPos);
                    }

                    if (isMainHandRod) {
                        this.outOfWaterTime = Math.max(0, this.outOfWaterTime - 1);
                        if (this.biting) {
                            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.1D * (double) this.lavaTickRand.nextFloat() * (double) this.lavaTickRand.nextFloat(), 0.0D));
                        }

                        if (!this.level().isClientSide) {
                            this.catchingFish(bobberPos);
                        }
                    } else {
                        this.outOfWaterTime = Math.min(10, this.outOfWaterTime + 1);
                    }
                }
            }
            if (!fluidState.is(FluidTags.LAVA)) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.03D, 0.0D));
            }

            this.move(MoverType.SELF, this.getDeltaMovement());
            this.updateRotation();
            if (this.currentState == FishingHook.FishHookState.FLYING && (this.onGround() || this.horizontalCollision)) {
                this.setDeltaMovement(Vec3.ZERO);
            }

            this.setDeltaMovement(this.getDeltaMovement().scale(0.92D));
            this.reapplyPosition();
        }
    }

    @Override
    protected void catchingFish(BlockPos pos) { //Copied from vanilla. Only changed marked things
        ServerLevel serverLevel = (ServerLevel) this.level();
        int delay = 1;
        BlockPos posUp = pos.above();
        if (this.random.nextFloat() < 0.25F && serverLevel.isRainingAt(posUp)) {
            ++delay;
        }

        if (this.random.nextFloat() < 0.5F && !serverLevel.canSeeSky(posUp)) {
            --delay;
        }

        if (this.nibble > 0) {
            --this.nibble;
            if (this.nibble <= 0) {
                this.timeUntilLured = 0;
                this.timeUntilHooked = 0;
                this.getEntityData().set(DATA_BITING, false);
            }
        } else if (this.timeUntilHooked > 0) {
            this.timeUntilHooked -= delay;
            if (this.timeUntilHooked > 0) {
                this.fishAngle += (float) this.random.triangle(0.0D, 9.188D);
                float angle = this.fishAngle * ((float) Math.PI / 180F);
                float sin = Mth.sin(angle);
                float cos = Mth.cos(angle);
                double x = this.getX() + (double) (sin * (float) this.timeUntilHooked * 0.1F);
                double y = ((float) Mth.floor(this.getBoundingBox().minY) + 1.0F);
                double z = this.getZ() + (double) (cos * (float) this.timeUntilHooked * 0.1F);
                FluidState fluidState = serverLevel.getFluidState(new BlockPos((int) x, (int) (y - 1.0D), (int) z)); //Replaced BlockState checks with fluidState checks
                float zOffset = sin * 0.04F; //Moved to be possible to use with both Lava & Water particles
                float xOffset = cos * 0.04F; //Moved to be possible to use with both Lava & Water particles
                if (fluidState.is(FluidTags.WATER)) { //Water check added
                    if (this.random.nextFloat() < 0.15F) {
                        serverLevel.sendParticles(ParticleTypes.BUBBLE, x, y - 0.10000000149011612D, z, 1, sin, 0.1D, cos, 0.0D);
                    }
                    serverLevel.sendParticles(ParticleTypes.FISHING, x, y, z, 0, xOffset, 0.01D, -zOffset, 1.0D);
                    serverLevel.sendParticles(ParticleTypes.FISHING, x, y, z, 0, -xOffset, 0.01D, zOffset, 1.0D);
                }
                if (fluidState.is(FluidTags.LAVA)) { //Lava added
                    if (this.random.nextFloat() < 0.15F) {
                        serverLevel.sendParticles(ParticleTypes.LAVA, x, y - 0.10000000149011612D, z, 1, sin, 0.1D, cos, 0.0D);
                    }
                    serverLevel.sendParticles(ParticleTypes.SMOKE, x, y, z, 0, xOffset, 0.01D, -zOffset, 1.0D);
                    serverLevel.sendParticles(ParticleTypes.SMOKE, x, y, z, 0, -xOffset, 0.01D, zOffset, 1.0D);
                }
                if (this.hasHook() && this.hook.getCatchSound() != null && this.getPlayerOwner() != null) { //Hook catch sound functionality
                    this.level().playSound(null, this.getPlayerOwner() != null ? this.getPlayerOwner().blockPosition() : this.blockPosition(), this.hook.getCatchSound(), this.getSoundSource(), 0.1F, 0.1F);
                }
            } else {
                Vec3 motion = this.getDeltaMovement();
                this.setDeltaMovement(motion.x, (-0.4F * Mth.nextFloat(this.random, 0.6F, 1.0F)), motion.z);
                double boundingBox = this.getBoundingBox().minY + 0.5D;
                if (serverLevel.getFluidState(this.blockPosition()).is(FluidTags.WATER)) { //Water check added
                    this.playSound(SoundEvents.FISHING_BOBBER_SPLASH, 0.25F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
                    serverLevel.sendParticles(ParticleTypes.BUBBLE, this.getX(), boundingBox, this.getZ(), (int) (1.0F + this.getBbWidth() * 20.0F), this.getBbWidth(), 0.0D, this.getBbWidth(), 0.2D);
                    serverLevel.sendParticles(ParticleTypes.FISHING, this.getX(), boundingBox, this.getZ(), (int) (1.0F + this.getBbWidth() * 20.0F), this.getBbWidth(), 0.0D, this.getBbWidth(), 0.2);
                }

                //Lava sound added
                if (serverLevel.getFluidState(this.blockPosition()).is(FluidTags.LAVA)) {
                    this.playSound(AquaSounds.BOBBER_LAND_IN_LAVA.get(), 1.00F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
                    serverLevel.sendParticles(ParticleTypes.LAVA, this.getX(), boundingBox, this.getZ(), (int) (1.0F + this.getBbWidth() * 20.0F), this.getBbWidth(), 0.0D, this.getBbWidth(), 0.2D);
                }
                if (this.hasHook() && this.hook.getMaxCatchable() > 0) { //Added check
                    this.nibble = Mth.nextInt(this.random, this.hook.getMinCatchable(), this.hook.getMaxCatchable());
                } else {
                    this.nibble = Mth.nextInt(this.random, 20, 40);
                }
                this.getEntityData().set(DATA_BITING, true);
            }
        } else if (this.timeUntilLured > 0) {
            this.timeUntilLured -= delay;
            float angle = 0.15F;
            if (this.timeUntilLured < 20) {
                angle = (float) ((double) angle + (double) (20 - this.timeUntilLured) * 0.05F);
            } else if (this.timeUntilLured < 40) {
                angle = (float) ((double) angle + (double) (40 - this.timeUntilLured) * 0.02F);
            } else if (this.timeUntilLured < 60) {
                angle = (float) ((double) angle + (double) (60 - this.timeUntilLured) * 0.01F);
            }

            if (this.random.nextFloat() < angle) {
                float sin = Mth.nextFloat(this.random, 0.0F, 360.0F) * ((float) Math.PI / 180F);
                float cos = Mth.nextFloat(this.random, 25.0F, 60.0F);
                double x = this.getX() + (double) (Mth.sin(sin) * cos * 0.1F);
                double y = ((float) Mth.floor(this.getY()) + 1.0F);
                double z = this.getZ() + (double) (Mth.cos(sin) * cos * 0.1F);
                FluidState fluidState = serverLevel.getFluidState(new BlockPos((int) x, (int) (y - 1.0D), (int) z)); //Replaced BlockState check, with a FluidState check
                if (fluidState.is(FluidTags.WATER)) { //Check tag, instead of only water block
                    serverLevel.sendParticles(ParticleTypes.SPLASH, x, y, z, 2 + this.random.nextInt(2), 0.10000000149011612D, 0.0D, 0.10000000149011612D, 0.0D);
                }
            }

            if (this.timeUntilLured <= 0) {
                this.fishAngle = Mth.nextFloat(this.random, 0.0F, 360.0F);
                this.timeUntilHooked = Mth.nextInt(this.random, 20, 80);
            }
        } else {
            this.timeUntilLured = Mth.nextInt(this.random, 100, 600);
            this.timeUntilLured -= this.lureSpeed * 20 * 5;
        }
    }

    @Override
    public void lavaHurt() {
        if (!this.hasHook() || this.hasHook() && !this.hook.getFluids().contains(FluidTags.LAVA)) {
            super.lavaHurt();
        }
    }

    @Override
    public boolean displayFireAnimation() {
        return (this.hasHook() && !this.hook.getFluids().contains(FluidTags.LAVA)) && super.displayFireAnimation();
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        Player player = this.getPlayerOwner();
        if (player != null) {
            buffer.writeUUID(player.getUUID());
        }
        buffer.writeInt(this.luck);
        buffer.writeUtf(this.hook.getName() == null ? "" : this.hook.getName());
        buffer.writeItem(this.fishingLine);
        buffer.writeItem(this.bobber);
        buffer.writeItem(this.fishingRod);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
    }
}