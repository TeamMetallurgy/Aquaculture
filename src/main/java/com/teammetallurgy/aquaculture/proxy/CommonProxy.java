package com.teammetallurgy.aquaculture.proxy;

import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.handlers.EntityCustomFishHook;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class CommonProxy {
    public void registerParticles() {
    }

    public void registerModelRenderers() {
    }

    public void registerItemModels() {
    }

    public void registerEntities() {
        EntityRegistry.registerModEntity(new ResourceLocation("aquaculture:custom_fish_hook"), EntityCustomFishHook.class, "CustomFishHook", 0, Aquaculture.instance, 64, 5, true);
    }
}
