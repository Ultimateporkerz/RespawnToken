package net.ultimporks.resptoken.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlayerDataSavedData extends SavedData {
    private final Set<UUID> knownPlayers = new HashSet<>();

    public static final String DATA_NAME = "respawntoken_player_tracker";

    public static PlayerDataSavedData get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(
                PlayerDataSavedData::load,
                PlayerDataSavedData::new,
                DATA_NAME
        );
    }

    private PlayerDataSavedData() {}

    public static PlayerDataSavedData load(CompoundTag tag) {
        PlayerDataSavedData data = new PlayerDataSavedData();
        ListTag list = tag.getList("knownPlayers", 8);
        for (int i = 0; i < list.size(); i++) {
            data.knownPlayers.add(UUID.fromString(list.getString(i)));
        }
        return data;
    }


    @Override
    public @NotNull CompoundTag save(CompoundTag tag) {
        ListTag list = new ListTag();
        for (UUID uuid : knownPlayers) {
            list.add(StringTag.valueOf(uuid.toString()));
        }
        tag.put("knownPlayers", list);
        return tag;
    }

    public boolean isPlayerKnown(UUID uuid) {
        return knownPlayers.contains(uuid);
    }

    public void addPlayer(UUID uuid) {
        if (knownPlayers.add(uuid)) {
            setDirty();
        }
    }

    public Set<UUID> getKnownPlayers() {
        return knownPlayers;
    }

}
