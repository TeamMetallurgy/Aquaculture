package com.teammetallurgy.aquaculture.init;

import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.api.AquaArmorMaterials;
import com.teammetallurgy.aquaculture.api.AquacultureAPI;
import com.teammetallurgy.aquaculture.api.fishing.Hooks;
import com.teammetallurgy.aquaculture.entity.FishType;
import com.teammetallurgy.aquaculture.item.*;
import com.teammetallurgy.aquaculture.item.neptunium.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Unbreakable;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

public class AquaItems {
    public static final DeferredRegister.Items ITEM_DEFERRED = DeferredRegister.createItems(Aquaculture.MOD_ID);
    public static final Collection<DeferredItem<Item>> ITEMS_FOR_TAB_LIST = new ArrayList<>();
    public static final Collection<DeferredItem<Item>> SPAWN_EGGS = new ArrayList<>();

    //Fishing
    public static final DeferredItem<Item> IRON_FISHING_ROD = registerWithTab(() -> new AquaFishingRodItem(Tiers.IRON, new Item.Properties().durability(125)), "iron_fishing_rod");
    public static final DeferredItem<Item> GOLD_FISHING_ROD = registerWithTab(() -> new AquaFishingRodItem(Tiers.GOLD, new Item.Properties().durability(55)), "gold_fishing_rod");
    public static final DeferredItem<Item> DIAMOND_FISHING_ROD = registerWithTab(() -> new AquaFishingRodItem(Tiers.DIAMOND, new Item.Properties().durability(450)), "diamond_fishing_rod");
    public static final DeferredItem<Item> NEPTUNIUM_FISHING_ROD = registerWithTab(() -> new AquaFishingRodItem(AquacultureAPI.MATS.NEPTUNIUM, new Item.Properties().durability(1000)), "neptunium_fishing_rod");
    public static final DeferredItem<Item> WORM = registerWithTab(() -> AquacultureAPI.createBait(20, 1), "worm");
    public static final DeferredItem<Item> FISHING_LINE = registerWithTab(SimpleItem::new, "fishing_line");
    public static final DeferredItem<Item> BOBBER = registerWithTab(SimpleItem::new, "bobber");

    // Neptunium
    public static final DeferredItem<Item> NEPTUNIUM_NUGGET = registerWithTab(SimpleItem::new, "neptunium_nugget");
    public static final DeferredItem<Item> NEPTUNIUM_INGOT = registerWithTab(SimpleItem::new, "neptunium_ingot");
    public static final DeferredItem<Item> NEPTUNIUM_PICKAXE = registerWithTab(() -> new NeptuniumPickaxe(AquacultureAPI.MATS.NEPTUNIUM, 1, -2.8F), "neptunium_pickaxe");
    public static final DeferredItem<Item> NEPTUNIUM_SHOVEL = registerWithTab(() -> new NeptuniumShovel(AquacultureAPI.MATS.NEPTUNIUM, 1.5F, -3.0F), "neptunium_shovel");
    public static final DeferredItem<Item> NEPTUNIUM_AXE = registerWithTab(() -> new AxeItem(AquacultureAPI.MATS.NEPTUNIUM, new Item.Properties().attributes(AxeItem.createAttributes(AquacultureAPI.MATS.NEPTUNIUM, 5.0F, -3.0F))), "neptunium_axe");
    public static final DeferredItem<Item> NEPTUNIUM_HOE = registerWithTab(() -> new NeptuniumHoe(AquacultureAPI.MATS.NEPTUNIUM, -3, 0.2F), "neptunium_hoe");
    public static final DeferredItem<Item> NEPTUNIUM_SWORD = registerWithTab(() -> new SwordItem(AquacultureAPI.MATS.NEPTUNIUM, new Item.Properties().attributes(SwordItem.createAttributes(AquacultureAPI.MATS.NEPTUNIUM, 3, -2.4F))), "neptunium_sword");
    public static final DeferredItem<Item> NEPTUNIUM_BOW = registerWithTab(NeptuniumBow::new, "neptunium_bow");
    public static final DeferredItem<Item> NEPTUNIUM_HELMET = registerWithTab(() -> new NeptuniumArmor(AquaArmorMaterials.NEPTUNIUM, ArmorItem.Type.HELMET).setArmorTexture("neptunium_layer_1"), "neptunium_helmet");
    public static final DeferredItem<Item> NEPTUNIUM_CHESTPLATE = registerWithTab(() -> new NeptuniumArmor(AquaArmorMaterials.NEPTUNIUM, ArmorItem.Type.CHESTPLATE).setArmorTexture("neptunium_layer_1"), "neptunium_chestplate");
    public static final DeferredItem<Item> NEPTUNIUM_LEGGINGS = registerWithTab(() -> new NeptuniumArmor(AquaArmorMaterials.NEPTUNIUM, ArmorItem.Type.LEGGINGS).setArmorTexture("neptunium_layer_2"), "neptunium_leggings");
    public static final DeferredItem<Item> NEPTUNIUM_BOOTS = registerWithTab(() -> new NeptuniumArmor(AquaArmorMaterials.NEPTUNIUM, ArmorItem.Type.BOOTS).setArmorTexture("neptunium_layer_1"), "neptunium_boots");

    // Fillet Knifes
    public static final DeferredItem<Item> WOODEN_FILLET_KNIFE = registerWithTab(() -> new ItemFilletKnife(Tiers.WOOD), "wooden_fillet_knife");
    public static final DeferredItem<Item> STONE_FILLET_KNIFE = registerWithTab(() -> new ItemFilletKnife(Tiers.STONE), "stone_fillet_knife");
    public static final DeferredItem<Item> IRON_FILLET_KNIFE = registerWithTab(() -> new ItemFilletKnife(Tiers.IRON), "iron_fillet_knife");
    public static final DeferredItem<Item> GOLD_FILLET_KNIFE = registerWithTab(() -> new ItemFilletKnife(Tiers.GOLD), "gold_fillet_knife");
    public static final DeferredItem<Item> DIAMOND_FILLET_KNIFE = registerWithTab(() -> new ItemFilletKnife(Tiers.DIAMOND), "diamond_fillet_knife");
    public static final DeferredItem<Item> NEPTUNIUM_FILLET_KNIFE = registerWithTab(() -> new ItemFilletKnife(AquacultureAPI.MATS.NEPTUNIUM, new Item.Properties().component(DataComponents.UNBREAKABLE, new Unbreakable(true))), "neptunium_fillet_knife");

    // Misc
    public static final DeferredItem<Item> DRIFTWOOD = registerWithTab(SimpleItem::new, "driftwood");
    public static final DeferredItem<Item> TIN_CAN = registerWithTab(SimpleItem::new, "tin_can");
    public static final DeferredItem<Item> NESSAGE_IN_A_BOTTLE = registerWithTab(ItemMessageInABottle::new, "message_in_a_bottle");
    public static final DeferredItem<Item> BOX = registerWithTab(() -> new LootBoxItem(AquaLootTables.BOX), "box");
    public static final DeferredItem<Item> LOCKBOX = registerWithTab(() -> new LootBoxItem(AquaLootTables.LOCKBOX), "lockbox");
    public static final DeferredItem<Item> TREASURE_CHEST = registerWithTab(() -> new LootBoxItem(AquaLootTables.TREASURE_CHEST), "treasure_chest");
    public static final DeferredItem<Item> ALGAE = registerWithTab(() -> new Item(new Item.Properties().food(AquaFoods.ALGAE)), "algae");
    public static final DeferredItem<Item> FISH_BONES = registerWithTab(SimpleItem::new, "fish_bones");

    // Food
    public static final DeferredItem<Item> FISH_FILLET = registerWithTab(() -> new Item(new Item.Properties().food(AquaFoods.FISH_RAW)), "fish_fillet_raw");
    public static final DeferredItem<Item> COOKED_FILLET = registerWithTab(() -> new Item(new Item.Properties().food(AquaFoods.FISH_FILLET)), "fish_fillet_cooked");
    public static final DeferredItem<Item> TURTLE_SOUP = registerWithTab(() -> new Item(new Item.Properties().stacksTo(1).food(Foods.MUSHROOM_STEW)), "turtle_soup");
    public static final DeferredItem<Item> SUSHI = registerWithTab(() -> new Item(new Item.Properties().food(AquaFoods.SUSHI)), "sushi");

    // Fish
    public static final DeferredItem<Item> ATLANTIC_COD = FishRegistry.register(() -> new FishItem(FishItem.LARGE_FISH_RAW), "atlantic_cod");
    public static final DeferredItem<Item> BLACKFISH = FishRegistry.register(FishItem::new, "blackfish");
    public static final DeferredItem<Item> PACIFIC_HALIBUT = FishRegistry.register(() -> new FishItem(FishItem.LARGE_FISH_RAW), "pacific_halibut", FishType.HALIBUT);
    public static final DeferredItem<Item> ATLANTIC_HALIBUT = FishRegistry.register(() -> new FishItem(FishItem.LARGE_FISH_RAW), "atlantic_halibut", FishType.HALIBUT);
    public static final DeferredItem<Item> ATLANTIC_HERRING = FishRegistry.register(() -> new FishItem(FishItem.SMALL_FISH_RAW), "atlantic_herring", FishType.SMALL);
    public static final DeferredItem<Item> PINK_SALMON = FishRegistry.register(FishItem::new, "pink_salmon");
    public static final DeferredItem<Item> POLLOCK = FishRegistry.register(FishItem::new, "pollock");
    public static final DeferredItem<Item> RAINBOW_TROUT = FishRegistry.register(FishItem::new, "rainbow_trout");
    public static final DeferredItem<Item> BAYAD = FishRegistry.register(FishItem::new, "bayad", FishType.CATFISH);
    public static final DeferredItem<Item> BOULTI = FishRegistry.register(() -> new FishItem(FishItem.SMALL_FISH_RAW), "boulti", FishType.SMALL);
    public static final DeferredItem<Item> CAPITAINE = FishRegistry.register(() -> new FishItem(FishItem.LARGE_FISH_RAW), "capitaine");
    public static final DeferredItem<Item> SYNODONTIS = FishRegistry.register(() -> new FishItem(FishItem.SMALL_FISH_RAW), "synodontis", FishType.SMALL);
    public static final DeferredItem<Item> SMALLMOUTH_BASS = FishRegistry.register(FishItem::new, "smallmouth_bass");
    public static final DeferredItem<Item> BLUEGILL = FishRegistry.register(() -> new FishItem(FishItem.SMALL_FISH_RAW), "bluegill", FishType.SMALL);
    public static final DeferredItem<Item> BROWN_TROUT = FishRegistry.register(FishItem::new, "brown_trout");
    public static final DeferredItem<Item> CARP = FishRegistry.register(FishItem::new, "carp", FishType.LARGE);
    public static final DeferredItem<Item> CATFISH = FishRegistry.register(() -> new FishItem(FishItem.LARGE_FISH_RAW), "catfish", FishType.CATFISH);
    public static final DeferredItem<Item> GAR = FishRegistry.register(FishItem::new, "gar", FishType.LONGNOSE);
    public static final DeferredItem<Item> MINNOW = FishRegistry.register(() -> AquacultureAPI.createBait(50, 1), "minnow", FishType.SMALL);
    public static final DeferredItem<Item> MUSKELLUNGE = FishRegistry.register(FishItem::new, "muskellunge", FishType.LONGNOSE);
    public static final DeferredItem<Item> PERCH = FishRegistry.register(() -> new FishItem(FishItem.SMALL_FISH_RAW), "perch", FishType.SMALL);
    public static final DeferredItem<Item> ARAPAIMA = FishRegistry.register(() -> new FishItem(FishItem.LARGE_FISH_RAW), "arapaima", FishType.LONGNOSE);
    public static final DeferredItem<Item> PIRANHA = FishRegistry.register(() -> new FishItem(FishItem.SMALL_FISH_RAW), "piranha", FishType.SMALL);
    public static final DeferredItem<Item> TAMBAQUI = FishRegistry.register(FishItem::new, "tambaqui", FishType.LARGE);
    public static final DeferredItem<Item> BROWN_SHROOMA = FishRegistry.register(() -> new FishItem(FishItem.SMALL_FISH_RAW), "brown_shrooma", FishType.SMALL);
    public static final DeferredItem<Item> RED_SHROOMA = FishRegistry.register(() -> new FishItem(FishItem.SMALL_FISH_RAW), "red_shrooma", FishType.SMALL);
    public static final DeferredItem<Item> JELLYFISH = FishRegistry.register(SimpleItem::new, "jellyfish", FishType.JELLYFISH);
    public static final DeferredItem<Item> RED_GROUPER = FishRegistry.register(FishItem::new, "red_grouper");
    public static final DeferredItem<Item> TUNA = FishRegistry.register(() -> new FishItem(FishItem.LARGE_FISH_RAW), "tuna", FishType.LARGE);
    public static final DeferredItem<Item> LEECH = registerWithTab(() -> AquacultureAPI.createBait(35, 1), "leech");
    public static final DeferredItem<Item> GOLDFISH = registerWithTab(SimpleItem::new, "goldfish");
    public static final DeferredItem<Item> BOX_TURTLE = registerWithTab(SimpleItem::new, "box_turtle");
    public static final DeferredItem<Item> ARRAU_TURTLE = registerWithTab(SimpleItem::new, "arrau_turtle");
    public static final DeferredItem<Item> STARSHELL_TURTLE = registerWithTab(SimpleItem::new, "starshell_turtle");

    //Fish Mounting
    public static final DeferredItem<Item> OAK_FISH_MOUNT = AquacultureAPI.registerFishMount("oak_fish_mount");
    public static final DeferredItem<Item> SPRUCE_FISH_MOUNT = AquacultureAPI.registerFishMount("spruce_fish_mount");
    public static final DeferredItem<Item> BIRCH_FISH_MOUNT = AquacultureAPI.registerFishMount("birch_fish_mount");
    public static final DeferredItem<Item> JUNGLE_FISH_MOUNT = AquacultureAPI.registerFishMount("jungle_fish_mount");
    public static final DeferredItem<Item> ACACIA_FISH_MOUNT = AquacultureAPI.registerFishMount("acacia_fish_mount");
    public static final DeferredItem<Item> DARK_OAK_FISH_MOUNT = AquacultureAPI.registerFishMount("dark_oak_fish_mount");

    //Hooks
    public static final DeferredItem<Item> IRON_HOOK = AquacultureAPI.registerHook(Hooks.IRON);
    public static final DeferredItem<Item> GOLD_HOOK = AquacultureAPI.registerHook(Hooks.GOLD);
    public static final DeferredItem<Item> DIAMOND_HOOK = AquacultureAPI.registerHook(Hooks.DIAMOND);
    public static final DeferredItem<Item> LIGHT_HOOK = AquacultureAPI.registerHook(Hooks.LIGHT);
    public static final DeferredItem<Item> HEAVY_HOOK = AquacultureAPI.registerHook(Hooks.HEAVY);
    public static final DeferredItem<Item> DOUBLE_HOOK = AquacultureAPI.registerHook(Hooks.DOUBLE);
    public static final DeferredItem<Item> REDSTONE_HOOK = AquacultureAPI.registerHook(Hooks.REDSTONE);
    public static final DeferredItem<Item> NOTE_HOOK = AquacultureAPI.registerHook(Hooks.NOTE);
    public static final DeferredItem<Item> NETHER_STAR_HOOK = AquacultureAPI.registerHook(Hooks.NETHER_STAR);

    /**
     * Registers an item
     *
     * @param initializer The item initializer
     * @param name        The name to register the item with
     * @return The Item that was registered
     */
    public static DeferredItem<Item> register(@Nonnull Supplier<Item> initializer, @Nonnull String name) {
        return ITEM_DEFERRED.register(name, initializer);
    }

    /**
     * Registers an item & add the item to the Aquaculture creative tab
     *
     * @param initializer The item initializer
     * @param name        The name to register the item with
     * @return The Item that was registered
     */
    public static DeferredItem<Item> registerWithTab(@Nonnull Supplier<Item> initializer, @Nonnull String name) {
        DeferredItem<Item> registryObject = register(initializer, name);
        ITEMS_FOR_TAB_LIST.add(registryObject);
        return registryObject;
    }
}