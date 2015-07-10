package net.minecraft.server.v1_8_R3;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;

public abstract class BlockDataAbstract
  implements IBlockData
{
  private static final Joiner a = Joiner.on(',');
  private static final Function<Map.Entry<IBlockState, Comparable>, String> b = new Function()
  {
    public String a(Map.Entry<IBlockState, Comparable> ☃)
    {
      if (☃ == null) {
        return "<NULL>";
      }
      IBlockState ☃ = (IBlockState)☃.getKey();
      return ☃.a() + "=" + ☃.a((Comparable)☃.getValue());
    }
  };
  
  public <T extends Comparable<T>> IBlockData a(IBlockState<T> ☃)
  {
    return set(☃, (Comparable)a(☃.c(), get(☃)));
  }
  
  protected static <T> T a(Collection<T> ☃, T ☃)
  {
    Iterator<T> ☃ = ☃.iterator();
    while (☃.hasNext()) {
      if (☃.next().equals(☃))
      {
        if (☃.hasNext()) {
          return (T)☃.next();
        }
        return (T)☃.iterator().next();
      }
    }
    return (T)☃.next();
  }
  
  public String toString()
  {
    StringBuilder ☃ = new StringBuilder();
    ☃.append(Block.REGISTRY.c(getBlock()));
    if (!b().isEmpty())
    {
      ☃.append("[");
      a.appendTo(☃, Iterables.transform(b().entrySet(), b));
      ☃.append("]");
    }
    return ☃.toString();
  }
}
