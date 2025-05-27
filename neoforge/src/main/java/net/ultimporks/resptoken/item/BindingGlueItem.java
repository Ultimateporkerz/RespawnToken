package net.ultimporks.resptoken.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.ultimporks.resptoken.configs.ModConfigs;

import java.util.List;

public class BindingGlueItem extends Item {
    public BindingGlueItem(Properties pProperties) {
        super(pProperties);
    }


    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        pTooltipComponents.add(Component.translatable("tooltip.resptoken.binding_glue"));
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
    }

    @Override
    public void onCraftedBy(ItemStack pStack, Level pLevel, Player pPlayer) {
        super.onCraftedBy(pStack, pLevel, pPlayer);
        if (ModConfigs.COMMON.bindingGlueMaxDamage.get() != 1) {
            pStack.set(DataComponents.MAX_DAMAGE, ModConfigs.COMMON.bindingGlueMaxDamage.get());
            pStack.set(DataComponents.DAMAGE, 0);
        }
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        ItemStack damagedItem = itemStack.copy();
        int newDamage = damagedItem.getDamageValue() + 1;
        if (newDamage >= damagedItem.getMaxDamage()) {
            return ItemStack.EMPTY;
        }
        damagedItem.setDamageValue(newDamage);
        return damagedItem;
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isBarVisible(ItemStack pStack) {
        return pStack.getDamageValue() > 0;
    }


}
