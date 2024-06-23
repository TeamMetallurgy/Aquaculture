package com.teammetallurgy.aquaculture.integration.crafttweaker.actions;

import com.blamejared.crafttweaker.api.action.base.IUndoableAction;
import com.teammetallurgy.aquaculture.api.AquacultureAPI;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

public class RemoveFishDataAction implements IUndoableAction {
    private final Item fish;
    private double min;
    private double max;
    private int filletAmount;

    public RemoveFishDataAction(Item fish) {
        this.fish = fish;
    }

    @Override
    public String systemName() {
        return "Aquaculture Remove Fish Data";
    }

    @Override
    public void apply() {
        this.min = AquacultureAPI.FISH_DATA.getMinWeight(this.fish, 0);
        this.max = AquacultureAPI.FISH_DATA.getMaxWeight(this.fish, 0);
        this.filletAmount = AquacultureAPI.FISH_DATA.getFilletAmount(this.fish, 1);
        AquacultureAPI.FISH_DATA.remove(this.fish);
    }

    @Override
    public String describe() {
        return "Removing FishData for: " + BuiltInRegistries.ITEM.getKey(this.fish).toString();
    }

    @Override
    public void undo() {
        if (this.filletAmount > 0) {
            AquacultureAPI.FISH_DATA.add(this.fish, this.min, this.max, this.filletAmount);
        } else {
            AquacultureAPI.FISH_DATA.add(this.fish, this.min, this.max, 0);
        }
    }

    @Override
    public String describeUndo() {
        return "Undoing removal of FishData for: " + BuiltInRegistries.ITEM.getKey(this.fish).toString();
    }
}