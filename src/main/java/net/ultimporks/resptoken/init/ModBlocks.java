package net.ultimporks.resptoken.init;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.ultimporks.resptoken.Reference;
import net.ultimporks.resptoken.block.DeathChestBlock;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(Reference.MOD_ID);

    public static final DeferredBlock<Block> DEATH_CHEST = registerBlock("death_chest",
            () -> new DeathChestBlock(BlockBehaviour.Properties.of()
                    .strength(1.5F, 50.5F)
                    .destroyTime(1.0F)
                    .sound(SoundType.WOOD),
                    ModBlockEntities.DEATH_CHEST_BE::get));



    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }
    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
