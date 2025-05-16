package net.ultimporks.resptoken.events;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ChestBlock;
import net.ultimporks.resptoken.block.blockentity.DeathChestBlockEntity;
import net.ultimporks.resptoken.init.ModBlocks;

import java.util.concurrent.atomic.AtomicInteger;

public class DeathChestHandler {

    /**
     * Places a chest (or double chest) at the given position and fills it with the player's inventory.
     *
     * @param player   The player whose inventory will be stored.
     * @param level    The server-level where the chest will be placed.
     * @param deathPos The position to place the chest.
     */
    public static void placeDeathChest(Player player, ServerLevel level, BlockPos deathPos) {
        // Define Chest positions
        BlockPos chestPos1 = deathPos;

        // Place first chest
        level.setBlock(chestPos1, ModBlocks.DEATH_CHEST.get().defaultBlockState().setValue(ChestBlock.FACING, Direction.NORTH), 3);

        fillChestWithInventory(player, level, chestPos1);
    }

    /**
     * Fills the given chest(s) with the player's inventory and armor slots.
     *
     * @param player    The player whose inventory will be stored.
     * @param level     The server-level where the chest entities reside.
     * @param chestPos The position of the first chest.
     */
    private static void fillChestWithInventory(Player player, ServerLevel level, BlockPos chestPos) {
        DeathChestBlockEntity chestEntity = (DeathChestBlockEntity) level.getBlockEntity(chestPos);

        if (chestEntity != null) {
            int chestSlots = chestEntity.getContainerSize();
            AtomicInteger slotIndex = new AtomicInteger();

            // Transfer main inventory, armor, and offhand items
            transferInventoryItems(player.getInventory().items, chestEntity, chestSlots, slotIndex, level, chestPos);
            transferInventoryItems(player.getInventory().armor, chestEntity, chestSlots, slotIndex, level, chestPos);
            transferInventoryItems(player.getInventory().offhand, chestEntity, chestSlots, slotIndex, level, chestPos);

            // Clear the player's inventory
            player.getInventory().clearContent();
        }
    }

    // Helper method to transfer items from an inventory list
    private static void transferInventoryItems(NonNullList<ItemStack> inventory, DeathChestBlockEntity chestEntity, int chestSlots, AtomicInteger slotIndex, ServerLevel level, BlockPos chestPos1) {
        for (ItemStack stack : inventory) {
            if (!stack.isEmpty()) {
                slotIndex.set(distributeItem(stack, chestEntity, chestSlots, slotIndex.get(), level, chestPos1));
                stack.setCount(0); // Clear the stack after transferring
            }
        }
    }

    /**
     * Distributes an item stack into the chest(s) or drops it if no space is available.
     *
     * @param stack      The item stack to distribute.
     * @param chest      The chest entity.
     * @param chestSlots The number of slots in each chest.
     * @param index      The current slot index.
     * @param level      The server-level where the chest entities reside.
     * @param chestPos   The position of the first chest.
     * @return The next available slot index.
     */
    private static int distributeItem(ItemStack stack, DeathChestBlockEntity chest, int chestSlots, int index, ServerLevel level, BlockPos chestPos) {
        if (!stack.isEmpty()) {
            if (index < chestSlots) {
                chest.setItem(index, stack.copy());
            } else {
                level.addFreshEntity(
                        new ItemEntity(
                                level,
                                chestPos.getX() + 0.5,
                                chestPos.getY() + 1,
                                chestPos.getZ() + 0.5,
                                stack
                        )
                );

            }
            index++;
        }
        return index;
    }
}