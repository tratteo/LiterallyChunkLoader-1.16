package net.literally.chunk.loader.network.packets.packet;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.literally.chunk.loader.GUI.handler.ChunkLoaderGUIHandler;
import net.literally.chunk.loader.data.SerializableChunkPos;
import net.literally.chunk.loader.loaders.LCLLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class ForcedChunksS2CPacket extends CustomS2CPacket
{
    public static Identifier PACKET_ID = new Identifier(LCLLoader.MOD_ID, "fc_packet");
    
    private ArrayList<SerializableChunkPos> chunksPos;
    private int x;
    private int z;
    
    public ForcedChunksS2CPacket(ArrayList<SerializableChunkPos> chunksPos, int x, int z)
    {
        this.chunksPos = chunksPos;
        this.x = x;
        this.z = z;
    }
    
    @Override public void sendTo(PlayerEntity player)
    {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        write(buf);
        ServerPlayNetworking.send((ServerPlayerEntity) player, PACKET_ID, buf);
    }
    
    @Override public void onReceive(MinecraftClient ctx)
    {
        ctx.execute(() ->
        {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            ScreenHandler handler = player.currentScreenHandler;
            if(handler != null)
            {
                if(handler instanceof ChunkLoaderGUIHandler)
                {
                    ChunkLoaderGUIHandler clHandler = (ChunkLoaderGUIHandler) handler;
                    clHandler.refreshGUI(chunksPos, x, z);
                }
            }
        });
    }
    
    @Override public void write(PacketByteBuf buf)
    {
        buf.writeInt(x);
        buf.writeInt(z);
        int size = chunksPos.size();
        buf.writeInt(size);
        for(int i = 0; i < size; i++)
        {
            chunksPos.get(i).write(buf);
        }
    }
    
    public static ForcedChunksS2CPacket read(PacketByteBuf buf)
    {
        ArrayList<SerializableChunkPos> chunks = new ArrayList<>();
        int x = buf.readInt();
        int z = buf.readInt();
        int size = buf.readInt();
        for(int i = 0;i < size; i++)
        {
            chunks.add(SerializableChunkPos.read(buf));
        }
        return new ForcedChunksS2CPacket(chunks, x, z);
    }
}
