package com.teammetallurgy.aquaculture.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teammetallurgy.aquaculture.client.ClientHandler;
import com.teammetallurgy.aquaculture.client.renderer.entity.AquaFishRenderer;
import com.teammetallurgy.aquaculture.client.renderer.entity.model.FishBaseModel;
import com.teammetallurgy.aquaculture.client.renderer.entity.model.JellyfishModel;
import com.teammetallurgy.aquaculture.client.renderer.entity.state.AquaFishRenderState;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;

import javax.annotation.Nonnull;

public class JellyfishLayer extends RenderLayer<AquaFishRenderState, FishBaseModel> {
    private final JellyfishModel jellyfishModel;

    public JellyfishLayer(RenderLayerParent<AquaFishRenderState, FishBaseModel> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.jellyfishModel = new JellyfishModel(modelSet.bakeLayer(ClientHandler.JELLYFISH_MODEL));
    }

    @Override
    public void render(@Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int i, AquaFishRenderState jellyfish, float v, float v1) {
        if (!jellyfish.isInvisible) {
            this.jellyfishModel.setupAnim(jellyfish);
            VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(AquaFishRenderer.JELLYFISH));

            this.jellyfishModel.renderToBuffer(poseStack, vertexConsumer, i, LivingEntityRenderer.getOverlayCoords(jellyfish, 0.0F));
        }
    }
}