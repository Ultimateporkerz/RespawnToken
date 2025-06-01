package net.ultimporks.resptoken.data;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

public class PlayerInfo {
    private final int x;
    private final int y;
    private final int z;
    private final ServerLevel deathLevel;

    // This class will store the Player Death location

    public PlayerInfo(Player player) {
        this.x = (int) player.getX();
        this.y = (int) player.getY();
        this.z = (int) player.getZ();
        this.deathLevel = (ServerLevel) player.level();
    }

    public int getDeathXPos() {
        return x;
    }

    public int getDeathYPos() {
        return y;
    }

    public int getDeathZPos() {
        return z;
    }

    public ServerLevel getPlayerDeathLevel() {
        return deathLevel;
    }

    public String getDimensionName() {
        return deathLevel.dimension().location().toString();
    }

}
