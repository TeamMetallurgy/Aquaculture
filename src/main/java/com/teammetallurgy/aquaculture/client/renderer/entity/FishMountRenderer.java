package com.teammetallurgy.aquaculture.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.entity.AquaFishEntity;
import com.teammetallurgy.aquaculture.entity.FishMountEntity;
import com.teammetallurgy.aquaculture.entity.FishType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ModelManager;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.passive.fish.PufferfishEntity;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;

public class FishMountRenderer extends EntityRenderer<FishMountEntity> {
    public static final ModelResourceLocation OAK = new ModelResourceLocation(new ResourceLocation(Aquaculture.MOD_ID, "oak_fish_mount"), "");
    public static final ModelResourceLocation SPRUCE = new ModelResourceLocation(new ResourceLocation(Aquaculture.MOD_ID, "spruce_fish_mount"), "");
    public static final ModelResourceLocation BIRCH = new ModelResourceLocation(new ResourceLocation(Aquaculture.MOD_ID, "birch_fish_mount"), "");
    public static final ModelResourceLocation JUNGLE = new ModelResourceLocation(new ResourceLocation(Aquaculture.MOD_ID, "jungle_fish_mount"), "");
    public static final ModelResourceLocation ACACIA = new ModelResourceLocation(new ResourceLocation(Aquaculture.MOD_ID, "acacia_fish_mount"), "");
    public static final ModelResourceLocation DARK_OAK = new ModelResourceLocation(new ResourceLocation(Aquaculture.MOD_ID, "dark_oak_fish_mount"), "");
    private final Minecraft mc = Minecraft.getInstance();

    public FishMountRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public void render(@Nonnull FishMountEntity fishMount, float entityYaw, float partialTicks, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer buffer, int i) {
        super.render(fishMount, entityYaw, partialTicks, matrixStack, buffer, i);
        matrixStack.push();
        Direction direction = fishMount.getHorizontalFacing();
        Vec3d pos = this.getRenderOffset(fishMount, partialTicks);
        matrixStack.translate(-pos.getX(), -pos.getY(), -pos.getZ());
        double multiplier = 0.46875D;
        matrixStack.translate((double) direction.getXOffset() * multiplier, (double) direction.getYOffset() * multiplier, (double) direction.getZOffset() * multiplier);
        matrixStack.rotate(Vector3f.XP.rotationDegrees(fishMount.rotationPitch));
        matrixStack.rotate(Vector3f.YP.rotationDegrees(180.0F - fishMount.rotationYaw));
        BlockRendererDispatcher rendererDispatcher = this.mc.getBlockRendererDispatcher();
        ModelManager manager = rendererDispatcher.getBlockModelShapes().getModelManager();

        matrixStack.push();
        matrixStack.translate(-0.5D, -0.5D, -0.5D);
        if (fishMount.getType().getRegistryName() != null) {
            ModelResourceLocation location = new ModelResourceLocation(fishMount.getType().getRegistryName(), ""); //Calling this instead of the fields for mod support'
            rendererDispatcher.getBlockModelRenderer().renderModelBrightnessColor(matrixStack.getLast(), buffer.getBuffer(Atlases.getSolidBlockType()), null, manager.getModel(location), 1.0F, 1.0F, 1.0F, i, OverlayTexture.NO_OVERLAY);
        }
        matrixStack.pop();
        this.renderFish(fishMount, matrixStack, buffer, i);
        matrixStack.pop();
    }

    private void renderFish(FishMountEntity fishMount, MatrixStack matrixStack, IRenderTypeBuffer buffer, int i) {
        Entity entity = fishMount.entity;
        if (entity instanceof MobEntity) {
            MobEntity fish = (MobEntity) entity;
            double x = 0.0D;
            double y = 0.0D;
            double depth = 0.42D;
            if (fish instanceof PufferfishEntity) {
                depth += 0.09D;
            } else if (fish instanceof AquaFishEntity && AquaFishEntity.TYPES.get(fish.getType()).equals(FishType.LONGNOSE)) {
                x = -0.1F;
                y = -0.18D;
            }
            fish.setNoAI(true);
            matrixStack.translate(x, y, depth);
            matrixStack.rotate(Vector3f.XP.rotationDegrees(-90.0F));
            matrixStack.rotate(Vector3f.YP.rotationDegrees(-90.0F));
            this.mc.getRenderManager().renderEntityStatic(fish, 0.0F, 0.0F, 0.0F, 0.0F, 0, matrixStack, buffer, i);
        }
    }

    @Override
    @Nonnull
    public ResourceLocation getEntityTexture(@Nonnull FishMountEntity fishMount) {
        return PlayerContainer.LOCATION_BLOCKS_TEXTURE;
    }

    @Override
    @Nonnull
    public Vec3d getRenderOffset(FishMountEntity fishMount, float partialTicks) {
        return new Vec3d((float) fishMount.getHorizontalFacing().getXOffset() * 0.3F, -0.25D, (float) fishMount.getHorizontalFacing().getZOffset() * 0.3F);
    }

    @Override
    protected boolean canRenderName(@Nonnull FishMountEntity fishMount) {
        if (Minecraft.isGuiEnabled() && fishMount.entity != null && (this.mc.objectMouseOver != null && fishMount.getDistanceSq(this.mc.objectMouseOver.getHitVec()) < 0.24D)) {
            double d0 = this.renderManager.squareDistanceTo(fishMount);
            float sneaking = fishMount.isDiscrete() ? 32.0F : 64.0F;
            return d0 < (double) (sneaking * sneaking);
        } else {
            return false;
        }
    }

    @Override
    protected void renderName(@Nonnull FishMountEntity fishMount, @Nonnull String name, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer buffer, int i) {
        super.renderName(fishMount, fishMount.entity.getDisplayName().getFormattedText(), matrixStack, buffer, i);

        ItemStack stack = fishMount.getDisplayedItem();
        if (stack.hasTag() && stack.getTag() != null && stack.getTag().contains("fishWeight")) {
            double weight = stack.getTag().getDouble("fishWeight");
            String lb = weight == 1.0D ? " lb" : " lbs";

            DecimalFormat df = new DecimalFormat("#,###.##");
            BigDecimal bd = new BigDecimal(weight);
            bd = bd.round(new MathContext(3));

            matrixStack.push();
            matrixStack.translate(0.0D, -0.25D, 0.0D); //Adjust weight label height
            if (bd.doubleValue() > 999) {
                super.renderName(fishMount, I18n.format("aquaculture.fishWeight.weight", df.format((int) bd.doubleValue()) + lb), matrixStack, buffer, i - 100);
            } else {
                super.renderName(fishMount, I18n.format("aquaculture.fishWeight.weight", bd + lb), matrixStack, buffer, i);
            }
            matrixStack.pop();
        }
    }
}