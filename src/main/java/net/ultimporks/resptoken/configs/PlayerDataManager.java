package net.ultimporks.resptoken.configs;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.Mod;
import net.ultimporks.resptoken.RespawnToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Set;

@Mod.EventBusSubscriber
public class PlayerDataManager {
    private static final Gson GSON = new Gson();
    private static final Set<String> knownPlayers = new HashSet<>();

    private static File getDataFile(MinecraftServer server) {
        if (server.isSingleplayer()) {
            return new File(server.getServerDirectory(), "saves/" + server.getWorldData().getLevelName() + "/serverconfig/resptoken_playerdata.json");
        } else {
            if (!server.isSingleplayer()) {
                return new File(server.getServerDirectory(), "" + server.getWorldData().getLevelName() + "/serverconfig/resptoken_playerdata.json");
            }
        }
        return null;
    }

    public static void loadPlayerData(MinecraftServer server) {
        File dataFile = getDataFile(server);
        if (!dataFile.exists()) return;

        try (FileReader reader = new FileReader(dataFile)) {
            JsonObject data = GSON.fromJson(reader, JsonObject.class);
            JsonArray players = data.getAsJsonArray("players");
            players.forEach(player -> knownPlayers.add(player.getAsString()));
            RespawnToken.LOGGING("(PlayerDataManager) - Successfully loaded player data.");
        } catch (Exception ex) {
            RespawnToken.LOGGING("(PlayerDataManager) - Could not load Player Data: " + ex);
        }
    }


    public static void savePlayerData(MinecraftServer server) {
        File dataFile = getDataFile(server);
        dataFile.getParentFile().mkdirs();

        try (FileWriter writer = new FileWriter(dataFile))  {
            JsonObject data = new JsonObject();
            JsonArray players = new JsonArray();
            knownPlayers.forEach(players::add);
            data.add("players", players);
            GSON.toJson(data, writer);
        } catch (Exception ex) {
            RespawnToken.LOGGING("(RespawnToken) - Could not save player data: " +  ex);
        }
    }

    public static boolean isPlayerKnown(String playerName) {
        return knownPlayers.contains(playerName);
    }

    public static void addPlayer(String playerName) {
        if (knownPlayers.add(playerName)) {
            RespawnToken.LOGGING("(PlayerDataManager) - New player added: " + playerName);
        }
    }

}
