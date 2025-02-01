package com.teammetallurgy.aquaculture.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.client.renderer.entity.state.AquaBobberRenderState;
import com.teammetallurgy.aquaculture.entity.AquaFishingBobberEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;

import javax.annotation.Nonnull;

public class AquaBobberRenderer extends EntityRenderer<AquaFishingBobberEntity, AquaBobberRenderState> {
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
    public void render(@Nonnull AquaBobberRenderState bobber, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.pushPose(); //Start Hook/Bobber rendering
        poseStack.scale(0.5F, 0.5F, 0.5F);
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        PoseStack.Pose pose = poseStack.last();
        //Bobber + Bobber Overlay
        VertexConsumer bobberOverlayVertex = bobber.hasBobber ? buffer.getBuffer(BOBBER_OVERLAY_RENDER) : buffer.getBuffer(BOBBER_VANILLA_RENDER);
        //Bobber Overlay
        ItemStack bobberStack = bobber.bobber;
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
        vertex(bobberOverlayVertex, pose, packedLight, 0.0F, 0, 0, 1, bobberR, bobberG, bobberB);
        vertex(bobberOverlayVertex, pose, packedLight, 1.0F, 0, 1, 1, bobberR, bobberG, bobberB);
        vertex(bobberOverlayVertex, pose, packedLight, 1.0F, 1, 1, 0, bobberR, bobberG, bobberB);
        vertex(bobberOverlayVertex, pose, packedLight, 0.0F, 1, 0, 0, bobberR, bobberG, bobberB);

        //Bobber Background
        if (bobber.hasBobber) {
            VertexConsumer bobberVertex = buffer.getBuffer(BOBBER_RENDER);
            renderPosTexture(bobberVertex, pose, packedLight, 0.0F, 0, 0, 1);
            renderPosTexture(bobberVertex, pose, packedLight, 1.0F, 0, 1, 1);
            renderPosTexture(bobberVertex, pose, packedLight, 1.0F, 1, 1, 0);
            renderPosTexture(bobberVertex, pose, packedLight, 0.0F, 1, 0, 0);
        }
        //Hook
        VertexConsumer hookVertex = bobber.hasHook ? buffer.getBuffer(RenderType.entityCutout(bobber.hook.getTexture())) : buffer.getBuffer(HOOK_RENDER);
        renderPosTexture(hookVertex, pose, packedLight, 0.0F, 0, 0, 1);
        renderPosTexture(hookVertex, pose, packedLight, 1.0F, 0, 1, 1);
        renderPosTexture(hookVertex, pose, packedLight, 1.0F, 1, 1, 0);
        renderPosTexture(hookVertex, pose, packedLight, 0.0F, 1, 0, 0);

        poseStack.popPose(); //End Hook/Bobber rendering

        float x = (float) bobber.lineOriginOffset.x;
        float y = (float) bobber.lineOriginOffset.y;
        float z = (float) bobber.lineOriginOffset.z;
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.lineStrip());
        PoseStack.Pose linePose = poseStack.last();

        //Line color
        ItemStack line = bobber.fishingLine;
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
            stringVertex(x, y, z, vertexConsumer, linePose, (float) size / (float) 15, (float) (size + 1) / (float) 16, r, g, b);
        }
        poseStack.popPose();
        super.render(bobber, poseStack, buffer, packedLight);
    }

    /*@Override
    @Nonnull
    public ResourceLocation getTextureLocation(@Nonnull AquaFishingBobberEntity fishHook) { //TODO???
        return BOBBER_VANILLA;
    }*/

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

    @Override
    @Nonnull
    public AquaBobberRenderState createRenderState() {
        return new AquaBobberRenderState();
    }

    @Override
    public void extractRenderState(@Nonnull AquaFishingBobberEntity bobber, @Nonnull AquaBobberRenderState reusedState, float partialTick) {
        super.extractRenderState(bobber, reusedState, partialTick);
        reusedState.hasBobber = bobber.hasBobber();
        reusedState.hasHook = bobber.hasHook();
        reusedState.bobber = bobber.getBobber();
        reusedState.fishingLine = bobber.getFishingLine();
        reusedState.hook = bobber.getHook();
    }
}