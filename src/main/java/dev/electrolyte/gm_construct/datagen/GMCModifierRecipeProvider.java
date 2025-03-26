package dev.electrolyte.gm_construct.datagen;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Items;
import net.minecraftforge.fluids.FluidStack;
import slimeknights.mantle.recipe.data.IRecipeHelper;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.fluids.TinkerFluids;
import slimeknights.tconstruct.library.recipe.casting.ItemCastingRecipeBuilder;
import slimeknights.tconstruct.shared.TinkerCommons;
import slimeknights.tconstruct.tables.TinkerTables;
import slimeknights.tconstruct.tools.TinkerModifiers;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.common.data.GTMaterials.*;

public class GMCModifierRecipeProvider implements IRecipeHelper {
    
    public void buildRecipes(Consumer<FinishedRecipe> consumer) {
        addItemRecipes(consumer);
    }
    
    private void addItemRecipes(Consumer<FinishedRecipe> consumer) {
        String folder = "tools/modifiers/";
        ItemCastingRecipeBuilder.tableRecipe(TinkerModifiers.slimesteelReinforcement)
                .setFluidAndTime(TinkerFluids.moltenSlimesteel, 16 * 3)
                .setCast(TinkerCommons.obsidianPane, true)
                .save(consumer, prefix(TinkerModifiers.slimesteelReinforcement, folder));
        ItemCastingRecipeBuilder.tableRecipe(TinkerModifiers.ironReinforcement)
                .setFluidAndTime(new FluidStack(Iron.getFluid(), 144))
                .setCast(TinkerTables.pattern, true)
                .save(consumer, prefix(TinkerModifiers.ironReinforcement, folder));
        ItemCastingRecipeBuilder.tableRecipe(TinkerModifiers.goldReinforcement)
                .setFluidAndTime(new FluidStack(Gold.getFluid(), 144))
                .setCast(TinkerTables.pattern, true)
                .save(consumer, prefix(TinkerModifiers.goldReinforcement, folder));
        ItemCastingRecipeBuilder.tableRecipe(TinkerModifiers.cobaltReinforcement)
                .setFluidAndTime(new FluidStack(Cobalt.getFluid(), 144))
                .setCast(TinkerTables.pattern, true)
                .save(consumer, prefix(TinkerModifiers.cobaltReinforcement, folder));

        ItemCastingRecipeBuilder.tableRecipe(TinkerModifiers.silkyCloth)
                .setCast(Items.COBWEB, true)
                .setFluidAndTime(new FluidStack(RoseGold.getFluid(), 144))
                .save(consumer, prefix(TinkerModifiers.silkyCloth, folder));
    }

    @Override
    public String getModId() {
        return TConstruct.MOD_ID;
    }
}
