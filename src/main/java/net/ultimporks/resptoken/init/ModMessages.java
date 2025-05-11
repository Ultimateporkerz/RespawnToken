package net.ultimporks.resptoken.init;

import com.mrcrayfish.framework.api.FrameworkAPI;
import com.mrcrayfish.framework.api.network.FrameworkNetwork;
import com.mrcrayfish.framework.api.network.MessageDirection;
import net.minecraft.resources.ResourceLocation;
import net.ultimporks.resptoken.Reference;
import net.ultimporks.resptoken.RespawnToken;
import net.ultimporks.resptoken.network.S2CMessageInvulnerableOverlay;
import net.ultimporks.resptoken.network.S2CMessageRespawnTeleport;

public class ModMessages {
    private static FrameworkNetwork playChannel;

    public static void init() {
        RespawnToken.LOGGING("(ModMessages) - Registering Network!");
        playChannel = FrameworkAPI.createNetworkBuilder(new ResourceLocation(
                Reference.NETWORK_ID), 1)

                // PLAY TO CLIENT
                .registerPlayMessage(S2CMessageInvulnerableOverlay.class, MessageDirection.PLAY_CLIENT_BOUND)
                .registerPlayMessage(S2CMessageRespawnTeleport.class, MessageDirection.PLAY_CLIENT_BOUND)


                .build();

        RespawnToken.LOGGING("(ModMessages) - Network registered successfully!");
    }

    public static FrameworkNetwork getPlayChannel() {
        return playChannel;
    }
}
