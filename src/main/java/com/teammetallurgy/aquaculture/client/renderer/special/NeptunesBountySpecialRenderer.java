package com.teammetallurgy.aquaculture.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.client.ClientHandler;
import com.teammetallurgy.aquaculture.client.model.NeptunesBountyModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Vector3fc;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class NeptunesBountySpecialRenderer implements NoDataSpecialModelRenderer {
    public static final Identifier NEPTUNES_BOUNTY = Identifier.fromNamespaceAndPath(Aquaculture.MOD_ID, "textures/block/neptunes_bounty.png");
    private final NeptunesBountyModel model;
    private final float openness;

    public NeptunesBountySpecialRenderer(NeptunesBountyModel model, float openness) {
        this.model = model;
        this.openness = openness;
    }

    @Override
    public void submit(@Nonnull ItemDisplayContext displayContext, @Nonnull PoseStack poseStack, @Nonnull SubmitNodeCollector nodeCollector, int packedLight, int packedOverlay, boolean hasFoil, int outlineColor) {
        nodeCollector.submitModel(
                this.model,
                this.openness,
                poseStack,
                this.model.renderType(NEPTUNES_BOUNTY),
                packedLight,
                packedOverlay,
                -1,
                null,
                outlineColor,
                null
        );
    }

    @Override
    public void getExtents(Consumer<Vector3fc> consumer) {
        PoseStack posestack = new PoseStack();
        this.model.setupAnim(this.openness);
        this.model.root().getExtentsForGui(posestack, consumer);
    }

    public static record Unbaked(float openness) implements SpecialModelRenderer.Unbaked {
        public static final MapCodec<NeptunesBountySpecialRenderer.Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(
                m -> m.group(
                                Codec.FLOAT.optionalFieldOf("openness", 0.0F).forGetter(NeptunesBountySpecialRenderer.Unbaked::openness)
                        )
                        .apply(m, NeptunesBountySpecialRenderer.Unbaked::new)
        );

        public Unbaked(Identifier location) {
            this(0.0F);
        }

        @Override
        @Nonnull
        public MapCodec<NeptunesBountySpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public SpecialModelRenderer<?> bake(@Nonnull SpecialModelRenderer.BakingContext context) {
            NeptunesBountyModel neptunesBountyModel = new NeptunesBountyModel(context.entityModelSet().bakeLayer(ClientHandler.NEPTUNES_BOUNTY));
            return new NeptunesBountySpecialRenderer(neptunesBountyModel, this.openness);
        }
    }
}