package org.bukkit.entity;

public abstract interface Ocelot
  extends Animals, Tameable
{
  public abstract Type getCatType();
  
  public abstract void setCatType(Type paramType);
  
  public abstract boolean isSitting();
  
  public abstract void setSitting(boolean paramBoolean);
  
  public static enum Type
  {
    WILD_OCELOT(0),  BLACK_CAT(1),  RED_CAT(2),  SIAMESE_CAT(3);
    
    private static final Type[] types;
    private final int id;
    
    static
    {
      types = new Type[values().length];
      Type[] arrayOfType;
      int i = (arrayOfType = values()).length;
      for (int j = 0; j < i; j++)
      {
        Type type = arrayOfType[j];
        types[type.getId()] = type;
      }
    }
    
    private Type(int id)
    {
      this.id = id;
    }
    
    @Deprecated
    public int getId()
    {
      return this.id;
    }
    
    @Deprecated
    public static Type getType(int id)
    {
      return id >= types.length ? null : types[id];
    }
  }
}
