package net.literally.chunk.loader.network.packets.packet;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.literally.chunk.loader.data.SerializableChunkPos;
import net.literally.chunk.loader.initializer.LCLPersistentChunks;
import net.literally.chunk.loader.loaders.LCLLoader;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class ForcedChunksC2SPacket extends CustomC2SPacket
{
    public static Identifier PACKET_ID = new Identifier(LCLLoader.MOD_ID, "fc_packet");
    
    private ArrayList<SerializableChunkPos> chunksPos;
    private int x;
    private int z;
    private boolean state;
    
    public ForcedChunksC2SPacket(int x, int z, boolean state, SerializableChunkPos... chunks)
    {
        chunksPos = new ArrayList<>();
        this.x = x;
        this.z = z;
        this.state = state;
        for(int i = 0; i < chunks.length; i++)
        {
            chunksPos.add(chunks[i]);
        }
    }
    
    
    public ForcedChunksC2SPacket(ArrayList<SerializableChunkPos> chunksPos, int x, int z, boolean state)
    {
        this.chunksPos = chunksPos;
        this.x = x;
        this.z = z;
        this.state = state;
    }
    
    @Override public void send()
    {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        write(buf);
        ClientPlayNetworking.send(PACKET_ID, buf);
    }
    
    @Override
    public void onReceive(MinecraftServer server, ServerPlayerEntity servPlayer, ServerPlayNetworkHandler handler, PacketSender sender)
    {
        for(int i = 0; i < chunksPos.size(); i++)
        {
            LCLPersistentChunks.forceLoadChunk(server, chunksPos.get(i), state);
        }
        //TODO send packet with modified chunks to all
    }
    
    @Override public void write(PacketByteBuf buf)
    {
        buf.writeInt(x);
        buf.writeInt(z);
        buf.writeBoolean(state);
        int size = chunksPos.size();
        buf.writeInt(size);
        for(int i = 0; i < size; i++)
        {
            chunksPos.get(i).write(buf);
        }
    }
    
    public static ForcedChunksC2SPacket read(PacketByteBuf buf)
    {
        ArrayList<SerializableChunkPos> chunks = new ArrayList<>();
        int x = buf.readInt();
        int z = buf.readInt();
        boolean state = buf.readBoolean();
        int size = buf.readInt();
        for(int i = 0; i < size; i++)
        {
            chunks.add(SerializableChunkPos.read(buf));
        }
        return new ForcedChunksC2SPacket(chunks, x, z, state);
    }
}
