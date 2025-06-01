package net.ultimporks.resptoken.init;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.ultimporks.resptoken.Reference;
import net.ultimporks.resptoken.item.RespawnTokenItem;
import net.ultimporks.resptoken.item.BindingGlueItem;
import net.ultimporks.resptoken.item.TokenPartItem;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Reference.MOD_ID);

    public static final DeferredItem<Item> RESPAWN_TOKEN = ITEMS.register("respawn_token",
            () -> new RespawnTokenItem(new Item.Properties()
                    .stacksTo(1)
                    .durability(8)
                    .rarity(Rarity.EPIC)));

    public static final DeferredItem<Item> TOKEN_PART = ITEMS.register("token_part",
            () -> new TokenPartItem(new Item.Properties()
                    .stacksTo(64)
                    .rarity(Rarity.UNCOMMON)));

    public static final DeferredItem<Item> BINDING_GLUE = ITEMS.register("binding_glue",
            () -> new BindingGlueItem(new Item.Properties()
                    .stacksTo(1)
                    .durability(8)));


    public static void registerItems(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }


}
