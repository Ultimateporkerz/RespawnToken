package net.ultimporks.resptoken.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import net.ultimporks.resptoken.init.ModItems;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries) {
        super(pOutput, pRegistries);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput pRecipeOutput) {

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.RESPAWN_TOKEN.get())
                .pattern("   ")
                .pattern("101")
                .pattern("   ")
                .define('0', ModItems.BINDING_GLUE.get())
                .define('1', ModItems.TOKEN_PART.get())
                .unlockedBy(getHasName(ModItems.BINDING_GLUE.get()), has(ModItems.BINDING_GLUE.get()))
                .save(pRecipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.BINDING_GLUE.get(), 1)
                .requires(Items.STICK)
                .requires(Items.HONEY_BOTTLE)
                .requires(Items.SLIME_BALL)
                .unlockedBy(getHasName(Items.HONEY_BOTTLE), has(Items.HONEY_BOTTLE))
                .save(pRecipeOutput);

    }
}
