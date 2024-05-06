package com.teammetallurgy.aquaculture.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.misc.AquaConfig;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class AquaBiomeModifiers {
    public static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS_DEFERRED = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Aquaculture.MOD_ID);

    public record MobSpawnBiomeModifier(HolderSet<Biome> includeList, HolderSet<Biome> excludeList,
                                        MobSpawnSettings.SpawnerData spawn) implements BiomeModifier {
        private static final RegistryObject<Codec<? extends BiomeModifier>> SERIALIZER = RegistryObject.create(new ResourceLocation(Aquaculture.MOD_ID, "mob_spawn_serializer"), ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Aquaculture.MOD_ID);

        @Override
        public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
            if (phase == Phase.ADD && this.includeList.contains(biome) && !this.excludeList.contains(biome)) {
                builder.getMobSpawnSettings().addSpawn(this.spawn.type.getCategory(), this.spawn);
            }
        }

        @Override
        public Codec<? extends BiomeModifier> codec() {
            return SERIALIZER.get();
        }

        public static Codec<MobSpawnBiomeModifier> makeCodec() {
            return RecordCodecBuilder.create(builder -> builder.group(
                    Biome.LIST_CODEC.fieldOf("includeBiomes").forGetter(MobSpawnBiomeModifier::includeList),
                    Biome.LIST_CODEC.fieldOf("excludeBiomes").forGetter(MobSpawnBiomeModifier::excludeList),
                    MobSpawnSettings.SpawnerData.CODEC.fieldOf("spawn").forGetter(MobSpawnBiomeModifier::spawn)
            ).apply(builder, MobSpawnBiomeModifier::new));
        }
    }

    public record FishSpawnBiomeModifier(List<HolderSet<Biome>> includeBiomes, List<HolderSet<Biome>> excludeBiomes,
                                         boolean and, MobSpawnSettings.SpawnerData spawn) implements BiomeModifier {
        private static final RegistryObject<Codec<? extends BiomeModifier>> SERIALIZER = RegistryObject.create(new ResourceLocation(Aquaculture.MOD_ID, "fish_spawn_serializer"), ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Aquaculture.MOD_ID);

        @Override
        public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
            if (phase == Phase.ADD) {
                if (biome.tags().noneMatch(BiomeTagPredicate.INVALID_TYPES::contains)) {
                    if (this.includeBiomes.stream().findAny().get().stream().findAny().isEmpty() && !this.excludeBiomes.isEmpty()) {
                        for (HolderSet<Biome> exclude : this.excludeBiomes) {
                            if (exclude.contains(biome)) {
                                return;
                            }
                        }
                        debugOutput(biome, "Exclude only. Valid biome included");
                        builder.getMobSpawnSettings().addSpawn(this.spawn.type.getCategory(), this.spawn);
                    } else if (this.and) {
                        for (HolderSet<Biome> include : this.includeBiomes) {
                            if (!include.contains(biome)) return;
                        }
                        debugOutput(biome, "And Include");
                        builder.getMobSpawnSettings().addSpawn(this.spawn.type.getCategory(), this.spawn);
                    } else {
                        for (HolderSet<Biome> exclude : this.excludeBiomes) {
                            if (exclude.contains(biome)) {
                                return;
                            }
                        }
                        for (HolderSet<Biome> include : this.includeBiomes) {
                            if (include.contains(biome)) {
                                debugOutput(biome, "Normal");
                                builder.getMobSpawnSettings().addSpawn(this.spawn.type.getCategory(), this.spawn);
                            }
                        }
                    }
                }
            }
        }

        private void debugOutput(Holder<Biome> biomeHolder, String s) {
            if (AquaConfig.BASIC_OPTIONS.debugMode.get()) {
                Aquaculture.LOG.info("Fish: " + ForgeRegistries.ENTITY_TYPES.getKey(spawn.type) + " | " + s + ": " + biomeHolder.unwrapKey().get().location());
            }
        }

        @Override
        public Codec<? extends BiomeModifier> codec() {
            return SERIALIZER.get();
        }

        public static Codec<FishSpawnBiomeModifier> makeCodec() {
            return RecordCodecBuilder.create(builder -> builder.group(
                    Biome.LIST_CODEC.listOf().fieldOf("includeBiomes").forGetter(FishSpawnBiomeModifier::includeBiomes),
                    Biome.LIST_CODEC.listOf().fieldOf("excludeBiomes").forGetter(FishSpawnBiomeModifier::excludeBiomes),
                    Codec.BOOL.fieldOf("and").forGetter(FishSpawnBiomeModifier::and),
                    MobSpawnSettings.SpawnerData.CODEC.fieldOf("spawn").forGetter(FishSpawnBiomeModifier::spawn)
            ).apply(builder, FishSpawnBiomeModifier::new));
        }
    }
}