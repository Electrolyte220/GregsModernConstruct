package dev.electrolyte.gm_construct.datagen;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CompoundIngredient;
import net.minecraftforge.fluids.FluidStack;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.library.data.recipe.IMaterialRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.IToolRecipeHelper;
import slimeknights.tconstruct.library.recipe.casting.ItemCastingRecipeBuilder;
import slimeknights.tconstruct.library.recipe.ingredient.MaterialIngredient;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import slimeknights.tconstruct.tools.TinkerToolParts;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.common.data.GTMaterials.Gold;

public class GMCToolsRecipeProvider implements IMaterialRecipeHelper, IToolRecipeHelper {

    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        String folder = "smeltery/casts/";

        this.castCreation(consumer, TinkerToolParts.repairKit, TinkerSmeltery.repairKitCast, folder);

        this.castCreation(consumer, TinkerToolParts.pickHead,     TinkerSmeltery.pickHeadCast,     folder);
        this.castCreation(consumer, TinkerToolParts.hammerHead,   TinkerSmeltery.hammerHeadCast,   folder);
        this.castCreation(consumer, TinkerToolParts.smallAxeHead, TinkerSmeltery.smallAxeHeadCast, folder);
        this.castCreation(consumer, TinkerToolParts.broadAxeHead, TinkerSmeltery.broadAxeHeadCast, folder);
        this.castCreation(consumer, TinkerToolParts.smallBlade,   TinkerSmeltery.smallBladeCast,   folder);
        this.castCreation(consumer, TinkerToolParts.broadBlade,   TinkerSmeltery.broadBladeCast,   folder);
        this.castCreation(consumer, TinkerToolParts.bowLimb,      TinkerSmeltery.bowLimbCast,      folder);
        this.castCreation(consumer, TinkerToolParts.bowGrip,      TinkerSmeltery.bowGripCast,      folder);

        this.castCreation(consumer, TinkerToolParts.toolBinding,  TinkerSmeltery.toolBindingCast,  folder);
        this.castCreation(consumer, TinkerToolParts.toughBinding, TinkerSmeltery.toughBindingCast, folder);
        this.castCreation(consumer, TinkerToolParts.adzeHead,     TinkerSmeltery.adzeHeadCast,     folder);
        this.castCreation(consumer, TinkerToolParts.largePlate,   TinkerSmeltery.largePlateCast,   folder);
        this.castCreation(consumer, TinkerToolParts.toolHandle,   TinkerSmeltery.toolHandleCast,   folder);
        this.castCreation(consumer, TinkerToolParts.toughHandle,  TinkerSmeltery.toughHandleCast,  folder);

        this.castCreation(consumer, CompoundIngredient.of(Ingredient.of(TinkerSmeltery.dummyPlating.get(ArmorItem.Type.HELMET)), MaterialIngredient.of(TinkerToolParts.plating.get(ArmorItem.Type.HELMET))),         TinkerSmeltery.helmetPlatingCast,     folder, id(TinkerToolParts.plating.get(ArmorItem.Type.HELMET)).getPath());
        this.castCreation(consumer, CompoundIngredient.of(Ingredient.of(TinkerSmeltery.dummyPlating.get(ArmorItem.Type.CHESTPLATE)), MaterialIngredient.of(TinkerToolParts.plating.get(ArmorItem.Type.CHESTPLATE))), TinkerSmeltery.chestplatePlatingCast, folder, id(TinkerToolParts.plating.get(ArmorItem.Type.CHESTPLATE)).getPath());
        this.castCreation(consumer, CompoundIngredient.of(Ingredient.of(TinkerSmeltery.dummyPlating.get(ArmorItem.Type.LEGGINGS)), MaterialIngredient.of(TinkerToolParts.plating.get(ArmorItem.Type.LEGGINGS))),     TinkerSmeltery.leggingsPlatingCast,   folder, id(TinkerToolParts.plating.get(ArmorItem.Type.LEGGINGS)).getPath());
        this.castCreation(consumer, CompoundIngredient.of(Ingredient.of(TinkerSmeltery.dummyPlating.get(ArmorItem.Type.BOOTS)), MaterialIngredient.of(TinkerToolParts.plating.get(ArmorItem.Type.BOOTS))),           TinkerSmeltery.bootsPlatingCast,      folder, id(TinkerToolParts.plating.get(ArmorItem.Type.BOOTS)).getPath());
        this.castCreation(consumer, TinkerToolParts.maille, TinkerSmeltery.mailleCast, folder);
    }

    @Override
    public void castCreation(Consumer<FinishedRecipe> consumer, Ingredient input, CastItemObject cast, String folder, String name) {
        ItemCastingRecipeBuilder.tableRecipe(cast)
                .setFluidAndTime(new FluidStack(Gold.getFluid(), 144))
                .setCast(input, true)
                .setSwitchSlots()
                .save(consumer, location(folder + "gold/" + name));
    }

    private void castCreation(Consumer<FinishedRecipe> consumer, ItemObject<?> part, CastItemObject cast, String folder) {
        castCreation(consumer, MaterialIngredient.of(part), cast, folder, id(part).getPath());
    }

    @Override
    public String getModId() {
        return TConstruct.MOD_ID;
    }
}
