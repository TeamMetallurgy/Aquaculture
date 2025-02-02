package com.teammetallurgy.aquaculture.client.renderer.entity.model;

import com.teammetallurgy.aquaculture.client.renderer.entity.state.AquaFishRenderState;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

import javax.annotation.Nonnull;

public class AquaTropicalFishBModel extends FishBaseModel {
    private final ModelPart tail = root.getChild("tail");

    public AquaTropicalFishBModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer(CubeDeformation cubeDeformation) {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 20).addBox(-1.0F, -3.0F, -3.0F, 2.0F, 6.0F, 6.0F, cubeDeformation), PartPose.offset(0.0F, 19.0F, 0.0F));
        partdefinition.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(21, 16).addBox(0.0F, -3.0F, 0.0F, 0.0F, 6.0F, 5.0F, cubeDeformation), PartPose.offset(0.0F, 19.0F, 3.0F));
        partdefinition.addOrReplaceChild("right_fin", CubeListBuilder.create().texOffs(2, 16).addBox(-2.0F, 0.0F, 0.0F, 2.0F, 2.0F, 0.0F, cubeDeformation), PartPose.offsetAndRotation(-1.0F, 20.0F, 0.0F, 0.0F, 0.7853982F, 0.0F));
        partdefinition.addOrReplaceChild("left_fin", CubeListBuilder.create().texOffs(2, 12).addBox(0.0F, 0.0F, 0.0F, 2.0F, 2.0F, 0.0F, cubeDeformation), PartPose.offsetAndRotation(1.0F, 20.0F, 0.0F, 0.0F, -0.7853982F, 0.0F));
        partdefinition.addOrReplaceChild("top_fin", CubeListBuilder.create().texOffs(20, 11).addBox(0.0F, -4.0F, 0.0F, 0.0F, 4.0F, 6.0F, cubeDeformation), PartPose.offset(0.0F, 16.0F, -3.0F));
        partdefinition.addOrReplaceChild("bottom_fin", CubeListBuilder.create().texOffs(20, 21).addBox(0.0F, 0.0F, 0.0F, 0.0F, 4.0F, 6.0F, cubeDeformation), PartPose.offset(0.0F, 22.0F, -3.0F));
        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(@Nonnull AquaFishRenderState renderState) {
        super.setupAnim(renderState);
        float f = renderState.isInWater ? 1.0F : 1.5F;
        this.tail.yRot = -f * 0.45F * Mth.sin(0.6F * renderState.ageInTicks);
    }
}
