package dev.electrolyte.gm_construct.event;

import dev.electrolyte.gm_construct.GMConstruct;
import dev.electrolyte.gm_construct.data.GMCDynamicDataPack;
import dev.electrolyte.gm_construct.data.GMCDynamicResourcePack;
import dev.electrolyte.gm_construct.helper.GTMaterialHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.loading.FMLLoader;

@Mod.EventBusSubscriber(modid = GMConstruct.MOD_ID, bus = Bus.FORGE)
public class GMCForgeEventHandler {

    @SubscribeEvent
    public static void reloadResources(AddReloadListenerEvent event) {
        GMCDynamicDataPack.clearData();
        GMCDynamicResourcePack.clearData();
        GTMaterialHelper.REGISTERED_TOOL_MATERIALS = GTMaterialHelper.getRegisteredMaterials();
        GMCDynamicDataPack.generateAllMaterialData();

        if(FMLLoader.getDist() != Dist.DEDICATED_SERVER) {
            GMCDynamicResourcePack.generateAllAssets();
        }
    }
}
