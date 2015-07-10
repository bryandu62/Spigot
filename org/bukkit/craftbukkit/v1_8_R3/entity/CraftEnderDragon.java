package org.bukkit.craftbukkit.v1_8_R3.entity;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import java.util.Set;
import net.minecraft.server.v1_8_R3.EntityComplexPart;
import net.minecraft.server.v1_8_R3.EntityEnderDragon;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.ComplexEntityPart;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;

public class CraftEnderDragon
  extends CraftComplexLivingEntity
  implements EnderDragon
{
  public CraftEnderDragon(CraftServer server, EntityEnderDragon entity)
  {
    super(server, entity);
  }
  
  public Set<ComplexEntityPart> getParts()
  {
    ImmutableSet.Builder<ComplexEntityPart> builder = ImmutableSet.builder();
    EntityComplexPart[] arrayOfEntityComplexPart;
    int i = (arrayOfEntityComplexPart = getHandle().children).length;
    for (int j = 0; j < i; j++)
    {
      EntityComplexPart part = arrayOfEntityComplexPart[j];
      builder.add((ComplexEntityPart)part.getBukkitEntity());
    }
    return builder.build();
  }
  
  public EntityEnderDragon getHandle()
  {
    return (EntityEnderDragon)this.entity;
  }
  
  public String toString()
  {
    return "CraftEnderDragon";
  }
  
  public EntityType getType()
  {
    return EntityType.ENDER_DRAGON;
  }
}
