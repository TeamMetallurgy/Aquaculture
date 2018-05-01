package com.teammetallurgy.aquaculture.handlers;

import com.google.common.base.Preconditions;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.internal.FMLMessage.EntitySpawnMessage;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry.EntityRegistration;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.function.Function;

public class AquacultureEntitySpawnHandler {

    @SideOnly(Side.CLIENT)
    public static void init() {
        EntityRegistration customFishHookRegistration = EntityRegistry.instance().lookupModSpawn(EntityCustomFishHook.class, false);
        Function<EntitySpawnMessage, Entity> fishHookSpawnHandler = input -> {
            int anglerId = 0;
            double posX = 0;
            double posY = 0;
            double posZ = 0;
            try {
                anglerId = ReflectionHelper.findField(EntitySpawnMessage.class, "throwerId").getInt(input);
                posX = ReflectionHelper.findField(EntitySpawnMessage.class, "rawX").getDouble(input);
                posY = ReflectionHelper.findField(EntitySpawnMessage.class, "rawY").getDouble(input);
                posZ = ReflectionHelper.findField(EntitySpawnMessage.class, "rawZ").getDouble(input);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
            WorldClient world = FMLClientHandler.instance().getWorldClient();

            Entity angler = world.getEntityByID(anglerId);
            if (!(angler instanceof EntityPlayer)) {
                return null;
            }

            return new EntityCustomFishHook(world, posX, posY, posZ, (EntityPlayer) angler);
        };
        Preconditions.checkNotNull(customFishHookRegistration, "fishHookRegistration");
        customFishHookRegistration.setCustomSpawning(fishHookSpawnHandler, false);
    }
}