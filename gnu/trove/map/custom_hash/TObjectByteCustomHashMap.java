package gnu.trove.map.custom_hash;

import gnu.trove.TByteCollection;
import gnu.trove.function.TByteFunction;
import gnu.trove.impl.Constants;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.TCustomObjectHash;
import gnu.trove.impl.hash.THash;
import gnu.trove.impl.hash.TObjectHash;
import gnu.trove.iterator.TByteIterator;
import gnu.trove.iterator.TObjectByteIterator;
import gnu.trove.iterator.hash.TObjectHashIterator;
import gnu.trove.map.TObjectByteMap;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TObjectByteProcedure;
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

public class TObjectByteCustomHashMap<K>
  extends TCustomObjectHash<K>
  implements TObjectByteMap<K>, Externalizable
{
  static final long serialVersionUID = 1L;
  private final TObjectByteProcedure<K> PUT_ALL_PROC = new TObjectByteProcedure()
  {
    public boolean execute(K key, byte value)
    {
      TObjectByteCustomHashMap.this.put(key, value);
      return true;
    }
  };
  protected transient byte[] _values;
  protected byte no_entry_value;
  
  public TObjectByteCustomHashMap() {}
  
  public TObjectByteCustomHashMap(HashingStrategy<? super K> strategy)
  {
    super(strategy);
    this.no_entry_value = Constants.DEFAULT_BYTE_NO_ENTRY_VALUE;
  }
  
  public TObjectByteCustomHashMap(HashingStrategy<? super K> strategy, int initialCapacity)
  {
    super(strategy, initialCapacity);
    
    this.no_entry_value = Constants.DEFAULT_BYTE_NO_ENTRY_VALUE;
  }
  
  public TObjectByteCustomHashMap(HashingStrategy<? super K> strategy, int initialCapacity, float loadFactor)
  {
    super(strategy, initialCapacity, loadFactor);
    
    this.no_entry_value = Constants.DEFAULT_BYTE_NO_ENTRY_VALUE;
  }
  
  public TObjectByteCustomHashMap(HashingStrategy<? super K> strategy, int initialCapacity, float loadFactor, byte noEntryValue)
  {
    super(strategy, initialCapacity, loadFactor);
    
    this.no_entry_value = noEntryValue;
    if (this.no_entry_value != 0) {
      Arrays.fill(this._values, this.no_entry_value);
    }
  }
  
  public TObjectByteCustomHashMap(HashingStrategy<? super K> strategy, TObjectByteMap<? extends K> map)
  {
    this(strategy, map.size(), 0.5F, map.getNoEntryValue());
    if ((map instanceof TObjectByteCustomHashMap))
    {
      TObjectByteCustomHashMap hashmap = (TObjectByteCustomHashMap)map;
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
    this._values = new byte[capacity];
    return capacity;
  }
  
  protected void rehash(int newCapacity)
  {
    int oldCapacity = this._set.length;
    
    K[] oldKeys = (Object[])this._set;
    byte[] oldVals = this._values;
    
    this._set = new Object[newCapacity];
    Arrays.fill(this._set, FREE);
    this._values = new byte[newCapacity];
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
  
  public byte getNoEntryValue()
  {
    return this.no_entry_value;
  }
  
  public boolean containsKey(Object key)
  {
    return contains(key);
  }
  
  public boolean containsValue(byte val)
  {
    Object[] keys = this._set;
    byte[] vals = this._values;
    for (int i = vals.length; i-- > 0;) {
      if ((keys[i] != FREE) && (keys[i] != REMOVED) && (val == vals[i])) {
        return true;
      }
    }
    return false;
  }
  
  public byte get(Object key)
  {
    int index = index(key);
    return index < 0 ? this.no_entry_value : this._values[index];
  }
  
  public byte put(K key, byte value)
  {
    int index = insertKey(key);
    return doPut(value, index);
  }
  
  public byte putIfAbsent(K key, byte value)
  {
    int index = insertKey(key);
    if (index < 0) {
      return this._values[(-index - 1)];
    }
    return doPut(value, index);
  }
  
  private byte doPut(byte value, int index)
  {
    byte previous = this.no_entry_value;
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
  
  public byte remove(Object key)
  {
    byte prev = this.no_entry_value;
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
  
  public void putAll(Map<? extends K, ? extends Byte> map)
  {
    Set<? extends Map.Entry<? extends K, ? extends Byte>> set = map.entrySet();
    for (Map.Entry<? extends K, ? extends Byte> entry : set) {
      put(entry.getKey(), ((Byte)entry.getValue()).byteValue());
    }
  }
  
  public void putAll(TObjectByteMap<? extends K> map)
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
  
  public TByteCollection valueCollection()
  {
    return new TByteValueCollection();
  }
  
  public byte[] values()
  {
    byte[] vals = new byte[size()];
    byte[] v = this._values;
    Object[] keys = this._set;
    
    int i = v.length;
    for (int j = 0; i-- > 0;) {
      if ((keys[i] != FREE) && (keys[i] != REMOVED)) {
        vals[(j++)] = v[i];
      }
    }
    return vals;
  }
  
  public byte[] values(byte[] array)
  {
    int size = size();
    if (array.length < size) {
      array = new byte[size];
    }
    byte[] v = this._values;
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
  
  public TObjectByteIterator<K> iterator()
  {
    return new TObjectByteHashIterator(this);
  }
  
  public boolean increment(K key)
  {
    return adjustValue(key, (byte)1);
  }
  
  public boolean adjustValue(K key, byte amount)
  {
    int index = index(key);
    if (index < 0) {
      return false;
    }
    int tmp17_16 = index; byte[] tmp17_13 = this._values;tmp17_13[tmp17_16] = ((byte)(tmp17_13[tmp17_16] + amount));
    return true;
  }
  
  public byte adjustOrPutValue(K key, byte adjust_amount, byte put_amount)
  {
    int index = insertKey(key);
    boolean isNewMapping;
    byte newValue;
    boolean isNewMapping;
    if (index < 0)
    {
      index = -index - 1; int 
        tmp25_23 = index; byte[] tmp25_20 = this._values;byte newValue = tmp25_20[tmp25_23] = (byte)(tmp25_20[tmp25_23] + adjust_amount);
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
  
  public boolean forEachValue(TByteProcedure procedure)
  {
    Object[] keys = this._set;
    byte[] values = this._values;
    for (int i = values.length; i-- > 0;) {
      if ((keys[i] != FREE) && (keys[i] != REMOVED) && (!procedure.execute(values[i]))) {
        return false;
      }
    }
    return true;
  }
  
  public boolean forEachEntry(TObjectByteProcedure<? super K> procedure)
  {
    Object[] keys = this._set;
    byte[] values = this._values;
    for (int i = keys.length; i-- > 0;) {
      if ((keys[i] != FREE) && (keys[i] != REMOVED) && (!procedure.execute(keys[i], values[i]))) {
        return false;
      }
    }
    return true;
  }
  
  public boolean retainEntries(TObjectByteProcedure<? super K> procedure)
  {
    boolean modified = false;
    
    K[] keys = (Object[])this._set;
    byte[] values = this._values;
    
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
  
  public void transformValues(TByteFunction function)
  {
    Object[] keys = this._set;
    byte[] values = this._values;
    for (int i = values.length; i-- > 0;) {
      if ((keys[i] != null) && (keys[i] != REMOVED)) {
        values[i] = function.execute(values[i]);
      }
    }
  }
  
  public boolean equals(Object other)
  {
    if (!(other instanceof TObjectByteMap)) {
      return false;
    }
    TObjectByteMap that = (TObjectByteMap)other;
    if (that.size() != size()) {
      return false;
    }
    try
    {
      TObjectByteIterator iter = iterator();
      while (iter.hasNext())
      {
        iter.advance();
        Object key = iter.key();
        byte value = iter.value();
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
    byte[] values = this._values;
    for (int i = values.length; i-- > 0;) {
      if ((keys[i] != FREE) && (keys[i] != REMOVED)) {
        hashcode += (HashFunctions.hash(values[i]) ^ (keys[i] == null ? 0 : keys[i].hashCode()));
      }
    }
    return hashcode;
  }
  
  protected class KeyView
    extends TObjectByteCustomHashMap<K>.MapBackedView<K>
  {
    protected KeyView()
    {
      super(null);
    }
    
    public Iterator<K> iterator()
    {
      return new TObjectHashIterator(TObjectByteCustomHashMap.this);
    }
    
    public boolean removeElement(K key)
    {
      return TObjectByteCustomHashMap.this.no_entry_value != TObjectByteCustomHashMap.this.remove(key);
    }
    
    public boolean containsElement(K key)
    {
      return TObjectByteCustomHashMap.this.contains(key);
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
      TObjectByteCustomHashMap.this.clear();
    }
    
    public boolean add(E obj)
    {
      throw new UnsupportedOperationException();
    }
    
    public int size()
    {
      return TObjectByteCustomHashMap.this.size();
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
      return TObjectByteCustomHashMap.this.isEmpty();
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
  
  class TByteValueCollection
    implements TByteCollection
  {
    TByteValueCollection() {}
    
    public TByteIterator iterator()
    {
      return new TObjectByteValueHashIterator();
    }
    
    public byte getNoEntryValue()
    {
      return TObjectByteCustomHashMap.this.no_entry_value;
    }
    
    public int size()
    {
      return TObjectByteCustomHashMap.this._size;
    }
    
    public boolean isEmpty()
    {
      return 0 == TObjectByteCustomHashMap.this._size;
    }
    
    public boolean contains(byte entry)
    {
      return TObjectByteCustomHashMap.this.containsValue(entry);
    }
    
    public byte[] toArray()
    {
      return TObjectByteCustomHashMap.this.values();
    }
    
    public byte[] toArray(byte[] dest)
    {
      return TObjectByteCustomHashMap.this.values(dest);
    }
    
    public boolean add(byte entry)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean remove(byte entry)
    {
      byte[] values = TObjectByteCustomHashMap.this._values;
      Object[] set = TObjectByteCustomHashMap.this._set;
      for (int i = values.length; i-- > 0;) {
        if ((set[i] != TObjectHash.FREE) && (set[i] != TObjectHash.REMOVED) && (entry == values[i]))
        {
          TObjectByteCustomHashMap.this.removeAt(i);
          return true;
        }
      }
      return false;
    }
    
    public boolean containsAll(Collection<?> collection)
    {
      for (Object element : collection) {
        if ((element instanceof Byte))
        {
          byte ele = ((Byte)element).byteValue();
          if (!TObjectByteCustomHashMap.this.containsValue(ele)) {
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
    
    public boolean containsAll(TByteCollection collection)
    {
      TByteIterator iter = collection.iterator();
      while (iter.hasNext()) {
        if (!TObjectByteCustomHashMap.this.containsValue(iter.next())) {
          return false;
        }
      }
      return true;
    }
    
    public boolean containsAll(byte[] array)
    {
      for (byte element : array) {
        if (!TObjectByteCustomHashMap.this.containsValue(element)) {
          return false;
        }
      }
      return true;
    }
    
    public boolean addAll(Collection<? extends Byte> collection)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean addAll(TByteCollection collection)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean addAll(byte[] array)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean retainAll(Collection<?> collection)
    {
      boolean modified = false;
      TByteIterator iter = iterator();
      while (iter.hasNext()) {
        if (!collection.contains(Byte.valueOf(iter.next())))
        {
          iter.remove();
          modified = true;
        }
      }
      return modified;
    }
    
    public boolean retainAll(TByteCollection collection)
    {
      if (this == collection) {
        return false;
      }
      boolean modified = false;
      TByteIterator iter = iterator();
      while (iter.hasNext()) {
        if (!collection.contains(iter.next()))
        {
          iter.remove();
          modified = true;
        }
      }
      return modified;
    }
    
    public boolean retainAll(byte[] array)
    {
      boolean changed = false;
      Arrays.sort(array);
      byte[] values = TObjectByteCustomHashMap.this._values;
      
      Object[] set = TObjectByteCustomHashMap.this._set;
      for (int i = set.length; i-- > 0;) {
        if ((set[i] != TObjectHash.FREE) && (set[i] != TObjectHash.REMOVED) && (Arrays.binarySearch(array, values[i]) < 0))
        {
          TObjectByteCustomHashMap.this.removeAt(i);
          changed = true;
        }
      }
      return changed;
    }
    
    public boolean removeAll(Collection<?> collection)
    {
      boolean changed = false;
      for (Object element : collection) {
        if ((element instanceof Byte))
        {
          byte c = ((Byte)element).byteValue();
          if (remove(c)) {
            changed = true;
          }
        }
      }
      return changed;
    }
    
    public boolean removeAll(TByteCollection collection)
    {
      if (this == collection)
      {
        clear();
        return true;
      }
      boolean changed = false;
      TByteIterator iter = collection.iterator();
      while (iter.hasNext())
      {
        byte element = iter.next();
        if (remove(element)) {
          changed = true;
        }
      }
      return changed;
    }
    
    public boolean removeAll(byte[] array)
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
      TObjectByteCustomHashMap.this.clear();
    }
    
    public boolean forEach(TByteProcedure procedure)
    {
      return TObjectByteCustomHashMap.this.forEachValue(procedure);
    }
    
    public String toString()
    {
      final StringBuilder buf = new StringBuilder("{");
      TObjectByteCustomHashMap.this.forEachValue(new TByteProcedure()
      {
        private boolean first = true;
        
        public boolean execute(byte value)
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
    
    class TObjectByteValueHashIterator
      implements TByteIterator
    {
      protected THash _hash = TObjectByteCustomHashMap.this;
      protected int _expectedSize;
      protected int _index;
      
      TObjectByteValueHashIterator()
      {
        this._expectedSize = this._hash.size();
        this._index = this._hash.capacity();
      }
      
      public boolean hasNext()
      {
        return nextIndex() >= 0;
      }
      
      public byte next()
      {
        moveToNextIndex();
        return TObjectByteCustomHashMap.this._values[this._index];
      }
      
      public void remove()
      {
        if (this._expectedSize != this._hash.size()) {
          throw new ConcurrentModificationException();
        }
        try
        {
          this._hash.tempDisableAutoCompaction();
          TObjectByteCustomHashMap.this.removeAt(this._index);
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
        Object[] set = TObjectByteCustomHashMap.this._set;
        int i = this._index;
        while ((i-- > 0) && ((set[i] == TCustomObjectHash.FREE) || (set[i] == TCustomObjectHash.REMOVED))) {}
        return i;
      }
    }
  }
  
  class TObjectByteHashIterator<K>
    extends TObjectHashIterator<K>
    implements TObjectByteIterator<K>
  {
    private final TObjectByteCustomHashMap<K> _map;
    
    public TObjectByteHashIterator()
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
    
    public byte value()
    {
      return this._map._values[this._index];
    }
    
    public byte setValue(byte val)
    {
      byte old = value();
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
    
    out.writeByte(this.no_entry_value);
    
    out.writeInt(this._size);
    for (int i = this._set.length; i-- > 0;) {
      if ((this._set[i] != REMOVED) && (this._set[i] != FREE))
      {
        out.writeObject(this._set[i]);
        out.writeByte(this._values[i]);
      }
    }
  }
  
  public void readExternal(ObjectInput in)
    throws IOException, ClassNotFoundException
  {
    in.readByte();
    
    super.readExternal(in);
    
    this.strategy = ((HashingStrategy)in.readObject());
    
    this.no_entry_value = in.readByte();
    
    int size = in.readInt();
    setUp(size);
    while (size-- > 0)
    {
      K key = in.readObject();
      byte val = in.readByte();
      put(key, val);
    }
  }
  
  public String toString()
  {
    final StringBuilder buf = new StringBuilder("{");
    forEachEntry(new TObjectByteProcedure()
    {
      private boolean first = true;
      
      public boolean execute(K key, byte value)
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
