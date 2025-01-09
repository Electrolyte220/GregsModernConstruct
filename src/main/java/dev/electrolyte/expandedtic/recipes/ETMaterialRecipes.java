package dev.electrolyte.expandedtic.recipes;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import dev.electrolyte.expandedtic.ExpandedTiC;
import dev.electrolyte.expandedtic.helper.GTMaterialHelper;
import net.minecraft.data.recipes.FinishedRecipe;
import slimeknights.tconstruct.library.data.recipe.IMaterialRecipeHelper;

import java.util.function.Consumer;

public class ETMaterialRecipes implements IMaterialRecipeHelper {

    public ETMaterialRecipes(Consumer<FinishedRecipe> provider) {
        register(provider);
    }

    public void register(Consumer<FinishedRecipe> provider) {
        for(Material material : GTMaterialHelper.getRegisteredMaterials()) {
            metalMaterialRecipe(provider, ExpandedTiC.materialId(material.getName()), "tools/materials/", material.getName(), true);
        }
    }

    @Override
    public String getModId() {
        return ExpandedTiC.MOD_ID;
    }
}
