package com.avaje.ebean.common;

import com.avaje.ebean.bean.BeanCollectionAdd;
import com.avaje.ebean.bean.BeanCollectionLoader;
import com.avaje.ebean.bean.SerializeControl;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public final class BeanSet<E>
  extends AbstractBeanCollection<E>
  implements Set<E>, BeanCollectionAdd
{
  private Set<E> set;
  
  public BeanSet(Set<E> set)
  {
    this.set = set;
  }
  
  public BeanSet()
  {
    this(new LinkedHashSet());
  }
  
  public BeanSet(BeanCollectionLoader loader, Object ownerBean, String propertyName)
  {
    super(loader, ownerBean, propertyName);
  }
  
  Object readResolve()
    throws ObjectStreamException
  {
    if (SerializeControl.isVanillaCollections()) {
      return this.set;
    }
    return this;
  }
  
  Object writeReplace()
    throws ObjectStreamException
  {
    if (SerializeControl.isVanillaCollections()) {
      return this.set;
    }
    return this;
  }
  
  public void addBean(Object bean)
  {
    this.set.add(bean);
  }
  
  public void internalAdd(Object bean)
  {
    this.set.add(bean);
  }
  
  public boolean isPopulated()
  {
    return this.set != null;
  }
  
  public boolean isReference()
  {
    return this.set == null;
  }
  
  public boolean checkEmptyLazyLoad()
  {
    if (this.set == null)
    {
      this.set = new LinkedHashSet();
      return true;
    }
    return false;
  }
  
  private void initClear()
  {
    synchronized (this)
    {
      if (this.set == null) {
        if (this.modifyListening) {
          lazyLoadCollection(true);
        } else {
          this.set = new LinkedHashSet();
        }
      }
      touched();
    }
  }
  
  private void init()
  {
    synchronized (this)
    {
      if (this.set == null) {
        lazyLoadCollection(true);
      }
      touched();
    }
  }
  
  public void setActualSet(Set<?> set)
  {
    this.set = set;
  }
  
  public Set<E> getActualSet()
  {
    return this.set;
  }
  
  public Collection<E> getActualDetails()
  {
    return this.set;
  }
  
  public Object getActualCollection()
  {
    return this.set;
  }
  
  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    sb.append("BeanSet ");
    if (isReadOnly()) {
      sb.append("readOnly ");
    }
    if (this.set == null)
    {
      sb.append("deferred ");
    }
    else
    {
      sb.append("size[").append(this.set.size()).append("]");
      sb.append(" hasMoreRows[").append(this.hasMoreRows).append("]");
      sb.append(" set").append(this.set);
    }
    return sb.toString();
  }
  
  public boolean equals(Object obj)
  {
    init();
    return this.set.equals(obj);
  }
  
  public int hashCode()
  {
    init();
    return this.set.hashCode();
  }
  
  public boolean add(E o)
  {
    checkReadOnly();
    init();
    if (this.modifyAddListening)
    {
      if (this.set.add(o))
      {
        modifyAddition(o);
        return true;
      }
      return false;
    }
    return this.set.add(o);
  }
  
  public boolean addAll(Collection<? extends E> c)
  {
    checkReadOnly();
    init();
    if (this.modifyAddListening)
    {
      boolean changed = false;
      Iterator<? extends E> it = c.iterator();
      while (it.hasNext())
      {
        E o = it.next();
        if (this.set.add(o))
        {
          modifyAddition(o);
          changed = true;
        }
      }
      return changed;
    }
    return this.set.addAll(c);
  }
  
  public void clear()
  {
    checkReadOnly();
    initClear();
    if (this.modifyRemoveListening)
    {
      Iterator<E> it = this.set.iterator();
      while (it.hasNext())
      {
        E e = it.next();
        modifyRemoval(e);
      }
    }
    this.set.clear();
  }
  
  public boolean contains(Object o)
  {
    init();
    return this.set.contains(o);
  }
  
  public boolean containsAll(Collection<?> c)
  {
    init();
    return this.set.containsAll(c);
  }
  
  public boolean isEmpty()
  {
    init();
    return this.set.isEmpty();
  }
  
  public Iterator<E> iterator()
  {
    init();
    if (isReadOnly()) {
      return new ReadOnlyIterator(this.set.iterator());
    }
    if (this.modifyListening) {
      return new ModifyIterator(this, this.set.iterator());
    }
    return this.set.iterator();
  }
  
  public boolean remove(Object o)
  {
    checkReadOnly();
    init();
    if (this.modifyRemoveListening)
    {
      if (this.set.remove(o))
      {
        modifyRemoval(o);
        return true;
      }
      return false;
    }
    return this.set.remove(o);
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
        if (this.set.remove(o))
        {
          modifyRemoval(o);
          changed = true;
        }
      }
      return changed;
    }
    return this.set.removeAll(c);
  }
  
  public boolean retainAll(Collection<?> c)
  {
    checkReadOnly();
    init();
    if (this.modifyRemoveListening)
    {
      boolean changed = false;
      Iterator<?> it = this.set.iterator();
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
    return this.set.retainAll(c);
  }
  
  public int size()
  {
    init();
    return this.set.size();
  }
  
  public Object[] toArray()
  {
    init();
    return this.set.toArray();
  }
  
  public <T> T[] toArray(T[] a)
  {
    init();
    return this.set.toArray(a);
  }
  
  private static class ReadOnlyIterator<E>
    implements Iterator<E>, Serializable
  {
    private static final long serialVersionUID = 2577697326745352605L;
    private final Iterator<E> it;
    
    ReadOnlyIterator(Iterator<E> it)
    {
      this.it = it;
    }
    
    public boolean hasNext()
    {
      return this.it.hasNext();
    }
    
    public E next()
    {
      return (E)this.it.next();
    }
    
    public void remove()
    {
      throw new IllegalStateException("This collection is in ReadOnly mode");
    }
  }
}
