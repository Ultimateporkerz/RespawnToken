package net.ultimporks.resptoken.client.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.ultimporks.resptoken.events.PlayerRespawnTeleporter;
import net.ultimporks.resptoken.Reference;

@EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public class InvulnerableOverlay {
    private static final Minecraft CLIENT = Minecraft.getInstance();

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiEvent.Pre event) {
        Player player = CLIENT.player;

        if (player != null) {
            String playerName = player.getName().getString();

            if (PlayerRespawnTeleporter.invulnerablePlayers.containsKey(playerName)) {
                long endTime = PlayerRespawnTeleporter.invulnerablePlayers.get(playerName);
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
