package com.avaje.ebean.common;

import com.avaje.ebean.bean.BeanCollection;
import java.util.ListIterator;

class ModifyListIterator<E>
  implements ListIterator<E>
{
  private final BeanCollection<E> owner;
  private final ListIterator<E> it;
  private E last;
  
  ModifyListIterator(BeanCollection<E> owner, ListIterator<E> it)
  {
    this.owner = owner;
    this.it = it;
  }
  
  public void add(E bean)
  {
    this.owner.modifyAddition(bean);
    this.last = null;
    this.it.add(bean);
  }
  
  public boolean hasNext()
  {
    return this.it.hasNext();
  }
  
  public boolean hasPrevious()
  {
    return this.it.hasPrevious();
  }
  
  public E next()
  {
    this.last = this.it.next();
    return (E)this.last;
  }
  
  public int nextIndex()
  {
    return this.it.nextIndex();
  }
  
  public E previous()
  {
    this.last = this.it.previous();
    return (E)this.last;
  }
  
  public int previousIndex()
  {
    return this.it.previousIndex();
  }
  
  public void remove()
  {
    this.owner.modifyRemoval(this.last);
    this.last = null;
    this.it.remove();
  }
  
  public void set(E o)
  {
    if (this.last != null)
    {
      this.owner.modifyRemoval(this.last);
      this.owner.modifyAddition(o);
    }
    this.it.set(o);
  }
}
