package com.teammetallurgy.aquaculture.client.model;

import com.teammetallurgy.aquaculture.client.renderer.blockentity.state.TackleBoxRenderState;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;

public class TackleBoxModel extends Model<TackleBoxRenderState> {
    private final ModelPart base;
    private final ModelPart lid;

    public TackleBoxModel(ModelPart root) {
        super(root, RenderType::entityCutout);
        this.base = root.getChild("base");
        this.lid = root.getChild("lid");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        PartDefinition lid = partDefinition.addOrReplaceChild("lid", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -3.0F, -8.0F, 14, 3, 8), PartPose.offset(7.0F, 12.0F, 4.0F));

        partDefinition.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 11).addBox(0.0F, -6.0F, -8.0F, 14, 6, 8), PartPose.offset(0.0F, 18.0F, 4.0F));
        lid.addOrReplaceChild("handle", CubeListBuilder.create().texOffs(36, 0).addBox(-2.0F, -4.0F, -5.0F, 4, 1, 2), PartPose.offset(0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(meshDefinition, 64, 32);
    }

    @Override
    public void setupAnim(TackleBoxRenderState renderState) {
        float openness = renderState.openness;
        openness = 1.0F - openness;
        openness = 1.0F - openness * openness * openness;

        this.lid.xRot = -(openness * (float) (Math.PI / 2));
    }
}