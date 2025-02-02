package com.teammetallurgy.aquaculture.client.renderer.entity.state;

import com.teammetallurgy.aquaculture.entity.FishType;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;

public class AquaFishRenderState extends LivingEntityRenderState {
    public FishType fishType;
    public ResourceLocation byName;
}
