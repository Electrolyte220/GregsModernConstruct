package dev.electrolyte.gm_construct.event;

import dev.electrolyte.gm_construct.GMConstruct;
import dev.electrolyte.gm_construct.data.GMCDynamicDataPack;
import dev.electrolyte.gm_construct.data.GMCDynamicResourcePack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.Pack.Position;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = GMConstruct.MOD_ID, bus = Bus.MOD)
public class GMCModEventHandler {

    @SubscribeEvent
    public static void addPacks(AddPackFindersEvent event) {
        if(event.getPackType() == PackType.SERVER_DATA) {
            event.addRepositorySource(c -> c.accept(Pack.readMetaAndCreate(
                    "gm_construct:dynamic_data",
                    Component.literal("gm_construct:dynamic_data"),
                    true,
                    GMCDynamicDataPack::new,
                    PackType.SERVER_DATA,
                    Position.BOTTOM,
                    PackSource.BUILT_IN)));
            }
        if(event.getPackType() == PackType.CLIENT_RESOURCES) {
            event.addRepositorySource(c -> c.accept(Pack.readMetaAndCreate(
                    "gm_construct:dynamic_assets",
                    Component.literal("gm_construct:dynamic_assets"),
                    true,
                    GMCDynamicResourcePack::new,
                    PackType.CLIENT_RESOURCES,
                    Position.BOTTOM,
                    PackSource.BUILT_IN)));
        }
    }
}
