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

    protected Pair<ResourceLocation, byte[]> generateMaterialStats(Material material) {
        MaterialStatJson json = convertMaterialStats(List.of(
                new HeadMaterialStats(material.getToolTier().getUses(), material.getToolTier().getSpeed(), getHarvestTier(material.getToolTier().getLevel()), material.getToolTier().getAttackDamageBonus()),
                new LimbMaterialStats(material.getToolTier().getUses(), 10 * 0.75f * 0.75f, 0.5F * 0.75f * 0.75f, 0.5f * 0.75f), //0.75f is for config multipliers
                new GripMaterialStats(material.getToolTier().getUses(), 0.5f * 0.75f, (material.getMass() / 10F) * 0.75f * 0.75f),
                HandleMaterialStats.multipliers().durability(0.5F * 0.75f).build(),
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
