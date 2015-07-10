package gnu.trove.map.hash;

import gnu.trove.function.TObjectFunction;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.TObjectHash;
import gnu.trove.iterator.hash.TObjectHashIterator;
import gnu.trove.map.TMap;
import gnu.trove.procedure.TObjectObjectProcedure;
import gnu.trove.procedure.TObjectProcedure;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Array;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class THashMap<K, V>
  extends TObjectHash<K>
  implements TMap<K, V>, Externalizable
{
  static final long serialVersionUID = 1L;
  protected transient V[] _values;
  
  public THashMap() {}
  
  public THashMap(int initialCapacity)
  {
    super(initialCapacity);
  }
  
  public THashMap(int initialCapacity, float loadFactor)
  {
    super(initialCapacity, loadFactor);
  }
  
  public THashMap(Map<? extends K, ? extends V> map)
  {
    this(map.size());
    putAll(map);
  }
  
  public THashMap(THashMap<? extends K, ? extends V> map)
  {
    this(map.size());
    putAll(map);
  }
  
  public int setUp(int initialCapacity)
  {
    int capacity = super.setUp(initialCapacity);
    
    this._values = ((Object[])new Object[capacity]);
    return capacity;
  }
  
  public V put(K key, V value)
  {
    int index = insertKey(key);
    return (V)doPut(value, index);
  }
  
  public V putIfAbsent(K key, V value)
  {
    int index = insertKey(key);
    if (index < 0) {
      return (V)this._values[(-index - 1)];
    }
    return (V)doPut(value, index);
  }
  
  private V doPut(V value, int index)
  {
    V previous = null;
    boolean isNewMapping = true;
    if (index < 0)
    {
      index = -index - 1;
      previous = this._values[index];
      isNewMapping = false;
    }
    this._values[index] = value;
    if (isNewMapping) {
      postInsertHook(this.consumeFreeSlot);
    }
    return previous;
  }
  
  public boolean equals(Object other)
  {
    if (!(other instanceof Map)) {
      return false;
    }
    Map<K, V> that = (Map)other;
    if (that.size() != size()) {
      return false;
    }
    return forEachEntry(new EqProcedure(that));
  }
  
  public int hashCode()
  {
    THashMap<K, V>.HashProcedure p = new HashProcedure(null);
    forEachEntry(p);
    return p.getHashCode();
  }
  
  public String toString()
  {
    final StringBuilder buf = new StringBuilder("{");
    forEachEntry(new TObjectObjectProcedure()
    {
      private boolean first = true;
      
      public boolean execute(K key, V value)
      {
        if (this.first) {
          this.first = false;
        } else {
          buf.append(", ");
        }
        buf.append(key);
        buf.append("=");
        buf.append(value);
        return true;
      }
    });
    buf.append("}");
    return buf.toString();
  }
  
  private final class HashProcedure
    implements TObjectObjectProcedure<K, V>
  {
    private int h = 0;
    
    private HashProcedure() {}
    
    public int getHashCode()
    {
      return this.h;
    }
    
    public final boolean execute(K key, V value)
    {
      this.h += (HashFunctions.hash(key) ^ (value == null ? 0 : value.hashCode()));
      return true;
    }
  }
  
  private final class EqProcedure<K, V>
    implements TObjectObjectProcedure<K, V>
  {
    private final Map<K, V> _otherMap;
    
    EqProcedure()
    {
      this._otherMap = otherMap;
    }
    
    public final boolean execute(K key, V value)
    {
      if ((value == null) && (!this._otherMap.containsKey(key))) {
        return false;
      }
      V oValue = this._otherMap.get(key);
      return (oValue == value) || ((oValue != null) && (THashMap.this.equals(oValue, value)));
    }
  }
  
  public boolean forEachKey(TObjectProcedure<? super K> procedure)
  {
    return forEach(procedure);
  }
  
  public boolean forEachValue(TObjectProcedure<? super V> procedure)
  {
    V[] values = this._values;
    Object[] set = this._set;
    for (int i = values.length; i-- > 0;) {
      if ((set[i] != FREE) && (set[i] != REMOVED) && (!procedure.execute(values[i]))) {
        return false;
      }
    }
    return true;
  }
  
  public boolean forEachEntry(TObjectObjectProcedure<? super K, ? super V> procedure)
  {
    Object[] keys = this._set;
    V[] values = this._values;
    for (int i = keys.length; i-- > 0;) {
      if ((keys[i] != FREE) && (keys[i] != REMOVED) && (!procedure.execute(keys[i], values[i]))) {
        return false;
      }
    }
    return true;
  }
  
  public boolean retainEntries(TObjectObjectProcedure<? super K, ? super V> procedure)
  {
    boolean modified = false;
    Object[] keys = this._set;
    V[] values = this._values;
    
    tempDisableAutoCompaction();
    try
    {
      for (i = keys.length; i-- > 0;) {
        if ((keys[i] != FREE) && (keys[i] != REMOVED) && (!procedure.execute(keys[i], values[i])))
        {
          removeAt(i);
          modified = true;
        }
      }
    }
    finally
    {
      int i;
      reenableAutoCompaction(true);
    }
    return modified;
  }
  
  public void transformValues(TObjectFunction<V, V> function)
  {
    V[] values = this._values;
    Object[] set = this._set;
    for (int i = values.length; i-- > 0;) {
      if ((set[i] != FREE) && (set[i] != REMOVED)) {
        values[i] = function.execute(values[i]);
      }
    }
  }
  
  protected void rehash(int newCapacity)
  {
    int oldCapacity = this._set.length;
    int oldSize = size();
    Object[] oldKeys = this._set;
    V[] oldVals = this._values;
    
    this._set = new Object[newCapacity];
    Arrays.fill(this._set, FREE);
    this._values = ((Object[])new Object[newCapacity]);
    
    int count = 0;
    for (int i = oldCapacity; i-- > 0;)
    {
      Object o = oldKeys[i];
      if ((o != FREE) && (o != REMOVED))
      {
        int index = insertKey(o);
        if (index < 0) {
          throwObjectContractViolation(this._set[(-index - 1)], o, size(), oldSize, oldKeys);
        }
        this._values[index] = oldVals[i];
        
        count++;
      }
    }
    reportPotentialConcurrentMod(size(), oldSize);
  }
  
  public V get(Object key)
  {
    int index = index(key);
    return index < 0 ? null : this._values[index];
  }
  
  public void clear()
  {
    if (size() == 0) {
      return;
    }
    super.clear();
    
    Arrays.fill(this._set, 0, this._set.length, FREE);
    Arrays.fill(this._values, 0, this._values.length, null);
  }
  
  public V remove(Object key)
  {
    V prev = null;
    int index = index(key);
    if (index >= 0)
    {
      prev = this._values[index];
      removeAt(index);
    }
    return prev;
  }
  
  public void removeAt(int index)
  {
    this._values[index] = null;
    super.removeAt(index);
  }
  
  public Collection<V> values()
  {
    return new ValueView();
  }
  
  public Set<K> keySet()
  {
    return new KeyView();
  }
  
  public Set<Map.Entry<K, V>> entrySet()
  {
    return new EntryView();
  }
  
  public boolean containsValue(Object val)
  {
    Object[] set = this._set;
    V[] vals = this._values;
    int i;
    int i;
    if (null == val) {
      for (i = vals.length; i-- > 0;) {
        if ((set[i] != FREE) && (set[i] != REMOVED) && (val == vals[i])) {
          return true;
        }
      }
    } else {
      for (i = vals.length; i-- > 0;) {
        if ((set[i] != FREE) && (set[i] != REMOVED) && ((val == vals[i]) || (equals(val, vals[i])))) {
          return true;
        }
      }
    }
    return false;
  }
  
  public boolean containsKey(Object key)
  {
    return contains(key);
  }
  
  public void putAll(Map<? extends K, ? extends V> map)
  {
    ensureCapacity(map.size());
    for (Map.Entry<? extends K, ? extends V> e : map.entrySet()) {
      put(e.getKey(), e.getValue());
    }
  }
  
  protected class ValueView
    extends THashMap<K, V>.MapBackedView<V>
  {
    protected ValueView()
    {
      super(null);
    }
    
    public Iterator<V> iterator()
    {
      new TObjectHashIterator(THashMap.this)
      {
        protected V objectAtIndex(int index)
        {
          return (V)THashMap.this._values[index];
        }
      };
    }
    
    public boolean containsElement(V value)
    {
      return THashMap.this.containsValue(value);
    }
    
    public boolean removeElement(V value)
    {
      Object[] values = THashMap.this._values;
      Object[] set = THashMap.this._set;
      for (int i = values.length; i-- > 0;) {
        if (((set[i] != TObjectHash.FREE) && (set[i] != TObjectHash.REMOVED) && (value == values[i])) || ((null != values[i]) && (THashMap.this.equals(values[i], value))))
        {
          THashMap.this.removeAt(i);
          return true;
        }
      }
      return false;
    }
  }
  
  protected class EntryView
    extends THashMap<K, V>.MapBackedView<Map.Entry<K, V>>
  {
    protected EntryView()
    {
      super(null);
    }
    
    private final class EntryIterator
      extends TObjectHashIterator
    {
      EntryIterator()
      {
        super();
      }
      
      public THashMap<K, V>.Entry objectAtIndex(int index)
      {
        return new THashMap.Entry(THashMap.this, THashMap.this._set[index], THashMap.this._values[index], index);
      }
    }
    
    public Iterator<Map.Entry<K, V>> iterator()
    {
      return new EntryIterator(THashMap.this);
    }
    
    public boolean removeElement(Map.Entry<K, V> entry)
    {
      if (entry == null) {
        return false;
      }
      K key = keyForEntry(entry);
      int index = THashMap.this.index(key);
      if (index >= 0)
      {
        V val = valueForEntry(entry);
        if ((val == THashMap.this._values[index]) || ((null != val) && (THashMap.this.equals(val, THashMap.this._values[index]))))
        {
          THashMap.this.removeAt(index);
          return true;
        }
      }
      return false;
    }
    
    public boolean containsElement(Map.Entry<K, V> entry)
    {
      V val = THashMap.this.get(keyForEntry(entry));
      V entryValue = entry.getValue();
      return (entryValue == val) || ((null != val) && (THashMap.this.equals(val, entryValue)));
    }
    
    protected V valueForEntry(Map.Entry<K, V> entry)
    {
      return (V)entry.getValue();
    }
    
    protected K keyForEntry(Map.Entry<K, V> entry)
    {
      return (K)entry.getKey();
    }
  }
  
  private abstract class MapBackedView<E>
    extends AbstractSet<E>
    implements Set<E>, Iterable<E>
  {
    private MapBackedView() {}
    
    public abstract Iterator<E> iterator();
    
    public abstract boolean removeElement(E paramE);
    
    public abstract boolean containsElement(E paramE);
    
    public boolean contains(Object key)
    {
      return containsElement(key);
    }
    
    public boolean remove(Object o)
    {
      try
      {
        return removeElement(o);
      }
      catch (ClassCastException ex) {}
      return false;
    }
    
    public void clear()
    {
      THashMap.this.clear();
    }
    
    public boolean add(E obj)
    {
      throw new UnsupportedOperationException();
    }
    
    public int size()
    {
      return THashMap.this.size();
    }
    
    public Object[] toArray()
    {
      Object[] result = new Object[size()];
      Iterator<E> e = iterator();
      for (int i = 0; e.hasNext(); i++) {
        result[i] = e.next();
      }
      return result;
    }
    
    public <T> T[] toArray(T[] a)
    {
      int size = size();
      if (a.length < size) {
        a = (Object[])Array.newInstance(a.getClass().getComponentType(), size);
      }
      Iterator<E> it = iterator();
      Object[] result = a;
      for (int i = 0; i < size; i++) {
        result[i] = it.next();
      }
      if (a.length > size) {
        a[size] = null;
      }
      return a;
    }
    
    public boolean isEmpty()
    {
      return THashMap.this.isEmpty();
    }
    
    public boolean addAll(Collection<? extends E> collection)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean retainAll(Collection<?> collection)
    {
      boolean changed = false;
      Iterator<E> i = iterator();
      while (i.hasNext()) {
        if (!collection.contains(i.next()))
        {
          i.remove();
          changed = true;
        }
      }
      return changed;
    }
    
    public String toString()
    {
      Iterator<E> i = iterator();
      if (!i.hasNext()) {
        return "{}";
      }
      StringBuilder sb = new StringBuilder();
      sb.append('{');
      for (;;)
      {
        E e = i.next();
        sb.append(e == this ? "(this Collection)" : e);
        if (!i.hasNext()) {
          return '}';
        }
        sb.append(", ");
      }
    }
  }
  
  protected class KeyView
    extends THashMap<K, V>.MapBackedView<K>
  {
    protected KeyView()
    {
      super(null);
    }
    
    public Iterator<K> iterator()
    {
      return new TObjectHashIterator(THashMap.this);
    }
    
    public boolean removeElement(K key)
    {
      return null != THashMap.this.remove(key);
    }
    
    public boolean containsElement(K key)
    {
      return THashMap.this.contains(key);
    }
  }
  
  final class Entry
    implements Map.Entry<K, V>
  {
    private K key;
    private V val;
    private final int index;
    
    Entry(V key, int value)
    {
      this.key = key;
      this.val = value;
      this.index = index;
    }
    
    public K getKey()
    {
      return (K)this.key;
    }
    
    public V getValue()
    {
      return (V)this.val;
    }
    
    public V setValue(V o)
    {
      if (THashMap.this._values[this.index] != this.val) {
        throw new ConcurrentModificationException();
      }
      V retval = this.val;
      
      THashMap.this._values[this.index] = o;
      this.val = o;
      return retval;
    }
    
    public boolean equals(Object o)
    {
      if ((o instanceof Map.Entry))
      {
        Map.Entry<K, V> e1 = this;
        Map.Entry e2 = (Map.Entry)o;
        return (THashMap.this.equals(e1.getKey(), e2.getKey())) && (THashMap.this.equals(e1.getValue(), e1.getValue()));
      }
      return false;
    }
    
    public int hashCode()
    {
      return (getKey() == null ? 0 : getKey().hashCode()) ^ (getValue() == null ? 0 : getValue().hashCode());
    }
    
    public String toString()
    {
      return this.key + "=" + this.val;
    }
  }
  
  public void writeExternal(ObjectOutput out)
    throws IOException
  {
    out.writeByte(1);
    
    super.writeExternal(out);
    
    out.writeInt(this._size);
    for (int i = this._set.length; i-- > 0;) {
      if ((this._set[i] != REMOVED) && (this._set[i] != FREE))
      {
        out.writeObject(this._set[i]);
        out.writeObject(this._values[i]);
      }
    }
  }
  
  public void readExternal(ObjectInput in)
    throws IOException, ClassNotFoundException
  {
    byte version = in.readByte();
    if (version != 0) {
      super.readExternal(in);
    }
    int size = in.readInt();
    setUp(size);
    while (size-- > 0)
    {
      K key = in.readObject();
      
      V val = in.readObject();
      put(key, val);
    }
  }
}
