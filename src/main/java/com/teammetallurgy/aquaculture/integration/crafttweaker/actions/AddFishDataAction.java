package com.teammetallurgy.aquaculture.integration.crafttweaker.actions;

import com.blamejared.crafttweaker.api.action.base.IUndoableAction;
import com.teammetallurgy.aquaculture.api.AquacultureAPI;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

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
    public String systemName() {
        return "Aquaculture Add Fish Data";
    }

    @Override
    public void apply() {
        if (AquacultureAPI.FISH_DATA.hasWeight(this.fish)) {
            this.oldMin = AquacultureAPI.FISH_DATA.getMinWeight(this.fish, 0);
            this.oldMax = AquacultureAPI.FISH_DATA.getMaxWeight(this.fish, 0);
            this.hasOldData = true;
        }
        if (AquacultureAPI.FISH_DATA.hasFilletAmount(this.fish)) {
            this.oldFilletAmount = AquacultureAPI.FISH_DATA.getFilletAmount(this.fish, 0);
            this.hasOldData = true;
        }
        AquacultureAPI.FISH_DATA.add(this.fish, this.min, this.max, this.filletAmount);
    }

    @Override
    public String describe() {
        return "Adding FishData for: " + BuiltInRegistries.ITEM.getKey(this.fish).toString() + " with min: " + this.min + ", max: " + this.max + " and fillet amount of: " + this.filletAmount;
    }

    @Override
    public void undo() {
        if (this.hasOldData) {
            AquacultureAPI.FISH_DATA.add(this.fish, this.oldMin, this.oldMax, this.oldFilletAmount);
        } else {
            AquacultureAPI.FISH_DATA.remove(this.fish);
        }
    }

    @Override
    public String describeUndo() {
        return "Undoing removal of FishData for: " + BuiltInRegistries.ITEM.getKey(this.fish).toString();
    }
}