package com.teammetallurgy.aquaculture.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.entity.AquaFishingBobberEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.ItemAbilities;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import javax.annotation.Nonnull;

public class AquaBobberRenderer extends EntityRenderer<AquaFishingBobberEntity> {
    private static final ResourceLocation BOBBER = ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, "textures/entity/rod/bobber/bobber.png");
    private static final ResourceLocation BOBBER_OVERLAY = ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, "textures/entity/rod/bobber/bobber_overlay.png");
    private static final ResourceLocation BOBBER_VANILLA = ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, "textures/entity/rod/bobber/bobber_vanilla.png");
    private static final ResourceLocation HOOK = ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, "textures/entity/rod/hook/hook.png");
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
            int bobberColorInt = FastColor.ARGB32.color(193, 38, 38);
            if (!bobberStack.isEmpty()) {
                if (bobberStack.is(ItemTags.DYEABLE)) {
                    DyedItemColor dyeditemcolor = bobberStack.get(DataComponents.DYED_COLOR);
                    if (dyeditemcolor != null) {
                        bobberColorInt = dyeditemcolor.rgb();
                    }
                    bobberR = (float) (bobberColorInt >> 16 & 255) / 255.0F;
                    bobberG = (float) (bobberColorInt >> 8 & 255) / 255.0F;
                    bobberB = (float) (bobberColorInt & 255) / 255.0F;
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

            float swingProgress = angler.getAttackAnim(partialTicks);
            float swingProgressSqrt = Mth.sin(Mth.sqrt(swingProgress) * (float) Math.PI);
            Vec3 playerHandPos = this.getPlayerHandPos(angler, swingProgressSqrt, partialTicks);
            Vec3 entityPos = bobber.getPosition(partialTicks).add(0.0, 0.25, 0.0);
            float x = (float) (playerHandPos.x - entityPos.x);
            float y = (float) (playerHandPos.y - entityPos.y);
            float z = (float) (playerHandPos.z - entityPos.z);
            VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.lineStrip());
            PoseStack.Pose pose = poseStack.last();

            //Line color
            ItemStack line = bobber.getFishingLine();
            float r = 0;
            float g = 0;
            float b = 0;
            if (!line.isEmpty()) {
                DyedItemColor dyeditemcolor = line.get(DataComponents.DYED_COLOR);
                if (dyeditemcolor != null) {
                    int colorInt = dyeditemcolor.rgb();
                    r = (float) (colorInt >> 16 & 255) / 255.0F;
                    g = (float) (colorInt >> 8 & 255) / 255.0F;
                    b = (float) (colorInt & 255) / 255.0F;
                }
            }
            for (int size = 0; size < 16; ++size) {
                stringVertex(x, y, z, vertexConsumer, pose, fraction(size, 15), fraction(size + 1, 16), r, g, b);
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
        builder.addVertex(matrix4f, x - 0.5F, (float) y - 0.5F, 0.0F).setColor(255, 255, 255, 255).setUv((float) u, (float) v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(i).setNormal(0.0F, 1.0F, 0.0F);
    }

    private static void vertex(VertexConsumer builder, Matrix4f matrix4f, Matrix3f matrix3f, int i, float x, int y, int u, int v, float r, float g, float b) {
        builder.addVertex(matrix4f, x - 0.5F, (float) y - 0.5F, 0.0F).setColor(r, g, b, 1.0F).setUv((float) u, (float) v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(i).setNormal(0.0F, 1.0F, 0.0F);
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
        vertexConsumer.addVertex(pose.pose(), var7, var8, var9).setColor(r, g, b, 1.0F).setNormal(pose, var10, var11, var12);
    }

    private static float fraction(int value1, int value2) {
        return (float) value1 / (float) value2;
    }

    private Vec3 getPlayerHandPos(Player player, float swingProgressSqrt, float partialTicks) { //Copied from FishingHookRenderer
        int i = player.getMainArm() == HumanoidArm.RIGHT ? 1 : -1;
        ItemStack itemstack = player.getMainHandItem();
        if (!itemstack.canPerformAction(ItemAbilities.FISHING_ROD_CAST)) {
            i = -i;
        }

        if (this.entityRenderDispatcher.options.getCameraType().isFirstPerson() && player == Minecraft.getInstance().player) {
            double d4 = 960.0 / (double) this.entityRenderDispatcher.options.fov().get().intValue();
            Vec3 vec3 = this.entityRenderDispatcher
                    .camera
                    .getNearPlane()
                    .getPointOnPlane((float) i * 0.525F, -0.1F)
                    .scale(d4)
                    .yRot(swingProgressSqrt * 0.5F)
                    .xRot(-swingProgressSqrt * 0.7F);
            return player.getEyePosition(partialTicks).add(vec3);
        } else {
            float f = Mth.lerp(partialTicks, player.yBodyRotO, player.yBodyRot) * (float) (Math.PI / 180.0);
            double d0 = Mth.sin(f);
            double d1 = Mth.cos(f);
            float f1 = player.getScale();
            double d2 = (double) i * 0.35 * (double) f1;
            double d3 = 0.8 * (double) f1;
            float f2 = player.isCrouching() ? -0.1875F : 0.0F;
            return player.getEyePosition(partialTicks).add(-d1 * d2 - d0 * d3, (double) f2 - 0.45 * (double) f1, -d0 * d2 + d1 * d3);
        }
    }
}