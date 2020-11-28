package com.teammetallurgy.aquaculture.item;

import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.misc.AquacultureSounds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

public class ItemMessageInABottle extends Item {
    private Random rand = new Random();

    public ItemMessageInABottle() {
        super(new Item.Properties().group(Aquaculture.GROUP));
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
        ItemStack heldStack = player.getHeldItem(hand);
        world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), AquacultureSounds.BOTTLE_OPEN , SoundCategory.PLAYERS, 0.5F, 0.4F / (rand.nextFloat() * 0.4F + 0.8F));

        int messageRoll = rand.nextInt(29) + 1;

        String message;
        if (messageRoll == 0) {
            message = "ERROR! Fish Escaping!";
        } else {
            message = "aquaculture.message" + messageRoll;
        }

        if (player instanceof ServerPlayerEntity) {
            TranslationTextComponent chatMessage = new TranslationTextComponent(message);
            player.sendMessage(chatMessage, Util.DUMMY_UUID);
        }

        heldStack.shrink(1);

        return super.onItemRightClick(world, player, hand);
    }
}
