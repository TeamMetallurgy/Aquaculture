package com.teammetallurgy.aquaculture.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.client.ClientHandler;
import com.teammetallurgy.aquaculture.client.model.TackleBoxModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Vector3f;

import javax.annotation.Nonnull;
import java.util.Set;

public class TackleBoxSpecialRenderer implements NoDataSpecialModelRenderer {
    public static final Material TACKLE_BOX_MATERIAL = new Material(TextureAtlas.LOCATION_BLOCKS, ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, "block/tackle_box"));
    public static final ResourceLocation TACKLE_BOX = ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, "tackle_box");
    private final TackleBoxModel model;
    private final float openness;

    public TackleBoxSpecialRenderer(TackleBoxModel model, float openness) {
        this.model = model;
        this.openness = openness;
    }

    @Override
    public void submit(@Nonnull ItemDisplayContext displayContext, @Nonnull PoseStack poseStack, @Nonnull SubmitNodeCollector nodeCollector, int packedLight, int packedOverlay, boolean hasFoil, int outlineColor) {

        //poseStack.mulPose(Axis.YP.rotationDegrees(-Direction.NORTH.toYRot())); //TODO Figure out how to do these 3
        //poseStack.translate(-1.0F, 1.125F, -0.5F); //Translate
        //poseStack.mulPose(Axis.XN.rotationDegrees(-180)); //Flip

        nodeCollector.submitModel(
                this.model,
                this.openness,
                poseStack,
                TACKLE_BOX_MATERIAL.renderType(RenderType::entitySolid),
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