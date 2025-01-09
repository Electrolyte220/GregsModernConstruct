package dev.electrolyte.expandedtic.event;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import dev.electrolyte.expandedtic.ExpandedTiC;
import dev.electrolyte.expandedtic.data.ETDynamicDataPack;
import dev.electrolyte.expandedtic.data.ETDynamicResourcePack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.Pack.Position;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import java.util.HashSet;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = ExpandedTiC.MOD_ID, bus = Bus.MOD)
public class ETEventHandler {

    @SubscribeEvent
    public static void addPacks(AddPackFindersEvent event) {
        if(event.getPackType() == PackType.SERVER_DATA) {

            ExpandedTiC.REGISTERED_TOOL_MATERIALS = GTCEuAPI.materialManager.getRegisteredMaterials().stream().filter(m -> {
                for(String s : ExpandedTiC.CONFIG_INSTANCE.gtMaterialGeneration.ignoredGTMaterials) {
                    if(m.getName().equals(s)) return false;
                }
                return m.hasProperty(PropertyKey.TOOL);
            }).collect(Collectors.toCollection(HashSet::new));

            ETDynamicDataPack.clearData();
            ETDynamicDataPack.generateAllMaterialData();

            event.addRepositorySource(c -> c.accept(Pack.readMetaAndCreate(
                    "expandedtic:dynamic_data",
                    Component.literal("expandedtic:dynamic_data"),
                    true,
                    ETDynamicDataPack::new,
                    PackType.SERVER_DATA,
                    Position.BOTTOM,
                    PackSource.BUILT_IN)));
        }
        if(event.getPackType() == PackType.CLIENT_RESOURCES) {
            ETDynamicResourcePack.clearData();
            ETDynamicResourcePack.generateAllAssets();

            event.addRepositorySource(c -> c.accept(Pack.readMetaAndCreate(
                    "expandedtic:dynamic_assets",
                    Component.literal("expandedtic:dynamic_assets"),
                    true,
                    ETDynamicResourcePack::new,
                    PackType.CLIENT_RESOURCES,
                    Position.BOTTOM,
                    PackSource.BUILT_IN)));
        }
    }
}
