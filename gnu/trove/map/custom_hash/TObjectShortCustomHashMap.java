package gnu.trove.map.custom_hash;

import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.impl.Constants;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.TCustomObjectHash;
import gnu.trove.impl.hash.THash;
import gnu.trove.impl.hash.TObjectHash;
import gnu.trove.iterator.TObjectShortIterator;
import gnu.trove.iterator.TShortIterator;
import gnu.trove.iterator.hash.TObjectHashIterator;
import gnu.trove.map.TObjectShortMap;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.procedure.TObjectShortProcedure;
import gnu.trove.procedure.TShortProcedure;
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

public class TObjectShortCustomHashMap<K>
  extends TCustomObjectHash<K>
  implements TObjectShortMap<K>, Externalizable
{
  static final long serialVersionUID = 1L;
  private final TObjectShortProcedure<K> PUT_ALL_PROC = new TObjectShortProcedure()
  {
    public boolean execute(K key, short value)
    {
      TObjectShortCustomHashMap.this.put(key, value);
      return true;
    }
  };
  protected transient short[] _values;
  protected short no_entry_value;
  
  public TObjectShortCustomHashMap() {}
  
  public TObjectShortCustomHashMap(HashingStrategy<? super K> strategy)
  {
    super(strategy);
    this.no_entry_value = Constants.DEFAULT_SHORT_NO_ENTRY_VALUE;
  }
  
  public TObjectShortCustomHashMap(HashingStrategy<? super K> strategy, int initialCapacity)
  {
    super(strategy, initialCapacity);
    
    this.no_entry_value = Constants.DEFAULT_SHORT_NO_ENTRY_VALUE;
  }
  
  public TObjectShortCustomHashMap(HashingStrategy<? super K> strategy, int initialCapacity, float loadFactor)
  {
    super(strategy, initialCapacity, loadFactor);
    
    this.no_entry_value = Constants.DEFAULT_SHORT_NO_ENTRY_VALUE;
  }
  
  public TObjectShortCustomHashMap(HashingStrategy<? super K> strategy, int initialCapacity, float loadFactor, short noEntryValue)
  {
    super(strategy, initialCapacity, loadFactor);
    
    this.no_entry_value = noEntryValue;
    if (this.no_entry_value != 0) {
      Arrays.fill(this._values, this.no_entry_value);
    }
  }
  
  public TObjectShortCustomHashMap(HashingStrategy<? super K> strategy, TObjectShortMap<? extends K> map)
  {
    this(strategy, map.size(), 0.5F, map.getNoEntryValue());
    if ((map instanceof TObjectShortCustomHashMap))
    {
      TObjectShortCustomHashMap hashmap = (TObjectShortCustomHashMap)map;
      this._loadFactor = hashmap._loadFactor;
      this.no_entry_value = hashmap.no_entry_value;
      this.strategy = hashmap.strategy;
      if (this.no_entry_value != 0) {
        Arrays.fill(this._values, this.no_entry_value);
      }
      setUp((int)Math.ceil(10.0F / this._loadFactor));
    }
    putAll(map);
  }
  
  public int setUp(int initialCapacity)
  {
    int capacity = super.setUp(initialCapacity);
    this._values = new short[capacity];
    return capacity;
  }
  
  protected void rehash(int newCapacity)
  {
    int oldCapacity = this._set.length;
    
    K[] oldKeys = (Object[])this._set;
    short[] oldVals = this._values;
    
    this._set = new Object[newCapacity];
    Arrays.fill(this._set, FREE);
    this._values = new short[newCapacity];
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
  
  public short getNoEntryValue()
  {
    return this.no_entry_value;
  }
  
  public boolean containsKey(Object key)
  {
    return contains(key);
  }
  
  public boolean containsValue(short val)
  {
    Object[] keys = this._set;
    short[] vals = this._values;
    for (int i = vals.length; i-- > 0;) {
      if ((keys[i] != FREE) && (keys[i] != REMOVED) && (val == vals[i])) {
        return true;
      }
    }
    return false;
  }
  
  public short get(Object key)
  {
    int index = index(key);
    return index < 0 ? this.no_entry_value : this._values[index];
  }
  
  public short put(K key, short value)
  {
    int index = insertKey(key);
    return doPut(value, index);
  }
  
  public short putIfAbsent(K key, short value)
  {
    int index = insertKey(key);
    if (index < 0) {
      return this._values[(-index - 1)];
    }
    return doPut(value, index);
  }
  
  private short doPut(short value, int index)
  {
    short previous = this.no_entry_value;
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
  
  public short remove(Object key)
  {
    short prev = this.no_entry_value;
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
  
  public void putAll(Map<? extends K, ? extends Short> map)
  {
    Set<? extends Map.Entry<? extends K, ? extends Short>> set = map.entrySet();
    for (Map.Entry<? extends K, ? extends Short> entry : set) {
      put(entry.getKey(), ((Short)entry.getValue()).shortValue());
    }
  }
  
  public void putAll(TObjectShortMap<? extends K> map)
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
  
  public TShortCollection valueCollection()
  {
    return new TShortValueCollection();
  }
  
  public short[] values()
  {
    short[] vals = new short[size()];
    short[] v = this._values;
    Object[] keys = this._set;
    
    int i = v.length;
    for (int j = 0; i-- > 0;) {
      if ((keys[i] != FREE) && (keys[i] != REMOVED)) {
        vals[(j++)] = v[i];
      }
    }
    return vals;
  }
  
  public short[] values(short[] array)
  {
    int size = size();
    if (array.length < size) {
      array = new short[size];
    }
    short[] v = this._values;
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
  
  public TObjectShortIterator<K> iterator()
  {
    return new TObjectShortHashIterator(this);
  }
  
  public boolean increment(K key)
  {
    return adjustValue(key, (short)1);
  }
  
  public boolean adjustValue(K key, short amount)
  {
    int index = index(key);
    if (index < 0) {
      return false;
    }
    int tmp17_16 = index; short[] tmp17_13 = this._values;tmp17_13[tmp17_16] = ((short)(tmp17_13[tmp17_16] + amount));
    return true;
  }
  
  public short adjustOrPutValue(K key, short adjust_amount, short put_amount)
  {
    int index = insertKey(key);
    boolean isNewMapping;
    short newValue;
    boolean isNewMapping;
    if (index < 0)
    {
      index = -index - 1; int 
        tmp25_23 = index; short[] tmp25_20 = this._values;short newValue = tmp25_20[tmp25_23] = (short)(tmp25_20[tmp25_23] + adjust_amount);
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
  
  public boolean forEachValue(TShortProcedure procedure)
  {
    Object[] keys = this._set;
    short[] values = this._values;
    for (int i = values.length; i-- > 0;) {
      if ((keys[i] != FREE) && (keys[i] != REMOVED) && (!procedure.execute(values[i]))) {
        return false;
      }
    }
    return true;
  }
  
  public boolean forEachEntry(TObjectShortProcedure<? super K> procedure)
  {
    Object[] keys = this._set;
    short[] values = this._values;
    for (int i = keys.length; i-- > 0;) {
      if ((keys[i] != FREE) && (keys[i] != REMOVED) && (!procedure.execute(keys[i], values[i]))) {
        return false;
      }
    }
    return true;
  }
  
  public boolean retainEntries(TObjectShortProcedure<? super K> procedure)
  {
    boolean modified = false;
    
    K[] keys = (Object[])this._set;
    short[] values = this._values;
    
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
  
  public void transformValues(TShortFunction function)
  {
    Object[] keys = this._set;
    short[] values = this._values;
    for (int i = values.length; i-- > 0;) {
      if ((keys[i] != null) && (keys[i] != REMOVED)) {
        values[i] = function.execute(values[i]);
      }
    }
  }
  
  public boolean equals(Object other)
  {
    if (!(other instanceof TObjectShortMap)) {
      return false;
    }
    TObjectShortMap that = (TObjectShortMap)other;
    if (that.size() != size()) {
      return false;
    }
    try
    {
      TObjectShortIterator iter = iterator();
      while (iter.hasNext())
      {
        iter.advance();
        Object key = iter.key();
        short value = iter.value();
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
    short[] values = this._values;
    for (int i = values.length; i-- > 0;) {
      if ((keys[i] != FREE) && (keys[i] != REMOVED)) {
        hashcode += (HashFunctions.hash(values[i]) ^ (keys[i] == null ? 0 : keys[i].hashCode()));
      }
    }
    return hashcode;
  }
  
  protected class KeyView
    extends TObjectShortCustomHashMap<K>.MapBackedView<K>
  {
    protected KeyView()
    {
      super(null);
    }
    
    public Iterator<K> iterator()
    {
      return new TObjectHashIterator(TObjectShortCustomHashMap.this);
    }
    
    public boolean removeElement(K key)
    {
      return TObjectShortCustomHashMap.this.no_entry_value != TObjectShortCustomHashMap.this.remove(key);
    }
    
    public boolean containsElement(K key)
    {
      return TObjectShortCustomHashMap.this.contains(key);
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
      TObjectShortCustomHashMap.this.clear();
    }
    
    public boolean add(E obj)
    {
      throw new UnsupportedOperationException();
    }
    
    public int size()
    {
      return TObjectShortCustomHashMap.this.size();
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
      return TObjectShortCustomHashMap.this.isEmpty();
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
  
  class TShortValueCollection
    implements TShortCollection
  {
    TShortValueCollection() {}
    
    public TShortIterator iterator()
    {
      return new TObjectShortValueHashIterator();
    }
    
    public short getNoEntryValue()
    {
      return TObjectShortCustomHashMap.this.no_entry_value;
    }
    
    public int size()
    {
      return TObjectShortCustomHashMap.this._size;
    }
    
    public boolean isEmpty()
    {
      return 0 == TObjectShortCustomHashMap.this._size;
    }
    
    public boolean contains(short entry)
    {
      return TObjectShortCustomHashMap.this.containsValue(entry);
    }
    
    public short[] toArray()
    {
      return TObjectShortCustomHashMap.this.values();
    }
    
    public short[] toArray(short[] dest)
    {
      return TObjectShortCustomHashMap.this.values(dest);
    }
    
    public boolean add(short entry)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean remove(short entry)
    {
      short[] values = TObjectShortCustomHashMap.this._values;
      Object[] set = TObjectShortCustomHashMap.this._set;
      for (int i = values.length; i-- > 0;) {
        if ((set[i] != TObjectHash.FREE) && (set[i] != TObjectHash.REMOVED) && (entry == values[i]))
        {
          TObjectShortCustomHashMap.this.removeAt(i);
          return true;
        }
      }
      return false;
    }
    
    public boolean containsAll(Collection<?> collection)
    {
      for (Object element : collection) {
        if ((element instanceof Short))
        {
          short ele = ((Short)element).shortValue();
          if (!TObjectShortCustomHashMap.this.containsValue(ele)) {
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
    
    public boolean containsAll(TShortCollection collection)
    {
      TShortIterator iter = collection.iterator();
      while (iter.hasNext()) {
        if (!TObjectShortCustomHashMap.this.containsValue(iter.next())) {
          return false;
        }
      }
      return true;
    }
    
    public boolean containsAll(short[] array)
    {
      for (short element : array) {
        if (!TObjectShortCustomHashMap.this.containsValue(element)) {
          return false;
        }
      }
      return true;
    }
    
    public boolean addAll(Collection<? extends Short> collection)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean addAll(TShortCollection collection)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean addAll(short[] array)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean retainAll(Collection<?> collection)
    {
      boolean modified = false;
      TShortIterator iter = iterator();
      while (iter.hasNext()) {
        if (!collection.contains(Short.valueOf(iter.next())))
        {
          iter.remove();
          modified = true;
        }
      }
      return modified;
    }
    
    public boolean retainAll(TShortCollection collection)
    {
      if (this == collection) {
        return false;
      }
      boolean modified = false;
      TShortIterator iter = iterator();
      while (iter.hasNext()) {
        if (!collection.contains(iter.next()))
        {
          iter.remove();
          modified = true;
        }
      }
      return modified;
    }
    
    public boolean retainAll(short[] array)
    {
      boolean changed = false;
      Arrays.sort(array);
      short[] values = TObjectShortCustomHashMap.this._values;
      
      Object[] set = TObjectShortCustomHashMap.this._set;
      for (int i = set.length; i-- > 0;) {
        if ((set[i] != TObjectHash.FREE) && (set[i] != TObjectHash.REMOVED) && (Arrays.binarySearch(array, values[i]) < 0))
        {
          TObjectShortCustomHashMap.this.removeAt(i);
          changed = true;
        }
      }
      return changed;
    }
    
    public boolean removeAll(Collection<?> collection)
    {
      boolean changed = false;
      for (Object element : collection) {
        if ((element instanceof Short))
        {
          short c = ((Short)element).shortValue();
          if (remove(c)) {
            changed = true;
          }
        }
      }
      return changed;
    }
    
    public boolean removeAll(TShortCollection collection)
    {
      if (this == collection)
      {
        clear();
        return true;
      }
      boolean changed = false;
      TShortIterator iter = collection.iterator();
      while (iter.hasNext())
      {
        short element = iter.next();
        if (remove(element)) {
          changed = true;
        }
      }
      return changed;
    }
    
    public boolean removeAll(short[] array)
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
      TObjectShortCustomHashMap.this.clear();
    }
    
    public boolean forEach(TShortProcedure procedure)
    {
      return TObjectShortCustomHashMap.this.forEachValue(procedure);
    }
    
    public String toString()
    {
      final StringBuilder buf = new StringBuilder("{");
      TObjectShortCustomHashMap.this.forEachValue(new TShortProcedure()
      {
        private boolean first = true;
        
        public boolean execute(short value)
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
    
    class TObjectShortValueHashIterator
      implements TShortIterator
    {
      protected THash _hash = TObjectShortCustomHashMap.this;
      protected int _expectedSize;
      protected int _index;
      
      TObjectShortValueHashIterator()
      {
        this._expectedSize = this._hash.size();
        this._index = this._hash.capacity();
      }
      
      public boolean hasNext()
      {
        return nextIndex() >= 0;
      }
      
      public short next()
      {
        moveToNextIndex();
        return TObjectShortCustomHashMap.this._values[this._index];
      }
      
      public void remove()
      {
        if (this._expectedSize != this._hash.size()) {
          throw new ConcurrentModificationException();
        }
        try
        {
          this._hash.tempDisableAutoCompaction();
          TObjectShortCustomHashMap.this.removeAt(this._index);
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
        Object[] set = TObjectShortCustomHashMap.this._set;
        int i = this._index;
        while ((i-- > 0) && ((set[i] == TCustomObjectHash.FREE) || (set[i] == TCustomObjectHash.REMOVED))) {}
        return i;
      }
    }
  }
  
  class TObjectShortHashIterator<K>
    extends TObjectHashIterator<K>
    implements TObjectShortIterator<K>
  {
    private final TObjectShortCustomHashMap<K> _map;
    
    public TObjectShortHashIterator()
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
    
    public short value()
    {
      return this._map._values[this._index];
    }
    
    public short setValue(short val)
    {
      short old = value();
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
    
    out.writeShort(this.no_entry_value);
    
    out.writeInt(this._size);
    for (int i = this._set.length; i-- > 0;) {
      if ((this._set[i] != REMOVED) && (this._set[i] != FREE))
      {
        out.writeObject(this._set[i]);
        out.writeShort(this._values[i]);
      }
    }
  }
  
  public void readExternal(ObjectInput in)
    throws IOException, ClassNotFoundException
  {
    in.readByte();
    
    super.readExternal(in);
    
    this.strategy = ((HashingStrategy)in.readObject());
    
    this.no_entry_value = in.readShort();
    
    int size = in.readInt();
    setUp(size);
    while (size-- > 0)
    {
      K key = in.readObject();
      short val = in.readShort();
      put(key, val);
    }
  }
  
  public String toString()
  {
    final StringBuilder buf = new StringBuilder("{");
    forEachEntry(new TObjectShortProcedure()
    {
      private boolean first = true;
      
      public boolean execute(K key, short value)
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
