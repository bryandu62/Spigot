package org.bukkit.craftbukkit.v1_8_R3;

import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Location;
import org.bukkit.World;

public class CraftWorldBorder
  implements org.bukkit.WorldBorder
{
  private final World world;
  private final net.minecraft.server.v1_8_R3.WorldBorder handle;
  
  public CraftWorldBorder(CraftWorld world)
  {
    this.world = world;
    this.handle = world.getHandle().getWorldBorder();
  }
  
  public void reset()
  {
    setSize(6.0E7D);
    setDamageAmount(0.2D);
    setDamageBuffer(5.0D);
    setWarningDistance(5);
    setWarningTime(15);
    setCenter(0.0D, 0.0D);
  }
  
  public double getSize()
  {
    return this.handle.getSize();
  }
  
  public void setSize(double newSize)
  {
    setSize(newSize, 0L);
  }
  
  public void setSize(double newSize, long time)
  {
    newSize = Math.min(6.0E7D, Math.max(1.0D, newSize));
    time = Math.min(9223372036854775L, Math.max(0L, time));
    if (time > 0L) {
      this.handle.transitionSizeBetween(this.handle.getSize(), newSize, time * 1000L);
    } else {
      this.handle.setSize(newSize);
    }
  }
  
  public Location getCenter()
  {
    double x = this.handle.getCenterX();
    double z = this.handle.getCenterZ();
    
    return new Location(this.world, x, 0.0D, z);
  }
  
  public void setCenter(double x, double z)
  {
    x = Math.min(3.0E7D, Math.max(-3.0E7D, x));
    z = Math.min(3.0E7D, Math.max(-3.0E7D, z));
    
    this.handle.setCenter(x, z);
  }
  
  public void setCenter(Location location)
  {
    setCenter(location.getX(), location.getZ());
  }
  
  public double getDamageBuffer()
  {
    return this.handle.getDamageBuffer();
  }
  
  public void setDamageBuffer(double blocks)
  {
    this.handle.setDamageBuffer(blocks);
  }
  
  public double getDamageAmount()
  {
    return this.handle.getDamageAmount();
  }
  
  public void setDamageAmount(double damage)
  {
    this.handle.setDamageAmount(damage);
  }
  
  public int getWarningTime()
  {
    return this.handle.getWarningTime();
  }
  
  public void setWarningTime(int time)
  {
    this.handle.setWarningTime(time);
  }
  
  public int getWarningDistance()
  {
    return this.handle.getWarningDistance();
  }
  
  public void setWarningDistance(int distance)
  {
    this.handle.setWarningDistance(distance);
  }
}
