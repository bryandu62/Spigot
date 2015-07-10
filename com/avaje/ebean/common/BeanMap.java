package com.avaje.ebean.common;

import com.avaje.ebean.bean.BeanCollectionLoader;
import com.avaje.ebean.bean.SerializeControl;
import java.io.ObjectStreamException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public final class BeanMap<K, E>
  extends AbstractBeanCollection<E>
  implements Map<K, E>
{
  private Map<K, E> map;
  
  public BeanMap(Map<K, E> map)
  {
    this.map = map;
  }
  
  public BeanMap()
  {
    this(new LinkedHashMap());
  }
  
  public BeanMap(BeanCollectionLoader ebeanServer, Object ownerBean, String propertyName)
  {
    super(ebeanServer, ownerBean, propertyName);
  }
  
  Object readResolve()
    throws ObjectStreamException
  {
    if (SerializeControl.isVanillaCollections()) {
      return this.map;
    }
    return this;
  }
  
  Object writeReplace()
    throws ObjectStreamException
  {
    if (SerializeControl.isVanillaCollections()) {
      return this.map;
    }
    return this;
  }
  
  public void internalAdd(Object bean)
  {
    throw new RuntimeException("Not allowed for map");
  }
  
  public boolean isPopulated()
  {
    return this.map != null;
  }
  
  public boolean isReference()
  {
    return this.map == null;
  }
  
  public boolean checkEmptyLazyLoad()
  {
    if (this.map == null)
    {
      this.map = new LinkedHashMap();
      return true;
    }
    return false;
  }
  
  private void initClear()
  {
    synchronized (this)
    {
      if (this.map == null) {
        if (this.modifyListening) {
          lazyLoadCollection(true);
        } else {
          this.map = new LinkedHashMap();
        }
      }
      touched();
    }
  }
  
  private void init()
  {
    synchronized (this)
    {
      if (this.map == null) {
        lazyLoadCollection(false);
      }
      touched();
    }
  }
  
  public void setActualMap(Map<?, ?> map)
  {
    this.map = map;
  }
  
  public Map<K, E> getActualMap()
  {
    return this.map;
  }
  
  public Collection<E> getActualDetails()
  {
    return this.map.values();
  }
  
  public Object getActualCollection()
  {
    return this.map;
  }
  
  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    sb.append("BeanMap ");
    if (isReadOnly()) {
      sb.append("readOnly ");
    }
    if (this.map == null)
    {
      sb.append("deferred ");
    }
    else
    {
      sb.append("size[").append(this.map.size()).append("]");
      sb.append(" hasMoreRows[").append(this.hasMoreRows).append("]");
      sb.append(" map").append(this.map);
    }
    return sb.toString();
  }
  
  public boolean equals(Object obj)
  {
    init();
    return this.map.equals(obj);
  }
  
  public int hashCode()
  {
    init();
    return this.map.hashCode();
  }
  
  public void clear()
  {
    checkReadOnly();
    initClear();
    if (this.modifyRemoveListening) {
      for (K key : this.map.keySet())
      {
        E o = this.map.remove(key);
        modifyRemoval(o);
      }
    }
    this.map.clear();
  }
  
  public boolean containsKey(Object key)
  {
    init();
    return this.map.containsKey(key);
  }
  
  public boolean containsValue(Object value)
  {
    init();
    return this.map.containsValue(value);
  }
  
  public Set<Map.Entry<K, E>> entrySet()
  {
    init();
    if (isReadOnly()) {
      return Collections.unmodifiableSet(this.map.entrySet());
    }
    if (this.modifyListening)
    {
      Set<Map.Entry<K, E>> s = this.map.entrySet();
      return new ModifySet(this, s);
    }
    return this.map.entrySet();
  }
  
  public E get(Object key)
  {
    init();
    return (E)this.map.get(key);
  }
  
  public boolean isEmpty()
  {
    init();
    return this.map.isEmpty();
  }
  
  public Set<K> keySet()
  {
    init();
    if (isReadOnly()) {
      return Collections.unmodifiableSet(this.map.keySet());
    }
    return this.map.keySet();
  }
  
  public E put(K key, E value)
  {
    checkReadOnly();
    init();
    if (this.modifyListening)
    {
      Object o = this.map.put(key, value);
      modifyAddition(value);
      modifyRemoval(o);
    }
    return (E)this.map.put(key, value);
  }
  
  public void putAll(Map<? extends K, ? extends E> t)
  {
    checkReadOnly();
    init();
    if (this.modifyListening)
    {
      Iterator it = t.entrySet().iterator();
      while (it.hasNext())
      {
        Map.Entry entry = (Map.Entry)it.next();
        Object o = this.map.put(entry.getKey(), entry.getValue());
        modifyAddition(entry.getValue());
        modifyRemoval(o);
      }
    }
    this.map.putAll(t);
  }
  
  public E remove(Object key)
  {
    checkReadOnly();
    init();
    if (this.modifyRemoveListening)
    {
      E o = this.map.remove(key);
      modifyRemoval(o);
      return o;
    }
    return (E)this.map.remove(key);
  }
  
  public int size()
  {
    init();
    return this.map.size();
  }
  
  public Collection<E> values()
  {
    init();
    if (isReadOnly()) {
      return Collections.unmodifiableCollection(this.map.values());
    }
    if (this.modifyListening)
    {
      Collection<E> c = this.map.values();
      return new ModifyCollection(this, c);
    }
    return this.map.values();
  }
}
