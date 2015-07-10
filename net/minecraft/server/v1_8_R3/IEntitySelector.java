package net.minecraft.server.v1_8_R3;

import com.google.common.base.Predicate;

public final class IEntitySelector
{
  public static final Predicate<Entity> a = new Predicate()
  {
    public boolean a(Entity ☃)
    {
      return ☃.isAlive();
    }
  };
  public static final Predicate<Entity> b = new Predicate()
  {
    public boolean a(Entity ☃)
    {
      return (☃.isAlive()) && (☃.passenger == null) && (☃.vehicle == null);
    }
  };
  public static final Predicate<Entity> c = new Predicate()
  {
    public boolean a(Entity ☃)
    {
      return ((☃ instanceof IInventory)) && (☃.isAlive());
    }
  };
  public static final Predicate<Entity> d = new Predicate()
  {
    public boolean a(Entity ☃)
    {
      return (!(☃ instanceof EntityHuman)) || (!((EntityHuman)☃).isSpectator());
    }
  };
  
  public static class EntitySelectorEquipable
    implements Predicate<Entity>
  {
    private final ItemStack a;
    
    public EntitySelectorEquipable(ItemStack ☃)
    {
      this.a = ☃;
    }
    
    public boolean a(Entity ☃)
    {
      if (!☃.isAlive()) {
        return false;
      }
      if (!(☃ instanceof EntityLiving)) {
        return false;
      }
      EntityLiving ☃ = (EntityLiving)☃;
      if (☃.getEquipment(EntityInsentient.c(this.a)) != null) {
        return false;
      }
      if ((☃ instanceof EntityInsentient)) {
        return ((EntityInsentient)☃).bY();
      }
      if ((☃ instanceof EntityArmorStand)) {
        return true;
      }
      if ((☃ instanceof EntityHuman)) {
        return true;
      }
      return false;
    }
  }
}
