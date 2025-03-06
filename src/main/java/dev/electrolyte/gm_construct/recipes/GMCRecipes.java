package dev.electrolyte.gm_construct.recipes;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

public class GMCRecipes {

    public static void register(Consumer<FinishedRecipe> provider) {
        GMCMaterialMachineRecipes.register(provider);
        new GMCMaterialRecipes(provider);
    }
}
