package net.ultimporks.resptoken.network;

import com.mrcrayfish.framework.api.network.MessageContext;
import com.mrcrayfish.framework.api.network.message.PlayMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.ultimporks.resptoken.events.PlayerRespawnTeleporter;

import java.util.UUID;

public class S2CMessageInvulnerableOverlay extends PlayMessage<S2CMessageInvulnerableOverlay> {
    private UUID playerUUID;
    private long endTime;

    public S2CMessageInvulnerableOverlay() {}

    public S2CMessageInvulnerableOverlay(UUID playerUUID, long endTime) {
        this.playerUUID = playerUUID;
        this.endTime = endTime;
    }

    @Override
    public void encode(S2CMessageInvulnerableOverlay message, FriendlyByteBuf buf) {
        buf.writeUUID(message.playerUUID);
        buf.writeLong(message.endTime);
    }

    @Override
    public S2CMessageInvulnerableOverlay decode(FriendlyByteBuf buf) {
        return new S2CMessageInvulnerableOverlay(buf.readUUID(), buf.readLong());
    }

    @Override
    public void handle(S2CMessageInvulnerableOverlay packet, MessageContext ctx) {
        PlayerRespawnTeleporter.invulnerablePlayers.put(packet.playerUUID, packet.endTime);
        ctx.setHandled(true);
    }
}
