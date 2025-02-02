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

public class FishCathfishModel extends FishBaseModel { //Based on Cod
    private final ModelPart head;
    private final ModelPart headFront;
    private final ModelPart body;
    private final ModelPart topFin;
    private final ModelPart leftFin;
    private final ModelPart rightFin;
    private final ModelPart bottomFin;
    private final ModelPart tail;
    private final ModelPart leftWhisker;
    private final ModelPart rightWhisker;

    public FishCathfishModel(ModelPart part) {
        super(part);
        this.head = part.getChild("head");
        this.headFront = part.getChild("head_front");
        this.body = part.getChild("body");
        this.topFin = part.getChild("top_fin");
        this.leftFin = part.getChild("left_fin");
        this.rightFin = part.getChild("right_fin");
        this.bottomFin = part.getChild("bottom_fin");
        this.tail = part.getChild("tail");
        this.leftWhisker = part.getChild("left_whisker");
        this.rightWhisker = part.getChild("right_whisker");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition modelDefinition = new MeshDefinition();
        PartDefinition def = modelDefinition.getRoot();
        def.addOrReplaceChild("head", CubeListBuilder.create().texOffs(11, 0).addBox(-1.0F, -2.0F, -3.0F, 4, 3, 3), PartPose.offset(-1.0F, 22.5F, 0.0F));
        def.addOrReplaceChild("head_front", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2, 3, 1), PartPose.offset(0.0F, 22.5F, -3.0F));
        def.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, 0.0F, 2, 4, 7), PartPose.offset(0.0F, 22.0F, 0.0F));
        def.addOrReplaceChild("top_fin", CubeListBuilder.create().texOffs(20, 7).addBox(0.0F, -1.0F, 0.0F, 0, 2, 6), PartPose.offset(0.0F, 19.0F, 0.0F));
        def.addOrReplaceChild("left_fin", CubeListBuilder.create().texOffs(26, 2).addBox(0.0F, 0.0F, -1.0F, 2, 0, 2), PartPose.offsetAndRotation(1.0F, 21.9F, 1.5F, -1.5707963267948966F, 0.0F, 0.7853981633974483F));
        def.addOrReplaceChild("right_fin", CubeListBuilder.create().texOffs(26, 4).addBox(-2.0F, 0.0F, -1.0F, 2, 0, 2), PartPose.offsetAndRotation(-1.0F, 22.0F, 1.0F, -1.5707963267948966F, 0.0F, -0.7853981633974483F));
        def.addOrReplaceChild("bottom_fin", CubeListBuilder.create().texOffs(20, 5).addBox(0.0F, -1.0F, 0.0F, 0, 2, 6), PartPose.offset(0.0F, 25.0F, 0.0F));
        def.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(22, 3).addBox(0.0F, -2.0F, 0.0F, 0, 4, 4), PartPose.offset(0.0F, 22.0F, 7.0F));
        def.addOrReplaceChild("left_whisker", CubeListBuilder.create().texOffs(28, 15).addBox(0.0F, -1.0F, -0.5F, 2, 2, 0), PartPose.offset(2.0F, 22.5F, -2.0F));
        def.addOrReplaceChild("right_whisker", CubeListBuilder.create().texOffs(28, 17).addBox(-2.0F, -1.0F, -0.5F, 2, 2, 0), PartPose.offset(-2.0F, 22.5F, -2.0F));
        return LayerDefinition.create(modelDefinition, 32, 32);
    }

    @Override
    public void setupAnim(@Nonnull AquaFishRenderState renderState) {
        super.setupAnim(renderState);
        float f = renderState.isInWater ? 1.0F : 1.5F;
        this.tail.yRot = -f * 0.45F * Mth.sin(0.6F * renderState.ageInTicks);
    }
}