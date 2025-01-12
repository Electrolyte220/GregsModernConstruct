package dev.electrolyte.gm_construct.recipes;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import dev.electrolyte.gm_construct.GMConstruct;
import dev.electrolyte.gm_construct.helper.GTMaterialHelper;
import net.minecraft.data.recipes.FinishedRecipe;
import slimeknights.tconstruct.library.data.recipe.IMaterialRecipeHelper;

import java.util.function.Consumer;

public class GMCMaterialRecipes implements IMaterialRecipeHelper {

    public GMCMaterialRecipes(Consumer<FinishedRecipe> provider) {
        register(provider);
    }

    public void register(Consumer<FinishedRecipe> provider) {
        for(Material material : GTMaterialHelper.getRegisteredMaterials()) {
            metalMaterialRecipe(provider, GMConstruct.materialId(material.getName()), "tools/materials/", material.getName(), true);
        }
    }

    @Override
    public String getModId() {
        return GMConstruct.MOD_ID;
    }
}
