package net.ultimporks.resptoken;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.ultimporks.resptoken.configs.ModConfigs;
import net.ultimporks.resptoken.init.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Reference.MOD_ID)
public class RespawnToken {
    private static final Logger LOGGER = LogManager.getLogger();

    private final ModContainer modContainer;

    public RespawnToken(FMLJavaModLoadingContext context) {
        this.modContainer = context.getContainer();
        IEventBus modEventBus = context.getModEventBus();

        this.registerConfigs();

        ModItems.registerItems(modEventBus);
        ModCreativeTabs.registerCreativeTab(modEventBus);
        ModLootModifiers.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        ModMessages.register();
    }

    private void registerConfigs() {
        ModConfig commonConfig = new ModConfig(
                ModConfig.Type.COMMON,
                ModConfigs.COMMON_SPEC,
                this.modContainer
        );
        this.modContainer.addConfig(commonConfig);
    }

    public static void LOGGING(String message) {
        if (ModConfigs.COMMON.enableDebugging.get()) {
            LOGGER.info("RespawnToken LOGGER {}", message);
        }
    }
}
