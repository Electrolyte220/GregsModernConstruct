package dev.electrolyte.expandedtic.data;

import com.google.common.collect.Sets;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.mojang.datafixers.util.Pair;
import dev.electrolyte.expandedtic.ExpandedTiC;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.resources.IoSupplier;
import slimeknights.tconstruct.library.materials.definition.MaterialId;

import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ETDynamicDataPack implements PackResources {

    private static final ObjectSet<String> SERVER_DOMAINS = new ObjectOpenHashSet<>();
    private static final Map<ResourceLocation, byte[]> DATA = new HashMap<>();

    private String name;

    static {
        SERVER_DOMAINS.addAll(Sets.newHashSet(ExpandedTiC.MOD_ID, "minecraft", "forge", "c"));
    }

    public ETDynamicDataPack(String name) {
        this.name = name;
    }

    public static void clearData() {
        DATA.clear();
    }

    @Override
    public @Nullable IoSupplier<InputStream> getRootResource(String... pElements) {
        return null;
    }

    @Override
    public @Nullable IoSupplier<InputStream> getResource(PackType pPackType, ResourceLocation pLocation) {
        if(pPackType == PackType.SERVER_DATA) {
            var arr = DATA.get(pLocation);
            if(arr != null) return () -> new ByteArrayInputStream(arr);
            else return null;
        } else {
            return null;
        }
    }

    public static void generateAllMaterialData() {
        for(Material material : ExpandedTiC.REGISTERED_TOOL_MATERIALS) {
            MaterialId key = ExpandedTiC.materialId(material.getName());

            Pair<ResourceLocation, byte[]> data = MaterialDataGeneration.INSTANCE.generateMaterialData(key, material);
            DATA.put(data.getFirst(), data.getSecond());

            data = MaterialStatsGeneration.INSTANCE.generateMaterialStats(material);
            DATA.put(data.getFirst(), data.getSecond());

            data = MaterialTraitsGeneration.INSTANCE.generateMaterialTraits(material);
            DATA.put(data.getFirst(), data.getSecond());
        }
    }

    @Override
    public void listResources(PackType pPackType, String pNamespace, String pPath, ResourceOutput pResourceOutput) {
        if(pPackType == PackType.SERVER_DATA) {
            if(!pPath.endsWith("/")) pPath += "/";
            final String finalPath = pPath;
            DATA.keySet().stream().filter(Objects::nonNull).filter(loc -> loc.getPath().startsWith(finalPath))
                    .forEach(id -> {
                        IoSupplier<InputStream> resource = this.getResource(pPackType, id);
                        if(resource != null) pResourceOutput.accept(id, resource);
                    });
        }
    }

    @Override
    public Set<String> getNamespaces(PackType pType) {
        return pType == PackType.SERVER_DATA ? SERVER_DOMAINS : Set.of();
    }

    @Override
    public @Nullable <T> T getMetadataSection(MetadataSectionSerializer<T> pDeserializer) {
        if(pDeserializer == PackMetadataSection.TYPE) {
            return (T) new PackMetadataSection(Component.literal("ExpandedTiC Dynamic Data"),
                    SharedConstants.getCurrentVersion().getPackVersion(PackType.SERVER_DATA));
        }
        return null;
    }

    @Override
    public String packId() {
        return name;
    }

    @Override
    public void close() {}
}
