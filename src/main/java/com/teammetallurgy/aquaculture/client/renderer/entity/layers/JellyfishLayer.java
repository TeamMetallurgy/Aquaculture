package com.teammetallurgy.aquaculture.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teammetallurgy.aquaculture.client.ClientHandler;
import com.teammetallurgy.aquaculture.client.renderer.entity.model.JellyfishModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nonnull;

public class JellyfishLayer<T extends LivingEntity> extends RenderLayer<T, JellyfishModel<T>> {
    private final JellyfishModel<T> jellyfishModel;

    public JellyfishLayer(RenderLayerParent<T, JellyfishModel<T>> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.jellyfishModel = new JellyfishModel<>(modelSet.bakeLayer(ClientHandler.JELLYFISH_MODEL));
    }

    @Override
    public void render(@Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int i, T jellyfish, float v, float v1) {
        if (!jellyfish.isInvisible()) {
            this.getParentModel().copyPropertiesTo(this.jellyfishModel);
            this.jellyfishModel.prepareMobModel(jellyfish, p_225628_5_, p_225628_6_, p_225628_7_);
            this.jellyfishModel.setupAnim(jellyfish, v, v1);
            VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(jellyfish)));
            this.jellyfishModel.parts().forEach((p_228272_8_) -> {
                p_228272_8_.render(poseStack, vertexConsumer, i, LivingEntityRenderer.getOverlayCoords(jellyfish, 0.0F));
            });
        }
    }
}