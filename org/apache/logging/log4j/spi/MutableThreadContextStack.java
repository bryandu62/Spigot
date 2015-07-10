package org.apache.logging.log4j.spi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class MutableThreadContextStack
  implements ThreadContextStack
{
  private static final long serialVersionUID = 50505011L;
  private final List<String> list;
  
  public MutableThreadContextStack(List<String> list)
  {
    this.list = new ArrayList(list);
  }
  
  private MutableThreadContextStack(MutableThreadContextStack stack)
  {
    this.list = new ArrayList(stack.list);
  }
  
  public String pop()
  {
    if (this.list.isEmpty()) {
      return null;
    }
    int last = this.list.size() - 1;
    String result = (String)this.list.remove(last);
    return result;
  }
  
  public String peek()
  {
    if (this.list.isEmpty()) {
      return null;
    }
    int last = this.list.size() - 1;
    return (String)this.list.get(last);
  }
  
  public void push(String message)
  {
    this.list.add(message);
  }
  
  public int getDepth()
  {
    return this.list.size();
  }
  
  public List<String> asList()
  {
    return this.list;
  }
  
  public void trim(int depth)
  {
    if (depth < 0) {
      throw new IllegalArgumentException("Maximum stack depth cannot be negative");
    }
    if (this.list == null) {
      return;
    }
    List<String> copy = new ArrayList(this.list.size());
    int count = Math.min(depth, this.list.size());
    for (int i = 0; i < count; i++) {
      copy.add(this.list.get(i));
    }
    this.list.clear();
    this.list.addAll(copy);
  }
  
  public ThreadContextStack copy()
  {
    return new MutableThreadContextStack(this);
  }
  
  public void clear()
  {
    this.list.clear();
  }
  
  public int size()
  {
    return this.list.size();
  }
  
  public boolean isEmpty()
  {
    return this.list.isEmpty();
  }
  
  public boolean contains(Object o)
  {
    return this.list.contains(o);
  }
  
  public Iterator<String> iterator()
  {
    return this.list.iterator();
  }
  
  public Object[] toArray()
  {
    return this.list.toArray();
  }
  
  public <T> T[] toArray(T[] ts)
  {
    return this.list.toArray(ts);
  }
  
  public boolean add(String s)
  {
    return this.list.add(s);
  }
  
  public boolean remove(Object o)
  {
    return this.list.remove(o);
  }
  
  public boolean containsAll(Collection<?> objects)
  {
    return this.list.containsAll(objects);
  }
  
  public boolean addAll(Collection<? extends String> strings)
  {
    return this.list.addAll(strings);
  }
  
  public boolean removeAll(Collection<?> objects)
  {
    return this.list.removeAll(objects);
  }
  
  public boolean retainAll(Collection<?> objects)
  {
    return this.list.retainAll(objects);
  }
  
  public String toString()
  {
    return String.valueOf(this.list);
  }
}
