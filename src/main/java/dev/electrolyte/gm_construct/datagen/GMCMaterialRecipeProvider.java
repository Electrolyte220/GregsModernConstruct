package dev.electrolyte.gm_construct.datagen;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import dev.electrolyte.gm_construct.helper.GTMaterialHelper;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.common.crafting.conditions.OrCondition;
import net.minecraftforge.fluids.FluidStack;
import slimeknights.mantle.recipe.data.IRecipeHelper;
import slimeknights.mantle.recipe.helper.FluidOutput;
import slimeknights.mantle.registration.object.FluidObject;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.fluids.TinkerFluids;
import slimeknights.tconstruct.library.data.recipe.ICastCreationHelper;
import slimeknights.tconstruct.library.data.recipe.IMaterialRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.ISmelteryRecipeHelper;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.library.materials.definition.MaterialVariantId;
import slimeknights.tconstruct.library.recipe.casting.material.MaterialFluidRecipeBuilder;
import slimeknights.tconstruct.library.recipe.melting.MaterialMeltingRecipeBuilder;
import slimeknights.tconstruct.tools.data.material.MaterialIds;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.common.data.GTMaterials.*;

public class GMCMaterialRecipeProvider implements IMaterialRecipeHelper, IConditionBuilder, IRecipeHelper, ICastCreationHelper, ISmelteryRecipeHelper {

    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        addMaterialSmeltery(consumer);
    }

    private void addMaterialSmeltery(Consumer<FinishedRecipe> consumer) {
        String folder = "tools/materials/";
        materialMeltingCasting(consumer, MaterialIds.iron,          Iron,    folder);
        materialMeltingCasting(consumer, MaterialIds.copper,        Copper,  folder);

        materialMeltingCasting(consumer, MaterialIds.slimesteel,     TinkerFluids.moltenSlimesteel, folder);
        materialMeltingCasting(consumer, MaterialIds.amethystBronze, TinkerFluids.moltenAmethystBronze, folder);
        materialMeltingCasting(consumer, MaterialIds.roseGold,       RoseGold, folder);
        materialMeltingCasting(consumer, MaterialIds.pigIron,        TinkerFluids.moltenPigIron, folder);
        materialMeltingCasting(consumer, MaterialIds.cobalt,         Cobalt, folder);
        materialMeltingCasting(consumer, MaterialIds.steel,          Steel, folder);

        materialComposite(consumer,        MaterialIds.string, MaterialIds.roseGold,   RoseGold, 144, folder);

        materialMeltingCasting(consumer, MaterialIds.cinderslime, TinkerFluids.moltenCinderslime, folder);
        materialMeltingCasting(consumer, MaterialIds.queensSlime, TinkerFluids.moltenQueensSlime, folder);
        materialMeltingCasting(consumer, MaterialIds.hepatizon,   TinkerFluids.moltenHepatizon,   folder);
        materialMeltingCasting(consumer, MaterialIds.manyullyn,   TinkerFluids.moltenManyullyn,   folder);
        materialMeltingComposite(consumer, MaterialIds.leather, MaterialIds.ancientHide, TinkerFluids.moltenDebris, 144, folder);

        compatMeltingCasting(consumer, MaterialIds.osmium,   Osmium,   folder);
        compatMeltingCasting(consumer, MaterialIds.tungsten, Tungsten, folder);
        compatMeltingCasting(consumer, MaterialIds.platinum, Platinum, folder);
        compatMeltingCasting(consumer, MaterialIds.silver,   Silver,   folder);
        compatMeltingCasting(consumer, MaterialIds.lead,     Lead,     folder);
        compatMeltingCasting(consumer, MaterialIds.aluminum, TinkerFluids.moltenAluminum, folder);
        materialComposite(withCondition(consumer, tagCondition("ingots/aluminum")), MaterialIds.rock, MaterialIds.whitestoneAluminum, TinkerFluids.moltenAluminum, 144, folder, "whitestone_from_aluminum");
        materialComposite(withCondition(consumer, tagCondition("ingots/tin")),      MaterialIds.rock, MaterialIds.whitestoneTin,      Tin,      144, folder, "whitestone_from_tin");
        materialComposite(withCondition(consumer, tagCondition("ingots/zinc")),     MaterialIds.rock, MaterialIds.whitestoneZinc,     Zinc,     144, folder, "whitestone_from_zinc");

        compatMeltingCasting(consumer, MaterialIds.constantan,     TinkerFluids.moltenConstantan, "nickel", folder);
        compatMeltingCasting(consumer, MaterialIds.invar,          Invar,      "nickel", folder);
        compatMeltingCasting(consumer, MaterialIds.electrum,       Electrum,   "silver", folder);
        compatMeltingCasting(consumer, MaterialIds.bronze,         Bronze,     "tin", folder);
        materialMeltingComposite(withCondition(consumer, tagCondition("ingots/uranium")), MaterialIds.necroticBone, MaterialIds.necronium, Uranium238, 144, folder);
        materialMeltingComposite(withCondition(consumer, new OrCondition(tagCondition("ingots/brass"), tagCondition("ingots/zinc"))),
                MaterialIds.slimewood, MaterialIds.platedSlimewood, Brass, 144, folder);

        materialMeltingCasting(consumer, MaterialIds.gold, Gold, folder);
    }

    @Override
    public void materialMeltingCasting(Consumer<FinishedRecipe> consumer, MaterialVariantId material, FluidObject<?> fluid, String folder) {
        IMaterialRecipeHelper.super.materialMeltingCasting(consumer, material, fluid, 144, folder);
    }

    public void materialComposite(Consumer<FinishedRecipe> consumer, MaterialVariantId input, MaterialVariantId output, Material material, int amount, String folder) {
        MaterialFluidRecipeBuilder.material(output)
                .setInputId(input)
                .setFluid(material.getFluidTag(), 144)
                .setTemperature(GTMaterialHelper.findTemp(material))
                .save(consumer, location(folder + "composite/" + output.getLocation('_').getPath()));
    }

    public void materialMeltingCasting(Consumer<FinishedRecipe> consumer, MaterialVariantId materialId, Material material, String folder) {
        MaterialFluidRecipeBuilder.material(materialId)
                .setFluid(material.getFluidTag(), 144)
                .setTemperature(GTMaterialHelper.findTemp(material))
                .save(consumer, location(folder + "casting/" + material.getName()));

        MaterialMeltingRecipeBuilder.material(materialId, GTMaterialHelper.findTemp(material), FluidOutput.fromFluid(material.getFluid(), 144))
                .save(consumer, location(folder + "melting/" + material.getName()));
    }

    public void compatMeltingCasting(Consumer<FinishedRecipe> consumer, MaterialId materialId, Material material, String folder) {
        materialMeltingCasting(withCondition(consumer, tagCondition("ingots/" + materialId.getPath())), materialId, material, folder);
    }

    public void compatMeltingCasting(Consumer<FinishedRecipe> consumer, MaterialId materialId, Material material, String altTag, String folder) {
        materialMeltingCasting(withCondition(consumer, new OrCondition(tagCondition("ingots/" + material.getName()), tagCondition("ingots/" + altTag))), materialId, material, folder);
    }

    public void materialComposite(Consumer<FinishedRecipe> consumer, MaterialVariantId input, MaterialVariantId output, Material material, int amount, String folder, String name) {
        MaterialFluidRecipeBuilder.material(output)
                .setInputId(input)
                .setFluid(material.getFluidTag(), amount)
                .setTemperature(GTMaterialHelper.findTemp(material))
                .save(consumer, location(folder + "composite/" + name));
    }

    public void materialMeltingComposite(Consumer<FinishedRecipe> consumer, MaterialVariantId input, MaterialVariantId output, Material material, int amount, String folder) {
        MaterialMeltingRecipeBuilder.material(output, GTMaterialHelper.findTemp(material), new FluidStack(material.getFluid(), amount))
                .save(consumer, location(folder + "melting/" + output.getLocation('_').getPath()));
        materialComposite(consumer, input, output, material, amount, folder);
    }

    @Override
    public String getModId() {
        return TConstruct.MOD_ID;
    }
}
