package com.teammetallurgy.aquaculture.client.renderer.entity.model;

import net.minecraft.client.model.BabyModelTransform;
import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;

import javax.annotation.Nonnull;
import java.util.Set;

public class TurtleLandModel extends QuadrupedModel<LivingEntityRenderState> {
    public static final MeshTransformer BABY_TRANSFORMER = new BabyModelTransform(false, 1.1F, 1.5F, Set.of("head")); ////false, 1.1F, 1.5F, 2.0F, 2.0F, 24 //TODO Test
    private final ModelPart tail;
    private final ModelPart shellTop;
    private final ModelPart belly;

    public TurtleLandModel(ModelPart part) {
        super(part);
        this.tail = part.getChild("tail");
        this.shellTop = part.getChild("shell_top");
        this.belly = part.getChild("belly");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition modelDefinition = new MeshDefinition();
        PartDefinition def = modelDefinition.getRoot();
        def.addOrReplaceChild("head", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -1.0F, -0.5F, 2, 2, 2), PartPose.offset(0.0F, 22.2F, -4.0F));
        def.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -1.0F, -3.0F, 5, 2, 6), PartPose.offset(0.0F, 22.0F, 0.0F));
        def.addOrReplaceChild("belly", CubeListBuilder.create().texOffs(0, 14).addBox(-2.0F, -0.5F, -2.5F, 4, 1, 5), PartPose.offset(0.0F, 23.0F, 0.0F));
        def.addOrReplaceChild("shell_top", CubeListBuilder.create().texOffs(0, 8).addBox(-2.0F, -0.5F, -2.5F, 4, 1, 5), PartPose.offset(0.0F, 20.5F, 0.0F));
        def.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(16, 3).addBox(-0.5F, -1.0F, -0.5F, 1, 2, 1), PartPose.offsetAndRotation(-2.5F, 23.0F, 3.0F, 0.5235987755982988F, 5.759586531581287F, 0.0F));
        def.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(16, 0).addBox(-0.5F, -1.0F, -0.5F, 1, 2, 1), PartPose.offsetAndRotation(2.5F, 23.0F, -3.0F, -0.5235987755982988F, -0.5235987755982988F, 0.0F));
        def.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(20, 3).addBox(-0.5F, -1.0F, -0.5F, 1, 2, 1), PartPose.offsetAndRotation(-2.5F, 23.0F, -3.0F, -0.5235987755982988F, 0.5235987755982988F, 0.0F));
        def.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(20, 0).addBox(-0.5F, -1.0F, -0.5F, 1, 2, 1), PartPose.offsetAndRotation(2.5F, 23.0F, 3.0F, 0.5235987755982988F, 0.5235987755982988F, 0.0F));
        def.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(26, 0).addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1), PartPose.offset(0.0F, 22.5F, 3.2F));
        return LayerDefinition.create(modelDefinition, 64, 32);
    }

    @Override
    public void setupAnim(@Nonnull LivingEntityRenderState renderState) {
        float walkAnimationPos = renderState.walkAnimationPos;
        float walkAnimationSpeed = renderState.walkAnimationSpeed;
        this.head.xRot = renderState.xRot * 0.017453292F;
        this.head.yRot = renderState.yRot * 0.017453292F;
        this.rightHindLeg.xRot = 0.5235987755982988F + (Mth.cos(walkAnimationPos * 5.0F) * 1.4F * walkAnimationSpeed);
        this.leftHindLeg.xRot = -0.5235987755982988F + -(Mth.cos(walkAnimationPos * 5.0F) * 1.4F * walkAnimationSpeed);
        this.rightFrontLeg.xRot = -0.5235987755982988F + -(Mth.cos(walkAnimationPos * 5.0F) * 1.4F * walkAnimationSpeed);
        this.leftFrontLeg.xRot = 0.5235987755982988F + (Mth.cos(walkAnimationPos * 5.0F) * 1.4F * walkAnimationSpeed);
        this.tail.yRot = Mth.cos(walkAnimationPos * 0.4662F) * 0.6F * walkAnimationSpeed;
    }
}