package net.ultimporks.resptoken.client.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.ultimporks.resptoken.events.PlayerRespawnTeleporter;
import net.ultimporks.resptoken.Reference;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public class InvulnerableOverlay {
    private static final Minecraft CLIENT = Minecraft.getInstance();

    @SubscribeEvent
    public static void onRenderOverlay(CustomizeGuiOverlayEvent event) {
        Player player = CLIENT.player;

        if (player != null) {
            UUID playerUUID = player.getUUID();

            if (PlayerRespawnTeleporter.invulnerablePlayers.containsKey(playerUUID)) {
                long endTime = PlayerRespawnTeleporter.invulnerablePlayers.get(playerUUID);
                long currentTime = player.level().getGameTime();
                long ticksLeft = endTime - currentTime;


                if (ticksLeft > 0) {
                    long secondsLeft = ticksLeft / 20;

                    String text = "Invulnerability: " + secondsLeft;

                    int x = 10;
                    int y = 10;

                    Component boldText = Component.literal(text).setStyle(Style.EMPTY.withBold(true));

                    GuiGraphics guiGraphics = event.getGuiGraphics();

                    guiGraphics.drawString(CLIENT.font, boldText, x, y, 0xffa500);
                }
            }
        }
    }
}
