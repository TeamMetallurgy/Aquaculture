package com.teammetallurgy.aquaculture.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.client.ClientHandler;
import com.teammetallurgy.aquaculture.client.model.TackleBoxModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Vector3f;

import javax.annotation.Nonnull;
import java.util.Set;

public class TackleBoxSpecialRenderer implements NoDataSpecialModelRenderer {
    public static final Material TACKLE_BOX_MATERIAL = new Material(TextureAtlas.LOCATION_BLOCKS, ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, "block/tackle_box"));
    public static final ResourceLocation TACKLE_BOX = ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, "tackle_box");
    private final TackleBoxModel model;
    private final float angle;

    public TackleBoxSpecialRenderer(TackleBoxModel model, float angle) {
        this.model = model;
        this.angle = angle;
    }

    @Override
    public void render(@Nonnull ItemDisplayContext displayContext, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource bufferSource, int packedLight, int packedOverlay, boolean b) {
        VertexConsumer vertexconsumer = TACKLE_BOX_MATERIAL.buffer(bufferSource, RenderType::entitySolid);
        poseStack.pushPose();
        this.model.setupAnim(this.angle);
        poseStack.mulPose(Axis.YP.rotationDegrees(-Direction.NORTH.toYRot()));
        poseStack.translate(-1.0F, 1.125F, -0.5F); //Translate
        poseStack.mulPose(Axis.XN.rotationDegrees(-180)); //Flip
        this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, packedOverlay);
        poseStack.popPose();
    }

    @Override
    public void getExtents(Set<Vector3f> output) {
        PoseStack posestack = new PoseStack();
        this.model.setupAnim(this.angle);
        this.model.root().getExtentsForGui(posestack, output);
    }

    @OnlyIn(Dist.CLIENT)
    public static record Unbaked(float angle) implements SpecialModelRenderer.Unbaked {
        public static final MapCodec<TackleBoxSpecialRenderer.Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(
                r -> r.group(
                        Codec.FLOAT.optionalFieldOf("angle", 0.0F).forGetter(TackleBoxSpecialRenderer.Unbaked::angle)
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
        public SpecialModelRenderer<?> bake(EntityModelSet modelSet) {
            TackleBoxModel tackleBoxModel = new TackleBoxModel(modelSet.bakeLayer(ClientHandler.TACKLE_BOX));
            return new TackleBoxSpecialRenderer(tackleBoxModel, this.angle);
        }
    }
}