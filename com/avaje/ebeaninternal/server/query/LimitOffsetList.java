package com.avaje.ebeaninternal.server.query;

import com.avaje.ebean.Page;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class LimitOffsetList<T>
  implements List<T>
{
  private final LimitOffsetPagingQuery<T> owner;
  private List<T> localCopy;
  
  public LimitOffsetList(LimitOffsetPagingQuery<T> owner)
  {
    this.owner = owner;
  }
  
  private void ensureLocalCopy()
  {
    if (this.localCopy == null)
    {
      this.localCopy = new ArrayList();
      
      int pgIndex = 0;
      for (;;)
      {
        Page<T> page = this.owner.getPage(pgIndex++);
        List<T> list = page.getList();
        this.localCopy.addAll(list);
        if (!page.hasNext()) {
          break;
        }
      }
    }
  }
  
  private boolean hasNext(int position)
  {
    return this.owner.hasNext(position);
  }
  
  public void clear()
  {
    this.localCopy = new ArrayList();
  }
  
  public T get(int index)
  {
    if (this.localCopy != null) {
      return (T)this.localCopy.get(index);
    }
    return (T)this.owner.get(index);
  }
  
  public boolean isEmpty()
  {
    if (this.localCopy != null) {
      return this.localCopy.isEmpty();
    }
    return this.owner.getTotalRowCount() == 0;
  }
  
  public int size()
  {
    if (this.localCopy != null) {
      return this.localCopy.size();
    }
    return this.owner.getTotalRowCount();
  }
  
  public Iterator<T> iterator()
  {
    if (this.localCopy != null) {
      return this.localCopy.iterator();
    }
    return new ListItr(this, 0);
  }
  
  public ListIterator<T> listIterator()
  {
    if (this.localCopy != null) {
      return this.localCopy.listIterator();
    }
    return new ListItr(this, 0);
  }
  
  public ListIterator<T> listIterator(int index)
  {
    if (this.localCopy != null) {
      return this.localCopy.listIterator(index);
    }
    return new ListItr(this, index);
  }
  
  public List<T> subList(int fromIndex, int toIndex)
  {
    if (this.localCopy != null) {
      return this.localCopy.subList(fromIndex, toIndex);
    }
    throw new RuntimeException("Not implemented at this point");
  }
  
  public int lastIndexOf(Object o)
  {
    ensureLocalCopy();
    return this.localCopy.lastIndexOf(o);
  }
  
  public void add(int index, T element)
  {
    ensureLocalCopy();
    this.localCopy.add(index, element);
  }
  
  public boolean add(T o)
  {
    ensureLocalCopy();
    return this.localCopy.add(o);
  }
  
  public boolean addAll(Collection<? extends T> c)
  {
    ensureLocalCopy();
    return this.localCopy.addAll(c);
  }
  
  public boolean addAll(int index, Collection<? extends T> c)
  {
    ensureLocalCopy();
    return this.localCopy.addAll(index, c);
  }
  
  public boolean contains(Object o)
  {
    ensureLocalCopy();
    return this.localCopy.contains(o);
  }
  
  public boolean containsAll(Collection<?> c)
  {
    ensureLocalCopy();
    return this.localCopy.containsAll(c);
  }
  
  public int indexOf(Object o)
  {
    ensureLocalCopy();
    return this.localCopy.indexOf(o);
  }
  
  public T remove(int index)
  {
    ensureLocalCopy();
    return (T)this.localCopy.remove(index);
  }
  
  public boolean remove(Object o)
  {
    ensureLocalCopy();
    return this.localCopy.remove(o);
  }
  
  public boolean removeAll(Collection<?> c)
  {
    ensureLocalCopy();
    return this.localCopy.removeAll(c);
  }
  
  public boolean retainAll(Collection<?> c)
  {
    ensureLocalCopy();
    return this.localCopy.retainAll(c);
  }
  
  public T set(int index, T element)
  {
    ensureLocalCopy();
    return (T)this.localCopy.set(index, element);
  }
  
  public Object[] toArray()
  {
    ensureLocalCopy();
    return this.localCopy.toArray();
  }
  
  public <K> K[] toArray(K[] a)
  {
    ensureLocalCopy();
    return this.localCopy.toArray(a);
  }
  
  private class ListItr
    implements ListIterator<T>
  {
    private LimitOffsetList<T> ownerList;
    private int position;
    
    ListItr(int ownerList)
    {
      this.ownerList = ownerList;
      this.position = position;
    }
    
    public void add(T o)
    {
      this.ownerList.add(this.position++, o);
    }
    
    public boolean hasNext()
    {
      return this.ownerList.hasNext(this.position);
    }
    
    public boolean hasPrevious()
    {
      return this.position > 0;
    }
    
    public T next()
    {
      return (T)this.ownerList.get(this.position++);
    }
    
    public int nextIndex()
    {
      return this.position;
    }
    
    public T previous()
    {
      return (T)LimitOffsetList.this.get(--this.position);
    }
    
    public int previousIndex()
    {
      return this.position - 1;
    }
    
    public void remove()
    {
      throw new RuntimeException("Not supported yet");
    }
    
    public void set(T o)
    {
      throw new RuntimeException("Not supported yet");
    }
  }
}
