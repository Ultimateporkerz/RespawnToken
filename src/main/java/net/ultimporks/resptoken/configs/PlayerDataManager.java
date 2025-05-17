package net.ultimporks.resptoken.configs;

import com.google.gson.*;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.Mod;
import net.ultimporks.resptoken.RespawnToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber
public class PlayerDataManager {
    /*

    private static final Gson GSON = new Gson();
    private static final Set<String> knownPlayers = ConcurrentHashMap.newKeySet();

    private static final Gson GSON_PRETTY = new GsonBuilder().setPrettyPrinting().create();
    private static final Map<String, Set<UUID>> worldToPlayerUUIDs = new ConcurrentHashMap<>();



    private static File getDataFile(MinecraftServer server) {
        File baseDir = server.getServerDirectory().toFile();
        String levelName = server.getWorldData().getLevelName();
        return new File(baseDir, "saves/" + levelName + "/serverconfig/resptoken_playerdata.json");
    }

    public static void loadPlayerData(MinecraftServer server) {
        File dataFile = getDataFile(server);
        if (!dataFile.exists()) return;

        try (FileReader reader = new FileReader(dataFile, StandardCharsets.UTF_8)) {
            JsonObject data = GSON.fromJson(reader, JsonObject.class);
            String levelName = server.getWorldData().getLevelName();
            Set<UUID> playerUUIDs = new HashSet<>();

            JsonArray players = data.getAsJsonArray("players");
            for (JsonElement element : players) {
                try {
                    playerUUIDs.add(UUID.fromString(element.getAsString()));
                } catch (IllegalArgumentException ignored) {}
            }

            worldToPlayerUUIDs.put(levelName, playerUUIDs);
            RespawnToken.LOGGING("(PlayerDataManager) - Loaded data for world: " + levelName);
        } catch (Exception ex) {
            RespawnToken.LOGGING("(PlayerDataManager) - Error loading player data: " + ex);
        }
    }

    public static void savePlayerData(MinecraftServer server) {
        File dataFile = getDataFile(server);
        dataFile.getParentFile().mkdirs();

        try (FileWriter writer = new FileWriter(dataFile, StandardCharsets.UTF_8)) {
            String levelName = server.getWorldData().getLevelName();
            Set<UUID> playerUUIDs = worldToPlayerUUIDs.getOrDefault(levelName, new HashSet<>());

            JsonObject data = new JsonObject();
            JsonArray players = new JsonArray();
            playerUUIDs.forEach(uuid -> players.add(uuid.toString()));
            data.add("players", players);

            GSON.toJson(data, writer);
        } catch (Exception ex) {
            RespawnToken.LOGGING("(PlayerDataManager) - Error saving player data: " + ex);
        }
    }

    public static boolean isPlayerKnown(MinecraftServer server, UUID uuid) {
        String levelName = server.getWorldData().getLevelName();
        return worldToPlayerUUIDs.getOrDefault(levelName, Collections.emptySet()).contains(uuid);
    }

    public static void addPlayer(MinecraftServer server, UUID uuid) {
        String levelName = server.getWorldData().getLevelName();
        worldToPlayerUUIDs.computeIfAbsent(levelName, k -> ConcurrentHashMap.newKeySet()).add(uuid);
        RespawnToken.LOGGING("(PlayerDataManager) - Player added for world " + levelName + ": " + uuid);
    }

     */

}
