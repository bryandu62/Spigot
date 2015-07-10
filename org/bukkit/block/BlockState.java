package org.bukkit.block;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.Metadatable;

public abstract interface BlockState
  extends Metadatable
{
  public abstract Block getBlock();
  
  public abstract MaterialData getData();
  
  public abstract Material getType();
  
  @Deprecated
  public abstract int getTypeId();
  
  public abstract byte getLightLevel();
  
  public abstract World getWorld();
  
  public abstract int getX();
  
  public abstract int getY();
  
  public abstract int getZ();
  
  public abstract Location getLocation();
  
  public abstract Location getLocation(Location paramLocation);
  
  public abstract Chunk getChunk();
  
  public abstract void setData(MaterialData paramMaterialData);
  
  public abstract void setType(Material paramMaterial);
  
  @Deprecated
  public abstract boolean setTypeId(int paramInt);
  
  public abstract boolean update();
  
  public abstract boolean update(boolean paramBoolean);
  
  public abstract boolean update(boolean paramBoolean1, boolean paramBoolean2);
  
  @Deprecated
  public abstract byte getRawData();
  
  @Deprecated
  public abstract void setRawData(byte paramByte);
  
  public abstract boolean isPlaced();
}
