package gnu.trove.map.hash;

import gnu.trove.TLongCollection;
import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.THashPrimitiveIterator;
import gnu.trove.impl.hash.TLongShortHash;
import gnu.trove.impl.hash.TPrimitiveHash;
import gnu.trove.iterator.TLongIterator;
import gnu.trove.iterator.TLongShortIterator;
import gnu.trove.iterator.TShortIterator;
import gnu.trove.map.TLongShortMap;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.procedure.TLongShortProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TLongSet;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.Map.Entry;

public class TLongShortHashMap
  extends TLongShortHash
  implements TLongShortMap, Externalizable
{
  static final long serialVersionUID = 1L;
  protected transient short[] _values;
  
  public TLongShortHashMap() {}
  
  public TLongShortHashMap(int initialCapacity)
  {
    super(initialCapacity);
  }
  
  public TLongShortHashMap(int initialCapacity, float loadFactor)
  {
    super(initialCapacity, loadFactor);
  }
  
  public TLongShortHashMap(int initialCapacity, float loadFactor, long noEntryKey, short noEntryValue)
  {
    super(initialCapacity, loadFactor, noEntryKey, noEntryValue);
  }
  
  public TLongShortHashMap(long[] keys, short[] values)
  {
    super(Math.max(keys.length, values.length));
    
    int size = Math.min(keys.length, values.length);
    for (int i = 0; i < size; i++) {
      put(keys[i], values[i]);
    }
  }
  
  public TLongShortHashMap(TLongShortMap map)
  {
    super(map.size());
    if ((map instanceof TLongShortHashMap))
    {
      TLongShortHashMap hashmap = (TLongShortHashMap)map;
      this._loadFactor = hashmap._loadFactor;
      this.no_entry_key = hashmap.no_entry_key;
      this.no_entry_value = hashmap.no_entry_value;
      if (this.no_entry_key != 0L) {
        Arrays.fill(this._set, this.no_entry_key);
      }
      if (this.no_entry_value != 0) {
        Arrays.fill(this._values, this.no_entry_value);
      }
      setUp((int)Math.ceil(10.0F / this._loadFactor));
    }
    putAll(map);
  }
  
  protected int setUp(int initialCapacity)
  {
    int capacity = super.setUp(initialCapacity);
    this._values = new short[capacity];
    return capacity;
  }
  
  protected void rehash(int newCapacity)
  {
    int oldCapacity = this._set.length;
    
    long[] oldKeys = this._set;
    short[] oldVals = this._values;
    byte[] oldStates = this._states;
    
    this._set = new long[newCapacity];
    this._values = new short[newCapacity];
    this._states = new byte[newCapacity];
    for (int i = oldCapacity; i-- > 0;) {
      if (oldStates[i] == 1)
      {
        long o = oldKeys[i];
        int index = insertKey(o);
        this._values[index] = oldVals[i];
      }
    }
  }
  
  public short put(long key, short value)
  {
    int index = insertKey(key);
    return doPut(key, value, index);
  }
  
  public short putIfAbsent(long key, short value)
  {
    int index = insertKey(key);
    if (index < 0) {
      return this._values[(-index - 1)];
    }
    return doPut(key, value, index);
  }
  
  private short doPut(long key, short value, int index)
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
  
  public void putAll(Map<? extends Long, ? extends Short> map)
  {
    ensureCapacity(map.size());
    for (Map.Entry<? extends Long, ? extends Short> entry : map.entrySet()) {
      put(((Long)entry.getKey()).longValue(), ((Short)entry.getValue()).shortValue());
    }
  }
  
  public void putAll(TLongShortMap map)
  {
    ensureCapacity(map.size());
    TLongShortIterator iter = map.iterator();
    while (iter.hasNext())
    {
      iter.advance();
      put(iter.key(), iter.value());
    }
  }
  
  public short get(long key)
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
  
  public short remove(long key)
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
  
  public TLongSet keySet()
  {
    return new TKeyView();
  }
  
  public long[] keys()
  {
    long[] keys = new long[size()];
    long[] k = this._set;
    byte[] states = this._states;
    
    int i = k.length;
    for (int j = 0; i-- > 0;) {
      if (states[i] == 1) {
        keys[(j++)] = k[i];
      }
    }
    return keys;
  }
  
  public long[] keys(long[] array)
  {
    int size = size();
    if (array.length < size) {
      array = new long[size];
    }
    long[] keys = this._set;
    byte[] states = this._states;
    
    int i = keys.length;
    for (int j = 0; i-- > 0;) {
      if (states[i] == 1) {
        array[(j++)] = keys[i];
      }
    }
    return array;
  }
  
  public TShortCollection valueCollection()
  {
    return new TValueView();
  }
  
  public short[] values()
  {
    short[] vals = new short[size()];
    short[] v = this._values;
    byte[] states = this._states;
    
    int i = v.length;
    for (int j = 0; i-- > 0;) {
      if (states[i] == 1) {
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
    byte[] states = this._states;
    
    int i = v.length;
    for (int j = 0; i-- > 0;) {
      if (states[i] == 1) {
        array[(j++)] = v[i];
      }
    }
    return array;
  }
  
  public boolean containsValue(short val)
  {
    byte[] states = this._states;
    short[] vals = this._values;
    for (int i = vals.length; i-- > 0;) {
      if ((states[i] == 1) && (val == vals[i])) {
        return true;
      }
    }
    return false;
  }
  
  public boolean containsKey(long key)
  {
    return contains(key);
  }
  
  public TLongShortIterator iterator()
  {
    return new TLongShortHashIterator(this);
  }
  
  public boolean forEachKey(TLongProcedure procedure)
  {
    return forEach(procedure);
  }
  
  public boolean forEachValue(TShortProcedure procedure)
  {
    byte[] states = this._states;
    short[] values = this._values;
    for (int i = values.length; i-- > 0;) {
      if ((states[i] == 1) && (!procedure.execute(values[i]))) {
        return false;
      }
    }
    return true;
  }
  
  public boolean forEachEntry(TLongShortProcedure procedure)
  {
    byte[] states = this._states;
    long[] keys = this._set;
    short[] values = this._values;
    for (int i = keys.length; i-- > 0;) {
      if ((states[i] == 1) && (!procedure.execute(keys[i], values[i]))) {
        return false;
      }
    }
    return true;
  }
  
  public void transformValues(TShortFunction function)
  {
    byte[] states = this._states;
    short[] values = this._values;
    for (int i = values.length; i-- > 0;) {
      if (states[i] == 1) {
        values[i] = function.execute(values[i]);
      }
    }
  }
  
  public boolean retainEntries(TLongShortProcedure procedure)
  {
    boolean modified = false;
    byte[] states = this._states;
    long[] keys = this._set;
    short[] values = this._values;
    
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
  
  public boolean increment(long key)
  {
    return adjustValue(key, (short)1);
  }
  
  public boolean adjustValue(long key, short amount)
  {
    int index = index(key);
    if (index < 0) {
      return false;
    }
    int tmp20_18 = index; short[] tmp20_15 = this._values;tmp20_15[tmp20_18] = ((short)(tmp20_15[tmp20_18] + amount));
    return true;
  }
  
  public short adjustOrPutValue(long key, short adjust_amount, short put_amount)
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
    byte previousState = this._states[index];
    if (isNewMapping) {
      postInsertHook(this.consumeFreeSlot);
    }
    return newValue;
  }
  
  protected class TKeyView
    implements TLongSet
  {
    protected TKeyView() {}
    
    public TLongIterator iterator()
    {
      return new TLongShortHashMap.TLongShortKeyHashIterator(TLongShortHashMap.this, TLongShortHashMap.this);
    }
    
    public long getNoEntryValue()
    {
      return TLongShortHashMap.this.no_entry_key;
    }
    
    public int size()
    {
      return TLongShortHashMap.this._size;
    }
    
    public boolean isEmpty()
    {
      return 0 == TLongShortHashMap.this._size;
    }
    
    public boolean contains(long entry)
    {
      return TLongShortHashMap.this.contains(entry);
    }
    
    public long[] toArray()
    {
      return TLongShortHashMap.this.keys();
    }
    
    public long[] toArray(long[] dest)
    {
      return TLongShortHashMap.this.keys(dest);
    }
    
    public boolean add(long entry)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean remove(long entry)
    {
      return TLongShortHashMap.this.no_entry_value != TLongShortHashMap.this.remove(entry);
    }
    
    public boolean containsAll(Collection<?> collection)
    {
      for (Object element : collection) {
        if ((element instanceof Long))
        {
          long ele = ((Long)element).longValue();
          if (!TLongShortHashMap.this.containsKey(ele)) {
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
        if (!TLongShortHashMap.this.containsKey(iter.next())) {
          return false;
        }
      }
      return true;
    }
    
    public boolean containsAll(long[] array)
    {
      for (long element : array) {
        if (!TLongShortHashMap.this.contains(element)) {
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
      long[] set = TLongShortHashMap.this._set;
      byte[] states = TLongShortHashMap.this._states;
      for (int i = set.length; i-- > 0;) {
        if ((states[i] == 1) && (Arrays.binarySearch(array, set[i]) < 0))
        {
          TLongShortHashMap.this.removeAt(i);
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
      TLongShortHashMap.this.clear();
    }
    
    public boolean forEach(TLongProcedure procedure)
    {
      return TLongShortHashMap.this.forEachKey(procedure);
    }
    
    public boolean equals(Object other)
    {
      if (!(other instanceof TLongSet)) {
        return false;
      }
      TLongSet that = (TLongSet)other;
      if (that.size() != size()) {
        return false;
      }
      for (int i = TLongShortHashMap.this._states.length; i-- > 0;) {
        if ((TLongShortHashMap.this._states[i] == 1) && 
          (!that.contains(TLongShortHashMap.this._set[i]))) {
          return false;
        }
      }
      return true;
    }
    
    public int hashCode()
    {
      int hashcode = 0;
      for (int i = TLongShortHashMap.this._states.length; i-- > 0;) {
        if (TLongShortHashMap.this._states[i] == 1) {
          hashcode += HashFunctions.hash(TLongShortHashMap.this._set[i]);
        }
      }
      return hashcode;
    }
    
    public String toString()
    {
      final StringBuilder buf = new StringBuilder("{");
      TLongShortHashMap.this.forEachKey(new TLongProcedure()
      {
        private boolean first = true;
        
        public boolean execute(long key)
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
    implements TShortCollection
  {
    protected TValueView() {}
    
    public TShortIterator iterator()
    {
      return new TLongShortHashMap.TLongShortValueHashIterator(TLongShortHashMap.this, TLongShortHashMap.this);
    }
    
    public short getNoEntryValue()
    {
      return TLongShortHashMap.this.no_entry_value;
    }
    
    public int size()
    {
      return TLongShortHashMap.this._size;
    }
    
    public boolean isEmpty()
    {
      return 0 == TLongShortHashMap.this._size;
    }
    
    public boolean contains(short entry)
    {
      return TLongShortHashMap.this.containsValue(entry);
    }
    
    public short[] toArray()
    {
      return TLongShortHashMap.this.values();
    }
    
    public short[] toArray(short[] dest)
    {
      return TLongShortHashMap.this.values(dest);
    }
    
    public boolean add(short entry)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean remove(short entry)
    {
      short[] values = TLongShortHashMap.this._values;
      long[] set = TLongShortHashMap.this._set;
      for (int i = values.length; i-- > 0;) {
        if ((set[i] != 0L) && (set[i] != 2L) && (entry == values[i]))
        {
          TLongShortHashMap.this.removeAt(i);
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
          if (!TLongShortHashMap.this.containsValue(ele)) {
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
        if (!TLongShortHashMap.this.containsValue(iter.next())) {
          return false;
        }
      }
      return true;
    }
    
    public boolean containsAll(short[] array)
    {
      for (short element : array) {
        if (!TLongShortHashMap.this.containsValue(element)) {
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
      short[] values = TLongShortHashMap.this._values;
      byte[] states = TLongShortHashMap.this._states;
      for (int i = values.length; i-- > 0;) {
        if ((states[i] == 1) && (Arrays.binarySearch(array, values[i]) < 0))
        {
          TLongShortHashMap.this.removeAt(i);
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
      TLongShortHashMap.this.clear();
    }
    
    public boolean forEach(TShortProcedure procedure)
    {
      return TLongShortHashMap.this.forEachValue(procedure);
    }
    
    public String toString()
    {
      final StringBuilder buf = new StringBuilder("{");
      TLongShortHashMap.this.forEachValue(new TShortProcedure()
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
  }
  
  class TLongShortKeyHashIterator
    extends THashPrimitiveIterator
    implements TLongIterator
  {
    TLongShortKeyHashIterator(TPrimitiveHash hash)
    {
      super();
    }
    
    public long next()
    {
      moveToNextIndex();
      return TLongShortHashMap.this._set[this._index];
    }
    
    public void remove()
    {
      if (this._expectedSize != this._hash.size()) {
        throw new ConcurrentModificationException();
      }
      try
      {
        this._hash.tempDisableAutoCompaction();
        TLongShortHashMap.this.removeAt(this._index);
      }
      finally
      {
        this._hash.reenableAutoCompaction(false);
      }
      this._expectedSize -= 1;
    }
  }
  
  class TLongShortValueHashIterator
    extends THashPrimitiveIterator
    implements TShortIterator
  {
    TLongShortValueHashIterator(TPrimitiveHash hash)
    {
      super();
    }
    
    public short next()
    {
      moveToNextIndex();
      return TLongShortHashMap.this._values[this._index];
    }
    
    public void remove()
    {
      if (this._expectedSize != this._hash.size()) {
        throw new ConcurrentModificationException();
      }
      try
      {
        this._hash.tempDisableAutoCompaction();
        TLongShortHashMap.this.removeAt(this._index);
      }
      finally
      {
        this._hash.reenableAutoCompaction(false);
      }
      this._expectedSize -= 1;
    }
  }
  
  class TLongShortHashIterator
    extends THashPrimitiveIterator
    implements TLongShortIterator
  {
    TLongShortHashIterator(TLongShortHashMap map)
    {
      super();
    }
    
    public void advance()
    {
      moveToNextIndex();
    }
    
    public long key()
    {
      return TLongShortHashMap.this._set[this._index];
    }
    
    public short value()
    {
      return TLongShortHashMap.this._values[this._index];
    }
    
    public short setValue(short val)
    {
      short old = value();
      TLongShortHashMap.this._values[this._index] = val;
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
        TLongShortHashMap.this.removeAt(this._index);
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
    if (!(other instanceof TLongShortMap)) {
      return false;
    }
    TLongShortMap that = (TLongShortMap)other;
    if (that.size() != size()) {
      return false;
    }
    short[] values = this._values;
    byte[] states = this._states;
    short this_no_entry_value = getNoEntryValue();
    short that_no_entry_value = that.getNoEntryValue();
    for (int i = values.length; i-- > 0;) {
      if (states[i] == 1)
      {
        long key = this._set[i];
        short that_value = that.get(key);
        short this_value = values[i];
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
    forEachEntry(new TLongShortProcedure()
    {
      private boolean first = true;
      
      public boolean execute(long key, short value)
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
        out.writeLong(this._set[i]);
        out.writeShort(this._values[i]);
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
      long key = in.readLong();
      short val = in.readShort();
      put(key, val);
    }
  }
}
