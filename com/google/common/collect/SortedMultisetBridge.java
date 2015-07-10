package com.google.common.collect;

import java.util.SortedSet;

abstract interface SortedMultisetBridge<E>
  extends Multiset<E>
{
  public abstract SortedSet<E> elementSet();
}
