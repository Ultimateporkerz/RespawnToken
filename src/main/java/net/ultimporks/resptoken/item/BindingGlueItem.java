package net.ultimporks.resptoken.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.ultimporks.resptoken.configs.ModConfigs;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BindingGlueItem extends Item {
    private static final int MAX_DAMAGE = 8;

    public BindingGlueItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltip.resptoken.binding_glue"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        if (ModConfigs.COMMON.bindingGlueMaxDamage.get() != -1) {
            return ModConfigs.COMMON.bindingGlueMaxDamage.get();
        }
        return MAX_DAMAGE;
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        if (!isDamageable(itemStack)) {
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
