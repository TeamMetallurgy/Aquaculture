package com.teammetallurgy.aquaculture.api.fish;

import com.teammetallurgy.aquaculture.Aquaculture;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class FishData {
    private final ConcurrentHashMap<Item, Double> WEIGHT_MIN = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Item, Double> WEIGHT_MAX = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Item, Integer> FILLET_AMOUNT = new ConcurrentHashMap<>();

    public void addWeight(Item fish, double min, double max) {
        if (!WEIGHT_MAX.containsKey(fish)) {
            WEIGHT_MIN.put(fish, min);
            WEIGHT_MAX.put(fish, max);
        } else {
            Aquaculture.LOG.error(fish.getRegistryName() + " already have a fish weight assigned to it");
        }
    }

    public double getMinWeight(Item fish) {
        return WEIGHT_MIN.get(fish);
    }

    public double getMaxWeight(Item fish) {
        return WEIGHT_MAX.get(fish);
    }

    public boolean hasWeight(Item fish) {
        return WEIGHT_MIN.containsKey(fish) || WEIGHT_MAX.containsKey(fish);
    }

    public List<Item> getFish() {
        return new ArrayList<>(WEIGHT_MIN.keySet());
    }

    public void add(Item fish, double min, double max) {
        this.add(fish, min, max, 1);
    }

    public void add(Item fish, double min, double max, int filletAmount) {
        this.addWeight(fish, min, max);
        if (filletAmount > 0) {
            this.addFilletAmount(fish, filletAmount);
        }
    }

    public void addFilletAmount(Item fish, int filletAmount) {
        if (!FILLET_AMOUNT.containsKey(fish)) {
            FILLET_AMOUNT.put(fish, filletAmount);
        } else {
            Aquaculture.LOG.error(fish.getRegistryName() + " already have a fish fillet amount assigned to it");
        }
    }

    public boolean hasFilletAmount(Item fish) {
        return FILLET_AMOUNT.containsKey(fish);
    }

    public int getFilletAmount(Item fish) {
        return FILLET_AMOUNT.get(fish);
    }
}