package com.teammetallurgy.aquaculture.init;

import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.block.FarmlandMoistBlock;
import com.teammetallurgy.aquaculture.block.NeptunesBountyBlock;
import com.teammetallurgy.aquaculture.block.TackleBoxBlock;
import com.teammetallurgy.aquaculture.block.WormFarmBlock;
import com.teammetallurgy.aquaculture.item.BlockItemWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Function;
import java.util.function.Supplier;

public class AquaBlocks {
    public static final DeferredRegister.Blocks BLOCK_DEFERRED = DeferredRegister.createBlocks(Aquaculture.MOD_ID);
    public static final DeferredBlock<Block> FARMLAND = register(FarmlandMoistBlock::new, "farmland", null);
    public static final DeferredBlock<Block> NEPTUNIUM_BLOCK = register(p -> new Block(p.mapColor(MapColor.COLOR_CYAN).strength(5.0F, 6.0F).sound(SoundType.METAL)), "neptunium_block");
    public static final DeferredBlock<Block> NEPTUNES_BOUNTY = registerWithRenderer(p -> new NeptunesBountyBlock(AquaBlockEntities.NEPTUNES_BOUNTY::get, p), "neptunes_bounty", new Item.Properties());
    public static final DeferredBlock<Block> TACKLE_BOX = registerWithRenderer(TackleBoxBlock::new, "tackle_box", new Item.Properties());
    public static final DeferredBlock<Block> WORM_FARM = register(WormFarmBlock::new, "worm_farm");

    /**
     * Same as {@link AquaBlocks#register(Function, String, Item.Properties)}, but have group set by default
     */
    public static DeferredBlock<Block> register(Function<BlockBehaviour.Properties, ? extends Block> function, @Nonnull String name) {
        return register(function, name, new Item.Properties());
    }

    /**
     * Registers a block with a basic BlockItem
     *
     * @param function The block to register
     * @param name     The name to register the block with
     * @return The Block that was registered
     */
    public static DeferredBlock<Block> register(Function<BlockBehaviour.Properties, ? extends Block> function, @Nonnull String name, @Nullable Item.Properties properties) {
        DeferredBlock<Block> block = BLOCK_DEFERRED.registerBlock(name, function);

        if (properties == null) {
            AquaItems.register(p -> new BlockItem(block.get(), p), name);
        } else {
            AquaItems.registerWithTab(p -> new BlockItem(block.get(), properties), name);
        }

        return block;
    }

    /**
     * Registers a block with a BlockItemWithoutLevelRenderer
     *
     * @param function The block to register
     * @param name     The name to register the block with
     * @return The Block that was registered
     */
    public static DeferredBlock<Block> registerWithRenderer(Function<BlockBehaviour.Properties, ? extends Block> function, @Nonnull String name, @Nullable Item.Properties itemProperties) {
        DeferredBlock<Block> block = BLOCK_DEFERRED.registerBlock(name, function);

        if (itemProperties == null) {
            AquaItems.register(p -> new BlockItemWithoutLevelRenderer(block.get(), p), name);
        } else {
            AquaItems.registerWithTab(p -> new BlockItemWithoutLevelRenderer(block.get(), itemProperties), name);
        }

        return block;
    }
}