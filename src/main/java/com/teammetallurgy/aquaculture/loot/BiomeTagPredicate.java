package com.teammetallurgy.aquaculture.loot;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public record BiomeTagPredicate(Optional<PositionPredicate> position, Optional<List<TagKey<Biome>>> include, Optional<List<TagKey<Biome>>> exclude, Optional<Boolean> and) {
    public static final Codec<BiomeTagPredicate> CODEC = RecordCodecBuilder.create(rb -> rb.group(
                            PositionPredicate.CODEC.optionalFieldOf("position").forGetter(BiomeTagPredicate::position),
                            Codec.list(TagKey.codec(Registries.BIOME)).optionalFieldOf("include").forGetter(BiomeTagPredicate::include),
                            Codec.list(TagKey.codec(Registries.BIOME)).optionalFieldOf("exclude").forGetter(BiomeTagPredicate::exclude),
                            Codec.BOOL.optionalFieldOf("and").forGetter(BiomeTagPredicate::and))
                    .apply(rb, BiomeTagPredicate::new)
    );
    private static final HashMap<CheckType, Set<Holder<Biome>>> CACHE = new HashMap<>();
    public static final List<TagKey<Biome>> INVALID_TYPES = Arrays.asList(BiomeTags.IS_NETHER, BiomeTags.IS_END, Tags.Biomes.IS_VOID);

    public boolean matches(ServerLevel serverLevel, double x, double y, double z) {
        if (this.position.isPresent() && !this.position.get().matches(x, y, z)) {
            return false;
        } else {
            BlockPos pos = BlockPos.containing(x, y, z);
            if (serverLevel.isLoaded(pos)) {
                Biome biome = serverLevel.getBiome(pos).value();
                Registry<Biome> biomeRegistry = serverLevel.registryAccess().lookupOrThrow(Registries.BIOME);
                Optional<ResourceKey<Biome>> resourceKey = biomeRegistry.getResourceKey(biome);

                if (resourceKey.isPresent()) {
                    Optional<Holder.Reference<Biome>> biomeHolder = biomeRegistry.get(resourceKey.get());
                    if (biomeHolder.isPresent()) {
                        CheckType checkType = CheckType.getOrCreate(this.include.map(Lists::newArrayList).orElseGet(ArrayList::new), this.exclude.map(Lists::newArrayList).orElseGet(ArrayList::new), this.and.orElse(false));

                        Set<Holder<Biome>> validBiomes = CACHE.get(checkType);
                        if (validBiomes == null) {
                            validBiomes = getValidBiomes(serverLevel, checkType);
                            CACHE.put(checkType, validBiomes);
                        }
                        return validBiomes.contains(biomeHolder.get());
                    }
                }
            }
        }
        return false;
    }

    public static Set<Holder<Biome>> getValidBiomes(ServerLevel serverLevel, CheckType checkType) {
        return getValidBiomes(serverLevel, checkType.getInclude(), checkType.getExclude(), checkType.isAnd());
    }

    public static Set<Holder<Biome>> getValidBiomes(ServerLevel serverLevel, List<TagKey<Biome>> includeList, List<TagKey<Biome>> excludeList, boolean and) {
        Set<Holder<Biome>> biomes = new HashSet<>();
        Optional<? extends Registry<Biome>> optionalBiomeRegistry = serverLevel.registryAccess().lookup(Registries.BIOME);
        if (optionalBiomeRegistry.isPresent()) {
            Registry<Biome> biomeRegistry = optionalBiomeRegistry.get();

            if (includeList.isEmpty() && !excludeList.isEmpty()) { //Add all tags, when only excluding biomes
                includeList.addAll(biomeRegistry.getTags().map(HolderSet.Named::key).collect(Collectors.toSet()));
                excludeList.addAll(INVALID_TYPES);
            }

            if (and) {
                for (TagKey<Biome> tagKey : includeList) {
                    getBiomeFromTag(biomeRegistry, tagKey).forEach(a -> {
                        List<TagKey<Biome>> tags = a.tags().collect(Collectors.toList());
                        int beforeTagCount = tags.size();
                        tags.removeAll(includeList);
                        int afterTagCount = tags.size();

                        if (beforeTagCount - afterTagCount == includeList.size()) {
                            biomes.add(a);
                        }
                    });
                }
            } else {
                for (TagKey<Biome> tagKey : includeList) {
                    getBiomeFromTag(biomeRegistry, tagKey).forEach(biomes::add);
                }
            }

            if (!excludeList.isEmpty()) {
                for (TagKey<Biome> tagKey : excludeList) {
                    getBiomeFromTag(biomeRegistry, tagKey).forEach(biomes::remove);
                }
            }
        }
        return biomes;
    }

    public static Iterable<Holder<Biome>> getBiomeFromTag(Registry<Biome> biomeRegistry, TagKey<Biome> tagKey) {
        return biomeRegistry.getTagOrEmpty(tagKey);
    }

    public static record PositionPredicate(MinMaxBounds.Doubles x, MinMaxBounds.Doubles y, MinMaxBounds.Doubles z) {
        public static final Codec<PositionPredicate> CODEC = RecordCodecBuilder.create(
                p_299107_ -> p_299107_.group(
                                MinMaxBounds.Doubles.CODEC.optionalFieldOf("x", MinMaxBounds.Doubles.ANY).forGetter(PositionPredicate::x),
                                MinMaxBounds.Doubles.CODEC.optionalFieldOf("y", MinMaxBounds.Doubles.ANY).forGetter(PositionPredicate::y),
                                MinMaxBounds.Doubles.CODEC.optionalFieldOf("z", MinMaxBounds.Doubles.ANY).forGetter(PositionPredicate::z)
                        )
                        .apply(p_299107_, PositionPredicate::new)
        );

        static Optional<PositionPredicate> of(MinMaxBounds.Doubles p_298771_, MinMaxBounds.Doubles p_298418_, MinMaxBounds.Doubles p_299133_) {
            return p_298771_.isAny() && p_298418_.isAny() && p_299133_.isAny()
                    ? Optional.empty()
                    : Optional.of(new PositionPredicate(p_298771_, p_298418_, p_299133_));
        }

        public boolean matches(double p_298782_, double p_299123_, double p_298955_) {
            return this.x.matches(p_298782_) && this.y.matches(p_299123_) && this.z.matches(p_298955_);
        }
    }

    public static class CheckType {
        private static final Map<Integer, CheckType> BY_NAME = new TreeMap<>();
        private final List<TagKey<Biome>> include;
        private final List<TagKey<Biome>> exclude;
        private final boolean and;

        private CheckType(@Nullable List<TagKey<Biome>> include,  List<TagKey<Biome>> exclude, boolean and) {
            this.include = include;
            this.exclude = exclude;
            this.and = and;

            BY_NAME.put(this.hashCode(), this);
        }

        public List<TagKey<Biome>> getInclude() {
            return this.include;
        }

        public List<TagKey<Biome>> getExclude() {
            return this.exclude;
        }

        public boolean isAnd() {
            return this.and;
        }

        public static CheckType getOrCreate(List<TagKey<Biome>> include, List<TagKey<Biome>> exclude, boolean and) {
            CheckType checkType = BY_NAME.get(Objects.hash(include, exclude, and));
            if (checkType == null) {
                checkType = new CheckType(include, exclude, and);
            }
            return checkType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CheckType checkType = (CheckType) o;
            return this.and == checkType.and && Objects.equals(this.include, checkType.include) && Objects.equals(this.exclude, checkType.exclude);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.include, this.exclude, this.and);
        }
    }
}