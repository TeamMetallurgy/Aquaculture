package com.teammetallurgy.aquaculture.entity;

import com.teammetallurgy.aquaculture.api.AquacultureAPI;
import com.teammetallurgy.aquaculture.init.AquaEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.pathfinder.PathType;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TurtleLandEntity extends Animal {
    private static final EntityDimensions BABY_DIMENSIONS = AquaEntities.BOX_TURTLE.get().getDimensions().scale(0.5F).withEyeHeight(0.175F);

    public TurtleLandEntity(EntityType<? extends Animal> entityType, Level world) {
        super(entityType, world);
        this.moveControl = new TurtleLandMovementController(this);
        this.setPathfindingMalus(PathType.WATER, 0.2F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new TurtleLandSwimGoal());
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.2D));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.05D));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.15D, this::isFood, false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(5, new GetOutOfWaterGoal(this));
        this.goalSelector.addGoal(6, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.MOVEMENT_SPEED, 0.1D).add(Attributes.ARMOR, 1.5D);
    }

    @Override
    public boolean isFood(@Nonnull ItemStack stack) {
        return stack.is(AquacultureAPI.Tags.TURTLE_EDIBLE);
    }

    @Override
    @Nullable
    public AgeableMob getBreedOffspring(@Nonnull ServerLevel world, @Nonnull AgeableMob ageableMob) {
        return (AgeableMob) this.getType().create(this.level(), EntitySpawnReason.BREEDING);
    }

    @Override
    public boolean canDrownInFluidType(@Nonnull FluidType type) {
        return type == NeoForgeMod.WATER_TYPE.value() ? false : super.canDrownInFluidType(type);
    }

    @Override
    protected float getWaterSlowDown() {
        return 1.0F;
    }
    public class TurtleLandSwimGoal extends FloatGoal {

        public TurtleLandSwimGoal() {
            super(TurtleLandEntity.this);
        }

        @Override
        public boolean canUse() {
            return TurtleLandEntity.this.isInWater() && TurtleLandEntity.this.getFluidHeight(FluidTags.WATER) > 0.25D * 0.55D || TurtleLandEntity.this.isInLava();
        }
    }

    static class TurtleLandMovementController extends MoveControl {
        private final TurtleLandEntity turtle;

        TurtleLandMovementController(TurtleLandEntity turtle) {
            super(turtle);
            this.turtle = turtle;
        }

        @Override
        public void tick() {
            super.tick();
            this.updateSpeed();
        }

        private void updateSpeed() {
            if (this.turtle.isInWater()) {
                if (this.turtle.isBaby()) {
                    this.turtle.setSpeed(0.2F);
                } else {
                    this.turtle.setSpeed(0.18F);
                }
            } else if (this.turtle.onGround()) {
                this.turtle.setSpeed(0.1F);
            }
        }
    }

    static class GetOutOfWaterGoal extends MoveToBlockGoal {
        private final TurtleLandEntity turtle;

        private GetOutOfWaterGoal(TurtleLandEntity turtle) {
            super(turtle, turtle.isBaby() ? 1.4D : 1.2D, 24);
            this.turtle = turtle;
            this.verticalSearchStart = -1;
        }

        @Override
        public void tick() {
            super.tick();
        }

        @Override
        public boolean canContinueToUse() {
            return this.turtle.isInWater() && this.tryTicks <= 1200 && this.isValidTarget(this.turtle.level(), this.blockPos);
        }

        @Override
        public boolean canUse() {
            return this.turtle.isInWater() && super.canUse();
        }

        @Override
        protected boolean isValidTarget(LevelReader reader, @Nonnull BlockPos pos) {
            Block block = reader.getBlockState(pos).getBlock();
            return !(block instanceof LiquidBlock);
        }
    }
}