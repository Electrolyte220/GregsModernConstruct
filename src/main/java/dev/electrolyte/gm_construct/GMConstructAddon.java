package dev.electrolyte.gm_construct;

import com.gregtechceu.gtceu.api.addon.GTAddon;
import com.gregtechceu.gtceu.api.addon.IGTAddon;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;
import dev.electrolyte.gm_construct.recipes.GMCRecipes;
import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

@GTAddon
public class GMConstructAddon implements IGTAddon {
    @Override
    public GTRegistrate getRegistrate() {
        return GMConstruct.REGISTRATE;
    }

    @Override
    public void initializeAddon() {
        GMConstruct.LOGGER.info("Initialized GregTech Material Construct Addon.");
    }

    @Override
    public String addonModId() {
        return GMConstruct.MOD_ID;
    }

    @Override
    public void addRecipes(Consumer<FinishedRecipe> provider) {
        GMCRecipes.register(provider);
    }
}
