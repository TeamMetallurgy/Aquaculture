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
    public static final SoundEvent TACKLE_BOX_OPEN = registersound("tackle_box_open");
    public static final SoundEvent TACKLE_BOX_CLOSE = registersound("tackle_box_close");
    public static final SoundEvent WORM_FARM_EMPTY = registersound("worm_farm_empty");
    public static final SoundEvent FISH_MOUNT_REMOVED = registersound("fish_mount_removed");
    public static final SoundEvent FISH_MOUNT_BROKEN = registersound("fish_mount_broken");
    public static final SoundEvent FISH_MOUNT_PLACED = registersound("fish_mount_placed");
    public static final SoundEvent FISH_MOUNT_ADD_ITEM = registersound("fish_mount_add_item");
    public static final SoundEvent BOBBER_BAIT_BREAK = registersound("bobber_bait");
    public static final SoundEvent BOBBER_LAND_IN_LAVA = registersound("bobber_land_lava");
    public static final SoundEvent JELLYFISH_FLOP = registersound("jellyfish_flop");
    public static final SoundEvent FISH_FLOP = registersound("fish_flop");
    public static final SoundEvent FISH_AMBIENT = registersound("fish_ambient");
    public static final SoundEvent FISH_DEATH = registersound("fish_death");
    public static final SoundEvent FISH_HURT = registersound("fish_hurt");
    public static final SoundEvent FISH_COLLIDE = registersound("fish_collide");
    public static final SoundEvent BOTTLE_OPEN = registersound("bottle_open");
    public static final SoundEvent BOBBER_NOTE = registersound("bobber_note_catch");

    private static SoundEvent makeSoundEvent(String name) {
        ResourceLocation location = new ResourceLocation("aquaculture", name);
        return (SoundEvent)(new SoundEvent(location)).setRegistryName(location);
    }

    @SubscribeEvent
    public static void registerSounds(Register<SoundEvent> event) {
        IForgeRegistry<SoundEvent> registry = event.getRegistry();
        registry.register(TACKLE_BOX_OPEN);
        registry.register(TACKLE_BOX_CLOSE);
        registry.register(WORM_FARM_EMPTY);
        registry.register(FISH_MOUNT_REMOVED);
        registry.register(FISH_MOUNT_BROKEN);
        registry.register(FISH_MOUNT_PLACED);
        registry.register(FISH_MOUNT_ADD_ITEM);
        registry.register(BOBBER_BAIT_BREAK);
        registry.register(BOBBER_LAND_IN_LAVA);
        registry.register(JELLYFISH_FLOP);
        registry.register(FISH_FLOP);
        registry.register(FISH_AMBIENT);
        registry.register(FISH_DEATH);
        registry.register(FISH_HURT);
        registry.register(FISH_COLLIDE);
        registry.register(BOTTLE_OPEN);
        registry.register(BOBBER_NOTE);
    }
}
