package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityOcelot;
import org.apache.commons.lang.Validate;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Ocelot.Type;

public class CraftOcelot
  extends CraftTameableAnimal
  implements Ocelot
{
  public CraftOcelot(CraftServer server, EntityOcelot wolf)
  {
    super(server, wolf);
  }
  
  public EntityOcelot getHandle()
  {
    return (EntityOcelot)this.entity;
  }
  
  public Ocelot.Type getCatType()
  {
    return Ocelot.Type.getType(getHandle().getCatType());
  }
  
  public void setCatType(Ocelot.Type type)
  {
    Validate.notNull(type, "Cat type cannot be null");
    getHandle().setCatType(type.getId());
  }
  
  public EntityType getType()
  {
    return EntityType.OCELOT;
  }
}
