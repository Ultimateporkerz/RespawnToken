package net.ultimporks.resptoken.events;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.ultimporks.resptoken.Reference;
import net.ultimporks.resptoken.init.ModBlockEntities;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        BlockEntityRenderers.register(
                ModBlockEntities.DEATH_CHEST_BE.get(),
                ChestRenderer::new);
    }


}
