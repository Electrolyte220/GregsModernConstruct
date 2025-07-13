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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.data.recipe.IMaterialRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.ISmelteryRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.SmelteryRecipeBuilder;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.library.recipe.casting.material.MaterialFluidRecipeBuilder;
import slimeknights.tconstruct.library.recipe.melting.MaterialMeltingRecipeBuilder;

import java.util.Optional;
import java.util.function.Consumer;

public class GMCMaterialRecipes implements IMaterialRecipeHelper, ISmelteryRecipeHelper {

    public GMCMaterialRecipes(Consumer<FinishedRecipe> provider) {
        register(provider);
    }

    public void register(Consumer<FinishedRecipe> provider) {
        for(Material material : GTCEuAPI.materialManager.getRegisteredMaterials()) {
            if(!material.hasProperty(PropertyKey.ORE)) continue;
            Material smeltsIntoMaterial = material.getProperty(PropertyKey.ORE).getDirectSmeltResult();
            if(smeltsIntoMaterial == null) continue;
            if(material.getName().equals(smeltsIntoMaterial.getName())) continue;
            if(!smeltsIntoMaterial.hasProperty(PropertyKey.FLUID)) continue;
            Optional<Fluid> fluid = getFluid(smeltsIntoMaterial, smeltsIntoMaterial.getFluidTag());
            if(fluid.isEmpty()) continue;
            metal(provider, material.getName(), fluid.get(), GTMaterialHelper.findTemp(smeltsIntoMaterial)).optional().ore().metal().dust().plate().gear().coin().rod().wire().sheetmetal().geore();
        }

        for(Material material : GTMaterialHelper.REGISTERED_TOOL_MATERIALS) {
            MaterialId materialId = GMConstruct.materialId(material.getName());
            metalMaterialRecipe(provider, materialId, "tools/materials/", material.getName(), false); //repairing via material nugget/ingot/block in tool station

            if(GMCConfig.GENERATE_MELTING_CASTING_RECIPES.get()) {
                Optional<Fluid> fluid = getFluid(material, material.getFluidTag());
                if(fluid.isEmpty()) continue;
                int temp = GTMaterialHelper.findTemp(material);
                MaterialMeltingRecipeBuilder.material(materialId, temp, new FluidStack(fluid.get(), 144))
                        .save(provider, location("tools/materials/melting/" + material.getName())); //melting recipes for tool parts

                metal(provider, material.getName(), fluid.get(), GTMaterialHelper.findTemp(material)).optional().ore().metal().dust().plate().gear().coin().rod().wire().sheetmetal().geore();

                MaterialFluidRecipeBuilder.material(materialId)
                        .setFluid(material.getFluidTag(), 144)
                        .setTemperature(temp)
                        .save(provider, location("tools/materials/casting/" + material.getName())); //casting recipes for material tool parts
            }
        }
    }

    public SmelteryRecipeBuilder metal(Consumer<FinishedRecipe> consumer, String name, Fluid fluid, int temp) {
        return SmelteryRecipeBuilder.fluid(consumer, location(name), fluid).castingFolder("smeltery/casting/metal").meltingFolder("smeltery/melting/metal").temperature(temp);
    }

    public Optional<Fluid> getFluid(Material material, TagKey<Fluid> fluidTag) {
        ResourceLocation tagLoc = fluidTag.location();
        String fluidName = tagLoc.getPath();
        Fluid output;
        ResourceLocation fluidLoc = new ResourceLocation(TConstruct.MOD_ID, "molten_" + fluidName);
        if(material.hasProperty(PropertyKey.FLUID)) {
            output = material.getFluid();
        }
        else if(ForgeRegistries.FLUIDS.containsKey(fluidLoc)) {
            output = ForgeRegistries.FLUIDS.getValue(fluidLoc);
        } else {
            GMConstruct.LOGGER.warn("Material {} does not have a fluid, no smeltery recipes will be added for this material.", material);
            return Optional.empty();
        }
        return Optional.of(output);
    }

    @Override
    public String getModId() {
        return GMConstruct.MOD_ID;
    }
}
