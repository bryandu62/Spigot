package net.minecraft.server.v1_8_R3;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

public class BlockStatePredicate
  implements Predicate<IBlockData>
{
  private final BlockStateList a;
  private final Map<IBlockState, Predicate> b = Maps.newHashMap();
  
  private BlockStatePredicate(BlockStateList ☃)
  {
    this.a = ☃;
  }
  
  public static BlockStatePredicate a(Block ☃)
  {
    return new BlockStatePredicate(☃.P());
  }
  
  public boolean a(IBlockData ☃)
  {
    if ((☃ == null) || (!☃.getBlock().equals(this.a.getBlock()))) {
      return false;
    }
    for (Map.Entry<IBlockState, Predicate> ☃ : this.b.entrySet())
    {
      Object ☃ = ☃.get((IBlockState)☃.getKey());
      if (!((Predicate)☃.getValue()).apply(☃)) {
        return false;
      }
    }
    return true;
  }
  
  public <V extends Comparable<V>> BlockStatePredicate a(IBlockState<V> ☃, Predicate<? extends V> ☃)
  {
    if (!this.a.d().contains(☃)) {
      throw new IllegalArgumentException(this.a + " cannot support property " + ☃);
    }
    this.b.put(☃, ☃);
    return this;
  }
}
