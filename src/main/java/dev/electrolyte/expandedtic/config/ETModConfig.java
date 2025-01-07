package dev.electrolyte.expandedtic.config;

import dev.electrolyte.expandedtic.ExpandedTiC;
import dev.toma.configuration.Configuration;
import dev.toma.configuration.config.Config;
import dev.toma.configuration.config.Configurable;
import dev.toma.configuration.config.format.ConfigFormats;

@Config(id = ExpandedTiC.MOD_ID)
public class ETModConfig {

    public static ETModConfig INSTANCE;
    private static final Object LOCK = new Object();

    public static void init() {
        synchronized(LOCK) {
            if(INSTANCE == null) {
                INSTANCE = Configuration.registerConfig(ETModConfig.class, ConfigFormats.yaml()).getConfigInstance();
            }
        }
    }

    @Configurable
    @Configurable.Comment({"[REQUIRES WORLD RESTART]", "Gregtech Materials to ignore generating tinker's material information for"})
    public String[] ignoredGTMaterials = new String[] {
            "bronze", "cobalt", "copper", "diamond", "flint", "invar", "iron", "netherite", "polybenzimidazole", "polyethylene", "polytetrafluoroethylene", "rose_gold", "rubber", "steel", "silicone_rubber", "styrene_butadiene_rubber", "wood"
    };
}
