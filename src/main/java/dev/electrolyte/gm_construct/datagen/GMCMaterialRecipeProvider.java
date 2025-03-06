package dev.electrolyte.gm_construct.datagen;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.common.crafting.conditions.OrCondition;
import slimeknights.mantle.recipe.data.IRecipeHelper;
import slimeknights.mantle.registration.object.FluidObject;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.fluids.TinkerFluids;
import slimeknights.tconstruct.library.data.recipe.ICastCreationHelper;
import slimeknights.tconstruct.library.data.recipe.IMaterialRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.ISmelteryRecipeHelper;
import slimeknights.tconstruct.library.materials.definition.MaterialVariantId;
import slimeknights.tconstruct.tools.data.material.MaterialIds;

import java.util.function.Consumer;

public class GMCMaterialRecipeProvider implements IMaterialRecipeHelper, IConditionBuilder, IRecipeHelper, ICastCreationHelper, ISmelteryRecipeHelper {

    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        addMaterialSmeltery(consumer);
    }

    private void addMaterialSmeltery(Consumer<FinishedRecipe> consumer) {
        String folder = "tools/materials/";
        materialMeltingCasting(consumer, MaterialIds.iron,          TinkerFluids.moltenIron,    folder);
        materialMeltingCasting(consumer, MaterialIds.copper,        TinkerFluids.moltenCopper,  folder);

        materialMeltingCasting(consumer, MaterialIds.slimesteel,     TinkerFluids.moltenSlimesteel, folder);
        materialMeltingCasting(consumer, MaterialIds.amethystBronze, TinkerFluids.moltenAmethystBronze, folder);
        materialMeltingCasting(consumer, MaterialIds.roseGold,       TinkerFluids.moltenRoseGold, folder);
        materialMeltingCasting(consumer, MaterialIds.pigIron,        TinkerFluids.moltenPigIron, folder);
        materialMeltingCasting(consumer, MaterialIds.cobalt,         TinkerFluids.moltenCobalt, folder);
        materialMeltingCasting(consumer, MaterialIds.steel,          TinkerFluids.moltenSteel, folder);

        materialComposite(consumer,        MaterialIds.string, MaterialIds.roseGold,   TinkerFluids.moltenRoseGold, 144, folder);

        materialMeltingCasting(consumer, MaterialIds.cinderslime, TinkerFluids.moltenCinderslime, folder);
        materialMeltingCasting(consumer, MaterialIds.queensSlime, TinkerFluids.moltenQueensSlime, folder);
        materialMeltingCasting(consumer, MaterialIds.hepatizon,   TinkerFluids.moltenHepatizon,   folder);
        materialMeltingCasting(consumer, MaterialIds.manyullyn,   TinkerFluids.moltenManyullyn,   folder);

        materialMeltingComposite(consumer, MaterialIds.leather, MaterialIds.ancientHide, TinkerFluids.moltenDebris, 144, folder);

        compatMeltingCasting(consumer, MaterialIds.osmium,   TinkerFluids.moltenOsmium,   folder);
        compatMeltingCasting(consumer, MaterialIds.tungsten, TinkerFluids.moltenTungsten, folder);
        compatMeltingCasting(consumer, MaterialIds.platinum, TinkerFluids.moltenPlatinum, folder);
        compatMeltingCasting(consumer, MaterialIds.silver,   TinkerFluids.moltenSilver,   folder);
        compatMeltingCasting(consumer, MaterialIds.lead,     TinkerFluids.moltenLead,     folder);
        compatMeltingCasting(consumer, MaterialIds.aluminum, TinkerFluids.moltenAluminum, folder);
        materialComposite(withCondition(consumer, tagCondition("ingots/aluminum")), MaterialIds.rock, MaterialIds.whitestoneAluminum, TinkerFluids.moltenAluminum, 144, folder, "whitestone_from_aluminum");
        materialComposite(withCondition(consumer, tagCondition("ingots/tin")),      MaterialIds.rock, MaterialIds.whitestoneTin,      TinkerFluids.moltenTin,      144, folder, "whitestone_from_tin");
        materialComposite(withCondition(consumer, tagCondition("ingots/zinc")),     MaterialIds.rock, MaterialIds.whitestoneZinc,     TinkerFluids.moltenZinc,     144, folder, "whitestone_from_zinc");

        compatMeltingCasting(consumer, MaterialIds.constantan,     TinkerFluids.moltenConstantan, "nickel", folder);
        compatMeltingCasting(consumer, MaterialIds.invar,          TinkerFluids.moltenInvar,      "nickel", folder);
        compatMeltingCasting(consumer, MaterialIds.electrum,       TinkerFluids.moltenElectrum,   "silver", folder);
        compatMeltingCasting(consumer, MaterialIds.bronze,         TinkerFluids.moltenBronze,     "tin", folder);
        materialMeltingComposite(withCondition(consumer, tagCondition("ingots/uranium")), MaterialIds.necroticBone, MaterialIds.necronium, TinkerFluids.moltenUranium, 144, folder);
        materialMeltingComposite(withCondition(consumer, new OrCondition(tagCondition("ingots/brass"), tagCondition("ingots/zinc"))),
                MaterialIds.slimewood, MaterialIds.platedSlimewood, TinkerFluids.moltenBrass, 144, folder);

        materialMeltingCasting(consumer, MaterialIds.gold, TinkerFluids.moltenGold, folder);
    }

    @Override
    public void materialMeltingCasting(Consumer<FinishedRecipe> consumer, MaterialVariantId material, FluidObject<?> fluid, String folder) {
        IMaterialRecipeHelper.super.materialMeltingCasting(consumer, material, fluid, 144, folder);
    }

    @Override
    public String getModId() {
        return TConstruct.MOD_ID;
    }
}
