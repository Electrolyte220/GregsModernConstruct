package dev.electrolyte.expandedtic.datagen;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import dev.electrolyte.expandedtic.ExpandedTiC;
import dev.electrolyte.expandedtic.helper.GTMaterialHelper;
import slimeknights.tconstruct.library.client.data.material.AbstractMaterialSpriteProvider;
import slimeknights.tconstruct.library.client.data.spritetransformer.GreyToColorMapping;

public class ETMaterialSpriteProvider extends AbstractMaterialSpriteProvider {

    @Override
    public String getName() {
        return ExpandedTiC.MOD_ID + " Material Sprites";
    }

    @Override
    protected void addAllMaterials() {
        for(Material material : GTMaterialHelper.REGISTERED_TOOL_MATERIALS) {
            buildMaterial(ExpandedTiC.materialId(material.getName()))
                    .meleeHarvest().ranged()
                    .fallbacks("metal")
                    .colorMapper(GreyToColorMapping.builderFromBlack().addARGB(63, material.getMaterialARGB()).build());
        }
    }
}
