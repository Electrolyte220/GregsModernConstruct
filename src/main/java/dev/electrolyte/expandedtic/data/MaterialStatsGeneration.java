package dev.electrolyte.expandedtic.data;

import com.google.gson.JsonElement;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.mojang.datafixers.util.Pair;
import dev.electrolyte.expandedtic.ExpandedTiC;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tiers;
import slimeknights.tconstruct.library.materials.definition.MaterialManager;
import slimeknights.tconstruct.library.materials.json.MaterialStatJson;
import slimeknights.tconstruct.library.materials.stats.IMaterialStats;
import slimeknights.tconstruct.library.materials.stats.MaterialStatType;
import slimeknights.tconstruct.tools.stats.*;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class MaterialStatsGeneration {

    protected static final MaterialStatsGeneration INSTANCE = new MaterialStatsGeneration();

    private static final float HEAD_DURABILITY_MODIFIER = ExpandedTiC.CONFIG_INSTANCE.gtMaterialGeneration.headModifiers.headDurabilityModifier;
    private static final float HEAD_MINING_SPEED_MODIFIER = ExpandedTiC.CONFIG_INSTANCE.gtMaterialGeneration.headModifiers.headMiningSpeedModifier;
    private static final float HEAD_ATTACK_DAMAGE_MODIFIER = ExpandedTiC.CONFIG_INSTANCE.gtMaterialGeneration.headModifiers.headAttackDamageModifier;

    private static final float LIMB_DURABILITY_MODIFIER = ExpandedTiC.CONFIG_INSTANCE.gtMaterialGeneration.limbModifiers.limbDurabilityModifier;
    private static final float LIMB_DRAW_SPEED_MODIFIER = ExpandedTiC.CONFIG_INSTANCE.gtMaterialGeneration.limbModifiers.limbDrawSpeedModifier;
    private static final float LIMB_VELOCITY_MODIFIER = ExpandedTiC.CONFIG_INSTANCE.gtMaterialGeneration.limbModifiers.limbVelocityModifier;
    private static final float LIMB_ACCURACY_MODIFIER = ExpandedTiC.CONFIG_INSTANCE.gtMaterialGeneration.limbModifiers.limbAccuracyModifier;

    private static final float GRIP_DURABILITY_MODIFIER = ExpandedTiC.CONFIG_INSTANCE.gtMaterialGeneration.gripModifiers.gripDurabilityModifier;
    private static final float GRIP_ACCURACY_MODIFIER = ExpandedTiC.CONFIG_INSTANCE.gtMaterialGeneration.gripModifiers.gripAccuracyModifier;
    private static final float GRIP_MELEE_DAMAGE_MODIFIER = ExpandedTiC.CONFIG_INSTANCE.gtMaterialGeneration.gripModifiers.gripMeleeDamageModifier;

    private static final float HANDLE_DURABILITY_MODIFIER = ExpandedTiC.CONFIG_INSTANCE.gtMaterialGeneration.handleModifiers.handleDurabilityModifier;
    private static final float HANDLE_MINING_SPEED_MODIFIER = ExpandedTiC.CONFIG_INSTANCE.gtMaterialGeneration.handleModifiers.handleMiningSpeedModifier;
    private static final float HANDLE_ATTACK_DAMAGE_MODIFIER = ExpandedTiC.CONFIG_INSTANCE.gtMaterialGeneration.handleModifiers.handleAttackDamageModifier;
    private static final float HANDLE_ATTACK_SPEED_MODIFIER = ExpandedTiC.CONFIG_INSTANCE.gtMaterialGeneration.handleModifiers.handleAttackSpeedModifier;

    protected Pair<ResourceLocation, byte[]> generateMaterialStats(Material material) {
        MaterialStatJson json = convertMaterialStats(List.of(
                new HeadMaterialStats(
                        (int) (material.getToolTier().getUses() * HEAD_DURABILITY_MODIFIER),
                        material.getToolTier().getSpeed() * HEAD_MINING_SPEED_MODIFIER,
                        getHarvestTier(material.getToolTier().getLevel()),
                        material.getToolTier().getAttackDamageBonus() * HEAD_ATTACK_DAMAGE_MODIFIER),
                new LimbMaterialStats(
                        (int) (material.getToolTier().getUses() * LIMB_DURABILITY_MODIFIER),
                        material.getMass() * LIMB_DRAW_SPEED_MODIFIER,
                        -material.getMass() * LIMB_VELOCITY_MODIFIER,
                        -material.getMass() * LIMB_ACCURACY_MODIFIER),
                new GripMaterialStats(
                        GRIP_DURABILITY_MODIFIER,
                        GRIP_ACCURACY_MODIFIER,
                        GRIP_MELEE_DAMAGE_MODIFIER),
                HandleMaterialStats.multipliers()
                        .durability(HANDLE_DURABILITY_MODIFIER)
                        .miningSpeed(HANDLE_MINING_SPEED_MODIFIER)
                        .attackDamage(HANDLE_ATTACK_DAMAGE_MODIFIER)
                        .attackSpeed(HANDLE_ATTACK_SPEED_MODIFIER)
                        .build(),
                StatlessMaterialStats.BINDING
        ));
        return new Pair<>(new ResourceLocation(ExpandedTiC.MOD_ID, "tinkering/materials/stats/" + material.getName() + ".json"), MaterialManager.GSON.toJsonTree(json).toString().getBytes(StandardCharsets.UTF_8));
    }

    private MaterialStatJson convertMaterialStats(List<IMaterialStats> stats) {
        return new MaterialStatJson(stats.stream()
                .collect(Collectors.toMap(IMaterialStats::getIdentifier, stat -> encodeStats(stat, stat.getType()))));
    }

    private <T extends IMaterialStats> JsonElement encodeStats(IMaterialStats stats, MaterialStatType<T> type) {
        return type.getLoadable().serialize((T) stats);
    }

    private Tiers getHarvestTier(int tier) {
        return switch(tier) {
            case 0 -> Tiers.WOOD;
            case 1 -> Tiers.STONE;
            case 2 -> Tiers.IRON;
            case 3 -> Tiers.DIAMOND;
            default -> Tiers.NETHERITE;
        };
    }
}
