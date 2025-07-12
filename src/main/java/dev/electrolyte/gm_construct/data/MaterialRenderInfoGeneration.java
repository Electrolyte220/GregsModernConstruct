package dev.electrolyte.gm_construct.data;

import com.google.gson.JsonObject;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.mojang.datafixers.util.Pair;
import dev.electrolyte.gm_construct.GMConstruct;
import dev.electrolyte.gm_construct.config.GMCConfig;
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
        MaterialSpriteInfoBuilder spriteBuilder = new MaterialSpriteInfoBuilder(texture)
                .fallbacks("metal")
                .colorMapper(GreyToColorMapping.builderFromBlack().addARGB(63, material.getMaterialARGB()).build());
        if(material.hasProperty(PropertyKey.TOOL)) {
            spriteBuilder.meleeHarvest().ranged();
        }
        if(material.hasProperty(PropertyKey.ARMOR)) {
            spriteBuilder.armor();
            if(GMCConfig.GENERATE_PLATE_SHIELD.get()) {
                spriteBuilder.shieldCore();
            }
        }
        MaterialSpriteInfo spriteInfo = spriteBuilder.build();
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
        private String[] fallbacks = new String[0];
        private int color = -1;
        @Setter
        private int luminosity = 0;
        @Setter
        private MaterialGeneratorInfo generator = null;

        public void color(int color) {
            if ((color & 0xFF000000) == 0) {
                color |= 0xFF000000;
            }
            this.color = color;
        }

        public void fallbacks(String... fallbacks) {
            this.fallbacks = fallbacks;
        }

        @CheckReturnValue
        public JsonObject build(MaterialVariantId id) {
            JsonObject json = new JsonObject();
            MaterialRenderInfo.LOADABLE.serialize(new MaterialRenderInfo(id, texture, fallbacks, color, luminosity), json);
            if (generator != null) {
                json.add("generator", MaterialGeneratorInfo.LOADABLE.serialize(generator));
            }
            return json;
        }
    }
}
