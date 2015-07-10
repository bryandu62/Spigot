package com.avaje.ebean.common;

import com.avaje.ebean.bean.BeanCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

class ModifyList<E>
  extends ModifyCollection<E>
  implements List<E>
{
  private final List<E> list;
  
  ModifyList(BeanCollection<E> owner, List<E> list)
  {
    super(owner, list);
    this.list = list;
  }
  
  public void add(int index, E element)
  {
    this.list.add(index, element);
    this.owner.modifyAddition(element);
  }
  
  public boolean addAll(int index, Collection<? extends E> co)
  {
    if (this.list.addAll(index, co))
    {
      Iterator<? extends E> it = co.iterator();
      while (it.hasNext())
      {
        E o = it.next();
        this.owner.modifyAddition(o);
      }
      return true;
    }
    return false;
  }
  
  public E get(int index)
  {
    return (E)this.list.get(index);
  }
  
  public int indexOf(Object o)
  {
    return this.list.indexOf(o);
  }
  
  public int lastIndexOf(Object o)
  {
    return this.list.lastIndexOf(o);
  }
  
  public ListIterator<E> listIterator()
  {
    return new ModifyListIterator(this.owner, this.list.listIterator());
  }
  
  public ListIterator<E> listIterator(int index)
  {
    return new ModifyListIterator(this.owner, this.list.listIterator(index));
  }
  
  public E remove(int index)
  {
    E o = this.list.remove(index);
    this.owner.modifyRemoval(o);
    return o;
  }
  
  public E set(int index, E element)
  {
    E o = this.list.set(index, element);
    this.owner.modifyAddition(element);
    this.owner.modifyRemoval(o);
    return o;
  }
  
  public List<E> subList(int fromIndex, int toIndex)
  {
    return new ModifyList(this.owner, this.list.subList(fromIndex, toIndex));
  }
}
