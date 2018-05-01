package com.teammetallurgy.aquaculture.items;

import com.google.common.base.Preconditions;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

public class ItemFish extends Item {
    public NonNullList<Fish> fish;

    public ItemFish() {
        super();
        this.setHasSubtypes(true);
        this.setMaxDamage(0);

        fish = NonNullList.create();
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

    @Override
    @Nonnull
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound() != null) {
            if (stack.getTagCompound().hasKey("Prefix")) {
                return stack.getTagCompound().getString("Prefix") + " " + super.getItemStackDisplayName(stack);
            }
        }
        return super.getItemStackDisplayName(stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, @Nullable World world, List<String> toolTip, ITooltipFlag tooltipType) {
        if (stack.hasTagCompound() && stack.getTagCompound() != null) {
            if (stack.getTagCompound().hasKey("Weight")) {
                float weight = stack.getTagCompound().getFloat("Weight");

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

    public void assignRandomWeight(@Nonnull ItemStack stack, int heavyLineLvl) {
        if (stack.isEmpty())
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

        Preconditions.checkNotNull(stack.getTagCompound(), "tagCompound");
        stack.getTagCompound().setFloat("Weight", weight);

        if (weight <= f.maxWeight / 10.0) {
            stack.getTagCompound().setString("Prefix", "Juvenile");
        }

        if (weight > f.maxWeight) {
            stack.getTagCompound().setString("Prefix", "Massive");
        }

    }

    @Nonnull
    public ItemStack getItemStackFish(String name) {
        for (int i = 0; i < fish.size(); i++) {
            if (fish.get(i).name.equals(name)) {
                return new ItemStack(this, 1, i);
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    @Nonnull
    public String getUnlocalizedName(@Nonnull ItemStack stack) {
        int i = MathHelper.clamp(stack.getItemDamage(), 0, fish.size());
        String uname = super.getUnlocalizedName() + "." + fish.get(i).name;
        uname = uname.replace(" ", "_");
        return uname;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> subItems) {
        if (this.isInCreativeTab(tab)) {
            for (int j = 0; j < fish.size(); ++j) {
                subItems.add(new ItemStack(this, 1, j));
            }
        }
    }

    public class Fish {
        public String name;
        public int filletAmount;
        public int minWeight;
        public int maxWeight;

        public Fish(String name, int amount, int min, int max) {
            this.name = name;
            filletAmount = amount;
            maxWeight = max;
            minWeight = min;
        }
    }
}
