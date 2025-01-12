package dev.electrolyte.gm_construct.data;

import com.google.gson.JsonElement;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.mojang.datafixers.util.Pair;
import dev.electrolyte.gm_construct.GMConstruct;
import dev.electrolyte.gm_construct.config.GMCConfig;
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
                new HeadMaterialStats(
                        (int) (material.getToolTier().getUses() * GMCConfig.GLOBAL_HEAD_DURABILITY_MODIFIER.get()),
                        (float) (material.getToolTier().getSpeed() * GMCConfig.GLOBAL_HEAD_MINING_SPEED_MODIFIER.get()),
                        getHarvestTier(material.getToolTier().getLevel()),
                        (float) (material.getToolTier().getAttackDamageBonus() * GMCConfig.GLOBAL_HEAD_ATTACK_DAMAGE_MODIFIER.get())),
                new LimbMaterialStats(
                        (int) (material.getToolTier().getUses() * GMCConfig.GLOBAL_LIMB_DURABILITY_MODIFIER.get()),
                        (float) (material.getMass() * GMCConfig.GLOBAL_LIMB_DRAW_SPEED_MODIFIER.get()),
                        (float) (material.getMass() * GMCConfig.GLOBAL_LIMB_VELOCITY_MODIFIER.get()),
                        (float) (material.getMass() * GMCConfig.GLOBAL_LIMB_ACCURACY_MODIFIER.get())),
                new GripMaterialStats(
                        (float) (1 * GMCConfig.GLOBAL_GRIP_DURABILITY_MODIFIER.get()),
                        (float) (1 * GMCConfig.GLOBAL_GRIP_ACCURACY_MODIFIER.get()),
                        (float) (1 * GMCConfig.GLOBAL_GRIP_MELEE_DAMAGE_MODIFIER.get())),
                HandleMaterialStats.multipliers()
                        .durability((float) (1 * GMCConfig.GLOBAL_HANDLE_DURABILITY_MODIFIER.get()))
                        .miningSpeed((float) (1 * GMCConfig.GLOBAL_HANDLE_MINING_SPEED_MODIFIER.get()))
                        .attackDamage((float) (1 * GMCConfig.GLOBAL_HANDLE_ATTACK_DAMAGE_MODIFIER.get()))
                        .attackSpeed((float) (1 * GMCConfig.GLOBAL_HANDLE_ATTACK_SPEED_MODIFIER.get()))
                        .build(),
                StatlessMaterialStats.BINDING
        ));
        return new Pair<>(new ResourceLocation(GMConstruct.MOD_ID, "tinkering/materials/stats/" + material.getName() + ".json"), MaterialManager.GSON.toJsonTree(json).toString().getBytes(StandardCharsets.UTF_8));
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
