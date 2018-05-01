package com.teammetallurgy.aquaculture.items;

import com.teammetallurgy.aquaculture.items.meta.MetaItem;
import com.teammetallurgy.aquaculture.items.meta.SubItem;
import com.teammetallurgy.aquaculture.loot.WeightedLootSet;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemBox extends SubItem {
    public WeightedLootSet loot;

    public ItemBox(MetaItem metaItem) {
        super(metaItem);
        loot = new WeightedLootSet();
        loot.addLoot(Blocks.STONE, 5, 1, 1);
        loot.addLoot(Blocks.DIRT, 5, 1, 1);
        loot.addLoot(Blocks.COBBLESTONE, 5);
        loot.addLoot(new ItemStack(Blocks.PLANKS, 1, 0), 1);
        loot.addLoot(new ItemStack(Blocks.PLANKS, 1, 1), 1);
        loot.addLoot(new ItemStack(Blocks.PLANKS, 1, 2), 1);
        loot.addLoot(new ItemStack(Blocks.PLANKS, 1, 3), 1);
        loot.addLoot(new ItemStack(Blocks.SAPLING, 1, 0), 1);
        loot.addLoot(new ItemStack(Blocks.SAPLING, 1, 1), 1);
        loot.addLoot(new ItemStack(Blocks.SAPLING, 1, 2), 1);
        loot.addLoot(new ItemStack(Blocks.SAPLING, 1, 3), 1);
        loot.addLoot(new ItemStack(Blocks.LOG, 1, 0), 1);
        loot.addLoot(new ItemStack(Blocks.LOG, 1, 1), 1);
        loot.addLoot(new ItemStack(Blocks.LOG, 1, 2), 1);
        loot.addLoot(new ItemStack(Blocks.LOG, 1, 3), 1);
        loot.addLoot(Blocks.GRAVEL, 5);
        loot.addLoot(new ItemStack(Items.COAL, 1, 0), 3);
        loot.addLoot(new ItemStack(Items.COAL, 1, 1), 3);
        loot.addLoot(Items.WHEAT_SEEDS, 3);
        loot.addLoot(Items.STICK, 5);
        loot.addLoot(Items.BOWL, 3);
        loot.addLoot(Items.LEATHER_HELMET, 2);
        loot.addLoot(Items.LEATHER_CHESTPLATE, 2);
        loot.addLoot(Items.LEATHER_LEGGINGS, 2);
        loot.addLoot(Items.LEATHER_BOOTS, 2);
        loot.addLoot(Items.FLINT, 4);
        loot.addLoot(Items.CLAY_BALL, 4);
        loot.addLoot(Items.BUCKET, 1);
        loot.addLoot(Items.LEATHER, 4);
        loot.addLoot(Items.SLIME_BALL, 1);
        loot.addLoot(Blocks.REEDS, 1);
        loot.addLoot(Items.BONE, 5);
        loot.addLoot(Items.ROTTEN_FLESH, 5);
        loot.addLoot(Items.GLASS_BOTTLE, 1);
        loot.addLoot(Items.CARROT, 1);
        loot.addLoot(Items.POTATO, 1);
        loot.addLoot(Blocks.VINE, 1);
        loot.addLoot(Blocks.TALLGRASS, 3);
        loot.addLoot(Items.STRING, 3);
        loot.addLoot(Items.FEATHER, 4);
        loot.addLoot(Items.APPLE, 1);
    }

    @Override
    @Nonnull
    public ItemStack onItemRightClick(@Nonnull ItemStack stack, World world, EntityPlayer player) {
        if (world.isRemote)
            return stack;

        ItemStack randomLoot = loot.getRandomLoot();

        EntityItem entityitem = new EntityItem(player.world, player.posX, player.posY, player.posZ, randomLoot);
        world.spawnEntity(entityitem);

        stack.shrink(1);
        return stack;
    }
}
