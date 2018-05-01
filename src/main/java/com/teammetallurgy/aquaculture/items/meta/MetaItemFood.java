package com.teammetallurgy.aquaculture.items.meta;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.Optional.Interface;
import squeek.applecore.api.food.FoodValues;
import squeek.applecore.api.food.IEdible;

import javax.annotation.Nonnull;
import java.util.ArrayList;

@Interface(iface = "squeek.applecore.api.food.IEdible", modid = "applecore")
public class MetaItemFood extends ItemFood implements IEdible {

    ArrayList<SubItemFood> subItems;

    public MetaItemFood() {
        super(0, 0, false);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    public int addSubItem(SubItemFood subItem) {
        if (subItems == null)
            subItems = new ArrayList<>();

        subItems.add(subItem);
        return subItems.size() - 1;
    }

    @Override
    @Nonnull
    public String getUnlocalizedName(@Nonnull ItemStack stack) {
        int i = MathHelper.clamp(stack.getItemDamage(), 0, subItems.size() - 1);
        String uname = subItems.get(i).getUnlocalizedName(stack);
        uname = uname.replace(" ", "_");
        return "item." + uname;
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            for (SubItemFood subItem : subItems) {
                items.add(subItem.getItemStack());
            }
        }
    }

    @Override
    @Optional.Method(modid = "applecore")
    public FoodValues getFoodValues(@Nonnull ItemStack stack) {
        int damage = stack.getItemDamage();
        if (damage >= 0 && damage < subItems.size()) {
            return subItems.get(damage).getFoodValues(stack);
        }
    
        return null;
    }
    
    @Optional.Method(modid = "applecore")
    public void onEatenAppleCore(ItemStack stack, EntityPlayer player) {
        int damage = stack.getItemDamage();
        if (damage >= 0 && damage < subItems.size()) {
            subItems.get(damage).onEatenAppleCore(stack, player);
        }
    }

    // ItemRedirects
    @Override
    @Nonnull
    public ItemStack onItemUseFinish(@Nonnull ItemStack stack, @Nonnull World world, EntityLivingBase player) {
        int damage = stack.getItemDamage();
        if (damage >= 0 && damage < subItems.size()) {
            return subItems.get(damage).onEaten(stack, world, player);
        }
        return stack;
    }

    @Override
    public int getMaxItemUseDuration(@Nonnull ItemStack stack) {
        int damage = stack.getItemDamage();
        if (damage >= 0 && damage < subItems.size()) {
            return subItems.get(damage).getMaxItemUseDuration(stack);
        }

        return 0;
    }

    @Override
    @Nonnull
    public EnumAction getItemUseAction(@Nonnull ItemStack stack) {
        int damage = stack.getItemDamage();
        if (damage >= 0 && damage < subItems.size()) {
            return subItems.get(damage).getItemUseAction(stack);
        }

        return EnumAction.NONE;
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);

        int damage = stack.getItemDamage();
        if (damage >= 0 && damage < subItems.size()) {
            ItemStack result = subItems.get(damage).onItemRightClick(stack, world, player, hand);
            return new ActionResult<>(EnumActionResult.SUCCESS, result);
        }
        return new ActionResult<>(EnumActionResult.FAIL, stack);
    }

    @Override
    public int getHealAmount(@Nonnull ItemStack stack) {
        int damage = stack.getItemDamage();
        if (damage >= 0 && damage < subItems.size()) {
            return subItems.get(damage).getHealAmount();
        }
        return super.getHealAmount(stack);
    }

    @Override
    public float getSaturationModifier(@Nonnull ItemStack stack) {
        int damage = stack.getItemDamage();
        if (damage >= 0 && damage < subItems.size()) {
            return subItems.get(damage).getSaturationModifier();
        }
        return super.getSaturationModifier(stack);
    }
}