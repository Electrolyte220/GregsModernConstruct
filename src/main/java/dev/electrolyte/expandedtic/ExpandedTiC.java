package dev.electrolyte.expandedtic;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;
import com.mojang.logging.LogUtils;
import dev.electrolyte.expandedtic.config.ETModConfig;
import dev.electrolyte.expandedtic.data.ETDynamicDataPack;
import dev.electrolyte.expandedtic.data.ETDynamicResourcePack;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import slimeknights.tconstruct.library.materials.definition.MaterialId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Mod(ExpandedTiC.MOD_ID)
public class ExpandedTiC {

    public static final String MOD_ID = "expandedtic";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final GTRegistrate REGISTRATE = GTRegistrate.create(MOD_ID);

    public static Collection<Material> REGISTERED_TOOL_MATERIALS = new ArrayList<>();

    public ExpandedTiC() {
        MinecraftForge.EVENT_BUS.register(this);
        REGISTRATE.registerRegistrate();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::gatherData);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupMaterials);
        ETModConfig.init();
    }

    private void gatherData(GatherDataEvent event) {


        //ETMaterialSpriteProvider spriteProvider = new ETMaterialSpriteProvider();
        //event.getGenerator().addProvider(event.includeClient(), new ETMaterialRenderInfoProvider(event.getGenerator().getPackOutput(), spriteProvider, event.getExistingFileHelper()));
        //event.getGenerator().addProvider(event.includeClient(), new ETMaterialLangProvider(event.getGenerator().getPackOutput()));
    }

    public static ResourceLocation id(String location) {
        return new ResourceLocation(ExpandedTiC.MOD_ID, location);
    }

    public static MaterialId materialId(String location) {
        return new MaterialId(ExpandedTiC.MOD_ID, location);
    }

    private void setupMaterials(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            REGISTERED_TOOL_MATERIALS = GTCEuAPI.materialManager.getRegisteredMaterials().stream().filter(m -> {
                for(String s : ETModConfig.INSTANCE.ignoredGTMaterials) {
                    if(m.getName().equals(s)) return false;
                }
                return m.hasProperty(PropertyKey.TOOL);
            }).collect(Collectors.toCollection(ArrayList::new));

            ETDynamicDataPack.generateAllMaterialData();
            ETDynamicResourcePack.generateAllAssets();
        });
    }
}
