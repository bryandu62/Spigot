package com.avaje.ebeaninternal.server.loadcontext;

import java.util.List;

public abstract interface DLoadList<T>
{
  public abstract int add(T paramT);
  
  public abstract List<T> getNextBatch(int paramInt);
  
  public abstract void removeEntry(int paramInt);
  
  public abstract List<T> getLoadBatch(int paramInt1, int paramInt2);
}
