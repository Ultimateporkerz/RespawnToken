package net.ultimporks.resptoken.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.ultimporks.resptoken.configs.ModConfigs;

import java.util.List;

public class BindingGlueItem extends Item {
    private static final int MAX_DAMAGE = 8;

    public BindingGlueItem(Properties pProperties) {
        super(pProperties);
    }


    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        pTooltipComponents.add(Component.translatable("tooltip.resptoken.binding_glue"));
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
    }

    public static int getMaxDamage() {
        if (ModConfigs.COMMON.glueDurability.get() != -1) {
            return ModConfigs.COMMON.glueDurability.get();
        }
        return MAX_DAMAGE;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        if (ModConfigs.COMMON.glueDurability.get() == -1) {
            return itemStack.copy();
        } else {
            ItemStack damagedItem = itemStack.copy();
            int newDamage = damagedItem.getDamageValue() + 1;
            if (newDamage >= damagedItem.getMaxDamage()) {
                return ItemStack.EMPTY;
            }
            damagedItem.setDamageValue(newDamage);
            return damagedItem;
        }
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
