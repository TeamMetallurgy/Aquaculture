package com.teammetallurgy.aquaculture.loot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BiomeType {
    public static BiomeType freshwater = new BiomeType("Freshwater", new int[] { 1, 4, 6, 7, 14, 15, 18, 27, 28, 29, 33, 129, 132, 134, 155, 156, 157, 161 });
    public static BiomeType arid = new BiomeType("Arid", new int[] { 2, 17, 35, 36, 37, 38, 39, 130, 133, 163, 164, 165, 166, 167 });
    public static BiomeType arctic = new BiomeType("Arctic", new int[] { 3, 5, 10, 11, 12, 13, 19, 25, 26, 30, 31, 32, 34, 131, 140, 158, 160, 162 });
    public static BiomeType saltwater = new BiomeType("Saltwater", new int[] { 0, 16, 10, 24, 26 });
    public static BiomeType tropical = new BiomeType("Tropical", new int[] { 21, 22, 23, 149, 151 });
    public static BiomeType brackish = new BiomeType("Brackish", new int[] { 6, 134 });
    public static BiomeType mushroom = new BiomeType("Mushroom", new int[] { 14, 15 });

    private static Map<Integer, ArrayList<BiomeType>> biomeMap;

    private String name;

    private BiomeType(String name, int[] biomes) {
        this.name = name;
        for (int biome : biomes) {
            addBiome(biome, this);
        }
        FishLoot.instance().addBiome(this);
    }

    public static void addBiome(int biomeID, BiomeType biomeType) {
        if (biomeMap == null) {
            biomeMap = new HashMap<>();
        }

        if (biomeMap.containsKey(biomeID) && biomeMap.get(biomeID).contains(biomeType)) {
            System.out.println("[Aquaculture] Error: Biome ID " + biomeID + " is already registered as " + biomeMap.get(biomeID));
        } else {
        	if(!biomeMap.containsKey(biomeID))
        		biomeMap.put(biomeID, new ArrayList<BiomeType>());
            biomeMap.get(biomeID).add(biomeType);
        }
    }

    public static BiomeType getBiomeType(int biomeID) {
    	ArrayList<BiomeType> list = biomeMap.get(biomeID);
        return list.get(new Random().nextInt(list.size()));
    }

    @Override
    public String toString() {
        return "BiomeType:" + name;
    }
}