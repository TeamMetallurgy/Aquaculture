package com.teammetallurgy.aquaculture.client.renderer.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.AbstractTropicalFishModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;

public class FishSmallModel<T extends Entity> extends AbstractTropicalFishModel<T> { //Based on TropicalFishA
    private final ModelRenderer finRight;
    private final ModelRenderer finTop;
    private final ModelRenderer tail;
    private final ModelRenderer finLeft;
    private final ModelRenderer body;
    private final ModelRenderer finBottom;

    public FishSmallModel() {
        this.textureWidth = 32;
        this.textureHeight = 32;
        this.body = new ModelRenderer(this, 0, 0);
        this.body.setRotationPoint(0.0F, 22.0F, 0.0F);
        this.body.addBox(-1.0F, -1.5F, -3.0F, 2, 3, 6, 0.0F);
        this.finTop = new ModelRenderer(this, 10, -5);
        this.finTop.setRotationPoint(0.0F, 20.5F, -3.0F);
        this.finTop.addBox(0.0F, -3.0F, 0.0F, 0, 3, 6, 0.0F);
        this.tail = new ModelRenderer(this, 22, -6);
        this.tail.setRotationPoint(0.0F, 22.0F, 3.0F);
        this.tail.addBox(0.0F, -1.5F, 0.0F, 0, 3, 6, 0.0F);
        this.finLeft = new ModelRenderer(this, 2, 12);
        this.finLeft.setRotationPoint(1.0F, 22.5F, 0.0F);
        this.finLeft.addBox(0.0F, -1.0F, 0.0F, 2, 2, 0, 0.0F);
        this.finLeft.rotateAngleZ = 0.7853981852531433F;
        this.finRight = new ModelRenderer(this, 2, 16);
        this.finRight.setRotationPoint(-1.0F, 22.5F, 0.0F);
        this.finRight.addBox(-2.0F, -1.0F, 0.0F, 2, 2, 0, 0.0F);
        this.finRight.rotateAngleZ = -0.7853981852531433F;
        this.finBottom = new ModelRenderer(this, 10, 5);
        this.finBottom.setRotationPoint(0.0F, 23.5F, -3.0F);
        this.finBottom.addBox(0.0F, 0.0F, 0.0F, 0, 3, 6, 0.0F);
    }

    @Override
    @Nonnull
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(this.body, this.tail, this.finRight, this.finLeft, this.finTop, this.finBottom);
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