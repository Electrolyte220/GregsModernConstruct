package dev.electrolyte.gm_construct.data;

import com.google.gson.JsonElement;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.ArmorProperty.ArmorMaterial;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.mojang.datafixers.util.Pair;
import dev.electrolyte.gm_construct.GMConstruct;
import dev.electrolyte.gm_construct.config.GMCConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorItem.Type;
import net.minecraft.world.item.Tiers;
import slimeknights.tconstruct.library.materials.definition.MaterialManager;
import slimeknights.tconstruct.library.materials.json.MaterialStatJson;
import slimeknights.tconstruct.library.materials.stats.IMaterialStats;
import slimeknights.tconstruct.library.materials.stats.MaterialStatType;
import slimeknights.tconstruct.tools.stats.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MaterialStatsGeneration {

    protected static final MaterialStatsGeneration INSTANCE = new MaterialStatsGeneration();

    protected Pair<ResourceLocation, byte[]> generateMaterialStats(Material material) {
        List<IMaterialStats> stats = new ArrayList<>();
        if(material.hasProperty(PropertyKey.TOOL)) {
            stats.addAll(generateToolStats(material));
        }
        if(material.hasProperty(PropertyKey.ARMOR)) {
            stats.addAll(generateArmorStats(material));
        }
        MaterialStatJson json = convertMaterialStats(stats);
        return new Pair<>(new ResourceLocation(GMConstruct.MOD_ID, "tinkering/materials/stats/" + material.getName() + ".json"), MaterialManager.GSON.toJsonTree(json).toString().getBytes(StandardCharsets.UTF_8));
    }

    private List<IMaterialStats> generateToolStats(Material material) {
        return List.of(
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
        );
    }

    private List<IMaterialStats> generateArmorStats(Material material) {
        ArmorMaterial armorMat = material.getProperty(PropertyKey.ARMOR).getArmorMaterial();
        List<IMaterialStats> stats = new ArrayList<>();
        for(ArmorItem.Type slotType : ArmorItem.Type.values()) {
            PlatingMaterialStats.Builder armorBuilder = PlatingMaterialStats.builder()
                    .durabilityFactor((float) (armorMat.getDurabilityForType(slotType) * GMCConfig.GLOBAL_ARMOR_DURABILITY_MODIFIER.get()))
                    .armor(armorMat.getDefenseForType(Type.BOOTS), armorMat.getDefenseForType(Type.LEGGINGS), armorMat.getDefenseForType(Type.CHESTPLATE), armorMat.getDefenseForType(Type.HELMET))
                    .toughness(armorMat.getToughness())
                    .knockbackResistance(armorMat.getKnockbackResistance());
            PlatingMaterialStats matStats = armorBuilder.build(slotType);
            stats.add(matStats);
        }
        if(GMCConfig.GENERATE_PLATE_SHIELD.get()) {
            stats.add(PlatingMaterialStats.builder()
                    .shieldDurability((int) (armorMat.getDurabilityForType(Type.CHESTPLATE) * GMCConfig.GLOBAL_SHIELD_DURABILITY_MODIFIER.get()))
                    .toughness(armorMat.getToughness())
                    .knockbackResistance(armorMat.getKnockbackResistance())
                    .buildShield());
        }
        stats.add(StatlessMaterialStats.MAILLE);
        return stats;
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
