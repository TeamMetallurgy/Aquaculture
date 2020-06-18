package com.teammetallurgy.aquaculture.client.renderer.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;

public class FishLongnoseModel <T extends Entity> extends SegmentedModel<T> { //Based on Salmon
    private final ModelRenderer nose;
    private final ModelRenderer finRight;
    private final ModelRenderer finLeft;
    private final ModelRenderer bodyFront;
    private final ModelRenderer bodyRear;
    private final ModelRenderer finTopFront;
    private final ModelRenderer finFrontBottom;
    private final ModelRenderer finRearBottom;
    private final ModelRenderer finTopRear;
    private final ModelRenderer tail;

    public FishLongnoseModel() {
        this.textureWidth = 32;
        this.textureHeight = 32;
        this.bodyRear = new ModelRenderer(this, 0, 13);
        this.bodyRear.setRotationPoint(0.0F, 20.0F, 8.0F);
        this.bodyRear.addBox(-1.5F, -2.5F, 0.0F, 3, 4, 8, 0.0F);
        this.finRight = new ModelRenderer(this, -4, 0);
        this.finRight.setRotationPoint(-1.5F, 21.5F, 4.0F);
        this.finRight.addBox(-2.0F, 0.0F, 0.0F, 2, 0, 2, 0.0F);
        this.finRight.rotateAngleZ = -0.7853981633974483F;
        this.nose = new ModelRenderer(this, 22, 0);
        this.nose.setRotationPoint(0.0F, 20.5F, 0.0F);
        this.nose.addBox(-1.0F, -2.0F, -3.0F, 2, 2, 3, 0.0F);
        this.finTopFront = new ModelRenderer(this, 2, 1);
        this.finTopFront.setRotationPoint(0.0F, -4.5F, 5.0F);
        this.finTopFront.addBox(0.0F, 0.0F, 0.0F, 0, 2, 3, 0.0F);
        this.finTopRear = new ModelRenderer(this, 0, 2);
        this.finTopRear.setRotationPoint(0.0F, -4.5F, -1.0F);
        this.finTopRear.addBox(0.0F, 0.0F, 1.0F, 0, 2, 4, 0.0F);
        this.finLeft = new ModelRenderer(this, 0, 0);
        this.finLeft.setRotationPoint(1.5F, 21.5F, 4.0F);
        this.finLeft.addBox(0.0F, 0.0F, 0.0F, 2, 0, 2, 0.0F);
        this.finLeft.rotateAngleZ = 0.7853981633974483F;
        this.finFrontBottom = new ModelRenderer(this, 0, 25);
        this.finFrontBottom.setRotationPoint(0.0F, 1.5F, 5.0F);
        this.finFrontBottom.addBox(0.0F, 0.0F, 0.0F, 0, 2, 3, 0.0F);
        this.tail = new ModelRenderer(this, 20, 10);
        this.tail.setRotationPoint(0.0F, 0.0F, 8.0F);
        this.tail.addBox(0.0F, -2.5F, 0.0F, 0, 4, 6, 0.0F);
        this.bodyFront = new ModelRenderer(this, 0, 0);
        this.bodyFront.setRotationPoint(0.0F, 20.0F, 0.0F);
        this.bodyFront.addBox(-1.5F, -2.5F, 0.0F, 3, 4, 8, 0.0F);
        this.finRearBottom = new ModelRenderer(this, 0, 22);
        this.finRearBottom.setRotationPoint(0.0F, 1.5F, -1.0F);
        this.finRearBottom.addBox(0.0F, 0.0F, 1.0F, 0, 2, 4, 0.0F);
        this.bodyFront.addChild(this.finTopFront);
        this.bodyFront.addChild(this.finFrontBottom);
        this.bodyRear.addChild(this.finTopRear);
        this.bodyRear.addChild(this.tail);
        this.bodyRear.addChild(this.finRearBottom);
    }

    @Override
    @Nonnull
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(this.bodyFront, this.bodyRear, this.nose, this.finRight, this.finLeft);
    }

    @Override
    public void setRotationAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float f = 1.0F;
        float f1 = 1.0F;
        if (!entity.isInWater()) {
            f = 1.3F;
            f1 = 1.7F;
        }
        float moveAmount = 0.15F; //Default for Salmon is 0.25F
        this.bodyRear.rotateAngleY = -f * moveAmount * MathHelper.sin(f1 * 0.6F * ageInTicks);
    }
}