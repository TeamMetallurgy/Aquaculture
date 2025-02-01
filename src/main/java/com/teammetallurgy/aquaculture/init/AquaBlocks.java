package com.teammetallurgy.aquaculture.init;

import com.teammetallurgy.aquaculture.Aquaculture;
import com.teammetallurgy.aquaculture.block.FarmlandMoistBlock;
import com.teammetallurgy.aquaculture.block.NeptunesBountyBlock;
import com.teammetallurgy.aquaculture.block.TackleBoxBlock;
import com.teammetallurgy.aquaculture.block.WormFarmBlock;
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

public class AquaBlocks {
    public static final DeferredRegister.Blocks BLOCK_DEFERRED = DeferredRegister.createBlocks(Aquaculture.MOD_ID);
    public static final DeferredBlock<Block> FARMLAND = register(FarmlandMoistBlock::new, "farmland", false);
    public static final DeferredBlock<Block> NEPTUNIUM_BLOCK = register(p -> new Block(p.mapColor(MapColor.COLOR_CYAN).strength(5.0F, 6.0F).sound(SoundType.METAL)), "neptunium_block");
    public static final DeferredBlock<Block> NEPTUNES_BOUNTY = register(p -> new NeptunesBountyBlock(AquaBlockEntities.NEPTUNES_BOUNTY::get, p), "neptunes_bounty");
    public static final DeferredBlock<Block> TACKLE_BOX = register(TackleBoxBlock::new, "tackle_box");
    public static final DeferredBlock<Block> WORM_FARM = register(WormFarmBlock::new, "worm_farm");

    /**
     * Same as {@link AquaBlocks#register(Function, String, boolean)}, but have group set by default
     */
    public static DeferredBlock<Block> register(Function<BlockBehaviour.Properties, ? extends Block> function, @Nonnull String name) {
        return register(function, name, true);
    }

    /**
     * Registers a block with a basic BlockItem
     *
     * @param function The block to register
     * @param name     The name to register the block with
     * @return The Block that was registered
     */
    public static DeferredBlock<Block> register(Function<BlockBehaviour.Properties, ? extends Block> function, @Nonnull String name, boolean addToItemGroup) {
        DeferredBlock<Block> block = BLOCK_DEFERRED.registerBlock(name, function);

        if (addToItemGroup) {
            AquaItems.registerWithTab(p -> new BlockItem(block.get(), p.useBlockDescriptionPrefix()), name);
        } else {
            AquaItems.register(p -> new BlockItem(block.get(), p.useBlockDescriptionPrefix()), name);
        }

        return block;
    }
}