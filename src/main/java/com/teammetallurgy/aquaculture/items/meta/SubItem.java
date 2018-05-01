package com.teammetallurgy.aquaculture.items.meta;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class SubItem {
    public int itemID;
    public int damage;
    public MetaItem item;

    public String unlocalizedName;

    public SubItem(MetaItem metaItem) {

        item = metaItem;
        damage = item.addSubItem(this);
    }

    public SubItem setCreativeTab(CreativeTabs tab) {
        item.setCreativeTab(tab);
        return this;
    }

    public SubItem setUnlocalizedName(String name) {
        this.unlocalizedName = name;
        return this;
    }

    @Nonnull
    public ItemStack getItemStack() {
        return getItemStack(1);
    }

    @Nonnull
    public ItemStack getItemStack(int i) {
        return new ItemStack(item, i, damage);
    }

    public String getUnlocalizedName(@Nonnull ItemStack stack) {
        return unlocalizedName;
    }

    @Nonnull
    public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player) {
        return stack;
    }

    public int getMaxItemUseDuration(@Nonnull ItemStack stack) {
        return 0;
    }

    public EnumAction getItemUseAction(@Nonnull ItemStack stack) {
        return EnumAction.NONE;
    }

    @Nonnull
    public ItemStack onItemRightClick(@Nonnull ItemStack stack, World world, EntityPlayer player) {
        return stack;
    }
}
