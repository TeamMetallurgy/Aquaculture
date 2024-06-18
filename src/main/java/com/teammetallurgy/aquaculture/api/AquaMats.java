package com.teammetallurgy.aquaculture.api;

import com.teammetallurgy.aquaculture.init.AquaItems;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nonnull;

public class AquaMats {
    public Tier NEPTUNIUM = new Tier() {
        @Override
        public int getUses() {
            return 1796;
        }

        @Override
        public float getSpeed() {
            return 8.5F;
        }

        @Override
        public float getAttackDamageBonus() {
            return 3.5F;
        }

        @Override
        @Nonnull
        public TagKey<Block> getIncorrectBlocksForDrops() {
            return BlockTags.INCORRECT_FOR_DIAMOND_TOOL;
        }

        @Override
        public int getEnchantmentValue() {
            return 14;
        }

        @Override
        @Nonnull
        public Ingredient getRepairIngredient() {
            return Ingredient.of(AquaItems.NEPTUNIUM_INGOT.get());
        }
    };
}