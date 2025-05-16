package net.ultimporks.resptoken.init;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.ultimporks.resptoken.Reference;
import net.ultimporks.resptoken.item.RespawnTokenItem;
import net.ultimporks.resptoken.item.BindingGlueItem;
import net.ultimporks.resptoken.item.TokenPartItem;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Reference.MOD_ID);

    // Respawn Token
    public static final RegistryObject<Item> RESPAWN_TOKEN = ITEMS.register("respawn_token",
            () -> new RespawnTokenItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

    // Crafting Items
    public static final RegistryObject<Item> TOKEN_PART = ITEMS.register("token_part",
            () -> new TokenPartItem(new Item.Properties().stacksTo(64).rarity(Rarity.UNCOMMON)));

    public static final RegistryObject<Item> BINDING_GLUE = ITEMS.register("binding_glue",
            () -> new BindingGlueItem(new Item.Properties().stacksTo(1)));

    public static void registerItems(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }


}
