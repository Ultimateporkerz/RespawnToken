package net.ultimporks.resptoken.events;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.ultimporks.resptoken.RespawnToken;
import net.ultimporks.resptoken.data.PlayerInfo;
import net.ultimporks.resptoken.data.PlayerInfoManager;
import net.ultimporks.resptoken.init.ModDataComponents;
import net.ultimporks.resptoken.item.RespawnTokenItem;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class PlayerRespawnHandler {

    public static final HashMap<UUID, List<ItemStack>> tokenStorage = new HashMap<>();

    public static void checkInventory(Player player, boolean isInLava, boolean isInVoid, boolean isDeathChestEnabled) {
        AtomicBoolean hasRespawnToken = new AtomicBoolean(false);
        UUID playerUUID = player.getUUID();
        List<ItemStack> tokens = new ArrayList<>();

        for (ItemStack stack : player.getInventory().items) {
            if (!stack.isEmpty() && stack.getItem() instanceof RespawnTokenItem) {
                RespawnToken.LOGGING("Respawn Token detected in inventory!");
                stack.set(ModDataComponents.LAST_DEATH.get(), player.getOnPos());
                stack.set(ModDataComponents.HAS_DIED.get(), true);
                hasRespawnToken.set(true);
                tokens.add(stack.copy());
                if (!isDeathChestEnabled) {
                    stack.copyAndClear();
                }
            }
        }

        // Save the token(s) if any were found
        if (!tokens.isEmpty()) {
            tokenStorage.put(playerUUID, tokens);
        }

        // Handle the case where no token is found
        if (!hasRespawnToken.get()) {
            PlayerRespawnTeleporter.shouldTeleportOnRespawn.put(playerUUID, false);
            RespawnToken.LOGGING("No Respawn Token found. No special handling for respawn.");
        } else if (hasRespawnToken.get()) {
            PlayerRespawnTeleporter.shouldTeleportOnRespawn.put(playerUUID, true);
            if (isInLava) {
                PlayerRespawnTeleporter.shouldExtendInvulnerabilityTimer.put(playerUUID, true);
            }
            if (isInVoid) {
                PlayerRespawnTeleporter.shouldTeleportToSafePosition.put(playerUUID, true);
            }
        }
        // Remove old player info
        if (PlayerInfoManager.hasPlayerInfo(playerUUID)) {
            PlayerInfoManager.removePlayerInfo(playerUUID);
        }
        // Add new player info
        PlayerInfo playerInfo = new PlayerInfo(player);
        PlayerInfoManager.addPlayerInfo(playerUUID, playerInfo);

        if (isDeathChestEnabled && player.level() instanceof ServerLevel serverLevel) {
            if (isInVoid) {
                BlockPos safeBlockPos = PlayerRespawnTeleporter.getSafeBlockPosition(playerUUID);
                DeathChestHandler.placeDeathChest(player, serverLevel, safeBlockPos);
            } else {
                DeathChestHandler.placeDeathChest(player, serverLevel, player.blockPosition());
            }
        }
    }
}
