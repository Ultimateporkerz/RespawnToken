package net.ultimporks.resptoken.events;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.ITeleporter;
import net.ultimporks.resptoken.RespawnToken;
import net.ultimporks.resptoken.configs.ModConfigs;
import net.ultimporks.resptoken.data.PlayerInfo;
import net.ultimporks.resptoken.data.PlayerInfoManager;
import net.ultimporks.resptoken.init.ModMessages;
import net.ultimporks.resptoken.network.S2CMessageInvulnerableOverlay;

import java.util.*;

public class PlayerRespawnTeleporter implements ITeleporter {
    public static final Map<UUID, Boolean> shouldTeleportOnRespawn = new HashMap<>();
    public static final Map<UUID, Boolean> shouldExtendInvulnerabilityTimer = new HashMap<>();
    public static final Map<UUID, Boolean> shouldTeleportToSafePosition = new HashMap<>();

    // Saves safe positions if player dies in the end.
    public static final Map<UUID, List<BlockPos>> safePlayerBlockPosition = new HashMap<>();

    public static final Map<UUID, Long> invulnerablePlayers = new HashMap<>();
    public final static int INVULNERABLE_TIMER = ModConfigs.COMMON.invulnerabilityCountdown.get();

    public static final Map<UUID, Long> waitingToTeleport = new HashMap<>();
    public final static int TELEPORT_TIMER = ModConfigs.COMMON.teleportCountdown.get();

    public static void teleportPlayer(Player player) {
        UUID playerUUID = player.getUUID();
        Level currentLevel = player.level();
        long currentTime = currentLevel.getGameTime();

        RespawnToken.LOGGING("(PlayerRespawnTeleporter) - Is client side? " + currentLevel.isClientSide);

        PlayerInfo playerInfo = PlayerInfoManager.getPlayerDeathInfo(playerUUID);
        if (playerInfo == null) {
            RespawnToken.LOGGING("No player info found for " + player.getName().getString());
            return;
        }

        BlockPos respawnPos = new BlockPos(playerInfo.getDeathXPos(), playerInfo.getDeathYPos(), playerInfo.getDeathZPos());
        ServerLevel targetDimension = playerInfo.getPlayerDeathLevel();

        if (shouldTeleportToSafePosition(playerUUID)) {
            BlockPos safeRespawnPos = getSafeBlockPosition(playerUUID);
            if (safeRespawnPos != null) {
                player.teleportTo(targetDimension, safeRespawnPos.getX(), safeRespawnPos.getY(), safeRespawnPos.getZ(), Collections.emptySet(), 0, 0);
                targetDimension.playSound(null, safeRespawnPos, SoundEvents.ENDERMAN_TELEPORT, SoundSource.AMBIENT, 1.0F, 1.0F);
            } else {
                RespawnToken.LOGGING("(PlayerRespawnTeleporter) - SAFE RESPAWN POS IS NULL!");
                return;
            }
        } else {
            player.teleportTo(targetDimension, respawnPos.getX(), respawnPos.getY(), respawnPos.getZ(), Collections.emptySet(), 0, 0);
            targetDimension.playSound(null, respawnPos, SoundEvents.ENDERMAN_TELEPORT, SoundSource.AMBIENT, 1.0F, 1.0F);
        }

        if (ModConfigs.COMMON.invulnerabilityEnabled.get()) {
            handlePlayerInvulnerability(player, currentLevel, currentTime, playerUUID);
        }

    }

    private static void handlePlayerInvulnerability(Player player, Level currentLevel, long currentTime, UUID playerUUID) {
        if (ModConfigs.COMMON.lavaDeathExtendInvulnerability.get() && shouldExtendInvulnerabilityTimer(playerUUID)) {
            // Add the extra time to the timer set in the config
            int addedTime = ModConfigs.COMMON.lavaDeathExtensionAmount.get();
            int ticksTotal = (INVULNERABLE_TIMER + addedTime) * 20;
            invulnerablePlayers.put(playerUUID, currentTime + ticksTotal);
            player.setInvulnerable(true);

            if (!currentLevel.isClientSide) {
                // Send network packet for invulnerability effect
                ModMessages.sendToPlayer(new S2CMessageInvulnerableOverlay(playerUUID, currentTime + ticksTotal), ((ServerPlayer) player));
            }
        } else {
            // Run default if Lava death doesn't extend invulnerability
            int ticks = INVULNERABLE_TIMER * 20;
            invulnerablePlayers.put(playerUUID, currentTime + ticks);
            player.setInvulnerable(true);

            if (!currentLevel.isClientSide) {
                // Send network packet for invulnerability effect
                ModMessages.sendToPlayer(new S2CMessageInvulnerableOverlay(playerUUID, currentTime + ticks), (ServerPlayer) player);
            }
        }

        shouldTeleportOnRespawn.put(playerUUID, true);
        shouldExtendInvulnerabilityTimer.remove(playerUUID);
    }

    public static void addSafeBlockPosition(UUID playerUUID, BlockPos currentBlockPos, Level level) {
        if (isSafeBlock(level, currentBlockPos)) {
            safePlayerBlockPosition.putIfAbsent(playerUUID, new ArrayList<>());
            List<BlockPos> positions = safePlayerBlockPosition.get(playerUUID);

            if (!positions.contains(currentBlockPos)) {
                positions.add(currentBlockPos);

                RespawnToken.LOGGING("(PlayerRespawnTeleporter) - Safe Block List: " + safePlayerBlockPosition.get(playerUUID));

                if (positions.size() > 4) {
                    positions.remove(0);
                }
            }
        }
    }
    public static BlockPos getSafeBlockPosition(UUID playerUUID) {
        List<BlockPos> positions = safePlayerBlockPosition.get(playerUUID);
        if (positions != null && !positions.isEmpty()) {
            // Iterate from the most recent to the oldest position
            for (int i = positions.size() - 1; i >= 0; i--) {
                if (positions.get(i) != null) {
                    return positions.get(i);
                }
            }
        }
        // Return null if no safe position is found
        return null;
    }

    public static boolean shouldTeleportOnRespawn(UUID playerUUID) {
        return shouldTeleportOnRespawn.getOrDefault(playerUUID, false);
    }
    public static boolean shouldExtendInvulnerabilityTimer(UUID playerUUID) {
        return shouldExtendInvulnerabilityTimer.getOrDefault(playerUUID, false);
    }
    public static boolean shouldTeleportToSafePosition(UUID playerUUID) {
        return shouldTeleportToSafePosition.getOrDefault(playerUUID, false);
    }

    private static boolean isSafeBlock(Level level, BlockPos pos) {
        BlockState blockBelow = level.getBlockState(pos.below());
        BlockState blockAt = level.getBlockState(pos.above(1));
        BlockState blockAbove = level.getBlockState(pos.above(2));

        boolean isSafe = blockBelow.isSolid() && blockAt.isAir() && blockAbove.isAir();
        RespawnToken.LOGGING("Checking block at " + pos + ": Below is " + blockBelow.getBlock() + ", At is " + blockAt.getBlock() + ", Above is " + blockAbove.getBlock() + "— Safe: " + isSafe);
        return isSafe;
    }




}