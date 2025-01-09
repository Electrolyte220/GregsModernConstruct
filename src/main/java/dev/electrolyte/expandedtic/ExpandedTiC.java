package dev.electrolyte.expandedtic;

import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;
import com.mojang.logging.LogUtils;
import dev.electrolyte.expandedtic.config.ETModConfig;
import dev.electrolyte.expandedtic.data.ETDynamicResourcePack;
import dev.electrolyte.expandedtic.datagen.ETMaterialLangProvider;
import dev.electrolyte.expandedtic.helper.GTMaterialHelper;
import dev.toma.configuration.Configuration;
import dev.toma.configuration.config.format.ConfigFormats;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import slimeknights.tconstruct.library.materials.definition.MaterialId;

@Mod(ExpandedTiC.MOD_ID)
public class ExpandedTiC {

    public static final String MOD_ID = "expandedtic";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final GTRegistrate REGISTRATE = GTRegistrate.create(MOD_ID);
    public static ETModConfig CONFIG_INSTANCE;

    public ExpandedTiC() {
        MinecraftForge.EVENT_BUS.register(this);
        REGISTRATE.registerRegistrate();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::gatherData);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupGTMaterials);
        CONFIG_INSTANCE = Configuration.registerConfig(ETModConfig.class, ConfigFormats.YAML).getConfigInstance();
    }

    private void gatherData(GatherDataEvent event) {

        //ETMaterialSpriteProvider spriteProvider = new ETMaterialSpriteProvider();
        //event.getGenerator().addProvider(event.includeClient(), new ETMaterialRenderInfoProvider(event.getGenerator().getPackOutput(), spriteProvider, event.getExistingFileHelper()));
        event.getGenerator().addProvider(event.includeClient(), new ETMaterialLangProvider(event.getGenerator().getPackOutput()));
    }

    public static ResourceLocation id(String location) {
        return new ResourceLocation(ExpandedTiC.MOD_ID, location);
    }

    public static MaterialId materialId(String location) {
        return new MaterialId(ExpandedTiC.MOD_ID, location);
    }

    private void setupGTMaterials(FMLCommonSetupEvent event) {
        if(ModList.get().isLoaded("gtceu") && ExpandedTiC.CONFIG_INSTANCE.gtMaterialGeneration.enableGTIntegration) {
            event.enqueueWork(() -> {
                GTMaterialHelper.REGISTERED_TOOL_MATERIALS = GTMaterialHelper.getRegisteredMaterials();
                ETDynamicResourcePack.generateAllAssets();
            });
        }
    }
}
