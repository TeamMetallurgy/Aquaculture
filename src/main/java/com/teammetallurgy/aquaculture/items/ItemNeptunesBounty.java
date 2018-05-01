package com.teammetallurgy.aquaculture.items;

import com.teammetallurgy.aquaculture.items.meta.MetaItem;
import com.teammetallurgy.aquaculture.items.meta.SubItem;
import com.teammetallurgy.aquaculture.loot.WeightedLootSet;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemNeptunesBounty extends SubItem {
    private WeightedLootSet loot;

    public ItemNeptunesBounty(MetaItem metaItem) {
        super(metaItem);
        loot = null;
    }

    public void initLoot() {
        loot = new WeightedLootSet();

        loot.addLoot(AquacultureItems.neptuniumAxe, 1, 1, 1);
        loot.addLoot(AquacultureItems.neptuniumPickaxe, 1, 1, 1);
        loot.addLoot(AquacultureItems.neptuniumShovel, 1, 1, 1);
        loot.addLoot(AquacultureItems.neptuniumHoe, 1, 1, 1);
        loot.addLoot(AquacultureItems.neptuniumSword, 1, 1, 1);
        loot.addLoot(AquacultureItems.neptuniumHelmet, 1, 1, 1);
        loot.addLoot(AquacultureItems.neptuniumPlate, 1, 1, 1);
        loot.addLoot(AquacultureItems.neptuniumLegs, 1, 1, 1);
        loot.addLoot(AquacultureItems.neptuniumBoots, 1, 1, 1);
    }

    @Override
    @Nonnull
    public ItemStack onItemRightClick(@Nonnull ItemStack stack, World world, EntityPlayer player) {
        if (world.isRemote)
            return stack;

        if (loot == null)
            initLoot();

        ItemStack item = loot.getRandomLoot();

        EntityItem entityitem = new EntityItem(player.world, player.posX, player.posY, player.posZ, item);
        world.spawnEntity(entityitem);

        stack.shrink(1);
        return stack;
    }
}