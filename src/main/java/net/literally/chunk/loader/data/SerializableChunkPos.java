package net.literally.chunk.loader.data;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.io.Serializable;

public class SerializableChunkPos implements Serializable
{
    private int x;
    private int z;
    private String dimension;
    
    public SerializableChunkPos(BlockPos origin, String dimension)
    {
        this((float) origin.getX(), (float) origin.getZ(), dimension);
    }
    
    public SerializableChunkPos(long pos, String dimension)
    {
        this((int) pos, (int) (pos >> 32), dimension);
    }
    
    public SerializableChunkPos(float blockX, float blockY, String dimension)
    {
        this((int) blockX >> 4, (int) blockY >> 4, dimension);
    }
    
    public SerializableChunkPos(int x, int z, String dimension)
    {
        this.x = x;
        this.z = z;
        this.dimension = dimension;
    }
    
    public static SerializableChunkPos read(PacketByteBuf buf)
    {
        int x = buf.readInt();
        int y = buf.readInt();
        String dimension = buf.readString();
        return new SerializableChunkPos(x, y, dimension);
    }
    
    @Override public boolean equals(Object obj)
    {
        if(obj instanceof SerializableChunkPos)
        {
            SerializableChunkPos other = (SerializableChunkPos) obj;
            return this.getDimension().equals(other.getDimension()) && x == other.getX() && z == other.getZ();
        }
        return super.equals(obj);
    }
    
    public void write(PacketByteBuf buf)
    {
        buf.writeInt(getX());
        buf.writeInt(getZ());
        buf.writeString(dimension);
    }
    
    public int getX()
    {
        return x;
    }
    
    public int getZ()
    {
        return z;
    }
    
    public String getDimension()
    {
        return dimension;
    }
    
    public RegistryKey<World> getDimensionRegistryKey()
    {
        String overworldID = World.OVERWORLD.getValue().getPath();
        String netherID = World.NETHER.getValue().getPath();
        String endID = World.END.getValue().getPath();
        if(dimension.equals(overworldID))
        {
            return World.OVERWORLD;
        }
        else if(dimension.equals(netherID))
        {
            return World.NETHER;
        }
        else if(dimension.equals(endID))
        {
            return World.END;
        }
        return null;
    }
}
