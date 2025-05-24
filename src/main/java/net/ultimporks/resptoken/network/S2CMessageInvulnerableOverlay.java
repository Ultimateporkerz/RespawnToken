package net.ultimporks.resptoken.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.ultimporks.resptoken.events.PlayerRespawnTeleporter;

import java.util.UUID;
import java.util.function.Supplier;

public class S2CMessageInvulnerableOverlay {
    private final UUID playerUUID;
    private final long endTime;

    public S2CMessageInvulnerableOverlay(UUID playerUUID, long endTime) {
        this.playerUUID = playerUUID;
        this.endTime = endTime;
    }

    public S2CMessageInvulnerableOverlay(FriendlyByteBuf buf) {
        this.playerUUID = buf.readUUID();
        this.endTime = buf.readLong();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(playerUUID);
        buf.writeLong(endTime);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        PlayerRespawnTeleporter.invulnerablePlayers.put(playerUUID, endTime);
    }
}
