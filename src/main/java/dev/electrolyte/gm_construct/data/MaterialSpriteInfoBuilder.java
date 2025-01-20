package dev.electrolyte.gm_construct.data;

import com.google.common.collect.ImmutableSet;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.CheckReturnValue;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.resources.ResourceLocation;
import slimeknights.tconstruct.library.client.data.material.AbstractMaterialSpriteProvider.MaterialSpriteInfo;
import slimeknights.tconstruct.library.client.data.spritetransformer.IColorMapping;
import slimeknights.tconstruct.library.client.data.spritetransformer.ISpriteTransformer;
import slimeknights.tconstruct.library.client.data.spritetransformer.RecolorSpriteTransformer;
import slimeknights.tconstruct.library.materials.stats.MaterialStatType;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.tools.data.sprite.TinkerPartSpriteProvider;
import slimeknights.tconstruct.tools.stats.*;

import javax.annotation.Nullable;
import java.util.Set;

//AbstractMaterialSpriteProvider$MaterialSpriteInfoBuilder
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@CanIgnoreReturnValue
@Accessors(fluent = true)
public class MaterialSpriteInfoBuilder {

    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    private final ResourceLocation texture;
    private String[] fallbacks = EMPTY_STRING_ARRAY;
    private final ImmutableSet.Builder<MaterialStatsId> statTypes = ImmutableSet.builder();

    /** Transformer to modify textures */
    @Setter
    @Nullable
    private ISpriteTransformer transformer;
    @Setter
    private boolean variant = false;

    /** Sets the fallbacks */
    public MaterialSpriteInfoBuilder fallbacks(String... fallbacks) {
        this.fallbacks = fallbacks;
        return this;
    }

    /** Sets the transformer to a color mapping transform */
    public MaterialSpriteInfoBuilder colorMapper(IColorMapping mapping) {
        return transformer(new RecolorSpriteTransformer(mapping));
    }

    /** Marks this as a variant texture, which is skipped by some sprites such as ancient tools (which can never obtain them) */
    public MaterialSpriteInfoBuilder variant() {
        return variant(true);
    }

    /** Adds a stat type as supported */
    public MaterialSpriteInfoBuilder statType(MaterialStatsId statsId) {
        statTypes.add(statsId);
        return this;
    }

    /** Adds a stat type as supported */
    public MaterialSpriteInfoBuilder statType(MaterialStatsId... statsId) {
        statTypes.add(statsId);
        return this;
    }

    /** Adds repair kits */
    public MaterialSpriteInfoBuilder repairKit() {
        return statType(StatlessMaterialStats.REPAIR_KIT.getIdentifier());
    }

    /** Adds stat types for melee and harvest tools - head, handle and extra */
    public MaterialSpriteInfoBuilder meleeHarvest() {
        statType(HeadMaterialStats.ID);
        statType(HandleMaterialStats.ID);
        statType(StatlessMaterialStats.BINDING.getIdentifier());
        repairKit();
        return this;
    }

    /** Adds stat types for ranged tools - includes limb and grip */
    public MaterialSpriteInfoBuilder ranged() {
        statType(LimbMaterialStats.ID);
        statType(GripMaterialStats.ID);
        repairKit();
        return this;
    }

    /** Adds stat types for maille */
    public MaterialSpriteInfoBuilder maille() {
        statType(StatlessMaterialStats.MAILLE.getIdentifier());
        statType(TinkerPartSpriteProvider.ARMOR_MAILLE);
        return this;
    }

    /** Adds stat types for armor, all plating plus maille */
    public MaterialSpriteInfoBuilder armor() {
        statType(TinkerPartSpriteProvider.ARMOR_PLATING);
        for (MaterialStatType<?> type : PlatingMaterialStats.TYPES) {
            statType(type.getId());
        }
        maille();
        repairKit();
        return this;
    }

    /** Makes this work as the wood part for a shield */
    public MaterialSpriteInfoBuilder shieldCore() {
        return statType(StatlessMaterialStats.SHIELD_CORE.getIdentifier());
    }

    /** Builds a material sprite info */
    @CheckReturnValue
    public MaterialSpriteInfo build() {
        if (transformer == null) {
            throw new IllegalStateException("Material must have a transformer for a sprite provider");
        }
        Set<MaterialStatsId> supportedStats = this.statTypes.build();
        if (supportedStats.isEmpty()) {
            throw new IllegalStateException("Material must support at least one stat type");
        }
        return new MaterialSpriteInfo(texture, fallbacks, transformer, supportedStats, variant);
    }
}
