package com.teammetallurgy.aquaculture.item;

import com.teammetallurgy.aquaculture.api.AquacultureAPI;
import com.teammetallurgy.aquaculture.api.fishing.Hook;
import com.teammetallurgy.aquaculture.api.fishing.Hooks;
import com.teammetallurgy.aquaculture.entity.AquaFishingBobberEntity;
import com.teammetallurgy.aquaculture.init.AquaDataComponents;
import com.teammetallurgy.aquaculture.misc.AquaConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class AquaFishingRodItem extends FishingRodItem {
    private final ToolMaterial toolMaterial;

    public AquaFishingRodItem(ToolMaterial toolMaterial, Properties properties) {
        super(properties.enchantable(toolMaterial == ToolMaterial.WOOD ? 10 : toolMaterial.enchantmentValue()).repairable(toolMaterial.repairItems()));
        this.toolMaterial = toolMaterial;
    }

    public ToolMaterial getTier() { //Added getter, so other mods can access it
        return toolMaterial;
    }

    @Override
    public boolean isBarVisible(@Nonnull ItemStack stack) {
        return this.getDamage(stack) < this.getMaxDamage(stack) && super.isBarVisible(stack);
    }

    @Override
    @Nonnull
    public InteractionResult use(@Nonnull Level level, Player player, @Nonnull InteractionHand hand) {
        ItemStack heldStack = player.getItemInHand(hand);

        if (player instanceof FakePlayer) return InteractionResult.FAIL;

        boolean isAdminRod = AquaConfig.BASIC_OPTIONS.debugMode.get() && this.toolMaterial == AquacultureAPI.MATS.NEPTUNIUM;
        int damage = this.getDamage(heldStack);
        if (damage >= this.getMaxDamage(heldStack))
            return InteractionResult.FAIL;
        Hook hook = getHookType(heldStack);
        if (player.fishing != null) {
            if (!level.isClientSide) {
                int retrieve = player.fishing.retrieve(heldStack);
                int currentDamage = this.getMaxDamage(heldStack) - damage;
                if (retrieve >= currentDamage) {
                    retrieve = currentDamage;
                }
                if (!isAdminRod) {
                    if (hook != Hooks.EMPTY && hook.getDurabilityChance() > 0) {
                        if (level.random.nextDouble() >= hook.getDurabilityChance()) {
                            heldStack.hurtAndBreak(retrieve, player, LivingEntity.getSlotForHand(hand));
                        }
                    } else {
                        heldStack.hurtAndBreak(retrieve, player, LivingEntity.getSlotForHand(hand));
                    }
                }
            }
            player.swing(hand);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.NEUTRAL, 1.0F, 0.4F / (level.random.nextFloat() * 0.4F + 0.8F));
            player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
        } else {
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FISHING_BOBBER_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.random.nextFloat() * 0.4F + 0.8F));
            if (level instanceof ServerLevel serverLevel) {
                //Lure Speed
                int lureSpeed = (int) (EnchantmentHelper.getFishingTimeReduction(serverLevel, heldStack, player) * 20.0F);
                if (this.toolMaterial == AquacultureAPI.MATS.NEPTUNIUM) lureSpeed += 100;
                ItemStack bait = getBait(heldStack);
                if (!isAdminRod && !bait.isEmpty()) {
                    lureSpeed += (((BaitItem) bait.getItem()).getLureSpeedModifier() * 100);
                }
                lureSpeed = Math.min(500, lureSpeed);

                //Luck
                int luck = EnchantmentHelper.getFishingLuckBonus(serverLevel, heldStack, player);
                if (hook != Hooks.EMPTY && hook.getLuckModifier() > 0) luck += hook.getLuckModifier();

                Projectile.spawnProjectile(new AquaFishingBobberEntity(player, level, luck, lureSpeed, hook, getFishingLine(heldStack), getBobber(heldStack), heldStack), serverLevel, heldStack);
            }
            player.awardStat(Stats.ITEM_USED.get(this));
            player.gameEvent(GameEvent.ITEM_INTERACT_START);
        }
        return InteractionResult.SUCCESS.heldItemTransformedTo(heldStack);
    }

    @Override
    public boolean canPerformAction(@Nonnull ItemStack stack, @Nonnull ItemAbility toolAction) {
        return ItemAbilities.DEFAULT_FISHING_ROD_ACTIONS.contains(toolAction);
    }

    @Nonnull
    public static Hook getHookType(@Nonnull ItemStack fishingRod) {
        Hook hook = Hooks.EMPTY;
        ItemStack hookStack = getHandler(fishingRod).getStackInSlot(0);
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
        ItemStackHandler rodHandler = (ItemStackHandler) fishingRod.getCapability(Capabilities.ItemHandler.ITEM);
        if (rodHandler == null) {
            rodHandler = FishingRodEquipmentHandler.EMPTY;
        } else {
            ItemContainerContents rodInventory = fishingRod.get(AquaDataComponents.ROD_INVENTORY);
            if (!fishingRod.isEmpty() && rodInventory != null && fishingRod.has(AquaDataComponents.ROD_INVENTORY)) {
                for (int slot = 0; slot < rodInventory.getSlots(); slot++) {
                    rodHandler.setStackInSlot(slot, rodInventory.getStackInSlot(slot)); //Reload
                }
            }
        }
        return rodHandler;
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nonnull Item.TooltipContext context, TooltipDisplay display, @Nonnull Consumer<Component> tooltips, @Nonnull TooltipFlag tooltipFlag) {
        if (this.getDamage(stack) >= this.getMaxDamage(stack)) {
            MutableComponent broken = Component.translatable("aquaculture.fishing_rod.broken");
            tooltips.accept(broken.withStyle(broken.getStyle().withItalic(true).withColor(ChatFormatting.GRAY)));
        }

        Hook hook = getHookType(stack);
        if (hook != Hooks.EMPTY) {
            MutableComponent hookColor = Component.translatable(hook.getItem().getDescriptionId());
            tooltips.accept(hookColor.withStyle(hookColor.getStyle().withColor(hook.getColor())));
        }
    }

    public static class FishingRodEquipmentHandler extends ItemStackHandler {
        public static final FishingRodEquipmentHandler EMPTY = new FishingRodEquipmentHandler(ItemStack.EMPTY);
        private final ItemStack stack;

        public FishingRodEquipmentHandler(ItemStack stack) {
            super(4);
            this.stack = stack;
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return switch (slot) {
                case 0 -> stack.getItem() instanceof HookItem;
                case 1 -> stack.getItem() instanceof BaitItem;
                case 2 -> stack.is(AquacultureAPI.Tags.FISHING_LINE);
                case 3 -> stack.is(AquacultureAPI.Tags.BOBBER);
                default -> false;
            };
        }

        @Override
        protected void onContentsChanged(int slot) {
            ItemContainerContents rodInventory = stack.get(AquaDataComponents.ROD_INVENTORY);
            if (!stack.isEmpty() && rodInventory != null && stack.has(AquaDataComponents.ROD_INVENTORY)) {
                NonNullList<ItemStack> list = NonNullList.create();
                for (int i = 0; i < getSlots(); i++) {
                    list.add(getStackInSlot(i));
                }

                stack.set(AquaDataComponents.ROD_INVENTORY, ItemContainerContents.fromItems(list));
            }
        }
    }
}