package dev.electrolyte.gm_construct.recipes;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import dev.electrolyte.gm_construct.GMConstruct;
import dev.electrolyte.gm_construct.config.GMCConfig;
import dev.electrolyte.gm_construct.helper.GTMaterialHelper;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import slimeknights.mantle.recipe.helper.ItemOutput;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.library.data.recipe.IMaterialRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.ISmelteryRecipeHelper;
import slimeknights.tconstruct.library.recipe.FluidValues;
import slimeknights.tconstruct.library.recipe.casting.ItemCastingRecipeBuilder;
import slimeknights.tconstruct.library.recipe.casting.material.MaterialFluidRecipeBuilder;
import slimeknights.tconstruct.library.recipe.melting.MaterialMeltingRecipeBuilder;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;

import java.util.function.Consumer;

import static slimeknights.mantle.Mantle.COMMON;

public class GMCMaterialRecipes implements IMaterialRecipeHelper, ISmelteryRecipeHelper {

    public GMCMaterialRecipes(Consumer<FinishedRecipe> provider) {
        register(provider);
    }

    public void register(Consumer<FinishedRecipe> provider) {
        for(Material material : GTMaterialHelper.getRegisteredMaterials()) {
            metalMaterialRecipe(provider, GMConstruct.materialId(material.getName()), "tools/materials/", material.getName(), false); //repairing via material nugget/ingot/block in tool station

            if(GMCConfig.GENERATE_CASTING_RECIPES.get()) {
                MaterialMeltingRecipeBuilder.material(GMConstruct.materialId(material.getName()), material.getFluid().getFluidType().getTemperature() - 300, new FluidStack(material.getFluid(), FluidValues.INGOT))//todo: check temp
                        .save(provider, location("tools/materials/melting/" + material.getName())); //melting recipes for tool parts

                metalMelting(provider, material.getFluid(), material.getName(), material.hasProperty(PropertyKey.ORE), material.hasProperty(PropertyKey.DUST), "smeltery/melting/metal", false); //melting recipes for material ingot, gear, wire, etc. to fluid

                MaterialFluidRecipeBuilder.material(GMConstruct.materialId(material.getName()))
                        .setFluid(material.getFluidTag(), FluidValues.INGOT)
                        .setTemperature(material.getFluid().getFluidType().getTemperature() - 300)
                        .save(provider, location("tools/materials/casting/" + material.getName())); //casting recipes for material tool parts

                metalCasting(provider, material.getFluid(),
                        ItemOutput.fromTag(getItemTag(COMMON, "storage_blocks/" + material.getName())),
                        ItemOutput.fromTag(getItemTag(COMMON, "ingots/" + material.getName())),
                        ItemOutput.fromTag(getItemTag(COMMON, "nuggets/" + material.getName())),
                        "smeltery/casting/metal/", material.getName()); //casting recipes for material blocks, ingots & nuggets ONLY.

            }
        }
    }

    private void metalCasting(Consumer<FinishedRecipe> consumer, Fluid fluid, ItemOutput block, ItemOutput ingot, ItemOutput nugget, String folder, String metal) {
        String metalFolder = folder + metal + "/";
        if (block != null) {
            ItemCastingRecipeBuilder.basinRecipe(block)
                    .setFluidAndTime(new FluidStack(fluid, FluidValues.METAL_BLOCK))
                    .save(consumer, location(metalFolder + "block"));
        }

        castingWithCast(consumer, fluid, FluidValues.INGOT, TinkerSmeltery.ingotCast, ingot, metalFolder + "ingot");
        castingWithCast(consumer, fluid, FluidValues.NUGGET, TinkerSmeltery.nuggetCast, nugget, metalFolder + "nugget");
        castingWithCast(consumer, fluid, FluidValues.INGOT, TinkerSmeltery.plateCast, ItemOutput.fromTag(getItemTag(COMMON, "plates/" + metal)), folder + metal + "/plate");
        castingWithCast(consumer, fluid, FluidValues.INGOT * 4, TinkerSmeltery.gearCast, ItemOutput.fromTag(getItemTag(COMMON, "gears/" + metal)), folder + metal + "/gear");
        castingWithCast(consumer, fluid, FluidValues.INGOT / 2, TinkerSmeltery.rodCast, ItemOutput.fromTag(getItemTag(COMMON, "rods/" + metal)), folder + metal + "/rod");
    }

    private void castingWithCast(Consumer<FinishedRecipe> consumer, Fluid fluid, int amount, CastItemObject cast, ItemOutput output, String location) {
        ItemCastingRecipeBuilder.tableRecipe(output)
                .setFluidAndTime(new FluidStack(fluid, amount))
                .setCast(cast.getMultiUseTag(), false)
                .save(consumer, location(location + "_gold_cast"));
        ItemCastingRecipeBuilder.tableRecipe(output)
                .setFluidAndTime(new FluidStack(fluid, amount))
                .setCast(cast.getSingleUseTag(), true)
                .save(consumer, location(location + "_sand_cast"));
    }

    @Override
    public String getModId() {
        return GMConstruct.MOD_ID;
    }
}
