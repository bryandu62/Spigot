package com.avaje.ebean.common;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

class ModifyHolder<E>
  implements Serializable
{
  private static final long serialVersionUID = 2572572897923801083L;
  private Set<E> modifyDeletions = new LinkedHashSet();
  private Set<E> modifyAdditions = new LinkedHashSet();
  
  void reset()
  {
    this.modifyDeletions = new LinkedHashSet();
    this.modifyAdditions = new LinkedHashSet();
  }
  
  void modifyAdditionAll(Collection<? extends E> c)
  {
    if (c != null) {
      for (E e : c) {
        modifyAddition(e);
      }
    }
  }
  
  void modifyAddition(E bean)
  {
    if (bean != null) {
      if (!this.modifyDeletions.remove(bean)) {
        this.modifyAdditions.add(bean);
      }
    }
  }
  
  void modifyRemoval(Object bean)
  {
    if (bean != null) {
      if (!this.modifyAdditions.remove(bean)) {
        this.modifyDeletions.add(bean);
      }
    }
  }
  
  Set<E> getModifyAdditions()
  {
    return this.modifyAdditions;
  }
  
  Set<E> getModifyRemovals()
  {
    return this.modifyDeletions;
  }
}
