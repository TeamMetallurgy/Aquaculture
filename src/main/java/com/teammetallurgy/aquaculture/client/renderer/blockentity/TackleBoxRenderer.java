package com.teammetallurgy.aquaculture.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.block.TackleBoxBlock;
import com.teammetallurgy.aquaculture.block.blockentity.TackleBoxBlockEntity;
import com.teammetallurgy.aquaculture.client.ClientHandler;
import com.teammetallurgy.aquaculture.client.model.TackleBoxModel;
import com.teammetallurgy.aquaculture.client.renderer.blockentity.state.TackleBoxRenderState;
import com.teammetallurgy.aquaculture.init.AquaBlocks;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class TackleBoxRenderer<T extends TackleBoxBlockEntity> implements BlockEntityRenderer<T, TackleBoxRenderState> {
    public static final ResourceLocation TACKLE_BOX_TEXTURE = ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, "textures/block/tackle_box.png");
    public final TackleBoxModel tackleBoxModel;

    public TackleBoxRenderer(BlockEntityRendererProvider.Context context) {
        this.tackleBoxModel = new TackleBoxModel(context.bakeLayer(ClientHandler.TACKLE_BOX));
    }

    @Override
    @Nonnull
    public TackleBoxRenderState createRenderState() {
        return new TackleBoxRenderState();
    }

    @Override
    public void extractRenderState(@Nonnull T tackleBox, @Nonnull TackleBoxRenderState renderState, float partialTick, @Nonnull Vec3 cameraPosition, @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(tackleBox, renderState, partialTick, cameraPosition, breakProgress);
        boolean hasLevel = tackleBox.getLevel() != null;
        BlockState blockstate = hasLevel ? tackleBox.getBlockState() : AquaBlocks.TACKLE_BOX.get().defaultBlockState().setValue(TackleBoxBlock.FACING, Direction.SOUTH);
        renderState.angle = blockstate.getValue(TackleBoxBlock.FACING).toYRot();
        renderState.openness = tackleBox.getOpenNess(partialTick);

        DoubleBlockCombiner.NeighborCombineResult<?> neighborCombineResult = DoubleBlockCombiner.Combiner::acceptNone;
        renderState.lightCoords = ((Int2IntFunction) neighborCombineResult.apply(new BrightnessCombiner())).applyAsInt(renderState.lightCoords);
    }

    @Override
    public void submit(TackleBoxRenderState renderState, PoseStack poseStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        poseStack.translate(0.5D, 0.5D, 0.5D);
        poseStack.mulPose(Axis.YP.rotationDegrees(-renderState.angle));
        poseStack.translate(-0.5D, -0.5D, -0.5D);
        poseStack.translate(0.0625F, 1.125F, 0.5F); //Translate
        poseStack.mulPose(Axis.XN.rotationDegrees(-180)); //Flip

        float openness = renderState.openness;
        openness = 1.0F - openness;
        openness = 1.0F - openness * openness * openness;

        nodeCollector.submitModel(this.tackleBoxModel, openness, poseStack, RenderType.entityCutout(TACKLE_BOX_TEXTURE), renderState.lightCoords, OverlayTexture.NO_OVERLAY, -1, null, 0, renderState.breakProgress);

        poseStack.popPose();
    }
}