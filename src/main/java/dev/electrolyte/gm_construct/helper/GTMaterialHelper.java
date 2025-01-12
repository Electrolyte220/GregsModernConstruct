package dev.electrolyte.gm_construct.helper;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import dev.electrolyte.gm_construct.config.GMCConfig;

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
}
