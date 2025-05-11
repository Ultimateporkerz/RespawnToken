package net.ultimporks.resptoken.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.ultimporks.resptoken.Reference;
import net.ultimporks.resptoken.data.PlayerInfoManager;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class LastDeathCommand {

    public LastDeathCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        registerDeathCommand(dispatcher);
    }

    private void registerDeathCommand(@NotNull CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("lastdeath")
                .executes(commandContext -> lastDeath(commandContext.getSource())));

        dispatcher.register(Commands.literal("resptokenversion")
                .executes(commandContext -> versionCheck(commandContext.getSource())));
    }

    public int lastDeath(@NotNull CommandSourceStack stack) throws CommandSyntaxException {
        ServerPlayer player = stack.getPlayerOrException();
        UUID playerUUID = player.getUUID();

        if (PlayerInfoManager.hasPlayerInfo(playerUUID)) {
            int xCord = PlayerInfoManager.getPlayerDeathInfo(playerUUID).getDeathXPos();
            int yCord = PlayerInfoManager.getPlayerDeathInfo(playerUUID).getDeathYPos();
            int zCord = PlayerInfoManager.getPlayerDeathInfo(playerUUID).getDeathZPos();
            String dimensionName = PlayerInfoManager.getPlayerDeathInfo(playerUUID).getDimensionName().replace("minecraft:", "");

            player.sendSystemMessage(Component.literal("Death Coordinates: " + xCord + " " + yCord + " " + zCord + " Dimension: " + dimensionName).withStyle(ChatFormatting.BOLD, ChatFormatting.YELLOW));
            return 1;
        } else {
            player.sendSystemMessage(Component.literal("No death location has been stored!").withStyle(ChatFormatting.RED));
            return 0;
        }
    }

    public int versionCheck(@NotNull CommandSourceStack stack) throws CommandSyntaxException {
        ServerPlayer player = stack.getPlayerOrException();
        player.sendSystemMessage(Component.literal("RespawnToken Version is " + Reference.MOD_VERSION));
        return 1;
    }


}
