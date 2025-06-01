package net.ultimporks.resptoken.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
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
import net.ultimporks.resptoken.configs.ModConfigs;
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

        if (!pLevel.isClientSide()) {
            if (PlayerRespawnTeleporter.shouldTeleportOnRespawn(playerUUID) && PlayerInfoManager.hasPlayerInfo(playerUUID) && !PlayerRespawnTeleporter.waitingToTeleport.containsKey(playerUUID)) {
                PlayerRespawnTeleporter.waitingToTeleport.put(playerUUID, currentTime + ticks);

                pContext.getItemInHand().hurtAndBreak(1, ((ServerLevel) pLevel), ((ServerPlayer) pContext.getPlayer()),
                        item -> pContext.getPlayer().onEquippedItemBroken(item, EquipmentSlot.MAINHAND));
                RespawnToken.LOGGING("Damaged the Respawn Token 1 HP!");

                // Update the Client
                ModMessages.sendToPlayer(new S2CMessageRespawnTeleport(playerUUID, currentTime + ticks), ((ServerPlayer) pPlayer));

                pContext.getItemInHand().set(ModDataComponents.HAS_DIED.get(), false);

                PlayerInfoManager.removeDidPlayerDie(playerUUID);
                return InteractionResult.SUCCESS;

            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipcomponents, TooltipFlag pTooltipFlag) {
        BlockPos deathPos = pStack.get(ModDataComponents.LAST_DEATH.get());
        pTooltipcomponents.add(Component.translatable("tooltip.resptoken.respawn_token_info").withStyle(ChatFormatting.BOLD));
        // Player is holding shift
        if (Screen.hasShiftDown()) {
            pTooltipcomponents.add(Component.translatable("tooltip.resptoken.respawn_token").withStyle(ChatFormatting.BOLD, ChatFormatting.GOLD));
            pTooltipcomponents.add(Component.literal("------------------------").withStyle(ChatFormatting.GRAY));
            if (deathPos != null) {
                pTooltipcomponents.add(Component.literal("Last Death: (" + deathPos.getX() + ", " + deathPos.getY() + ", " + deathPos.getZ() + ")").withStyle(ChatFormatting.BOLD, ChatFormatting.RED));
            }
        } else {
            pTooltipcomponents.add(Component.translatable("tooltip.resptoken.more_info").withStyle(ChatFormatting.BOLD, ChatFormatting.GRAY));
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
        if (Boolean.TRUE.equals(pStack.get(ModDataComponents.HAS_DIED.get()))) {
            return true;
        } else if (Boolean.FALSE.equals(pStack.get(ModDataComponents.HAS_DIED.get()))) {
            return false;

        }
        return false;
    }

    @Override
    public void onCraftedBy(ItemStack pStack, Level pLevel, Player pPlayer) {
        super.onCraftedBy(pStack, pLevel, pPlayer);
        if (ModConfigs.COMMON.respawnTokenMaxDamage.get() != 1) {
            pStack.set(DataComponents.MAX_DAMAGE, ModConfigs.COMMON.respawnTokenMaxDamage.get());
            pStack.set(DataComponents.DAMAGE, 0);
        }
    }
}
