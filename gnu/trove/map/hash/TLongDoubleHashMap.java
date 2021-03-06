package gnu.trove.map.hash;

import gnu.trove.TDoubleCollection;
import gnu.trove.TLongCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.THashPrimitiveIterator;
import gnu.trove.impl.hash.TLongDoubleHash;
import gnu.trove.impl.hash.TPrimitiveHash;
import gnu.trove.iterator.TDoubleIterator;
import gnu.trove.iterator.TLongDoubleIterator;
import gnu.trove.iterator.TLongIterator;
import gnu.trove.map.TLongDoubleMap;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TLongDoubleProcedure;
import gnu.trove.procedure.TLongProcedure;
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

public class TLongDoubleHashMap
  extends TLongDoubleHash
  implements TLongDoubleMap, Externalizable
{
  static final long serialVersionUID = 1L;
  protected transient double[] _values;
  
  public TLongDoubleHashMap() {}
  
  public TLongDoubleHashMap(int initialCapacity)
  {
    super(initialCapacity);
  }
  
  public TLongDoubleHashMap(int initialCapacity, float loadFactor)
  {
    super(initialCapacity, loadFactor);
  }
  
  public TLongDoubleHashMap(int initialCapacity, float loadFactor, long noEntryKey, double noEntryValue)
  {
    super(initialCapacity, loadFactor, noEntryKey, noEntryValue);
  }
  
  public TLongDoubleHashMap(long[] keys, double[] values)
  {
    super(Math.max(keys.length, values.length));
    
    int size = Math.min(keys.length, values.length);
    for (int i = 0; i < size; i++) {
      put(keys[i], values[i]);
    }
  }
  
  public TLongDoubleHashMap(TLongDoubleMap map)
  {
    super(map.size());
    if ((map instanceof TLongDoubleHashMap))
    {
      TLongDoubleHashMap hashmap = (TLongDoubleHashMap)map;
      this._loadFactor = hashmap._loadFactor;
      this.no_entry_key = hashmap.no_entry_key;
      this.no_entry_value = hashmap.no_entry_value;
      if (this.no_entry_key != 0L) {
        Arrays.fill(this._set, this.no_entry_key);
      }
      if (this.no_entry_value != 0.0D) {
        Arrays.fill(this._values, this.no_entry_value);
      }
      setUp((int)Math.ceil(10.0F / this._loadFactor));
    }
    putAll(map);
  }
  
  protected int setUp(int initialCapacity)
  {
    int capacity = super.setUp(initialCapacity);
    this._values = new double[capacity];
    return capacity;
  }
  
  protected void rehash(int newCapacity)
  {
    int oldCapacity = this._set.length;
    
    long[] oldKeys = this._set;
    double[] oldVals = this._values;
    byte[] oldStates = this._states;
    
    this._set = new long[newCapacity];
    this._values = new double[newCapacity];
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
  
  public double put(long key, double value)
  {
    int index = insertKey(key);
    return doPut(key, value, index);
  }
  
  public double putIfAbsent(long key, double value)
  {
    int index = insertKey(key);
    if (index < 0) {
      return this._values[(-index - 1)];
    }
    return doPut(key, value, index);
  }
  
  private double doPut(long key, double value, int index)
  {
    double previous = this.no_entry_value;
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
  
  public void putAll(Map<? extends Long, ? extends Double> map)
  {
    ensureCapacity(map.size());
    for (Map.Entry<? extends Long, ? extends Double> entry : map.entrySet()) {
      put(((Long)entry.getKey()).longValue(), ((Double)entry.getValue()).doubleValue());
    }
  }
  
  public void putAll(TLongDoubleMap map)
  {
    ensureCapacity(map.size());
    TLongDoubleIterator iter = map.iterator();
    while (iter.hasNext())
    {
      iter.advance();
      put(iter.key(), iter.value());
    }
  }
  
  public double get(long key)
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
  
  public double remove(long key)
  {
    double prev = this.no_entry_value;
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
  
  public TDoubleCollection valueCollection()
  {
    return new TValueView();
  }
  
  public double[] values()
  {
    double[] vals = new double[size()];
    double[] v = this._values;
    byte[] states = this._states;
    
    int i = v.length;
    for (int j = 0; i-- > 0;) {
      if (states[i] == 1) {
        vals[(j++)] = v[i];
      }
    }
    return vals;
  }
  
  public double[] values(double[] array)
  {
    int size = size();
    if (array.length < size) {
      array = new double[size];
    }
    double[] v = this._values;
    byte[] states = this._states;
    
    int i = v.length;
    for (int j = 0; i-- > 0;) {
      if (states[i] == 1) {
        array[(j++)] = v[i];
      }
    }
    return array;
  }
  
  public boolean containsValue(double val)
  {
    byte[] states = this._states;
    double[] vals = this._values;
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
  
  public TLongDoubleIterator iterator()
  {
    return new TLongDoubleHashIterator(this);
  }
  
  public boolean forEachKey(TLongProcedure procedure)
  {
    return forEach(procedure);
  }
  
  public boolean forEachValue(TDoubleProcedure procedure)
  {
    byte[] states = this._states;
    double[] values = this._values;
    for (int i = values.length; i-- > 0;) {
      if ((states[i] == 1) && (!procedure.execute(values[i]))) {
        return false;
      }
    }
    return true;
  }
  
  public boolean forEachEntry(TLongDoubleProcedure procedure)
  {
    byte[] states = this._states;
    long[] keys = this._set;
    double[] values = this._values;
    for (int i = keys.length; i-- > 0;) {
      if ((states[i] == 1) && (!procedure.execute(keys[i], values[i]))) {
        return false;
      }
    }
    return true;
  }
  
  public void transformValues(TDoubleFunction function)
  {
    byte[] states = this._states;
    double[] values = this._values;
    for (int i = values.length; i-- > 0;) {
      if (states[i] == 1) {
        values[i] = function.execute(values[i]);
      }
    }
  }
  
  public boolean retainEntries(TLongDoubleProcedure procedure)
  {
    boolean modified = false;
    byte[] states = this._states;
    long[] keys = this._set;
    double[] values = this._values;
    
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
    return adjustValue(key, 1.0D);
  }
  
  public boolean adjustValue(long key, double amount)
  {
    int index = index(key);
    if (index < 0) {
      return false;
    }
    this._values[index] += amount;
    return true;
  }
  
  public double adjustOrPutValue(long key, double adjust_amount, double put_amount)
  {
    int index = insertKey(key);
    boolean isNewMapping;
    double newValue;
    boolean isNewMapping;
    if (index < 0)
    {
      index = -index - 1;
      double newValue = this._values[index] += adjust_amount;
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
      return new TLongDoubleHashMap.TLongDoubleKeyHashIterator(TLongDoubleHashMap.this, TLongDoubleHashMap.this);
    }
    
    public long getNoEntryValue()
    {
      return TLongDoubleHashMap.this.no_entry_key;
    }
    
    public int size()
    {
      return TLongDoubleHashMap.this._size;
    }
    
    public boolean isEmpty()
    {
      return 0 == TLongDoubleHashMap.this._size;
    }
    
    public boolean contains(long entry)
    {
      return TLongDoubleHashMap.this.contains(entry);
    }
    
    public long[] toArray()
    {
      return TLongDoubleHashMap.this.keys();
    }
    
    public long[] toArray(long[] dest)
    {
      return TLongDoubleHashMap.this.keys(dest);
    }
    
    public boolean add(long entry)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean remove(long entry)
    {
      return TLongDoubleHashMap.this.no_entry_value != TLongDoubleHashMap.this.remove(entry);
    }
    
    public boolean containsAll(Collection<?> collection)
    {
      for (Object element : collection) {
        if ((element instanceof Long))
        {
          long ele = ((Long)element).longValue();
          if (!TLongDoubleHashMap.this.containsKey(ele)) {
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
        if (!TLongDoubleHashMap.this.containsKey(iter.next())) {
          return false;
        }
      }
      return true;
    }
    
    public boolean containsAll(long[] array)
    {
      for (long element : array) {
        if (!TLongDoubleHashMap.this.contains(element)) {
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
      long[] set = TLongDoubleHashMap.this._set;
      byte[] states = TLongDoubleHashMap.this._states;
      for (int i = set.length; i-- > 0;) {
        if ((states[i] == 1) && (Arrays.binarySearch(array, set[i]) < 0))
        {
          TLongDoubleHashMap.this.removeAt(i);
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
      TLongDoubleHashMap.this.clear();
    }
    
    public boolean forEach(TLongProcedure procedure)
    {
      return TLongDoubleHashMap.this.forEachKey(procedure);
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
      for (int i = TLongDoubleHashMap.this._states.length; i-- > 0;) {
        if ((TLongDoubleHashMap.this._states[i] == 1) && 
          (!that.contains(TLongDoubleHashMap.this._set[i]))) {
          return false;
        }
      }
      return true;
    }
    
    public int hashCode()
    {
      int hashcode = 0;
      for (int i = TLongDoubleHashMap.this._states.length; i-- > 0;) {
        if (TLongDoubleHashMap.this._states[i] == 1) {
          hashcode += HashFunctions.hash(TLongDoubleHashMap.this._set[i]);
        }
      }
      return hashcode;
    }
    
    public String toString()
    {
      final StringBuilder buf = new StringBuilder("{");
      TLongDoubleHashMap.this.forEachKey(new TLongProcedure()
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
    implements TDoubleCollection
  {
    protected TValueView() {}
    
    public TDoubleIterator iterator()
    {
      return new TLongDoubleHashMap.TLongDoubleValueHashIterator(TLongDoubleHashMap.this, TLongDoubleHashMap.this);
    }
    
    public double getNoEntryValue()
    {
      return TLongDoubleHashMap.this.no_entry_value;
    }
    
    public int size()
    {
      return TLongDoubleHashMap.this._size;
    }
    
    public boolean isEmpty()
    {
      return 0 == TLongDoubleHashMap.this._size;
    }
    
    public boolean contains(double entry)
    {
      return TLongDoubleHashMap.this.containsValue(entry);
    }
    
    public double[] toArray()
    {
      return TLongDoubleHashMap.this.values();
    }
    
    public double[] toArray(double[] dest)
    {
      return TLongDoubleHashMap.this.values(dest);
    }
    
    public boolean add(double entry)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean remove(double entry)
    {
      double[] values = TLongDoubleHashMap.this._values;
      long[] set = TLongDoubleHashMap.this._set;
      for (int i = values.length; i-- > 0;) {
        if ((set[i] != 0L) && (set[i] != 2L) && (entry == values[i]))
        {
          TLongDoubleHashMap.this.removeAt(i);
          return true;
        }
      }
      return false;
    }
    
    public boolean containsAll(Collection<?> collection)
    {
      for (Object element : collection) {
        if ((element instanceof Double))
        {
          double ele = ((Double)element).doubleValue();
          if (!TLongDoubleHashMap.this.containsValue(ele)) {
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
    
    public boolean containsAll(TDoubleCollection collection)
    {
      TDoubleIterator iter = collection.iterator();
      while (iter.hasNext()) {
        if (!TLongDoubleHashMap.this.containsValue(iter.next())) {
          return false;
        }
      }
      return true;
    }
    
    public boolean containsAll(double[] array)
    {
      for (double element : array) {
        if (!TLongDoubleHashMap.this.containsValue(element)) {
          return false;
        }
      }
      return true;
    }
    
    public boolean addAll(Collection<? extends Double> collection)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean addAll(TDoubleCollection collection)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean addAll(double[] array)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean retainAll(Collection<?> collection)
    {
      boolean modified = false;
      TDoubleIterator iter = iterator();
      while (iter.hasNext()) {
        if (!collection.contains(Double.valueOf(iter.next())))
        {
          iter.remove();
          modified = true;
        }
      }
      return modified;
    }
    
    public boolean retainAll(TDoubleCollection collection)
    {
      if (this == collection) {
        return false;
      }
      boolean modified = false;
      TDoubleIterator iter = iterator();
      while (iter.hasNext()) {
        if (!collection.contains(iter.next()))
        {
          iter.remove();
          modified = true;
        }
      }
      return modified;
    }
    
    public boolean retainAll(double[] array)
    {
      boolean changed = false;
      Arrays.sort(array);
      double[] values = TLongDoubleHashMap.this._values;
      byte[] states = TLongDoubleHashMap.this._states;
      for (int i = values.length; i-- > 0;) {
        if ((states[i] == 1) && (Arrays.binarySearch(array, values[i]) < 0))
        {
          TLongDoubleHashMap.this.removeAt(i);
          changed = true;
        }
      }
      return changed;
    }
    
    public boolean removeAll(Collection<?> collection)
    {
      boolean changed = false;
      for (Object element : collection) {
        if ((element instanceof Double))
        {
          double c = ((Double)element).doubleValue();
          if (remove(c)) {
            changed = true;
          }
        }
      }
      return changed;
    }
    
    public boolean removeAll(TDoubleCollection collection)
    {
      if (this == collection)
      {
        clear();
        return true;
      }
      boolean changed = false;
      TDoubleIterator iter = collection.iterator();
      while (iter.hasNext())
      {
        double element = iter.next();
        if (remove(element)) {
          changed = true;
        }
      }
      return changed;
    }
    
    public boolean removeAll(double[] array)
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
      TLongDoubleHashMap.this.clear();
    }
    
    public boolean forEach(TDoubleProcedure procedure)
    {
      return TLongDoubleHashMap.this.forEachValue(procedure);
    }
    
    public String toString()
    {
      final StringBuilder buf = new StringBuilder("{");
      TLongDoubleHashMap.this.forEachValue(new TDoubleProcedure()
      {
        private boolean first = true;
        
        public boolean execute(double value)
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
  
  class TLongDoubleKeyHashIterator
    extends THashPrimitiveIterator
    implements TLongIterator
  {
    TLongDoubleKeyHashIterator(TPrimitiveHash hash)
    {
      super();
    }
    
    public long next()
    {
      moveToNextIndex();
      return TLongDoubleHashMap.this._set[this._index];
    }
    
    public void remove()
    {
      if (this._expectedSize != this._hash.size()) {
        throw new ConcurrentModificationException();
      }
      try
      {
        this._hash.tempDisableAutoCompaction();
        TLongDoubleHashMap.this.removeAt(this._index);
      }
      finally
      {
        this._hash.reenableAutoCompaction(false);
      }
      this._expectedSize -= 1;
    }
  }
  
  class TLongDoubleValueHashIterator
    extends THashPrimitiveIterator
    implements TDoubleIterator
  {
    TLongDoubleValueHashIterator(TPrimitiveHash hash)
    {
      super();
    }
    
    public double next()
    {
      moveToNextIndex();
      return TLongDoubleHashMap.this._values[this._index];
    }
    
    public void remove()
    {
      if (this._expectedSize != this._hash.size()) {
        throw new ConcurrentModificationException();
      }
      try
      {
        this._hash.tempDisableAutoCompaction();
        TLongDoubleHashMap.this.removeAt(this._index);
      }
      finally
      {
        this._hash.reenableAutoCompaction(false);
      }
      this._expectedSize -= 1;
    }
  }
  
  class TLongDoubleHashIterator
    extends THashPrimitiveIterator
    implements TLongDoubleIterator
  {
    TLongDoubleHashIterator(TLongDoubleHashMap map)
    {
      super();
    }
    
    public void advance()
    {
      moveToNextIndex();
    }
    
    public long key()
    {
      return TLongDoubleHashMap.this._set[this._index];
    }
    
    public double value()
    {
      return TLongDoubleHashMap.this._values[this._index];
    }
    
    public double setValue(double val)
    {
      double old = value();
      TLongDoubleHashMap.this._values[this._index] = val;
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
        TLongDoubleHashMap.this.removeAt(this._index);
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
    if (!(other instanceof TLongDoubleMap)) {
      return false;
    }
    TLongDoubleMap that = (TLongDoubleMap)other;
    if (that.size() != size()) {
      return false;
    }
    double[] values = this._values;
    byte[] states = this._states;
    double this_no_entry_value = getNoEntryValue();
    double that_no_entry_value = that.getNoEntryValue();
    for (int i = values.length; i-- > 0;) {
      if (states[i] == 1)
      {
        long key = this._set[i];
        double that_value = that.get(key);
        double this_value = values[i];
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
    forEachEntry(new TLongDoubleProcedure()
    {
      private boolean first = true;
      
      public boolean execute(long key, double value)
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
        out.writeDouble(this._values[i]);
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
      double val = in.readDouble();
      put(key, val);
    }
  }
}
