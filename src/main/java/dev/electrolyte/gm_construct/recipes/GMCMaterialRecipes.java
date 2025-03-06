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

import java.util.function.Consumer;

public class GMCMaterialRecipes implements IMaterialRecipeHelper, ISmelteryRecipeHelper {

    public GMCMaterialRecipes(Consumer<FinishedRecipe> provider) {
        register(provider);
    }

    public void register(Consumer<FinishedRecipe> provider) {
        for(Material material : GTCEuAPI.materialManager.getRegisteredMaterials()) {
            if(! material.hasProperty(PropertyKey.ORE))
                continue;
            Material smeltsIntoMaterial = material.getProperty(PropertyKey.ORE).getDirectSmeltResult();
            if(smeltsIntoMaterial == null)
                continue;
            if(material.getName().equals(smeltsIntoMaterial.getName()))
                continue;
            if(! smeltsIntoMaterial.hasProperty(PropertyKey.FLUID))
                continue;
            metal(provider, material.getName(), smeltsIntoMaterial.getFluidTag(), smeltsIntoMaterial.getFluid()).optional().metal().ore().rawOre().dust().plate().gear().coin().rod().wire().sheetmetal().geore();
        }

        for(Material material : GTMaterialHelper.getRegisteredMaterials()) {
            if(! material.hasProperty(PropertyKey.FLUID)) {
                GMConstruct.LOGGER.warn("Material {} does not have a fluid, no solidification recipes will be added for this material.", material);
                continue;
            }
            MaterialId materialId = GMConstruct.materialId(material.getName());
            metalMaterialRecipe(provider, materialId, "tools/materials/", material.getName(), false); //repairing via material nugget/ingot/block in tool station

            if(GMCConfig.GENERATE_MELTING_CASTING_RECIPES.get()) {
                MaterialMeltingRecipeBuilder.material(materialId, material.getFluid().getFluidType().getTemperature() - 300, new FluidStack(material.getFluid(), 144))
                        .save(provider, location("tools/materials/melting/" + material.getName())); //melting recipes for tool parts

                metal(provider, material.getName(), material.getFluidTag(), material.getFluid()).optional().metal().ore().rawOre().dust().plate().gear().coin().rod().wire().sheetmetal().geore();

                MaterialFluidRecipeBuilder.material(materialId)
                        .setFluid(material.getFluidTag(), 144)
                        .setTemperature(material.getFluid().getFluidType().getTemperature() - 300)
                        .save(provider, location("tools/materials/casting/" + material.getName())); //casting recipes for material tool parts
            }
        }
    }

    public SmelteryRecipeBuilder metal(Consumer<FinishedRecipe> consumer, String name, TagKey<Fluid> fluidTag, Fluid fallback) {
        ResourceLocation tagLoc = fluidTag.location();
        String fluidName = tagLoc.getPath();
        Fluid output;
        ResourceLocation fluidLoc = new ResourceLocation(TConstruct.MOD_ID, "molten_" + fluidName);
        if(ForgeRegistries.FLUIDS.containsKey(fluidLoc)) {
            output = ForgeRegistries.FLUIDS.getValue(fluidLoc);
        } else {
            output = fallback;
        }
        return SmelteryRecipeBuilder.fluid(consumer, location(name), output).castingFolder("smeltery/casting/metal").meltingFolder("smeltery/melting/metal");
    }

    @Override
    public String getModId() {
        return GMConstruct.MOD_ID;
    }
}
