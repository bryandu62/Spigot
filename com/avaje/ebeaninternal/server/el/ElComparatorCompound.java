package com.avaje.ebeaninternal.server.el;

import java.util.Comparator;

public final class ElComparatorCompound<T>
  implements Comparator<T>, ElComparator<T>
{
  private final ElComparator<T>[] array;
  
  public ElComparatorCompound(ElComparator<T>[] array)
  {
    this.array = array;
  }
  
  public int compare(T o1, T o2)
  {
    for (int i = 0; i < this.array.length; i++)
    {
      int ret = this.array[i].compare(o1, o2);
      if (ret != 0) {
        return ret;
      }
    }
    return 0;
  }
  
  public int compareValue(Object value, T o2)
  {
    for (int i = 0; i < this.array.length; i++)
    {
      int ret = this.array[i].compareValue(value, o2);
      if (ret != 0) {
        return ret;
      }
    }
    return 0;
  }
}
