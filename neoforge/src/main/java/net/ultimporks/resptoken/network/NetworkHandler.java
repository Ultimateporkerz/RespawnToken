package net.ultimporks.resptoken.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class NetworkHandler {

    @SubscribeEvent
    public static void resgister(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1")
                        .executesOn(HandlerThread.NETWORK);
        registrar.playToClient(
                S2CMessageInvulnerableOverlay.TYPE,
                S2CMessageInvulnerableOverlay.STREAM_CODEC,
                ClientPayloadHandler::handleInvulnerableOverlay
        );

        registrar.playToClient(
                S2CMessageRespawnTeleport.TYPE,
                S2CMessageRespawnTeleport.STREAM_CODEC,
                ClientPayloadHandler::handleRespawnTeleport
        );
    }
}
