package net.ultimporks.resptoken.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.ultimporks.resptoken.Reference;

public record S2CMessageInvulnerableOverlay(String playerName, long endTime) implements CustomPacketPayload {
    public static final Type<S2CMessageInvulnerableOverlay> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "inv_overlay"));

    public static final StreamCodec<ByteBuf, S2CMessageInvulnerableOverlay> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            S2CMessageInvulnerableOverlay::playerName,
            ByteBufCodecs.VAR_LONG,
            S2CMessageInvulnerableOverlay::endTime,
            S2CMessageInvulnerableOverlay::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
