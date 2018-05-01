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

public class ItemTreasureChest extends SubItem {
    private WeightedLootSet loot;

    public ItemTreasureChest(MetaItem par1) {
        super(par1);
        loot = new WeightedLootSet();
        loot.addLoot(Items.IRON_INGOT, 40);
        loot.addLoot(Items.GOLD_INGOT, 30);
        loot.addLoot(Items.DIAMOND, 15);
        loot.addLoot(Items.EMERALD, 15);
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