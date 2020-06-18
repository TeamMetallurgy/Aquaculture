package com.teammetallurgy.aquaculture.client;

import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.client.gui.screen.TackleBoxScreen;
import com.teammetallurgy.aquaculture.client.renderer.entity.AquaBobberRenderer;
import com.teammetallurgy.aquaculture.client.renderer.entity.AquaFishRenderer;
import com.teammetallurgy.aquaculture.client.renderer.entity.FishMountRenderer;
import com.teammetallurgy.aquaculture.client.renderer.entity.TurtleLandRenderer;
import com.teammetallurgy.aquaculture.client.renderer.tileentity.NeptunesBountyRenderer;
import com.teammetallurgy.aquaculture.client.renderer.tileentity.TackleBoxRenderer;
import com.teammetallurgy.aquaculture.entity.AquaFishEntity;
import com.teammetallurgy.aquaculture.entity.FishMountEntity;
import com.teammetallurgy.aquaculture.init.*;
import com.teammetallurgy.aquaculture.item.DyeableItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.entity.TippedArrowRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Aquaculture.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientHandler {

    public static void setupClient() {
        ScreenManager.registerFactory(AquaGuis.TACKLE_BOX, TackleBoxScreen::new);
        ClientRegistry.bindTileEntityRenderer(AquaBlocks.AquaTileEntities.NEPTUNES_BOUNTY, NeptunesBountyRenderer::new);
        ClientRegistry.bindTileEntityRenderer(AquaBlocks.AquaTileEntities.TACKLE_BOX,  TackleBoxRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(AquaEntities.BOBBER, AquaBobberRenderer::new);
        for (EntityType<AquaFishEntity> fish : FishRegistry.fishEntities) {
            RenderingRegistry.registerEntityRenderingHandler(fish, (manager) -> new AquaFishRenderer(manager, fish.getRegistryName() != null && fish.getRegistryName().equals(new ResourceLocation(Aquaculture.MOD_ID, "jellyfish"))));
        }
        RenderingRegistry.registerEntityRenderingHandler(AquaEntities.WATER_ARROW, TippedArrowRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(AquaEntities.BOX_TURTLE, TurtleLandRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(AquaEntities.ARRAU_TURTLE, TurtleLandRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(AquaEntities.STARSHELL_TURTLE, TurtleLandRenderer::new);
        for (EntityType<FishMountEntity> fishMount : FishRegistry.fishMounts) {
            RenderingRegistry.registerEntityRenderingHandler(fishMount, FishMountRenderer::new);
        }
        //Item Colors
        ItemColors itemColor = Minecraft.getInstance().getItemColors();
        itemColor.register((stack, tintIndex) -> tintIndex > 0 ? -1 : ((DyeableItem) stack.getItem()).getColor(stack), AquaItems.FISHING_LINE, AquaItems.BOBBER);
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ModelLoader.addSpecialModel(FishMountRenderer.OAK);
        ModelLoader.addSpecialModel(FishMountRenderer.SPRUCE);
        ModelLoader.addSpecialModel(FishMountRenderer.BIRCH);
        ModelLoader.addSpecialModel(FishMountRenderer.JUNGLE);
        ModelLoader.addSpecialModel(FishMountRenderer.ACACIA);
        ModelLoader.addSpecialModel(FishMountRenderer.DARK_OAK);
    }
}