package com.teammetallurgy.aquaculture.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.block.TackleBoxBlock;
import com.teammetallurgy.aquaculture.block.tileentity.TackleBoxTileEntity;
import com.teammetallurgy.aquaculture.init.AquaBlocks;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.DualBrightnessCallback;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.TileEntityMerger;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class TackleBoxRenderer <T extends TackleBoxTileEntity & IChestLid> extends TileEntityRenderer<T> {
    private static final ResourceLocation TACKLE_BOX_TEXTURE = new ResourceLocation(Aquaculture.MOD_ID, "textures/entity/tileentity/tackle_box.png");
    private static final RenderType TACKLE_BOX_RENDER = RenderType.getEntityCutout(TACKLE_BOX_TEXTURE);
    private ModelRenderer base;
    private ModelRenderer lid;
    private ModelRenderer handle;

    public TackleBoxRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
        this.lid = new ModelRenderer(64, 32, 0, 0);
        this.lid.setRotationPoint(7.0F, 12.0F, 4.0F);
        this.lid.addBox(-7.0F, -3.0F, -8.0F, 14, 3, 8, 0.0F);
        this.base = new ModelRenderer(64, 32, 0, 11);
        this.base.setRotationPoint(0.0F, 18.0F, 4.0F);
        this.base.addBox(0.0F, -6.0F, -8.0F, 14, 6, 8, 0.0F);
        this.handle = new ModelRenderer(64, 32, 36, 0);
        this.handle.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.handle.addBox(-2.0F, -4.0F, -5.0F, 4, 1, 2, 0.0F);
        this.lid.addChild(this.handle);
    }

    @Override
    public void render(@Nonnull T tackleBox, float partialTicks, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        World world = tackleBox.getWorld();
        boolean hasWorld = world != null;
        BlockState state = hasWorld ? tackleBox.getBlockState() : AquaBlocks.TACKLE_BOX.getDefaultState().with(ChestBlock.FACING, Direction.SOUTH);
        Block block = state.getBlock();
        if (block instanceof TackleBoxBlock) {
            matrixStack.push();
            matrixStack.translate(0.5D, 0.5D, 0.5D);
            float facing = state.get(TackleBoxBlock.FACING).getHorizontalAngle();
            matrixStack.rotate(Vector3f.YP.rotationDegrees(-facing));
            matrixStack.translate(-0.5D, -0.5D, -0.5D);
            matrixStack.translate(0.0625F, 1.125F, 0.5F); //Translate
            matrixStack.rotate(Vector3f.XN.rotationDegrees(-180)); //Flip

            TileEntityMerger.ICallbackWrapper<?> callbackWrapper = TileEntityMerger.ICallback::func_225537_b_;
            float angle = tackleBox.getLidAngle(partialTicks);
            angle = 1.0F - angle;
            angle = 1.0F - angle * angle * angle;
            int brightness = ((Int2IntFunction) callbackWrapper.apply(new DualBrightnessCallback())).applyAsInt(combinedLight);
            IVertexBuilder tackleBoxBuilder = buffer.getBuffer(TACKLE_BOX_RENDER);
            this.render(matrixStack, tackleBoxBuilder, this.base, this.lid, angle, brightness, combinedOverlay);
            matrixStack.pop();
        }
    }

    private void render(MatrixStack matrixStack, IVertexBuilder builder, ModelRenderer base, ModelRenderer lid, float angle, int brightness, int combinedOverlay) {
        lid.rotateAngleX = -(angle * 1.5707964F);
        base.render(matrixStack, builder, brightness, combinedOverlay);
        lid.render(matrixStack, builder, brightness, combinedOverlay);
    }
}