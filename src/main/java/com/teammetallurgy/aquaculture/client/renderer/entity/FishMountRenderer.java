package com.teammetallurgy.aquaculture.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.teammetallurgy.aquaculture.client.ClientHandler;
import com.teammetallurgy.aquaculture.client.renderer.entity.state.FishMountRenderState;
import com.teammetallurgy.aquaculture.entity.AquaFishEntity;
import com.teammetallurgy.aquaculture.entity.FishMountEntity;
import com.teammetallurgy.aquaculture.entity.FishType;
import com.teammetallurgy.aquaculture.init.AquaDataComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Pufferfish;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;

public class FishMountRenderer<T extends FishMountEntity> extends EntityRenderer<T, FishMountRenderState> {
    private final Minecraft mc = Minecraft.getInstance();

    public FishMountRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void submit(@Nonnull FishMountRenderState renderState, @Nonnull PoseStack poseStack, @Nonnull SubmitNodeCollector nodeCollector, @Nonnull CameraRenderState cameraRenderState) {
        super.submit(renderState, poseStack, nodeCollector, cameraRenderState);
        poseStack.pushPose();
        Direction direction = renderState.direction;
        Vec3 pos = this.getRenderOffset(renderState);
        poseStack.translate(-pos.x(), -pos.y(), -pos.z());
        double multiplier = 0.46875D;
        poseStack.translate((double) direction.getStepX() * multiplier, (double) direction.getStepY() * multiplier, (double) direction.getStepZ() * multiplier);
        float x;
        float y;
        if (direction.getAxis().isHorizontal()) {
            x = 0.0F;
            y = 180.0F - direction.toYRot();
        } else {
            x = (float)(-90 * direction.getAxisDirection().getStep());
            y = 180.0F;
        }
        poseStack.mulPose(Axis.XP.rotationDegrees(x));
        poseStack.mulPose(Axis.YP.rotationDegrees(y));
        if (!renderState.isInvisible) {
            BlockRenderDispatcher rendererDispatcher = this.mc.getBlockRenderer();
            ModelManager manager = rendererDispatcher.getBlockModelShaper().getModelManager();

            poseStack.pushPose();
            poseStack.translate(-0.5D, -0.5D, -0.5D);
            ResourceLocation entityTypeID = renderState.byName;
            if (entityTypeID != null) {
                BlockStateModel model = manager.getStandaloneModel(getStandaloneKeyFromType(entityTypeID.getPath()));
                if (model != null) {
                    nodeCollector.submitBlockModel(
                            poseStack,
                            RenderType.entitySolidZOffsetForward(TextureAtlas.LOCATION_BLOCKS),
                            model,
                            1.0F,
                            1.0F,
                            1.0F,
                            renderState.lightCoords,
                            OverlayTexture.NO_OVERLAY,
                            renderState.outlineColor
                    );
                }
            }
            poseStack.popPose();
        }
        this.renderFish(renderState, poseStack, nodeCollector, cameraRenderState);
        poseStack.popPose();
    }

    private void renderFish(@Nonnull FishMountRenderState renderState, @Nonnull PoseStack poseStack, @Nonnull SubmitNodeCollector nodeCollector, @Nonnull CameraRenderState cameraRenderState) {
        Entity entity = renderState.mountedFish;
        if (entity instanceof Mob fish) {
            double x = 0.0D;
            double y = 0.0D;
            double depth = 0.42D;
            if (fish instanceof Pufferfish) {
                depth += 0.09D;
            } else if (fish instanceof AquaFishEntity && ((AquaFishEntity) fish).getFishType().equals(FishType.LONGNOSE)) {
                x = -0.1F;
                y = -0.18D;
            }
            fish.setNoAi(true);
            poseStack.translate(x, y, depth);
            poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
            if (renderState.fishRenderState != null) {
                this.entityRenderDispatcher.submit(renderState.fishRenderState, cameraRenderState, 0.0D, 0.0D, 0.0F, poseStack, nodeCollector);
            }
        }
    }

    @Override
    @Nonnull
    public Vec3 getRenderOffset(FishMountRenderState fishMount) {
        return new Vec3((float) fishMount.direction.getStepX() * 0.3F, -0.25D, (float) fishMount.direction.getStepZ() * 0.3F);
    }

    @Override
    protected boolean shouldShowName(@Nonnull T fishMount, double distanceToCameraSq) {
        if (Minecraft.renderNames() && fishMount.entity != null && (this.mc.hitResult != null && fishMount.distanceToSqr(this.mc.hitResult.getLocation()) < 0.24D)) {
            double d0 = this.entityRenderDispatcher.distanceToSqr(fishMount);
            float sneaking = fishMount.isDiscrete() ? 32.0F : 64.0F;
            return d0 < (double) (sneaking * sneaking);
        } else {
            return false;
        }
    }

    @Override
    protected void submitNameTag(@Nonnull FishMountRenderState renderState, @Nonnull PoseStack poseStack, @Nonnull SubmitNodeCollector nodeCollector, @Nonnull CameraRenderState cameraRenderState) {
        //Render Fish Name
        if (renderState.mountedFish != null) {
            nodeCollector.submitNameTag(
                    poseStack,
                    renderState.nameTagAttachment,
                    0,
                    renderState.mountedFish.getName(),
                    !renderState.isDiscrete,
                    renderState.lightCoords,
                    renderState.distanceToCameraSq,
                    cameraRenderState
            );
        }

        ItemStack stack = renderState.stack;
        Float fishWeight = stack.get(AquaDataComponents.FISH_WEIGHT.get());
        if (stack.has(AquaDataComponents.FISH_WEIGHT) && fishWeight != null) {
            float weight = fishWeight;
            String lb = weight == 1.0D ? " lb" : " lbs";

            DecimalFormat df = new DecimalFormat("#,###.##");
            BigDecimal bd = new BigDecimal(weight);
            bd = bd.round(new MathContext(3));

            poseStack.pushPose();
            poseStack.translate(0.0D, -0.25D, 0.0D); //Adjust weight label height
            if (bd.doubleValue() > 999) {
                nodeCollector.submitNameTag(
                        poseStack,
                        renderState.nameTagAttachment,
                        0,
                        Component.translatable("aquaculture.fishWeight.weight", df.format((int) bd.doubleValue()) + lb),
                        !renderState.isDiscrete,
                        renderState.lightCoords,
                        renderState.distanceToCameraSq,
                        cameraRenderState
                );
            } else {
                nodeCollector.submitNameTag(
                        poseStack,
                        renderState.nameTagAttachment,
                        0,
                        Component.translatable("aquaculture.fishWeight.weight", bd + lb),
                        !renderState.isDiscrete,
                        renderState.lightCoords,
                        renderState.distanceToCameraSq,
                        cameraRenderState
                );
            }
            poseStack.popPose();
        }
    }

    @Override
    @Nonnull
    public FishMountRenderState createRenderState() {
        return new FishMountRenderState();
    }

    @Override
    public void extractRenderState(@Nonnull T entity, @Nonnull FishMountRenderState renderState, float partialTicks) {
        super.extractRenderState(entity, renderState, partialTicks);
        renderState.direction = entity.getDirection();
        renderState.stack = entity.getItem();
        renderState.byName = entity.byName();
        renderState.mountedFish = entity.entity;
        if (entity.level() != null && renderState.mountedFish != null) {
            renderState.fishRenderState = this.entityRenderDispatcher.extractEntity(renderState.mountedFish, 0.0F);
            renderState.fishRenderState.lightCoords = renderState.lightCoords;
        }
    }


    public StandaloneModelKey<BlockStateModel> getStandaloneKeyFromType(String mountType) {
        return switch (mountType) {
            case "spruce_fish_mount" -> ClientHandler.SPRUCE_FISH_MOUNT_KEY;
            case "birch_fish_mount" -> ClientHandler.BIRCH_FISH_MOUNT_KEY;
            case "jungle_fish_mount" -> ClientHandler.JUNGLE_FISH_MOUNT_KEY;
            case "acacia_fish_mount" -> ClientHandler.ACACIA_FISH_MOUNT_KEY;
            case "dark_oak_fish_mount" -> ClientHandler.DARK_OAK_FISH_MOUNT_KEY;
            default -> ClientHandler.OAK_FISH_MOUNT_KEY;
        };
    }
}