package net.minecraft.server.v1_8_R3;

import com.google.common.collect.ImmutableSet;
import java.util.Collection;

public class BlockStateBoolean
  extends BlockState<Boolean>
{
  private final ImmutableSet<Boolean> a;
  
  protected BlockStateBoolean(String ☃)
  {
    super(☃, Boolean.class);
    this.a = ImmutableSet.of(Boolean.valueOf(true), Boolean.valueOf(false));
  }
  
  public Collection<Boolean> c()
  {
    return this.a;
  }
  
  public static BlockStateBoolean of(String ☃)
  {
    return new BlockStateBoolean(☃);
  }
  
  public String a(Boolean ☃)
  {
    return ☃.toString();
  }
}
