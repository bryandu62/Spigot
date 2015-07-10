package net.minecraft.server.v1_8_R3;

import java.util.Collection;

public abstract interface IBlockState<T extends Comparable<T>>
{
  public abstract String a();
  
  public abstract Collection<T> c();
  
  public abstract Class<T> b();
  
  public abstract String a(T paramT);
}
