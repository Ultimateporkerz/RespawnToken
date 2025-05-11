package net.ultimporks.resptoken.data;

import net.ultimporks.resptoken.RespawnToken;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerInfoManager {

    private static final Map<UUID, PlayerInfo> playerInfoMap = new ConcurrentHashMap<>();
    private static final Map<UUID, Boolean> didPlayerDie = new ConcurrentHashMap<>();

    private PlayerInfoManager() {}

    // Add
    public static void addPlayerInfo(UUID playerUUID, PlayerInfo playerDeathInfo) {
        RespawnToken.LOGGING("(PlayerInfoManager) - Player Death Info added to map...");
        playerInfoMap.put(playerUUID, playerDeathInfo);
    }
    public static void addDidPlayerDie(UUID playerUUID, boolean die) {
        RespawnToken.LOGGING("(PlayerInfoManager) - Did player Die: " + die);
        didPlayerDie.put(playerUUID, die);
    }

    // Remove
    public static void removePlayerInfo(UUID playerUUID) {
        RespawnToken.LOGGING("(PlayerInfoManager) - Removing player death info from map...");
        playerInfoMap.remove(playerUUID);
    }
    public static void removeDidPlayerDie(UUID playerUUID) {
        didPlayerDie.remove(playerUUID);
    }

    // Get
    public static PlayerInfo getPlayerDeathInfo(UUID playerUUID) {
        RespawnToken.LOGGING("(PlayerInfoManager) - Returning player death info...");
        return playerInfoMap.get(playerUUID);
    }
    public static boolean didPlayerDie(UUID playerUUID) {
        RespawnToken.LOGGING("(PlayerInfoManager) - Checking if player died...");
        return didPlayerDie.get(playerUUID);
    }

    // Has
    public static boolean hasPlayerInfo(UUID playerUUID) {
        RespawnToken.LOGGING("Checking if Player has playerInfo...");
        return playerInfoMap.containsKey(playerUUID);
    }
    public static boolean hasDeathInfo(UUID playerUUID) {
        RespawnToken.LOGGING("(PlayerInfoManager) - Checking if player has Death Info");
        return didPlayerDie.containsKey(playerUUID);
    }


}
