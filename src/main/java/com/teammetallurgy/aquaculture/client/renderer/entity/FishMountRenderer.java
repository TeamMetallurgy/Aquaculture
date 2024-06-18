package com.teammetallurgy.aquaculture.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.entity.AquaFishEntity;
import com.teammetallurgy.aquaculture.entity.FishMountEntity;
import com.teammetallurgy.aquaculture.entity.FishType;
import com.teammetallurgy.aquaculture.init.AquaDataComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Pufferfish;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;

public class FishMountRenderer extends EntityRenderer<FishMountEntity> {
    private final Minecraft mc = Minecraft.getInstance();

    public FishMountRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(@Nonnull FishMountEntity fishMount, float entityYaw, float partialTicks, @Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int i) {
        super.render(fishMount, entityYaw, partialTicks, matrixStack, buffer, i);
        matrixStack.pushPose();
        Direction direction = fishMount.getDirection();
        Vec3 pos = this.getRenderOffset(fishMount, partialTicks);
        matrixStack.translate(-pos.x(), -pos.y(), -pos.z());
        double multiplier = 0.46875D;
        matrixStack.translate((double) direction.getStepX() * multiplier, (double) direction.getStepY() * multiplier, (double) direction.getStepZ() * multiplier);
        matrixStack.mulPose(Axis.XP.rotationDegrees(fishMount.getXRot()));
        matrixStack.mulPose(Axis.YP.rotationDegrees(180.0F - fishMount.getYRot()));
        BlockRenderDispatcher rendererDispatcher = this.mc.getBlockRenderer();
        ModelManager manager = rendererDispatcher.getBlockModelShaper().getModelManager();

        matrixStack.pushPose();
        matrixStack.translate(-0.5D, -0.5D, -0.5D);
        ResourceLocation entityTypeID = BuiltInRegistries.ENTITY_TYPE.getKey(fishMount.getType());
        if (entityTypeID != null) {
            ModelResourceLocation location = new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(Aquaculture.MOD_ID, "block/" + entityTypeID.getPath()), "standalone"); //Calling this instead of the fields for mod support'
            rendererDispatcher.getModelRenderer().renderModel(matrixStack.last(), buffer.getBuffer(Sheets.solidBlockSheet()), null, manager.getModel(location), 1.0F, 1.0F, 1.0F, i, OverlayTexture.NO_OVERLAY);
        }
        matrixStack.popPose();
        this.renderFish(fishMount, matrixStack, buffer, i);
        matrixStack.popPose();
    }

    private void renderFish(FishMountEntity fishMount, PoseStack matrixStack, MultiBufferSource buffer, int i) {
        Entity entity = fishMount.entity;
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
            matrixStack.translate(x, y, depth);
            matrixStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
            matrixStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
            this.mc.getEntityRenderDispatcher().render(fish, 0.0F, 0.0F, 0.0F, 0.0F, 0, matrixStack, buffer, i);
        }
    }

    @Override
    @Nonnull
    public ResourceLocation getTextureLocation(@Nonnull FishMountEntity fishMount) {
        return InventoryMenu.BLOCK_ATLAS;
    }

    @Override
    @Nonnull
    public Vec3 getRenderOffset(FishMountEntity fishMount, float partialTicks) {
        return new Vec3((float) fishMount.getDirection().getStepX() * 0.3F, -0.25D, (float) fishMount.getDirection().getStepZ() * 0.3F);
    }

    @Override
    protected boolean shouldShowName(@Nonnull FishMountEntity fishMount) {
        if (Minecraft.renderNames() && fishMount.entity != null && (this.mc.hitResult != null && fishMount.distanceToSqr(this.mc.hitResult.getLocation()) < 0.24D)) {
            double d0 = this.entityRenderDispatcher.distanceToSqr(fishMount);
            float sneaking = fishMount.isDiscrete() ? 32.0F : 64.0F;
            return d0 < (double) (sneaking * sneaking);
        } else {
            return false;
        }
    }

    @Override
    protected void renderNameTag(@Nonnull FishMountEntity fishMount, @Nonnull Component name, @Nonnull PoseStack matrixStack, @Nonnull MultiBufferSource buffer, int i, float partialTick) {
        super.renderNameTag(fishMount, fishMount.entity.getDisplayName(), matrixStack, buffer, i, partialTick);

        ItemStack stack = fishMount.getDisplayedItem();
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
                super.renderNameTag(fishMount, Component.translatable("aquaculture.fishWeight.weight", df.format((int) bd.doubleValue()) + lb), matrixStack, buffer, i - 100, partialTick);
            } else {
                super.renderNameTag(fishMount, Component.translatable("aquaculture.fishWeight.weight", bd + lb), matrixStack, buffer, i, partialTick);
            }
            matrixStack.popPose();
        }
    }
}