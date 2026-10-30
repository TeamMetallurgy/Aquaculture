package com.teammetallurgy.aquaculture.item;

import com.teammetallurgy.aquaculture.api.bait.IBaitItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class BaitItem extends Item implements IBaitItem {
    private final int durability;
    private final int lureSpeedModifier;

    public BaitItem(int durability, int lureSpeedModifier, Properties properties) {
        super(properties.setNoCombineRepair());
        this.durability = durability;
        this.lureSpeedModifier = lureSpeedModifier;
    }

    @Override
    public int getLureSpeedModifier() {
        return this.lureSpeedModifier;
    }

    public int getBaitDurability() {
        return this.durability;
    }

    public static int getBaitDamage(ItemStack stack) {
        return stack.getOrDefault(DataComponents.DAMAGE, 0);
    }

    public boolean damageBait(ItemStack stack) {
        int damage = getBaitDamage(stack) + 1;
        if (damage >= this.durability) {
            stack.shrink(1);
            stack.remove(DataComponents.DAMAGE);
            return true;
        }
        stack.set(DataComponents.DAMAGE, damage);
        return false;
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nonnull Item.TooltipContext context, @Nonnull TooltipDisplay display, @Nonnull Consumer<Component> tooltips, @Nonnull TooltipFlag tooltipFlag) {
        int damage = getBaitDamage(stack);
        if (tooltipFlag.isAdvanced() && damage > 0 && display.shows(DataComponents.DAMAGE)) {
            tooltips.accept(Component.translatable("item.durability", this.durability - damage, this.durability));
        }
    }

    @Override
    public boolean isBarVisible(@Nonnull ItemStack stack) {
        return getBaitDamage(stack) > 0;
    }

    @Override
    public int getBarWidth(@Nonnull ItemStack stack) {
        return Mth.clamp(Math.round(13.0F - getBaitDamage(stack) * 13.0F / this.durability), 0, 13);
    }

    @Override
    public int getBarColor(@Nonnull ItemStack stack) {
        return 16761035;
    }
}
