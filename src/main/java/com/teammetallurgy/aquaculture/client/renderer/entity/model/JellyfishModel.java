package com.teammetallurgy.aquaculture.client.renderer.entity.model;

import com.teammetallurgy.aquaculture.client.renderer.entity.state.AquaFishRenderState;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

import javax.annotation.Nonnull;

public class JellyfishModel extends FishBaseModel { //TODO Test is transparency works
    private final ModelPart head;
    private final ModelPart tentaclesMain;
    private final ModelPart tentaclesLeft;
    private final ModelPart tentaclesRight;
    private final ModelPart frill;
    private final ModelPart heart;

    public JellyfishModel(ModelPart part) {
        super(part);
        this.head = part.getChild("head");
        this.tentaclesMain = part.getChild("tentacles_main");
        this.tentaclesLeft = part.getChild("tentacles_left");
        this.tentaclesRight = part.getChild("tentacles_right");
        this.frill = part.getChild("frill");
        this.heart = part.getChild("heart");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition modelDefinition = new MeshDefinition();
        PartDefinition def = modelDefinition.getRoot();
        def.addOrReplaceChild("head", CubeListBuilder.create().texOffs(14, 0).addBox(-2.5F, -2.5F, -5.0F, 5, 5, 5), PartPose.offset(0.0F, 21.0F, -3.0F));
        def.addOrReplaceChild("tentacles_main", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -3.0F, 0.0F, 0, 6, 14), PartPose.offset(0.0F, 21.0F, -3.5F));
        def.addOrReplaceChild("tentacles_left", CubeListBuilder.create().texOffs(0, 8).addBox(2.0F, -3.0F, 0.0F, 0, 6, 12), PartPose.offset(0.0F, 21.0F, -3.5F));
        def.addOrReplaceChild("tentacles_right", CubeListBuilder.create().texOffs(0, 14).addBox(-2.0F, -3.0F, 0.0F, 0, 6, 12), PartPose.offset(0.0F, 21.0F, -3.5F));
        def.addOrReplaceChild("frill", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -3.0F, 0.0F, 6, 6, 1), PartPose.offset(0.0F, 21.0F, -5.0F));
        def.addOrReplaceChild("heart", CubeListBuilder.create().texOffs(34, 0).addBox(-1.5F, -1.5F, -2.0F, 3, 3, 3), PartPose.offset(0.0F, 21.0F, -3.0F));
        return LayerDefinition.create(modelDefinition, 64, 32);
    }

    @Override
    public void setupAnim(@Nonnull AquaFishRenderState renderState) {
        float stillMovement = 0.1F;
        if (!renderState.isInWater) {
            stillMovement = 0.05F;
        }

        float walkAnimationPos = renderState.walkAnimationPos;
        float walkAnimationSpeed = renderState.walkAnimationSpeed;
        this.tentaclesLeft.yRot = -stillMovement * 0.25F * Mth.sin(0.3F * renderState.ageInTicks) + Mth.cos(walkAnimationPos * 0.4662F) * 0.5F * walkAnimationSpeed;
        this.tentaclesMain.yRot = -stillMovement * 0.25F * Mth.sin(0.3F * renderState.ageInTicks) + Mth.cos(walkAnimationPos * 0.4662F) * 0.5F * walkAnimationSpeed;
        this.tentaclesRight.yRot = -stillMovement * 0.25F * Mth.sin(0.3F * renderState.ageInTicks) + Mth.cos(walkAnimationPos * 0.4662F) * 0.5F * walkAnimationSpeed;
    }
}