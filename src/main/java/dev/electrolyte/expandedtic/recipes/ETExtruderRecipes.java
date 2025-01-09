package dev.electrolyte.expandedtic.recipes;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.UnificationEntry;
import dev.electrolyte.expandedtic.ExpandedTiC;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import slimeknights.tconstruct.tools.TinkerToolParts;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.MV;
import static com.gregtechceu.gtceu.api.GTValues.VA;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.gem;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.ingot;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.EXTRUDER_RECIPES;

public class ETExtruderRecipes {

    public static void register(Consumer<FinishedRecipe> provider) {
        for(Material material : ExpandedTiC.REGISTERED_TOOL_MATERIALS) {
            UnificationEntry inputMaterial;
            if(material.hasProperty(PropertyKey.GEM)) {
                inputMaterial = new UnificationEntry(gem, material);
            } else {
                inputMaterial = new UnificationEntry(ingot, material);
            }

            if(ExpandedTiC.CONFIG_INSTANCE.gtMaterialGeneration.generateExtruderRecipes) {
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
        }
    }

    private static void generateExtruderRecipes(UnificationEntry inputMaterial, ItemObject<?> toolPartStack, int materialCost, CastItemObject cast, String path, Consumer<FinishedRecipe> provider) {
        EXTRUDER_RECIPES.recipeBuilder(ExpandedTiC.id("extrude_" + inputMaterial.material.getName() + "_to_" + path))
                .inputItems(inputMaterial, materialCost)
                .notConsumable(cast)
                .outputItems(getToolStack(toolPartStack, inputMaterial.material))
                .duration((int) (20 * inputMaterial.material.getMass() * materialCost))
                .EUt(VA[MV])
                .save(provider);
    }

    private static ItemStack getToolStack(ItemObject<?> toolPart, Material material) {
        ItemStack stack = new ItemStack(toolPart);
        stack.getOrCreateTag().putString("Material", ExpandedTiC.materialId(material.getName()).toString());
        return stack;
    }
}
