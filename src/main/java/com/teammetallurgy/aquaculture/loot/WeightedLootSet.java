package com.teammetallurgy.aquaculture.loot;

import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.*;

public class WeightedLootSet {
    public Map<Integer, ItemStack> loot;
    public Map<Integer, Integer> lootMin;
    public Map<Integer, Integer> lootMax;
    public int totalWeight;

    public WeightedLootSet() {
        loot = new HashMap<>();
        lootMin = new HashMap<>();
        lootMax = new HashMap<>();
        totalWeight = 0;
    }

    public void addLoot(Object item, int weight) {
        addLoot(item, weight, 1, 1);
    }

    public void addLoot(Object item, int weight, int min, int max) {
        if (item instanceof Item) {
            addLoot(new ItemStack((Item) item), weight, min, max);
        } else if (item instanceof Block) {
            addLoot(new ItemStack((Block) item), weight, min, max);
        } else if (item instanceof ItemStack) {
            addLoot((ItemStack) item, weight, min, max);
        }
    }

    public void addLoot(ItemStack stack, int weight, int min, int max) {
        if (weight <= 0 || stack == null)
            return;

        loot.put(totalWeight + weight, stack);
        lootMin.put(totalWeight + weight, min);
        lootMax.put(totalWeight + weight, max);
        totalWeight += weight;
    }

    public ItemStack getRandomLoot() {
        Random rand = new Random();
        int weight = rand.nextInt(totalWeight + 1);

        ItemStack stack = null;

        Set<Integer> keySet = loot.keySet();
        Integer[] keys = keySet.toArray(new Integer[keySet.size()]);
        Arrays.sort(keys);

        for (Integer key : keys) {
            if (key >= weight) {
                stack = loot.get(key).copy();
                int min = lootMin.get(key);
                int max = lootMax.get(key);
                int amount = rand.nextInt(max - min + 1) + min;
                stack.setCount(amount);
                if (stack.getItem() == Items.ENCHANTED_BOOK) {
                    EnchantmentHelper.addRandomEnchantment(rand, stack, 5 + rand.nextInt(15), true);
                }
                break;
            }
        }
        return stack;
    }
}