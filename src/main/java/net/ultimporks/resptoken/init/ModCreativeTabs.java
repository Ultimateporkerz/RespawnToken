package net.ultimporks.resptoken.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.ultimporks.resptoken.Reference;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Reference.MOD_ID);

    public static final RegistryObject<CreativeModeTab> RESPAWN_TOKEN = CREATIVE_MODE_TABS.register("respawn_token",
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
