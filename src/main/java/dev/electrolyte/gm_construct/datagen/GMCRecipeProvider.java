package dev.electrolyte.gm_construct.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;

import java.util.function.Consumer;

public class GMCRecipeProvider extends RecipeProvider {
    public GMCRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        GMCSmelteryRecipeProvider smelteryRecipes = new GMCSmelteryRecipeProvider();
        smelteryRecipes.buildRecipes(consumer);

        GMCToolsRecipeProvider toolRecipes = new GMCToolsRecipeProvider();
        toolRecipes.buildRecipes(consumer);

        GMCMaterialRecipeProvider materialRecipes = new GMCMaterialRecipeProvider();
        materialRecipes.buildRecipes(consumer);

        GMCModifierRecipeProvider modifierRecipes = new GMCModifierRecipeProvider();
        modifierRecipes.buildRecipes(consumer);
    }
}
