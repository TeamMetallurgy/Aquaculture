package com.teammetallurgy.aquaculture.item.neptunium;

import com.teammetallurgy.aquaculture.Aquaculture;
import net.minecraft.core.component.BlockTransformer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;

public class NeptuniumHoe extends Item {
    public static final ResourceKey<BlockTransformer> NEPTUNIUM_HOE_TRANSFORMER = ResourceKey.create(Registries.BLOCK_TRANSFORMER, Identifier.fromNamespaceAndPath(Aquaculture.MOD_ID, "neptunium_hoe"));

    public NeptuniumHoe(ToolMaterial toolMaterial, int damage, float speed, Properties properties) {
        super(properties.hoe(toolMaterial, damage, speed).delayedHolderComponent(DataComponents.BLOCK_TRANSFORMER, NEPTUNIUM_HOE_TRANSFORMER));
    }
}
