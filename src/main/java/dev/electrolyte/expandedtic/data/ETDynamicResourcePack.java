package dev.electrolyte.expandedtic.data;

import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
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

import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ETDynamicResourcePack implements PackResources {

    private static final ObjectSet<String> CLIENT_DOMAINS = new ObjectOpenHashSet<>();
    private static final ConcurrentMap<ResourceLocation, byte[]> DATA = new ConcurrentHashMap<>();
    private final String name;

    static {
        CLIENT_DOMAINS.addAll(Sets.newHashSet(ExpandedTiC.MOD_ID, "minecraft", "forge", "c"));
    }

    public ETDynamicResourcePack(String name) {
        this.name = name;
    }

    public static void clearData() {
        DATA.clear();
    }

    public static void generateAllAssets() {
        JsonObject langObject = new JsonObject();
        for(Material material : ExpandedTiC.REGISTERED_TOOL_MATERIALS) {
            Pair<ResourceLocation, byte[]> data = MaterialRenderInfoGeneration.INSTANCE.generateRenderInfo(ExpandedTiC.id(material.getName()), material);
            DATA.put(data.getFirst(), data.getSecond());

            Pair<String, String> entry = LangGeneration.INSTANCE.generateLangEntry(material);
            langObject.addProperty(entry.getFirst(), entry.getSecond());
        }
        DATA.put(new ResourceLocation(ExpandedTiC.MOD_ID, "lang/en_us.json"), langObject.toString().getBytes(StandardCharsets.UTF_8));
    }

    @Nullable
    @Override
    public IoSupplier<InputStream> getRootResource(String... strings) {
        return null;
    }

    @Nullable
    @Override
    public IoSupplier<InputStream> getResource(PackType packType, ResourceLocation resourceLocation) {
        if(packType == PackType.CLIENT_RESOURCES) {
            if(DATA.containsKey(resourceLocation)) return () -> new ByteArrayInputStream(DATA.get(resourceLocation));
        }
        return null;
    }

    @Override
    public void listResources(PackType packType, String namespace, String path, ResourceOutput resourceOutput) {
        if(packType == PackType.CLIENT_RESOURCES) {
            if(!path.endsWith("/")) path += "/";
            final String finalPath = path;
            DATA.keySet().stream().filter(Objects::nonNull).filter(loc -> loc.getPath().startsWith(finalPath))
                    .forEach(id -> {
                        IoSupplier<InputStream> resource = this.getResource(packType, id);
                        if(resource != null) {
                            resourceOutput.accept(id, resource);
                        }
                    });
        }
    }

    @Override
    public Set<String> getNamespaces(PackType packType) {
        return packType == PackType.CLIENT_RESOURCES ? CLIENT_DOMAINS : Set.of();
    }

    @Nullable
    @Override
    public <T> T getMetadataSection(MetadataSectionSerializer<T> metadataSectionSerializer) {
        if(metadataSectionSerializer == PackMetadataSection.TYPE) {
            return (T) new PackMetadataSection(Component.literal("ExpandedTiC Dynamic Assets"),
                    SharedConstants.getCurrentVersion().getPackVersion(PackType.CLIENT_RESOURCES));
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
