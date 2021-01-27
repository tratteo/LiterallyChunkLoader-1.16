package net.literally.chunk.loader.implementations;

import net.literally.chunk.loader.data.AreaData;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public final class AreaImplementation
{
    public static final String OVERWORLD_ID = "overworld";
    public static final String NETHER_ID = "nether";
    public static final String END_ID = "end";
    
    public static boolean areAreasEqual(AreaData first, AreaData second)
    {
        if(!first.getDimensionID().equals(second.getDimensionID())) return false;
        return first.getFromChunkX() == second.getFromChunkX() && first.getFromChunkZ() == second.getFromChunkZ() && first.getToChunkX() == second.getToChunkX() && first.getToChunkZ() == second.getToChunkZ();
    }
    
    public static boolean areAreasOverlapping(AreaData first, AreaData second)
    {
        if(!first.getDimensionID().equals(second.getDimensionID())) return false;
        
        if(first.getToChunkX() < second.getFromChunkX() || second.getToChunkX() < first.getFromChunkX())
        {
            return false;
        }
        if(first.getToChunkZ() < second.getFromChunkZ() || second.getToChunkZ() < first.getFromChunkZ())
        {
            return false;
        }
        return true;
    }
    
    public static RegistryKey<World> getDimensionRegistryKey(String id)
    {
        String overworldID = World.OVERWORLD.getValue().getPath();
        String netherID = World.NETHER.getValue().getPath();
        String endID = World.END.getValue().getPath();
        if(id.equals(overworldID))
        {
            return World.OVERWORLD;
        }
        else if(id.equals(netherID))
        {
            return World.NETHER;
        }
        else if(id.equals(endID))
        {
            return World.END;
        }
        return null;
    }
    //
    //public static String getIDFromRegistryKey(RegistryKey<World> type)
    //{
    //    if(type.equals(DimensionType.))
    //    {
    //        return OVERWORLD_ID;
    //    }
    //    else if(type.equals(DimensionType.THE_NETHER_REGISTRY_KEY))
    //    {
    //        return NETHER_ID;
    //    }
    //    else if(type.equals(DimensionType.THE_END_REGISTRY_KEY))
    //    {
    //        return END_ID;
    //    }
    //    else
    //    {
    //        return "";
    //    }
    //}
}
