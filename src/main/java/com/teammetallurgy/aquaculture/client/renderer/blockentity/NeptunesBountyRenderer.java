package com.teammetallurgy.aquaculture.client.renderer.blockentity;

import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.block.blockentity.NeptunesBountyBlockEntity;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.renderer.blockentity.state.ChestRenderState;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class NeptunesBountyRenderer extends ChestRenderer<NeptunesBountyBlockEntity> {

    public NeptunesBountyRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected @Nullable SpriteId getCustomSprite(NeptunesBountyBlockEntity blockEntity, @Nonnull ChestRenderState renderState) {
        return new SpriteId(Sheets.CHEST_SHEET, Identifier.fromNamespaceAndPath(Aquaculture.MOD_ID, "entity/chest/neptunes_bounty"));
    }
}