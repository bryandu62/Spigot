package net.minecraft.server.v1_8_R3;

import com.google.common.base.Predicate;

public abstract interface IMonster
  extends IAnimal
{
  public static final Predicate<Entity> d = new Predicate()
  {
    public boolean a(Entity ☃)
    {
      return ☃ instanceof IMonster;
    }
  };
  public static final Predicate<Entity> e = new Predicate()
  {
    public boolean a(Entity ☃)
    {
      return ((☃ instanceof IMonster)) && (!☃.isInvisible());
    }
  };
}
