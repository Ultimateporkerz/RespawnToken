package net.ultimporks.resptoken.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.ultimporks.resptoken.RespawnToken;
import net.ultimporks.resptoken.data.PlayerInfoManager;
import net.ultimporks.resptoken.events.PlayerRespawnTeleporter;
import net.ultimporks.resptoken.init.ModDataComponents;
import net.ultimporks.resptoken.init.ModMessages;
import net.ultimporks.resptoken.network.S2CMessageRespawnTeleport;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class RespawnTokenItem extends Item {
    public RespawnTokenItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        Player pPlayer = pContext.getPlayer();
        assert pPlayer != null;
        Level pLevel = pPlayer.level();
        UUID playerUUID = pPlayer.getUUID();
        int seconds = PlayerRespawnTeleporter.TELEPORT_TIMER;
        int ticks = seconds * 20;
        long currentTime = pLevel.getGameTime();

        if (PlayerRespawnTeleporter.shouldTeleportOnRespawn(playerUUID)) {
            if (PlayerInfoManager.hasPlayerInfo(playerUUID) && !PlayerRespawnTeleporter.waitingToTeleport.containsKey(playerUUID)) {
                PlayerRespawnTeleporter.waitingToTeleport.put(playerUUID, currentTime + ticks);

                pContext.getItemInHand().set(ModDataComponents.HAS_DIED.get(), false);

                if (!pLevel.isClientSide) {
                    pContext.getItemInHand().hurtAndBreak(1, ((ServerLevel) pLevel), ((ServerPlayer) pContext.getPlayer()),
                            item -> pContext.getPlayer().onEquippedItemBroken(item, EquipmentSlot.MAINHAND));
                    RespawnToken.LOGGING("Damaged the Respawn Token 1 HP!");
                }

                // Update the Client
                ModMessages.sendToPlayer(new S2CMessageRespawnTeleport
                        (playerUUID, currentTime + ticks),
                        ((ServerPlayer) pPlayer));
            }
            PlayerInfoManager.removeDidPlayerDie(playerUUID);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipcomponents, TooltipFlag pTooltipFlag) {
        pTooltipcomponents.add(Component.translatable("tooltip.resptoken.respawn_token").withStyle(ChatFormatting.BOLD, ChatFormatting.GOLD));
        BlockPos deathPos = pStack.get(ModDataComponents.LAST_DEATH.get());
        if (deathPos != null) {
            pTooltipcomponents.add(Component.literal("Last Death: (" + deathPos.getX() + ", " + deathPos.getY() + ", " + deathPos.getZ() + ")").withStyle(ChatFormatting.BOLD, ChatFormatting.RED));
        }
        pTooltipcomponents.add(Component.translatable("tooltip.resptoken.more_info").withStyle(ChatFormatting.BOLD, ChatFormatting.GRAY));
        if (Screen.hasShiftDown()) {
            pTooltipcomponents.add(Component.translatable("tooltip.resptoken.respawn_token_info").withStyle(ChatFormatting.BOLD));
        }
        super.appendHoverText(pStack, pContext, pTooltipcomponents, pTooltipFlag);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round(13.0F - (13.0F * stack.getDamageValue() / stack.getMaxDamage()));
    }

    @Override
    public boolean isEnchantable(ItemStack pStack) {
        return false;
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return Boolean.TRUE.equals(pStack.get(ModDataComponents.HAS_DIED.get()));
    }
}
