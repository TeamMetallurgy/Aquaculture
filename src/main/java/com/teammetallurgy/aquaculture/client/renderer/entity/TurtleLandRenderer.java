package com.teammetallurgy.aquaculture.client.renderer.entity;

import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.client.ClientHandler;
import com.teammetallurgy.aquaculture.client.renderer.entity.model.TurtleLandModel;
import com.teammetallurgy.aquaculture.client.renderer.entity.state.TurtleLandRenderState;
import com.teammetallurgy.aquaculture.entity.TurtleLandEntity;
import com.teammetallurgy.aquaculture.init.AquaEntities;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

import javax.annotation.Nonnull;

public class TurtleLandRenderer extends AgeableMobRenderer<TurtleLandEntity, TurtleLandRenderState, TurtleLandModel> {
    private static final Identifier BOX_TURTLE = Identifier.fromNamespaceAndPath(Aquaculture.MOD_ID, "textures/entity/turtle/box_turtle.png");
    private static final Identifier ARRAU_TURTLE = Identifier.fromNamespaceAndPath(Aquaculture.MOD_ID, "textures/entity/turtle/arrau_turtle.png");
    private static final Identifier STARSHELL_TURTLE = Identifier.fromNamespaceAndPath(Aquaculture.MOD_ID, "textures/entity/turtle/starshell_turtle.png");

    public TurtleLandRenderer(EntityRendererProvider.Context context) {
        super(context, new TurtleLandModel(context.bakeLayer(ClientHandler.TURTLE_LAND_LAYER)), new TurtleLandModel(context.bakeLayer(ClientHandler.TURTLE_LAND_BABY_LAYER)), 0.25F);
    }

    @Override
    @Nonnull
    public TurtleLandRenderState createRenderState() {
        return new TurtleLandRenderState();
    }

    @Override
    @Nonnull
    public Identifier getTextureLocation(@Nonnull TurtleLandRenderState turtle) {
        if (AquaEntities.ARRAU_TURTLE.get().equals(turtle.type)) {
            return ARRAU_TURTLE;
        } else if (AquaEntities.STARSHELL_TURTLE.get().equals(turtle.type)) {
            return STARSHELL_TURTLE;
        } else {
            return BOX_TURTLE;
        }
    }

    @Override
    public void extractRenderState(@Nonnull TurtleLandEntity turtle, @Nonnull TurtleLandRenderState renderState, float partialTick) {
        super.extractRenderState(turtle, renderState, partialTick);
        renderState.type = turtle.getType();
    }
}