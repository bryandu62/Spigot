package com.avaje.ebean.common;

import com.avaje.ebean.bean.BeanCollection;
import java.util.Collection;
import java.util.Iterator;

class ModifyCollection<E>
  implements Collection<E>
{
  protected final BeanCollection<E> owner;
  protected final Collection<E> c;
  
  public ModifyCollection(BeanCollection<E> owner, Collection<E> c)
  {
    this.owner = owner;
    this.c = c;
  }
  
  public boolean add(E o)
  {
    if (this.c.add(o))
    {
      this.owner.modifyAddition(o);
      return true;
    }
    return false;
  }
  
  public boolean addAll(Collection<? extends E> collection)
  {
    boolean changed = false;
    Iterator<? extends E> it = collection.iterator();
    while (it.hasNext())
    {
      E o = it.next();
      if (this.c.add(o))
      {
        this.owner.modifyAddition(o);
        changed = true;
      }
    }
    return changed;
  }
  
  public void clear()
  {
    this.c.clear();
  }
  
  public boolean contains(Object o)
  {
    return this.c.contains(o);
  }
  
  public boolean containsAll(Collection<?> collection)
  {
    return this.c.containsAll(collection);
  }
  
  public boolean isEmpty()
  {
    return this.c.isEmpty();
  }
  
  public Iterator<E> iterator()
  {
    Iterator<E> it = this.c.iterator();
    return new ModifyIterator(this.owner, it);
  }
  
  public boolean remove(Object o)
  {
    if (this.c.remove(o))
    {
      this.owner.modifyRemoval(o);
      return true;
    }
    return false;
  }
  
  public boolean removeAll(Collection<?> collection)
  {
    boolean changed = false;
    Iterator<?> it = collection.iterator();
    while (it.hasNext())
    {
      Object o = it.next();
      if (this.c.remove(o))
      {
        this.owner.modifyRemoval(o);
        changed = true;
      }
    }
    return changed;
  }
  
  public boolean retainAll(Collection<?> collection)
  {
    boolean changed = false;
    Iterator<?> it = this.c.iterator();
    while (it.hasNext())
    {
      Object o = it.next();
      if (!collection.contains(o))
      {
        it.remove();
        this.owner.modifyRemoval(o);
        changed = true;
      }
    }
    return changed;
  }
  
  public int size()
  {
    return this.c.size();
  }
  
  public Object[] toArray()
  {
    return this.c.toArray();
  }
  
  public <T> T[] toArray(T[] a)
  {
    return this.c.toArray(a);
  }
}
