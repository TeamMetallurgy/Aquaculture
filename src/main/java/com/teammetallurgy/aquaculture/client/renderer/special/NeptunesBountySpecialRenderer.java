package com.teammetallurgy.aquaculture.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.client.ClientHandler;
import com.teammetallurgy.aquaculture.client.model.NeptunesBountyModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Vector3f;

import javax.annotation.Nonnull;
import java.util.Set;

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
    public void submit(@Nonnull ItemDisplayContext displayContext, @Nonnull PoseStack poseStack, @Nonnull SubmitNodeCollector nodeCollector, int packedLight, int packedOverlay, boolean hasFoil, int outlineColor) {
        nodeCollector.submitModel(
                this.model,
                this.openness,
                poseStack,
                this.material.renderType(RenderType::entitySolid),
                packedLight,
                packedOverlay,
                -1,
                null,
                outlineColor,
                null
        );
    }

    @Override
    public void getExtents(Set<Vector3f> output) {
        PoseStack posestack = new PoseStack();
        this.model.setupAnim(this.openness);
        this.model.root().getExtentsForGui(posestack, output);
    }

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
        public SpecialModelRenderer<?> bake(@Nonnull SpecialModelRenderer.BakingContext context) {
            NeptunesBountyModel neptunesBountyModel = new NeptunesBountyModel(context.entityModelSet().bakeLayer(ClientHandler.NEPTUNES_BOUNTY));
            Material material = Sheets.CHEST_MAPPER.apply(this.texture);
            return new NeptunesBountySpecialRenderer(neptunesBountyModel, material, this.openness);
        }
    }
}