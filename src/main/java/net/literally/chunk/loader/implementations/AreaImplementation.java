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
        return first.getCentreData().equals(second.getCentreData());
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
}
