package com.teammetallurgy.aquaculture.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.entity.AquaFishingBobberEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ToolActions;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import javax.annotation.Nonnull;

public class AquaBobberRenderer extends EntityRenderer<AquaFishingBobberEntity> {
    private static final ResourceLocation BOBBER = new ResourceLocation(Aquaculture.MOD_ID, "textures/entity/rod/bobber/bobber.png");
    private static final ResourceLocation BOBBER_OVERLAY = new ResourceLocation(Aquaculture.MOD_ID, "textures/entity/rod/bobber/bobber_overlay.png");
    private static final ResourceLocation BOBBER_VANILLA = new ResourceLocation(Aquaculture.MOD_ID, "textures/entity/rod/bobber/bobber_vanilla.png");
    private static final ResourceLocation HOOK = new ResourceLocation(Aquaculture.MOD_ID, "textures/entity/rod/hook/hook.png");
    private static final RenderType BOBBER_RENDER = RenderType.entityCutout(BOBBER);
    private static final RenderType BOBBER_OVERLAY_RENDER = RenderType.entityCutout(BOBBER_OVERLAY);
    private static final RenderType BOBBER_VANILLA_RENDER = RenderType.entityCutout(BOBBER_VANILLA);
    private static final RenderType HOOK_RENDER = RenderType.entityCutout(HOOK);

    public AquaBobberRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(@Nonnull AquaFishingBobberEntity bobber, float entityYaw, float partialTicks, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int i) {
        Player angler = bobber.getPlayerOwner();
        if (angler != null) {
            poseStack.pushPose();
            poseStack.pushPose(); //Start Hook/Bobber rendering
            poseStack.scale(0.5F, 0.5F, 0.5F);
            poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
            PoseStack.Pose bobberMatrix = poseStack.last();
            Matrix4f posMatrix = bobberMatrix.pose();
            Matrix3f matrix3f = bobberMatrix.normal();
            //Bobber + Bobber Overlay
            VertexConsumer bobberOverlayVertex = bobber.hasBobber() ? buffer.getBuffer(BOBBER_OVERLAY_RENDER) : buffer.getBuffer(BOBBER_VANILLA_RENDER);
            //Bobber Overlay
            ItemStack bobberStack = bobber.getBobber();
            float bobberR = 1.0F;
            float bobberG = 1.0F;
            float bobberB = 1.0F;
            if (!bobberStack.isEmpty()) {
                if (bobberStack.getItem() instanceof DyeableLeatherItem) {
                    int colorInt = ((DyeableLeatherItem) bobberStack.getItem()).getColor(bobberStack);
                    bobberR = (float) (colorInt >> 16 & 255) / 255.0F;
                    bobberG = (float) (colorInt >> 8 & 255) / 255.0F;
                    bobberB = (float) (colorInt & 255) / 255.0F;
                }
            }
            vertex(bobberOverlayVertex, posMatrix, matrix3f, i, 0.0F, 0, 0, 1, bobberR, bobberG, bobberB);
            vertex(bobberOverlayVertex, posMatrix, matrix3f, i, 1.0F, 0, 1, 1, bobberR, bobberG, bobberB);
            vertex(bobberOverlayVertex, posMatrix, matrix3f, i, 1.0F, 1, 1, 0, bobberR, bobberG, bobberB);
            vertex(bobberOverlayVertex, posMatrix, matrix3f, i, 0.0F, 1, 0, 0, bobberR, bobberG, bobberB);
            //Bobber Background
            if (bobber.hasBobber()) {
                VertexConsumer bobberVertex = buffer.getBuffer(BOBBER_RENDER);
                renderPosTexture(bobberVertex, posMatrix, matrix3f, i, 0.0F, 0, 0, 1);
                renderPosTexture(bobberVertex, posMatrix, matrix3f, i, 1.0F, 0, 1, 1);
                renderPosTexture(bobberVertex, posMatrix, matrix3f, i, 1.0F, 1, 1, 0);
                renderPosTexture(bobberVertex, posMatrix, matrix3f, i, 0.0F, 1, 0, 0);
            }
            //Hook
            VertexConsumer hookVertex = bobber.hasHook() ? buffer.getBuffer(RenderType.entityCutout(bobber.getHook().getTexture())) : buffer.getBuffer(HOOK_RENDER);
            renderPosTexture(hookVertex, posMatrix, matrix3f, i, 0.0F, 0, 0, 1);
            renderPosTexture(hookVertex, posMatrix, matrix3f, i, 1.0F, 0, 1, 1);
            renderPosTexture(hookVertex, posMatrix, matrix3f, i, 1.0F, 1, 1, 0);
            renderPosTexture(hookVertex, posMatrix, matrix3f, i, 0.0F, 1, 0, 0);

            poseStack.popPose(); //End Hook/Bobber rendering

            int hand = angler.getMainArm() == HumanoidArm.RIGHT ? 1 : -1;
            ItemStack heldMain = angler.getMainHandItem();
            if (!heldMain.canPerformAction(ToolActions.FISHING_ROD_CAST)) {
                hand = -hand;
            }

            float swingProgress = angler.getAttackAnim(partialTicks);
            float swingProgressSqrt = Mth.sin(Mth.sqrt(swingProgress) * (float) Math.PI);
            float yawOffset = Mth.lerp(partialTicks, angler.yBodyRotO, angler.yBodyRot) * 0.017453292F;
            double sin = Mth.sin(yawOffset);
            double cos = Mth.cos(yawOffset);
            double handOffset = (double) hand * 0.35D;
            double anglerX;
            double anglerY;
            double anglerZ;
            float anglerEye;
            double fov;
            if ((this.entityRenderDispatcher.options == null || this.entityRenderDispatcher.options.getCameraType().isFirstPerson()) && angler == Minecraft.getInstance().player) {
                fov = 960.0D / this.entityRenderDispatcher.options.fov().get();
                Vec3 rod = this.entityRenderDispatcher.camera.getNearPlane().getPointOnPlane((float) hand * 0.525F, -0.1F);
                rod = rod.scale(fov);
                rod = rod.yRot(swingProgressSqrt * 0.5F);
                rod = rod.xRot(-swingProgressSqrt * 0.7F);
                anglerX = Mth.lerp(partialTicks, angler.xo, angler.getX()) + rod.x;
                anglerY = Mth.lerp(partialTicks, angler.yo, angler.getY()) + rod.y;
                anglerZ = Mth.lerp(partialTicks, angler.zo, angler.getZ()) + rod.z;
                anglerEye = angler.getEyeHeight();
            } else {
                anglerX = Mth.lerp(partialTicks, angler.xo, angler.getX()) - cos * handOffset - sin * 0.8D;
                anglerY = angler.yo + (double) angler.getEyeHeight() + (angler.getY() - angler.yo) * (double) partialTicks - 0.45D;
                anglerZ = Mth.lerp(partialTicks, angler.zo, angler.getZ()) - sin * handOffset + cos * 0.8D;
                anglerEye = angler.isCrouching() ? -0.1875F : 0.0F;
            }

            fov = Mth.lerp(partialTicks, bobber.xo, bobber.getX());
            double bobberY = Mth.lerp(partialTicks, bobber.yo, bobber.getY()) + 0.25D;
            double bobberZ = Mth.lerp(partialTicks, bobber.zo, bobber.getZ());
            float startX = (float) (anglerX - fov);
            float startY = (float) (anglerY - bobberY) + anglerEye;
            float startZ = (float) (anglerZ - bobberZ);
            VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.lineStrip());
            PoseStack.Pose pose = poseStack.last();

            //Line color
            ItemStack line = bobber.getFishingLine();
            float r = 0;
            float g = 0;
            float b = 0;
            if (!line.isEmpty()) {
                DyeableLeatherItem lineItem = (DyeableLeatherItem) line.getItem();
                if (lineItem.hasCustomColor(line)) {
                    int colorInt = lineItem.getColor(line);
                    r = (float) (colorInt >> 16 & 255) / 255.0F;
                    g = (float) (colorInt >> 8 & 255) / 255.0F;
                    b = (float) (colorInt & 255) / 255.0F;
                }
            }
            for (int size = 0; size <= 16; ++size) {
                stringVertex(startX, startY, startZ, vertexConsumer, pose, fraction(size, 16), fraction(size + 1, 16), r, g, b);
            }
            poseStack.popPose();
            super.render(bobber, entityYaw, partialTicks, poseStack, buffer, i);
        }
    }

    @Override
    @Nonnull
    public ResourceLocation getTextureLocation(@Nonnull AquaFishingBobberEntity fishHook) {
        return BOBBER_VANILLA;
    }

    private static void renderPosTexture(VertexConsumer builder, Matrix4f matrix4f, Matrix3f matrix3f, int i, float x, int y, int u, int v) {
        builder.vertex(matrix4f, x - 0.5F, (float) y - 0.5F, 0.0F).color(255, 255, 255, 255).uv((float) u, (float) v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(i).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
    }

    private static void vertex(VertexConsumer builder, Matrix4f matrix4f, Matrix3f matrix3f, int i, float x, int y, int u, int v, float r, float g, float b) {
        builder.vertex(matrix4f, x - 0.5F, (float) y - 0.5F, 0.0F).color(r, g, b, 1.0F).uv((float) u, (float) v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(i).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
    }

    private static void stringVertex(float x, float y, float z, VertexConsumer vertexConsumer, PoseStack.Pose pose, float f1, float f2, float r, float g, float b) {
        float var7 = x * f1;
        float var8 = y * (f1 * f1 + f1) * 0.5F + 0.25F;
        float var9 = z * f1;
        float var10 = x * f2 - var7;
        float var11 = y * (f2 * f2 + f2) * 0.5F + 0.25F - var8;
        float var12 = z * f2 - var9;
        float var13 = Mth.sqrt(var10 * var10 + var11 * var11 + var12 * var12);
        var10 /= var13;
        var11 /= var13;
        var12 /= var13;
        vertexConsumer.vertex(pose.pose(), var7, var8, var9).color(r, g, b, 1.0F).normal(pose.normal(), var10, var11, var12).endVertex();
    }

    private static float fraction(int value1, int value2) {
        return (float) value1 / (float) value2;
    }
}