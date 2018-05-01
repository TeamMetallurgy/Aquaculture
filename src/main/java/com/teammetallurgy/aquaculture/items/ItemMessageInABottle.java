package com.teammetallurgy.aquaculture.items;

import com.teammetallurgy.aquaculture.items.meta.MetaItem;
import com.teammetallurgy.aquaculture.items.meta.SubItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

public class ItemMessageInABottle extends SubItem {
    private Random rand = new Random();

    public ItemMessageInABottle(MetaItem metaItem) {
        super(metaItem);
    }

    @Override
    @Nonnull
    public ItemStack onItemRightClick(@Nonnull ItemStack stack, World world, EntityPlayer player) {
        if (world.isRemote)
            return stack;

        world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 0.5F, 0.4F / (rand.nextFloat() * 0.4F + 0.8F));

        int messageRoll = rand.nextInt(29) + 1;

        String message = "";
        if (messageRoll == 0) {
            message = "ERROR! Fish Escaping!";
        } else {
            message = "aquaculture.message" + messageRoll;
        }

        if (player instanceof EntityPlayerMP) {
            TextComponentTranslation chatMessage = new TextComponentTranslation(message);
            player.sendMessage(chatMessage);
        }

        stack.shrink(1);
        return stack;
    }
}