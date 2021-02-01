package net.literally.chunk.loader.loaders;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.literally.chunk.loader.GUI.handler.ChunkLoaderGUIHandler;
import net.literally.chunk.loader.GUI.screen.ChunkLoaderScreen;
import net.literally.chunk.loader.initializer.LCLBlocks;
import net.literally.chunk.loader.initializer.LCLGUIHandlers;
import net.literally.chunk.loader.network.packets.packet.ForcedChunksS2CPacket;
import net.minecraft.client.render.RenderLayer;

public class LCLClientLoader implements ClientModInitializer
{
    @Override public void onInitializeClient()
    {
        ScreenRegistry.<ChunkLoaderGUIHandler, ChunkLoaderScreen>register(LCLGUIHandlers.CHUNK_LOADER_SCREEN_HANDLER, (gui, inventory, title) -> new ChunkLoaderScreen(gui, inventory.player, title));
        ClientPlayNetworking.registerGlobalReceiver(ForcedChunksS2CPacket.PACKET_ID, (client, handler, buf, responseSender) -> ForcedChunksS2CPacket.read(buf).onReceive(client));
        BlockRenderLayerMap.INSTANCE.putBlock(LCLBlocks.CHUNK_LOADER_BLOCK, RenderLayer.getTranslucent());
    }
}
