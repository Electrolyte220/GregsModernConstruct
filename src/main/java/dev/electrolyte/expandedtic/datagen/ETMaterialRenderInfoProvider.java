package dev.electrolyte.expandedtic.datagen;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import dev.electrolyte.expandedtic.ExpandedTiC;
import dev.electrolyte.expandedtic.helper.GTMaterialHelper;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import slimeknights.tconstruct.library.client.data.material.AbstractMaterialRenderInfoProvider;
import slimeknights.tconstruct.library.client.data.material.AbstractMaterialSpriteProvider;

import javax.annotation.Nullable;

public class ETMaterialRenderInfoProvider extends AbstractMaterialRenderInfoProvider {
    public ETMaterialRenderInfoProvider(PackOutput packOutput, @Nullable AbstractMaterialSpriteProvider materialSprites, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, materialSprites, existingFileHelper);
    }

    @Override
    protected void addMaterialRenderInfo() {
        for(Material material : GTMaterialHelper.REGISTERED_TOOL_MATERIALS) {
            buildRenderInfo(ExpandedTiC.materialId(material.getName())).color(material.getMaterialRGB()).fallbacks("metal");
        }
    }

    @Override
    public String getName() {
        return ExpandedTiC.MOD_ID + " Material Render Info";
    }
}
