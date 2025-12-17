package com.teammetallurgy.aquaculture.entity.ai.goal;

import com.mojang.datafixers.DataFixUtils;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.fish.AbstractSchoolingFish;

import java.util.List;
import java.util.function.Predicate;

public class FollowTypeSchoolLeaderGoal extends Goal {
    private final AbstractSchoolingFish taskOwner;
    private int navigateTimer;
    private int cooldown;

    public FollowTypeSchoolLeaderGoal(AbstractSchoolingFish fishEntity) {
        this.taskOwner = fishEntity;
        this.cooldown = this.getNewCooldown(fishEntity);
    }

    private int getNewCooldown(AbstractSchoolingFish fishEntity) {
        return 200 + fishEntity.getRandom().nextInt(200) % 20;
    }

    @Override
    public boolean canUse() {
        if (this.taskOwner.hasFollowers()) {
            return false;
        } else if (this.taskOwner.isFollower()) {
            return true;
        } else if (this.cooldown > 0) {
            --this.cooldown;
            return false;
        } else {
            this.cooldown = this.getNewCooldown(this.taskOwner);
            Predicate<AbstractSchoolingFish> predicate = (fishEntity) -> fishEntity.getType() == this.taskOwner.getType() && (fishEntity.canBeFollowed() || !fishEntity.isFollower());
            List<? extends AbstractSchoolingFish> schoolList = this.taskOwner.level().getEntitiesOfClass(this.taskOwner.getClass(), this.taskOwner.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), predicate);
            AbstractSchoolingFish fishEntity = DataFixUtils.orElse(schoolList.stream().filter(AbstractSchoolingFish::canBeFollowed).findAny(), this.taskOwner);
            fishEntity.addFollowers(schoolList.stream().filter((schoolFish) -> !schoolFish.isFollower()));
            return this.taskOwner.isFollower();
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.taskOwner.isFollower() && this.taskOwner.inRangeOfLeader();
    }

    @Override
    public void start() {
        this.navigateTimer = 0;
    }

    @Override
    public void stop() {
        if (this.taskOwner != null) {
            this.taskOwner.stopFollowing();
        }
    }

    @Override
    public void tick() {
        if (--this.navigateTimer <= 0) {
            this.navigateTimer = 10;
            this.taskOwner.pathToLeader();
        }
    }
}