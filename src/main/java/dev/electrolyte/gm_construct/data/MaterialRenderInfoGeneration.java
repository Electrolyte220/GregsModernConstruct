package dev.electrolyte.gm_construct.data;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.mojang.datafixers.util.Pair;
import dev.electrolyte.gm_construct.GMConstruct;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.resources.ResourceLocation;
import slimeknights.mantle.data.loadable.common.ColorLoadable;
import slimeknights.tconstruct.library.client.data.material.AbstractMaterialSpriteProvider.MaterialSpriteInfo;
import slimeknights.tconstruct.library.client.data.spritetransformer.GreyToColorMapping;
import slimeknights.tconstruct.library.client.materials.MaterialRenderInfoJson;
import slimeknights.tconstruct.library.client.materials.MaterialRenderInfoLoader;
import slimeknights.tconstruct.library.materials.definition.MaterialVariantId;

import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;

public class MaterialRenderInfoGeneration {

    protected static final MaterialRenderInfoGeneration INSTANCE = new MaterialRenderInfoGeneration();

    protected Pair<ResourceLocation, byte[]> generateRenderInfo(ResourceLocation texture, Material material) {
        MaterialRenderInfoJson json = getBuilder(texture, material).build();
        return new Pair<>(new ResourceLocation(GMConstruct.MOD_ID, "tinkering/materials/" + material.getName() + ".json"), MaterialRenderInfoLoader.GSON.toJsonTree(json).toString().getBytes(StandardCharsets.UTF_8));
    }

    private RenderInfoBuilder getBuilder(ResourceLocation texture, Material material) {
        RenderInfoBuilder builder = new RenderInfoBuilder();
        MaterialSpriteInfo spriteInfo = new GMCMaterialSpriteInfoBuilder(texture)
                .meleeHarvest().ranged().fallbacks("metal")
                .colorMapper(GreyToColorMapping.builderFromBlack().addARGB(63, material.getMaterialARGB()).build()).build();
        if (spriteInfo != null) {
            String[] fallbacks = spriteInfo.getFallbacks();
            if (fallbacks.length > 0) {
                builder.fallbacks(fallbacks);
            }
            // colors are in AABBGGRR format, we want AARRGGBB, so swap red and blue
            int color = spriteInfo.getTransformer().getFallbackColor();
            if (color != 0xFFFFFFFF) {
                builder.color((color & 0x00FF00) | ((color >> 16) & 0x0000FF) | ((color << 16) & 0xFF0000));
            }
            builder.generator(spriteInfo);
        }
        return builder;
    }

    //AbstractMaterialRenderInfoProvider$RenderInfoBuilder
    @Accessors(fluent = true, chain = true)
    public static class RenderInfoBuilder {
        @Setter
        private ResourceLocation texture = null;
        private String[] fallbacks;
        private int color = -1;
        @Setter
        private boolean skipUniqueTexture;
        @Setter
        private int luminosity = 0;
        @Setter
        private MaterialRenderInfoJson.MaterialGeneratorJson generator = null;

        /** Sets the color */
        public RenderInfoBuilder color(int color) {
            if ((color & 0xFF000000) == 0) {
                color |= 0xFF000000;
            }
            this.color = color;
            return this;
        }

        /** Sets the fallback names */
        public RenderInfoBuilder fallbacks(@Nullable String... fallbacks) {
            this.fallbacks = fallbacks;
            return this;
        }

        /** Sets the texture from another material variant */
        public RenderInfoBuilder materialTexture(MaterialVariantId variantId) {
            return texture(variantId.getLocation('_'));
        }

        /** Builds the material */
        public MaterialRenderInfoJson build() {
            return new MaterialRenderInfoJson(texture, fallbacks, ColorLoadable.ALPHA.getString(color), skipUniqueTexture ? Boolean.TRUE : null, luminosity, generator);
        }
    }
}
