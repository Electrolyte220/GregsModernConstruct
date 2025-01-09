package dev.electrolyte.expandedtic.recipes;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

public class ETRecipes {

    public static void register(Consumer<FinishedRecipe> provider) {
        ETExtruderRecipes.register(provider);
        new ETMaterialRecipes(provider);
    }
}
