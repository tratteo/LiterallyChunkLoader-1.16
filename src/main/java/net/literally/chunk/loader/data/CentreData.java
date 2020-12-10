package net.literally.chunk.loader.data;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.io.Serializable;

public class CentreData implements Serializable
{
    private float x;
    private float z;
    private ChunkPos chunkPos;
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
        chunkPos = new ChunkPos(blockPos);
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
}
