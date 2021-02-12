package com.teammetallurgy.aquaculture.integration.crafttweaker.actions;

import com.blamejared.crafttweaker.api.actions.IUndoableAction;
import com.teammetallurgy.aquaculture.api.AquacultureAPI;
import net.minecraft.item.Item;

public class AddFishDataAction implements IUndoableAction {

    private final Item fish;

    private final double min;
    private final double max;
    private final int filletAmount;

    private boolean hasOldData;
    private double oldMin;
    private double oldMax;
    private int oldFilletAmount;

    public AddFishDataAction(Item fish, double min, double max, int filletAmount) {
        this.fish = fish;
        this.min = min;
        this.max = max;
        this.filletAmount = filletAmount;
    }

    @Override
    public void apply() {

        if (AquacultureAPI.FISH_DATA.hasWeight(fish)) {
            this.oldMin = AquacultureAPI.FISH_DATA.getMinWeight(fish, 0);
            this.oldMax = AquacultureAPI.FISH_DATA.getMaxWeight(fish, 0);
            hasOldData = true;
        }
        if (AquacultureAPI.FISH_DATA.hasFilletAmount(fish)) {
            this.oldFilletAmount = AquacultureAPI.FISH_DATA.getFilletAmount(fish, 0);
            hasOldData = true;
        }

        AquacultureAPI.FISH_DATA.add(fish, min, max, filletAmount);
    }

    @Override
    public String describe() {
        return "Adding FishData for: " + fish.getRegistryName() + " with min: " + min + ", max: " + max + " and fillet amount of: " + filletAmount;
    }

    @Override
    public void undo() {
        if (hasOldData) {
            AquacultureAPI.FISH_DATA.add(fish, oldMin, oldMax, oldFilletAmount);
        } else {
            AquacultureAPI.FISH_DATA.remove(fish, true);
        }
    }

    @Override
    public String describeUndo() {
        return "Undoing removal of FishData for: " + fish.getRegistryName();
    }
}
