package com.teammetallurgy.aquaculture.client.renderer.entity.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;

public class JellyfishModel<T extends Entity> extends SegmentedModel<T> {
    private final ModelRenderer head;
    private final ModelRenderer tentaclesMain;
    private final ModelRenderer tentaclesLeft;
    private final ModelRenderer tentaclesRight;
    private final ModelRenderer frill;
    private final ModelRenderer heart;

    public JellyfishModel() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.heart = new ModelRenderer(this, 34, 0);
        this.heart.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.heart.addBox(-1.5F, -1.5F, -2.0F, 3, 3, 3, 0.0F);
        this.head = new ModelRenderer(this, 14, 0);
        this.head.setRotationPoint(0.0F, 21.0F, -3.0F);
        this.head.addBox(-2.5F, -2.5F, -5.0F, 5, 5, 5, 0.0F);
        this.frill = new ModelRenderer(this, 0, 0);
        this.frill.setRotationPoint(0.0F, 0.0F, -2.0F);
        this.frill.addBox(-3.0F, -3.0F, 0.0F, 6, 6, 1, 0.0F);
        this.tentaclesMain = new ModelRenderer(this, 0, 0);
        this.tentaclesMain.setRotationPoint(0.0F, 21.0F, -3.5F);
        this.tentaclesMain.addBox(0.0F, -3.0F, 0.0F, 0, 6, 14, 0.0F);
        this.tentaclesRight = new ModelRenderer(this, 0, 14);
        this.tentaclesRight.setRotationPoint(0.0F, 21.0F, -3.5F);
        this.tentaclesRight.addBox(-2.0F, -3.0F, 0.0F, 0, 6, 12, 0.0F);
        this.tentaclesLeft = new ModelRenderer(this, 0, 8);
        this.tentaclesLeft.setRotationPoint(0.0F, 21.0F, -3.5F);
        this.tentaclesLeft.addBox(2.0F, -3.0F, 0.0F, 0, 6, 12, 0.0F);
        this.head.addChild(this.heart);
        this.head.addChild(this.frill);
    }

    @Override
    @Nonnull
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(this.head, this.tentaclesMain, this.tentaclesLeft, this.tentaclesRight);
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, @Nonnull IVertexBuilder builder, int p_225598_3_, int p_225598_4_, float p_225598_5_, float p_225598_6_, float p_225598_7_, float p_225598_8_) {
        //Actual rendering done in the layer. Only way I could figure out to keep the transparency
    }

    @Override
    public void setRotationAngles(@Nonnull T jellyfish, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float stillMovement = 0.1F;
        if (!jellyfish.isInWater()) {
            stillMovement = 0.05F;
        }
        this.tentaclesLeft.rotateAngleY = -stillMovement * 0.25F * MathHelper.sin(0.3F * ageInTicks) + MathHelper.cos(limbSwing * 0.4662F) * 0.5F * limbSwingAmount;
        this.tentaclesMain.rotateAngleY = -stillMovement * 0.25F * MathHelper.sin(0.3F * ageInTicks) + MathHelper.cos(limbSwing * 0.4662F) * 0.5F * limbSwingAmount;
        this.tentaclesRight.rotateAngleY = -stillMovement * 0.25F * MathHelper.sin(0.3F * ageInTicks) + MathHelper.cos(limbSwing * 0.4662F) * 0.5F * limbSwingAmount;
    }
}