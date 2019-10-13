package com.teammetallurgy.aquaculture.item;

import com.teammetallurgy.aquaculture.api.AquacultureAPI;
import com.teammetallurgy.aquaculture.api.fishing.Hook;
import com.teammetallurgy.aquaculture.api.fishing.Hooks;
import com.teammetallurgy.aquaculture.entity.AquaFishingBobberEntity;
import com.teammetallurgy.aquaculture.misc.AquaConfig;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class AquaFishingRodItem extends FishingRodItem {
    private IItemTier material;
    private int enchantability;

    public AquaFishingRodItem(IItemTier material, Properties properties) {
        super(properties);
        this.enchantability = material == ItemTier.WOOD ? 10 : material.getEnchantability();
        this.material = material;
    }

    @Override
    public int getItemEnchantability() {
        return enchantability;
    }

    @Override
    public boolean showDurabilityBar(@Nonnull ItemStack stack) {
        return this.getDamage(stack) < this.getMaxDamage(stack) && super.showDurabilityBar(stack);
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
        ItemStack heldStack = player.getHeldItem(hand);
        boolean isAdminRod = AquaConfig.BASIC_OPTIONS.debugMode.get() && this.material == AquacultureAPI.MATS.NEPTUNIUM;
        int lureSpeed;
        int damage = this.getDamage(heldStack);
        if (damage >= this.getMaxDamage(heldStack)) return new ActionResult<>(ActionResultType.FAIL, heldStack);
        Hook hook = getHookType(heldStack);
        if (player.fishingBobber != null) {
            if (!world.isRemote) {
                lureSpeed = player.fishingBobber.handleHookRetraction(heldStack);
                int currentDamage = this.getMaxDamage(heldStack) - damage;
                if (lureSpeed >= currentDamage) {
                    lureSpeed = currentDamage;
                }
                if (!isAdminRod) {
                    if (hook != Hooks.EMPTY && hook.getDurabilityChance() > 0) {
                        if (random.nextDouble() >= hook.getDurabilityChance()) {
                            heldStack.attemptDamageItem(lureSpeed, world.rand, null);
                        }
                    } else {
                        heldStack.attemptDamageItem(lureSpeed, world.rand, null);
                    }
                }
            }
            player.swingArm(hand);
            world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_FISHING_BOBBER_RETRIEVE, SoundCategory.NEUTRAL, 1.0F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
        } else {
            world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_FISHING_BOBBER_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
            if (!world.isRemote) {
                //Lure Speed
                lureSpeed = EnchantmentHelper.getFishingSpeedBonus(heldStack);
                if (this.material == AquacultureAPI.MATS.NEPTUNIUM) lureSpeed += 1;
                ItemStack bait = getBait(heldStack);
                if (!isAdminRod && !bait.isEmpty()) {
                    lureSpeed += ((BaitItem) bait.getItem()).getLureSpeedModifier();
                }
                lureSpeed = Math.min(5, lureSpeed);
                //Luck
                int luck = EnchantmentHelper.getFishingLuckBonus(heldStack);
                if (hook != Hooks.EMPTY && hook.getLuckModifier() > 0) luck += hook.getLuckModifier();

                world.addEntity(new AquaFishingBobberEntity(player, world, luck, lureSpeed, hook, getFishingLine(heldStack), getBobber(heldStack), heldStack));
            }
            player.swingArm(hand);
            player.addStat(Stats.ITEM_USED.get(this));
        }
        return new ActionResult<>(ActionResultType.SUCCESS, heldStack);
    }

    @Nonnull
    public static Hook getHookType(@Nonnull ItemStack fishingRod) {
        Hook hook = Hooks.EMPTY;
        ItemStackHandler rodHandler = (ItemStackHandler) fishingRod.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(AquaFishingRodItem.FishingRodEquipmentHandler.EMPTY.getItems());
        if (!fishingRod.isEmpty() && fishingRod.hasTag() && fishingRod.getTag() != null && fishingRod.getTag().contains("Inventory")) {
            rodHandler.deserializeNBT(fishingRod.getTag().getCompound("Inventory")); //Reload
        }

        ItemStack hookStack = rodHandler.getStackInSlot(0);
        if (hookStack.getItem() instanceof HookItem) {
            hook = ((HookItem) hookStack.getItem()).getHookType();
        }
        return hook;
    }

    @Nonnull
    public static ItemStack getBait(@Nonnull ItemStack fishingRod) {
        return getHandler(fishingRod).getStackInSlot(1);
    }

    @Nonnull
    public static ItemStack getFishingLine(@Nonnull ItemStack fishingRod) {
        return getHandler(fishingRod).getStackInSlot(2);
    }

    @Nonnull
    public static ItemStack getBobber(@Nonnull ItemStack fishingRod) {
        return getHandler(fishingRod).getStackInSlot(3);
    }

    public static ItemStackHandler getHandler(@Nonnull ItemStack fishingRod) {
        ItemStackHandler rodHandler = (ItemStackHandler) fishingRod.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(AquaFishingRodItem.FishingRodEquipmentHandler.EMPTY.getItems());
        if (!fishingRod.isEmpty() && fishingRod.hasTag() && fishingRod.getTag() != null && fishingRod.getTag().contains("Inventory")) {
            rodHandler.deserializeNBT(fishingRod.getTag().getCompound("Inventory")); //Reload
        }
        return rodHandler;
    }

    @Override
    @Nullable
    public ICapabilityProvider initCapabilities(@Nonnull ItemStack stack, @Nullable CompoundNBT nbt) {
        return new FishingRodEquipmentHandler(stack);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, @Nullable World world, List<ITextComponent> tooltips, ITooltipFlag tooltipFlag) {
        if (this.getDamage(stack) >= this.getMaxDamage(stack)) {
            tooltips.add(new TranslationTextComponent("aquaculture.fishing_rod.broken").setStyle(new Style().setItalic(true).setColor(TextFormatting.GRAY)));
        }

        Hook hook = getHookType(stack);
        if (hook != Hooks.EMPTY) {
            tooltips.add(new TranslationTextComponent(hook.getItem().getTranslationKey()).setStyle(new Style().setColor(hook.getColor())));
        }
        super.addInformation(stack, world, tooltips, tooltipFlag);
    }

    public static class FishingRodEquipmentHandler implements ICapabilityProvider {
        public static final FishingRodEquipmentHandler EMPTY = new FishingRodEquipmentHandler(ItemStack.EMPTY);
        private final LazyOptional<IItemHandler> holder = LazyOptional.of(this::getItems);
        private final ItemStack stack;
        private final ItemStackHandler items = new ItemStackHandler(4) {
            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                switch (slot) {
                    case 0:
                        return stack.getItem() instanceof HookItem;
                    case 1:
                        return stack.getItem() instanceof BaitItem;
                    case 2:
                        return stack.getItem().isIn(AquacultureAPI.Tags.FISHING_LINE) && stack.getItem() instanceof IDyeableArmorItem;
                    case 3:
                        return stack.getItem().isIn(AquacultureAPI.Tags.BOBBER) && stack.getItem() instanceof IDyeableArmorItem;
                    default:
                        return false;
                }
            }

            @Override
            protected void onContentsChanged(int slot) {
                CompoundNBT tag = FishingRodEquipmentHandler.this.stack.getOrCreateTag();
                tag.put("Inventory", this.serializeNBT());
                FishingRodEquipmentHandler.this.stack.setTag(tag);
            }
        };

        FishingRodEquipmentHandler(@Nonnull ItemStack stack) {
            this.stack = stack;
        }

        @Override
        @Nonnull
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction direction) {
            return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? holder.cast() : LazyOptional.empty();
        }

        @Nonnull
        public ItemStackHandler getItems() {
            return items;
        }
    }
}
