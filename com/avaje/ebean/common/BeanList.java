package com.avaje.ebean.common;

import com.avaje.ebean.bean.BeanCollectionAdd;
import com.avaje.ebean.bean.BeanCollectionLoader;
import com.avaje.ebean.bean.SerializeControl;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public final class BeanList<E>
  extends AbstractBeanCollection<E>
  implements List<E>, BeanCollectionAdd
{
  private List<E> list;
  
  public BeanList(List<E> list)
  {
    this.list = list;
  }
  
  public BeanList()
  {
    this(new ArrayList());
  }
  
  public BeanList(BeanCollectionLoader loader, Object ownerBean, String propertyName)
  {
    super(loader, ownerBean, propertyName);
  }
  
  Object readResolve()
    throws ObjectStreamException
  {
    if (SerializeControl.isVanillaCollections()) {
      return this.list;
    }
    return this;
  }
  
  Object writeReplace()
    throws ObjectStreamException
  {
    if (SerializeControl.isVanillaCollections()) {
      return this.list;
    }
    return this;
  }
  
  public void addBean(Object bean)
  {
    this.list.add(bean);
  }
  
  public void internalAdd(Object bean)
  {
    this.list.add(bean);
  }
  
  public boolean checkEmptyLazyLoad()
  {
    if (this.list == null)
    {
      this.list = new ArrayList();
      return true;
    }
    return false;
  }
  
  private void initClear()
  {
    synchronized (this)
    {
      if (this.list == null) {
        if (this.modifyListening) {
          lazyLoadCollection(true);
        } else {
          this.list = new ArrayList();
        }
      }
      touched();
    }
  }
  
  private void init()
  {
    synchronized (this)
    {
      if (this.list == null) {
        lazyLoadCollection(false);
      }
      touched();
    }
  }
  
  public void setActualList(List<?> list)
  {
    this.list = list;
  }
  
  public List<E> getActualList()
  {
    return this.list;
  }
  
  public Collection<E> getActualDetails()
  {
    return this.list;
  }
  
  public Object getActualCollection()
  {
    return this.list;
  }
  
  public boolean isPopulated()
  {
    return this.list != null;
  }
  
  public boolean isReference()
  {
    return this.list == null;
  }
  
  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    sb.append("BeanList ");
    if (isReadOnly()) {
      sb.append("readOnly ");
    }
    if (this.list == null)
    {
      sb.append("deferred ");
    }
    else
    {
      sb.append("size[").append(this.list.size()).append("] ");
      sb.append("hasMoreRows[").append(this.hasMoreRows).append("] ");
      sb.append("list").append(this.list).append("");
    }
    return sb.toString();
  }
  
  public boolean equals(Object obj)
  {
    init();
    return this.list.equals(obj);
  }
  
  public int hashCode()
  {
    init();
    return this.list.hashCode();
  }
  
  public void add(int index, E element)
  {
    checkReadOnly();
    init();
    if (this.modifyAddListening) {
      modifyAddition(element);
    }
    this.list.add(index, element);
  }
  
  public boolean add(E o)
  {
    checkReadOnly();
    init();
    if (this.modifyAddListening)
    {
      if (this.list.add(o))
      {
        modifyAddition(o);
        return true;
      }
      return false;
    }
    return this.list.add(o);
  }
  
  public boolean addAll(Collection<? extends E> c)
  {
    checkReadOnly();
    init();
    if (this.modifyAddListening) {
      getModifyHolder().modifyAdditionAll(c);
    }
    return this.list.addAll(c);
  }
  
  public boolean addAll(int index, Collection<? extends E> c)
  {
    checkReadOnly();
    init();
    if (this.modifyAddListening) {
      getModifyHolder().modifyAdditionAll(c);
    }
    return this.list.addAll(index, c);
  }
  
  public void clear()
  {
    checkReadOnly();
    
    initClear();
    if (this.modifyRemoveListening) {
      for (int i = 0; i < this.list.size(); i++) {
        getModifyHolder().modifyRemoval(this.list.get(i));
      }
    }
    this.list.clear();
  }
  
  public boolean contains(Object o)
  {
    init();
    return this.list.contains(o);
  }
  
  public boolean containsAll(Collection<?> c)
  {
    init();
    return this.list.containsAll(c);
  }
  
  public E get(int index)
  {
    init();
    return (E)this.list.get(index);
  }
  
  public int indexOf(Object o)
  {
    init();
    return this.list.indexOf(o);
  }
  
  public boolean isEmpty()
  {
    init();
    return this.list.isEmpty();
  }
  
  public Iterator<E> iterator()
  {
    init();
    if (isReadOnly()) {
      return new ReadOnlyListIterator(this.list.listIterator());
    }
    if (this.modifyListening)
    {
      Iterator<E> it = this.list.iterator();
      return new ModifyIterator(this, it);
    }
    return this.list.iterator();
  }
  
  public int lastIndexOf(Object o)
  {
    init();
    return this.list.lastIndexOf(o);
  }
  
  public ListIterator<E> listIterator()
  {
    init();
    if (isReadOnly()) {
      return new ReadOnlyListIterator(this.list.listIterator());
    }
    if (this.modifyListening)
    {
      ListIterator<E> it = this.list.listIterator();
      return new ModifyListIterator(this, it);
    }
    return this.list.listIterator();
  }
  
  public ListIterator<E> listIterator(int index)
  {
    init();
    if (isReadOnly()) {
      return new ReadOnlyListIterator(this.list.listIterator(index));
    }
    if (this.modifyListening)
    {
      ListIterator<E> it = this.list.listIterator(index);
      return new ModifyListIterator(this, it);
    }
    return this.list.listIterator(index);
  }
  
  public E remove(int index)
  {
    checkReadOnly();
    init();
    if (this.modifyRemoveListening)
    {
      E o = this.list.remove(index);
      modifyRemoval(o);
      return o;
    }
    return (E)this.list.remove(index);
  }
  
  public boolean remove(Object o)
  {
    checkReadOnly();
    init();
    if (this.modifyRemoveListening)
    {
      boolean isRemove = this.list.remove(o);
      if (isRemove) {
        modifyRemoval(o);
      }
      return isRemove;
    }
    return this.list.remove(o);
  }
  
  public boolean removeAll(Collection<?> c)
  {
    checkReadOnly();
    init();
    if (this.modifyRemoveListening)
    {
      boolean changed = false;
      Iterator<?> it = c.iterator();
      while (it.hasNext())
      {
        Object o = it.next();
        if (this.list.remove(o))
        {
          modifyRemoval(o);
          changed = true;
        }
      }
      return changed;
    }
    return this.list.removeAll(c);
  }
  
  public boolean retainAll(Collection<?> c)
  {
    checkReadOnly();
    init();
    if (this.modifyRemoveListening)
    {
      boolean changed = false;
      Iterator<E> it = this.list.iterator();
      while (it.hasNext())
      {
        Object o = it.next();
        if (!c.contains(o))
        {
          it.remove();
          modifyRemoval(o);
          changed = true;
        }
      }
      return changed;
    }
    return this.list.retainAll(c);
  }
  
  public E set(int index, E element)
  {
    checkReadOnly();
    init();
    if (this.modifyListening)
    {
      E o = this.list.set(index, element);
      modifyAddition(element);
      modifyRemoval(o);
      return o;
    }
    return (E)this.list.set(index, element);
  }
  
  public int size()
  {
    init();
    return this.list.size();
  }
  
  public List<E> subList(int fromIndex, int toIndex)
  {
    init();
    if (isReadOnly()) {
      return Collections.unmodifiableList(this.list.subList(fromIndex, toIndex));
    }
    if (this.modifyListening) {
      return new ModifyList(this, this.list.subList(fromIndex, toIndex));
    }
    return this.list.subList(fromIndex, toIndex);
  }
  
  public Object[] toArray()
  {
    init();
    return this.list.toArray();
  }
  
  public <T> T[] toArray(T[] a)
  {
    init();
    return this.list.toArray(a);
  }
  
  private static class ReadOnlyListIterator<E>
    implements ListIterator<E>, Serializable
  {
    private static final long serialVersionUID = 3097271091406323699L;
    private final ListIterator<E> i;
    
    ReadOnlyListIterator(ListIterator<E> i)
    {
      this.i = i;
    }
    
    public void add(E o)
    {
      throw new IllegalStateException("This collection is in ReadOnly mode");
    }
    
    public void remove()
    {
      throw new IllegalStateException("This collection is in ReadOnly mode");
    }
    
    public void set(E o)
    {
      throw new IllegalStateException("This collection is in ReadOnly mode");
    }
    
    public boolean hasNext()
    {
      return this.i.hasNext();
    }
    
    public boolean hasPrevious()
    {
      return this.i.hasPrevious();
    }
    
    public E next()
    {
      return (E)this.i.next();
    }
    
    public int nextIndex()
    {
      return this.i.nextIndex();
    }
    
    public E previous()
    {
      return (E)this.i.previous();
    }
    
    public int previousIndex()
    {
      return this.i.previousIndex();
    }
  }
}
