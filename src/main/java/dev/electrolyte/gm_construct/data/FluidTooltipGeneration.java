package dev.electrolyte.gm_construct.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.mojang.datafixers.util.Pair;
import dev.electrolyte.gm_construct.GMConstruct;
import dev.electrolyte.gm_construct.helper.GTMaterialHelper;
import net.minecraft.resources.ResourceLocation;

import java.nio.charset.StandardCharsets;

public class FluidTooltipGeneration {

    public static final FluidTooltipGeneration INSTANCE = new FluidTooltipGeneration();

    public Pair<ResourceLocation, byte[]> generateMaterialFluidTooltips() {
        JsonObject tagObj = new JsonObject();
        tagObj.addProperty("replace", false);
        JsonArray tagValues = new JsonArray();
        tagValues.add("#tconstruct:tooltips/metal");
        for(Material material : GTMaterialHelper.getRegisteredMaterials()) {
            JsonObject materialObj = new JsonObject();
            materialObj.addProperty("required", false);
            materialObj.addProperty("id", "#" + material.getFluidTag().location());
            tagValues.add(materialObj);
        }
        tagObj.add("values", tagValues);
        return new Pair<>(GMConstruct.id("tags/fluids/tooltips/metal.json"), tagObj.toString().getBytes(StandardCharsets.UTF_8));
    }
}
