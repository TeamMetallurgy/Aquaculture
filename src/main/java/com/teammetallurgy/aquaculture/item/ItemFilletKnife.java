package com.teammetallurgy.aquaculture.item;

import com.teammetallurgy.aquaculture.api.AquacultureAPI;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemFilletKnife extends SwordItem {

    public ItemFilletKnife(Tier tier) {
        super(tier, new Item.Properties().durability(tier == AquacultureAPI.MATS.NEPTUNIUM ? -1 : (int) (tier.getUses() * 0.75F)).attributes(SwordItem.createAttributes(tier, tier.getAttackDamageBonus() / 2, -2.2F)));
    }

    @Override
    public boolean isDamageable(@Nonnull ItemStack stack) {
        return this.getTier() != AquacultureAPI.MATS.NEPTUNIUM;
    }

    @Override
    public boolean canPerformAction(@Nonnull ItemStack stack, @Nonnull ItemAbility toolAction) {
        return toolAction == ItemAbilities.SWORD_DIG;
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