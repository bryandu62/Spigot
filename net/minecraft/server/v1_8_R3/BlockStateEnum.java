package net.minecraft.server.v1_8_R3;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;

public class BlockStateEnum<T extends Enum<T>,  extends INamable>
  extends BlockState<T>
{
  private final ImmutableSet<T> a;
  private final Map<String, T> b = Maps.newHashMap();
  
  protected BlockStateEnum(String ☃, Class<T> ☃, Collection<T> ☃)
  {
    super(☃, ☃);
    this.a = ImmutableSet.copyOf(☃);
    for (T ☃ : ☃)
    {
      String ☃ = ((INamable)☃).getName();
      if (this.b.containsKey(☃)) {
        throw new IllegalArgumentException("Multiple values have the same name '" + ☃ + "'");
      }
      this.b.put(☃, ☃);
    }
  }
  
  public Collection<T> c()
  {
    return this.a;
  }
  
  public String a(T ☃)
  {
    return ((INamable)☃).getName();
  }
  
  public static <T extends Enum<T>,  extends INamable> BlockStateEnum<T> of(String ☃, Class<T> ☃)
  {
    return a(☃, ☃, Predicates.alwaysTrue());
  }
  
  public static <T extends Enum<T>,  extends INamable> BlockStateEnum<T> a(String ☃, Class<T> ☃, Predicate<T> ☃)
  {
    return a(☃, ☃, Collections2.filter(Lists.newArrayList(☃.getEnumConstants()), ☃));
  }
  
  public static <T extends Enum<T>,  extends INamable> BlockStateEnum<T> of(String ☃, Class<T> ☃, T... ☃)
  {
    return a(☃, ☃, Lists.newArrayList(☃));
  }
  
  public static <T extends Enum<T>,  extends INamable> BlockStateEnum<T> a(String ☃, Class<T> ☃, Collection<T> ☃)
  {
    return new BlockStateEnum(☃, ☃, ☃);
  }
}
