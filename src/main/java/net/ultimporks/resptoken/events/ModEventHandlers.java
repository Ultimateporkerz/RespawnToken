package net.ultimporks.resptoken.events;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.ultimporks.resptoken.configs.ModConfigs;
import net.ultimporks.resptoken.Reference;
import net.ultimporks.resptoken.RespawnToken;
import net.ultimporks.resptoken.commands.LastDeathCommand;
import net.ultimporks.resptoken.configs.PlayerDataManager;
import net.ultimporks.resptoken.data.PlayerInfoManager;
import net.ultimporks.resptoken.init.ModItems;
import net.ultimporks.resptoken.init.ModMessages;
import net.ultimporks.resptoken.network.S2CMessageRespawnTeleport;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID)
public class ModEventHandlers {

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();
        UUID playerUUID = player.getUUID();

        if (PlayerRespawnTeleporter.invulnerablePlayers.containsKey(playerUUID)) {
            PlayerRespawnTeleporter.invulnerablePlayers.remove(playerUUID);
        }
        player.setInvulnerable(false);
        player.setInvisible(false);
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.getEntity().level().isClientSide) {
            ServerPlayer player = (ServerPlayer) event.getEntity();
            String playerName = player.getDisplayName().getString();

            if (ModConfigs.COMMON.firstTimePlayers.get() && !PlayerDataManager.isPlayerKnown(playerName)) {
                RespawnToken.LOGGING("(ModEventHandlers) - New Player Detected!");
                PlayerDataManager.addPlayer(playerName);
                PlayerDataManager.savePlayerData(player.getServer());
                ItemStack respawnToken = new ItemStack(ModItems.RESPAWN_TOKEN.get());
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
        ServerLevel currentLevel = (ServerLevel) player.level();
        long currentTime = currentLevel.getGameTime();
        if (PlayerInfoManager.hasDeathInfo(player.getUUID())) {
            boolean didDie = PlayerInfoManager.didPlayerDie(player.getUUID());
            if (didDie) {
                if (PlayerRespawnTeleporter.shouldTeleportOnRespawn(player.getUUID())) {
                    UUID playerUUID = player.getUUID();
                    if (PlayerInfoManager.hasPlayerInfo(playerUUID)) {

                        // Start teleport Timer
                        int seconds = PlayerRespawnTeleporter.TELEPORT_TIMER;
                        int ticks = seconds * 20;
                        PlayerRespawnTeleporter.waitingToTeleport.put(playerUUID, currentTime + ticks);

                        ModMessages.getPlayChannel().sendToPlayer(() -> (ServerPlayer) player,
                                new S2CMessageRespawnTeleport(playerUUID, currentTime + ticks));
                    }

                    PlayerInfoManager.removeDidPlayerDie(playerUUID);
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
    public static void onCommandsRegister(@NotNull RegisterCommandsEvent event) {
        new LastDeathCommand(event.getDispatcher());
    }

    @SubscribeEvent
    public static void serverStartedEvent(ServerStartedEvent event) {
        PlayerDataManager.loadPlayerData(event.getServer());
    }

    @SubscribeEvent
    public static void addCustomTrades(WandererTradesEvent event) {
        List<VillagerTrades.ItemListing> genericTrades = event.getGenericTrades();
        List<VillagerTrades.ItemListing> rareTrades = event.getRareTrades();

        rareTrades.add((pTrader, pRandom) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 25),
                new ItemStack(ModItems.RESPAWN_TOKEN.get(), 1),
                2, 12, 0.15f)
        );

        genericTrades.add(((pTrader, pRandom) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 10),
                new ItemStack(ModItems.TOKEN_PART.get(), 1),
                4, 6, 0.15f)
        ));



    }




}
