package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
abstract interface Constraint<E>
{
  public abstract E checkElement(E paramE);
  
  public abstract String toString();
}
