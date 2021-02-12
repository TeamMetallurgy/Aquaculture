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
        WEIGHT_MIN.put(fish, min);
        WEIGHT_MAX.put(fish, max);
    }

    public double getMinWeight(Item fish) {
        return WEIGHT_MIN.get(fish);
    }

    public double getMinWeight(Item fish, double defaultValue) {
        return WEIGHT_MIN.getOrDefault(fish, defaultValue);
    }

    public double getMaxWeight(Item fish) {
        return WEIGHT_MAX.get(fish);
    }

    public double getMaxWeight(Item fish, double defaultValue) {
        return WEIGHT_MAX.getOrDefault(fish, defaultValue);
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
        FILLET_AMOUNT.put(fish, filletAmount);
    }
    
    public void remove(Item fish, boolean fillet){
        WEIGHT_MIN.remove(fish);
        WEIGHT_MAX.remove(fish);
        if(fillet){
            FILLET_AMOUNT.remove(fish);
        }
    }

    public boolean hasFilletAmount(Item fish) {
        return FILLET_AMOUNT.containsKey(fish);
    }

    public int getFilletAmount(Item fish) {
        return FILLET_AMOUNT.get(fish);
    }

    public int getFilletAmount(Item fish, int defaultValue) {
        return FILLET_AMOUNT.getOrDefault(fish, defaultValue);
    }

    public static int getFilletAmountFromWeight(double weight) {
        if (weight >= 2 && weight < 10D) {
            return 1;
        } else if (weight >= 10 && weight < 50) {
            return 2;
        } else if (weight >= 50 && weight < 100) {
            return 3;
        } else if (weight >= 100 && weight < 150) {
            return 4;
        } else if (weight >= 150 && weight < 200) {
            return 5;
        } else if (weight >= 200 && weight < 250) {
            return 6;
        } else if (weight >= 250 && weight < 300) {
            return 7;
        } else if (weight >= 300 && weight < 350) {
            return 8;
        } else if (weight >= 350 && weight < 400) {
            return 9;
        } else if (weight >= 400 && weight < 450) {
            return 10;
        } else if (weight >= 450 && weight < 500) {
            return 11;
        } else if (weight >= 500 && weight < 600) {
            return 12;
        } else if (weight >= 600 && weight < 700) {
            return 13;
        } else if (weight >= 700 && weight < 800) {
            return 14;
        } else if (weight >= 800 && weight < 1000) {
            return 15;
        } else if (weight >= 1000) {
            return 16;
        } else {
            return 0;
        }
    }
}