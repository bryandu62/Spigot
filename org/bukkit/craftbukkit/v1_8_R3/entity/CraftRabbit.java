package org.bukkit.craftbukkit.v1_8_R3.entity;

import net.minecraft.server.v1_8_R3.EntityRabbit;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Rabbit.Type;

public class CraftRabbit
  extends CraftAnimals
  implements Rabbit
{
  public CraftRabbit(CraftServer server, EntityRabbit entity)
  {
    super(server, entity);
  }
  
  public EntityRabbit getHandle()
  {
    return (EntityRabbit)this.entity;
  }
  
  public String toString()
  {
    return "CraftRabbit{RabbitType=" + getRabbitType() + "}";
  }
  
  public EntityType getType()
  {
    return EntityType.RABBIT;
  }
  
  public Rabbit.Type getRabbitType()
  {
    int type = getHandle().getRabbitType();
    return CraftMagicMapping.fromMagic(type);
  }
  
  public void setRabbitType(Rabbit.Type type)
  {
    EntityRabbit entity = getHandle();
    if (getRabbitType() == Rabbit.Type.THE_KILLER_BUNNY)
    {
      World world = ((CraftWorld)getWorld()).getHandle();
      entity.goalSelector = new PathfinderGoalSelector((world != null) && (world.methodProfiler != null) ? world.methodProfiler : null);
      entity.targetSelector = new PathfinderGoalSelector((world != null) && (world.methodProfiler != null) ? world.methodProfiler : null);
      entity.initializePathFinderGoals();
    }
    entity.setRabbitType(CraftMagicMapping.toMagic(type));
  }
  
  private static class CraftMagicMapping
  {
    private static final int[] types = new int[Rabbit.Type.values().length];
    private static final Rabbit.Type[] reverse = new Rabbit.Type[Rabbit.Type.values().length];
    
    static
    {
      set(Rabbit.Type.BROWN, 0);
      set(Rabbit.Type.WHITE, 1);
      set(Rabbit.Type.BLACK, 2);
      set(Rabbit.Type.BLACK_AND_WHITE, 3);
      set(Rabbit.Type.GOLD, 4);
      set(Rabbit.Type.SALT_AND_PEPPER, 5);
      set(Rabbit.Type.THE_KILLER_BUNNY, 99);
    }
    
    private static void set(Rabbit.Type type, int value)
    {
      types[type.ordinal()] = value;
      if (value < reverse.length) {
        reverse[value] = type;
      }
    }
    
    public static Rabbit.Type fromMagic(int magic)
    {
      if ((magic >= 0) && (magic < reverse.length)) {
        return reverse[magic];
      }
      if (magic == 99) {
        return Rabbit.Type.THE_KILLER_BUNNY;
      }
      return null;
    }
    
    public static int toMagic(Rabbit.Type type)
    {
      return types[type.ordinal()];
    }
  }
}
