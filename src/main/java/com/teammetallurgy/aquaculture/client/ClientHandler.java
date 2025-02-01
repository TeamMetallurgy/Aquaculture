package com.teammetallurgy.aquaculture.client;

import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.client.gui.screen.TackleBoxScreen;
import com.teammetallurgy.aquaculture.client.renderer.entity.AquaBobberRenderer;
import com.teammetallurgy.aquaculture.client.renderer.entity.AquaFishRenderer;
import com.teammetallurgy.aquaculture.client.renderer.entity.FishMountRenderer;
import com.teammetallurgy.aquaculture.client.renderer.entity.TurtleLandRenderer;
import com.teammetallurgy.aquaculture.client.renderer.entity.model.*;
import com.teammetallurgy.aquaculture.client.renderer.blockentity.NeptunesBountyRenderer;
import com.teammetallurgy.aquaculture.client.renderer.blockentity.TackleBoxRenderer;
import com.teammetallurgy.aquaculture.entity.AquaFishEntity;
import com.teammetallurgy.aquaculture.entity.FishMountEntity;
import com.teammetallurgy.aquaculture.init.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.SpectralArrowRenderer;
import net.minecraft.client.renderer.entity.TippableArrowRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

@EventBusSubscriber(modid = Aquaculture.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ClientHandler {
    public static final ModelLayerLocation TACKLE_BOX = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, "tackle_box"), "tackle_box");
    public static final ModelLayerLocation TURTLE_LAND_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, "turtle_land"), "turtle_land");
    public static final ModelLayerLocation TURTLE_LAND_BABY_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, "turtle_land"), "turtle_land_baby");
    public static final ModelLayerLocation SMALL_MODEL = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, "small_model"), "small_model");
    public static final ModelLayerLocation MEDIUM_MODEL = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, "medium_model"), "medium_model");
    public static final ModelLayerLocation LARGE_MODEL = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, "large_model"), "large_model");
    public static final ModelLayerLocation LONGNOSE_MODEL = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, "longnose_model"), "longnose_model");
    public static final ModelLayerLocation CATFISH_MODEL = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, "catfish_model"), "catfish_model");
    public static final ModelLayerLocation TROPICAL_FISH_B = new ModelLayerLocation(ModelLayers.TROPICAL_FISH_SMALL.model(), "tropical_fish_b");
    public static final ModelLayerLocation JELLYFISH_MODEL = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, "jellyfish_model"), "jellyfish_model");

    public static void setupClient() {
        BlockEntityRenderers.register(AquaBlockEntities.NEPTUNES_BOUNTY.get(), NeptunesBountyRenderer::new);
        BlockEntityRenderers.register(AquaBlockEntities.TACKLE_BOX.get(), TackleBoxRenderer::new);
    }

    @SubscribeEvent
    public static void registerMenuScreen(RegisterMenuScreensEvent event) {
        event.register(AquaGuis.TACKLE_BOX.get(), TackleBoxScreen::new);
    }

    @SubscribeEvent
    public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(AquaEntities.BOBBER.get(), AquaBobberRenderer::new);
        for (DeferredHolder<EntityType<?>, EntityType<AquaFishEntity>> fish : FishRegistry.fishEntities) {
            event.registerEntityRenderer(fish.get(), AquaFishRenderer::new);
        }
        event.registerEntityRenderer(AquaEntities.WATER_ARROW.get(), TippableArrowRenderer::new);
        event.registerEntityRenderer(AquaEntities.SPECTRAL_WATER_ARROW.get(), SpectralArrowRenderer::new);
        event.registerEntityRenderer(AquaEntities.BOX_TURTLE.get(), TurtleLandRenderer::new);
        event.registerEntityRenderer(AquaEntities.ARRAU_TURTLE.get(), TurtleLandRenderer::new);
        event.registerEntityRenderer(AquaEntities.STARSHELL_TURTLE.get(), TurtleLandRenderer::new);
        for (DeferredHolder<EntityType<?>, EntityType<FishMountEntity>> fishMount : FishRegistry.fishMounts) {
            event.registerEntityRenderer(fishMount.get(), FishMountRenderer::new);
        }
    }

    @SubscribeEvent
    public static void registerLayerDefinition(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(TACKLE_BOX, TackleBoxRenderer::createLayer);
        event.registerLayerDefinition(TURTLE_LAND_LAYER, TurtleLandModel::createBodyLayer);
        event.registerLayerDefinition(TURTLE_LAND_BABY_LAYER, () -> TurtleLandModel.createBodyLayer().apply(TurtleLandModel.BABY_TRANSFORMER));
        event.registerLayerDefinition(SMALL_MODEL, FishSmallModel::createBodyLayer);
        event.registerLayerDefinition(MEDIUM_MODEL, FishMediumModel::createBodyLayer);
        event.registerLayerDefinition(LARGE_MODEL, FishLargeModel::createBodyLayer);
        event.registerLayerDefinition(LONGNOSE_MODEL, FishLongnoseModel::createBodyLayer);
        event.registerLayerDefinition(CATFISH_MODEL, FishCathfishModel::createBodyLayer);
        event.registerLayerDefinition(TROPICAL_FISH_B, () -> AquaTropicalFishBModel.createBodyLayer(CubeDeformation.NONE));
        event.registerLayerDefinition(JELLYFISH_MODEL, JellyfishModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerModels(ModelEvent.RegisterAdditional event) { //TODO Might be broken? Fish mounts not rendering at last
        event.register(ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, "block/oak_fish_mount"));
        event.register(ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, "block/spruce_fish_mount"));
        event.register(ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, "block/birch_fish_mount"));
        event.register(ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, "block/jungle_fish_mount"));
        event.register(ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, "block/acacia_fish_mount"));
        event.register(ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, "block/dark_oak_fish_mount"));
    }

            /*@SubscribeEvent
    public static void registerColors(RegisterColorHandlersEvent.Item event) { //TODO Move to Json
        event.register((stack, tintIndex) -> tintIndex > 0 ? -1 : DyedItemColor.getOrDefault(stack, ARGB.color(0, 0, 0)), AquaItems.FISHING_LINE.get());
        event.register((stack, tintIndex) -> tintIndex > 0 ? -1 : DyedItemColor.getOrDefault(stack, ARGB.color(193, 38, 38)), AquaItems.BOBBER.get());
    }*/

    public static void registerBowModelProperties(Item bow) { //TODO. Not sure, but probably should be done in new item jsons
        /*ItemProperties.register(bow, ResourceLocation.withDefaultNamespace("pull"), (stack, level, entity, i) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return entity.getUseItem() != stack ? 0.0F : (float) (stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) / 20.0F;
            }
        });
        ItemProperties.register(bow, ResourceLocation.withDefaultNamespace("pulling"), (stack, level, entity, i) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
    */
    }
}