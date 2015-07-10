package com.avaje.ebeaninternal.server.el;

import java.util.Comparator;

public abstract interface ElComparator<T>
  extends Comparator<T>
{
  public abstract int compare(T paramT1, T paramT2);
  
  public abstract int compareValue(Object paramObject, T paramT);
}
