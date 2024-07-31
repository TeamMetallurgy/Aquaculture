package com.teammetallurgy.aquaculture.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.teammetallurgy.aquaculture.Aquaculture;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;

public class BiomeTagCheck implements LootItemCondition {
    private final BiomeTagPredicate predicate;

    private BiomeTagCheck(BiomeTagPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean test(LootContext context) {
        Vec3 pos = context.getParamOrNull(LootContextParams.ORIGIN);
        return pos != null && this.predicate.test(context.getLevel(), (float) pos.x(), (float) pos.y(), (float) pos.z());
    }

    @Override
    @Nonnull
    public LootItemConditionType getType() {
        return Aquaculture.BIOME_TAG_CHECK.get();
    }

    public static class BiomeTagCheckSerializer implements Serializer<BiomeTagCheck> {

        @Override
        public void serialize(JsonObject json, BiomeTagCheck tagCheck, @Nonnull JsonSerializationContext context) {
            json.add("predicate", tagCheck.predicate.serialize());
        }

        @Override
        @Nonnull
        public BiomeTagCheck deserialize(JsonObject json, @Nonnull JsonDeserializationContext context) {
            return new BiomeTagCheck(BiomeTagPredicate.deserialize(json.get("predicate")));
        }
    }
}