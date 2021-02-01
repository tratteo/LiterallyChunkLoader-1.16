package net.literally.chunk.loader.data;

import javafx.util.Pair;
import net.minecraft.network.PacketByteBuf;

import java.io.Serializable;
import java.util.ArrayList;

public class AreaData implements Serializable
{
    public static final int SIZE = 5;
    private CentreData centre;
    private ArrayList<Pair<Integer, Integer>> bitmap;
    private String dimensionID;
    
    public AreaData(CentreData centre, String dimensionID)
    {
        this.centre = centre;
        this.dimensionID = dimensionID;
        bitmap = new ArrayList<>();
    }
    
    public void setBitmap(ArrayList<Pair<Integer, Integer>> loaded)
    {
        this.bitmap = loaded;
    }
    public ArrayList<Pair<Integer, Integer>> getBitmap()
    {
        return bitmap;
    }
    
    public void addBit(int i, int j)
    {
        Pair pair = new Pair(i,j);
        if(!bitmap.contains(pair))
        {
            bitmap.add(pair);
        }
    }
    public String getDimensionID() {return dimensionID;}
    
    public CentreData getCentreData()
    {
        return this.centre;
    }
    
    public static AreaData read(PacketByteBuf buf)
    {
        CentreData centreData = CentreData.read(buf);
        String dimension = buf.readString();
        AreaData area = new AreaData(centreData, dimension);
        int size = buf.readInt();
        for(int i = 0; i < size; i++)
        {
            area.addBit(buf.readInt(), buf.readInt());
        }
        return area;
    }
    
    
    @Override public boolean equals(Object obj)
    {
        if(obj instanceof AreaData)
        {
            AreaData other = (AreaData) obj;
            return other.getCentreData().equals(getCentreData()) && other.getDimensionID() == getDimensionID();
        }
        return super.equals(obj);
    }
    
    public void write(PacketByteBuf buf)
    {
        centre.write(buf);
        buf.writeString(dimensionID);
        buf.writeInt(bitmap.size());
        for(int i = 0; i < bitmap.size(); i++)
        {
            buf.writeInt(bitmap.get(i).getKey());
            buf.writeInt(bitmap.get(i).getValue());
        }
    }
    
    
    @Override public String toString()
    {
        return "Centre: "+ centre.toString()+", dimension: "+dimensionID;
    }
}
