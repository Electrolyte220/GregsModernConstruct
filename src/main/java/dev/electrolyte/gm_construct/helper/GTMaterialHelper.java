package dev.electrolyte.gm_construct.helper;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import dev.electrolyte.gm_construct.config.GMCConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.ForgeRegistries;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.fluids.TinkerFluids;

import java.util.HashSet;
import java.util.stream.Collectors;

public class GTMaterialHelper {

    public static HashSet<Material> REGISTERED_TOOL_MATERIALS = new HashSet<>();

    public static HashSet<Material> getRegisteredMaterials() {
        return GTCEuAPI.materialManager.getRegisteredMaterials().stream().filter(m -> {
            for(String s : GMCConfig.IGNORED_GT_MATERIALS.get()) {
                if(m.getName().equals(s))
                    return false;
            }
            return m.hasProperty(PropertyKey.TOOL);
        }).collect(Collectors.toCollection(HashSet::new));
    }

    public static int findTemp(Material material) {
        ResourceLocation fluidLoc = new ResourceLocation(TConstruct.MOD_ID, "molten_" + material.getName());
        if(ForgeRegistries.FLUIDS.containsKey(fluidLoc)) {
            return ForgeRegistries.FLUIDS.getValue(fluidLoc).getFluidType().getTemperature() - 300;
        }
        int materialTemp = material.getFluid().getFluidType().getTemperature();
        int lava = Fluids.LAVA.getFluidType().getTemperature();
        int bb = TinkerFluids.blazingBlood.get().getFluidType().getTemperature();
        if(materialTemp <= lava) return materialTemp - 300;
        return Math.min(materialTemp, bb) - 300;
    }
}
