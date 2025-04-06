package com.teammetallurgy.aquaculture.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.client.ClientHandler;
import com.teammetallurgy.aquaculture.client.model.NeptunesBountyModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class NeptunesBountySpecialRenderer implements NoDataSpecialModelRenderer {
    public static final ResourceLocation NEPTUNES_BOUNTY = ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, "neptunes");
    private final NeptunesBountyModel model;
    private final Material material;
    private final float openness;

    public NeptunesBountySpecialRenderer(NeptunesBountyModel model, Material material, float openness) {
        this.model = model;
        this.material = material;
        this.openness = openness;
    }

    @Override
    public void render(@Nonnull ItemDisplayContext displayContext, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource bufferSource, int packedLight, int packedOverlay, boolean b) {
        VertexConsumer vertexconsumer = this.material.buffer(bufferSource, RenderType::entitySolid);
        this.model.setupAnim(this.openness);
        this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, packedOverlay);
    }

    @OnlyIn(Dist.CLIENT)
    public static record Unbaked(ResourceLocation texture, float openness) implements SpecialModelRenderer.Unbaked {
        public static final MapCodec<NeptunesBountySpecialRenderer.Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(
                m -> m.group(
                                ResourceLocation.CODEC.fieldOf("texture").forGetter(NeptunesBountySpecialRenderer.Unbaked::texture),
                                Codec.FLOAT.optionalFieldOf("openness", 0.0F).forGetter(NeptunesBountySpecialRenderer.Unbaked::openness)
                        )
                        .apply(m, NeptunesBountySpecialRenderer.Unbaked::new)
        );

        public Unbaked(ResourceLocation location) {
            this(location, 0.0F);
        }

        @Override
        @Nonnull
        public MapCodec<NeptunesBountySpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public SpecialModelRenderer<?> bake(EntityModelSet modelSet) {
            NeptunesBountyModel neptunesBountyModel = new NeptunesBountyModel(modelSet.bakeLayer(ClientHandler.NEPTUNES_BOUNTY));
            Material material = Sheets.CHEST_MAPPER.apply(this.texture);
            return new NeptunesBountySpecialRenderer(neptunesBountyModel, material, this.openness);
        }
    }
}