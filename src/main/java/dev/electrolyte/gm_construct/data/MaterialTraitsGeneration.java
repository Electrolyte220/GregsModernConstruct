package dev.electrolyte.gm_construct.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.mojang.datafixers.util.Pair;
import dev.electrolyte.gm_construct.GMConstruct;
import net.minecraft.resources.ResourceLocation;
import slimeknights.tconstruct.library.materials.json.MaterialTraitsJson;
import slimeknights.tconstruct.library.materials.traits.MaterialTraits;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.data.ModifierIds;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

public class MaterialTraitsGeneration {

    private static final Gson GSON = (new GsonBuilder())
            .registerTypeAdapter(ModifierEntry.class, ModifierEntry.LOADABLE)
            .registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer())
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    protected static final MaterialTraitsGeneration INSTANCE = new MaterialTraitsGeneration();
    public static final HashMap<String, Pair<ModifierId, Integer>> SPECIAL_TRAITS = new HashMap<>();

    static {
        SPECIAL_TRAITS.put(GTMaterials.Neutronium.getName(), Pair.of(ModifierIds.reinforced, 3));
        SPECIAL_TRAITS.put(GTMaterials.Osmiridium.getName(), Pair.of(ModifierIds.reinforced, 3));
        SPECIAL_TRAITS.put(GTMaterials.TungstenSteel.getName(), Pair.of(ModifierIds.reinforced, 3));
        SPECIAL_TRAITS.put(GTMaterials.Titanium.getName(), Pair.of(ModifierIds.reinforced, 2));
        SPECIAL_TRAITS.put(GTMaterials.Iridium.getName(), Pair.of(ModifierIds.reinforced, 2));
        SPECIAL_TRAITS.put(GTMaterials.DamascusSteel.getName(), Pair.of(ModifierIds.reinforced, 2));
        SPECIAL_TRAITS.put(GTMaterials.Osmium.getName(), Pair.of(ModifierIds.reinforced, 2));
        SPECIAL_TRAITS.put(GTMaterials.StainlessSteel.getName(), Pair.of(ModifierIds.reinforced, 1));
        SPECIAL_TRAITS.put(GTMaterials.BlueSteel.getName(), Pair.of(ModifierIds.reinforced, 1));
        SPECIAL_TRAITS.put(GTMaterials.RedSteel.getName(), Pair.of(ModifierIds.reinforced, 1));
        SPECIAL_TRAITS.put(GTMaterials.BlackSteel.getName(), Pair.of(ModifierIds.reinforced, 1));
        SPECIAL_TRAITS.put(GTMaterials.SteelMagnetic.getName(), Pair.of(ModifierIds.reinforced, 1));
        SPECIAL_TRAITS.put(GTMaterials.RoseGold.getName(), Pair.of(ModifierIds.enhanced, 1));
        SPECIAL_TRAITS.put(GTMaterials.Bronze.getName(), Pair.of(ModifierIds.maintained, 1));
        SPECIAL_TRAITS.put(GTMaterials.Invar.getName(), Pair.of(ModifierIds.invariant, 1));
        SPECIAL_TRAITS.put(GTMaterials.Iron.getName(), Pair.of(TinkerModifiers.magnetic.getId(), 1));
        SPECIAL_TRAITS.put(GTMaterials.Steel.getName(), Pair.of(ModifierIds.ductile, 1));
    }

    protected Pair<ResourceLocation, byte[]> generateMaterialTraits(Material material) {
        MaterialTraits.Builder trait = new MaterialTraits.Builder();
        if(SPECIAL_TRAITS.containsKey(material.getName())) {
            Pair<ModifierId, Integer> entry = SPECIAL_TRAITS.get(material.getName());
            trait.setDefaultTraits(List.of(new ModifierEntry(entry.getFirst(), entry.getSecond())));
        }
        MaterialTraitsJson json = trait.serialize();
        return new Pair<>(new ResourceLocation(GMConstruct.MOD_ID, "tinkering/materials/traits/" + material.getName() + ".json"), GSON.toJsonTree(json).toString().getBytes(StandardCharsets.UTF_8));
    }
}
