package dev.electrolyte.gm_construct.config;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber
public class GMCConfig {

    public static ForgeConfigSpec COMMON_CONFIG;

    public static BooleanValue GENERATE_MELTING_CASTING_RECIPES;
    public static BooleanValue GENERATE_EXTRUDER_RECIPES;
    public static BooleanValue GENERATE_FLUID_SOLIDIFICATION_RECIPES;

    public static ConfigValue<List<String>> IGNORED_GT_MATERIALS;
    public static BooleanValue GENERATE_PLATE_SHIELD;

    public static DoubleValue GLOBAL_HEAD_DURABILITY_MODIFIER;
    public static DoubleValue GLOBAL_HEAD_MINING_SPEED_MODIFIER;
    public static DoubleValue GLOBAL_HEAD_ATTACK_DAMAGE_MODIFIER;

    public static DoubleValue GLOBAL_LIMB_DURABILITY_MODIFIER;
    public static DoubleValue GLOBAL_LIMB_DRAW_SPEED_MODIFIER;
    public static DoubleValue GLOBAL_LIMB_VELOCITY_MODIFIER;
    public static DoubleValue GLOBAL_LIMB_ACCURACY_MODIFIER;

    public static DoubleValue GLOBAL_GRIP_DURABILITY_MODIFIER;
    public static DoubleValue GLOBAL_GRIP_ACCURACY_MODIFIER;
    public static DoubleValue GLOBAL_GRIP_MELEE_DAMAGE_MODIFIER;

    public static DoubleValue GLOBAL_HANDLE_DURABILITY_MODIFIER;
    public static DoubleValue GLOBAL_HANDLE_MINING_SPEED_MODIFIER;
    public static DoubleValue GLOBAL_HANDLE_ATTACK_DAMAGE_MODIFIER;
    public static DoubleValue GLOBAL_HANDLE_ATTACK_SPEED_MODIFIER;

    public static DoubleValue GLOBAL_ARMOR_DURABILITY_MODIFIER;
    public static DoubleValue GLOBAL_SHIELD_DURABILITY_MODIFIER;

    static {
        Builder COMMON_BUILDER = new Builder();
        COMMON_BUILDER.comment("Configuration related to material recipe generation").push("Recipe Integration").worldRestart();
        GENERATE_MELTING_CASTING_RECIPES = COMMON_BUILDER.define("generateMeltingAndCastingRecipes", false);
        GENERATE_EXTRUDER_RECIPES = COMMON_BUILDER.define("generateExtruderRecipes", true);
        GENERATE_FLUID_SOLIDIFICATION_RECIPES = COMMON_BUILDER.define("generateFluidSolidificationRecipes", false);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Configuration related to material modification.").push("Material Modification");
        IGNORED_GT_MATERIALS = COMMON_BUILDER.comment("Disable generating tinker's material information for the following GT materials.").define("ignoredGTMaterials", Lists.newArrayList(
                "bronze", "cobalt", "copper", "diamond", "flint", "invar", "iron", "netherite", "polybenzimidazole", "polyethylene",
                "polytetrafluoroethylene", "rose_gold", "rubber", "steel", "silicone_rubber", "styrene_butadiene_rubber", "wood"));
        GENERATE_PLATE_SHIELD = COMMON_BUILDER.comment("Generate plate shields for GT materials.").define("generatePlateShield", true);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Configuration related to global modifiers.").push("Global Modifiers");

        COMMON_BUILDER.comment("Configuration related to head global modifiers").push("Head Global Modifiers");
        GLOBAL_HEAD_DURABILITY_MODIFIER = COMMON_BUILDER.defineInRange("globalHeadDurabilityModifier", 1.0d, Double.MIN_VALUE, Double.MAX_VALUE);
        GLOBAL_HEAD_MINING_SPEED_MODIFIER = COMMON_BUILDER.defineInRange("globalHeadMiningSpeedModifier", 1.0d, Double.MIN_VALUE, Double.MAX_VALUE);
        GLOBAL_HEAD_ATTACK_DAMAGE_MODIFIER = COMMON_BUILDER.defineInRange("globalHeadAttackDamageModifier", 1.0d, Double.MIN_VALUE, Double.MAX_VALUE);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Configuration related to limb global modifiers").push("Limb Global Modifiers");
        GLOBAL_LIMB_DURABILITY_MODIFIER = COMMON_BUILDER.defineInRange("globalLimbDurabilityModifier", 1.0d, Double.MIN_VALUE, Double.MAX_VALUE);
        GLOBAL_LIMB_DRAW_SPEED_MODIFIER = COMMON_BUILDER.defineInRange("globalLimbDrawSpeedModifier", 0.001d, Double.MIN_VALUE, Double.MAX_VALUE);
        GLOBAL_LIMB_VELOCITY_MODIFIER = COMMON_BUILDER.defineInRange("globalLimbVelocityModifier", -0.0008d, Double.MIN_VALUE, Double.MAX_VALUE);
        GLOBAL_LIMB_ACCURACY_MODIFIER = COMMON_BUILDER.defineInRange("globalLimbAccuracyModifier", -0.0005d, Double.MIN_VALUE, Double.MAX_VALUE);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Configuration related to grip global modifiers").push("Grip Global Modifiers");
        GLOBAL_GRIP_DURABILITY_MODIFIER = COMMON_BUILDER.defineInRange("globalGripDurabilityModifier", 0.2d, Double.MIN_VALUE, Double.MAX_VALUE);
        GLOBAL_GRIP_ACCURACY_MODIFIER = COMMON_BUILDER.defineInRange("globalGripAccuracyModifier", 0.16d, Double.MIN_VALUE, Double.MAX_VALUE);
        GLOBAL_GRIP_MELEE_DAMAGE_MODIFIER = COMMON_BUILDER.defineInRange("globalMeleeDamageModifier", 1.5d, Double.MIN_VALUE, Double.MAX_VALUE);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Configuration related to handle global modifiers").push("Handle Global Modifiers");
        GLOBAL_HANDLE_DURABILITY_MODIFIER = COMMON_BUILDER.defineInRange("globalHandleDurabilityModifier", 1.0d, 0.1d, Double.MAX_VALUE);
        GLOBAL_HANDLE_MINING_SPEED_MODIFIER = COMMON_BUILDER.defineInRange("globalHandleMiningSpeedModifier", 1.0d, 0.1d, Double.MAX_VALUE);
        GLOBAL_HANDLE_ATTACK_DAMAGE_MODIFIER = COMMON_BUILDER.defineInRange("globalHandleAttackDamageModifier", 1.0d, 0.1d, Double.MAX_VALUE);
        GLOBAL_HANDLE_ATTACK_SPEED_MODIFIER = COMMON_BUILDER.defineInRange("globalHandleAttackSpeedModifier", 1.0d, 0.1d, Double.MAX_VALUE);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Configuration related to armor global modifiers").push("Armor Global Modifiers");
        GLOBAL_ARMOR_DURABILITY_MODIFIER = COMMON_BUILDER.defineInRange("globalArmorDurabilityModifier", 0.15d, 0.1d, Double.MAX_VALUE);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Configuration related to shield global modifiers").push("Shield Global Modifiers");
        GLOBAL_SHIELD_DURABILITY_MODIFIER = COMMON_BUILDER.defineInRange("globalShieldDurabilityModifier", 1.0d, 0.1d, Double.MAX_VALUE);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.pop();

        COMMON_CONFIG = COMMON_BUILDER.build();
    }
}
