package com.teammetallurgy.aquaculture.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.client.ClientHandler;
import com.teammetallurgy.aquaculture.client.renderer.entity.layers.JellyfishLayer;
import com.teammetallurgy.aquaculture.client.renderer.entity.model.*;
import com.teammetallurgy.aquaculture.client.renderer.entity.state.AquaFishRenderState;
import com.teammetallurgy.aquaculture.entity.AquaFishEntity;
import com.teammetallurgy.aquaculture.entity.FishType;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import javax.annotation.Nonnull;

public class AquaFishRenderer extends MobRenderer<AquaFishEntity, AquaFishRenderState, FishBaseModel> {
    private static final ResourceLocation DEFAULT_LOCATION = ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, "textures/entity/fish/atlantic_cod.png");
    public static final ResourceLocation JELLYFISH = ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, "textures/entity/fish/jellyfish.png");
    private final AquaTropicalFishBModel tropicalFishBModel;
    private final FishSmallModel smallModel;
    private final FishMediumModel mediumModel;
    private final FishLargeModel largeModel;
    private final FishLongnoseModel longnoseModel;
    private final FishCathfishModel catfishModel;
    private final JellyfishModel jellyfishModel;

    public AquaFishRenderer(EntityRendererProvider.Context context, boolean isJellyfish) {
        super(context, new FishMediumModel(context.bakeLayer(ClientHandler.MEDIUM_MODEL)), 0.35F);
        this.tropicalFishBModel = new AquaTropicalFishBModel(context.bakeLayer(ModelLayers.TROPICAL_FISH_LARGE));
        this.smallModel = new FishSmallModel(context.bakeLayer(ClientHandler.SMALL_MODEL));
        this.mediumModel = new FishMediumModel(context.bakeLayer(ClientHandler.MEDIUM_MODEL));
        this.largeModel = new FishLargeModel(context.bakeLayer(ClientHandler.LARGE_MODEL));
        this.longnoseModel = new FishLongnoseModel(context.bakeLayer(ClientHandler.LONGNOSE_MODEL));
        this.catfishModel = new FishCathfishModel(context.bakeLayer(ClientHandler.CATFISH_MODEL));
        this.jellyfishModel = new JellyfishModel(context.bakeLayer(ClientHandler.JELLYFISH_MODEL));

        if (isJellyfish) {
            this.addLayer(new JellyfishLayer(this, context.getModelSet()));
        }
    }

    @Override
    public void render(@Nonnull AquaFishRenderState renderState, @Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int i) {
        switch (renderState.fishType) {
            case SMALL -> this.model = smallModel;
            case LARGE -> this.model = largeModel;
            case LONGNOSE -> this.model = longnoseModel;
            case CATFISH -> this.model = catfishModel;
            case JELLYFISH -> this.model = jellyfishModel;
            case HALIBUT -> this.model = tropicalFishBModel;
            default -> this.model = mediumModel;
        }
        super.render(renderState, matrixStack, buffer, i);
    }

    @Override
    @Nonnull
    public ResourceLocation getTextureLocation(@Nonnull AquaFishRenderState renderState) {
        ResourceLocation location = renderState.byName;
        if (location != null) {
            return ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, "textures/entity/fish/" + location.getPath() + ".png");
        }
        return DEFAULT_LOCATION;
    }

    @Override
    protected void setupRotations(@Nonnull AquaFishRenderState renderState, @Nonnull PoseStack matrixStack, float partialTicks, float f) {
        super.setupRotations(renderState, matrixStack, partialTicks, f);
        FishType fishType = renderState.fishType;
        if (fishType != FishType.JELLYFISH) {
            float salmonRotation = 1.0F;
            float salmonMultiplier = 1.0F;
            if (fishType == FishType.LONGNOSE) {
                if (!renderState.isInWater) {
                    salmonRotation = 1.3F;
                    salmonMultiplier = 1.7F;
                }
            }
            float fishRotation = fishType == FishType.LONGNOSE ? salmonRotation * 4.3F * Mth.sin(salmonMultiplier * 0.6F * renderState.ageInTicks) : 4.3F * Mth.sin(0.6F * renderState.ageInTicks);

            matrixStack.mulPose(Axis.YP.rotationDegrees(fishRotation));
            if (fishType == FishType.LONGNOSE) {
                matrixStack.translate(0.0F, 0.0F, -0.4F);
            }
            if (!renderState.isInWater && fishType != FishType.HALIBUT) {
                if (fishType == FishType.MEDIUM || fishType == FishType.LARGE || fishType == FishType.CATFISH) {
                    matrixStack.translate(0.1F, 0.1F, -0.1F);
                } else {
                    matrixStack.translate(0.2F, 0.1F, 0.0F);
                }
                matrixStack.mulPose(Axis.ZP.rotationDegrees(90));
            }
            if (fishType == FishType.HALIBUT) {
                matrixStack.translate(-0.4F, 0.1F, 0.0F);
                matrixStack.mulPose(Axis.ZP.rotationDegrees(-90));
            }
        }
    }

    @Override
    protected void scale(AquaFishRenderState renderState, @Nonnull PoseStack matrixStack) {
        ResourceLocation location = renderState.byName;
        float scale = 0.0F;
        if (location != null) {
            switch (location.getPath()) {
                case "minnow" -> scale = 0.5F;
                case "synodontis" -> scale = 0.8F;
                case "brown_trout", "piranha" -> scale = 0.9F;
                case "pollock" -> scale = 1.1F;
                case "atlantic_cod", "blackfish", "catfish", "tambaqui" -> scale = 1.2F;
                case "pacific_halibut", "atlantic_halibut", "capitaine", "largemouth_bass", "gar", "arapaima", "tuna" -> scale = 1.4F;
            }
        }
        if (scale > 0) {
            matrixStack.pushPose();
            matrixStack.scale(scale, scale, scale);
            matrixStack.popPose();
        }
    }

    @Override
    @Nonnull
    public AquaFishRenderState createRenderState() {
        return new AquaFishRenderState();
    }

    @Override
    public void extractRenderState(@Nonnull AquaFishEntity fishEntity, @Nonnull AquaFishRenderState renderState, float partialTicks) {
        super.extractRenderState(fishEntity, renderState, partialTicks);
        renderState.fishType = fishEntity.getFishType();
        renderState.byName = fishEntity.byName();
    }
}