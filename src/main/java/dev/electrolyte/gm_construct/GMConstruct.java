package dev.electrolyte.gm_construct;

import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;
import com.mojang.logging.LogUtils;
import dev.electrolyte.gm_construct.config.GMCConfig;
import dev.electrolyte.gm_construct.data.GMCDynamicResourcePack;
import dev.electrolyte.gm_construct.helper.GTMaterialHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import slimeknights.tconstruct.library.materials.definition.MaterialId;

@Mod(GMConstruct.MOD_ID)
public class GMConstruct {

    public static final String MOD_ID = "gm_construct";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final GTRegistrate REGISTRATE = GTRegistrate.create(MOD_ID);

    public GMConstruct() {
        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(Type.COMMON, GMCConfig.COMMON_CONFIG);
        REGISTRATE.registerRegistrate();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupMaterials);
    }

    public static ResourceLocation id(String location) {
        return new ResourceLocation(GMConstruct.MOD_ID, location);
    }

    public static MaterialId materialId(String location) {
        return new MaterialId(GMConstruct.MOD_ID, location);
    }

    private void setupMaterials(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            GTMaterialHelper.REGISTERED_TOOL_MATERIALS = GTMaterialHelper.getRegisteredMaterials();
            GMCDynamicResourcePack.generateAllAssets();
        });
    }
}
