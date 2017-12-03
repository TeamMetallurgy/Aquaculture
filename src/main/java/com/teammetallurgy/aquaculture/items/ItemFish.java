package com.teammetallurgy.aquaculture.items;

import com.teammetallurgy.aquaculture.loot.BiomeType;
import com.teammetallurgy.aquaculture.loot.FishLoot;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

public class ItemFish extends Item {
    public List<Fish> fish;

    public ItemFish() {
        super();
        this.setHasSubtypes(true);
        this.setMaxDamage(0);

        fish = new ArrayList<>();
    }

    public void addFish(String name, int filletAmount, int minWeight, int maxWeight, BiomeType biome, int rarity) {
        addFish(name, filletAmount, minWeight, maxWeight, new BiomeType[] { biome }, rarity);
    }

    public void addFish(String name, int filletAmount, int minWeight, int maxWeight, BiomeType[] biomes, int rarity) {
        fish.add(new Fish(name, filletAmount, minWeight, maxWeight));

        for (BiomeType biome : biomes) {
            FishLoot.instance().addFish(this.getItemStackFish(name), biome, rarity);
        }

    }

    public String getItemDisplayName(ItemStack par1ItemStack) {
        if (par1ItemStack.hasTagCompound()) {
            if (par1ItemStack.getTagCompound().hasKey("Prefix")) {
                return par1ItemStack.getTagCompound().getString("Prefix") + " " + super.getItemStackDisplayName(par1ItemStack);
            }
        }
        return super.getItemStackDisplayName(par1ItemStack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, @Nullable World world, List<String> toolTip, ITooltipFlag tooltipType) {
        if (itemStack.hasTagCompound()) {
            if (itemStack.getTagCompound().hasKey("Weight")) {
                float weight = itemStack.getTagCompound().getFloat("Weight");

                DecimalFormat df = new DecimalFormat("#,###.##");
                BigDecimal bd = new BigDecimal(weight);
                bd = bd.round(new MathContext(3));
                if (bd.doubleValue() > 999)
                    toolTip.add("Weight: " + df.format((int) bd.doubleValue()) + "lb");
                else
                    toolTip.add("Weight: " + bd + "lb");
            }
        }
    }

    public void assignRandomWeight(ItemStack stack, int heavyLineLvl) {
        if (stack == null)
            return;

        Fish f = fish.get(stack.getItemDamage());

        if (f.maxWeight == 1 && f.minWeight == 1)
            return;

        Random rand = new Random();

        float min = f.minWeight;

        min += (f.maxWeight - min) * (0.1 * heavyLineLvl);

        float weight = rand.nextFloat() * ((f.maxWeight * 1.1f) - min) + min;

        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }

        stack.getTagCompound().setFloat("Weight", weight);

        if (weight <= f.maxWeight / 10.0) {
            stack.getTagCompound().setString("Prefix", "Juvenile");
        }

        if (weight > f.maxWeight) {
            stack.getTagCompound().setString("Prefix", "Massive");
        }

    }

    public ItemStack getItemStackFish(String name) {
        for (int i = 0; i < fish.size(); i++) {
            if (fish.get(i).name.equals(name)) {
                return new ItemStack(this, 1, i);
            }
        }

        return null;
    }

    /*
    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconFromDamage(int par1) {
        int j = MathHelper.clamp_int(par1, 0, fish.size());
        return fish.get(j).icon;
    }
     */

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have different names based on their damage or NBT.
     */
    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack) {
        int i = MathHelper.clamp(par1ItemStack.getItemDamage(), 0, fish.size());
        String uname = super.getUnlocalizedName() + "." + fish.get(i).name;
        uname = uname.replace(" ", "_");
        return uname;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
        if (this.isInCreativeTab(tab)) {
            for (int j = 0; j < fish.size(); ++j) {
                subItems.add(new ItemStack(this, 1, j));
            }
        }
    }

    /*
    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister par1IconRegister) {
        for (Fish f : fish) {
            f.icon = par1IconRegister.registerIcon("Aquaculture:" + f.name);
        }
    }
    */

    public class Fish {
        public String name;
        public int filletAmount;
        public int minWeight;
        public int maxWeight;
        // public IIcon icon;

        public Fish(String name, int amount, int min, int max) {
            this.name = name;
            filletAmount = amount;
            maxWeight = max;
            minWeight = min;
        }
    }
}
