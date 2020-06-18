package com.teammetallurgy.aquaculture.client.renderer.entity;

import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.client.renderer.entity.model.TurtleLandModel;
import com.teammetallurgy.aquaculture.entity.TurtleLandEntity;
import com.teammetallurgy.aquaculture.init.AquaEntities;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class TurtleLandRenderer extends MobRenderer<TurtleLandEntity, TurtleLandModel<TurtleLandEntity>> {
    private static final ResourceLocation BOX_TURTLE = new ResourceLocation(Aquaculture.MOD_ID, "textures/entity/turtle/box_turtle.png");
    private static final ResourceLocation ARRAU_TURTLE = new ResourceLocation(Aquaculture.MOD_ID, "textures/entity/turtle/arrau_turtle.png");
    private static final ResourceLocation STARSHELL_TURTLE = new ResourceLocation(Aquaculture.MOD_ID, "textures/entity/turtle/starshell_turtle.png");

    public TurtleLandRenderer(EntityRendererManager manager) {
        super(manager, new TurtleLandModel<>(), 0.25F);
    }

    @Override
    @Nonnull
    public ResourceLocation getEntityTexture(@Nonnull TurtleLandEntity turtle) {
        if (AquaEntities.ARRAU_TURTLE.equals(turtle.getType())) {
            return ARRAU_TURTLE;
        } else if (AquaEntities.STARSHELL_TURTLE.equals(turtle.getType())) {
            return STARSHELL_TURTLE;
        } else {
            return BOX_TURTLE;
        }
    }
}