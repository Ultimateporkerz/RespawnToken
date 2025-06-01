package net.ultimporks.resptoken.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.ultimporks.resptoken.Reference;

public record S2CMessageRespawnTeleport(String playerName, long endTime) implements CustomPacketPayload {
    public static final Type<S2CMessageRespawnTeleport> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "resp_teleport"));


    public static final StreamCodec<ByteBuf, S2CMessageRespawnTeleport> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            S2CMessageRespawnTeleport::playerName,
            ByteBufCodecs.VAR_LONG,
            S2CMessageRespawnTeleport::endTime,
            S2CMessageRespawnTeleport::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}