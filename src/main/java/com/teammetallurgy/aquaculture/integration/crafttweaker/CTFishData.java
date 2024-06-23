package com.teammetallurgy.aquaculture.integration.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.teammetallurgy.aquaculture.integration.crafttweaker.actions.AddFishDataAction;
import com.teammetallurgy.aquaculture.integration.crafttweaker.actions.RemoveFishDataAction;
import net.minecraft.world.item.Item;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.aquaculture.FishData")
public class CTFishData {

    @ZenCodeType.Method
    public static void add(Item fish, double min, double max, int filletAmount) {
        CraftTweakerAPI.apply(new AddFishDataAction(fish, min, max, filletAmount));
    }

    @ZenCodeType.Method
    public static void remove(Item fish) {
        CraftTweakerAPI.apply(new RemoveFishDataAction(fish));
    }
}