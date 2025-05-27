package net.ultimporks.resptoken.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.ultimporks.resptoken.Reference;
import net.ultimporks.resptoken.block.blockentity.DeathChestBlockEntity;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Reference.MOD_ID);

    public static final Supplier<BlockEntityType<DeathChestBlockEntity>> DEATH_CHEST_BE =
            BLOCK_ENTITIES.register("death_chest_be", () ->
                BlockEntityType.Builder.of(DeathChestBlockEntity::new,
                        ModBlocks.DEATH_CHEST.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }

}
