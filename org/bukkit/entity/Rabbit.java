package org.bukkit.entity;

public abstract interface Rabbit
  extends Animals
{
  public abstract Type getRabbitType();
  
  public abstract void setRabbitType(Type paramType);
  
  public static enum Type
  {
    BROWN,  WHITE,  BLACK,  BLACK_AND_WHITE,  GOLD,  SALT_AND_PEPPER,  THE_KILLER_BUNNY;
  }
}
