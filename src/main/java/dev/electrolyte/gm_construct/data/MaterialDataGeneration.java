package dev.electrolyte.gm_construct.data;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.mojang.datafixers.util.Pair;
import dev.electrolyte.gm_construct.GMConstruct;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.NotCondition;
import net.minecraftforge.common.crafting.conditions.OrCondition;
import net.minecraftforge.common.crafting.conditions.TagEmptyCondition;
import slimeknights.tconstruct.common.json.ConfigEnabledCondition;
import slimeknights.tconstruct.library.json.JsonRedirect;
import slimeknights.tconstruct.library.materials.definition.IMaterial;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.library.materials.definition.MaterialManager;
import slimeknights.tconstruct.library.materials.json.MaterialJson;

import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;

import static slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider.ORDER_COMPAT;

public class MaterialDataGeneration {

    protected static final MaterialDataGeneration INSTANCE = new MaterialDataGeneration();

    protected Pair<ResourceLocation, byte[]> generateMaterialData(MaterialId key, Material material) {
        ICondition conditions = new OrCondition(new NotCondition(new TagEmptyCondition("forge", "ingots/" + material.getName())), ConfigEnabledCondition.FORCE_INTEGRATION_MATERIALS);
        MaterialJson json = convertMaterialData(new DataMaterial(
                new slimeknights.tconstruct.library.materials.definition.Material(key, 2, ORDER_COMPAT, false, false), conditions, null));
        return new Pair<>(GMConstruct.id("tinkering/materials/definition/" + material.getName() + ".json"), MaterialManager.GSON.toJsonTree(json).toString().getBytes(StandardCharsets.UTF_8));
    }

    protected MaterialJson convertMaterialData(DataMaterial data) {
        IMaterial material = data.material;
        JsonRedirect[] redirect = data.redirect;
        if(redirect != null && redirect.length == 0) {
            redirect = null;
        }
        if(material == null) {
            return new MaterialJson(data.condition, null, null, null, null, redirect);
        }
        return new MaterialJson(data.condition, material.isCraftable(), material.getTier(), material.getSortOrder(), material.isHidden(), redirect);
    }

    protected record DataMaterial(@Nullable IMaterial material, @Nullable ICondition condition, JsonRedirect[] redirect) {}
}
