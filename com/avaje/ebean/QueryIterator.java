package com.avaje.ebean;

import java.io.Closeable;
import java.util.Iterator;

public abstract interface QueryIterator<T>
  extends Iterator<T>, Closeable
{
  public abstract boolean hasNext();
  
  public abstract T next();
  
  public abstract void remove();
  
  public abstract void close();
}
