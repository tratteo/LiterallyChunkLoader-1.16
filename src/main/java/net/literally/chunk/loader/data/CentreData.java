package net.literally.chunk.loader.data;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

import java.io.Serializable;

public class CentreData implements Serializable
{
    private float x;
    private float z;
    private int chunkX;
    private int chunkZ;
    
    public float getX() {return this.x;}
    
    public float getZ() {return this.z;}
    
    public int getChunkX() {return this.chunkX;}
    
    public int getChunkZ() {return this.chunkZ;}
    
    public CentreData(BlockPos blockPos)
    {
        this.x = blockPos.getX();
        this.z = blockPos.getZ();
        chunkX = ((int) x >> 4);
        chunkZ = ((int) z >> 4);
    }
    
    public CentreData(float x, float z)
    {
        this.x = x;
        this.z = z;
        chunkX = ((int) x >> 4);
        chunkZ = ((int) z >> 4);
    }
    
    @Override public String toString()
    {
        return "coord: ( " + x + ", " + z + " )" + ", chunk: ( " + chunkX + ", " + chunkZ + " )";
    }
    
    public static CentreData read(PacketByteBuf buf)
    {
        float x = buf.readFloat();
        float y = buf.readFloat();
        return new CentreData(x, y);
    }
    
    @Override public boolean equals(Object obj)
    {
        if(obj instanceof CentreData)
        {
            CentreData other = (CentreData) obj;
            return chunkX == other.getChunkX() && chunkZ == other.getChunkZ();
        }
        return super.equals(obj);
    }
    
    public void write(PacketByteBuf buf)
    {
        buf.writeFloat(getX());
        buf.writeFloat(getZ());
    }
}
