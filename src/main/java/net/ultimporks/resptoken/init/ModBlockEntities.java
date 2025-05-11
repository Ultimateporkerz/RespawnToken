package net.ultimporks.resptoken.init;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.ultimporks.resptoken.Reference;
import net.ultimporks.resptoken.block.blockentity.DeathChestBlockEntity;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Reference.MOD_ID);

    public static final RegistryObject<BlockEntityType<DeathChestBlockEntity>> DEATH_CHEST_BE =
            BLOCK_ENTITIES.register("death_chest_be", () ->
                BlockEntityType.Builder.of(DeathChestBlockEntity::new, ModBlocks.DEATH_CHEST.get()).build(null));




    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }

}
