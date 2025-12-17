package com.teammetallurgy.aquaculture.client.renderer.entity.state;

import com.teammetallurgy.aquaculture.api.fishing.Hook;
import net.minecraft.client.renderer.entity.state.FishingHookRenderState;
import net.minecraft.world.item.ItemStack;

public class AquaFishingHookRenderState extends FishingHookRenderState {
    public boolean hasBobber;
    public boolean hasHook;
    public ItemStack bobber;
    public ItemStack fishingLine;
    public Hook hook;

    public AquaFishingHookRenderState() {}
}
