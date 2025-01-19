package dev.electrolyte.gm_construct.datagen;

import dev.electrolyte.gm_construct.GMConstruct;
import net.minecraft.data.PackOutput;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;
import slimeknights.tconstruct.library.materials.definition.MaterialId;

public class MaterialDataProvider extends AbstractMaterialDataProvider {
    public MaterialDataProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void addMaterials() {
        addRedirect(new MaterialId(TConstruct.MOD_ID, "rose_gold"), redirect(GMConstruct.materialId("rose_gold")));
        addRedirect(new MaterialId(TConstruct.MOD_ID, "bronze"), redirect(GMConstruct.materialId("bronze")));
        addRedirect(new MaterialId(TConstruct.MOD_ID, "invar"), redirect(GMConstruct.materialId("invar")));
        addRedirect(new MaterialId(TConstruct.MOD_ID, "iron"), redirect(GMConstruct.materialId("iron")));
        addRedirect(new MaterialId(TConstruct.MOD_ID, "steel"), redirect(GMConstruct.materialId("steel")));
        addRedirect(new MaterialId(TConstruct.MOD_ID, "tungsten"), redirect(GMConstruct.materialId("tungsten")));
    }

    @Override
    public String getName() {
        return GMConstruct.MOD_ID + " Redirected Material Definitions";
    }
}
