package net.ultimporks.resptoken.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.ultimporks.resptoken.RespawnToken;
import net.ultimporks.resptoken.configs.ModConfigs;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class RespawnTokenItem extends Item {
    public RespawnTokenItem(Properties properties) {
        super(properties);
    }

    private static final int MAX_DAMAGE = 8;


    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipcomponents, TooltipFlag pTooltipFlag) {
        pTooltipcomponents.add(Component.translatable("tooltip.resptoken.respawn_token").withStyle(ChatFormatting.BOLD, ChatFormatting.YELLOW));
        super.appendHoverText(pStack, pContext, pTooltipcomponents, pTooltipFlag);
    }

   /* public static void damageRespawnToken(ItemStack itemStack, LivingEntity livingEntity) {
        if (livingEntity instanceof Player player) {
            itemStack.hurtAndBreak(1, player, (p_289501_) -> {
                p_289501_.broadcastBreakEvent(player.getUsedItemHand());
                RespawnToken.LOGGING("Respawn Token has been damaged!");
            });
        }
    }
    */

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round(13.0F - (13.0F * stack.getDamageValue() / stack.getMaxDamage()));
    }

    @Override
    public int getBarColor(ItemStack pStack) {
        return 0xFFD700;
    }

    @Override
    public boolean isEnchantable(ItemStack pStack) {
        return true;
    }

}
