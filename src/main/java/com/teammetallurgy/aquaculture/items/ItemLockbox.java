package com.teammetallurgy.aquaculture.items;

import com.teammetallurgy.aquaculture.items.meta.MetaItem;
import com.teammetallurgy.aquaculture.items.meta.SubItem;
import com.teammetallurgy.aquaculture.loot.WeightedLootSet;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemLockbox extends SubItem {
    public WeightedLootSet loot;

    public ItemLockbox(MetaItem metaItem) {
        super(metaItem);
        loot = new WeightedLootSet();
        loot.addLoot(Items.IRON_INGOT, 8);
        loot.addLoot(Items.GOLD_INGOT, 7);
        loot.addLoot(Items.GOLDEN_APPLE, 5);
        loot.addLoot(new ItemStack(Items.DYE, 1, 4), 8);
        loot.addLoot(Items.REDSTONE, 8);
        loot.addLoot(Items.BOOK, 10);
        loot.addLoot(Items.PAPER, 10);
        loot.addLoot(Items.COMPASS, 10);
        loot.addLoot(Items.CLOCK, 10);
        loot.addLoot(Items.GLOWSTONE_DUST, 7);
        loot.addLoot(Items.GUNPOWDER, 10);
        loot.addLoot(Items.QUARTZ, 7);
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
