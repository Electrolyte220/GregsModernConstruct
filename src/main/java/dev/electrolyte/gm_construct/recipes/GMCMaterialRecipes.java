package dev.electrolyte.gm_construct.recipes;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import dev.electrolyte.gm_construct.GMConstruct;
import dev.electrolyte.gm_construct.config.GMCConfig;
import dev.electrolyte.gm_construct.helper.GTMaterialHelper;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import slimeknights.mantle.recipe.helper.FluidOutput;
import slimeknights.mantle.recipe.helper.ItemOutput;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.library.data.recipe.IByproduct;
import slimeknights.tconstruct.library.data.recipe.IMaterialRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.ISmelteryRecipeHelper;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.library.recipe.casting.ItemCastingRecipeBuilder;
import slimeknights.tconstruct.library.recipe.casting.material.MaterialFluidRecipeBuilder;
import slimeknights.tconstruct.library.recipe.melting.IMeltingContainer.OreRateType;
import slimeknights.tconstruct.library.recipe.melting.MaterialMeltingRecipeBuilder;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;

import java.util.function.Consumer;
import java.util.function.IntFunction;

import static slimeknights.mantle.Mantle.COMMON;
import static slimeknights.tconstruct.library.recipe.melting.IMeltingRecipe.getTemperature;

public class GMCMaterialRecipes implements IMaterialRecipeHelper, ISmelteryRecipeHelper {

    public GMCMaterialRecipes(Consumer<FinishedRecipe> provider) {
        register(provider);
    }

    public void register(Consumer<FinishedRecipe> provider) {
        for(Material material : GTCEuAPI.materialManager.getRegisteredMaterials()) {
            if(!material.hasProperty(PropertyKey.ORE)) continue;
            if(!material.hasProperty(PropertyKey.FLUID)) continue;
            Material smeltsIntoMaterial = material.getProperty(PropertyKey.ORE).getDirectSmeltResult();
            if(smeltsIntoMaterial == null) continue;
            if(material.getName().equals(smeltsIntoMaterial.getName())) continue;
            if(!smeltsIntoMaterial.hasProperty(PropertyKey.FLUID)) continue;
            metalMeltingTag(provider, smeltsIntoMaterial.getFluidTag(), smeltsIntoMaterial.getFluid(), material.getName(), true, material.hasProperty(PropertyKey.DUST), "smeltery/melting/metal", true);
        }

        for(Material material : GTMaterialHelper.getRegisteredMaterials()) {
            if(!material.hasProperty(PropertyKey.FLUID)) {
                GMConstruct.LOGGER.warn("Material {} does not have a fluid, no solidification recipes will be added for this material.", material);
                continue;
            }
            MaterialId materialId = GMConstruct.materialId(material.getName());
            metalMaterialRecipe(provider, materialId, "tools/materials/", material.getName(), false); //repairing via material nugget/ingot/block in tool station

            if(GMCConfig.GENERATE_MELTING_CASTING_RECIPES.get()) {
                MaterialMeltingRecipeBuilder.material(materialId, material.getFluid().getFluidType().getTemperature() - 300, new FluidStack(material.getFluid(), 144))//todo: check temp
                        .save(provider, location("tools/materials/melting/" + material.getName())); //melting recipes for tool parts

                metalMeltingTag(provider, material.getFluidTag(), material.getFluid(), material.getName(), material.hasProperty(PropertyKey.ORE), material.hasProperty(PropertyKey.DUST), "smeltery/melting/metal", false); //melting recipes for material ingot, gear, wire, etc. to fluid

                MaterialFluidRecipeBuilder.material(materialId)
                        .setFluid(material.getFluidTag(), 144)
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
    public void metalMeltingTag(Consumer<FinishedRecipe> consumer, TagKey<Fluid> fluidTag, Fluid fluid, String name, boolean hasOre, boolean hasDust, String folder, boolean isOptional, IByproduct... byproducts) {
        ResourceLocation tagLoc = fluidTag.location();
        String fluidName = tagLoc.getPath();
        Fluid output;
        ResourceLocation fluidLoc = new ResourceLocation(TConstruct.MOD_ID, "molten_" + fluidName);
        if(ForgeRegistries.FLUIDS.containsKey(fluidLoc)) {
            output = ForgeRegistries.FLUIDS.getValue(fluidLoc);
        } else {
            output = fluid;
        }
        metalMelting(consumer, size -> FluidOutput.fromFluid(output, size), getTemperature(output), name, hasOre, hasDust, folder, isOptional, byproducts);
    }

    private void metalMelting(Consumer<FinishedRecipe> consumer, IntFunction<FluidOutput> fluid, int temperature, String name, boolean hasOre, boolean hasDust, String folder, boolean isOptional, IByproduct... byproducts) {
        String prefix = folder + "/" + name + "/";
        tagMelting(consumer, fluid.apply(1296), temperature, "storage_blocks/" + name, 3.0f, prefix + "block", isOptional);
        tagMelting(consumer, fluid.apply(144), temperature, "ingots/" + name, 1.0f, prefix + "ingot", isOptional);
        tagMelting(consumer, fluid.apply(16), temperature, "nuggets/" + name, 1 / 3f, prefix + "nugget", isOptional);
        if (hasOre) {
            oreMelting(consumer, fluid.apply(144),     temperature, "raw_materials/" + name,      null, 1.5f, prefix + "raw",       isOptional, OreRateType.METAL, 1.0f, byproducts);
            oreMelting(consumer, fluid.apply(144 * 9), temperature, "storage_blocks/raw_" + name, null, 6.0f, prefix + "raw_block", isOptional, OreRateType.METAL, 9.0f, byproducts);
            oreMelting(consumer, fluid.apply(144),     temperature, "ores/" + name, Tags.Items.ORE_RATES_SPARSE,   1.5f, prefix + "ore_sparse",   isOptional, OreRateType.METAL, 1.0f, byproducts);
            oreMelting(consumer, fluid.apply(144 * 2), temperature, "ores/" + name, Tags.Items.ORE_RATES_SINGULAR, 2.5f, prefix + "ore_singular", isOptional, OreRateType.METAL, 2.0f, byproducts);
            oreMelting(consumer, fluid.apply(144 * 6), temperature, "ores/" + name, Tags.Items.ORE_RATES_DENSE,    4.5f, prefix + "ore_dense",    isOptional, OreRateType.METAL, 6.0f, byproducts);
            georeMelting(consumer, fluid, 144, temperature, name, prefix);
        }
        if (hasDust) {
            tagMelting(consumer, fluid.apply(144), temperature, "dusts/" + name, 0.75f, prefix + "dust", true);
        }
        tagMelting(consumer, fluid.apply(144),      temperature, "plates/" + name, 1.0f, prefix + "plates", true);
        tagMelting(consumer, fluid.apply(144 * 4),  temperature, "gears/" + name, 2.0f, prefix + "gear", true);
        tagMelting(consumer, fluid.apply(16 * 3), temperature, "coins/" + name, 2 / 3f, prefix + "coin", true);
        tagMelting(consumer, fluid.apply(144 / 2),  temperature, "rods/" + name, 1 / 5f, prefix + "rod", true);
        tagMelting(consumer, fluid.apply(144 / 2),  temperature, "wires/" + name, 1 / 5f, prefix + "wire", true);
        tagMelting(consumer, fluid.apply(144),      temperature, "sheetmetals/" + name, 1.0f, prefix + "sheetmetal", true);
    }

    private void georeMelting(Consumer<FinishedRecipe> consumer, IntFunction<FluidOutput> fluid, int unit, int temperature, String name, String folder) {
        tagMelting(consumer, fluid.apply(unit),     temperature, "geore_shards/" + name, 1.0f, folder + "geore/shard", true);
        tagMelting(consumer, fluid.apply(unit * 4), temperature, "geore_blocks/" + name, 2.0f, folder + "geore/block", true);
        tagMelting(consumer, fluid.apply(unit * 4), temperature, "geore_clusters/" + name,    2.5f, folder + "geore/cluster", true);
        tagMelting(consumer, fluid.apply(unit),     temperature, "geore_small_buds/" + name,  1.0f, folder + "geore/bud_small", true);
        tagMelting(consumer, fluid.apply(unit * 2), temperature, "geore_medium_buds/" + name, 1.5f, folder + "geore/bud_medium", true);
        tagMelting(consumer, fluid.apply(unit * 3), temperature, "geore_large_buds/" + name,  2.0f, folder + "geore/bud_large", true);
    }

    private void metalCasting(Consumer<FinishedRecipe> consumer, Fluid fluid, ItemOutput block, ItemOutput ingot, ItemOutput nugget, String folder, String metal) {
        String metalFolder = folder + metal + "/";
        if (block != null) {
            ItemCastingRecipeBuilder.basinRecipe(block)
                    .setFluidAndTime(new FluidStack(fluid, 144 * 9))
                    .save(consumer, location(metalFolder + "block"));
        }

        castingWithCast(consumer, fluid, 144, TinkerSmeltery.ingotCast, ingot, metalFolder + "ingot");
        castingWithCast(consumer, fluid, 144 / 9, TinkerSmeltery.nuggetCast, nugget, metalFolder + "nugget");
        castingWithCast(consumer, fluid, 144, TinkerSmeltery.plateCast, ItemOutput.fromTag(getItemTag(COMMON, "plates/" + metal)), folder + metal + "/plate");
        castingWithCast(consumer, fluid, 144 * 4, TinkerSmeltery.gearCast, ItemOutput.fromTag(getItemTag(COMMON, "gears/" + metal)), folder + metal + "/gear");
        castingWithCast(consumer, fluid, 144 / 2, TinkerSmeltery.rodCast, ItemOutput.fromTag(getItemTag(COMMON, "rods/" + metal)), folder + metal + "/rod");
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
