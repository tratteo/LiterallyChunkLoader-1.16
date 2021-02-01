package net.literally.chunk.loader.initializer;

import net.literally.chunk.loader.data.LclData;
import net.literally.chunk.loader.data.SerializableChunkPos;
import net.literally.chunk.loader.saves.ChunksSerializeManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

import java.util.ArrayList;

public final class LCLPersistentChunks
{
    public static String CURRENT_LEVEL_NAME;
    
    private static LclData data;
    
    public static void initialize(MinecraftServer server)
    {
        CURRENT_LEVEL_NAME = server.getSaveProperties().getLevelName();
        initializeForcedChunks(server);
    }
    
    public static boolean loaderRemoved(SerializableChunkPos chunk)
    {
        data.removeLoaderPos(chunk);
        //TODO remove all 25 chunks from force loading
        return save();
    }
    
    public static boolean loaderAdded(SerializableChunkPos chunk)
    {
        data.addLoaderPos(chunk);
        return save();
    }
    
    public static boolean forceLoadChunk(MinecraftServer server, SerializableChunkPos chunk, boolean state)
    {
        data.chunkForceLoaded(chunk, state);
        setChunkForceLoaded(server, chunk, state);
        return true;
    }
    
    
    public static boolean canPlaceLoaderAt(SerializableChunkPos chunk)
    {
        return !data.isLoaderPresentAt(chunk);
    }
    
    
    private static void initializeForcedChunks(MinecraftServer server)
    {
        data = ChunksSerializeManager.deserialize(server.getSaveProperties().getLevelName());
        if(data == null)
        {
            data = new LclData();
            save();
        }
        else
        {
            System.out.println("Initializing: " + data.getChunks().size() + " persistent areas");
            ArrayList<SerializableChunkPos> chunks = data.getChunks();
            for(int i = 0; i < chunks.size(); i++)
            {
                SerializableChunkPos chunk = chunks.get(i);
                setChunkForceLoaded(server, chunk, true);
            }
        }
    }
    
    
    public static boolean setChunkForceLoaded(MinecraftServer server, SerializableChunkPos chunk, boolean state)
    {
        ServerWorld serverWorld = server.getWorld(chunk.getDimensionRegistryKey());
        if(chunk.getX() >= -30000000 && chunk.getZ() >= -30000000 && chunk.getX() < 30000000 && chunk.getZ() < 30000000)
        {
            return serverWorld.setChunkForced(chunk.getX(), chunk.getZ(), state);
        }
        return false;
    }
    
    public static boolean save()
    {
        return ChunksSerializeManager.serialize(data, CURRENT_LEVEL_NAME);
    }
}
