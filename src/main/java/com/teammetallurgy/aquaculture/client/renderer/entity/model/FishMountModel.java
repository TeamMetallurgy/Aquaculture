package com.teammetallurgy.aquaculture.client.renderer.entity.model;


import com.teammetallurgy.aquaculture.client.renderer.entity.state.FishMountRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class FishMountModel<T extends Entity> extends EntityModel<FishMountRenderState> {
    private final ModelPart base;
    private final ModelPart frame;

    public FishMountModel(ModelPart root) {
        super(root);
        this.base = root.getChild("base");
        this.frame = this.base.getChild("frame");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition base = partdefinition.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 2).addBox(-10.01F, -1.01F, -2.01F, 8.02F, 1.02F, 1.02F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-13.0F, -9.0F, -1.0F, 14.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, 20.0F, 9.0F));

        PartDefinition frame = base.addOrReplaceChild("frame", CubeListBuilder.create().texOffs(2, 1).addBox(1.0F, -11.0F, -1.0F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(12, 1).addBox(-14.0F, -11.0F, -1.0F, 1.0F, 10.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 3).addBox(-13.0F, -11.0F, -1.0F, 14.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 7).addBox(-13.0F, -2.0F, -1.0F, 14.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, -1.0F));

        return LayerDefinition.create(meshdefinition, 16, 16);
    }
}