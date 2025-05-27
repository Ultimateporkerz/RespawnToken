package net.ultimporks.resptoken.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;
import net.ultimporks.resptoken.Reference;
import net.ultimporks.resptoken.network.S2CMessageInvulnerableOverlay;
import net.ultimporks.resptoken.network.S2CMessageRespawnTeleport;

public class ModMessages {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = ChannelBuilder.named(
            ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "resptoken_network"))
            .serverAcceptedVersions((status, version) -> true)
            .clientAcceptedVersions((status, version) -> true)
            .networkProtocolVersion(Integer.parseInt(PROTOCOL_VERSION))
            .simpleChannel();

    public static void register() {

        INSTANCE.messageBuilder(S2CMessageInvulnerableOverlay.class, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(S2CMessageInvulnerableOverlay::encode)
                .decoder(S2CMessageInvulnerableOverlay::new)
                .consumerMainThread(S2CMessageInvulnerableOverlay::handle)
                .add();

        INSTANCE.messageBuilder(S2CMessageRespawnTeleport.class, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(S2CMessageRespawnTeleport::encode)
                .decoder(S2CMessageRespawnTeleport::new)
                .consumerMainThread(S2CMessageRespawnTeleport::handle)
                .add();

    }

    public static void sendToServer(Object msg) {
        INSTANCE.send(msg, PacketDistributor.SERVER.noArg());
    }

    public static void sendToPlayer(Object msg, ServerPlayer player) {
        INSTANCE.send(msg, PacketDistributor.PLAYER.with(player));
    }

    public static void sendToAllPlayer(Object msg) {
        INSTANCE.send(msg, PacketDistributor.ALL.noArg());
    }

}
