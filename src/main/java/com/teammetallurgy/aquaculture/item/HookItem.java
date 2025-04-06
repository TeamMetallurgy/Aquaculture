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
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

public class HookItem extends Item {
    private final Hook hook;

    public HookItem(Hook hook, Item.Properties properties) {
        super(properties.stacksTo(16));
        this.hook = hook;
    }

    public Hook getHookType() {
        return hook;
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable Item.TooltipContext context, @Nonnull TooltipDisplay display, @Nonnull Consumer<Component> tooltips, @Nonnull TooltipFlag tooltipFlag) {
        Hook hook = getHookType();
        if (hook != Hooks.EMPTY && hook.getFluids().contains(FluidTags.LAVA)) {
            if (hook.getFluids().contains(FluidTags.WATER)) {
                MutableComponent universal = Component.translatable("aquaculture.universal");
                tooltips.accept(universal.withStyle(universal.getStyle().withColor(ChatFormatting.BOLD)));
            } else {
                MutableComponent lava = Component.translatable(Blocks.LAVA.getDescriptionId());
                tooltips.accept(lava.withStyle(lava.getStyle().withColor(ChatFormatting.RED)));
            }
        }
    }
}