package net.ultimporks.resptoken.events;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.ultimporks.resptoken.RespawnToken;
import net.ultimporks.resptoken.compat.CompatCheck;
import net.ultimporks.resptoken.configs.ModConfigs;
import net.ultimporks.resptoken.data.PlayerInfo;
import net.ultimporks.resptoken.data.PlayerInfoManager;
import net.ultimporks.resptoken.init.ModDataComponents;
import net.ultimporks.resptoken.item.RespawnTokenItem;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class PlayerRespawnHandler {

    public static final HashMap<UUID, List<ItemStack>> tokenStorage = new HashMap<>();

    public static void checkInventory(Player player, boolean isInLava, boolean isInVoid) {
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
            }
        }

        // Check the Curios inventory if no token was found in the main inventory
        // Check if the mod is installed...
        if (CompatCheck.IS_CURIOS_INSTALLED) {
            if (!hasRespawnToken.get()) {
                CuriosApi.getCuriosInventory(player).ifPresent(curiosInventory -> {
                    Optional<ICurioStacksHandler> slotInventory = curiosInventory.getStacksHandler("charm");
                    slotInventory.ifPresent(handler -> {
                        for (int i = 0; i < handler.getSlots(); i++) {
                            ItemStack stack = handler.getStacks().getStackInSlot(i);
                            if (stack.getItem() instanceof RespawnTokenItem) {
                                RespawnToken.LOGGING("Respawn Token detected in bauble slot.");
                                stack.set(ModDataComponents.LAST_DEATH.get(), player.getOnPos());
                                stack.set(ModDataComponents.HAS_DIED.get(), true);
                                hasRespawnToken.set(true);
                                tokens.add(stack.copy());
                            }
                        }
                    });
                });
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


        // Handle death coordinates and death chest
        if (ModConfigs.COMMON.enableDeathChest.get() && !isInVoid) {
            if (player.level() instanceof ServerLevel serverLevel) {
                DeathChestHandler.placeDeathChest(player, serverLevel, player.blockPosition());
            }
        } else if (isInVoid && ModConfigs.COMMON.enableDeathChest.get()) {
            if (player.level() instanceof ServerLevel serverLevel) {
                BlockPos safeBlockPos = PlayerRespawnTeleporter.getSafeBlockPosition(playerUUID);
                DeathChestHandler.placeDeathChest(player, serverLevel, safeBlockPos);
            }
        }
    }
}
