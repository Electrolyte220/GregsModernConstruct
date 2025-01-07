package dev.electrolyte.expandedtic;

import com.gregtechceu.gtceu.api.addon.GTAddon;
import com.gregtechceu.gtceu.api.addon.IGTAddon;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;
import dev.electrolyte.expandedtic.recipes.ETRecipes;
import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

@GTAddon
public class ExpandedTiCAddon implements IGTAddon {
    @Override
    public GTRegistrate getRegistrate() {
        return ExpandedTiC.REGISTRATE;
    }

    @Override
    public void initializeAddon() {
        ExpandedTiC.LOGGER.info("Initialized ExpandedTiC GT Addon.");
    }

    @Override
    public String addonModId() {
        return ExpandedTiC.MOD_ID;
    }

    @Override
    public void addRecipes(Consumer<FinishedRecipe> provider) {
        ETRecipes.register(provider);
    }
}
