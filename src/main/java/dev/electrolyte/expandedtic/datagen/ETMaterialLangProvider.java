package dev.electrolyte.expandedtic.datagen;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import dev.electrolyte.expandedtic.ExpandedTiC;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class ETMaterialLangProvider extends LanguageProvider {
    public ETMaterialLangProvider(PackOutput output) {
        super(output, ExpandedTiC.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        for(Material material : ExpandedTiC.REGISTERED_TOOL_MATERIALS) {
            this.add(String.format("material.expandedtic.%s", material.getName()), FormattingUtil.toEnglishName(material.getName()));
        }
    }
}
