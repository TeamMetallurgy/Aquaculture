package com.teammetallurgy.aquaculture.item;

import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.init.AquaItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;

@EventBusSubscriber(modid = Aquaculture.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ItemFilletKnife extends Item {

    public ItemFilletKnife(ToolMaterial toolMaterial, Item.Properties properties) {
        super(properties.sword(toolMaterial, toolMaterial.attackDamageBonus() / 2, -2.2F).durability((int) (toolMaterial.durability() * 0.75F)));
    }

    @SubscribeEvent
    public static void modifyDefaultComponents(ModifyDefaultComponentsEvent event) {
        event.modify(AquaItems.NEPTUNIUM_FILLET_KNIFE, b -> b.remove(DataComponents.DAMAGE).build());
        event.modify(AquaItems.NEPTUNIUM_FILLET_KNIFE, b -> b.remove(DataComponents.MAX_DAMAGE).build());
    }
}