package net.ultimporks.resptoken;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.ultimporks.resptoken.configs.ModConfigs;
import net.ultimporks.resptoken.events.ClientModEvents;
import net.ultimporks.resptoken.init.*;
import net.ultimporks.resptoken.network.NetworkHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Reference.MOD_ID)
public class RespawnToken {
    private static final Logger LOGGER = LogManager.getLogger();

    public RespawnToken(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, ModConfigs.COMMON_SPEC);

        ModItems.registerItems(modEventBus);
        ModCreativeTabs.registerCreativeTab(modEventBus);
        ModLootModifiers.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModDataComponents.register(modEventBus);

        modEventBus.addListener(NetworkHandler::resgister);
        modEventBus.addListener(ClientModEvents::onClientSetup);
    }

    public static void LOGGING(String message) {
        if (ModConfigs.COMMON.enableDebugging.get()) {
            LOGGER.info("RespawnToken LOGGER {}", message);
        }
    }

}
