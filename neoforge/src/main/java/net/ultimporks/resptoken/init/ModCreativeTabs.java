package net.ultimporks.resptoken.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.ultimporks.resptoken.Reference;

import java.util.function.Supplier;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Reference.MOD_ID);

    public static final Supplier<CreativeModeTab> RESPAWN_TOKEN_TAB = CREATIVE_MODE_TABS.register("respawn_token",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.RESPAWN_TOKEN.get()))
                    .title(Component.translatable("creativetab.resptoken_tab"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.RESPAWN_TOKEN.get());
                        output.accept(ModItems.TOKEN_PART.get());
                        output.accept(ModItems.BINDING_GLUE.get());
                    }) .build());

    public static void registerCreativeTab(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
