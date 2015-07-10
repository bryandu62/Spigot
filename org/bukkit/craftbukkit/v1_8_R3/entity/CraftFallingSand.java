package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.EntityFallingBlock;
import net.minecraft.server.v1_8_R3.IBlockData;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingSand;

public class CraftFallingSand
  extends CraftEntity
  implements FallingSand
{
  public CraftFallingSand(CraftServer server, EntityFallingBlock entity)
  {
    super(server, entity);
  }
  
  public EntityFallingBlock getHandle()
  {
    return (EntityFallingBlock)this.entity;
  }
  
  public String toString()
  {
    return "CraftFallingSand";
  }
  
  public EntityType getType()
  {
    return EntityType.FALLING_BLOCK;
  }
  
  public Material getMaterial()
  {
    return Material.getMaterial(getBlockId());
  }
  
  public int getBlockId()
  {
    return CraftMagicNumbers.getId(getHandle().getBlock().getBlock());
  }
  
  public byte getBlockData()
  {
    return (byte)getHandle().getBlock().getBlock().toLegacyData(getHandle().getBlock());
  }
  
  public boolean getDropItem()
  {
    return getHandle().dropItem;
  }
  
  public void setDropItem(boolean drop)
  {
    getHandle().dropItem = drop;
  }
}
