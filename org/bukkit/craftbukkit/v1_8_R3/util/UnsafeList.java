package org.bukkit.craftbukkit.v1_8_R3.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

public class UnsafeList<E>
  extends AbstractList<E>
  implements List<E>, RandomAccess, Cloneable, Serializable
{
  private static final long serialVersionUID = 8683452581112892191L;
  private transient Object[] data;
  private int size;
  private int initialCapacity;
  private Iterator[] iterPool = new Iterator[1];
  private int maxPool;
  private int poolCounter;
  
  public UnsafeList(int capacity, int maxIterPool)
  {
    if (capacity < 0) {
      capacity = 32;
    }
    int rounded = Integer.highestOneBit(capacity - 1) << 1;
    this.data = new Object[rounded];
    this.initialCapacity = rounded;
    this.maxPool = maxIterPool;
    this.iterPool[0] = new Itr();
  }
  
  public UnsafeList(int capacity)
  {
    this(capacity, 5);
  }
  
  public UnsafeList()
  {
    this(32);
  }
  
  public E get(int index)
  {
    rangeCheck(index);
    
    return (E)this.data[index];
  }
  
  public E unsafeGet(int index)
  {
    return (E)this.data[index];
  }
  
  public E set(int index, E element)
  {
    rangeCheck(index);
    
    E old = this.data[index];
    this.data[index] = element;
    return old;
  }
  
  public boolean add(E element)
  {
    growIfNeeded();
    this.data[(this.size++)] = element;
    return true;
  }
  
  public void add(int index, E element)
  {
    growIfNeeded();
    System.arraycopy(this.data, index, this.data, index + 1, this.size - index);
    this.data[index] = element;
    this.size += 1;
  }
  
  public E remove(int index)
  {
    rangeCheck(index);
    
    E old = this.data[index];
    int movedCount = this.size - index - 1;
    if (movedCount > 0) {
      System.arraycopy(this.data, index + 1, this.data, index, movedCount);
    }
    this.data[(--this.size)] = null;
    
    return old;
  }
  
  public boolean remove(Object o)
  {
    int index = indexOf(o);
    if (index >= 0)
    {
      remove(index);
      return true;
    }
    return false;
  }
  
  public int indexOf(Object o)
  {
    for (int i = 0; i < this.size; i++) {
      if ((o == this.data[i]) || (o.equals(this.data[i]))) {
        return i;
      }
    }
    return -1;
  }
  
  public boolean contains(Object o)
  {
    return indexOf(o) >= 0;
  }
  
  public void clear()
  {
    this.size = 0;
    if (this.data.length > this.initialCapacity << 3) {
      this.data = new Object[this.initialCapacity];
    } else {
      for (int i = 0; i < this.data.length; i++) {
        this.data[i] = null;
      }
    }
  }
  
  public void trimToSize()
  {
    int old = this.data.length;
    int rounded = Integer.highestOneBit(this.size - 1) << 1;
    if (rounded < old) {
      this.data = Arrays.copyOf(this.data, rounded);
    }
  }
  
  public int size()
  {
    return this.size;
  }
  
  public boolean isEmpty()
  {
    return this.size == 0;
  }
  
  public Object clone()
    throws CloneNotSupportedException
  {
    UnsafeList<E> copy = (UnsafeList)super.clone();
    copy.data = Arrays.copyOf(this.data, this.size);
    copy.size = this.size;
    copy.initialCapacity = this.initialCapacity;
    copy.iterPool = new Iterator[1];
    copy.iterPool[0] = new Itr();
    copy.maxPool = this.maxPool;
    copy.poolCounter = 0;
    return copy;
  }
  
  public Iterator<E> iterator()
  {
    Iterator[] arrayOfIterator1;
    int i = (arrayOfIterator1 = this.iterPool).length;
    for (int j = 0; j < i; j++)
    {
      Iterator iter = arrayOfIterator1[j];
      if (!((Itr)iter).valid)
      {
        UnsafeList<E>.Itr iterator = (Itr)iter;
        iterator.reset();
        return iterator;
      }
    }
    if (this.iterPool.length < this.maxPool)
    {
      Iterator[] newPool = new Iterator[this.iterPool.length + 1];
      System.arraycopy(this.iterPool, 0, newPool, 0, this.iterPool.length);
      this.iterPool = newPool;
      
      this.iterPool[(this.iterPool.length - 1)] = new Itr();
      return this.iterPool[(this.iterPool.length - 1)];
    }
    this.poolCounter = (++this.poolCounter % this.iterPool.length);
    this.iterPool[this.poolCounter] = new Itr();
    return this.iterPool[this.poolCounter];
  }
  
  private void rangeCheck(int index)
  {
    if ((index >= this.size) || (index < 0)) {
      throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size);
    }
  }
  
  private void growIfNeeded()
  {
    if (this.size == this.data.length)
    {
      Object[] newData = new Object[this.data.length << 1];
      System.arraycopy(this.data, 0, newData, 0, this.size);
      this.data = newData;
    }
  }
  
  private void writeObject(ObjectOutputStream os)
    throws IOException
  {
    os.defaultWriteObject();
    
    os.writeInt(this.size);
    os.writeInt(this.initialCapacity);
    for (int i = 0; i < this.size; i++) {
      os.writeObject(this.data[i]);
    }
    os.writeInt(this.maxPool);
  }
  
  private void readObject(ObjectInputStream is)
    throws IOException, ClassNotFoundException
  {
    is.defaultReadObject();
    
    this.size = is.readInt();
    this.initialCapacity = is.readInt();
    this.data = new Object[Integer.highestOneBit(this.size - 1) << 1];
    for (int i = 0; i < this.size; i++) {
      this.data[i] = is.readObject();
    }
    this.maxPool = is.readInt();
    this.iterPool = new Iterator[1];
    this.iterPool[0] = new Itr();
  }
  
  public class Itr
    implements Iterator<E>
  {
    int index;
    int lastRet = -1;
    int expectedModCount = UnsafeList.this.modCount;
    public boolean valid = true;
    
    public Itr() {}
    
    public void reset()
    {
      this.index = 0;
      this.lastRet = -1;
      this.expectedModCount = UnsafeList.this.modCount;
      this.valid = true;
    }
    
    public boolean hasNext()
    {
      this.valid = (this.index != UnsafeList.this.size);
      return this.valid;
    }
    
    public E next()
    {
      if (UnsafeList.this.modCount != this.expectedModCount) {
        throw new ConcurrentModificationException();
      }
      int i = this.index;
      if (i >= UnsafeList.this.size) {
        throw new NoSuchElementException();
      }
      if (i >= UnsafeList.this.data.length) {
        throw new ConcurrentModificationException();
      }
      this.index = (i + 1);
      return (E)UnsafeList.this.data[(this.lastRet = i)];
    }
    
    public void remove()
    {
      if (this.lastRet < 0) {
        throw new IllegalStateException();
      }
      if (UnsafeList.this.modCount != this.expectedModCount) {
        throw new ConcurrentModificationException();
      }
      try
      {
        UnsafeList.this.remove(this.lastRet);
        this.index = this.lastRet;
        this.lastRet = -1;
        this.expectedModCount = UnsafeList.this.modCount;
      }
      catch (IndexOutOfBoundsException localIndexOutOfBoundsException)
      {
        throw new ConcurrentModificationException();
      }
    }
  }
}
