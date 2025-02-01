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

public class FishMediumModel extends FishBaseModel { //Based on Cod
    private final ModelPart body;
    private final ModelPart finTop;
    private final ModelPart head;
    private final ModelPart headFront;
    private final ModelPart finRight;
    private final ModelPart finLeft;
    private final ModelPart tail;
    private final ModelPart bottomFin;

    public FishMediumModel(ModelPart part) {
        super(part);
        this.body = part.getChild("body");
        this.finTop = part.getChild("fin_top");
        this.head = part.getChild("head");
        this.headFront = part.getChild("head_front");
        this.finRight = part.getChild("fin_right");
        this.finLeft = part.getChild("fin_left");
        this.tail = part.getChild("tail");
        this.bottomFin = part.getChild("bottom_fin");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition modelDefinition = new MeshDefinition();
        PartDefinition def = modelDefinition.getRoot();
        def.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, 0.0F, 2, 4, 7), PartPose.offset(0.0F, 22.0F, 0.0F));
        def.addOrReplaceChild("fin_top", CubeListBuilder.create().texOffs(20, -6).addBox(0.0F, -1.0F, -1.0F, 0, 2, 6), PartPose.offset(0.0F, 19.0F, 0.0F));
        def.addOrReplaceChild("head", CubeListBuilder.create().texOffs(11, 0).addBox(-1.0F, -2.0F, -3.0F, 2, 4, 3), PartPose.offset(0.0F, 22.0F, 0.0F));
        def.addOrReplaceChild("head_front", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2, 3, 1), PartPose.offset(0.0F, 22.0F, -3.0F));
        def.addOrReplaceChild("fin_right", CubeListBuilder.create().texOffs(26, 4).addBox(-2.0F, 0.0F, -1.0F, 2, 0, 2), PartPose.offsetAndRotation(-1.0F, 23.0F, 0.0F, 0.0F, 0.0F, -0.7853981633974483F));
        def.addOrReplaceChild("fin_left", CubeListBuilder.create().texOffs(22, 4).addBox(0.0F, 0.0F, -1.0F, 2, 0, 2), PartPose.offsetAndRotation(1.0F, 23.0F, 0.0F, 0.0F, 0.0F, 0.7853981633974483F));
        def.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(22, 3).addBox(0.0F, -2.0F, 0.0F, 0, 4, 4), PartPose.offset(0.0F, 22.0F, 7.0F));
        def.addOrReplaceChild("bottom_fin", CubeListBuilder.create().texOffs(20, 5).addBox(0.0F, -1.0F, -1.0F, 0, 2, 6), PartPose.offset(0.0F, 25.0F, 0.0F));
        return LayerDefinition.create(modelDefinition, 32, 32);
    }

    @Override
    public void setupAnim(@Nonnull AquaFishRenderState renderState) {
        super.setupAnim(renderState);
        float f = renderState.isInWater ? 1.0F : 1.5F;
        this.tail.yRot = -f * 0.45F * Mth.sin(0.6F * renderState.ageInTicks);
    }
}