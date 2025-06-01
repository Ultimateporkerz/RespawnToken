package net.ultimporks.resptoken.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;
import net.ultimporks.resptoken.Reference;
import net.ultimporks.resptoken.init.ModItems;
import net.ultimporks.resptoken.loot.AddItemModifier;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModGlobalLootModifiersGenerator extends GlobalLootModifierProvider {

    public ModGlobalLootModifiersGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, Reference.MOD_ID, registries);
    }

    @Override
    protected void start(HolderLookup.@NotNull Provider provider) {

        // Woodland Mansion
        add("respawn_token_woodland_mansion", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft", "chests/woodland_mansion")).build(),
                LootItemRandomChanceCondition.randomChance(1.00f).build()}, ModItems.RESPAWN_TOKEN.get()));

        add("token_part_woodland_mansion", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/woodland_mansion")).build(),
                LootItemRandomChanceCondition.randomChance(1.00f).build()}, ModItems.TOKEN_PART.get()));



        // End City Treasure
        add("respawn_token_end_city_treasure", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/end_city_treasure")).build(),
                LootItemRandomChanceCondition.randomChance(0.04f).build()}, ModItems.RESPAWN_TOKEN.get()));

        add("token_part_end_city_treasure", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/end_city_treasure")).build(),
                LootItemRandomChanceCondition.randomChance(0.20f).build()}, ModItems.TOKEN_PART.get()));



        // Abandoned Mineshaft
        add("respawn_token_abandoned_mineshaft", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/abandoned_mineshaft")).build(),
                LootItemRandomChanceCondition.randomChance(0.04f).build()}, ModItems.RESPAWN_TOKEN.get()));

        add("token_part_abandoned_mineshaft", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/abandoned_mineshaft")).build(),
                LootItemRandomChanceCondition.randomChance(0.20f).build()}, ModItems.TOKEN_PART.get()));



        // Ancient City
        add("respawn_token_ancient_city", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/ancient_city")).build(),
                LootItemRandomChanceCondition.randomChance(0.04f).build()}, ModItems.RESPAWN_TOKEN.get()));

        add("token_part_ancient_city", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/ancient_city")).build(),
                LootItemRandomChanceCondition.randomChance(0.20f).build()}, ModItems.TOKEN_PART.get()));



        // Bastion Bridge
        add("respawn_token_bastion_bridge", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/bastion_bridge")).build(),
                LootItemRandomChanceCondition.randomChance(0.04f).build()}, ModItems.RESPAWN_TOKEN.get()));

        add("token_part_bastion_bridge", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/bastion_bridge")).build(),
                LootItemRandomChanceCondition.randomChance(0.20f).build()}, ModItems.TOKEN_PART.get()));



        // Bastion Hoglin Stable
        add("respawn_token_bastion_hoglin_stable", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/bastion_hoglin_stable")).build(),
                LootItemRandomChanceCondition.randomChance(0.04f).build()}, ModItems.RESPAWN_TOKEN.get()));

        add("token_part_bastion_hoglin_stable", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/bastion_hoglin_stable")).build(),
                LootItemRandomChanceCondition.randomChance(0.20f).build()}, ModItems.TOKEN_PART.get()));



        // Bastion Other
        add("respawn_token_bastion_other", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/bastion_other")).build(),
                LootItemRandomChanceCondition.randomChance(0.04f).build()}, ModItems.RESPAWN_TOKEN.get()));

        add("token_part_bastion_other", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/bastion_other")).build(),
                LootItemRandomChanceCondition.randomChance(0.20f).build()}, ModItems.TOKEN_PART.get()));



        // Bastion Treasure
        add("respawn_token_bastion_treasure", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/bastion_treasure")).build(),
                LootItemRandomChanceCondition.randomChance(0.04f).build()}, ModItems.RESPAWN_TOKEN.get()));

        add("token_part_bastion_treasure", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/bastion_treasure")).build(),
                LootItemRandomChanceCondition.randomChance(0.20f).build()}, ModItems.TOKEN_PART.get()));



        // Buried Treasure
        add("respawn_token_buried_treasure", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/buried_treasure")).build(),
                LootItemRandomChanceCondition.randomChance(0.04f).build()}, ModItems.RESPAWN_TOKEN.get()));

        add("token_part_buried_treasure", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/buried_treasure")).build(),
                LootItemRandomChanceCondition.randomChance(0.20f).build()}, ModItems.TOKEN_PART.get()));


        // Igloo Chest
        add("respawn_token_igloo_chest", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/igloo_chest")).build(),
                LootItemRandomChanceCondition.randomChance(0.04f).build()}, ModItems.RESPAWN_TOKEN.get()));

        add("token_part_igloo_chest", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/igloo_chest")).build(),
                LootItemRandomChanceCondition.randomChance(0.20f).build()}, ModItems.TOKEN_PART.get()));



        // Desert Pyramid
        add("respawn_token_desert_pyramid", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/desert_pyramid")).build(),
                LootItemRandomChanceCondition.randomChance(0.04f).build()}, ModItems.RESPAWN_TOKEN.get()));

        add("token_part_desert_pyramid", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/desert_pyramid")).build(),
                LootItemRandomChanceCondition.randomChance(0.20f).build()}, ModItems.TOKEN_PART.get()));


        // Jungle Temple
        add("respawn_token_jungle_temple", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/jungle_temple")).build(),
                LootItemRandomChanceCondition.randomChance(0.04f).build()}, ModItems.RESPAWN_TOKEN.get()));

        add("token_part_jungle_temple", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/jungle_temple")).build(),
                LootItemRandomChanceCondition.randomChance(0.20f).build()}, ModItems.TOKEN_PART.get()));



        // Nether Bridge
        add("respawn_token_nether_bridge", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/nether_bridge")).build(),
                LootItemRandomChanceCondition.randomChance(0.04f).build()}, ModItems.RESPAWN_TOKEN.get()));

        add("token_part_nether_bridge", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/nether_bridge")).build(),
                LootItemRandomChanceCondition.randomChance(0.20f).build()}, ModItems.TOKEN_PART.get()));



        // Pillager Outpost
        add("respawn_token_pillager_outpost", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/pillager_outpost")).build(),
                LootItemRandomChanceCondition.randomChance(0.04f).build()}, ModItems.RESPAWN_TOKEN.get()));

        add("token_part_pillager_outpost", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/pillager_outpost")).build(),
                LootItemRandomChanceCondition.randomChance(0.20f).build()}, ModItems.TOKEN_PART.get()));



        // Ruined Portal
        add("respawn_token_ruined_portal", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/ruined_portal")).build(),
                LootItemRandomChanceCondition.randomChance(0.04f).build()}, ModItems.RESPAWN_TOKEN.get()));

        add("token_part_ruined_portal", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/ruined_portal")).build(),
                LootItemRandomChanceCondition.randomChance(0.20f).build()}, ModItems.TOKEN_PART.get()));



        // Shipwreck Treasure
        add("respawn_token_shipwreck_treasure", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/shipwreck_treasure")).build(),
                LootItemRandomChanceCondition.randomChance(0.04f).build()}, ModItems.RESPAWN_TOKEN.get()));

        add("token_part_shipwreck_treasure", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/shipwreck_treasure")).build(),
                LootItemRandomChanceCondition.randomChance(0.20f).build()}, ModItems.TOKEN_PART.get()));



        // Shipwreck Supply
        add("respawn_token_shipwreck_supply", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/shipwreck_supply")).build(),
                LootItemRandomChanceCondition.randomChance(0.04f).build()}, ModItems.RESPAWN_TOKEN.get()));

        add("token_part_shipwreck_supply", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/shipwreck_supply")).build(),
                LootItemRandomChanceCondition.randomChance(0.20f).build()}, ModItems.TOKEN_PART.get()));



        // Spawn bonus chest
        add("respawn_token_spawn_bonus_chest", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/spawn_bonus_chest")).build(),
                LootItemRandomChanceCondition.randomChance(0.04f).build()}, ModItems.RESPAWN_TOKEN.get()));

        add("token_part_spawn_bonus_chest", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/spawn_bonus_chest")).build(),
                LootItemRandomChanceCondition.randomChance(0.20f).build()}, ModItems.TOKEN_PART.get()));



        // Simple Dungeon
        add("respawn_token_simple_dungeon", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/simple_dungeon")).build(),
                LootItemRandomChanceCondition.randomChance(0.04f).build()}, ModItems.RESPAWN_TOKEN.get()));

        add("token_part_simple_dungeon", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/simple_dungeon")).build(),
                LootItemRandomChanceCondition.randomChance(0.20f).build()}, ModItems.TOKEN_PART.get()));



        // Stronghold Corridor
        add("respawn_token_stronghold_corridor", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/stronghold_corridor")).build(),
                LootItemRandomChanceCondition.randomChance(0.04f).build()}, ModItems.RESPAWN_TOKEN.get()));

        add("token_part_stronghold_corridor", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/stronghold_corridor")).build(),
                LootItemRandomChanceCondition.randomChance(0.20f).build()}, ModItems.TOKEN_PART.get()));



        // Stronghold Crossing
        add("respawn_token_stronghold_crossing", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/stronghold_crossing")).build(),
                LootItemRandomChanceCondition.randomChance(0.04f).build()}, ModItems.RESPAWN_TOKEN.get()));

        add("token_part_stronghold_crossing", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/stronghold_crossing")).build(),
                LootItemRandomChanceCondition.randomChance(0.20f).build()}, ModItems.TOKEN_PART.get()));



        // Stronghold Library
        add("respawn_token_stronghold_library", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/stronghold_library")).build(),
                LootItemRandomChanceCondition.randomChance(0.04f).build()}, ModItems.RESPAWN_TOKEN.get()));

        add("token_part_stronghold_library", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/stronghold_library")).build(),
                LootItemRandomChanceCondition.randomChance(0.20f).build()}, ModItems.TOKEN_PART.get()));



        // Underwater Ruin Big
        add("respawn_token_underwater_ruin_big", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/underwater_ruin_big")).build(),
                LootItemRandomChanceCondition.randomChance(0.04f).build()}, ModItems.RESPAWN_TOKEN.get()));

        add("token_part_underwater_ruin_big", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/underwater_ruin_big")).build(),
                LootItemRandomChanceCondition.randomChance(0.20f).build()}, ModItems.TOKEN_PART.get()));



        // Underwater Ruin Small
        add("respawn_token_underwater_ruin_small", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/underwater_ruin_small")).build(),
                LootItemRandomChanceCondition.randomChance(0.04f).build()}, ModItems.RESPAWN_TOKEN.get()));

        add("token_part_underwater_ruin_small", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/underwater_ruin_small")).build(),
                LootItemRandomChanceCondition.randomChance(0.20f).build()}, ModItems.TOKEN_PART.get()));



        // Village Tool Smith
        add("respawn_token_village_toolsmith", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/village/village_toolsmith")).build(),
                LootItemRandomChanceCondition.randomChance(0.10f).build()}, ModItems.RESPAWN_TOKEN.get()));

        add("token_part_village_toolsmith", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("minecraft","chests/village/village_toolsmith")).build(),
                LootItemRandomChanceCondition.randomChance(0.20f).build()}, ModItems.TOKEN_PART.get()));
    }
}
