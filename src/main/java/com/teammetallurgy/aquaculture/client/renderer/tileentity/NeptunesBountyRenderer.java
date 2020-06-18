package com.teammetallurgy.aquaculture.client.renderer.tileentity;

import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.block.tileentity.NeptunesBountyTileEntity;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.tileentity.ChestTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.state.properties.ChestType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = Aquaculture.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NeptunesBountyRenderer extends ChestTileEntityRenderer<NeptunesBountyTileEntity> {
    private static final ResourceLocation NEPTUNES_BOUNTY = new ResourceLocation(Aquaculture.MOD_ID, "entity/tileentity/neptunes_bounty");

    public NeptunesBountyRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    @Nonnull
    protected Material getMaterial(@Nonnull NeptunesBountyTileEntity tileEntity, @Nonnull ChestType chestType) {
        return new Material(Atlases.CHEST_ATLAS, NEPTUNES_BOUNTY);
    }

    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent.Pre event) {
        if (event.getMap().getTextureLocation().equals(Atlases.CHEST_ATLAS)) {
            event.addSprite(NEPTUNES_BOUNTY);
        }
    }
}