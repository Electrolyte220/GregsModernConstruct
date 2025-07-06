package dev.electrolyte.gm_construct.data;

import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.data.pack.GTDynamicPackContents;
import com.mojang.datafixers.util.Pair;
import dev.electrolyte.gm_construct.GMConstruct;
import dev.electrolyte.gm_construct.helper.GTMaterialHelper;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.resources.IoSupplier;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Set;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GMCDynamicResourcePack implements PackResources {

    private static final ObjectSet<String> CLIENT_DOMAINS = new ObjectOpenHashSet<>();
    private static final GTDynamicPackContents DATA = new GTDynamicPackContents();
    private final String name;

    static {
        CLIENT_DOMAINS.addAll(Sets.newHashSet(GMConstruct.MOD_ID, "minecraft", "forge", "c"));
    }

    public GMCDynamicResourcePack(String name) {
        this.name = name;
    }

    public static void clearData() {
        DATA.clearData();
    }

    public static void generateAllAssets() {
        JsonObject langObject = new JsonObject();
        for(Material material : GTMaterialHelper.REGISTERED_TOOL_MATERIALS) {
            Pair<ResourceLocation, byte[]> data = MaterialRenderInfoGeneration.INSTANCE.generateRenderInfo(GMConstruct.id(material.getName()), material);
            DATA.addToData(data.getFirst(), data.getSecond());

            Pair<String, String> entry = LangGeneration.INSTANCE.generateLangEntry(material);
            langObject.addProperty(entry.getFirst(), entry.getSecond());
        }
        DATA.addToData(new ResourceLocation(GMConstruct.MOD_ID, "lang/en_us.json"), langObject.toString().getBytes(StandardCharsets.UTF_8));
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
            return DATA.getResource(resourceLocation);
        }
        return null;
    }

    @Override
    public void listResources(PackType packType, String namespace, String path, ResourceOutput resourceOutput) {
        if(packType == PackType.CLIENT_RESOURCES) {
            DATA.listResources(namespace, path, resourceOutput);
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
            return (T) new PackMetadataSection(Component.literal("GMConstruct Dynamic Assets"),
                    SharedConstants.getCurrentVersion().getPackVersion(PackType.CLIENT_RESOURCES));
        }
        return null;
    }

    @Override
    public String packId() {
        return name;
    }

    @Override
    public boolean isBuiltin() {
        return true;
    }

    @Override
    public void close() {}
}
