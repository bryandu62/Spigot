package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.EntityMinecartAbstract;
import net.minecraft.server.v1_8_R3.IBlockData;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.bukkit.entity.Minecart;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

public abstract class CraftMinecart
  extends CraftVehicle
  implements Minecart
{
  public CraftMinecart(CraftServer server, EntityMinecartAbstract entity)
  {
    super(server, entity);
  }
  
  public void setDamage(double damage)
  {
    getHandle().setDamage((float)damage);
  }
  
  public double getDamage()
  {
    return getHandle().getDamage();
  }
  
  public double getMaxSpeed()
  {
    return getHandle().maxSpeed;
  }
  
  public void setMaxSpeed(double speed)
  {
    if (speed >= 0.0D) {
      getHandle().maxSpeed = speed;
    }
  }
  
  public boolean isSlowWhenEmpty()
  {
    return getHandle().slowWhenEmpty;
  }
  
  public void setSlowWhenEmpty(boolean slow)
  {
    getHandle().slowWhenEmpty = slow;
  }
  
  public Vector getFlyingVelocityMod()
  {
    return getHandle().getFlyingVelocityMod();
  }
  
  public void setFlyingVelocityMod(Vector flying)
  {
    getHandle().setFlyingVelocityMod(flying);
  }
  
  public Vector getDerailedVelocityMod()
  {
    return getHandle().getDerailedVelocityMod();
  }
  
  public void setDerailedVelocityMod(Vector derailed)
  {
    getHandle().setDerailedVelocityMod(derailed);
  }
  
  public EntityMinecartAbstract getHandle()
  {
    return (EntityMinecartAbstract)this.entity;
  }
  
  public void setDisplayBlock(MaterialData material)
  {
    if (material != null)
    {
      IBlockData block = CraftMagicNumbers.getBlock(material.getItemTypeId()).fromLegacyData(material.getData());
      getHandle().setDisplayBlock(block);
    }
    else
    {
      getHandle().setDisplayBlock(Blocks.AIR.getBlockData());
      getHandle().a(false);
    }
  }
  
  public MaterialData getDisplayBlock()
  {
    IBlockData blockData = getHandle().getDisplayBlock();
    return CraftMagicNumbers.getMaterial(blockData.getBlock()).getNewData((byte)blockData.getBlock().toLegacyData(blockData));
  }
  
  public void setDisplayBlockOffset(int offset)
  {
    getHandle().SetDisplayBlockOffset(offset);
  }
  
  public int getDisplayBlockOffset()
  {
    return getHandle().getDisplayBlockOffset();
  }
}
