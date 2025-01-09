package dev.electrolyte.expandedtic.datagen;

import dev.electrolyte.expandedtic.ExpandedTiC;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class ETMaterialLangProvider extends LanguageProvider {
    public ETMaterialLangProvider(PackOutput output) {
        super(output, ExpandedTiC.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.add("config.expandedtic.option.enableGTIntegration", "Enable/Disable GT Integration");
        this.add("config.expandedtic.option.gtMaterialGeneration", "GT Material Generation");
        this.add("config.expandedtic.option.ignoredGTMaterials", "Ignored GT Materials");
        this.add("config.expandedtic.option.ignoredGTMaterials.entry", "");
        this.add("config.expandedtic.option.generateExtruderRecipes", "Generate Extruder Recipes");
        this.add("config.expandedtic.option.generateFluidSolidificationRecipes", "Generate Fluid Solidifier Recipes");
        this.add("config.expandedtic.option.ignoredDefaultMatDefGen", "Ignored Default Material Definitions");
        this.add("config.expandedtic.option.ignoredDefaultMatDefGen.entry", "");
        this.add("config.expandedtic.option.ignoredDefaultMatStatsGen", "Ignored Default Material Stats");
        this.add("config.expandedtic.option.ignoredDefaultMatStatsGen.entry", "");
        this.add("config.expandedtic.option.ignoredDefaultMatTraitsGen", "Ignored Default Material Traits");
        this.add("config.expandedtic.option.ignoredDefaultMatTraitsGen.entry", "");
        this.add("config.expandedtic.option.headModifiers", "Global Head Modifiers");
        this.add("config.expandedtic.option.headDurabilityModifier", "Global Head Durability Modifier");
        this.add("config.expandedtic.option.headMiningSpeedModifier", "Global Mining Speed Modifier");
        this.add("config.expandedtic.option.headAttackDamageModifier", "Global Attack Damage Modifier");
        this.add("config.expandedtic.option.limbModifiers", "Global Limb Modifiers");
        this.add("config.expandedtic.option.limbDurabilityModifier", "Global Limb Durability Modifier");
        this.add("config.expandedtic.option.limbDrawSpeedModifier", "Global Limb Draw Speed Modifier");
        this.add("config.expandedtic.option.limbVelocityModifier", "Global Limb Velocity Modifier");
        this.add("config.expandedtic.option.limbAccuracyModifier", "Global Limb Accuracy Modifier");
        this.add("config.expandedtic.option.gripModifiers", "Global Grip Modifiers");
        this.add("config.expandedtic.option.gripDurabilityModifier", "Global Grip Durability Modifier");
        this.add("config.expandedtic.option.gripAccuracyModifier", "Global Grip Accuracy Modifier");
        this.add("config.expandedtic.option.gripMeleeDamageModifier", "Global Grip Melee Damage Modifier");
        this.add("config.expandedtic.option.handleModifiers", "Global Handle Modifiers");
        this.add("config.expandedtic.option.handleDurabilityModifier", "Global Handle Durability Modifier");
        this.add("config.expandedtic.option.handleMiningSpeedModifier", "Global Handle Mining Speed Modifier");
        this.add("config.expandedtic.option.handleAttackDamageModifier", "Global Handle Attack Damage Modifier");
        this.add("config.expandedtic.option.handleAttackSpeedModifier", "Global Handle Attack Speed Modifier");
    }
}
