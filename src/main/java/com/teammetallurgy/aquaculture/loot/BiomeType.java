package com.teammetallurgy.aquaculture.loot;

import java.util.HashMap;
import java.util.Map;

public class BiomeType {
    public static BiomeType freshwater = new BiomeType("Freshwater", new int[] { 1, 3, 4, 7, 18, 20 });
    public static BiomeType arid = new BiomeType("Arid", new int[] { 2, 17 });
    public static BiomeType arctic = new BiomeType("Arctic", new int[] { 5, 10, 11, 12, 13, 19 });
    public static BiomeType saltwater = new BiomeType("Saltwater", new int[] { 0, 16 });
    public static BiomeType tropical = new BiomeType("Tropical", new int[] { 21, 22 });
    public static BiomeType brackish = new BiomeType("Brackish", new int[] { 6 });
    public static BiomeType mushroom = new BiomeType("Mushroom", new int[] { 14, 15 });

    protected static Map<Integer, BiomeType> biomeMap;

    private String name;

    private BiomeType(String name, int[] biomes) {
        this.name = name;
        for (int biome : biomes) {
            addBiome(biome, this);
        }
        FishLoot.instance().addBiome(this);
    }

    public static void addBiome(int biomeID, BiomeType biomeType) {
        if (biomeMap == null)
            biomeMap = new HashMap<>();

        if (biomeMap.containsKey(biomeID)) {
            System.out.println("[Aquaculture] Error: Biome ID " + biomeID + " already registered as " + biomeMap.get(biomeID) + " when trying to register " + biomeType);
        } else {
            biomeMap.put(biomeID, biomeType);
        }
    }

    public static BiomeType getBiomeType(int biomeID) {
        return biomeMap.get(biomeID);
    }

    @Override
    public String toString() {
        return "BiomeType:" + name;
    }
}