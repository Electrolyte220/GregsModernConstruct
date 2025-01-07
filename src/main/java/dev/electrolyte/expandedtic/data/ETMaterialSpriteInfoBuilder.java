package dev.electrolyte.expandedtic.data;

import com.google.common.collect.ImmutableSet;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
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

import java.util.Set;

//AbstractMaterialSpriteProvider$MaterialSpriteInfoBuilder
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class ETMaterialSpriteInfoBuilder {

    /** Builder for material sprite info */
        private static final String[] EMPTY_STRING_ARRAY = new String[0];
        private final ResourceLocation texture;
        private String[] fallbacks = EMPTY_STRING_ARRAY;
        private final ImmutableSet.Builder<MaterialStatsId> statTypes = ImmutableSet.builder();

        /** Transformer to modify textures */
        @Setter
        @Accessors(fluent = true)
        private ISpriteTransformer transformer;

        /** Sets the fallbacks */
        public ETMaterialSpriteInfoBuilder fallbacks(String... fallbacks) {
            this.fallbacks = fallbacks;
            return this;
        }

        /** Sets the transformer to a color mapping transform */
        @CanIgnoreReturnValue
        public ETMaterialSpriteInfoBuilder colorMapper(IColorMapping mapping) {
            return transformer(new RecolorSpriteTransformer(mapping));
        }

        /** Adds a stat type as supported */
        public ETMaterialSpriteInfoBuilder statType(MaterialStatsId statsId) {
            statTypes.add(statsId);
            return this;
        }

        /** Adds a stat type as supported */
        public ETMaterialSpriteInfoBuilder statType(MaterialStatsId... statsId) {
            statTypes.add(statsId);
            return this;
        }

        /** Adds repair kits */
        public ETMaterialSpriteInfoBuilder repairKit() {
            return statType(StatlessMaterialStats.REPAIR_KIT.getIdentifier());
        }

        /** Adds stat types for melee and harvest tools - head, handle and extra */
        public ETMaterialSpriteInfoBuilder meleeHarvest() {
            statType(HeadMaterialStats.ID);
            statType(HandleMaterialStats.ID);
            statType(StatlessMaterialStats.BINDING.getIdentifier());
            repairKit();
            return this;
        }

        /** Adds stat types for ranged tools - includes limb and grip */
        public ETMaterialSpriteInfoBuilder ranged() {
            statType(LimbMaterialStats.ID);
            statType(GripMaterialStats.ID);
            repairKit();
            return this;
        }

        /** Adds stat types for maille */
        public ETMaterialSpriteInfoBuilder maille() {
            statType(StatlessMaterialStats.MAILLE.getIdentifier());
            statType(TinkerPartSpriteProvider.ARMOR_MAILLE);
            return this;
        }

        /** Adds stat types for armor, all plating plus maille */
        public ETMaterialSpriteInfoBuilder armor() {
            statType(TinkerPartSpriteProvider.ARMOR_PLATING);
            for (MaterialStatType<?> type : PlatingMaterialStats.TYPES) {
                statType(type.getId());
            }
            maille();
            repairKit();
            return this;
        }

        /** Makes this work as the wood part for a shield */
        public ETMaterialSpriteInfoBuilder shieldCore() {
            return statType(StatlessMaterialStats.SHIELD_CORE.getIdentifier());
        }

        /** Builds a material sprite info */
        public MaterialSpriteInfo build() {
            if (transformer == null) {
                throw new IllegalStateException("Material must have a transformer for a sprite provider");
            }
            Set<MaterialStatsId> supportedStats = this.statTypes.build();
            if (supportedStats.isEmpty()) {
                throw new IllegalStateException("Material must support at least one stat type");
            }
            return new MaterialSpriteInfo(texture, fallbacks, transformer, supportedStats);
    }
}
