package gnu.trove.map.custom_hash;

import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.impl.Constants;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.TCustomObjectHash;
import gnu.trove.impl.hash.THash;
import gnu.trove.impl.hash.TObjectHash;
import gnu.trove.iterator.TLongIterator;
import gnu.trove.iterator.TObjectLongIterator;
import gnu.trove.iterator.hash.TObjectHashIterator;
import gnu.trove.map.TObjectLongMap;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.procedure.TObjectLongProcedure;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.strategy.HashingStrategy;
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
import java.util.NoSuchElementException;
import java.util.Set;

public class TObjectLongCustomHashMap<K>
  extends TCustomObjectHash<K>
  implements TObjectLongMap<K>, Externalizable
{
  static final long serialVersionUID = 1L;
  private final TObjectLongProcedure<K> PUT_ALL_PROC = new TObjectLongProcedure()
  {
    public boolean execute(K key, long value)
    {
      TObjectLongCustomHashMap.this.put(key, value);
      return true;
    }
  };
  protected transient long[] _values;
  protected long no_entry_value;
  
  public TObjectLongCustomHashMap() {}
  
  public TObjectLongCustomHashMap(HashingStrategy<? super K> strategy)
  {
    super(strategy);
    this.no_entry_value = Constants.DEFAULT_LONG_NO_ENTRY_VALUE;
  }
  
  public TObjectLongCustomHashMap(HashingStrategy<? super K> strategy, int initialCapacity)
  {
    super(strategy, initialCapacity);
    
    this.no_entry_value = Constants.DEFAULT_LONG_NO_ENTRY_VALUE;
  }
  
  public TObjectLongCustomHashMap(HashingStrategy<? super K> strategy, int initialCapacity, float loadFactor)
  {
    super(strategy, initialCapacity, loadFactor);
    
    this.no_entry_value = Constants.DEFAULT_LONG_NO_ENTRY_VALUE;
  }
  
  public TObjectLongCustomHashMap(HashingStrategy<? super K> strategy, int initialCapacity, float loadFactor, long noEntryValue)
  {
    super(strategy, initialCapacity, loadFactor);
    
    this.no_entry_value = noEntryValue;
    if (this.no_entry_value != 0L) {
      Arrays.fill(this._values, this.no_entry_value);
    }
  }
  
  public TObjectLongCustomHashMap(HashingStrategy<? super K> strategy, TObjectLongMap<? extends K> map)
  {
    this(strategy, map.size(), 0.5F, map.getNoEntryValue());
    if ((map instanceof TObjectLongCustomHashMap))
    {
      TObjectLongCustomHashMap hashmap = (TObjectLongCustomHashMap)map;
      this._loadFactor = hashmap._loadFactor;
      this.no_entry_value = hashmap.no_entry_value;
      this.strategy = hashmap.strategy;
      if (this.no_entry_value != 0L) {
        Arrays.fill(this._values, this.no_entry_value);
      }
      setUp((int)Math.ceil(10.0F / this._loadFactor));
    }
    putAll(map);
  }
  
  public int setUp(int initialCapacity)
  {
    int capacity = super.setUp(initialCapacity);
    this._values = new long[capacity];
    return capacity;
  }
  
  protected void rehash(int newCapacity)
  {
    int oldCapacity = this._set.length;
    
    K[] oldKeys = (Object[])this._set;
    long[] oldVals = this._values;
    
    this._set = new Object[newCapacity];
    Arrays.fill(this._set, FREE);
    this._values = new long[newCapacity];
    Arrays.fill(this._values, this.no_entry_value);
    for (int i = oldCapacity; i-- > 0;)
    {
      K o = oldKeys[i];
      if ((o != FREE) && (o != REMOVED))
      {
        int index = insertKey(o);
        if (index < 0) {
          throwObjectContractViolation(this._set[(-index - 1)], o);
        }
        this._values[index] = oldVals[i];
      }
    }
  }
  
  public long getNoEntryValue()
  {
    return this.no_entry_value;
  }
  
  public boolean containsKey(Object key)
  {
    return contains(key);
  }
  
  public boolean containsValue(long val)
  {
    Object[] keys = this._set;
    long[] vals = this._values;
    for (int i = vals.length; i-- > 0;) {
      if ((keys[i] != FREE) && (keys[i] != REMOVED) && (val == vals[i])) {
        return true;
      }
    }
    return false;
  }
  
  public long get(Object key)
  {
    int index = index(key);
    return index < 0 ? this.no_entry_value : this._values[index];
  }
  
  public long put(K key, long value)
  {
    int index = insertKey(key);
    return doPut(value, index);
  }
  
  public long putIfAbsent(K key, long value)
  {
    int index = insertKey(key);
    if (index < 0) {
      return this._values[(-index - 1)];
    }
    return doPut(value, index);
  }
  
  private long doPut(long value, int index)
  {
    long previous = this.no_entry_value;
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
  
  public long remove(Object key)
  {
    long prev = this.no_entry_value;
    int index = index(key);
    if (index >= 0)
    {
      prev = this._values[index];
      removeAt(index);
    }
    return prev;
  }
  
  protected void removeAt(int index)
  {
    this._values[index] = this.no_entry_value;
    super.removeAt(index);
  }
  
  public void putAll(Map<? extends K, ? extends Long> map)
  {
    Set<? extends Map.Entry<? extends K, ? extends Long>> set = map.entrySet();
    for (Map.Entry<? extends K, ? extends Long> entry : set) {
      put(entry.getKey(), ((Long)entry.getValue()).longValue());
    }
  }
  
  public void putAll(TObjectLongMap<? extends K> map)
  {
    map.forEachEntry(this.PUT_ALL_PROC);
  }
  
  public void clear()
  {
    super.clear();
    Arrays.fill(this._set, 0, this._set.length, FREE);
    Arrays.fill(this._values, 0, this._values.length, this.no_entry_value);
  }
  
  public Set<K> keySet()
  {
    return new KeyView();
  }
  
  public Object[] keys()
  {
    K[] keys = (Object[])new Object[size()];
    Object[] k = this._set;
    
    int i = k.length;
    for (int j = 0; i-- > 0;) {
      if ((k[i] != FREE) && (k[i] != REMOVED)) {
        keys[(j++)] = k[i];
      }
    }
    return keys;
  }
  
  public K[] keys(K[] a)
  {
    int size = size();
    if (a.length < size) {
      a = (Object[])Array.newInstance(a.getClass().getComponentType(), size);
    }
    Object[] k = this._set;
    
    int i = k.length;
    for (int j = 0; i-- > 0;) {
      if ((k[i] != FREE) && (k[i] != REMOVED)) {
        a[(j++)] = k[i];
      }
    }
    return a;
  }
  
  public TLongCollection valueCollection()
  {
    return new TLongValueCollection();
  }
  
  public long[] values()
  {
    long[] vals = new long[size()];
    long[] v = this._values;
    Object[] keys = this._set;
    
    int i = v.length;
    for (int j = 0; i-- > 0;) {
      if ((keys[i] != FREE) && (keys[i] != REMOVED)) {
        vals[(j++)] = v[i];
      }
    }
    return vals;
  }
  
  public long[] values(long[] array)
  {
    int size = size();
    if (array.length < size) {
      array = new long[size];
    }
    long[] v = this._values;
    Object[] keys = this._set;
    
    int i = v.length;
    for (int j = 0; i-- > 0;) {
      if ((keys[i] != FREE) && (keys[i] != REMOVED)) {
        array[(j++)] = v[i];
      }
    }
    if (array.length > size) {
      array[size] = this.no_entry_value;
    }
    return array;
  }
  
  public TObjectLongIterator<K> iterator()
  {
    return new TObjectLongHashIterator(this);
  }
  
  public boolean increment(K key)
  {
    return adjustValue(key, 1L);
  }
  
  public boolean adjustValue(K key, long amount)
  {
    int index = index(key);
    if (index < 0) {
      return false;
    }
    this._values[index] += amount;
    return true;
  }
  
  public long adjustOrPutValue(K key, long adjust_amount, long put_amount)
  {
    int index = insertKey(key);
    boolean isNewMapping;
    long newValue;
    boolean isNewMapping;
    if (index < 0)
    {
      index = -index - 1;
      long newValue = this._values[index] += adjust_amount;
      isNewMapping = false;
    }
    else
    {
      newValue = this._values[index] = put_amount;
      isNewMapping = true;
    }
    if (isNewMapping) {
      postInsertHook(this.consumeFreeSlot);
    }
    return newValue;
  }
  
  public boolean forEachKey(TObjectProcedure<? super K> procedure)
  {
    return forEach(procedure);
  }
  
  public boolean forEachValue(TLongProcedure procedure)
  {
    Object[] keys = this._set;
    long[] values = this._values;
    for (int i = values.length; i-- > 0;) {
      if ((keys[i] != FREE) && (keys[i] != REMOVED) && (!procedure.execute(values[i]))) {
        return false;
      }
    }
    return true;
  }
  
  public boolean forEachEntry(TObjectLongProcedure<? super K> procedure)
  {
    Object[] keys = this._set;
    long[] values = this._values;
    for (int i = keys.length; i-- > 0;) {
      if ((keys[i] != FREE) && (keys[i] != REMOVED) && (!procedure.execute(keys[i], values[i]))) {
        return false;
      }
    }
    return true;
  }
  
  public boolean retainEntries(TObjectLongProcedure<? super K> procedure)
  {
    boolean modified = false;
    
    K[] keys = (Object[])this._set;
    long[] values = this._values;
    
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
  
  public void transformValues(TLongFunction function)
  {
    Object[] keys = this._set;
    long[] values = this._values;
    for (int i = values.length; i-- > 0;) {
      if ((keys[i] != null) && (keys[i] != REMOVED)) {
        values[i] = function.execute(values[i]);
      }
    }
  }
  
  public boolean equals(Object other)
  {
    if (!(other instanceof TObjectLongMap)) {
      return false;
    }
    TObjectLongMap that = (TObjectLongMap)other;
    if (that.size() != size()) {
      return false;
    }
    try
    {
      TObjectLongIterator iter = iterator();
      while (iter.hasNext())
      {
        iter.advance();
        Object key = iter.key();
        long value = iter.value();
        if (value == this.no_entry_value)
        {
          if ((that.get(key) != that.getNoEntryValue()) || (!that.containsKey(key))) {
            return false;
          }
        }
        else if (value != that.get(key)) {
          return false;
        }
      }
    }
    catch (ClassCastException ex) {}
    return true;
  }
  
  public int hashCode()
  {
    int hashcode = 0;
    Object[] keys = this._set;
    long[] values = this._values;
    for (int i = values.length; i-- > 0;) {
      if ((keys[i] != FREE) && (keys[i] != REMOVED)) {
        hashcode += (HashFunctions.hash(values[i]) ^ (keys[i] == null ? 0 : keys[i].hashCode()));
      }
    }
    return hashcode;
  }
  
  protected class KeyView
    extends TObjectLongCustomHashMap<K>.MapBackedView<K>
  {
    protected KeyView()
    {
      super(null);
    }
    
    public Iterator<K> iterator()
    {
      return new TObjectHashIterator(TObjectLongCustomHashMap.this);
    }
    
    public boolean removeElement(K key)
    {
      return TObjectLongCustomHashMap.this.no_entry_value != TObjectLongCustomHashMap.this.remove(key);
    }
    
    public boolean containsElement(K key)
    {
      return TObjectLongCustomHashMap.this.contains(key);
    }
  }
  
  private abstract class MapBackedView<E>
    extends AbstractSet<E>
    implements Set<E>, Iterable<E>
  {
    private MapBackedView() {}
    
    public abstract boolean removeElement(E paramE);
    
    public abstract boolean containsElement(E paramE);
    
    public boolean contains(Object key)
    {
      return containsElement(key);
    }
    
    public boolean remove(Object o)
    {
      return removeElement(o);
    }
    
    public void clear()
    {
      TObjectLongCustomHashMap.this.clear();
    }
    
    public boolean add(E obj)
    {
      throw new UnsupportedOperationException();
    }
    
    public int size()
    {
      return TObjectLongCustomHashMap.this.size();
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
      return TObjectLongCustomHashMap.this.isEmpty();
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
  }
  
  class TLongValueCollection
    implements TLongCollection
  {
    TLongValueCollection() {}
    
    public TLongIterator iterator()
    {
      return new TObjectLongValueHashIterator();
    }
    
    public long getNoEntryValue()
    {
      return TObjectLongCustomHashMap.this.no_entry_value;
    }
    
    public int size()
    {
      return TObjectLongCustomHashMap.this._size;
    }
    
    public boolean isEmpty()
    {
      return 0 == TObjectLongCustomHashMap.this._size;
    }
    
    public boolean contains(long entry)
    {
      return TObjectLongCustomHashMap.this.containsValue(entry);
    }
    
    public long[] toArray()
    {
      return TObjectLongCustomHashMap.this.values();
    }
    
    public long[] toArray(long[] dest)
    {
      return TObjectLongCustomHashMap.this.values(dest);
    }
    
    public boolean add(long entry)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean remove(long entry)
    {
      long[] values = TObjectLongCustomHashMap.this._values;
      Object[] set = TObjectLongCustomHashMap.this._set;
      for (int i = values.length; i-- > 0;) {
        if ((set[i] != TObjectHash.FREE) && (set[i] != TObjectHash.REMOVED) && (entry == values[i]))
        {
          TObjectLongCustomHashMap.this.removeAt(i);
          return true;
        }
      }
      return false;
    }
    
    public boolean containsAll(Collection<?> collection)
    {
      for (Object element : collection) {
        if ((element instanceof Long))
        {
          long ele = ((Long)element).longValue();
          if (!TObjectLongCustomHashMap.this.containsValue(ele)) {
            return false;
          }
        }
        else
        {
          return false;
        }
      }
      return true;
    }
    
    public boolean containsAll(TLongCollection collection)
    {
      TLongIterator iter = collection.iterator();
      while (iter.hasNext()) {
        if (!TObjectLongCustomHashMap.this.containsValue(iter.next())) {
          return false;
        }
      }
      return true;
    }
    
    public boolean containsAll(long[] array)
    {
      for (long element : array) {
        if (!TObjectLongCustomHashMap.this.containsValue(element)) {
          return false;
        }
      }
      return true;
    }
    
    public boolean addAll(Collection<? extends Long> collection)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean addAll(TLongCollection collection)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean addAll(long[] array)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean retainAll(Collection<?> collection)
    {
      boolean modified = false;
      TLongIterator iter = iterator();
      while (iter.hasNext()) {
        if (!collection.contains(Long.valueOf(iter.next())))
        {
          iter.remove();
          modified = true;
        }
      }
      return modified;
    }
    
    public boolean retainAll(TLongCollection collection)
    {
      if (this == collection) {
        return false;
      }
      boolean modified = false;
      TLongIterator iter = iterator();
      while (iter.hasNext()) {
        if (!collection.contains(iter.next()))
        {
          iter.remove();
          modified = true;
        }
      }
      return modified;
    }
    
    public boolean retainAll(long[] array)
    {
      boolean changed = false;
      Arrays.sort(array);
      long[] values = TObjectLongCustomHashMap.this._values;
      
      Object[] set = TObjectLongCustomHashMap.this._set;
      for (int i = set.length; i-- > 0;) {
        if ((set[i] != TObjectHash.FREE) && (set[i] != TObjectHash.REMOVED) && (Arrays.binarySearch(array, values[i]) < 0))
        {
          TObjectLongCustomHashMap.this.removeAt(i);
          changed = true;
        }
      }
      return changed;
    }
    
    public boolean removeAll(Collection<?> collection)
    {
      boolean changed = false;
      for (Object element : collection) {
        if ((element instanceof Long))
        {
          long c = ((Long)element).longValue();
          if (remove(c)) {
            changed = true;
          }
        }
      }
      return changed;
    }
    
    public boolean removeAll(TLongCollection collection)
    {
      if (this == collection)
      {
        clear();
        return true;
      }
      boolean changed = false;
      TLongIterator iter = collection.iterator();
      while (iter.hasNext())
      {
        long element = iter.next();
        if (remove(element)) {
          changed = true;
        }
      }
      return changed;
    }
    
    public boolean removeAll(long[] array)
    {
      boolean changed = false;
      for (int i = array.length; i-- > 0;) {
        if (remove(array[i])) {
          changed = true;
        }
      }
      return changed;
    }
    
    public void clear()
    {
      TObjectLongCustomHashMap.this.clear();
    }
    
    public boolean forEach(TLongProcedure procedure)
    {
      return TObjectLongCustomHashMap.this.forEachValue(procedure);
    }
    
    public String toString()
    {
      final StringBuilder buf = new StringBuilder("{");
      TObjectLongCustomHashMap.this.forEachValue(new TLongProcedure()
      {
        private boolean first = true;
        
        public boolean execute(long value)
        {
          if (this.first) {
            this.first = false;
          } else {
            buf.append(", ");
          }
          buf.append(value);
          return true;
        }
      });
      buf.append("}");
      return buf.toString();
    }
    
    class TObjectLongValueHashIterator
      implements TLongIterator
    {
      protected THash _hash = TObjectLongCustomHashMap.this;
      protected int _expectedSize;
      protected int _index;
      
      TObjectLongValueHashIterator()
      {
        this._expectedSize = this._hash.size();
        this._index = this._hash.capacity();
      }
      
      public boolean hasNext()
      {
        return nextIndex() >= 0;
      }
      
      public long next()
      {
        moveToNextIndex();
        return TObjectLongCustomHashMap.this._values[this._index];
      }
      
      public void remove()
      {
        if (this._expectedSize != this._hash.size()) {
          throw new ConcurrentModificationException();
        }
        try
        {
          this._hash.tempDisableAutoCompaction();
          TObjectLongCustomHashMap.this.removeAt(this._index);
        }
        finally
        {
          this._hash.reenableAutoCompaction(false);
        }
        this._expectedSize -= 1;
      }
      
      protected final void moveToNextIndex()
      {
        if ((this._index = nextIndex()) < 0) {
          throw new NoSuchElementException();
        }
      }
      
      protected final int nextIndex()
      {
        if (this._expectedSize != this._hash.size()) {
          throw new ConcurrentModificationException();
        }
        Object[] set = TObjectLongCustomHashMap.this._set;
        int i = this._index;
        while ((i-- > 0) && ((set[i] == TCustomObjectHash.FREE) || (set[i] == TCustomObjectHash.REMOVED))) {}
        return i;
      }
    }
  }
  
  class TObjectLongHashIterator<K>
    extends TObjectHashIterator<K>
    implements TObjectLongIterator<K>
  {
    private final TObjectLongCustomHashMap<K> _map;
    
    public TObjectLongHashIterator()
    {
      super();
      this._map = map;
    }
    
    public void advance()
    {
      moveToNextIndex();
    }
    
    public K key()
    {
      return (K)this._map._set[this._index];
    }
    
    public long value()
    {
      return this._map._values[this._index];
    }
    
    public long setValue(long val)
    {
      long old = value();
      this._map._values[this._index] = val;
      return old;
    }
  }
  
  public void writeExternal(ObjectOutput out)
    throws IOException
  {
    out.writeByte(0);
    
    super.writeExternal(out);
    
    out.writeObject(this.strategy);
    
    out.writeLong(this.no_entry_value);
    
    out.writeInt(this._size);
    for (int i = this._set.length; i-- > 0;) {
      if ((this._set[i] != REMOVED) && (this._set[i] != FREE))
      {
        out.writeObject(this._set[i]);
        out.writeLong(this._values[i]);
      }
    }
  }
  
  public void readExternal(ObjectInput in)
    throws IOException, ClassNotFoundException
  {
    in.readByte();
    
    super.readExternal(in);
    
    this.strategy = ((HashingStrategy)in.readObject());
    
    this.no_entry_value = in.readLong();
    
    int size = in.readInt();
    setUp(size);
    while (size-- > 0)
    {
      K key = in.readObject();
      long val = in.readLong();
      put(key, val);
    }
  }
  
  public String toString()
  {
    final StringBuilder buf = new StringBuilder("{");
    forEachEntry(new TObjectLongProcedure()
    {
      private boolean first = true;
      
      public boolean execute(K key, long value)
      {
        if (this.first) {
          this.first = false;
        } else {
          buf.append(",");
        }
        buf.append(key).append("=").append(value);
        return true;
      }
    });
    buf.append("}");
    return buf.toString();
  }
}
