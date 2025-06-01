package net.ultimporks.resptoken.events;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.ultimporks.resptoken.configs.ModConfigs;
import net.ultimporks.resptoken.Reference;
import net.ultimporks.resptoken.RespawnToken;
import net.ultimporks.resptoken.data.PlayerDataSavedData;
import net.ultimporks.resptoken.data.PlayerInfoManager;
import net.ultimporks.resptoken.init.ModItems;
import net.ultimporks.resptoken.item.RespawnTokenItem;

import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class ModEventHandlers {

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity().level().isClientSide) return;
        ServerPlayer player = (ServerPlayer) event.getEntity();
        UUID playerUUID = player.getUUID();
        PlayerRespawnTeleporter.invulnerablePlayers.remove(playerUUID);
        player.setInvulnerable(false);
        player.setInvisible(false);
    }

    @SubscribeEvent
    public static void onItemPickup(EntityItemPickupEvent event) {
        if (event.getEntity().level().isClientSide) return;
        Player player = event.getEntity();
        ItemStack pickedUp = event.getItem().getItem();

        if (pickedUp.getItem() instanceof RespawnTokenItem) {
            for (ItemStack stack : player.getInventory().items) {
                if (stack.getItem() instanceof RespawnTokenItem) {
                    event.setCanceled(true);
                    return;
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity().level().isClientSide) return;
        ServerPlayer player = (ServerPlayer) event.getEntity();
        UUID playerUUID = player.getUUID();
        PlayerDataSavedData data = PlayerDataSavedData.get(player.getServer());
        if (ModConfigs.COMMON.firstTimePlayers.get() && !data.isPlayerKnown(playerUUID)) {
            RespawnToken.LOGGING("(ModEventHandlers) - New Player Detected!");
            data.addPlayer(playerUUID);
            ItemStack respawnToken = new ItemStack(ModItems.RESPAWN_TOKEN.get());
            player.getInventory().add(respawnToken);
        }
    }

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (event.getEntity().level().isClientSide) return;
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        PlayerInfoManager.addDidPlayerDie(player.getUUID(), true);

        boolean isInLava = event.getEntity().isInLava();
        boolean isInVoid = event.getSource().is(DamageTypes.FELL_OUT_OF_WORLD) && player.level().dimension().equals(ServerLevel.END);
        boolean isDeathChestEnabled = ModConfigs.COMMON.enableDeathChest.get();

        PlayerRespawnHandler.checkInventory(player, isInLava, isInVoid, isDeathChestEnabled);
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity().level().isClientSide) return;
        Player player = event.getEntity();
        UUID playerUUID = player.getUUID();
        if (PlayerInfoManager.hasDeathInfo(playerUUID) && PlayerInfoManager.didPlayerDie(playerUUID)) {
            if (PlayerRespawnHandler.tokenStorage.containsKey(playerUUID)) {
                List<ItemStack> tokens = PlayerRespawnHandler.tokenStorage.remove(playerUUID);

                for (ItemStack token : tokens) {
                    player.getInventory().add(token);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.player.level().isClientSide) return;
        Player player = event.player;
        UUID playerId = player.getUUID();
        Level level = player.level();
        if (event.phase == TickEvent.Phase.START) {
            long currentTime = player.level().getGameTime();
            if (PlayerRespawnTeleporter.invulnerablePlayers.containsKey(playerId)) {
                long endTime = PlayerRespawnTeleporter.invulnerablePlayers.get(playerId);

                if (currentTime >= endTime) {
                    // Timer expired
                    player.setInvulnerable(false);
                    PlayerRespawnTeleporter.invulnerablePlayers.remove(playerId);

                }
            }
            if (PlayerRespawnTeleporter.waitingToTeleport.containsKey(playerId)) {
                long endTime = PlayerRespawnTeleporter.waitingToTeleport.get(playerId);

                if (currentTime >= endTime) {
                    // Timer expired
                    PlayerRespawnTeleporter.teleportPlayer(player);
                    PlayerRespawnTeleporter.waitingToTeleport.remove(playerId);
                    PlayerRespawnTeleporter.shouldTeleportOnRespawn.remove(playerId);
                }
            }
        }
        if (event.phase == TickEvent.Phase.END && level.dimension().equals(ServerLevel.END)) {
            // Periodically save playerPos for safe respawns when player is in the end
            if (player.tickCount % 60 == 0) {
                BlockPos currentEndPosition = player.getOnPos();
                BlockPos currentEndPositionAdded = currentEndPosition.above();
                PlayerRespawnTeleporter.addSafeBlockPosition(playerId, currentEndPositionAdded, level);
            }
        }


    }

    @SubscribeEvent
    public static void addCustomTrades(WandererTradesEvent event) {
        List<VillagerTrades.ItemListing> genericTrades = event.getGenericTrades();
        List<VillagerTrades.ItemListing> rareTrades = event.getRareTrades();

        rareTrades.add((pTrader, pRandom) -> new MerchantOffer(
                new ItemCost(Items.EMERALD, 25),
                new ItemStack(ModItems.RESPAWN_TOKEN.get(), 1),
                2, 12, 0.15f)
        );

        genericTrades.add(((pTrader, pRandom) -> new MerchantOffer(
                new ItemCost(Items.EMERALD, 10),
                new ItemStack(ModItems.TOKEN_PART.get(), 1),
                4, 6, 0.15f)
        ));



    }

}
