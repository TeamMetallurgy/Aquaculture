package com.teammetallurgy.aquaculture.client.renderer.entity.state;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public class FishMountRenderState extends EntityRenderState {
    public Direction direction;
    public ItemStack stack;
    public ResourceLocation byName;
    public Entity mountedFish;
    public EntityRenderState fishRenderState;

    public FishMountRenderState() {
        this.direction = Direction.NORTH;
        this.stack = ItemStack.EMPTY;
    }
}