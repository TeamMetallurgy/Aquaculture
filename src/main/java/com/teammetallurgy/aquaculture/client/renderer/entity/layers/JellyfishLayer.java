package com.teammetallurgy.aquaculture.client.renderer.entity.layers;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.teammetallurgy.aquaculture.client.renderer.entity.model.JellyfishModel;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.LivingEntity;

import javax.annotation.Nonnull;

public class JellyfishLayer<T extends LivingEntity> extends LayerRenderer<T, JellyfishModel<T>> {
    private final JellyfishModel<T> jellyfishModel = new JellyfishModel<>();

    public JellyfishLayer(IEntityRenderer<T, JellyfishModel<T>> renderer) {
        super(renderer);
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer buffer, int i, T jellyfish, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
        if (!jellyfish.isInvisible()) {
            this.getEntityModel().copyModelAttributesTo(this.jellyfishModel);
            this.jellyfishModel.setLivingAnimations(jellyfish, p_225628_5_, p_225628_6_, p_225628_7_);
            this.jellyfishModel.setRotationAngles(jellyfish, p_225628_5_, p_225628_6_, p_225628_8_, p_225628_9_, p_225628_10_);
            IVertexBuilder lvt_11_1_ = buffer.getBuffer(RenderType.getEntityTranslucent(this.getEntityTexture(jellyfish)));
            this.jellyfishModel.getParts().forEach((p_228272_8_) -> {
                p_228272_8_.render(matrixStack, lvt_11_1_, i, LivingRenderer.getPackedOverlay(jellyfish, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
            });
        }
    }
}