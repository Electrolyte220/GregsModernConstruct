package dev.electrolyte.gm_construct.recipes;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.UnificationEntry;
import dev.electrolyte.gm_construct.GMConstruct;
import dev.electrolyte.gm_construct.config.GMCConfig;
import dev.electrolyte.gm_construct.helper.GTMaterialHelper;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import slimeknights.tconstruct.tools.TinkerToolParts;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.gem;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.ingot;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.EXTRUDER_RECIPES;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.FLUID_SOLIDFICATION_RECIPES;

public class GMCMaterialMachineRecipes {

    public static void register(Consumer<FinishedRecipe> provider) {
        for(Material material : GTMaterialHelper.REGISTERED_TOOL_MATERIALS) {
            UnificationEntry inputMaterial;
            if(material.hasProperty(PropertyKey.GEM)) {
                inputMaterial = new UnificationEntry(gem, material);
            } else {
                inputMaterial = new UnificationEntry(ingot, material);
            }

            if(GMCConfig.GENERATE_EXTRUDER_RECIPES.get()) {
                generateExtruderRecipes(inputMaterial, TinkerToolParts.repairKit, 2, TinkerSmeltery.repairKitCast, "repair_kit", provider);

                generateExtruderRecipes(inputMaterial, TinkerToolParts.pickHead, 2, TinkerSmeltery.pickHeadCast, "pick_head", provider);
                generateExtruderRecipes(inputMaterial, TinkerToolParts.hammerHead, 8, TinkerSmeltery.hammerHeadCast, "hammer_head", provider);
                generateExtruderRecipes(inputMaterial, TinkerToolParts.smallAxeHead, 2, TinkerSmeltery.smallAxeHeadCast, "small_axe_head", provider);
                generateExtruderRecipes(inputMaterial, TinkerToolParts.broadAxeHead, 8, TinkerSmeltery.broadAxeHeadCast, "broad_axe_head", provider);
                generateExtruderRecipes(inputMaterial, TinkerToolParts.smallBlade, 2, TinkerSmeltery.smallBladeCast, "small_blade", provider);
                generateExtruderRecipes(inputMaterial, TinkerToolParts.broadBlade, 8, TinkerSmeltery.broadBladeCast, "broad_blade", provider);
                generateExtruderRecipes(inputMaterial, TinkerToolParts.bowLimb, 2, TinkerSmeltery.bowLimbCast, "bow_limb", provider);
                generateExtruderRecipes(inputMaterial, TinkerToolParts.bowGrip, 2, TinkerSmeltery.bowGripCast, "bow_grip", provider);

                generateExtruderRecipes(inputMaterial, TinkerToolParts.toolBinding, 1, TinkerSmeltery.toolBindingCast, "tool_binding", provider);
                generateExtruderRecipes(inputMaterial, TinkerToolParts.toughBinding, 3, TinkerSmeltery.toughBindingCast, "tough_binding", provider);
                generateExtruderRecipes(inputMaterial, TinkerToolParts.adzeHead, 2, TinkerSmeltery.adzeHeadCast, "adze_head", provider);
                generateExtruderRecipes(inputMaterial, TinkerToolParts.largePlate, 4, TinkerSmeltery.largePlateCast, "large_plate", provider);
                generateExtruderRecipes(inputMaterial, TinkerToolParts.toolHandle, 1, TinkerSmeltery.toolHandleCast, "tool_handle", provider);
                generateExtruderRecipes(inputMaterial, TinkerToolParts.toughHandle, 3, TinkerSmeltery.toughHandleCast, "tough_handle", provider);
            }

            if(GMCConfig.GENERATE_FLUID_SOLIDIFICATION_RECIPES.get()) {
                if(!inputMaterial.material.hasProperty(PropertyKey.FLUID)) {
                    GMConstruct.LOGGER.warn("Material {} does not have a fluid, no solidification recipes will be added for this material.", inputMaterial.material);
                    continue;
                }
                generateSolidifierRecipes(inputMaterial, TinkerToolParts.repairKit, 2, TinkerSmeltery.repairKitCast, "repair_kit", provider);

                generateSolidifierRecipes(inputMaterial, TinkerToolParts.pickHead, 2, TinkerSmeltery.pickHeadCast, "pick_head", provider);
                generateSolidifierRecipes(inputMaterial, TinkerToolParts.hammerHead, 8, TinkerSmeltery.hammerHeadCast, "hammer_head", provider);
                generateSolidifierRecipes(inputMaterial, TinkerToolParts.smallAxeHead, 2, TinkerSmeltery.smallAxeHeadCast, "small_axe_head", provider);
                generateSolidifierRecipes(inputMaterial, TinkerToolParts.broadAxeHead, 8, TinkerSmeltery.broadAxeHeadCast, "broad_axe_head", provider);
                generateSolidifierRecipes(inputMaterial, TinkerToolParts.smallBlade, 2, TinkerSmeltery.smallBladeCast, "small_blade", provider);
                generateSolidifierRecipes(inputMaterial, TinkerToolParts.broadBlade, 8, TinkerSmeltery.broadBladeCast, "broad_blade", provider);
                generateSolidifierRecipes(inputMaterial, TinkerToolParts.bowLimb, 2, TinkerSmeltery.bowLimbCast, "bow_limb", provider);
                generateSolidifierRecipes(inputMaterial, TinkerToolParts.bowGrip, 2, TinkerSmeltery.bowGripCast, "bow_grip", provider);

                generateSolidifierRecipes(inputMaterial, TinkerToolParts.toolBinding, 1, TinkerSmeltery.toolBindingCast, "tool_binding", provider);
                generateSolidifierRecipes(inputMaterial, TinkerToolParts.toughBinding, 3, TinkerSmeltery.toughBindingCast, "tough_binding", provider);
                generateSolidifierRecipes(inputMaterial, TinkerToolParts.adzeHead, 2, TinkerSmeltery.adzeHeadCast, "adze_head", provider);
                generateSolidifierRecipes(inputMaterial, TinkerToolParts.largePlate, 4, TinkerSmeltery.largePlateCast, "large_plate", provider);
                generateSolidifierRecipes(inputMaterial, TinkerToolParts.toolHandle, 1, TinkerSmeltery.toolHandleCast, "tool_handle", provider);
                generateSolidifierRecipes(inputMaterial, TinkerToolParts.toughHandle, 3, TinkerSmeltery.toughHandleCast, "tough_handle", provider);
            }
        }
    }

    private static void generateExtruderRecipes(UnificationEntry inputMaterial, ItemObject<?> toolPartStack, int materialCost, CastItemObject cast, String path, Consumer<FinishedRecipe> provider) {
        EXTRUDER_RECIPES.recipeBuilder(GMConstruct.id("extrude_" + inputMaterial.material.getName() + "_to_" + path))
                .inputItems(inputMaterial, materialCost)
                .notConsumable(cast)
                .outputItems(getToolStack(toolPartStack, inputMaterial.material))
                .duration((int) (20 * inputMaterial.material.getMass() * materialCost))
                .EUt(VA[MV])
                .save(provider);
    }

    private static void generateSolidifierRecipes(UnificationEntry inputMaterial, ItemObject<?> toolPartStack, int materialCost, CastItemObject cast, String path, Consumer<FinishedRecipe> provider) {
        FLUID_SOLIDFICATION_RECIPES.recipeBuilder(GMConstruct.id("solidify_" + inputMaterial.material.getName() + "_to_" + path))
                .inputFluids(inputMaterial.material.getFluid(materialCost * L))
                .notConsumable(cast)
                .outputItems(getToolStack(toolPartStack, inputMaterial.material))
                .duration((int) (20 * inputMaterial.material.getMass() * materialCost))
                .EUt(VA[MV])
                .save(provider);
    }

    private static ItemStack getToolStack(ItemObject<?> toolPart, Material material) {
        ItemStack stack = new ItemStack(toolPart);
        stack.getOrCreateTag().putString("Material", GMConstruct.materialId(material.getName()).toString());
        return stack;
    }
}
