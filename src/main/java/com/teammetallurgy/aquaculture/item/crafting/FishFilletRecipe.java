package com.teammetallurgy.aquaculture.item.crafting;

import com.teammetallurgy.aquaculture.api.AquacultureAPI;
import com.teammetallurgy.aquaculture.api.fish.FishData;
import com.teammetallurgy.aquaculture.init.AquaDataComponents;
import com.teammetallurgy.aquaculture.init.AquaItems;
import com.teammetallurgy.aquaculture.init.AquaRecipeSerializers;
import com.teammetallurgy.aquaculture.misc.AquaConfig;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class FishFilletRecipe extends CustomRecipe {

    public FishFilletRecipe(CraftingBookCategory craftingBookCategory) {
        super(craftingBookCategory);
    }

    @Override
    public boolean matches(@Nonnull CraftingInput craftingInventory, @Nonnull Level world) {
        ItemStack stack = ItemStack.EMPTY;
        List<ItemStack> list = new ArrayList<>();

        for (int i = 0; i < craftingInventory.size(); ++i) {
            ItemStack slotStack = craftingInventory.getItem(i);
            if (!slotStack.isEmpty()) {
                if (AquacultureAPI.FISH_DATA.hasFilletAmount(slotStack.getItem())) {
                    if (!stack.isEmpty()) {
                        return false;
                    }
                    stack = slotStack;
                } else {
                    if (!(slotStack.is(AquacultureAPI.Tags.KNIVES) && (slotStack.isDamageableItem() || isKnifeNeptunium(slotStack.getItem())) && slotStack.getItem() instanceof TieredItem)) {
                        return false;
                    }
                    list.add(slotStack);
                }
            }
        }
        return !stack.isEmpty() && !list.isEmpty();
    }

    @Override
    @Nonnull
    public ItemStack assemble(@Nonnull CraftingInput craftingInventory, @Nonnull HolderLookup.Provider provider) {
        ItemStack fish = ItemStack.EMPTY;
        Item knife = null;

        for (int i = 0; i < craftingInventory.size(); ++i) {
            ItemStack stackSlot = craftingInventory.getItem(i);
            if (!stackSlot.isEmpty()) {
                Item item = stackSlot.getItem();
                if (AquacultureAPI.FISH_DATA.hasFilletAmount(item)) {
                    if (!fish.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    fish = stackSlot.copy();
                } else {
                    if (!(stackSlot.is(AquacultureAPI.Tags.KNIVES))) {
                        return ItemStack.EMPTY;
                    }
                    knife = item;
                }
            }
        }
        if (!fish.isEmpty() && knife != null) {
            int filletAmount = AquacultureAPI.FISH_DATA.getFilletAmount(fish.getItem());
            Float fishWeight = fish.get(AquaDataComponents.FISH_WEIGHT.get());
            if (AquaConfig.BASIC_OPTIONS.randomWeight.get() && fish.has(AquaDataComponents.FISH_WEIGHT) && fishWeight != null) {
                filletAmount = FishData.getFilletAmountFromWeight(fishWeight);
            }
            if (isKnifeNeptunium(knife)) {
                filletAmount += filletAmount * (25.0F / 100.0F);
            }
            return new ItemStack(AquaItems.FISH_FILLET.get(), filletAmount);
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    @Nonnull
    public NonNullList<ItemStack> getRemainingItems(CraftingInput craftingInventory) {
        NonNullList<ItemStack> list = NonNullList.withSize(craftingInventory.size(), ItemStack.EMPTY);
        for (int i = 0; i < list.size(); ++i) {
            ItemStack stack = craftingInventory.getItem(i);
            if (stack.is(AquacultureAPI.Tags.KNIVES)) {
                ItemStack knife = stack.copy();
                if (!isKnifeNeptunium(knife.getItem())) {
                    MinecraftServer server = ServerLifecycleHooks.getCurrentServer(); //Workaround
                    if (server != null) {
                        knife.hurtAndBreak(1, server.overworld(), null, item -> {
                            knife.shrink(1);
                        });
                    }
                }
                list.set(i, knife);
            }
        }
        return list;
    }

    public static boolean isKnifeNeptunium(@Nonnull Item knife) {
        return knife instanceof TieredItem && ((TieredItem) knife).getTier() == AquacultureAPI.MATS.NEPTUNIUM;
    }

    @Override
    @Nonnull
    public RecipeSerializer<?> getSerializer() {
        return AquaRecipeSerializers.FISH_FILLET_SERIALIZER.get();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }
}