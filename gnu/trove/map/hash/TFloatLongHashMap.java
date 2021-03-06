package gnu.trove.map.hash;

import gnu.trove.TFloatCollection;
import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.TFloatLongHash;
import gnu.trove.impl.hash.THashPrimitiveIterator;
import gnu.trove.impl.hash.TPrimitiveHash;
import gnu.trove.iterator.TFloatIterator;
import gnu.trove.iterator.TFloatLongIterator;
import gnu.trove.iterator.TLongIterator;
import gnu.trove.map.TFloatLongMap;
import gnu.trove.procedure.TFloatLongProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TFloatSet;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.Map.Entry;

public class TFloatLongHashMap
  extends TFloatLongHash
  implements TFloatLongMap, Externalizable
{
  static final long serialVersionUID = 1L;
  protected transient long[] _values;
  
  public TFloatLongHashMap() {}
  
  public TFloatLongHashMap(int initialCapacity)
  {
    super(initialCapacity);
  }
  
  public TFloatLongHashMap(int initialCapacity, float loadFactor)
  {
    super(initialCapacity, loadFactor);
  }
  
  public TFloatLongHashMap(int initialCapacity, float loadFactor, float noEntryKey, long noEntryValue)
  {
    super(initialCapacity, loadFactor, noEntryKey, noEntryValue);
  }
  
  public TFloatLongHashMap(float[] keys, long[] values)
  {
    super(Math.max(keys.length, values.length));
    
    int size = Math.min(keys.length, values.length);
    for (int i = 0; i < size; i++) {
      put(keys[i], values[i]);
    }
  }
  
  public TFloatLongHashMap(TFloatLongMap map)
  {
    super(map.size());
    if ((map instanceof TFloatLongHashMap))
    {
      TFloatLongHashMap hashmap = (TFloatLongHashMap)map;
      this._loadFactor = hashmap._loadFactor;
      this.no_entry_key = hashmap.no_entry_key;
      this.no_entry_value = hashmap.no_entry_value;
      if (this.no_entry_key != 0.0F) {
        Arrays.fill(this._set, this.no_entry_key);
      }
      if (this.no_entry_value != 0L) {
        Arrays.fill(this._values, this.no_entry_value);
      }
      setUp((int)Math.ceil(10.0F / this._loadFactor));
    }
    putAll(map);
  }
  
  protected int setUp(int initialCapacity)
  {
    int capacity = super.setUp(initialCapacity);
    this._values = new long[capacity];
    return capacity;
  }
  
  protected void rehash(int newCapacity)
  {
    int oldCapacity = this._set.length;
    
    float[] oldKeys = this._set;
    long[] oldVals = this._values;
    byte[] oldStates = this._states;
    
    this._set = new float[newCapacity];
    this._values = new long[newCapacity];
    this._states = new byte[newCapacity];
    for (int i = oldCapacity; i-- > 0;) {
      if (oldStates[i] == 1)
      {
        float o = oldKeys[i];
        int index = insertKey(o);
        this._values[index] = oldVals[i];
      }
    }
  }
  
  public long put(float key, long value)
  {
    int index = insertKey(key);
    return doPut(key, value, index);
  }
  
  public long putIfAbsent(float key, long value)
  {
    int index = insertKey(key);
    if (index < 0) {
      return this._values[(-index - 1)];
    }
    return doPut(key, value, index);
  }
  
  private long doPut(float key, long value, int index)
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
  
  public void putAll(Map<? extends Float, ? extends Long> map)
  {
    ensureCapacity(map.size());
    for (Map.Entry<? extends Float, ? extends Long> entry : map.entrySet()) {
      put(((Float)entry.getKey()).floatValue(), ((Long)entry.getValue()).longValue());
    }
  }
  
  public void putAll(TFloatLongMap map)
  {
    ensureCapacity(map.size());
    TFloatLongIterator iter = map.iterator();
    while (iter.hasNext())
    {
      iter.advance();
      put(iter.key(), iter.value());
    }
  }
  
  public long get(float key)
  {
    int index = index(key);
    return index < 0 ? this.no_entry_value : this._values[index];
  }
  
  public void clear()
  {
    super.clear();
    Arrays.fill(this._set, 0, this._set.length, this.no_entry_key);
    Arrays.fill(this._values, 0, this._values.length, this.no_entry_value);
    Arrays.fill(this._states, 0, this._states.length, (byte)0);
  }
  
  public boolean isEmpty()
  {
    return 0 == this._size;
  }
  
  public long remove(float key)
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
  
  public TFloatSet keySet()
  {
    return new TKeyView();
  }
  
  public float[] keys()
  {
    float[] keys = new float[size()];
    float[] k = this._set;
    byte[] states = this._states;
    
    int i = k.length;
    for (int j = 0; i-- > 0;) {
      if (states[i] == 1) {
        keys[(j++)] = k[i];
      }
    }
    return keys;
  }
  
  public float[] keys(float[] array)
  {
    int size = size();
    if (array.length < size) {
      array = new float[size];
    }
    float[] keys = this._set;
    byte[] states = this._states;
    
    int i = keys.length;
    for (int j = 0; i-- > 0;) {
      if (states[i] == 1) {
        array[(j++)] = keys[i];
      }
    }
    return array;
  }
  
  public TLongCollection valueCollection()
  {
    return new TValueView();
  }
  
  public long[] values()
  {
    long[] vals = new long[size()];
    long[] v = this._values;
    byte[] states = this._states;
    
    int i = v.length;
    for (int j = 0; i-- > 0;) {
      if (states[i] == 1) {
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
    byte[] states = this._states;
    
    int i = v.length;
    for (int j = 0; i-- > 0;) {
      if (states[i] == 1) {
        array[(j++)] = v[i];
      }
    }
    return array;
  }
  
  public boolean containsValue(long val)
  {
    byte[] states = this._states;
    long[] vals = this._values;
    for (int i = vals.length; i-- > 0;) {
      if ((states[i] == 1) && (val == vals[i])) {
        return true;
      }
    }
    return false;
  }
  
  public boolean containsKey(float key)
  {
    return contains(key);
  }
  
  public TFloatLongIterator iterator()
  {
    return new TFloatLongHashIterator(this);
  }
  
  public boolean forEachKey(TFloatProcedure procedure)
  {
    return forEach(procedure);
  }
  
  public boolean forEachValue(TLongProcedure procedure)
  {
    byte[] states = this._states;
    long[] values = this._values;
    for (int i = values.length; i-- > 0;) {
      if ((states[i] == 1) && (!procedure.execute(values[i]))) {
        return false;
      }
    }
    return true;
  }
  
  public boolean forEachEntry(TFloatLongProcedure procedure)
  {
    byte[] states = this._states;
    float[] keys = this._set;
    long[] values = this._values;
    for (int i = keys.length; i-- > 0;) {
      if ((states[i] == 1) && (!procedure.execute(keys[i], values[i]))) {
        return false;
      }
    }
    return true;
  }
  
  public void transformValues(TLongFunction function)
  {
    byte[] states = this._states;
    long[] values = this._values;
    for (int i = values.length; i-- > 0;) {
      if (states[i] == 1) {
        values[i] = function.execute(values[i]);
      }
    }
  }
  
  public boolean retainEntries(TFloatLongProcedure procedure)
  {
    boolean modified = false;
    byte[] states = this._states;
    float[] keys = this._set;
    long[] values = this._values;
    
    tempDisableAutoCompaction();
    try
    {
      for (i = keys.length; i-- > 0;) {
        if ((states[i] == 1) && (!procedure.execute(keys[i], values[i])))
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
  
  public boolean increment(float key)
  {
    return adjustValue(key, 1L);
  }
  
  public boolean adjustValue(float key, long amount)
  {
    int index = index(key);
    if (index < 0) {
      return false;
    }
    this._values[index] += amount;
    return true;
  }
  
  public long adjustOrPutValue(float key, long adjust_amount, long put_amount)
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
    byte previousState = this._states[index];
    if (isNewMapping) {
      postInsertHook(this.consumeFreeSlot);
    }
    return newValue;
  }
  
  protected class TKeyView
    implements TFloatSet
  {
    protected TKeyView() {}
    
    public TFloatIterator iterator()
    {
      return new TFloatLongHashMap.TFloatLongKeyHashIterator(TFloatLongHashMap.this, TFloatLongHashMap.this);
    }
    
    public float getNoEntryValue()
    {
      return TFloatLongHashMap.this.no_entry_key;
    }
    
    public int size()
    {
      return TFloatLongHashMap.this._size;
    }
    
    public boolean isEmpty()
    {
      return 0 == TFloatLongHashMap.this._size;
    }
    
    public boolean contains(float entry)
    {
      return TFloatLongHashMap.this.contains(entry);
    }
    
    public float[] toArray()
    {
      return TFloatLongHashMap.this.keys();
    }
    
    public float[] toArray(float[] dest)
    {
      return TFloatLongHashMap.this.keys(dest);
    }
    
    public boolean add(float entry)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean remove(float entry)
    {
      return TFloatLongHashMap.this.no_entry_value != TFloatLongHashMap.this.remove(entry);
    }
    
    public boolean containsAll(Collection<?> collection)
    {
      for (Object element : collection) {
        if ((element instanceof Float))
        {
          float ele = ((Float)element).floatValue();
          if (!TFloatLongHashMap.this.containsKey(ele)) {
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
        if (!TFloatLongHashMap.this.containsKey(iter.next())) {
          return false;
        }
      }
      return true;
    }
    
    public boolean containsAll(float[] array)
    {
      for (float element : array) {
        if (!TFloatLongHashMap.this.contains(element)) {
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
      float[] set = TFloatLongHashMap.this._set;
      byte[] states = TFloatLongHashMap.this._states;
      for (int i = set.length; i-- > 0;) {
        if ((states[i] == 1) && (Arrays.binarySearch(array, set[i]) < 0))
        {
          TFloatLongHashMap.this.removeAt(i);
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
      TFloatLongHashMap.this.clear();
    }
    
    public boolean forEach(TFloatProcedure procedure)
    {
      return TFloatLongHashMap.this.forEachKey(procedure);
    }
    
    public boolean equals(Object other)
    {
      if (!(other instanceof TFloatSet)) {
        return false;
      }
      TFloatSet that = (TFloatSet)other;
      if (that.size() != size()) {
        return false;
      }
      for (int i = TFloatLongHashMap.this._states.length; i-- > 0;) {
        if ((TFloatLongHashMap.this._states[i] == 1) && 
          (!that.contains(TFloatLongHashMap.this._set[i]))) {
          return false;
        }
      }
      return true;
    }
    
    public int hashCode()
    {
      int hashcode = 0;
      for (int i = TFloatLongHashMap.this._states.length; i-- > 0;) {
        if (TFloatLongHashMap.this._states[i] == 1) {
          hashcode += HashFunctions.hash(TFloatLongHashMap.this._set[i]);
        }
      }
      return hashcode;
    }
    
    public String toString()
    {
      final StringBuilder buf = new StringBuilder("{");
      TFloatLongHashMap.this.forEachKey(new TFloatProcedure()
      {
        private boolean first = true;
        
        public boolean execute(float key)
        {
          if (this.first) {
            this.first = false;
          } else {
            buf.append(", ");
          }
          buf.append(key);
          return true;
        }
      });
      buf.append("}");
      return buf.toString();
    }
  }
  
  protected class TValueView
    implements TLongCollection
  {
    protected TValueView() {}
    
    public TLongIterator iterator()
    {
      return new TFloatLongHashMap.TFloatLongValueHashIterator(TFloatLongHashMap.this, TFloatLongHashMap.this);
    }
    
    public long getNoEntryValue()
    {
      return TFloatLongHashMap.this.no_entry_value;
    }
    
    public int size()
    {
      return TFloatLongHashMap.this._size;
    }
    
    public boolean isEmpty()
    {
      return 0 == TFloatLongHashMap.this._size;
    }
    
    public boolean contains(long entry)
    {
      return TFloatLongHashMap.this.containsValue(entry);
    }
    
    public long[] toArray()
    {
      return TFloatLongHashMap.this.values();
    }
    
    public long[] toArray(long[] dest)
    {
      return TFloatLongHashMap.this.values(dest);
    }
    
    public boolean add(long entry)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean remove(long entry)
    {
      long[] values = TFloatLongHashMap.this._values;
      float[] set = TFloatLongHashMap.this._set;
      for (int i = values.length; i-- > 0;) {
        if ((set[i] != 0.0F) && (set[i] != 2.0F) && (entry == values[i]))
        {
          TFloatLongHashMap.this.removeAt(i);
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
          if (!TFloatLongHashMap.this.containsValue(ele)) {
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
        if (!TFloatLongHashMap.this.containsValue(iter.next())) {
          return false;
        }
      }
      return true;
    }
    
    public boolean containsAll(long[] array)
    {
      for (long element : array) {
        if (!TFloatLongHashMap.this.containsValue(element)) {
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
      long[] values = TFloatLongHashMap.this._values;
      byte[] states = TFloatLongHashMap.this._states;
      for (int i = values.length; i-- > 0;) {
        if ((states[i] == 1) && (Arrays.binarySearch(array, values[i]) < 0))
        {
          TFloatLongHashMap.this.removeAt(i);
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
      TFloatLongHashMap.this.clear();
    }
    
    public boolean forEach(TLongProcedure procedure)
    {
      return TFloatLongHashMap.this.forEachValue(procedure);
    }
    
    public String toString()
    {
      final StringBuilder buf = new StringBuilder("{");
      TFloatLongHashMap.this.forEachValue(new TLongProcedure()
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
  }
  
  class TFloatLongKeyHashIterator
    extends THashPrimitiveIterator
    implements TFloatIterator
  {
    TFloatLongKeyHashIterator(TPrimitiveHash hash)
    {
      super();
    }
    
    public float next()
    {
      moveToNextIndex();
      return TFloatLongHashMap.this._set[this._index];
    }
    
    public void remove()
    {
      if (this._expectedSize != this._hash.size()) {
        throw new ConcurrentModificationException();
      }
      try
      {
        this._hash.tempDisableAutoCompaction();
        TFloatLongHashMap.this.removeAt(this._index);
      }
      finally
      {
        this._hash.reenableAutoCompaction(false);
      }
      this._expectedSize -= 1;
    }
  }
  
  class TFloatLongValueHashIterator
    extends THashPrimitiveIterator
    implements TLongIterator
  {
    TFloatLongValueHashIterator(TPrimitiveHash hash)
    {
      super();
    }
    
    public long next()
    {
      moveToNextIndex();
      return TFloatLongHashMap.this._values[this._index];
    }
    
    public void remove()
    {
      if (this._expectedSize != this._hash.size()) {
        throw new ConcurrentModificationException();
      }
      try
      {
        this._hash.tempDisableAutoCompaction();
        TFloatLongHashMap.this.removeAt(this._index);
      }
      finally
      {
        this._hash.reenableAutoCompaction(false);
      }
      this._expectedSize -= 1;
    }
  }
  
  class TFloatLongHashIterator
    extends THashPrimitiveIterator
    implements TFloatLongIterator
  {
    TFloatLongHashIterator(TFloatLongHashMap map)
    {
      super();
    }
    
    public void advance()
    {
      moveToNextIndex();
    }
    
    public float key()
    {
      return TFloatLongHashMap.this._set[this._index];
    }
    
    public long value()
    {
      return TFloatLongHashMap.this._values[this._index];
    }
    
    public long setValue(long val)
    {
      long old = value();
      TFloatLongHashMap.this._values[this._index] = val;
      return old;
    }
    
    public void remove()
    {
      if (this._expectedSize != this._hash.size()) {
        throw new ConcurrentModificationException();
      }
      try
      {
        this._hash.tempDisableAutoCompaction();
        TFloatLongHashMap.this.removeAt(this._index);
      }
      finally
      {
        this._hash.reenableAutoCompaction(false);
      }
      this._expectedSize -= 1;
    }
  }
  
  public boolean equals(Object other)
  {
    if (!(other instanceof TFloatLongMap)) {
      return false;
    }
    TFloatLongMap that = (TFloatLongMap)other;
    if (that.size() != size()) {
      return false;
    }
    long[] values = this._values;
    byte[] states = this._states;
    long this_no_entry_value = getNoEntryValue();
    long that_no_entry_value = that.getNoEntryValue();
    for (int i = values.length; i-- > 0;) {
      if (states[i] == 1)
      {
        float key = this._set[i];
        long that_value = that.get(key);
        long this_value = values[i];
        if ((this_value != that_value) && (this_value != this_no_entry_value) && (that_value != that_no_entry_value)) {
          return false;
        }
      }
    }
    return true;
  }
  
  public int hashCode()
  {
    int hashcode = 0;
    byte[] states = this._states;
    for (int i = this._values.length; i-- > 0;) {
      if (states[i] == 1) {
        hashcode += (HashFunctions.hash(this._set[i]) ^ HashFunctions.hash(this._values[i]));
      }
    }
    return hashcode;
  }
  
  public String toString()
  {
    final StringBuilder buf = new StringBuilder("{");
    forEachEntry(new TFloatLongProcedure()
    {
      private boolean first = true;
      
      public boolean execute(float key, long value)
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
  
  public void writeExternal(ObjectOutput out)
    throws IOException
  {
    out.writeByte(0);
    
    super.writeExternal(out);
    
    out.writeInt(this._size);
    for (int i = this._states.length; i-- > 0;) {
      if (this._states[i] == 1)
      {
        out.writeFloat(this._set[i]);
        out.writeLong(this._values[i]);
      }
    }
  }
  
  public void readExternal(ObjectInput in)
    throws IOException, ClassNotFoundException
  {
    in.readByte();
    
    super.readExternal(in);
    
    int size = in.readInt();
    setUp(size);
    while (size-- > 0)
    {
      float key = in.readFloat();
      long val = in.readLong();
      put(key, val);
    }
  }
}
