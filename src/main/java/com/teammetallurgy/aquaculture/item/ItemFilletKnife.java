package com.teammetallurgy.aquaculture.item;

import com.teammetallurgy.aquaculture.api.AquacultureAPI;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.ToolAction;
import net.neoforged.neoforge.common.ToolActions;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemFilletKnife extends SwordItem {

    public ItemFilletKnife(Tier tier) {
        super(tier, new Item.Properties().durability(tier == AquacultureAPI.MATS.NEPTUNIUM ? -1 : (int) (tier.getUses() * 0.75F)).attributes(SwordItem.createAttributes(tier, tier.getAttackDamageBonus() / 2, -2.2F)));
    }

    @Override
    public boolean isPrimaryItemFor(@Nonnull ItemStack stack, @Nonnull Holder<Enchantment> enchantment) {
        return super.isPrimaryItemFor(stack, enchantment) && canApplyEnchantment(enchantment);
    }

    private boolean canApplyEnchantment(Holder<Enchantment> enchantment) {
        return enchantment != Enchantments.LOOTING && enchantment != Enchantments.SWEEPING_EDGE;
    }

    @Override
    public boolean canPerformAction(@Nonnull ItemStack stack, @Nonnull ToolAction toolAction) {
        return toolAction == ToolActions.SWORD_DIG;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@Nonnull ItemStack stack, @Nonnull Item.TooltipContext tooltipContext, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag tooltipFlag) {
        if (this.getTier() == AquacultureAPI.MATS.NEPTUNIUM) {
            MutableComponent unbreakable = Component.translatable("aquaculture.unbreakable");
            tooltip.add(unbreakable.withStyle(unbreakable.getStyle().withColor(ChatFormatting.DARK_GRAY).withBold(true)));
        }
        super.appendHoverText(stack, tooltipContext, tooltip, tooltipFlag);
    }
}