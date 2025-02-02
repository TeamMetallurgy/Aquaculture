package com.teammetallurgy.aquaculture.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.block.TackleBoxBlock;
import com.teammetallurgy.aquaculture.block.blockentity.TackleBoxBlockEntity;
import com.teammetallurgy.aquaculture.client.ClientHandler;
import com.teammetallurgy.aquaculture.client.model.TackleBoxModel;
import com.teammetallurgy.aquaculture.init.AquaBlocks;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public class TackleBoxRenderer <T extends TackleBoxBlockEntity> implements BlockEntityRenderer<T> {
    public static final ResourceLocation TACKLE_BOX_TEXTURE = ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, "textures/block/tackle_box.png");
    public final TackleBoxModel tackleBoxModel;

    public TackleBoxRenderer(BlockEntityRendererProvider.Context context) {
        this.tackleBoxModel = new TackleBoxModel(context.bakeLayer(ClientHandler.TACKLE_BOX));
    }

    @Override
    public void render(@Nonnull T tackleBox, float partialTicks, @Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        Level world = tackleBox.getLevel();
        boolean hasWorld = world != null;
        BlockState state = hasWorld ? tackleBox.getBlockState() : AquaBlocks.TACKLE_BOX.get().defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH);
        Block block = state.getBlock();
        if (block instanceof TackleBoxBlock) {
            matrixStack.pushPose();
            matrixStack.translate(0.5D, 0.5D, 0.5D);
            float facing = state.getValue(TackleBoxBlock.FACING).toYRot();
            matrixStack.mulPose(Axis.YP.rotationDegrees(-facing));
            matrixStack.translate(-0.5D, -0.5D, -0.5D);
            matrixStack.translate(0.0625F, 1.125F, 0.5F); //Translate
            matrixStack.mulPose(Axis.XN.rotationDegrees(-180)); //Flip

            DoubleBlockCombiner.NeighborCombineResult<?> callbackWrapper = DoubleBlockCombiner.Combiner::acceptNone;
            float angle = tackleBox.getOpenNess(partialTicks);
            angle = 1.0F - angle;
            angle = 1.0F - angle * angle * angle;
            int brightness = ((Int2IntFunction) callbackWrapper.apply(new BrightnessCombiner())).applyAsInt(combinedLight);
            VertexConsumer tackleBoxBuilder = buffer.getBuffer(RenderType.entityCutout(TACKLE_BOX_TEXTURE));
            this.render(matrixStack, tackleBoxBuilder, this.tackleBoxModel, angle, brightness, combinedOverlay);
            matrixStack.popPose();
        }
    }

    private void render(PoseStack poseStack, VertexConsumer buffer, TackleBoxModel tackleBoxModel, float angle, int packedLight, int packedOverlay) {
        tackleBoxModel.setupAnim(angle);
        tackleBoxModel.renderToBuffer(poseStack, buffer, packedLight, packedOverlay);
    }
}