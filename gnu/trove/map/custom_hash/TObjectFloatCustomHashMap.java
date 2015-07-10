package gnu.trove.map.custom_hash;

import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.impl.Constants;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.TCustomObjectHash;
import gnu.trove.impl.hash.THash;
import gnu.trove.impl.hash.TObjectHash;
import gnu.trove.iterator.TFloatIterator;
import gnu.trove.iterator.TObjectFloatIterator;
import gnu.trove.iterator.hash.TObjectHashIterator;
import gnu.trove.map.TObjectFloatMap;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.procedure.TObjectFloatProcedure;
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

public class TObjectFloatCustomHashMap<K>
  extends TCustomObjectHash<K>
  implements TObjectFloatMap<K>, Externalizable
{
  static final long serialVersionUID = 1L;
  private final TObjectFloatProcedure<K> PUT_ALL_PROC = new TObjectFloatProcedure()
  {
    public boolean execute(K key, float value)
    {
      TObjectFloatCustomHashMap.this.put(key, value);
      return true;
    }
  };
  protected transient float[] _values;
  protected float no_entry_value;
  
  public TObjectFloatCustomHashMap() {}
  
  public TObjectFloatCustomHashMap(HashingStrategy<? super K> strategy)
  {
    super(strategy);
    this.no_entry_value = Constants.DEFAULT_FLOAT_NO_ENTRY_VALUE;
  }
  
  public TObjectFloatCustomHashMap(HashingStrategy<? super K> strategy, int initialCapacity)
  {
    super(strategy, initialCapacity);
    
    this.no_entry_value = Constants.DEFAULT_FLOAT_NO_ENTRY_VALUE;
  }
  
  public TObjectFloatCustomHashMap(HashingStrategy<? super K> strategy, int initialCapacity, float loadFactor)
  {
    super(strategy, initialCapacity, loadFactor);
    
    this.no_entry_value = Constants.DEFAULT_FLOAT_NO_ENTRY_VALUE;
  }
  
  public TObjectFloatCustomHashMap(HashingStrategy<? super K> strategy, int initialCapacity, float loadFactor, float noEntryValue)
  {
    super(strategy, initialCapacity, loadFactor);
    
    this.no_entry_value = noEntryValue;
    if (this.no_entry_value != 0.0F) {
      Arrays.fill(this._values, this.no_entry_value);
    }
  }
  
  public TObjectFloatCustomHashMap(HashingStrategy<? super K> strategy, TObjectFloatMap<? extends K> map)
  {
    this(strategy, map.size(), 0.5F, map.getNoEntryValue());
    if ((map instanceof TObjectFloatCustomHashMap))
    {
      TObjectFloatCustomHashMap hashmap = (TObjectFloatCustomHashMap)map;
      this._loadFactor = hashmap._loadFactor;
      this.no_entry_value = hashmap.no_entry_value;
      this.strategy = hashmap.strategy;
      if (this.no_entry_value != 0.0F) {
        Arrays.fill(this._values, this.no_entry_value);
      }
      setUp((int)Math.ceil(10.0F / this._loadFactor));
    }
    putAll(map);
  }
  
  public int setUp(int initialCapacity)
  {
    int capacity = super.setUp(initialCapacity);
    this._values = new float[capacity];
    return capacity;
  }
  
  protected void rehash(int newCapacity)
  {
    int oldCapacity = this._set.length;
    
    K[] oldKeys = (Object[])this._set;
    float[] oldVals = this._values;
    
    this._set = new Object[newCapacity];
    Arrays.fill(this._set, FREE);
    this._values = new float[newCapacity];
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
  
  public float getNoEntryValue()
  {
    return this.no_entry_value;
  }
  
  public boolean containsKey(Object key)
  {
    return contains(key);
  }
  
  public boolean containsValue(float val)
  {
    Object[] keys = this._set;
    float[] vals = this._values;
    for (int i = vals.length; i-- > 0;) {
      if ((keys[i] != FREE) && (keys[i] != REMOVED) && (val == vals[i])) {
        return true;
      }
    }
    return false;
  }
  
  public float get(Object key)
  {
    int index = index(key);
    return index < 0 ? this.no_entry_value : this._values[index];
  }
  
  public float put(K key, float value)
  {
    int index = insertKey(key);
    return doPut(value, index);
  }
  
  public float putIfAbsent(K key, float value)
  {
    int index = insertKey(key);
    if (index < 0) {
      return this._values[(-index - 1)];
    }
    return doPut(value, index);
  }
  
  private float doPut(float value, int index)
  {
    float previous = this.no_entry_value;
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
  
  public float remove(Object key)
  {
    float prev = this.no_entry_value;
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
  
  public void putAll(Map<? extends K, ? extends Float> map)
  {
    Set<? extends Map.Entry<? extends K, ? extends Float>> set = map.entrySet();
    for (Map.Entry<? extends K, ? extends Float> entry : set) {
      put(entry.getKey(), ((Float)entry.getValue()).floatValue());
    }
  }
  
  public void putAll(TObjectFloatMap<? extends K> map)
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
  
  public TFloatCollection valueCollection()
  {
    return new TFloatValueCollection();
  }
  
  public float[] values()
  {
    float[] vals = new float[size()];
    float[] v = this._values;
    Object[] keys = this._set;
    
    int i = v.length;
    for (int j = 0; i-- > 0;) {
      if ((keys[i] != FREE) && (keys[i] != REMOVED)) {
        vals[(j++)] = v[i];
      }
    }
    return vals;
  }
  
  public float[] values(float[] array)
  {
    int size = size();
    if (array.length < size) {
      array = new float[size];
    }
    float[] v = this._values;
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
  
  public TObjectFloatIterator<K> iterator()
  {
    return new TObjectFloatHashIterator(this);
  }
  
  public boolean increment(K key)
  {
    return adjustValue(key, 1.0F);
  }
  
  public boolean adjustValue(K key, float amount)
  {
    int index = index(key);
    if (index < 0) {
      return false;
    }
    this._values[index] += amount;
    return true;
  }
  
  public float adjustOrPutValue(K key, float adjust_amount, float put_amount)
  {
    int index = insertKey(key);
    boolean isNewMapping;
    float newValue;
    boolean isNewMapping;
    if (index < 0)
    {
      index = -index - 1;
      float newValue = this._values[index] += adjust_amount;
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
  
  public boolean forEachValue(TFloatProcedure procedure)
  {
    Object[] keys = this._set;
    float[] values = this._values;
    for (int i = values.length; i-- > 0;) {
      if ((keys[i] != FREE) && (keys[i] != REMOVED) && (!procedure.execute(values[i]))) {
        return false;
      }
    }
    return true;
  }
  
  public boolean forEachEntry(TObjectFloatProcedure<? super K> procedure)
  {
    Object[] keys = this._set;
    float[] values = this._values;
    for (int i = keys.length; i-- > 0;) {
      if ((keys[i] != FREE) && (keys[i] != REMOVED) && (!procedure.execute(keys[i], values[i]))) {
        return false;
      }
    }
    return true;
  }
  
  public boolean retainEntries(TObjectFloatProcedure<? super K> procedure)
  {
    boolean modified = false;
    
    K[] keys = (Object[])this._set;
    float[] values = this._values;
    
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
  
  public void transformValues(TFloatFunction function)
  {
    Object[] keys = this._set;
    float[] values = this._values;
    for (int i = values.length; i-- > 0;) {
      if ((keys[i] != null) && (keys[i] != REMOVED)) {
        values[i] = function.execute(values[i]);
      }
    }
  }
  
  public boolean equals(Object other)
  {
    if (!(other instanceof TObjectFloatMap)) {
      return false;
    }
    TObjectFloatMap that = (TObjectFloatMap)other;
    if (that.size() != size()) {
      return false;
    }
    try
    {
      TObjectFloatIterator iter = iterator();
      while (iter.hasNext())
      {
        iter.advance();
        Object key = iter.key();
        float value = iter.value();
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
    float[] values = this._values;
    for (int i = values.length; i-- > 0;) {
      if ((keys[i] != FREE) && (keys[i] != REMOVED)) {
        hashcode += (HashFunctions.hash(values[i]) ^ (keys[i] == null ? 0 : keys[i].hashCode()));
      }
    }
    return hashcode;
  }
  
  protected class KeyView
    extends TObjectFloatCustomHashMap<K>.MapBackedView<K>
  {
    protected KeyView()
    {
      super(null);
    }
    
    public Iterator<K> iterator()
    {
      return new TObjectHashIterator(TObjectFloatCustomHashMap.this);
    }
    
    public boolean removeElement(K key)
    {
      return TObjectFloatCustomHashMap.this.no_entry_value != TObjectFloatCustomHashMap.this.remove(key);
    }
    
    public boolean containsElement(K key)
    {
      return TObjectFloatCustomHashMap.this.contains(key);
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
      TObjectFloatCustomHashMap.this.clear();
    }
    
    public boolean add(E obj)
    {
      throw new UnsupportedOperationException();
    }
    
    public int size()
    {
      return TObjectFloatCustomHashMap.this.size();
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
      return TObjectFloatCustomHashMap.this.isEmpty();
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
  
  class TFloatValueCollection
    implements TFloatCollection
  {
    TFloatValueCollection() {}
    
    public TFloatIterator iterator()
    {
      return new TObjectFloatValueHashIterator();
    }
    
    public float getNoEntryValue()
    {
      return TObjectFloatCustomHashMap.this.no_entry_value;
    }
    
    public int size()
    {
      return TObjectFloatCustomHashMap.this._size;
    }
    
    public boolean isEmpty()
    {
      return 0 == TObjectFloatCustomHashMap.this._size;
    }
    
    public boolean contains(float entry)
    {
      return TObjectFloatCustomHashMap.this.containsValue(entry);
    }
    
    public float[] toArray()
    {
      return TObjectFloatCustomHashMap.this.values();
    }
    
    public float[] toArray(float[] dest)
    {
      return TObjectFloatCustomHashMap.this.values(dest);
    }
    
    public boolean add(float entry)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean remove(float entry)
    {
      float[] values = TObjectFloatCustomHashMap.this._values;
      Object[] set = TObjectFloatCustomHashMap.this._set;
      for (int i = values.length; i-- > 0;) {
        if ((set[i] != TObjectHash.FREE) && (set[i] != TObjectHash.REMOVED) && (entry == values[i]))
        {
          TObjectFloatCustomHashMap.this.removeAt(i);
          return true;
        }
      }
      return false;
    }
    
    public boolean containsAll(Collection<?> collection)
    {
      for (Object element : collection) {
        if ((element instanceof Float))
        {
          float ele = ((Float)element).floatValue();
          if (!TObjectFloatCustomHashMap.this.containsValue(ele)) {
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
    
    public boolean containsAll(TFloatCollection collection)
    {
      TFloatIterator iter = collection.iterator();
      while (iter.hasNext()) {
        if (!TObjectFloatCustomHashMap.this.containsValue(iter.next())) {
          return false;
        }
      }
      return true;
    }
    
    public boolean containsAll(float[] array)
    {
      for (float element : array) {
        if (!TObjectFloatCustomHashMap.this.containsValue(element)) {
          return false;
        }
      }
      return true;
    }
    
    public boolean addAll(Collection<? extends Float> collection)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean addAll(TFloatCollection collection)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean addAll(float[] array)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean retainAll(Collection<?> collection)
    {
      boolean modified = false;
      TFloatIterator iter = iterator();
      while (iter.hasNext()) {
        if (!collection.contains(Float.valueOf(iter.next())))
        {
          iter.remove();
          modified = true;
        }
      }
      return modified;
    }
    
    public boolean retainAll(TFloatCollection collection)
    {
      if (this == collection) {
        return false;
      }
      boolean modified = false;
      TFloatIterator iter = iterator();
      while (iter.hasNext()) {
        if (!collection.contains(iter.next()))
        {
          iter.remove();
          modified = true;
        }
      }
      return modified;
    }
    
    public boolean retainAll(float[] array)
    {
      boolean changed = false;
      Arrays.sort(array);
      float[] values = TObjectFloatCustomHashMap.this._values;
      
      Object[] set = TObjectFloatCustomHashMap.this._set;
      for (int i = set.length; i-- > 0;) {
        if ((set[i] != TObjectHash.FREE) && (set[i] != TObjectHash.REMOVED) && (Arrays.binarySearch(array, values[i]) < 0))
        {
          TObjectFloatCustomHashMap.this.removeAt(i);
          changed = true;
        }
      }
      return changed;
    }
    
    public boolean removeAll(Collection<?> collection)
    {
      boolean changed = false;
      for (Object element : collection) {
        if ((element instanceof Float))
        {
          float c = ((Float)element).floatValue();
          if (remove(c)) {
            changed = true;
          }
        }
      }
      return changed;
    }
    
    public boolean removeAll(TFloatCollection collection)
    {
      if (this == collection)
      {
        clear();
        return true;
      }
      boolean changed = false;
      TFloatIterator iter = collection.iterator();
      while (iter.hasNext())
      {
        float element = iter.next();
        if (remove(element)) {
          changed = true;
        }
      }
      return changed;
    }
    
    public boolean removeAll(float[] array)
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
      TObjectFloatCustomHashMap.this.clear();
    }
    
    public boolean forEach(TFloatProcedure procedure)
    {
      return TObjectFloatCustomHashMap.this.forEachValue(procedure);
    }
    
    public String toString()
    {
      final StringBuilder buf = new StringBuilder("{");
      TObjectFloatCustomHashMap.this.forEachValue(new TFloatProcedure()
      {
        private boolean first = true;
        
        public boolean execute(float value)
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
    
    class TObjectFloatValueHashIterator
      implements TFloatIterator
    {
      protected THash _hash = TObjectFloatCustomHashMap.this;
      protected int _expectedSize;
      protected int _index;
      
      TObjectFloatValueHashIterator()
      {
        this._expectedSize = this._hash.size();
        this._index = this._hash.capacity();
      }
      
      public boolean hasNext()
      {
        return nextIndex() >= 0;
      }
      
      public float next()
      {
        moveToNextIndex();
        return TObjectFloatCustomHashMap.this._values[this._index];
      }
      
      public void remove()
      {
        if (this._expectedSize != this._hash.size()) {
          throw new ConcurrentModificationException();
        }
        try
        {
          this._hash.tempDisableAutoCompaction();
          TObjectFloatCustomHashMap.this.removeAt(this._index);
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
        Object[] set = TObjectFloatCustomHashMap.this._set;
        int i = this._index;
        while ((i-- > 0) && ((set[i] == TCustomObjectHash.FREE) || (set[i] == TCustomObjectHash.REMOVED))) {}
        return i;
      }
    }
  }
  
  class TObjectFloatHashIterator<K>
    extends TObjectHashIterator<K>
    implements TObjectFloatIterator<K>
  {
    private final TObjectFloatCustomHashMap<K> _map;
    
    public TObjectFloatHashIterator()
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
    
    public float value()
    {
      return this._map._values[this._index];
    }
    
    public float setValue(float val)
    {
      float old = value();
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
    
    out.writeFloat(this.no_entry_value);
    
    out.writeInt(this._size);
    for (int i = this._set.length; i-- > 0;) {
      if ((this._set[i] != REMOVED) && (this._set[i] != FREE))
      {
        out.writeObject(this._set[i]);
        out.writeFloat(this._values[i]);
      }
    }
  }
  
  public void readExternal(ObjectInput in)
    throws IOException, ClassNotFoundException
  {
    in.readByte();
    
    super.readExternal(in);
    
    this.strategy = ((HashingStrategy)in.readObject());
    
    this.no_entry_value = in.readFloat();
    
    int size = in.readInt();
    setUp(size);
    while (size-- > 0)
    {
      K key = in.readObject();
      float val = in.readFloat();
      put(key, val);
    }
  }
  
  public String toString()
  {
    final StringBuilder buf = new StringBuilder("{");
    forEachEntry(new TObjectFloatProcedure()
    {
      private boolean first = true;
      
      public boolean execute(K key, float value)
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
