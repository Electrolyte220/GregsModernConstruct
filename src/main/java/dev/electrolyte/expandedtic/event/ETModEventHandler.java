package dev.electrolyte.expandedtic.event;

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

@Mod.EventBusSubscriber(modid = ExpandedTiC.MOD_ID, bus = Bus.MOD)
public class ETModEventHandler {

    @SubscribeEvent
    public static void addPacks(AddPackFindersEvent event) {
        if(event.getPackType() == PackType.SERVER_DATA) {
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
