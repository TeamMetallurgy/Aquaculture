package com.teammetallurgy.aquaculture.item;

import com.teammetallurgy.aquaculture.api.AquacultureAPI;
import com.teammetallurgy.aquaculture.api.bait.IBaitItem;
import com.teammetallurgy.aquaculture.api.fishing.Hook;
import com.teammetallurgy.aquaculture.api.fishing.Hooks;
import com.teammetallurgy.aquaculture.entity.AquaFishingBobberEntity;
import com.teammetallurgy.aquaculture.misc.AquaConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class AquaFishingRodItem extends FishingRodItem {
    private final Tier tier;
    private final int enchantability;

    public AquaFishingRodItem(Tier tier, Properties properties) {
        super(properties);
        this.enchantability = tier == Tiers.WOOD ? 10 : tier.getEnchantmentValue();
        this.tier = tier;
    }

    public Tier getTier() { //Added getter, so other mods can access it
        return tier;
    }

    @Override
    public int getEnchantmentValue() {
        return enchantability;
    }

    @Override
    public boolean isBarVisible(@Nonnull ItemStack stack) {
        return this.getDamage(stack) < this.getMaxDamage(stack) && super.isBarVisible(stack);
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, Player player, @Nonnull InteractionHand hand) {
        ItemStack heldStack = player.getItemInHand(hand);

        if (player instanceof FakePlayer) return InteractionResultHolder.fail(heldStack);

        boolean isAdminRod = AquaConfig.BASIC_OPTIONS.debugMode.get() && this.tier == AquacultureAPI.MATS.NEPTUNIUM;
        int lureSpeed;
        int damage = this.getDamage(heldStack);
        if (damage >= this.getMaxDamage(heldStack)) return new InteractionResultHolder<>(InteractionResult.FAIL, heldStack);
        Hook hook = getHookType(heldStack);
        if (player.fishing != null) {
            if (!level.isClientSide) {
                lureSpeed = player.fishing.retrieve(heldStack);
                int currentDamage = this.getMaxDamage(heldStack) - damage;
                if (lureSpeed >= currentDamage) {
                    lureSpeed = currentDamage;
                }
                if (!isAdminRod) {
                    if (hook != Hooks.EMPTY && hook.getDurabilityChance() > 0) {
                        if (level.random.nextDouble() >= hook.getDurabilityChance()) {
                            heldStack.hurt(lureSpeed, level.random, null);
                        }
                    } else {
                        heldStack.hurt(lureSpeed, level.random, null);
                    }
                }
            }
            player.swing(hand);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.NEUTRAL, 1.0F, 0.4F / (level.random.nextFloat() * 0.4F + 0.8F));
            player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
        } else {
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FISHING_BOBBER_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.random.nextFloat() * 0.4F + 0.8F));
            if (!level.isClientSide) {
                //Lure Speed
                lureSpeed = EnchantmentHelper.getFishingSpeedBonus(heldStack);
                if (this.tier == AquacultureAPI.MATS.NEPTUNIUM) lureSpeed += 1;
                ItemStack bait = getBait(heldStack);
                if (!isAdminRod && !bait.isEmpty()) {
                    if (bait.getItem() instanceof IBaitItem baitItem) {
                        lureSpeed += baitItem.getLureSpeedModifier();
                    }
                }
                lureSpeed = Math.min(5, lureSpeed);
                //Luck
                int luck = EnchantmentHelper.getFishingLuckBonus(heldStack);
                if (hook != Hooks.EMPTY && hook.getLuckModifier() > 0) luck += hook.getLuckModifier();

                level.addFreshEntity(new AquaFishingBobberEntity(player, level, luck, lureSpeed, hook, getFishingLine(heldStack), getBobber(heldStack), heldStack));
            }
            player.awardStat(Stats.ITEM_USED.get(this));
            player.gameEvent(GameEvent.ITEM_INTERACT_START);
        }
        return InteractionResultHolder.sidedSuccess(heldStack, level.isClientSide());
    }

    @Override
    public boolean isValidRepairItem(@Nonnull ItemStack toRepair, @Nonnull ItemStack repair) {
        return this.tier.getRepairIngredient().test(repair) || super.isValidRepairItem(toRepair, repair);
    }

    @Nonnull
    public static Hook getHookType(@Nonnull ItemStack fishingRod) {
        Hook hook = Hooks.EMPTY;
        ItemStackHandler rodHandler = (ItemStackHandler) fishingRod.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(AquaFishingRodItem.FishingRodEquipmentHandler.EMPTY.getItems());
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
        ItemStackHandler rodHandler = (ItemStackHandler) fishingRod.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(AquaFishingRodItem.FishingRodEquipmentHandler.EMPTY.getItems());
        if (!fishingRod.isEmpty() && fishingRod.hasTag() && fishingRod.getTag() != null && fishingRod.getTag().contains("Inventory")) {
            rodHandler.deserializeNBT(fishingRod.getTag().getCompound("Inventory")); //Reload
        }
        return rodHandler;
    }

    @Override
    @Nullable
    public ICapabilityProvider initCapabilities(@Nonnull ItemStack stack, @Nullable CompoundTag nbt) {
        return new FishingRodEquipmentHandler(stack);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level level, @Nonnull List<Component> tooltips, @Nonnull TooltipFlag tooltipFlag) {
        if (this.getDamage(stack) >= this.getMaxDamage(stack)) {
            MutableComponent broken = Component.translatable("aquaculture.fishing_rod.broken");
            tooltips.add(broken.withStyle(broken.getStyle().withItalic(true).withColor(ChatFormatting.GRAY)));
        }

        Hook hook = getHookType(stack);
        if (hook != Hooks.EMPTY) {
            MutableComponent hookColor = Component.translatable(hook.getItem().getDescriptionId());
            tooltips.add(hookColor.withStyle(hookColor.getStyle().withColor(hook.getColor())));
        }
        super.appendHoverText(stack, level, tooltips, tooltipFlag);
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
                return switch (slot) {
                    case 0 -> stack.getItem() instanceof HookItem;
                    case 1 -> stack.getItem() instanceof IBaitItem;
                    case 2 ->
                            stack.is(AquacultureAPI.Tags.FISHING_LINE) && stack.getItem() instanceof DyeableLeatherItem;
                    case 3 -> stack.is(AquacultureAPI.Tags.BOBBER) && stack.getItem() instanceof DyeableLeatherItem;
                    default -> false;
                };
            }

            @Override
            protected void onContentsChanged(int slot) {
                CompoundTag tag = FishingRodEquipmentHandler.this.stack.getOrCreateTag();
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
            return capability == ForgeCapabilities.ITEM_HANDLER ? holder.cast() : LazyOptional.empty();
        }

        @Nonnull
        public ItemStackHandler getItems() {
            return items;
        }
    }
}