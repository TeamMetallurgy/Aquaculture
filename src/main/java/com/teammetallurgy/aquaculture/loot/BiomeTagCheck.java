package com.teammetallurgy.aquaculture.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;
import java.util.Optional;

public record BiomeTagCheck(Optional<BiomeTagPredicate> predicate) implements LootItemCondition {
    public static final MapCodec<BiomeTagCheck> CODEC = RecordCodecBuilder.mapCodec(rb -> rb.group(
            BiomeTagPredicate.CODEC.optionalFieldOf("predicate").forGetter(BiomeTagCheck::predicate)
    ).apply(rb, BiomeTagCheck::new));
    public static final LootItemConditionType BIOME_TAG_CHECK = new LootItemConditionType(CODEC);

    @Override
    public boolean test(LootContext context) {
        Vec3 pos = context.getParamOrNull(LootContextParams.ORIGIN);
        return pos != null && this.predicate.isPresent() && this.predicate.get().matches(context.getLevel(), pos.x(), pos.y(), pos.z());
    }

    @Override
    @Nonnull
    public LootItemConditionType getType() {
        return BIOME_TAG_CHECK;
    }
}