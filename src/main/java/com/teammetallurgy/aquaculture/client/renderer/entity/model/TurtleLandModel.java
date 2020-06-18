package com.teammetallurgy.aquaculture.client.renderer.entity.model;

import com.google.common.collect.ImmutableList;
import com.teammetallurgy.aquaculture.entity.TurtleLandEntity;
import net.minecraft.client.renderer.entity.model.QuadrupedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;

public class TurtleLandModel<T extends TurtleLandEntity> extends QuadrupedModel<T> {
    private final ModelRenderer tail;
    private final ModelRenderer shellTop;
    private final ModelRenderer belly;

    public TurtleLandModel() {
        super(0, 0.0F, false, 1.1F, 1.5F, 2.0F, 2.0F, 24);
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.headModel = new ModelRenderer(this, 24, 0);
        this.headModel.setRotationPoint(0.0F, 22.2F, -4.0F);
        this.headModel.addBox(-1.0F, -1.0F, -0.5F, 2, 2, 2, -0.2F);
        this.body = new ModelRenderer(this, 0, 0);
        this.body.setRotationPoint(0.0F, 22.0F, 0.0F);
        this.body.addBox(-2.5F, -1.0F, -3.0F, 5, 2, 6, 0.0F);
        this.belly = new ModelRenderer(this, 0, 14);
        this.belly.setRotationPoint(0.0F, 1.0F, 0.0F);
        this.belly.addBox(-2.0F, -0.5F, -2.5F, 4, 1, 5, 0.0F);
        this.shellTop = new ModelRenderer(this, 0, 8);
        this.shellTop.setRotationPoint(0.0F, -1.5F, 0.0F);
        this.shellTop.addBox(-2.0F, -0.5F, -2.5F, 4, 1, 5, 0.0F);
        this.legBackRight = new ModelRenderer(this, 16, 3);
        this.legBackRight.setRotationPoint(-2.5F, 23.0F, 3.0F);
        this.legBackRight.addBox(-0.5F, -1.0F, -0.5F, 1, 2, 1, 0.1F);
        this.setRotateAngle(legBackRight, 0.5235987755982988F, 5.759586531581287F, 0.0F);
        this.legBackLeft = new ModelRenderer(this, 16, 0);
        this.legBackLeft.setRotationPoint(2.5F, 23.0F, -3.0F);
        this.legBackLeft.addBox(-0.5F, -1.0F, -0.5F, 1, 2, 1, 0.1F);
        this.setRotateAngle(legBackLeft, -0.5235987755982988F, -0.5235987755982988F, 0.0F);
        this.legFrontRight = new ModelRenderer(this, 20, 3);
        this.legFrontRight.setRotationPoint(-2.5F, 23.0F, -3.0F);
        this.legFrontRight.addBox(-0.5F, -1.0F, -0.5F, 1, 2, 1, 0.1F);
        this.setRotateAngle(legFrontRight, -0.5235987755982988F, 0.5235987755982988F, 0.0F);
        this.legFrontLeft = new ModelRenderer(this, 20, 0);
        this.legFrontLeft.setRotationPoint(2.5F, 23.0F, 3.0F);
        this.legFrontLeft.addBox(-0.5F, -1.0F, -0.5F, 1, 2, 1, 0.1F);
        this.setRotateAngle(legFrontLeft, 0.5235987755982988F, 0.5235987755982988F, 0.0F);
        this.tail = new ModelRenderer(this, 26, 0);
        this.tail.setRotationPoint(0.0F, 22.5F, 3.2F);
        this.tail.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, -0.2F);
        this.body.addChild(this.belly);
        this.body.addChild(this.shellTop);
    }

    @Override
    @Nonnull
    protected Iterable<ModelRenderer> getBodyParts() {
        return ImmutableList.of(this.body, this.legBackRight, this.legBackLeft, this.legFrontRight, this.legFrontLeft, this.tail);
    }

    @Override
    public void setRotationAngles(@Nonnull T turtle, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.headModel.rotateAngleX = headPitch * 0.017453292F;
        this.headModel.rotateAngleY = netHeadYaw * 0.017453292F;
        this.legBackRight.rotateAngleX = 0.5235987755982988F + (MathHelper.cos(limbSwing * 5.0F) * 1.4F * limbSwingAmount);
        this.legBackLeft.rotateAngleX = -0.5235987755982988F + -(MathHelper.cos(limbSwing * 5.0F) * 1.4F * limbSwingAmount);
        this.legFrontRight.rotateAngleX = -0.5235987755982988F + -(MathHelper.cos(limbSwing * 5.0F) * 1.4F * limbSwingAmount);
        this.legFrontLeft.rotateAngleX = 0.5235987755982988F + (MathHelper.cos(limbSwing * 5.0F) * 1.4F * limbSwingAmount);
        this.tail.rotateAngleY = MathHelper.cos(limbSwing * 0.4662F) * 0.6F * limbSwingAmount;
    }

    private void setRotateAngle(ModelRenderer ModelRenderer, float x, float y, float z) {
        ModelRenderer.rotateAngleX = x;
        ModelRenderer.rotateAngleY = y;
        ModelRenderer.rotateAngleZ = z;
    }
}