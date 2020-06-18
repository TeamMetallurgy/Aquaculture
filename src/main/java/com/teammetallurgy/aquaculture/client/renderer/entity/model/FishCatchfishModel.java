package com.teammetallurgy.aquaculture.client.renderer.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;

public class FishCatchfishModel<T extends Entity> extends SegmentedModel<T> { //Based on Cod
    private final ModelRenderer head;
    private final ModelRenderer headFront;
    private final ModelRenderer body;
    private final ModelRenderer topFin;
    private final ModelRenderer leftFin;
    private final ModelRenderer rightFin;
    private final ModelRenderer bottomFin;
    private final ModelRenderer tail;
    private final ModelRenderer leftWhisker;
    private final ModelRenderer rightWhisker;

    public FishCatchfishModel() {
        this.textureWidth = 32;
        this.textureHeight = 32;
        this.tail = new ModelRenderer(this, 22, 3);
        this.tail.setRotationPoint(0.0F, 22.0F, 7.0F);
        this.tail.addBox(0.0F, -2.0F, 0.0F, 0, 4, 4, 0.0F);
        this.headFront = new ModelRenderer(this, 0, 0);
        this.headFront.setRotationPoint(0.0F, 22.5F, -3.0F);
        this.headFront.addBox(-1.0F, -2.0F, -1.0F, 2, 3, 1, 0.0F);
        this.rightFin = new ModelRenderer(this, 26, 4);
        this.rightFin.setRotationPoint(-1.0F, 22.0F, 1.0F);
        this.rightFin.addBox(-2.0F, 0.0F, -1.0F, 2, 0, 2, 0.0F);
        this.setRotateAngle(rightFin, -1.5707963267948966F, 0.0F, -0.7853981633974483F);
        this.leftFin = new ModelRenderer(this, 22, 4);
        this.leftFin.setRotationPoint(1.0F, 22.0F, 1.5F);
        this.leftFin.addBox(0.0F, 0.0F, -1.0F, 2, 0, 2, 0.0F);
        this.setRotateAngle(leftFin, -1.5707963267948966F, 0.0F, 0.7853981633974483F);
        this.topFin = new ModelRenderer(this, 20, 7);
        this.topFin.setRotationPoint(0.0F, 19.0F, 0.0F);
        this.topFin.addBox(0.0F, -1.0F, 0.0F, 0, 2, 6, 0.0F);
        this.bottomFin = new ModelRenderer(this, 20, 5);
        this.bottomFin.setRotationPoint(0.0F, 25.0F, 0.0F);
        this.bottomFin.addBox(0.0F, -1.0F, 0.0F, 0, 2, 6, 0.0F);
        this.head = new ModelRenderer(this, 11, 0);
        this.head.setRotationPoint(-1.0F, 22.5F, 0.0F);
        this.head.addBox(-1.0F, -2.0F, -3.0F, 4, 3, 3, 0.0F);
        this.rightWhisker = new ModelRenderer(this, 28, 17);
        this.rightWhisker.setRotationPoint(-1.0F, 0.0F, -2.0F);
        this.rightWhisker.addBox(-2.0F, -1.0F, -0.5F, 2, 2, 0, 0.0F);
        this.body = new ModelRenderer(this, 0, 0);
        this.body.setRotationPoint(0.0F, 22.0F, 0.0F);
        this.body.addBox(-1.0F, -2.0F, 0.0F, 2, 4, 7, 0.0F);
        this.leftWhisker = new ModelRenderer(this, 28, 15);
        this.leftWhisker.setRotationPoint(3.0F, 0.0F, -2.0F);
        this.leftWhisker.addBox(0.0F, -1.0F, -0.5F, 2, 2, 0, 0.0F);
        this.head.addChild(this.rightWhisker);
        this.head.addChild(this.leftWhisker);
    }

    @Override
    @Nonnull
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(this.head, this.headFront, this.body, this.topFin, this.leftFin, this.rightFin, this.bottomFin, this.tail);
    }

    @Override
    public void setRotationAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float movement = 1.0F;
        if (!entity.isInWater()) {
            movement = 1.5F;
        }
        this.tail.rotateAngleY = -movement * 0.45F * MathHelper.sin(0.6F * ageInTicks);
    }

    public void setRotateAngle(ModelRenderer ModelRenderer, float x, float y, float z) {
        ModelRenderer.rotateAngleX = x;
        ModelRenderer.rotateAngleY = y;
        ModelRenderer.rotateAngleZ = z;
    }
}