package com.teammetallurgy.aquaculture.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.client.ClientHandler;
import com.teammetallurgy.aquaculture.client.model.TackleBoxModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Vector3f;

import javax.annotation.Nonnull;
import java.util.Set;

public class TackleBoxSpecialRenderer implements NoDataSpecialModelRenderer {
    public static final ResourceLocation TACKLE_BOX = ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, "textures/block/tackle_box.png");
    private final TackleBoxModel model;
    private final float openness;

    public TackleBoxSpecialRenderer(TackleBoxModel model, float openness) {
        this.model = model;
        this.openness = openness;
    }

    @Override
    public void submit(@Nonnull ItemDisplayContext displayContext, @Nonnull PoseStack poseStack, @Nonnull SubmitNodeCollector nodeCollector, int packedLight, int packedOverlay, boolean hasFoil, int outlineColor) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(-Direction.NORTH.toYRot()));
        poseStack.translate(-1.0F, 1.125F, -0.5F); //Translate
        poseStack.mulPose(Axis.XN.rotationDegrees(-180)); //Flip

        nodeCollector.submitModelPart(
                this.model.root(),
                poseStack,
                this.model.renderType(TACKLE_BOX),
                packedLight,
                packedOverlay,
                null,
                false,
                hasFoil,
                -1,
                null,
                outlineColor
        );
        poseStack.popPose();
    }

    @Override
    public void getExtents(Set<Vector3f> output) {
        PoseStack posestack = new PoseStack();
        this.model.root().getExtentsForGui(posestack, output);
    }

    public record Unbaked(float openness) implements SpecialModelRenderer.Unbaked {
        public static final MapCodec<TackleBoxSpecialRenderer.Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(
                r -> r.group(
                        Codec.FLOAT.optionalFieldOf("angle", 0.0F).forGetter(TackleBoxSpecialRenderer.Unbaked::openness)
                ).apply(r, TackleBoxSpecialRenderer.Unbaked::new)
        );

        public Unbaked() {
            this(0.0F);
        }

        @Override
        @Nonnull
        public MapCodec<TackleBoxSpecialRenderer.Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public SpecialModelRenderer<?> bake(@Nonnull SpecialModelRenderer.BakingContext context) {
            TackleBoxModel tackleBoxModel = new TackleBoxModel(context.entityModelSet().bakeLayer(ClientHandler.TACKLE_BOX));
            return new TackleBoxSpecialRenderer(tackleBoxModel, this.openness);
        }
    }
}