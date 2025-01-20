package dev.electrolyte.gm_construct.data;

import com.google.gson.JsonObject;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.mojang.datafixers.util.Pair;
import dev.electrolyte.gm_construct.GMConstruct;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.resources.ResourceLocation;
import slimeknights.tconstruct.library.client.data.material.AbstractMaterialSpriteProvider.MaterialSpriteInfo;
import slimeknights.tconstruct.library.client.data.spritetransformer.GreyToColorMapping;
import slimeknights.tconstruct.library.client.materials.MaterialGeneratorInfo;
import slimeknights.tconstruct.library.client.materials.MaterialRenderInfo;
import slimeknights.tconstruct.library.materials.definition.MaterialVariantId;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;

public class MaterialRenderInfoGeneration {

    protected static final MaterialRenderInfoGeneration INSTANCE = new MaterialRenderInfoGeneration();

    protected Pair<ResourceLocation, byte[]> generateRenderInfo(ResourceLocation texture, Material material) {
        JsonObject json = getBuilder(texture, material).build(GMConstruct.materialId(material.getName()));
        return new Pair<>(new ResourceLocation(GMConstruct.MOD_ID, "tinkering/materials/" + material.getName() + ".json"), json.toString().getBytes(StandardCharsets.UTF_8));
    }

    private RenderInfoBuilder getBuilder(ResourceLocation texture, Material material) {
        RenderInfoBuilder builder = new RenderInfoBuilder().texture(texture);
        MaterialSpriteInfo spriteInfo = new MaterialSpriteInfoBuilder(texture)
                .meleeHarvest().ranged().fallbacks("metal")
                .colorMapper(GreyToColorMapping.builderFromBlack().addARGB(63, material.getMaterialARGB()).build()).build();
        builder.fallbacks(spriteInfo.getFallbacks());
        // colors are in AABBGGRR format, we want AARRGGBB, so swap red and blue
        int color = spriteInfo.getTransformer().getFallbackColor();
        if (color != 0xFFFFFFFF) {
            builder.color((color & 0x00FF00) | ((color >> 16) & 0x0000FF) | ((color << 16) & 0xFF0000));
        }
        builder.generator(spriteInfo);
        return builder;
    }

    //AbstractMaterialRenderInfoProvider$RenderInfoBuilder
    @Accessors(fluent = true, chain = true)
    public static class RenderInfoBuilder {
        @Setter
        @Nullable
        private ResourceLocation texture = null;
        @Setter
        @Nullable
        private ResourceLocation parent = null;
        private String[] fallbacks = new String[0];
        private int color = -1;
        @Setter
        private int luminosity = 0;
        @Setter
        private MaterialGeneratorInfo generator = null;

        /** Sets the color */
        public RenderInfoBuilder color(int color) {
            if ((color & 0xFF000000) == 0) {
                color |= 0xFF000000;
            }
            this.color = color;
            return this;
        }

        /** Sets the parent to the given material ID */
        public RenderInfoBuilder parentMaterial(MaterialVariantId material) {
            return parent(material.getLocation('/'));
        }

        /** Sets the fallback names */
        public RenderInfoBuilder fallbacks(String... fallbacks) {
            this.fallbacks = fallbacks;
            return this;
        }

        /** Sets the texture from another material variant */
        public RenderInfoBuilder materialTexture(MaterialVariantId variantId) {
            return texture(variantId.getLocation('_'));
        }

        /** Tells the builder to skip the unique texture for this material */
        public RenderInfoBuilder skipUniqueTexture() {
            return texture(null);
        }

        /** Builds the material */
        @CheckReturnValue
        public JsonObject build(MaterialVariantId id) {
            JsonObject json = new JsonObject();
            if (parent != null) {
                json.addProperty("parent", parent.toString());
            }
            MaterialRenderInfo.LOADABLE.serialize(new MaterialRenderInfo(id, texture, fallbacks, color, luminosity), json);
            if (generator != null) {
                json.add("generator", MaterialGeneratorInfo.LOADABLE.serialize(generator));
            }
            return json;
        }
    }
}
