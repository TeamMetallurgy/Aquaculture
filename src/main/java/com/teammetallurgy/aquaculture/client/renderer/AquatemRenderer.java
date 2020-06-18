package com.teammetallurgy.aquaculture.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.teammetallurgy.aquaculture.block.NeptunesBountyBlock;
import com.teammetallurgy.aquaculture.block.TackleBoxBlock;
import com.teammetallurgy.aquaculture.block.tileentity.NeptunesBountyTileEntity;
import com.teammetallurgy.aquaculture.block.tileentity.TackleBoxTileEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class AquatemRenderer extends ItemStackTileEntityRenderer {
    private static final NeptunesBountyTileEntity NEPTUNES_BOUNTY = new NeptunesBountyTileEntity();
    private static final TackleBoxTileEntity TACKLE_BOX = new TackleBoxTileEntity();

    @Override
    public void render(@Nonnull ItemStack stack, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer buffer, int i, int i1) {
        Item item = stack.getItem();
        boolean isBlockItem = item instanceof BlockItem;
        if (isBlockItem && ((BlockItem) item).getBlock() instanceof TackleBoxBlock) {
            TileEntityRendererDispatcher.instance.renderItem(TACKLE_BOX, matrixStack, buffer, i, i1);
        } else if (isBlockItem && ((BlockItem) item).getBlock() instanceof NeptunesBountyBlock) {
            TileEntityRendererDispatcher.instance.renderItem(NEPTUNES_BOUNTY, matrixStack, buffer, i, i1);
        }
    }
}