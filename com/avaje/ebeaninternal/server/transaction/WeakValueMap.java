package com.avaje.ebeaninternal.server.transaction;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class WeakValueMap<K, V>
{
  protected final ReferenceQueue<V> refQueue = new ReferenceQueue();
  private final Map<K, WeakReferenceWithKey<K, V>> backing;
  
  private static class WeakReferenceWithKey<K, V>
    extends WeakReference<V>
  {
    private final K key;
    
    public WeakReferenceWithKey(K key, V referent, ReferenceQueue<? super V> q)
    {
      super(q);
      this.key = key;
    }
    
    public K getKey()
    {
      return (K)this.key;
    }
  }
  
  public WeakValueMap()
  {
    this.backing = new HashMap();
  }
  
  private WeakReferenceWithKey<K, V> createReference(K key, V value)
  {
    return new WeakReferenceWithKey(key, value, this.refQueue);
  }
  
  private void expunge()
  {
    Reference ref;
    while ((ref = this.refQueue.poll()) != null) {
      this.backing.remove(((WeakReferenceWithKey)ref).getKey());
    }
  }
  
  public Object putIfAbsent(K key, V value)
  {
    expunge();
    
    Reference<V> ref = (Reference)this.backing.get(key);
    if (ref != null)
    {
      V existingValue = ref.get();
      if (existingValue != null) {
        return existingValue;
      }
    }
    this.backing.put(key, createReference(key, value));
    return null;
  }
  
  public void put(K key, V value)
  {
    expunge();
    
    this.backing.put(key, createReference(key, value));
  }
  
  public V get(K key)
  {
    expunge();
    
    Reference<V> v = (Reference)this.backing.get(key);
    return v == null ? null : v.get();
  }
  
  public int size()
  {
    expunge();
    
    return this.backing.size();
  }
  
  public boolean isEmpty()
  {
    expunge();
    
    return this.backing.isEmpty();
  }
  
  public boolean containsKey(Object key)
  {
    expunge();
    
    return this.backing.containsKey(key);
  }
  
  public V remove(K key)
  {
    expunge();
    
    Reference<V> v = (Reference)this.backing.remove(key);
    return v == null ? null : v.get();
  }
  
  public void clear()
  {
    expunge();
    this.backing.clear();
    expunge();
  }
  
  public String toString()
  {
    expunge();
    
    return this.backing.toString();
  }
}
