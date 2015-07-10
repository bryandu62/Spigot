package org.bukkit.entity;

public abstract interface Skeleton
  extends Monster
{
  public abstract SkeletonType getSkeletonType();
  
  public abstract void setSkeletonType(SkeletonType paramSkeletonType);
  
  public static enum SkeletonType
  {
    NORMAL(0),  WITHER(1);
    
    private static final SkeletonType[] types;
    private final int id;
    
    static
    {
      types = new SkeletonType[values().length];
      SkeletonType[] arrayOfSkeletonType;
      int i = (arrayOfSkeletonType = values()).length;
      for (int j = 0; j < i; j++)
      {
        SkeletonType type = arrayOfSkeletonType[j];
        types[type.getId()] = type;
      }
    }
    
    private SkeletonType(int id)
    {
      this.id = id;
    }
    
    @Deprecated
    public int getId()
    {
      return this.id;
    }
    
    @Deprecated
    public static SkeletonType getType(int id)
    {
      return id >= types.length ? null : types[id];
    }
  }
}
