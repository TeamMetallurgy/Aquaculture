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

public class FishLongnoseModel extends FishBaseModel { //Based on Salmon
    private final ModelPart nose;
    private final ModelPart finRight;
    private final ModelPart finLeft;
    private final ModelPart bodyFront;
    private final ModelPart bodyRear;

    public FishLongnoseModel(ModelPart part) {
        super(part);
        this.nose = part.getChild("nose");
        this.finRight = part.getChild("fin_right");
        this.finLeft = part.getChild("fin_left");
        this.bodyFront = part.getChild("body_front");
        this.bodyRear = part.getChild("body_rear");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition modelDefinition = new MeshDefinition();
        PartDefinition def = modelDefinition.getRoot();
        PartDefinition front = def.addOrReplaceChild("body_front", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -2.5F, 0.0F, 3, 4, 8), PartPose.offset(0.0F, 20.0F, 0.0F));
        PartDefinition rear = def.addOrReplaceChild("body_rear", CubeListBuilder.create().texOffs(0, 13).addBox(-1.5F, -2.5F, 0.0F, 3, 4, 8), PartPose.offset(0.0F, 20.0F, 8.0F));

        def.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(22, 0).addBox(-1.0F, -2.0F, -3.0F, 2, 2, 3), PartPose.offset(0.0F, 20.5F, 0.0F));
        def.addOrReplaceChild("fin_right", CubeListBuilder.create().texOffs(-4, 0).addBox(-2.0F, 0.0F, 0.0F, 2, 0, 2), PartPose.offsetAndRotation(-1.5F, 21.5F, 4.0F, 0.0F, 0.0F, -0.7853981633974483F));
        def.addOrReplaceChild("fin_left", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 2, 0, 2), PartPose.offsetAndRotation(1.5F, 21.5F, 4.0F, 0.0F, 0.0F, 0.7853981633974483F));
        front.addOrReplaceChild("fin_top_front", CubeListBuilder.create().texOffs(2, 1).addBox(0.0F, 0.0F, 0.0F, 0, 2, 3), PartPose.offset(0.0F, -4.5F, 5.0F));
        front.addOrReplaceChild("fin_front_bottom", CubeListBuilder.create().texOffs(0, 25).addBox(0.0F, 0.0F, 0.0F, 0, 2, 3), PartPose.offset(0.0F, 1.5F, 5.0F));
        rear.addOrReplaceChild("fin_rear_bottom", CubeListBuilder.create().texOffs(0, 22).addBox(0.0F, 0.0F, 1.0F, 0, 2, 4), PartPose.offset(0.0F, 1.5F, -1.0F));
        rear.addOrReplaceChild("fin_top_rear", CubeListBuilder.create().texOffs(0, 2).addBox(0.0F, 0.0F, 1.0F, 0, 2, 4), PartPose.offset(0.0F, -4.5F, -1.0F));
        rear.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(20, 10).addBox(0.0F, -2.5F, 0.0F, 0, 4, 6), PartPose.offset(0.0F, 0.0F, 8.0F));
        return LayerDefinition.create(modelDefinition, 32, 32);
    }

    @Override
    public void setupAnim(@Nonnull AquaFishRenderState renderState) {
        super.setupAnim(renderState);
        float f = 1.0F;
        float f1 = 1.0F;
        if (!renderState.isInWater) {
            f = 1.3F;
            f1 = 1.7F;
        }

        float moveAmount = 0.15F; //Default for Salmon is 0.25F
        this.bodyRear.yRot = -f * moveAmount * Mth.sin(f1 * 0.6F * renderState.ageInTicks);
    }
}