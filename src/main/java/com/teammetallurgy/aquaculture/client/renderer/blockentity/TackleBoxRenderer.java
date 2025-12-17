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
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class TackleBoxRenderer<T extends TackleBoxBlockEntity & LidBlockEntity> implements BlockEntityRenderer<T, TackleBoxRenderState> {
    public static final Identifier TACKLE_BOX_TEXTURE = Identifier.fromNamespaceAndPath(Aquaculture.MOD_ID, "textures/block/tackle_box.png");
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
    }

    @Override
    public void submit(TackleBoxRenderState renderState, PoseStack poseStack, SubmitNodeCollector nodeCollector, @Nonnull CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        poseStack.translate(0.5D, 0.5D, 0.5D);
        poseStack.mulPose(Axis.YP.rotationDegrees(-renderState.angle));
        poseStack.translate(-0.5D, -0.5D, -0.5D);
        poseStack.translate(0.0625F, 1.125F, 0.5F); //Translate
        poseStack.mulPose(Axis.XN.rotationDegrees(-180)); //Flip

        this.tackleBoxModel.setupAnim(renderState);
        nodeCollector.submitModel(this.tackleBoxModel, renderState, poseStack, RenderTypes.entityCutout(TACKLE_BOX_TEXTURE), renderState.lightCoords, OverlayTexture.NO_OVERLAY, -1, null, 0, renderState.breakProgress);

        poseStack.popPose();
    }
}