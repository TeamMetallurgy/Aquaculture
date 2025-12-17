package com.teammetallurgy.aquaculture.loot;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.misc.AquaConfig;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.MobSpawnSettingsBuilder;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Function;

public class AquaBiomeModifiers {
    public static final DeferredRegister<MapCodec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS_DEFERRED = DeferredRegister.create(NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Aquaculture.MOD_ID);

    public record MobSpawnBiomeModifier(HolderSet<Biome> includeList, HolderSet<Biome> excludeList, WeightedList<MobSpawnSettings.SpawnerData> spawners) implements BiomeModifier {

        @Override
        public void modify(@Nonnull Holder<Biome> biome, @Nonnull Phase phase, @Nonnull ModifiableBiomeInfo.BiomeInfo.Builder builder) {
            if (phase == Phase.ADD && this.includeList.contains(biome) && !this.excludeList.contains(biome)) {
                MobSpawnSettingsBuilder spawns = builder.getMobSpawnSettings();
                for (Weighted<MobSpawnSettings.SpawnerData> spawner : this.spawners.unwrap()) {
                    EntityType<?> type = spawner.value().type();
                    spawns.addSpawn(type.getCategory(), spawner.weight(), spawner.value());
                }
            }
        }

        @Override
        @Nonnull
        public MapCodec<? extends BiomeModifier> codec() {
            return makeCodec();
        }

        public static MapCodec<MobSpawnBiomeModifier> makeCodec() {
            return RecordCodecBuilder.mapCodec(builder -> builder.group(
                    Biome.LIST_CODEC.fieldOf("includeBiomes").forGetter(MobSpawnBiomeModifier::includeList),
                    Biome.LIST_CODEC.fieldOf("excludeBiomes").forGetter(MobSpawnBiomeModifier::excludeList),
                    Codec.either(WeightedList.codec(MobSpawnSettings.SpawnerData.CODEC), Weighted.codec(MobSpawnSettings.SpawnerData.CODEC)).xmap(
                            either -> either.map(Function.identity(), WeightedList::<MobSpawnSettings.SpawnerData>of), // convert list/singleton to list when decoding
                            list -> list.unwrap().size() == 1 ? Either.right(list.unwrap().get(0)) : Either.left(list) // convert list to singleton/list when encoding
                    ).fieldOf("spawn").forGetter(MobSpawnBiomeModifier::spawners)
            ).apply(builder, MobSpawnBiomeModifier::new));
        }
    }

    public record FishSpawnBiomeModifier(List<HolderSet<Biome>> includeBiomes, List<HolderSet<Biome>> excludeBiomes, boolean and, WeightedList<MobSpawnSettings.SpawnerData> spawners) implements BiomeModifier {

        @Override
        public void modify(@Nonnull Holder<Biome> biome, @Nonnull Phase phase, @Nonnull ModifiableBiomeInfo.BiomeInfo.Builder builder) {
            if (phase == Phase.ADD) {
                if (biome.tags().noneMatch(BiomeTagPredicate.INVALID_TYPES::contains)) {
                    MobSpawnSettingsBuilder spawns = builder.getMobSpawnSettings();
                    for (Weighted<MobSpawnSettings.SpawnerData> spawner : this.spawners.unwrap()) {
                        MobCategory category = spawner.value().type().getCategory();
                        if (this.includeBiomes.stream().findAny().get().stream().findAny().isEmpty() && !this.excludeBiomes.isEmpty()) {
                            for (HolderSet<Biome> exclude : this.excludeBiomes) {
                                if (exclude.contains(biome)) {
                                    return;
                                }
                            }
                            debugOutput(biome, "Exclude only. Valid biome included");
                            spawns.addSpawn(category, spawner.weight(), spawner.value());
                        } else if (this.and) {
                            for (HolderSet<Biome> include : this.includeBiomes) {
                                if (!include.contains(biome)) return;
                            }
                            debugOutput(biome, "And Include");
                            spawns.addSpawn(category, spawner.weight(), spawner.value());
                        } else {
                            for (HolderSet<Biome> exclude : this.excludeBiomes) {
                                if (exclude.contains(biome)) {
                                    return;
                                }
                            }
                            for (HolderSet<Biome> include : this.includeBiomes) {
                                if (include.contains(biome)) {
                                    debugOutput(biome, "Normal");
                                    spawns.addSpawn(category, spawner.weight(), spawner.value());
                                }
                            }
                        }
                    }
                }
            }
        }

        private void debugOutput(Holder<Biome> biomeHolder, String s) {
            if (AquaConfig.BASIC_OPTIONS.debugMode.get()) {
                for (Weighted<MobSpawnSettings.SpawnerData> spawner : this.spawners.unwrap()) {
                    Aquaculture.LOG.info("Fish: " + BuiltInRegistries.ENTITY_TYPE.getKey(spawner.value().type()) + " | " + s + ": " + biomeHolder.unwrapKey().get().identifier());
                }
            }
        }

        @Override
        @Nonnull
        public MapCodec<? extends BiomeModifier> codec() {
            return makeCodec();
        }

        public static MapCodec<FishSpawnBiomeModifier> makeCodec() {
            return RecordCodecBuilder.mapCodec(builder -> builder.group(
                    Biome.LIST_CODEC.listOf().fieldOf("includeBiomes").forGetter(FishSpawnBiomeModifier::includeBiomes),
                    Biome.LIST_CODEC.listOf().fieldOf("excludeBiomes").forGetter(FishSpawnBiomeModifier::excludeBiomes),
                    Codec.BOOL.fieldOf("and").forGetter(FishSpawnBiomeModifier::and),
                    Codec.either(WeightedList.codec(MobSpawnSettings.SpawnerData.CODEC), Weighted.codec(MobSpawnSettings.SpawnerData.CODEC)).xmap(
                            either -> either.map(Function.identity(), WeightedList::<MobSpawnSettings.SpawnerData>of), // convert list/singleton to list when decoding
                            list -> list.unwrap().size() == 1 ? Either.right(list.unwrap().get(0)) : Either.left(list) // convert list to singleton/list when encoding
                    ).fieldOf("spawn").forGetter(FishSpawnBiomeModifier::spawners)
            ).apply(builder, FishSpawnBiomeModifier::new));
        }
    }
}