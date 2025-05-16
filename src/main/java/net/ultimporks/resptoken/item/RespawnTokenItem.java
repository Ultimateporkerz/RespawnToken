package net.ultimporks.resptoken.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
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
import net.ultimporks.resptoken.data.PlayerInfoManager;
import net.ultimporks.resptoken.events.PlayerRespawnTeleporter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

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
        if (PlayerRespawnTeleporter.shouldTeleportOnRespawn(playerUUID)) {
            if (PlayerInfoManager.hasPlayerInfo(playerUUID) && !PlayerRespawnTeleporter.waitingToTeleport.containsKey(playerUUID)) {
                int seconds = PlayerRespawnTeleporter.TELEPORT_TIMER;
                int ticks = seconds * 20;
                PlayerRespawnTeleporter.waitingToTeleport.put(playerUUID, pLevel.getGameTime() + ticks);
                if (!pLevel.isClientSide) {
                    pContext.getItemInHand().hurtAndBreak(1, ((ServerLevel) pLevel), ((ServerPlayer) pContext.getPlayer()),
                            item -> pContext.getPlayer().onEquippedItemBroken(item, EquipmentSlot.MAINHAND));
                }
                return InteractionResult.SUCCESS;
            }
        }
        return super.useOn(pContext);
    }


    @Override
    public int damageItem(ItemStack stack, int damage, ServerLevel level, @Nullable ServerPlayer player, Consumer<Item> onBroken) {
        return super.damageItem(stack, damage, level, player, onBroken);
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipcomponents, TooltipFlag pTooltipFlag) {
        pTooltipcomponents.add(Component.translatable("tooltip.resptoken.respawn_token").withStyle(ChatFormatting.BOLD));
        pTooltipcomponents.add(Component.translatable("tooltip.resptoken.more_info").withStyle(ChatFormatting.BOLD, ChatFormatting.GRAY));
        if (Screen.hasShiftDown()) {
            pTooltipcomponents.add(Component.translatable("tooltip.resptoken.respawn_token_info").withStyle(ChatFormatting.BOLD, ChatFormatting.GOLD));
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

}
