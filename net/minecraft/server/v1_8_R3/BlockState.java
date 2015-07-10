package net.minecraft.server.v1_8_R3;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;

public abstract class BlockState<T extends Comparable<T>>
  implements IBlockState<T>
{
  private final Class<T> a;
  private final String b;
  
  protected BlockState(String ☃, Class<T> ☃)
  {
    this.a = ☃;
    this.b = ☃;
  }
  
  public String a()
  {
    return this.b;
  }
  
  public Class<T> b()
  {
    return this.a;
  }
  
  public String toString()
  {
    return Objects.toStringHelper(this).add("name", this.b).add("clazz", this.a).add("values", c()).toString();
  }
  
  public boolean equals(Object ☃)
  {
    if (this == ☃) {
      return true;
    }
    if ((☃ == null) || (getClass() != ☃.getClass())) {
      return false;
    }
    BlockState ☃ = (BlockState)☃;
    return (this.a.equals(☃.a)) && (this.b.equals(☃.b));
  }
  
  public int hashCode()
  {
    return 31 * this.a.hashCode() + this.b.hashCode();
  }
}
