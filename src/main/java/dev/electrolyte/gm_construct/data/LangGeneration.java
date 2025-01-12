package dev.electrolyte.gm_construct.data;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.mojang.datafixers.util.Pair;

import java.util.HashMap;

public class LangGeneration {

    protected static final LangGeneration INSTANCE = new LangGeneration();
    private static final HashMap<String, String> SPECIAL_LANG = new HashMap<>();

    static {
        SPECIAL_LANG.put("material.gm_construct.hsse", "HSS-E");
    }

    protected Pair<String, String> generateLangEntry(Material material) {
        String materialKey = "material.gm_construct." + material.getName();
        if(SPECIAL_LANG.containsKey(materialKey)) {
            return new Pair<>(materialKey, SPECIAL_LANG.get(materialKey));
        } else {
            return new Pair<>(materialKey, FormattingUtil.toEnglishName(material.getName()));
        }
    }
}
