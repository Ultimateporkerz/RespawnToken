package net.ultimporks.resptoken.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.ultimporks.resptoken.events.PlayerRespawnTeleporter;

import java.util.UUID;

public class S2CMessageRespawnTeleport {
    private UUID playerUUID;
    private long endTime;

    public S2CMessageRespawnTeleport(UUID playerUUID, long endTime) {
        this.playerUUID = playerUUID;
        this.endTime = endTime;
    }

    public S2CMessageRespawnTeleport(FriendlyByteBuf buf) {
        this.playerUUID = buf.readUUID();
        this.endTime = buf.readLong();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(playerUUID);
        buf.writeLong(endTime);
    }

    public void handle(CustomPayloadEvent.Context context) {
        PlayerRespawnTeleporter.waitingToTeleport.put(playerUUID, endTime);
    }
}