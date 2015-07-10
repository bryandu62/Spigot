package org.bukkit.map;

import java.util.List;
import org.bukkit.World;

public abstract interface MapView
{
  @Deprecated
  public abstract short getId();
  
  public abstract boolean isVirtual();
  
  public abstract Scale getScale();
  
  public abstract void setScale(Scale paramScale);
  
  public abstract int getCenterX();
  
  public abstract int getCenterZ();
  
  public abstract void setCenterX(int paramInt);
  
  public abstract void setCenterZ(int paramInt);
  
  public abstract World getWorld();
  
  public abstract void setWorld(World paramWorld);
  
  public abstract List<MapRenderer> getRenderers();
  
  public abstract void addRenderer(MapRenderer paramMapRenderer);
  
  public abstract boolean removeRenderer(MapRenderer paramMapRenderer);
  
  public static enum Scale
  {
    CLOSEST(0),  CLOSE(1),  NORMAL(2),  FAR(3),  FARTHEST(4);
    
    private byte value;
    
    private Scale(int value)
    {
      this.value = ((byte)value);
    }
    
    @Deprecated
    public static Scale valueOf(byte value)
    {
      switch (value)
      {
      case 0: 
        return CLOSEST;
      case 1: 
        return CLOSE;
      case 2: 
        return NORMAL;
      case 3: 
        return FAR;
      case 4: 
        return FARTHEST;
      }
      return null;
    }
    
    @Deprecated
    public byte getValue()
    {
      return this.value;
    }
  }
}
