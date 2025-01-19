package dev.electrolyte.gm_construct.data;

import com.google.gson.JsonElement;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.mojang.datafixers.util.Pair;
import dev.electrolyte.gm_construct.GMConstruct;
import net.minecraft.resources.ResourceLocation;
import slimeknights.mantle.fluid.tooltip.FluidTooltipHandler;
import slimeknights.mantle.fluid.tooltip.FluidUnit;

import java.nio.charset.StandardCharsets;

public class FluidTooltipGeneration {

    public static final FluidTooltipGeneration INSTANCE = new FluidTooltipGeneration();

    public Pair<ResourceLocation, byte[]> generateMaterialFluidTooltip(Material material) {
        FluidUnit fluidUnit = new FluidUnit(GMConstruct.id(material.getName()).toString(), 144);
        JsonElement obj = FluidTooltipHandler.GSON.toJsonTree(fluidUnit);
        return new Pair<>(GMConstruct.id(material.getName() + ".json"), obj.toString().getBytes(StandardCharsets.UTF_8));
    }
}
