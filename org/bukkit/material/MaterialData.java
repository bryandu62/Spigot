package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MaterialData
  implements Cloneable
{
  private final int type;
  private byte data = 0;
  
  @Deprecated
  public MaterialData(int type)
  {
    this(type, (byte)0);
  }
  
  public MaterialData(Material type)
  {
    this(type, (byte)0);
  }
  
  @Deprecated
  public MaterialData(int type, byte data)
  {
    this.type = type;
    this.data = data;
  }
  
  @Deprecated
  public MaterialData(Material type, byte data)
  {
    this(type.getId(), data);
  }
  
  @Deprecated
  public byte getData()
  {
    return this.data;
  }
  
  @Deprecated
  public void setData(byte data)
  {
    this.data = data;
  }
  
  public Material getItemType()
  {
    return Material.getMaterial(this.type);
  }
  
  @Deprecated
  public int getItemTypeId()
  {
    return this.type;
  }
  
  public ItemStack toItemStack()
  {
    return new ItemStack(this.type, 0, this.data);
  }
  
  public ItemStack toItemStack(int amount)
  {
    return new ItemStack(this.type, amount, this.data);
  }
  
  public String toString()
  {
    return getItemType() + "(" + getData() + ")";
  }
  
  public int hashCode()
  {
    return getItemTypeId() << 8 ^ getData();
  }
  
  public boolean equals(Object obj)
  {
    if ((obj != null) && ((obj instanceof MaterialData)))
    {
      MaterialData md = (MaterialData)obj;
      
      return (md.getItemTypeId() == getItemTypeId()) && (md.getData() == getData());
    }
    return false;
  }
  
  public MaterialData clone()
  {
    try
    {
      return (MaterialData)super.clone();
    }
    catch (CloneNotSupportedException e)
    {
      throw new Error(e);
    }
  }
}
