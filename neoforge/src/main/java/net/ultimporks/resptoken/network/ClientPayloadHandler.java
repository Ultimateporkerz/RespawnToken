package net.ultimporks.resptoken.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.ultimporks.resptoken.events.PlayerRespawnTeleporter;

public class ClientPayloadHandler {

    public static void handleInvulnerableOverlay(final S2CMessageInvulnerableOverlay data, final IPayloadContext context) {
        PlayerRespawnTeleporter.invulnerablePlayers.put(data.playerName(), data.endTime());
    }

    public static void handleRespawnTeleport(final S2CMessageRespawnTeleport data, final IPayloadContext context) {
        PlayerRespawnTeleporter.waitingToTeleport.put(data.playerName(), data.endTime());
    }
}
