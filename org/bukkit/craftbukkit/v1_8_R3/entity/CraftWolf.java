package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityWolf;
import net.minecraft.server.v1_8_R3.EnumColor;
import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Wolf;

public class CraftWolf
  extends CraftTameableAnimal
  implements Wolf
{
  public CraftWolf(CraftServer server, EntityWolf wolf)
  {
    super(server, wolf);
  }
  
  public boolean isAngry()
  {
    return getHandle().isAngry();
  }
  
  public void setAngry(boolean angry)
  {
    getHandle().setAngry(angry);
  }
  
  public EntityWolf getHandle()
  {
    return (EntityWolf)this.entity;
  }
  
  public EntityType getType()
  {
    return EntityType.WOLF;
  }
  
  public DyeColor getCollarColor()
  {
    return DyeColor.getByWoolData((byte)getHandle().getCollarColor().getColorIndex());
  }
  
  public void setCollarColor(DyeColor color)
  {
    getHandle().setCollarColor(EnumColor.fromColorIndex(color.getWoolData()));
  }
}
