package com.teammetallurgy.aquaculture.integration.crafttweaker.actions;

import com.blamejared.crafttweaker.api.actions.IUndoableAction;
import com.teammetallurgy.aquaculture.api.AquacultureAPI;
import net.minecraft.item.Item;

public class RemoveFishDataAction implements IUndoableAction {

    private final Item fish;
    private final boolean fillet;

    private double min;
    private double max;
    private int filletAmount;

    public RemoveFishDataAction(Item fish, boolean fillet) {
        this.fish = fish;
        this.fillet = fillet;
    }

    @Override
    public void apply() {
        this.min = AquacultureAPI.FISH_DATA.getMinWeight(fish, 0);
        this.max = AquacultureAPI.FISH_DATA.getMaxWeight(fish, 0);
        this.filletAmount = AquacultureAPI.FISH_DATA.getFilletAmount(fish, 1);
        AquacultureAPI.FISH_DATA.remove(fish, fillet);
    }

    @Override
    public String describe() {
        return "Removing FishData for: " + fish.getRegistryName();
    }

    @Override
    public void undo() {
        if (fillet) {
            AquacultureAPI.FISH_DATA.add(fish, min, max, filletAmount);
        } else {
            AquacultureAPI.FISH_DATA.add(fish, min, max);
        }
    }

    @Override
    public String describeUndo() {
        return "Undoing removal of FishData for: " + fish.getRegistryName();
    }
}
