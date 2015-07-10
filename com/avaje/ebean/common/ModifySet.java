package com.avaje.ebean.common;

import com.avaje.ebean.bean.BeanCollection;
import java.util.Set;

class ModifySet<E>
  extends ModifyCollection<E>
  implements Set<E>
{
  public ModifySet(BeanCollection<E> owner, Set<E> s)
  {
    super(owner, s);
  }
}
