package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.EntityEnderman;
import net.minecraft.server.v1_8_R3.IBlockData;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.EntityType;
import org.bukkit.material.MaterialData;

public class CraftEnderman
  extends CraftMonster
  implements Enderman
{
  public CraftEnderman(CraftServer server, EntityEnderman entity)
  {
    super(server, entity);
  }
  
  public MaterialData getCarriedMaterial()
  {
    IBlockData blockData = getHandle().getCarried();
    return CraftMagicNumbers.getMaterial(blockData.getBlock()).getNewData((byte)blockData.getBlock().toLegacyData(blockData));
  }
  
  public void setCarriedMaterial(MaterialData data)
  {
    getHandle().setCarried(CraftMagicNumbers.getBlock(data.getItemTypeId()).fromLegacyData(data.getData()));
  }
  
  public EntityEnderman getHandle()
  {
    return (EntityEnderman)this.entity;
  }
  
  public String toString()
  {
    return "CraftEnderman";
  }
  
  public EntityType getType()
  {
    return EntityType.ENDERMAN;
  }
}
