package com.teammetallurgy.aquaculture.client.renderer.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;

public class FishLargeModel<T extends Entity> extends SegmentedModel<T> { //Based on cod
    private final ModelRenderer head;
    private final ModelRenderer topFin;
    private final ModelRenderer tail;
    private final ModelRenderer leftFin;
    private final ModelRenderer body;
    private final ModelRenderer rightFin;
    private final ModelRenderer headFront;
    private final ModelRenderer bottomFin;

    public FishLargeModel() {
        this.textureWidth = 32;
        this.textureHeight = 32;
        this.bottomFin = new ModelRenderer(this, 20, 6);
        this.bottomFin.setRotationPoint(0.0F, 26.0F, 0.0F);
        this.bottomFin.addBox(0.0F, -1.0F, 0.0F, 0, 2, 6, 0.0F);
        this.head = new ModelRenderer(this, 11, 0);
        this.head.setRotationPoint(0.0F, 22.0F, 0.0F);
        this.head.addBox(-1.0F, -2.0F, -3.0F, 2, 4, 3, 0.0F);
        this.headFront = new ModelRenderer(this, 0, 0);
        this.headFront.setRotationPoint(0.0F, 22.0F, -3.0F);
        this.headFront.addBox(-1.0F, -2.0F, -1.0F, 2, 3, 1, 0.0F);
        this.leftFin = new ModelRenderer(this, 22, 4);
        this.leftFin.setRotationPoint(1.0F, 24.0F, 0.0F);
        this.leftFin.addBox(0.0F, 0.0F, -1.0F, 2, 0, 2, 0.0F);
        this.leftFin.rotateAngleZ = 0.7853981633974483F;
        this.rightFin = new ModelRenderer(this, 26, 4);
        this.rightFin.setRotationPoint(-1.0F, 24.0F, 0.0F);
        this.rightFin.addBox(-2.0F, 0.0F, -1.0F, 2, 0, 2, 0.0F);
        this.rightFin.rotateAngleZ = -0.7853981633974483F;
        this.topFin = new ModelRenderer(this, 18, -7);
        this.topFin.setRotationPoint(0.0F, 19.0F, 0.0F);
        this.topFin.addBox(0.0F, -1.0F, -1.0F, 0, 2, 7, 0.0F);
        this.tail = new ModelRenderer(this, 21, 2);
        this.tail.setRotationPoint(0.0F, 21.5F, 7.0F);
        this.tail.addBox(0.0F, -2.0F, 0.0F, 0, 5, 5, 0.0F);
        this.body = new ModelRenderer(this, 0, 1);
        this.body.setRotationPoint(0.0F, 22.0F, 0.0F);
        this.body.addBox(-1.0F, -2.0F, 0.0F, 2, 5, 7, 0.0F);
    }

    @Override
    @Nonnull
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(this.head, this.topFin, this.tail, this.leftFin, this.body, this.rightFin, this.headFront, this.bottomFin);
    }

    @Override
    public void setRotationAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float movement = 1.0F;
        if (!entity.isInWater()) {
            movement = 1.5F;
        }
        this.tail.rotateAngleY = -movement * 0.45F * MathHelper.sin(0.6F * ageInTicks);
    }
}