package com.teammetallurgy.aquaculture.items.meta;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.Optional.Interface;
import squeek.applecore.api.food.FoodValues;
import squeek.applecore.api.food.IEdible;
import squeek.applecore.api.food.ItemFoodProxy;

import javax.annotation.Nonnull;

@Interface(iface = "squeek.applecore.api.food.IEdible", modid = "applecore")
public class SubItemFood implements IEdible {
    public int damage;
    public MetaItemFood item;
    public String unlocalizedName;
    public final int itemUseDuration;
    private final int healAmount;
    private final float saturationModifier;
    private final boolean isWolfsFavoriteMeat;
    private boolean alwaysEdible;
    private Potion potion;
    private int potionDuration;
    private int potionAmplifier;
    private float potionEffectProbability;
    private int eatTime;

    public SubItemFood(MetaItemFood metaItem, int heal, float saturation, boolean isWolfFood) {
        item = metaItem;
        damage = item.addSubItem(this);

        this.itemUseDuration = 32;
        this.healAmount = heal;
        this.isWolfsFavoriteMeat = isWolfFood;
        this.saturationModifier = saturation;
        this.eatTime = 32;
    }

    public SubItemFood(MetaItemFood metaItem, int heal, boolean isWolfFood) {
        this(metaItem, heal, 0.6F, isWolfFood);
    }

    public SubItemFood setCreativeTab(CreativeTabs tab) {
        item.setCreativeTab(tab);
        return this;
    }

    public SubItemFood setUnlocalizedName(String name) {
        this.unlocalizedName = name;
        return this;
    }

    @Nonnull
    public ItemStack getItemStack() {
        return getItemStack(1);
    }

    @Nonnull
    public ItemStack getItemStack(int amount) {
        return new ItemStack(item, amount, damage);
    }

    public String getUnlocalizedName(@Nonnull ItemStack stack) {
        return unlocalizedName;
    }

    public SubItemFood setEatTime(int eatTime) {
        this.eatTime = eatTime;
        return this;
    }

    @Override
    @Optional.Method(modid = "applecore")
    public FoodValues getFoodValues(@Nonnull ItemStack stack) {
        return new FoodValues(this.getHealAmount(), this.getSaturationModifier());
    }
    
    @Optional.Method(modid = "applecore")
    public void onEatenAppleCore(@Nonnull ItemStack stack, EntityPlayer player) {
        player.getFoodStats().addStats(new ItemFoodProxy(this), stack);
    }

    public ItemStack onEaten(@Nonnull ItemStack stack, World world, EntityLivingBase entity) {
        stack.shrink(1);
        if (!(entity instanceof EntityPlayer)) {
            return stack;
        }

        EntityPlayer player = (EntityPlayer) entity;

        if (Loader.isModLoaded("applecore")) {
            onEatenAppleCore(stack, player);
        } else {
            player.getFoodStats().addStats(this.getHealAmount(), this.getSaturationModifier());
            world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
            this.onFoodEaten(stack, world, player);
        }
        return stack;
    }

    protected void onFoodEaten(@Nonnull ItemStack stack, World world, EntityPlayer player) {
        if (!world.isRemote && this.potion != null && world.rand.nextFloat() < this.potionEffectProbability) {
            player.addPotionEffect(new PotionEffect(this.potion, this.potionDuration * 20, this.potionAmplifier));
        }
    }

    public int getMaxItemUseDuration(@Nonnull ItemStack stack) {
        return eatTime;
    }

    public EnumAction getItemUseAction(@Nonnull ItemStack stack) {
        return EnumAction.EAT;
    }

    @Nonnull
    public ItemStack onItemRightClick(@Nonnull ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        if (player.canEat(this.alwaysEdible)) {
            player.setActiveHand(hand);
        }

        return stack;
    }

    public int getHealAmount() {
        return this.healAmount;
    }

    public float getSaturationModifier() {
        return this.saturationModifier;
    }

    public boolean isWolfsFavoriteMeat() {
        return this.isWolfsFavoriteMeat;
    }

    public SubItemFood setPotionEffect(Potion potion, int duration, int amplifier, float effectPribability) {
        this.potion = potion;
        this.potionDuration = duration;
        this.potionAmplifier = amplifier;
        this.potionEffectProbability = effectPribability;
        return this;
    }

    public SubItemFood setAlwaysEdible() {
        this.alwaysEdible = true;
        return this;
    }
}
