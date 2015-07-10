package net.minecraft.server.v1_8_R3;

import com.google.common.collect.ImmutableMap;
import java.util.Collection;

public abstract interface IBlockData
{
  public abstract Collection<IBlockState> a();
  
  public abstract <T extends Comparable<T>> T get(IBlockState<T> paramIBlockState);
  
  public abstract <T extends Comparable<T>, V extends T> IBlockData set(IBlockState<T> paramIBlockState, V paramV);
  
  public abstract <T extends Comparable<T>> IBlockData a(IBlockState<T> paramIBlockState);
  
  public abstract ImmutableMap<IBlockState, Comparable> b();
  
  public abstract Block getBlock();
}
