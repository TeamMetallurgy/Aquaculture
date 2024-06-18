package com.teammetallurgy.aquaculture.item;

import com.teammetallurgy.aquaculture.api.fishing.Hook;
import com.teammetallurgy.aquaculture.api.fishing.Hooks;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class HookItem extends Item {
    private final Hook hook;

    public HookItem(Hook hook) {
        super(new Item.Properties().stacksTo(16));
        this.hook = hook;
    }

    public Hook getHookType() {
        return hook;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Item.TooltipContext tooltipContext, List<Component> tooltips, TooltipFlag tooltipFlag) {
        Hook hook = getHookType();
        if (hook != Hooks.EMPTY && hook.getFluids().contains(FluidTags.LAVA)) {
            if (hook.getFluids().contains(FluidTags.WATER)) {
                MutableComponent universal = Component.translatable("aquaculture.universal");
                tooltips.add(universal.withStyle(universal.getStyle().withColor(ChatFormatting.BOLD)));
            } else {
                MutableComponent lava = Component.translatable(Blocks.LAVA.getDescriptionId());
                tooltips.add(lava.withStyle(lava.getStyle().withColor(ChatFormatting.RED)));
            }
        }
        super.appendHoverText(stack, tooltipContext, tooltips, tooltipFlag);
    }
}