package org.apache.logging.log4j.spi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class DefaultThreadContextStack
  implements ThreadContextStack
{
  private static final long serialVersionUID = 5050501L;
  private static ThreadLocal<List<String>> stack = new ThreadLocal();
  private final boolean useStack;
  
  public DefaultThreadContextStack(boolean useStack)
  {
    this.useStack = useStack;
  }
  
  public String pop()
  {
    if (!this.useStack) {
      return "";
    }
    List<String> list = (List)stack.get();
    if ((list == null) || (list.size() == 0)) {
      throw new NoSuchElementException("The ThreadContext stack is empty");
    }
    List<String> copy = new ArrayList(list);
    int last = copy.size() - 1;
    String result = (String)copy.remove(last);
    stack.set(Collections.unmodifiableList(copy));
    return result;
  }
  
  public String peek()
  {
    List<String> list = (List)stack.get();
    if ((list == null) || (list.size() == 0)) {
      return null;
    }
    int last = list.size() - 1;
    return (String)list.get(last);
  }
  
  public void push(String message)
  {
    if (!this.useStack) {
      return;
    }
    add(message);
  }
  
  public int getDepth()
  {
    List<String> list = (List)stack.get();
    return list == null ? 0 : list.size();
  }
  
  public List<String> asList()
  {
    List<String> list = (List)stack.get();
    if (list == null) {
      return Collections.emptyList();
    }
    return list;
  }
  
  public void trim(int depth)
  {
    if (depth < 0) {
      throw new IllegalArgumentException("Maximum stack depth cannot be negative");
    }
    List<String> list = (List)stack.get();
    if (list == null) {
      return;
    }
    List<String> copy = new ArrayList();
    int count = Math.min(depth, list.size());
    for (int i = 0; i < count; i++) {
      copy.add(list.get(i));
    }
    stack.set(copy);
  }
  
  public ThreadContextStack copy()
  {
    List<String> result = null;
    if ((!this.useStack) || ((result = (List)stack.get()) == null)) {
      return new MutableThreadContextStack(new ArrayList());
    }
    return new MutableThreadContextStack(result);
  }
  
  public void clear()
  {
    stack.remove();
  }
  
  public int size()
  {
    List<String> result = (List)stack.get();
    return result == null ? 0 : result.size();
  }
  
  public boolean isEmpty()
  {
    List<String> result = (List)stack.get();
    return (result == null) || (result.isEmpty());
  }
  
  public boolean contains(Object o)
  {
    List<String> result = (List)stack.get();
    return (result != null) && (result.contains(o));
  }
  
  public Iterator<String> iterator()
  {
    List<String> immutable = (List)stack.get();
    if (immutable == null)
    {
      List<String> empty = Collections.emptyList();
      return empty.iterator();
    }
    return immutable.iterator();
  }
  
  public Object[] toArray()
  {
    List<String> result = (List)stack.get();
    if (result == null) {
      return new String[0];
    }
    return result.toArray(new Object[result.size()]);
  }
  
  public <T> T[] toArray(T[] ts)
  {
    List<String> result = (List)stack.get();
    if (result == null)
    {
      if (ts.length > 0) {
        ts[0] = null;
      }
      return ts;
    }
    return result.toArray(ts);
  }
  
  public boolean add(String s)
  {
    if (!this.useStack) {
      return false;
    }
    List<String> list = (List)stack.get();
    List<String> copy = list == null ? new ArrayList() : new ArrayList(list);
    
    copy.add(s);
    stack.set(Collections.unmodifiableList(copy));
    return true;
  }
  
  public boolean remove(Object o)
  {
    if (!this.useStack) {
      return false;
    }
    List<String> list = (List)stack.get();
    if ((list == null) || (list.size() == 0)) {
      return false;
    }
    List<String> copy = new ArrayList(list);
    boolean result = copy.remove(o);
    stack.set(Collections.unmodifiableList(copy));
    return result;
  }
  
  public boolean containsAll(Collection<?> objects)
  {
    if (objects.isEmpty()) {
      return true;
    }
    List<String> list = (List)stack.get();
    return (list != null) && (list.containsAll(objects));
  }
  
  public boolean addAll(Collection<? extends String> strings)
  {
    if ((!this.useStack) || (strings.isEmpty())) {
      return false;
    }
    List<String> list = (List)stack.get();
    List<String> copy = list == null ? new ArrayList() : new ArrayList(list);
    
    copy.addAll(strings);
    stack.set(Collections.unmodifiableList(copy));
    return true;
  }
  
  public boolean removeAll(Collection<?> objects)
  {
    if ((!this.useStack) || (objects.isEmpty())) {
      return false;
    }
    List<String> list = (List)stack.get();
    if ((list == null) || (list.isEmpty())) {
      return false;
    }
    List<String> copy = new ArrayList(list);
    boolean result = copy.removeAll(objects);
    stack.set(Collections.unmodifiableList(copy));
    return result;
  }
  
  public boolean retainAll(Collection<?> objects)
  {
    if ((!this.useStack) || (objects.isEmpty())) {
      return false;
    }
    List<String> list = (List)stack.get();
    if ((list == null) || (list.isEmpty())) {
      return false;
    }
    List<String> copy = new ArrayList(list);
    boolean result = copy.retainAll(objects);
    stack.set(Collections.unmodifiableList(copy));
    return result;
  }
  
  public String toString()
  {
    List<String> list = (List)stack.get();
    return list == null ? "[]" : list.toString();
  }
}
