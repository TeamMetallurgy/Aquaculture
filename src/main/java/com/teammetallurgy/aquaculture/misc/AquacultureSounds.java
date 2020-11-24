package com.teammetallurgy.aquaculture.misc;

import com.teammetallurgy.aquaculture.Aquaculture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;


@Mod.EventBusSubscriber(modid = Aquaculture.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class AquacultureSounds{
    public static final SoundEvent tackleBoxOpen = registersound("tackle_box_open");
    public static final SoundEvent tacbleBoxClose = registersound("tackle_box_close");
    public static final SoundEvent wormFarmEmpty = registersound("worm_farm_empty");
    public static final SoundEvent fishMountRemoved = registersound("fish_mount_removed");
    public static final SoundEvent fismMountBroken = registersound("fish_mount_broken");
    public static final SoundEvent fishMountFishPlaced = registersound("fish_mount_placed");
    public static final SoundEvent bobberBait = registersound("bobber_bait");
    public static final SoundEvent jellyfishFlop = registersound("jellyfish_flop");
    public static final SoundEvent fishFlop = registersound("fish_flop");
    public static final SoundEvent fishAmbient = registersound("fish_ambient");
    public static final SoundEvent fishDeath = registersound("fish_death");
    public static final SoundEvent fishHurt = registersound("fish_hurt");
    public static final SoundEvent bottleOpened = registersound("bottle_open");

    private static SoundEvent makeSoundEvent(String name) {
        ResourceLocation location = new ResourceLocation("aquaculture", name);
        return (SoundEvent)(new SoundEvent(location)).setRegistryName(location);
    }

    @SubscribeEvent
    public static void registerSounds(Register<SoundEvent> event) {
        IForgeRegistry<SoundEvent> registry = event.getRegistry();
        registry.register(tackleBoxOpen);
        registry.register(tacbleBoxClose);
        registry.register(wormFarmEmpty);
        registry.register(fishMountRemoved);
        registry.register(fismMountBroken);
        registry.register(fishMountFishPlaced);
        registry.register(bobberBait);
        registry.register(jellyfishFlop);
        registry.register(fishFlop);
        registry.register(fishAmbient);
        registry.register(fishDeath);
        registry.register(fishHurt);
        registry.register(bottleOpened);
    }
}
