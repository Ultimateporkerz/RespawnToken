package net.ultimporks.resptoken.events;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.ultimporks.resptoken.init.ModBlockEntities;

public class ClientModEvents {

    public static void onClientSetup(FMLClientSetupEvent event) {
        BlockEntityRenderers.register(
                ModBlockEntities.DEATH_CHEST_BE.get(),
                ChestRenderer::new);
    }


}
