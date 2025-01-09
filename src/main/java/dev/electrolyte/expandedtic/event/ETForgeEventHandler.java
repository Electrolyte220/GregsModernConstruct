package dev.electrolyte.expandedtic.event;

import dev.electrolyte.expandedtic.ExpandedTiC;
import dev.electrolyte.expandedtic.data.ETDynamicDataPack;
import dev.electrolyte.expandedtic.data.ETDynamicResourcePack;
import dev.electrolyte.expandedtic.helper.GTMaterialHelper;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = ExpandedTiC.MOD_ID, bus = Bus.FORGE)
public class ETForgeEventHandler {

    @SubscribeEvent
    public static void reloadResources(AddReloadListenerEvent event) {
        ETDynamicDataPack.clearData();
        ETDynamicResourcePack.clearData();
        if(ModList.get().isLoaded("gtceu") && ExpandedTiC.CONFIG_INSTANCE.gtMaterialGeneration.enableGTIntegration) {
            GTMaterialHelper.REGISTERED_TOOL_MATERIALS = GTMaterialHelper.getRegisteredMaterials();
            ETDynamicDataPack.generateAllMaterialData();
            ETDynamicResourcePack.generateAllAssets();
        }
    }
}
