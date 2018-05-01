package com.teammetallurgy.aquaculture.items;

import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.handlers.Config;
import com.teammetallurgy.aquaculture.items.meta.MetaItem;
import com.teammetallurgy.aquaculture.items.meta.MetaItemFood;
import com.teammetallurgy.aquaculture.items.meta.SubItem;
import com.teammetallurgy.aquaculture.items.meta.SubItemFood;
import com.teammetallurgy.aquaculture.loot.BiomeType;
import com.teammetallurgy.aquaculture.loot.FishLoot;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Locale;

public class AquacultureItems {
    public static final Item woodenFishingRod = new ItemAquacultureWoodenFishingRod(50, 10, "Wood").setUnlocalizedName("fishingRod").setCreativeTab(Aquaculture.tab);
    public static final Item ironFishingRod = new ItemAquacultureFishingRod(75, 14, "Iron").setUnlocalizedName("IronFishingRod").setCreativeTab(Aquaculture.tab);
    public static final Item goldFishingRod = new ItemAquacultureFishingRod(50, 22, "Gold").setUnlocalizedName("GoldFishingRod").setCreativeTab(Aquaculture.tab);
    public static final Item diamondFishingRod = new ItemAquacultureFishingRod(250, 10, "Diamond").setUnlocalizedName("DiamondFishingRod").setCreativeTab(Aquaculture.tab);
    public static final Item adminFishingRod = new ItemAdminFishingRod(75).setUnlocalizedName("AdminFishingRod").setCreativeTab(Aquaculture.tab);

    public static final MetaItemFood metaFoodItem = (MetaItemFood) new MetaItemFood().setUnlocalizedName("food").setCreativeTab(Aquaculture.tab);
    public static final MetaItem metaLootItem = (MetaItem) new MetaItem().setUnlocalizedName("loot").setCreativeTab(Aquaculture.tab);

    public static final SubItemFood seaweed = new SubItemFood(metaFoodItem, 2, 0, false).setUnlocalizedName("Seaweed").setCreativeTab(Aquaculture.tab);
    public static final SubItemFood algae = new SubItemFood(metaFoodItem, 2, 0, false).setUnlocalizedName("Algae").setCreativeTab(Aquaculture.tab);

    // Food
    public static final SubItemFood whaleSteak = new SubItemFood(metaFoodItem, 2, 0.3F, false).setUnlocalizedName("RawWhaleSteak").setCreativeTab(Aquaculture.tab);
    public static final SubItemFood fishFillet = new SubItemFood(metaFoodItem, 2, 0.3F, false).setUnlocalizedName("RawFishFillet").setCreativeTab(Aquaculture.tab);
    public static final SubItemFood cookedFillet = new SubItemFood(metaFoodItem, 5, 0.6F, false).setUnlocalizedName("CookedFishFillet").setCreativeTab(Aquaculture.tab);
    public static final SubItemFood cookedWhaleSteak = new SubItemFood(metaFoodItem, 10, 0.8F, false).setUnlocalizedName("CookedWhaleSteak").setCreativeTab(Aquaculture.tab);
    public static final SubItemFood whaleBurger = new SubItemFood(metaFoodItem, 20, 0.8F, false).setUnlocalizedName("Whaleburger").setCreativeTab(Aquaculture.tab);
    public static final SubItemFood frogLegs = new SubItemFood(metaFoodItem, 2, 0.3F, false).setUnlocalizedName("FrogLegs").setCreativeTab(Aquaculture.tab);
    public static final SubItemFood cookedFrogLegs = new SubItemFood(metaFoodItem, 3, 0.6F, false).setUnlocalizedName("CookedFrogLegs").setCreativeTab(Aquaculture.tab);
    public static final SubItemFood turtleSoup = new SubItemFood(metaFoodItem, 6, 0.6F, false).setUnlocalizedName("TurtleSoup").setCreativeTab(Aquaculture.tab);
    public static final SubItemFood sushi = new SubItemFood(metaFoodItem, 4, 0.6F, false).setUnlocalizedName("Sushi").setCreativeTab(Aquaculture.tab);

    public static final SubItem driftwood = new SubItem(metaLootItem).setUnlocalizedName("Driftwood").setCreativeTab(Aquaculture.tab);
    public static final SubItem neptuniumBar = new SubItem(metaLootItem).setUnlocalizedName("NeptuniumIngot").setCreativeTab(Aquaculture.tab);
    public static final SubItem tinCan = new SubItem(metaLootItem).setUnlocalizedName("TinCan").setCreativeTab(Aquaculture.tab);
    public static final SubItem nessageInABottle = new ItemMessageInABottle(metaLootItem).setUnlocalizedName("MessageInABottle").setCreativeTab(Aquaculture.tab);
    public static final SubItem box = new ItemBox(metaLootItem).setUnlocalizedName("Box").setCreativeTab(Aquaculture.tab);
    public static final SubItem lockbox = new ItemLockbox(metaLootItem).setUnlocalizedName("Lockbox").setCreativeTab(Aquaculture.tab);
    public static final SubItem treasureChest = new ItemTreasureChest(metaLootItem).setUnlocalizedName("TreasureChest").setCreativeTab(Aquaculture.tab);
    public static final SubItem neptunesBounty = new ItemNeptunesBounty(metaLootItem).setUnlocalizedName("NeptunesBounty").setCreativeTab(Aquaculture.tab);
    public static final ItemFish fish = (ItemFish) new ItemFish().setUnlocalizedName("Fish").setCreativeTab(Aquaculture.tab);
    private static ToolMaterial neptuniumEnum = EnumHelper.addToolMaterial("Neptunium", 3, 2500, 9F, 6F, 15);
    public static final Item neptuniumPickaxe = new AquaItemPickaxe(neptuniumEnum).setUnlocalizedName("NeptuniumPickaxe").setCreativeTab(Aquaculture.tab);
    public static final Item neptuniumShovel = new AquaItemSpade(neptuniumEnum).setUnlocalizedName("NeptuniumShovel").setCreativeTab(Aquaculture.tab);
    public static final Item neptuniumAxe = new AquaItemAxe(neptuniumEnum).setUnlocalizedName("NeptuniumAxe").setCreativeTab(Aquaculture.tab);
    public static final Item neptuniumHoe = new AquaItemHoe(neptuniumEnum).setUnlocalizedName("NeptuniumHoe").setCreativeTab(Aquaculture.tab);
    public static final Item neptuniumSword = new AquaItemSword(neptuniumEnum).setUnlocalizedName("NeptuniumSword").setCreativeTab(Aquaculture.tab);
    private static ArmorMaterial neptuniumArmorEnum = EnumHelper.addArmorMaterial("Neptunium", "", 75, new int[] { 3, 8, 6, 3 }, 15, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2.0F);
    public static final Item neptuniumHelmet = new NeptuniumArmor(neptuniumArmorEnum, 0, EntityEquipmentSlot.HEAD).setArmorTexture("neptunium_layer_1").setUnlocalizedName("NeptuniumHelmet").setCreativeTab(Aquaculture.tab);
    public static final Item neptuniumPlate = new NeptuniumArmor(neptuniumArmorEnum, 0, EntityEquipmentSlot.CHEST).setArmorTexture("neptunium_layer_1").setUnlocalizedName("NeptuniumChestplate").setCreativeTab(Aquaculture.tab);
    public static final Item neptuniumLegs = new NeptuniumArmor(neptuniumArmorEnum, 0, EntityEquipmentSlot.LEGS).setArmorTexture("neptunium_layer_2").setUnlocalizedName("NeptuniumLeggings").setCreativeTab(Aquaculture.tab);
    public static final Item neptuniumBoots = new NeptuniumArmor(neptuniumArmorEnum, 0, EntityEquipmentSlot.FEET).setArmorTexture("neptunium_layer_1").setUnlocalizedName("NeptuniumBoots").setCreativeTab(Aquaculture.tab);

    public void register() {

        register(woodenFishingRod);
        register(ironFishingRod);
        register(goldFishingRod);
        register(diamondFishingRod);
        register(adminFishingRod);

        register(metaLootItem);
        register(metaFoodItem);

        register(neptuniumPickaxe);
        register(neptuniumShovel);
        register(neptuniumAxe);
        register(neptuniumHoe);
        register(neptuniumSword);

        register(neptuniumHelmet);
        register(neptuniumPlate);
        register(neptuniumLegs);
        register(neptuniumBoots);

        register(fish);

        // Adding fish to main fish item

        // Freshwater
        fish.addFish("Bluegill", 1, 1, 5, BiomeType.freshwater, Config.fishRarity.get("Bluegill"));
        fish.addFish("Perch", 1, 1, 5, BiomeType.freshwater, Config.fishRarity.get("Perch"));
        fish.addFish("Gar", 2, 1, 10, BiomeType.freshwater, Config.fishRarity.get("Gar"));
        fish.addFish("Bass", 3, 1, 25, BiomeType.freshwater, Config.fishRarity.get("Bass"));
        fish.addFish("Muskellunge", 3, 1, 35, BiomeType.freshwater, Config.fishRarity.get("Muskellunge"));
        fish.addFish("Brown Trout", 3, 1, 40, BiomeType.freshwater, Config.fishRarity.get("Brown Trout"));
        fish.addFish("Catfish", 4, 1, 50, BiomeType.freshwater, Config.fishRarity.get("Catfish"));
        fish.addFish("Carp", 5, 1, 100, BiomeType.freshwater, Config.fishRarity.get("Carp"));

        // Saltwater
        fish.addFish("Blowfish", 3, 1, 25, BiomeType.saltwater, Config.fishRarity.get("Blowfish"));
        fish.addFish("Red Grouper", 4, 1, 50, BiomeType.saltwater, Config.fishRarity.get("Red Grouper"));
        fish.addFish("Salmon", 5, 1, 100, BiomeType.saltwater, Config.fishRarity.get("Salmon"));
        fish.addFish("Tuna", 5, 1, 135, BiomeType.saltwater, Config.fishRarity.get("Tuna"));
        fish.addFish("Swordfish", 7, 1, 1400, BiomeType.saltwater, Config.fishRarity.get("Swordfish"));
        fish.addFish("Shark", 8, 1, 5000, BiomeType.saltwater, Config.fishRarity.get("Shark"));
        fish.addFish("Whale", 0, 1, 190000, BiomeType.saltwater, Config.fishRarity.get("Whale"));
        fish.addFish("Squid", 0, 1, 1000, BiomeType.saltwater, Config.fishRarity.get("Squid"));
        fish.addFish("Jellyfish", 0, 1, 500, BiomeType.saltwater, Config.fishRarity.get("Jellyfish"));

        // Brackish
        fish.addFish("Frog", 0, 1, 1, BiomeType.brackish, Config.fishRarity.get("Frog"));
        fish.addFish("Turtle", 0, 1, 5, BiomeType.brackish, Config.fishRarity.get("Turtle"));
        fish.addFish("Leech", 0, 1, 1, BiomeType.brackish, Config.fishRarity.get("Leech"));

        // Jungle
        fish.addFish("Pirahna", 1, 1, 8, BiomeType.tropical, Config.fishRarity.get("Pirahna"));
        fish.addFish("Electric Eel", 3, 1, 45, BiomeType.tropical, Config.fishRarity.get("Electric Eel"));
        fish.addFish("Tambaqui", 4, 1, 75, BiomeType.tropical, Config.fishRarity.get("Tambaqui"));
        fish.addFish("Arapaima", 6, 1, 220, BiomeType.tropical, Config.fishRarity.get("Arapaima"));

        // Tundra
        fish.addFish("Cod", 6, 1, 210, BiomeType.arctic, Config.fishRarity.get("Cod"));
        fish.addFish("Pollock", 3, 1, 45, BiomeType.arctic, Config.fishRarity.get("Pollock"));
        fish.addFish("Herring", 1, 1, 3, BiomeType.arctic, Config.fishRarity.get("Herring"));
        fish.addFish("Halibut", 7, 1, 700, BiomeType.arctic, Config.fishRarity.get("Halibut"));
        fish.addFish("Pink Salmon", 5, 1, 100, BiomeType.arctic, Config.fishRarity.get("Pink Salmon"));
        fish.addFish("Rainbow Trout", 4, 1, 50, BiomeType.arctic, Config.fishRarity.get("Rainbow Trout"));
        fish.addFish("Blackfish", 2, 1, 10, BiomeType.arctic, Config.fishRarity.get("Blackfish"));

        // Desert
        fish.addFish("Capitaine", 4, 1, 450, BiomeType.arid, Config.fishRarity.get("Capitaine"));
        fish.addFish("Boulti", 2, 1, 10, BiomeType.arid, Config.fishRarity.get("Boulti"));
        fish.addFish("Bagrid", 3, 1, 25, BiomeType.arid, Config.fishRarity.get("Bagrid"));
        fish.addFish("Syndontis", 1, 1, 3, BiomeType.arid, Config.fishRarity.get("Syndontis"));

        // Mushroom Island
        fish.addFish("Red Shrooma", 0, 1, 5, BiomeType.mushroom, Config.fishRarity.get("Red Shrooma"));
        fish.addFish("Brown Shrooma", 0, 1, 5, BiomeType.mushroom, Config.fishRarity.get("Brown Shrooma"));

        fish.addFish("Goldfish", 0, 1, 4, new BiomeType[] {}, 1);
        fish.addFish("Fish Bones", 0, 1, 1, new BiomeType[] {}, 1);

        FishLoot.instance().addJunkLoot(seaweed.getItemStack(), BiomeType.saltwater, Config.junkRarity.get("Seaweed"));
        FishLoot.instance().addJunkLoot(algae.getItemStack(), new BiomeType[] { BiomeType.arctic, BiomeType.arid, BiomeType.brackish, BiomeType.freshwater, BiomeType.mushroom, BiomeType.tropical }, Config.junkRarity.get("Algae"));
        FishLoot.instance().addJunkLoot(driftwood.getItemStack(), Config.junkRarity.get("Driftwood"));
        FishLoot.instance().addJunkLoot(tinCan.getItemStack(), Config.junkRarity.get("Tin Can"));
        FishLoot.instance().addJunkLoot(box.getItemStack(), Config.junkRarity.get("Box"));
        FishLoot.instance().addJunkLoot(lockbox.getItemStack(), Config.junkRarity.get("Lockbox"));
        FishLoot.instance().addJunkLoot(treasureChest.getItemStack(), Config.junkRarity.get("Treasure Chest"));
        FishLoot.instance().addJunkLoot(new ItemStack(Items.STICK), Config.junkRarity.get("Stick"));
        FishLoot.instance().addJunkLoot(new ItemStack(Items.BONE), Config.junkRarity.get("Bone"));
        FishLoot.instance().addJunkLoot(new ItemStack(Items.LEATHER_BOOTS), Config.junkRarity.get("Leather Boots"));
        FishLoot.instance().addJunkLoot(new ItemStack(Items.APPLE), Config.junkRarity.get("Apple"));
        FishLoot.instance().addJunkLoot(fish.getItemStackFish("Goldfish"), Config.junkRarity.get("Goldfish"));
        FishLoot.instance().addJunkLoot(nessageInABottle.getItemStack(), Config.junkRarity.get("Message In A Bottle"));
        FishLoot.instance().addJunkLoot(neptunesBounty.getItemStack(), Config.junkRarity.get("Neptunes Bounty"));

        OreDictionary.registerOre("listAllfishraw", fishFillet.getItemStack());
        OreDictionary.registerOre("listAllfishcooked", cookedFillet.getItemStack());
        OreDictionary.registerOre("cropSeaweed", seaweed.getItemStack());
    }

    public void register(Item item) {
        String name = item.getUnlocalizedName();
        name = name.replaceAll("item.", "");
        name = name.replaceAll("([A-Za-z][a-z\\d]+)(?=([A-Z][a-z\\d]+))", "$1_").toLowerCase(Locale.US);
        item.setRegistryName(name);
        ForgeRegistries.ITEMS.register(item);
    }
}
