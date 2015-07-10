package com.avaje.ebean.common;

import com.avaje.ebean.bean.BeanCollection;
import java.util.Iterator;

class ModifyIterator<E>
  implements Iterator<E>
{
  private final BeanCollection<E> owner;
  private final Iterator<E> it;
  private E last;
  
  ModifyIterator(BeanCollection<E> owner, Iterator<E> it)
  {
    this.owner = owner;
    this.it = it;
  }
  
  public boolean hasNext()
  {
    return this.it.hasNext();
  }
  
  public E next()
  {
    this.last = this.it.next();
    return (E)this.last;
  }
  
  public void remove()
  {
    this.owner.modifyRemoval(this.last);
    this.it.remove();
  }
}
