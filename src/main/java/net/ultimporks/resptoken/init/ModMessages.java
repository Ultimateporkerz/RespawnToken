package net.ultimporks.resptoken.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.ultimporks.resptoken.Reference;
import net.ultimporks.resptoken.network.S2CMessageInvulnerableOverlay;
import net.ultimporks.resptoken.network.S2CMessageRespawnTeleport;

public class ModMessages {
    public static SimpleChannel INSTANCE;
    private static int packetId = 0;

    private static int id() {
        return packetId++;
    }


    public static void register() {

        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(Reference.MOD_ID, "resptoken_network"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(S2CMessageInvulnerableOverlay.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(S2CMessageInvulnerableOverlay::encode)
                .decoder(S2CMessageInvulnerableOverlay::new)
                .consumerMainThread(S2CMessageInvulnerableOverlay::handle)
                .add();

        net.messageBuilder(S2CMessageRespawnTeleport.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(S2CMessageRespawnTeleport::encode)
                .decoder(S2CMessageRespawnTeleport::new)
                .consumerMainThread(S2CMessageRespawnTeleport::handle)
                .add();


    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }
    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
    public static <MSG> void sendToAll(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }
}
