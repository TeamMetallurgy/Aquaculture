package com.teammetallurgy.aquaculture.misc;

import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.init.AquaItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.entity.living.LivingHurtEvent;

@EventBusSubscriber(modid = Aquaculture.MOD_ID)
public class NeptunesMight {

    @SubscribeEvent
    public static void onAttack(LivingHurtEvent event) {
        Entity source = event.getSource().getEntity();
        if (source instanceof LivingEntity living) {
            if (living.isEyeInFluidType(NeoForgeMod.WATER_TYPE.value())) {
                ItemStack heldStack = living.getMainHandItem();
                if (heldStack.getItem() == AquaItems.NEPTUNIUM_SWORD.get() || heldStack.getItem() == AquaItems.NEPTUNIUM_AXE.get()) {
                    event.setAmount(event.getAmount() * 1.5F);
                }
            }
        }
    }
}