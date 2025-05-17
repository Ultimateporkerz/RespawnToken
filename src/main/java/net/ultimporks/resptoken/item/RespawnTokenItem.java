package net.ultimporks.resptoken.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.ultimporks.resptoken.RespawnToken;
import net.ultimporks.resptoken.data.PlayerInfoManager;
import net.ultimporks.resptoken.events.PlayerRespawnTeleporter;
import net.ultimporks.resptoken.init.ModMessages;
import net.ultimporks.resptoken.network.S2CMessageRespawnTeleport;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class RespawnTokenItem extends Item {
    public RespawnTokenItem(Properties properties) {
        super(properties);
    }
    private static final int MAX_DAMAGE = 12;

    @Override
    public void appendHoverText(@Nonnull ItemStack pStack, @Nullable Level pLevel, @Nonnull List<Component> pTooltipcomponents, @Nonnull TooltipFlag pIsAdvanced) {
        pTooltipcomponents.add(Component.translatable("tooltip.resptoken.respawn_token").withStyle(ChatFormatting.BOLD, ChatFormatting.GOLD));
        BlockPos deathPos = new BlockPos(pStack.getOrCreateTag().getInt("xPos"), pStack.getOrCreateTag().getInt("yPos"), pStack.getOrCreateTag().getInt("zPos"));
        pTooltipcomponents.add(Component.literal("Last Death: (" + deathPos.getX() + ", " + deathPos.getY() + ", " + deathPos.getZ() + ")").withStyle(ChatFormatting.BOLD, ChatFormatting.RED));
        pTooltipcomponents.add(Component.translatable("tooltip.resptoken.more_info").withStyle(ChatFormatting.BOLD, ChatFormatting.GRAY));
        if (Screen.hasShiftDown()) {
            pTooltipcomponents.add(Component.translatable("tooltip.resptoken.respawn_token_info").withStyle(ChatFormatting.BOLD));
        }
        super.appendHoverText(pStack, pLevel, pTooltipcomponents, pIsAdvanced);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        Level pLevel = pContext.getLevel();
        Player pPlayer = pContext.getPlayer();
        UUID playerUUID = pPlayer.getUUID();
        int seconds = PlayerRespawnTeleporter.TELEPORT_TIMER;
        int ticks = seconds * 20;
        long currentTime = pLevel.getGameTime() + ticks;

        if (!pLevel.isClientSide()) {
            if (PlayerRespawnTeleporter.shouldTeleportOnRespawn(playerUUID) && PlayerInfoManager.hasPlayerInfo(playerUUID) && !PlayerRespawnTeleporter.waitingToTeleport.containsKey(playerUUID)) {
                PlayerRespawnTeleporter.waitingToTeleport.put(playerUUID, currentTime);

                pContext.getItemInHand().getOrCreateTag().putBoolean("has_died", false);

                pContext.getItemInHand().hurtAndBreak(1, pContext.getPlayer(), (player) -> {
                    player.broadcastBreakEvent(pContext.getHand());
                    RespawnToken.LOGGING("Damaged the Respawn Token 1 HP!");
                });

                // Update the client
                ModMessages.sendToPlayer(new S2CMessageRespawnTeleport(playerUUID, currentTime), ((ServerPlayer) pPlayer));

                PlayerInfoManager.removeDidPlayerDie(playerUUID);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }


    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round(13.0F - (13.0F * stack.getDamageValue() / stack.getMaxDamage()));
    }

    @Override
    public boolean canBeDepleted() {
        return true;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return MAX_DAMAGE;
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isEnchantable(ItemStack pStack) {
        return false;
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        assert pStack.getTag() != null;
        return pStack.getOrCreateTag().getBoolean("has_died");
    }
}
