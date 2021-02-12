package com.teammetallurgy.aquaculture.integration.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.actions.IUndoableAction;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.api.AquacultureAPI;
import com.teammetallurgy.aquaculture.integration.crafttweaker.actions.AddFishDataAction;
import com.teammetallurgy.aquaculture.integration.crafttweaker.actions.RemoveFishDataAction;
import net.minecraft.item.Item;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.aquaculture.FishData")
public class CTFishData {

    @ZenCodeType.Method
    public static void add(Item fish, double min, double max, int filletAmount) {
        CraftTweakerAPI.apply(new AddFishDataAction(fish, min, max, filletAmount));
    }

    @ZenCodeType.Method
    public static void remove(Item fish, boolean fillet) {
        CraftTweakerAPI.apply(new RemoveFishDataAction(fish, fillet));
    }

}