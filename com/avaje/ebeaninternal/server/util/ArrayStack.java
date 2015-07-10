package com.avaje.ebeaninternal.server.util;

import java.util.ArrayList;
import java.util.EmptyStackException;

public class ArrayStack<E>
{
  private final ArrayList<E> list;
  
  public ArrayStack(int size)
  {
    this.list = new ArrayList(size);
  }
  
  public ArrayStack()
  {
    this.list = new ArrayList();
  }
  
  public E push(E item)
  {
    this.list.add(item);
    return item;
  }
  
  public E pop()
  {
    int len = this.list.size();
    E obj = peek();
    this.list.remove(len - 1);
    return obj;
  }
  
  protected E peekZero(boolean retNull)
  {
    int len = this.list.size();
    if (len == 0)
    {
      if (retNull) {
        return null;
      }
      throw new EmptyStackException();
    }
    return (E)this.list.get(len - 1);
  }
  
  public E peek()
  {
    return (E)peekZero(false);
  }
  
  public E peekWithNull()
  {
    return (E)peekZero(true);
  }
  
  public boolean isEmpty()
  {
    return this.list.isEmpty();
  }
  
  public int size()
  {
    return this.list.size();
  }
  
  public boolean contains(Object o)
  {
    return this.list.contains(o);
  }
}
