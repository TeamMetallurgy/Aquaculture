package com.teammetallurgy.aquaculture.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.client.renderer.entity.state.FishMountRenderState;
import com.teammetallurgy.aquaculture.entity.AquaFishEntity;
import com.teammetallurgy.aquaculture.entity.FishMountEntity;
import com.teammetallurgy.aquaculture.entity.FishType;
import com.teammetallurgy.aquaculture.init.AquaDataComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ItemModelResolver;
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
    private final ItemModelResolver itemModelResolver;

    public FishMountRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemModelResolver = context.getItemModelResolver();
    }

    @Override
    public void render(@Nonnull FishMountRenderState renderState, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int i) {
        super.render(renderState, poseStack, buffer, i);
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
                ResourceLocation location = ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, "block/" + entityTypeID.getPath()); //Calling this instead of the fields for mod support

                BlockStateModel model = manager.getStandaloneModel(new StandaloneModelKey<>(location));
                if (model != null) { //TODO test
                    ModelBlockRenderer.renderModel(poseStack.last(), buffer.getBuffer(RenderType.entitySolidZOffsetForward(TextureAtlas.LOCATION_BLOCKS)), model, 1.0F, 1.0F, 1.0F, i, OverlayTexture.NO_OVERLAY);
                }
            }
            poseStack.popPose();
        }
        this.renderFish(renderState, poseStack, buffer, i);
        poseStack.popPose();
    }

    private void renderFish(FishMountRenderState renderState, PoseStack poseStack, MultiBufferSource buffer, int i) {
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
            this.mc.getEntityRenderDispatcher().render(fish, 0.0D, 0.0D, 0.0D, 0.0F, poseStack, buffer, i);
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
    protected void renderNameTag(@Nonnull FishMountRenderState renderState, @Nonnull Component name, @Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int i) {
        super.renderNameTag(renderState, renderState.mountedFish.getName(), matrixStack, buffer, i);

        ItemStack stack = renderState.stack;
        Float fishWeight = stack.get(AquaDataComponents.FISH_WEIGHT.get());
        if (stack.has(AquaDataComponents.FISH_WEIGHT) && fishWeight != null) {
            float weight = fishWeight;
            String lb = weight == 1.0D ? " lb" : " lbs";

            DecimalFormat df = new DecimalFormat("#,###.##");
            BigDecimal bd = new BigDecimal(weight);
            bd = bd.round(new MathContext(3));

            matrixStack.pushPose();
            matrixStack.translate(0.0D, -0.25D, 0.0D); //Adjust weight label height
            if (bd.doubleValue() > 999) {
                super.renderNameTag(renderState, Component.translatable("aquaculture.fishWeight.weight", df.format((int) bd.doubleValue()) + lb), matrixStack, buffer, i - 100);
            } else {
                super.renderNameTag(renderState, Component.translatable("aquaculture.fishWeight.weight", bd + lb), matrixStack, buffer, i);
            }
            matrixStack.popPose();
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
        ItemStack itemstack = entity.getItem();
        renderState.stack = itemstack;
        this.itemModelResolver.updateForNonLiving(renderState.item, itemstack, ItemDisplayContext.FIXED, entity);
        renderState.byName = entity.byName();
        renderState.mountedFish = entity.entity;
    }
}