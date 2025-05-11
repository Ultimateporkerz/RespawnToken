package net.ultimporks.resptoken.network;

import com.mrcrayfish.framework.api.network.MessageContext;
import com.mrcrayfish.framework.api.network.message.PlayMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.ultimporks.resptoken.events.PlayerRespawnTeleporter;

import java.util.UUID;

public class S2CMessageRespawnTeleport extends PlayMessage<S2CMessageRespawnTeleport> {
    private UUID playerUUID;
    private long endTime;

    public S2CMessageRespawnTeleport() {}

    public S2CMessageRespawnTeleport(UUID playerUUID, long endTime) {
        this.playerUUID = playerUUID;
        this.endTime = endTime;
    }

    @Override
    public void encode(S2CMessageRespawnTeleport message, FriendlyByteBuf buf) {
        buf.writeUUID(message.playerUUID);
        buf.writeLong(message.endTime);
    }

    @Override
    public S2CMessageRespawnTeleport decode(FriendlyByteBuf buf) {
        return new S2CMessageRespawnTeleport(buf.readUUID(), buf.readLong());
    }

    @Override
    public void handle(S2CMessageRespawnTeleport packet, MessageContext context) {
        PlayerRespawnTeleporter.waitingToTeleport.put(packet.playerUUID, packet.endTime);
        context.setHandled(true);
    }
}