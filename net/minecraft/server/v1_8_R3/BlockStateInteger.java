package net.minecraft.server.v1_8_R3;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Set;

public class BlockStateInteger
  extends BlockState<Integer>
{
  private final ImmutableSet<Integer> a;
  
  protected BlockStateInteger(String ☃, int ☃, int ☃)
  {
    super(☃, Integer.class);
    if (☃ < 0) {
      throw new IllegalArgumentException("Min value of " + ☃ + " must be 0 or greater");
    }
    if (☃ <= ☃) {
      throw new IllegalArgumentException("Max value of " + ☃ + " must be greater than min (" + ☃ + ")");
    }
    Set<Integer> ☃ = Sets.newHashSet();
    for (int ☃ = ☃; ☃ <= ☃; ☃++) {
      ☃.add(Integer.valueOf(☃));
    }
    this.a = ImmutableSet.copyOf(☃);
  }
  
  public Collection<Integer> c()
  {
    return this.a;
  }
  
  public boolean equals(Object ☃)
  {
    if (this == ☃) {
      return true;
    }
    if ((☃ == null) || (getClass() != ☃.getClass())) {
      return false;
    }
    if (!super.equals(☃)) {
      return false;
    }
    BlockStateInteger ☃ = (BlockStateInteger)☃;
    return this.a.equals(☃.a);
  }
  
  public int hashCode()
  {
    int ☃ = super.hashCode();
    ☃ = 31 * ☃ + this.a.hashCode();
    return ☃;
  }
  
  public static BlockStateInteger of(String ☃, int ☃, int ☃)
  {
    return new BlockStateInteger(☃, ☃, ☃);
  }
  
  public String a(Integer ☃)
  {
    return ☃.toString();
  }
}
