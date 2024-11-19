package com.teammetallurgy.aquaculture.item;

import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.api.AquacultureAPI;
import com.teammetallurgy.aquaculture.init.AquaItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Unbreakable;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;

import javax.annotation.Nonnull;
import java.util.List;

@EventBusSubscriber(modid = Aquaculture.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ItemFilletKnife extends SwordItem {

    public ItemFilletKnife(Tier tier, Item.Properties properties) {
        super(tier, properties);
    }

    public ItemFilletKnife(Tier tier) {
        super(tier, new Item.Properties().durability((int) (tier.getUses() * 0.75F)).attributes(SwordItem.createAttributes(tier, tier.getAttackDamageBonus() / 2, -2.2F)));
    }

    @SubscribeEvent
    public static void modifyDefaultComponents(ModifyDefaultComponentsEvent event) {
        event.modify(AquaItems.NEPTUNIUM_FILLET_KNIFE, b -> b.remove(DataComponents.DAMAGE).build());
        event.modify(AquaItems.NEPTUNIUM_FILLET_KNIFE, b -> b.remove(DataComponents.MAX_DAMAGE).build());
    }

    @Override
    public boolean canPerformAction(@Nonnull ItemStack stack, @Nonnull ItemAbility toolAction) {
        return toolAction == ItemAbilities.SWORD_DIG;
    }
}