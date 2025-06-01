package net.ultimporks.resptoken.configs;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ModConfigs {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec COMMON_SPEC;

    public static final Common COMMON;

    static {
        COMMON = new Common(BUILDER);
        COMMON_SPEC = BUILDER.build();
    }

    public static class Common {
        // Logging settings
        public final ModConfigSpec.BooleanValue enableDebugging;
        // Player Settings
        public final ModConfigSpec.BooleanValue firstTimePlayers;
        public final ModConfigSpec.IntValue teleportCountdown;
        public final ModConfigSpec.BooleanValue invulnerabilityEnabled;
        public final ModConfigSpec.IntValue invulnerabilityCountdown;
        public final ModConfigSpec.BooleanValue lavaDeathExtendInvulnerability;
        public final ModConfigSpec.IntValue lavaDeathExtensionAmount;
        public final ModConfigSpec.BooleanValue enableDeathChest;

        public Common(ModConfigSpec.Builder builder) {
            builder.comment("General Settings").push("general");
            enableDebugging = builder
                    .comment("Should debugging be enabled? (Default false)")
                    .define("enableDebugging", false);
            // Player Settings
            firstTimePlayers = builder
                    .comment("Should players joining for the first time be given a respawn token? (default true)")
                    .define("enableFirstTimeJoin", true);

            teleportCountdown = builder
                    .comment("The amount of time it takes to teleport a player to their last death location when respawning. (Default 8)")
                    .defineInRange("teleportCountdown", 8, 0, Integer.MAX_VALUE);

            invulnerabilityEnabled = builder
                    .comment("Should the player be invulnerable when teleporting back to their death location?")
                    .define("invulnerabilityEnabled", true);

            invulnerabilityCountdown = builder
                    .comment("How long the player is invulnerable for when teleporting back to their death location upon respawn. (default 16)")
                    .defineInRange("invulnerabilityCountdown", 16, 0, Integer.MAX_VALUE);

            lavaDeathExtendInvulnerability = builder
                    .comment("If a player dies in lava, should it extend their invulnerability?")
                    .define("lavaDeathExtendInvulnerability", true);

            lavaDeathExtensionAmount = builder
                    .comment("How much should it extend it by? This will add on top of the currently set countdown. (default 60)")
                    .defineInRange("lavaDeathExtensionAmount", 60, 0, Integer.MAX_VALUE);

            enableDeathChest = builder
                    .comment("Should death chests be enabled? This will save the players inventory in a chest upon death. (default true)")
                    .define("enableDeathChest", true);
            builder.pop();
        }
    }


}
