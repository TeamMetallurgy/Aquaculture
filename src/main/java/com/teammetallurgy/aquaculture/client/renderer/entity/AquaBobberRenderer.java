package com.teammetallurgy.aquaculture.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.entity.AquaFishingBobberEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.IDyeableArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;
import java.util.Objects;

public class AquaBobberRenderer extends EntityRenderer<AquaFishingBobberEntity> {
    private static final ResourceLocation BOBBER = new ResourceLocation(Aquaculture.MOD_ID, "textures/entity/rod/bobber/bobber.png");
    private static final ResourceLocation BOBBER_OVERLAY = new ResourceLocation(Aquaculture.MOD_ID, "textures/entity/rod/bobber/bobber_overlay.png");
    private static final ResourceLocation BOBBER_VANILLA = new ResourceLocation(Aquaculture.MOD_ID, "textures/entity/rod/bobber/bobber_vanilla.png");
    private static final ResourceLocation HOOK = new ResourceLocation(Aquaculture.MOD_ID, "textures/entity/rod/hook/hook.png");
    private static final RenderType BOBBER_RENDER = RenderType.getEntityCutout(BOBBER);
    private static final RenderType BOBBER_OVERLAY_RENDER = RenderType.getEntityCutout(BOBBER_OVERLAY);
    private static final RenderType BOBBER_VANILLA_RENDER = RenderType.getEntityCutout(BOBBER_VANILLA);
    private static final RenderType HOOK_RENDER = RenderType.getEntityCutout(HOOK);

    public AquaBobberRenderer(EntityRendererManager manager) {
        super(manager);
    }

    @Override
    public void render(@Nonnull AquaFishingBobberEntity bobber, float entityYaw, float partialTicks, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer buffer, int i) {
        PlayerEntity angler = bobber.getAngler();
        if (angler != null) {
            matrixStack.push();
            matrixStack.push(); //Start Hook/Bobber rendering
            matrixStack.scale(0.5F, 0.5F, 0.5F);
            matrixStack.rotate(this.renderManager.getCameraOrientation());
            matrixStack.rotate(Vector3f.YP.rotationDegrees(180.0F));
            MatrixStack.Entry bobberMatrix = matrixStack.getLast();
            Matrix4f posMatrix = bobberMatrix.getMatrix();
            Matrix3f matrix3f = bobberMatrix.getNormal();
            //Bobber + Bobber Overlay
            IVertexBuilder bobberOverlayVertex = bobber.hasBobber() ? buffer.getBuffer(BOBBER_OVERLAY_RENDER) : buffer.getBuffer(BOBBER_VANILLA_RENDER);
            //Bobber Overlay
            ItemStack bobberStack = bobber.getBobber();
            float bobberR = 1.0F;
            float bobberG = 1.0F;
            float bobberB = 1.0F;
            if (!bobberStack.isEmpty()) {
                if (bobberStack.getItem() instanceof IDyeableArmorItem) {
                    int colorInt = ((IDyeableArmorItem) bobberStack.getItem()).getColor(bobberStack);
                    bobberR = (float) (colorInt >> 16 & 255) / 255.0F;
                    bobberG = (float) (colorInt >> 8 & 255) / 255.0F;
                    bobberB = (float) (colorInt & 255) / 255.0F;
                }
            }
            renderPosTextureColor(bobberOverlayVertex, posMatrix, matrix3f, i, 0.0F, 0, 0, 1, bobberR, bobberG, bobberB);
            renderPosTextureColor(bobberOverlayVertex, posMatrix, matrix3f, i, 1.0F, 0, 1, 1, bobberR, bobberG, bobberB);
            renderPosTextureColor(bobberOverlayVertex, posMatrix, matrix3f, i, 1.0F, 1, 1, 0, bobberR, bobberG, bobberB);
            renderPosTextureColor(bobberOverlayVertex, posMatrix, matrix3f, i, 0.0F, 1, 0, 0, bobberR, bobberG, bobberB);
            //Bobber Background
            if (bobber.hasBobber()) {
                IVertexBuilder bobberVertex = buffer.getBuffer(BOBBER_RENDER);
                renderPosTexture(bobberVertex, posMatrix, matrix3f, i, 0.0F, 0, 0, 1);
                renderPosTexture(bobberVertex, posMatrix, matrix3f, i, 1.0F, 0, 1, 1);
                renderPosTexture(bobberVertex, posMatrix, matrix3f, i, 1.0F, 1, 1, 0);
                renderPosTexture(bobberVertex, posMatrix, matrix3f, i, 0.0F, 1, 0, 0);
            }
            //Hook
            IVertexBuilder hookVertex = bobber.hasHook() ? buffer.getBuffer(RenderType.getEntityCutout(bobber.getHook().getTexture())) : buffer.getBuffer(HOOK_RENDER);
            renderPosTexture(hookVertex, posMatrix, matrix3f, i, 0.0F, 0, 0, 1);
            renderPosTexture(hookVertex, posMatrix, matrix3f, i, 1.0F, 0, 1, 1);
            renderPosTexture(hookVertex, posMatrix, matrix3f, i, 1.0F, 1, 1, 0);
            renderPosTexture(hookVertex, posMatrix, matrix3f, i, 0.0F, 1, 0, 0);
            matrixStack.pop(); //End Hook/Bobber rendering

            int hand = angler.getPrimaryHand() == HandSide.RIGHT ? 1 : -1;
            ItemStack heldMain = angler.getHeldItemMainhand();
            if (!(heldMain.getItem() instanceof FishingRodItem)) {
                hand = -hand;
            }

            float swingProgress = angler.getSwingProgress(partialTicks);
            float swingProgressSqrt = MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) Math.PI);
            float yawOffset = MathHelper.lerp(partialTicks, angler.prevRenderYawOffset, angler.renderYawOffset) * ((float) Math.PI / 180.0F);
            double sin = MathHelper.sin(yawOffset);
            double cos = MathHelper.cos(yawOffset);
            double handOffset = (double) hand * 0.35D;
            double anglerX;
            double anglerY;
            double anglerZ;
            float anglerEye;
            if ((this.renderManager.options == null || this.renderManager.options.thirdPersonView <= 0) && angler == Minecraft.getInstance().player) {
                double fov = Objects.requireNonNull(this.renderManager.options).fov;
                fov /= 100.0D;
                Vec3d rod = new Vec3d((double) hand * -0.36D * fov, -0.045D * fov, 0.4D);
                rod = rod.rotatePitch(-MathHelper.lerp(partialTicks, angler.prevRotationPitch, angler.rotationPitch) * ((float) Math.PI / 180F));
                rod = rod.rotateYaw(-MathHelper.lerp(partialTicks, angler.prevRotationYaw, angler.rotationYaw) * ((float) Math.PI / 180F));
                rod = rod.rotateYaw(swingProgressSqrt * 0.5F);
                rod = rod.rotatePitch(-swingProgressSqrt * 0.7F);
                anglerX = MathHelper.lerp(partialTicks, angler.prevPosX, angler.getPosX()) + rod.x;
                anglerY = MathHelper.lerp(partialTicks, angler.prevPosY, angler.getPosY()) + rod.y;
                anglerZ = MathHelper.lerp(partialTicks, angler.prevPosZ, angler.getPosZ()) + rod.z;
                anglerEye = angler.getEyeHeight();
            } else {
                anglerX = MathHelper.lerp(partialTicks, angler.prevPosX, angler.getPosX()) - cos * handOffset - sin * 0.8D;
                anglerY = angler.prevPosY + (double) angler.getEyeHeight() + (angler.getPosY() - angler.prevPosY) * (double) partialTicks - 0.45D;
                anglerZ = MathHelper.lerp(partialTicks, angler.prevPosZ, angler.getPosZ()) - sin * handOffset + cos * 0.8D;
                anglerEye = angler.isCrouching() ? -0.1875F : 0.0F;
            }

            double bobberX = MathHelper.lerp(partialTicks, bobber.prevPosX, bobber.getPosX());
            double bobberY = MathHelper.lerp(partialTicks, bobber.prevPosY, bobber.getPosY()) + 0.25D;
            double bobberZ = MathHelper.lerp(partialTicks, bobber.prevPosZ, bobber.getPosZ());
            float startX = (float) (anglerX - bobberX);
            float startY = (float) (anglerY - bobberY) + anglerEye;
            float startZ = (float) (anglerZ - bobberZ);
            IVertexBuilder vertexBuilder = buffer.getBuffer(RenderType.getLines());
            Matrix4f matrix4f1 = matrixStack.getLast().getMatrix();

            //Line color
            ItemStack line = bobber.getFishingLine();
            float r = 0;
            float g = 0;
            float b = 0;
            if (!line.isEmpty()) {
                IDyeableArmorItem lineItem = (IDyeableArmorItem) line.getItem();
                if (lineItem.hasColor(line)) {
                    int colorInt = lineItem.getColor(line);
                    r = (float) (colorInt >> 16 & 255) / 255.0F;
                    g = (float) (colorInt >> 8 & 255) / 255.0F;
                    b = (float) (colorInt & 255) / 255.0F;
                }
            }
            for (int size = 0; size < 16; ++size) {
                renderPosColor(startX, startY, startZ, vertexBuilder, matrix4f1, divide(size, 16), r, g, b);
                renderPosColor(startX, startY, startZ, vertexBuilder, matrix4f1, divide(size + 1, 16), r, g, b);
            }
            matrixStack.pop();
            super.render(bobber, entityYaw, partialTicks, matrixStack, buffer, i);
        }
    }

    @Override
    @Nonnull
    public ResourceLocation getEntityTexture(@Nonnull AquaFishingBobberEntity fishHook) {
        return BOBBER_VANILLA;
    }

    private static void renderPosTexture(IVertexBuilder builder, Matrix4f matrix4f, Matrix3f matrix3f, int i, float x, int y, int u, int v) {
        builder.pos(matrix4f, x - 0.5F, (float) y - 0.5F, 0.0F).color(255, 255, 255, 255).tex((float) u, (float) v).overlay(OverlayTexture.NO_OVERLAY).lightmap(i).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
    }

    private static void renderPosTextureColor(IVertexBuilder builder, Matrix4f matrix4f, Matrix3f matrix3f, int i, float x, int y, int u, int v, float r, float g, float b) {
        builder.pos(matrix4f, x - 0.5F, (float) y - 0.5F, 0.0F).color(r, g, b, 1.0F).tex((float) u, (float) v).overlay(OverlayTexture.NO_OVERLAY).lightmap(i).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
    }

    private static void renderPosColor(float x, float y, float z, IVertexBuilder builder, Matrix4f matrix4f, float f, float r, float g, float b) {
        builder.pos(matrix4f, x * f, y * (f * f + f) * 0.5F + 0.25F, z * f).color(r, g, b, 1.0F).endVertex();
    }

    private static float divide(int value1, int value2) {
        return (float) value1 / (float) value2;
    }
}