package dev.electrolyte.expandedtic.data;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.mojang.datafixers.util.Pair;

import java.util.HashMap;

public class LangGeneration {

    protected static final LangGeneration INSTANCE = new LangGeneration();
    private static final HashMap<String, String> SPECIAL_LANG = new HashMap<>();

    static {
        SPECIAL_LANG.put("material.expandedtic.hsse", "HSS-E");
    }

    protected Pair<String, String> generateLangEntry(Material material) {
        String materialKey = "material.expandedtic." + material.getName();
        if(SPECIAL_LANG.containsKey(materialKey)) {
            return new Pair<>(materialKey, SPECIAL_LANG.get(materialKey));
        } else {
            return new Pair<>(materialKey, FormattingUtil.toEnglishName(material.getName()));
        }
    }
}
