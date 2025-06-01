package net.ultimporks.resptoken.events;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
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
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handlers.ServerPayloadHandler;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.ultimporks.resptoken.configs.ModConfigs;
import net.ultimporks.resptoken.Reference;
import net.ultimporks.resptoken.RespawnToken;
import net.ultimporks.resptoken.data.PlayerDataSavedData;
import net.ultimporks.resptoken.data.PlayerInfoManager;
import net.ultimporks.resptoken.init.ModItems;
import net.ultimporks.resptoken.network.ClientPayloadHandler;
import net.ultimporks.resptoken.network.S2CMessageInvulnerableOverlay;
import net.ultimporks.resptoken.network.S2CMessageRespawnTeleport;

import java.util.List;
import java.util.UUID;

@EventBusSubscriber(modid = Reference.MOD_ID)
public class ModEventHandlers {

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();
        String playerName = player.getName().getString();

        PlayerRespawnTeleporter.invulnerablePlayers.remove(playerName);
        player.setInvulnerable(false);
        player.setInvisible(false);
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.getEntity().level().isClientSide) {
            ServerPlayer player = (ServerPlayer) event.getEntity();
            UUID playerUUID = player.getUUID();
            PlayerDataSavedData data = PlayerDataSavedData.get(player.getServer());

            if (ModConfigs.COMMON.firstTimePlayers.get() && !data.isPlayerKnown(playerUUID)) {
                RespawnToken.LOGGING("(ModEventHandlers) - New Player Detected!");
                data.addPlayer(playerUUID);
                ItemStack respawnToken = new ItemStack(ModItems.RESPAWN_TOKEN.get());
                if (ModConfigs.COMMON.respawnTokenMaxDamage.get() != 1) {
                    respawnToken.set(DataComponents.MAX_DAMAGE, ModConfigs.COMMON.respawnTokenMaxDamage.get());
                    respawnToken.set(DataComponents.DAMAGE, 0);
                }
                player.getInventory().add(respawnToken);
            }
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

        PlayerRespawnHandler.checkInventory(player, isInLava, isInVoid);
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
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
    public static void onPlayerTickPre(PlayerTickEvent.Pre event) {
        if (event.getEntity().level().isClientSide) return;
        Player player = event.getEntity();
        String playerName = player.getName().getString();
        UUID playerId = player.getUUID();
        long currentTime = player.level().getGameTime();
        if (PlayerRespawnTeleporter.invulnerablePlayers.containsKey(playerName)) {
            long endTime = PlayerRespawnTeleporter.invulnerablePlayers.get(playerName);

            if (currentTime >= endTime) {
                // Timer expired
                player.setInvulnerable(false);
                PlayerRespawnTeleporter.invulnerablePlayers.remove(playerName);
            }
        }
        if (PlayerRespawnTeleporter.waitingToTeleport.containsKey(playerName)) {
            long endTime = PlayerRespawnTeleporter.waitingToTeleport.get(playerName);
            if (currentTime >= endTime) {
                // Timer expired
                PlayerRespawnTeleporter.teleportPlayer(player);
                PlayerRespawnTeleporter.waitingToTeleport.remove(playerName);
                PlayerRespawnTeleporter.shouldTeleportOnRespawn.remove(playerId);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTickPost(PlayerTickEvent.Pre event) {
        if (event.getEntity().level().isClientSide) return;
        Player player = event.getEntity();
        UUID playerId = player.getUUID();
        Level level = player.level();
        if (level.dimension().equals(ServerLevel.END)) {
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
