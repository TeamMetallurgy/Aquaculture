package com.teammetallurgy.aquaculture.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.client.renderer.entity.state.AquaBobberRenderState;
import com.teammetallurgy.aquaculture.entity.AquaFishingBobberEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.FishingHookRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;

public class AquaFishingHookRenderer extends EntityRenderer<AquaFishingBobberEntity, AquaBobberRenderState> {
    private static final ResourceLocation BOBBER = ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, "textures/entity/rod/bobber/bobber.png");
    private static final ResourceLocation BOBBER_OVERLAY = ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, "textures/entity/rod/bobber/bobber_overlay.png");
    private static final ResourceLocation BOBBER_VANILLA = ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, "textures/entity/rod/bobber/bobber_vanilla.png");
    private static final ResourceLocation HOOK = ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, "textures/entity/rod/hook/hook.png");
    private static final RenderType BOBBER_RENDER = RenderType.entityCutout(BOBBER);
    private static final RenderType BOBBER_OVERLAY_RENDER = RenderType.entityCutout(BOBBER_OVERLAY);
    private static final RenderType BOBBER_VANILLA_RENDER = RenderType.entityCutout(BOBBER_VANILLA);
    private static final RenderType HOOK_RENDER = RenderType.entityCutout(HOOK);

    public AquaFishingHookRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public boolean shouldRender(@Nonnull AquaFishingBobberEntity bobber, @Nonnull Frustum frustum, double x, double y, double z) {
        return super.shouldRender(bobber, frustum, x, y, z) && bobber.getPlayerOwner() != null;
    }

    @Override
    public void submit(@Nonnull AquaBobberRenderState renderState, PoseStack poseStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        poseStack.pushPose(); //Start Hook/Bobber rendering
        poseStack.scale(0.5F, 0.5F, 0.5F);
        poseStack.mulPose(cameraRenderState.orientation);

        //Bobber Overlay
        nodeCollector.submitCustomGeometry(poseStack, renderState.hasBobber ? BOBBER_OVERLAY_RENDER : BOBBER_VANILLA_RENDER, (pose, vertexConsumer) -> {
            ItemStack bobberStack = renderState.bobber;
            float bobberR = 1.0F;
            float bobberG = 1.0F;
            float bobberB = 1.0F;
            int bobberColorInt = ARGB.color(193, 38, 38);
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
            vertex(vertexConsumer, pose, renderState.lightCoords, 0.0F, 0, 0, 1, bobberR, bobberG, bobberB);
            vertex(vertexConsumer, pose, renderState.lightCoords, 1.0F, 0, 1, 1, bobberR, bobberG, bobberB);
            vertex(vertexConsumer, pose, renderState.lightCoords, 1.0F, 1, 1, 0, bobberR, bobberG, bobberB);
            vertex(vertexConsumer, pose, renderState.lightCoords, 0.0F, 1, 0, 0, bobberR, bobberG, bobberB);
        });

        //Bobber Background
        nodeCollector.submitCustomGeometry(poseStack, BOBBER_RENDER, (pose, vertexConsumer) -> {
            if (renderState.hasBobber) {
                renderPosTexture(vertexConsumer, pose, renderState.lightCoords, 0.0F, 0, 0, 1);
                renderPosTexture(vertexConsumer, pose, renderState.lightCoords, 1.0F, 0, 1, 1);
                renderPosTexture(vertexConsumer, pose, renderState.lightCoords, 1.0F, 1, 1, 0);
                renderPosTexture(vertexConsumer, pose, renderState.lightCoords, 0.0F, 1, 0, 0);
            }
        });

        //Hook
        nodeCollector.submitCustomGeometry(poseStack, renderState.hasHook ? RenderType.entityCutout(renderState.hook.getTexture()) : HOOK_RENDER, (pose, vertexConsumer) -> {
            renderPosTexture(vertexConsumer, pose, renderState.lightCoords, 0.0F, 0, 0, 1);
            renderPosTexture(vertexConsumer, pose, renderState.lightCoords, 1.0F, 0, 1, 1);
            renderPosTexture(vertexConsumer, pose, renderState.lightCoords, 1.0F, 1, 1, 0);
            renderPosTexture(vertexConsumer, pose, renderState.lightCoords, 0.0F, 1, 0, 0);
        });

        poseStack.popPose(); //End Hook/Bobber rendering

        //Line color
        float x = (float) renderState.lineOriginOffset.x;
        float y = (float) renderState.lineOriginOffset.y;
        float z = (float) renderState.lineOriginOffset.z;
        nodeCollector.submitCustomGeometry(poseStack, RenderType.lineStrip(), (pose, vertexConsumer) -> {
            ItemStack line = renderState.fishingLine;
            float r = 0;
            float g = 0;
            float b = 0;
            if (!line.isEmpty()) {
                if (line.is(ItemTags.DYEABLE)) {
                    DyedItemColor dyeditemcolor = line.get(DataComponents.DYED_COLOR);
                    if (dyeditemcolor != null) {
                        int colorInt = dyeditemcolor.rgb();
                        r = (float) (colorInt >> 16 & 255) / 255.0F;
                        g = (float) (colorInt >> 8 & 255) / 255.0F;
                        b = (float) (colorInt & 255) / 255.0F;
                    }
                }
            }
            for (int size = 0; size < 16; ++size) {
                float sizeFraction = fraction(size, 16);
                float sizeFraction1 = fraction(size + 1, 16);
                stringVertex(x, y, z, vertexConsumer, pose, sizeFraction, sizeFraction1, r, g, b);
                stringVertex(x, y, z, vertexConsumer, pose, sizeFraction1, sizeFraction, r, g, b);
            }
        });
        poseStack.popPose();
        super.submit(renderState, poseStack, nodeCollector, cameraRenderState);
    }

    private static void renderPosTexture(VertexConsumer builder, PoseStack.Pose pose, int packedLight, float x, int y, int u, int v) {
        builder.addVertex(pose, x - 0.5F, (float) y - 0.5F, 0.0F).setColor(255, 255, 255, 255).setUv((float) u, (float) v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(0.0F, 1.0F, 0.0F);
    }

    private static void vertex(VertexConsumer builder, PoseStack.Pose pose, int packedLight, float x, int y, int u, int v, float r, float g, float b) { //Same as vanilla, but with color
        builder.addVertex(pose, x - 0.5F, (float) y - 0.5F, 0.0F).setColor(r, g, b, 1.0F).setUv((float) u, (float) v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(0.0F, 1.0F, 0.0F);
    }

    private static void stringVertex(float x, float y, float z, VertexConsumer vertexConsumer, PoseStack.Pose pose, float f1, float f2, float r, float g, float b) {  //Same as vanilla, but with color
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

    private static float fraction(int numerator, int denominator) {
        return (float)numerator / (float)denominator;
    }

    private Vec3 getPlayerHandPos(Player player, float handAngle, float partialTick) { //Copied from Fishing HookRenderer
        int i = FishingHookRenderer.getHoldingArm(player) == HumanoidArm.RIGHT ? 1 : -1;
        if (this.entityRenderDispatcher.options.getCameraType().isFirstPerson() && player == Minecraft.getInstance().player) {
            double d4 = 960.0 / (double)this.entityRenderDispatcher.options.fov().get().intValue();
            Vec3 vec3 = this.entityRenderDispatcher
                    .camera
                    .getNearPlane()
                    .getPointOnPlane((float)i * 0.525F, -0.1F)
                    .scale(d4)
                    .yRot(handAngle * 0.5F)
                    .xRot(-handAngle * 0.7F);
            return player.getEyePosition(partialTick).add(vec3);
        } else {
            float f = Mth.lerp(partialTick, player.yBodyRotO, player.yBodyRot) * (float) (Math.PI / 180.0);
            double d0 = (double)Mth.sin(f);
            double d1 = (double)Mth.cos(f);
            float f1 = player.getScale();
            double d2 = (double)i * 0.35 * (double)f1;
            double d3 = 0.8 * (double)f1;
            float f2 = player.isCrouching() ? -0.1875F : 0.0F;
            return player.getEyePosition(partialTick).add(-d1 * d2 - d0 * d3, (double)f2 - 0.45 * (double)f1, -d0 * d2 + d1 * d3);
        }
    }

    @Override
    @Nonnull
    public AquaBobberRenderState createRenderState() {
        return new AquaBobberRenderState();
    }

    @Override
    public void extractRenderState(@Nonnull AquaFishingBobberEntity bobber, @Nonnull AquaBobberRenderState reusedState, float partialTick) {
        super.extractRenderState(bobber, reusedState, partialTick);
        Player player = bobber.getPlayerOwner();
        if (player == null) {
            reusedState.lineOriginOffset = Vec3.ZERO;
        } else {
            float attackAnim = player.getAttackAnim(partialTick);
            float f1 = Mth.sin(Mth.sqrt(attackAnim) * (float) Math.PI);
            Vec3 vec3 = this.getPlayerHandPos(player, f1, partialTick);
            Vec3 vec31 = bobber.getPosition(partialTick).add(0.0, 0.25, 0.0);
            reusedState.lineOriginOffset = vec3.subtract(vec31);
        }

        reusedState.hasBobber = bobber.hasBobber();
        reusedState.hasHook = bobber.hasHook();
        reusedState.bobber = bobber.getBobber();
        reusedState.fishingLine = bobber.getFishingLine();
        reusedState.hook = bobber.getHook();
    }

    @Override
    protected boolean affectedByCulling(@Nonnull AquaFishingBobberEntity bobber) {
        return false;
    }
}