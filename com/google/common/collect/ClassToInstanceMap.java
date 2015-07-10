package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Map;
import javax.annotation.Nullable;

@GwtCompatible
public abstract interface ClassToInstanceMap<B>
  extends Map<Class<? extends B>, B>
{
  public abstract <T extends B> T getInstance(Class<T> paramClass);
  
  public abstract <T extends B> T putInstance(Class<T> paramClass, @Nullable T paramT);
}
