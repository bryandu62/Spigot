package net.minecraft.server.v1_8_R3;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import java.util.Collection;

public class BlockStateDirection
  extends BlockStateEnum<EnumDirection>
{
  protected BlockStateDirection(String ☃, Collection<EnumDirection> ☃)
  {
    super(☃, EnumDirection.class, ☃);
  }
  
  public static BlockStateDirection of(String ☃)
  {
    return of(☃, Predicates.alwaysTrue());
  }
  
  public static BlockStateDirection of(String ☃, Predicate<EnumDirection> ☃)
  {
    return a(☃, Collections2.filter(Lists.newArrayList(EnumDirection.values()), ☃));
  }
  
  public static BlockStateDirection a(String ☃, Collection<EnumDirection> ☃)
  {
    return new BlockStateDirection(☃, ☃);
  }
}
