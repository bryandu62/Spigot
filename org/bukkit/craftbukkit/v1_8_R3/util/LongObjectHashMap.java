package org.bukkit.craftbukkit.v1_8_R3.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

public class LongObjectHashMap<V>
  implements Cloneable, Serializable
{
  static final long serialVersionUID = 2841537710170573815L;
  private static final long EMPTY_KEY = Long.MIN_VALUE;
  private static final int BUCKET_SIZE = 4096;
  private transient long[][] keys;
  private transient V[][] values;
  private transient int modCount;
  private transient int size;
  
  public LongObjectHashMap()
  {
    initialize();
  }
  
  public LongObjectHashMap(Map<? extends Long, ? extends V> map)
  {
    this();
    putAll(map);
  }
  
  public int size()
  {
    return this.size;
  }
  
  public boolean isEmpty()
  {
    return this.size == 0;
  }
  
  public boolean containsKey(long key)
  {
    return get(key) != null;
  }
  
  public boolean containsValue(V value)
  {
    for (V val : values()) {
      if ((val == value) || (val.equals(value))) {
        return true;
      }
    }
    return false;
  }
  
  public V get(long key)
  {
    int index = (int)(keyIndex(key) & 0xFFF);
    long[] inner = this.keys[index];
    if (inner == null) {
      return null;
    }
    for (int i = 0; i < inner.length; i++)
    {
      long innerKey = inner[i];
      if (innerKey == Long.MIN_VALUE) {
        return null;
      }
      if (innerKey == key) {
        return (V)this.values[index][i];
      }
    }
    return null;
  }
  
  public V put(long key, V value)
  {
    int index = (int)(keyIndex(key) & 0xFFF);
    long[] innerKeys = this.keys[index];
    Object[] innerValues = this.values[index];
    this.modCount += 1;
    if (innerKeys == null)
    {
      this.keys[index] = (innerKeys = new long[8]);
      Arrays.fill(innerKeys, Long.MIN_VALUE);
      this.values[index] = (innerValues = new Object[8]);
      innerKeys[0] = key;
      innerValues[0] = value;
      this.size += 1;
    }
    else
    {
      for (int i = 0; i < innerKeys.length; i++)
      {
        if (innerKeys[i] == Long.MIN_VALUE)
        {
          this.size += 1;
          innerKeys[i] = key;
          innerValues[i] = value;
          return null;
        }
        if (innerKeys[i] == key)
        {
          V oldValue = innerValues[i];
          innerKeys[i] = key;
          innerValues[i] = value;
          return oldValue;
        }
      }
      this.keys[index] = (innerKeys = Arrays.copyOf(innerKeys, i << 1));
      Arrays.fill(innerKeys, i, innerKeys.length, Long.MIN_VALUE);
      this.values[index] = (innerValues = Arrays.copyOf(innerValues, i << 1));
      innerKeys[i] = key;
      innerValues[i] = value;
      this.size += 1;
    }
    return null;
  }
  
  public V remove(long key)
  {
    int index = (int)(keyIndex(key) & 0xFFF);
    long[] inner = this.keys[index];
    if (inner == null) {
      return null;
    }
    for (int i = 0; i < inner.length; i++)
    {
      if (inner[i] == Long.MIN_VALUE) {
        break;
      }
      if (inner[i] == key)
      {
        V value = this.values[index][i];
        for (i++; i < inner.length; i++)
        {
          if (inner[i] == Long.MIN_VALUE) {
            break;
          }
          inner[(i - 1)] = inner[i];
          this.values[index][(i - 1)] = this.values[index][i];
        }
        inner[(i - 1)] = Long.MIN_VALUE;
        this.values[index][(i - 1)] = null;
        this.size -= 1;
        this.modCount += 1;
        return value;
      }
    }
    return null;
  }
  
  public void putAll(Map<? extends Long, ? extends V> map)
  {
    for (Map.Entry entry : map.entrySet()) {
      put(((Long)entry.getKey()).longValue(), entry.getValue());
    }
  }
  
  public void clear()
  {
    if (this.size == 0) {
      return;
    }
    this.modCount += 1;
    this.size = 0;
    Arrays.fill(this.keys, null);
    Arrays.fill(this.values, null);
  }
  
  public Set<Long> keySet()
  {
    return new KeySet(null);
  }
  
  public Collection<V> values()
  {
    return new ValueCollection(null);
  }
  
  @Deprecated
  public Set<Map.Entry<Long, V>> entrySet()
  {
    HashSet<Map.Entry<Long, V>> set = new HashSet();
    for (Iterator localIterator = keySet().iterator(); localIterator.hasNext();)
    {
      long key = ((Long)localIterator.next()).longValue();
      set.add(new Entry(key, get(key)));
    }
    return set;
  }
  
  public Object clone()
    throws CloneNotSupportedException
  {
    LongObjectHashMap clone = (LongObjectHashMap)super.clone();
    
    clone.clear();
    
    clone.initialize();
    for (Iterator localIterator = keySet().iterator(); localIterator.hasNext();)
    {
      long key = ((Long)localIterator.next()).longValue();
      V value = get(key);
      clone.put(key, value);
    }
    return clone;
  }
  
  private void initialize()
  {
    this.keys = new long['က'][];
    this.values = new Object['က'][];
  }
  
  private long keyIndex(long key)
  {
    key ^= key >>> 33;
    key *= -49064778989728563L;
    key ^= key >>> 33;
    key *= -4265267296055464877L;
    key ^= key >>> 33;
    return key;
  }
  
  private void writeObject(ObjectOutputStream outputStream)
    throws IOException
  {
    outputStream.defaultWriteObject();
    for (Iterator localIterator = keySet().iterator(); localIterator.hasNext();)
    {
      long key = ((Long)localIterator.next()).longValue();
      V value = get(key);
      outputStream.writeLong(key);
      outputStream.writeObject(value);
    }
    outputStream.writeLong(Long.MIN_VALUE);
    outputStream.writeObject(null);
  }
  
  private void readObject(ObjectInputStream inputStream)
    throws ClassNotFoundException, IOException
  {
    inputStream.defaultReadObject();
    initialize();
    for (;;)
    {
      long key = inputStream.readLong();
      V value = inputStream.readObject();
      if ((key == Long.MIN_VALUE) && (value == null)) {
        break;
      }
      put(key, value);
    }
  }
  
  private class ValueIterator
    implements Iterator<V>
  {
    private int count;
    private int index;
    private int innerIndex;
    private int expectedModCount;
    private long lastReturned = Long.MIN_VALUE;
    long prevKey = Long.MIN_VALUE;
    V prevValue;
    
    ValueIterator()
    {
      this.expectedModCount = LongObjectHashMap.this.modCount;
    }
    
    public boolean hasNext()
    {
      return this.count < LongObjectHashMap.this.size;
    }
    
    public void remove()
    {
      if (LongObjectHashMap.this.modCount != this.expectedModCount) {
        throw new ConcurrentModificationException();
      }
      if (this.lastReturned == Long.MIN_VALUE) {
        throw new IllegalStateException();
      }
      this.count -= 1;
      LongObjectHashMap.this.remove(this.lastReturned);
      this.lastReturned = Long.MIN_VALUE;
      this.expectedModCount = LongObjectHashMap.this.modCount;
    }
    
    public V next()
    {
      if (LongObjectHashMap.this.modCount != this.expectedModCount) {
        throw new ConcurrentModificationException();
      }
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      long[][] keys = LongObjectHashMap.this.keys;
      this.count += 1;
      if (this.prevKey != Long.MIN_VALUE) {
        this.innerIndex += 1;
      }
      for (; this.index < keys.length; this.index += 1) {
        if (keys[this.index] != null)
        {
          if (this.innerIndex < keys[this.index].length)
          {
            long key = keys[this.index][this.innerIndex];
            V value = LongObjectHashMap.this.values[this.index][this.innerIndex];
            if (key != Long.MIN_VALUE)
            {
              this.lastReturned = key;
              this.prevKey = key;
              this.prevValue = value;
              return (V)this.prevValue;
            }
          }
          this.innerIndex = 0;
        }
      }
      throw new NoSuchElementException();
    }
  }
  
  private class KeyIterator
    implements Iterator<Long>
  {
    final LongObjectHashMap<V>.ValueIterator iterator;
    
    public KeyIterator()
    {
      this.iterator = new LongObjectHashMap.ValueIterator(LongObjectHashMap.this);
    }
    
    public void remove()
    {
      this.iterator.remove();
    }
    
    public boolean hasNext()
    {
      return this.iterator.hasNext();
    }
    
    public Long next()
    {
      this.iterator.next();
      return Long.valueOf(this.iterator.prevKey);
    }
  }
  
  private class KeySet
    extends AbstractSet<Long>
  {
    private KeySet() {}
    
    public void clear()
    {
      LongObjectHashMap.this.clear();
    }
    
    public int size()
    {
      return LongObjectHashMap.this.size();
    }
    
    public boolean contains(Object key)
    {
      return ((key instanceof Long)) && (LongObjectHashMap.this.containsKey(((Long)key).longValue()));
    }
    
    public boolean remove(Object key)
    {
      return LongObjectHashMap.this.remove(((Long)key).longValue()) != null;
    }
    
    public Iterator<Long> iterator()
    {
      return new LongObjectHashMap.KeyIterator(LongObjectHashMap.this);
    }
  }
  
  private class ValueCollection
    extends AbstractCollection<V>
  {
    private ValueCollection() {}
    
    public void clear()
    {
      LongObjectHashMap.this.clear();
    }
    
    public int size()
    {
      return LongObjectHashMap.this.size();
    }
    
    public boolean contains(Object value)
    {
      return LongObjectHashMap.this.containsValue(value);
    }
    
    public Iterator<V> iterator()
    {
      return new LongObjectHashMap.ValueIterator(LongObjectHashMap.this);
    }
  }
  
  private class Entry
    implements Map.Entry<Long, V>
  {
    private final Long key;
    private V value;
    
    Entry(V arg3)
    {
      this.key = Long.valueOf(k);
      this.value = v;
    }
    
    public Long getKey()
    {
      return this.key;
    }
    
    public V getValue()
    {
      return (V)this.value;
    }
    
    public V setValue(V v)
    {
      V old = this.value;
      this.value = v;
      LongObjectHashMap.this.put(this.key.longValue(), v);
      return old;
    }
  }
}
