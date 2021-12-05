package com.teammetallurgy.aquaculture.init;

import com.teammetallurgy.aquaculture.Aquaculture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;


@Mod.EventBusSubscriber(modid = Aquaculture.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class AquaSounds {
    public static final SoundEvent TACKLE_BOX_OPEN = registerSound("tackle_box_open");
    public static final SoundEvent TACKLE_BOX_CLOSE = registerSound("tackle_box_close");
    public static final SoundEvent WORM_FARM_EMPTY = registerSound("worm_farm_empty");
    public static final SoundEvent FISH_MOUNT_REMOVED = registerSound("fish_mount_removed");
    public static final SoundEvent FISH_MOUNT_BROKEN = registerSound("fish_mount_broken");
    public static final SoundEvent FISH_MOUNT_PLACED = registerSound("fish_mount_placed");
    public static final SoundEvent FISH_MOUNT_ADD_ITEM = registerSound("fish_mount_add_item");
    public static final SoundEvent BOBBER_BAIT_BREAK = registerSound("bobber_bait");
    public static final SoundEvent BOBBER_LAND_IN_LAVA = registerSound("bobber_land_lava");
    public static final SoundEvent JELLYFISH_FLOP = registerSound("jellyfish_flop");
    public static final SoundEvent FISH_FLOP = registerSound("fish_flop");
    public static final SoundEvent FISH_AMBIENT = registerSound("fish_ambient");
    public static final SoundEvent FISH_DEATH = registerSound("fish_death");
    public static final SoundEvent FISH_HURT = registerSound("fish_hurt");
    public static final SoundEvent JELLYFISH_COLLIDE = registerSound("jellyfish_collide");
    public static final SoundEvent BOTTLE_OPEN = registerSound("bottle_open");
    public static final SoundEvent BOBBER_NOTE = registerSound("bobber_note_catch");

    private static SoundEvent registerSound(String name) {
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
        registry.register(JELLYFISH_COLLIDE);
        registry.register(BOTTLE_OPEN);
        registry.register(BOBBER_NOTE);
    }
}
