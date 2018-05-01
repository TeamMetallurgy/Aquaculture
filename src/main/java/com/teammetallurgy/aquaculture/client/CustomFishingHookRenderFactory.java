package com.teammetallurgy.aquaculture.client;

import com.teammetallurgy.aquaculture.handlers.EntityCustomFishHook;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class CustomFishingHookRenderFactory implements IRenderFactory<EntityCustomFishHook> {

    @Override
    public Render<? super EntityCustomFishHook> createRenderFor(RenderManager manager) {
        return new RenderCustomFishinghook(manager);
    }
}