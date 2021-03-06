package gnu.trove.map.hash;

import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.TFloatFloatHash;
import gnu.trove.impl.hash.THashPrimitiveIterator;
import gnu.trove.impl.hash.TPrimitiveHash;
import gnu.trove.iterator.TFloatFloatIterator;
import gnu.trove.iterator.TFloatIterator;
import gnu.trove.map.TFloatFloatMap;
import gnu.trove.procedure.TFloatFloatProcedure;
import gnu.trove.procedure.TFloatProcedure;
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

public class TFloatFloatHashMap
  extends TFloatFloatHash
  implements TFloatFloatMap, Externalizable
{
  static final long serialVersionUID = 1L;
  protected transient float[] _values;
  
  public TFloatFloatHashMap() {}
  
  public TFloatFloatHashMap(int initialCapacity)
  {
    super(initialCapacity);
  }
  
  public TFloatFloatHashMap(int initialCapacity, float loadFactor)
  {
    super(initialCapacity, loadFactor);
  }
  
  public TFloatFloatHashMap(int initialCapacity, float loadFactor, float noEntryKey, float noEntryValue)
  {
    super(initialCapacity, loadFactor, noEntryKey, noEntryValue);
  }
  
  public TFloatFloatHashMap(float[] keys, float[] values)
  {
    super(Math.max(keys.length, values.length));
    
    int size = Math.min(keys.length, values.length);
    for (int i = 0; i < size; i++) {
      put(keys[i], values[i]);
    }
  }
  
  public TFloatFloatHashMap(TFloatFloatMap map)
  {
    super(map.size());
    if ((map instanceof TFloatFloatHashMap))
    {
      TFloatFloatHashMap hashmap = (TFloatFloatHashMap)map;
      this._loadFactor = hashmap._loadFactor;
      this.no_entry_key = hashmap.no_entry_key;
      this.no_entry_value = hashmap.no_entry_value;
      if (this.no_entry_key != 0.0F) {
        Arrays.fill(this._set, this.no_entry_key);
      }
      if (this.no_entry_value != 0.0F) {
        Arrays.fill(this._values, this.no_entry_value);
      }
      setUp((int)Math.ceil(10.0F / this._loadFactor));
    }
    putAll(map);
  }
  
  protected int setUp(int initialCapacity)
  {
    int capacity = super.setUp(initialCapacity);
    this._values = new float[capacity];
    return capacity;
  }
  
  protected void rehash(int newCapacity)
  {
    int oldCapacity = this._set.length;
    
    float[] oldKeys = this._set;
    float[] oldVals = this._values;
    byte[] oldStates = this._states;
    
    this._set = new float[newCapacity];
    this._values = new float[newCapacity];
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
  
  public float put(float key, float value)
  {
    int index = insertKey(key);
    return doPut(key, value, index);
  }
  
  public float putIfAbsent(float key, float value)
  {
    int index = insertKey(key);
    if (index < 0) {
      return this._values[(-index - 1)];
    }
    return doPut(key, value, index);
  }
  
  private float doPut(float key, float value, int index)
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
  
  public void putAll(Map<? extends Float, ? extends Float> map)
  {
    ensureCapacity(map.size());
    for (Map.Entry<? extends Float, ? extends Float> entry : map.entrySet()) {
      put(((Float)entry.getKey()).floatValue(), ((Float)entry.getValue()).floatValue());
    }
  }
  
  public void putAll(TFloatFloatMap map)
  {
    ensureCapacity(map.size());
    TFloatFloatIterator iter = map.iterator();
    while (iter.hasNext())
    {
      iter.advance();
      put(iter.key(), iter.value());
    }
  }
  
  public float get(float key)
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
  
  public float remove(float key)
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
  
  public TFloatCollection valueCollection()
  {
    return new TValueView();
  }
  
  public float[] values()
  {
    float[] vals = new float[size()];
    float[] v = this._values;
    byte[] states = this._states;
    
    int i = v.length;
    for (int j = 0; i-- > 0;) {
      if (states[i] == 1) {
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
    byte[] states = this._states;
    
    int i = v.length;
    for (int j = 0; i-- > 0;) {
      if (states[i] == 1) {
        array[(j++)] = v[i];
      }
    }
    return array;
  }
  
  public boolean containsValue(float val)
  {
    byte[] states = this._states;
    float[] vals = this._values;
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
  
  public TFloatFloatIterator iterator()
  {
    return new TFloatFloatHashIterator(this);
  }
  
  public boolean forEachKey(TFloatProcedure procedure)
  {
    return forEach(procedure);
  }
  
  public boolean forEachValue(TFloatProcedure procedure)
  {
    byte[] states = this._states;
    float[] values = this._values;
    for (int i = values.length; i-- > 0;) {
      if ((states[i] == 1) && (!procedure.execute(values[i]))) {
        return false;
      }
    }
    return true;
  }
  
  public boolean forEachEntry(TFloatFloatProcedure procedure)
  {
    byte[] states = this._states;
    float[] keys = this._set;
    float[] values = this._values;
    for (int i = keys.length; i-- > 0;) {
      if ((states[i] == 1) && (!procedure.execute(keys[i], values[i]))) {
        return false;
      }
    }
    return true;
  }
  
  public void transformValues(TFloatFunction function)
  {
    byte[] states = this._states;
    float[] values = this._values;
    for (int i = values.length; i-- > 0;) {
      if (states[i] == 1) {
        values[i] = function.execute(values[i]);
      }
    }
  }
  
  public boolean retainEntries(TFloatFloatProcedure procedure)
  {
    boolean modified = false;
    byte[] states = this._states;
    float[] keys = this._set;
    float[] values = this._values;
    
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
    return adjustValue(key, 1.0F);
  }
  
  public boolean adjustValue(float key, float amount)
  {
    int index = index(key);
    if (index < 0) {
      return false;
    }
    this._values[index] += amount;
    return true;
  }
  
  public float adjustOrPutValue(float key, float adjust_amount, float put_amount)
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
      return new TFloatFloatHashMap.TFloatFloatKeyHashIterator(TFloatFloatHashMap.this, TFloatFloatHashMap.this);
    }
    
    public float getNoEntryValue()
    {
      return TFloatFloatHashMap.this.no_entry_key;
    }
    
    public int size()
    {
      return TFloatFloatHashMap.this._size;
    }
    
    public boolean isEmpty()
    {
      return 0 == TFloatFloatHashMap.this._size;
    }
    
    public boolean contains(float entry)
    {
      return TFloatFloatHashMap.this.contains(entry);
    }
    
    public float[] toArray()
    {
      return TFloatFloatHashMap.this.keys();
    }
    
    public float[] toArray(float[] dest)
    {
      return TFloatFloatHashMap.this.keys(dest);
    }
    
    public boolean add(float entry)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean remove(float entry)
    {
      return TFloatFloatHashMap.this.no_entry_value != TFloatFloatHashMap.this.remove(entry);
    }
    
    public boolean containsAll(Collection<?> collection)
    {
      for (Object element : collection) {
        if ((element instanceof Float))
        {
          float ele = ((Float)element).floatValue();
          if (!TFloatFloatHashMap.this.containsKey(ele)) {
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
        if (!TFloatFloatHashMap.this.containsKey(iter.next())) {
          return false;
        }
      }
      return true;
    }
    
    public boolean containsAll(float[] array)
    {
      for (float element : array) {
        if (!TFloatFloatHashMap.this.contains(element)) {
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
      float[] set = TFloatFloatHashMap.this._set;
      byte[] states = TFloatFloatHashMap.this._states;
      for (int i = set.length; i-- > 0;) {
        if ((states[i] == 1) && (Arrays.binarySearch(array, set[i]) < 0))
        {
          TFloatFloatHashMap.this.removeAt(i);
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
      TFloatFloatHashMap.this.clear();
    }
    
    public boolean forEach(TFloatProcedure procedure)
    {
      return TFloatFloatHashMap.this.forEachKey(procedure);
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
      for (int i = TFloatFloatHashMap.this._states.length; i-- > 0;) {
        if ((TFloatFloatHashMap.this._states[i] == 1) && 
          (!that.contains(TFloatFloatHashMap.this._set[i]))) {
          return false;
        }
      }
      return true;
    }
    
    public int hashCode()
    {
      int hashcode = 0;
      for (int i = TFloatFloatHashMap.this._states.length; i-- > 0;) {
        if (TFloatFloatHashMap.this._states[i] == 1) {
          hashcode += HashFunctions.hash(TFloatFloatHashMap.this._set[i]);
        }
      }
      return hashcode;
    }
    
    public String toString()
    {
      final StringBuilder buf = new StringBuilder("{");
      TFloatFloatHashMap.this.forEachKey(new TFloatProcedure()
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
    implements TFloatCollection
  {
    protected TValueView() {}
    
    public TFloatIterator iterator()
    {
      return new TFloatFloatHashMap.TFloatFloatValueHashIterator(TFloatFloatHashMap.this, TFloatFloatHashMap.this);
    }
    
    public float getNoEntryValue()
    {
      return TFloatFloatHashMap.this.no_entry_value;
    }
    
    public int size()
    {
      return TFloatFloatHashMap.this._size;
    }
    
    public boolean isEmpty()
    {
      return 0 == TFloatFloatHashMap.this._size;
    }
    
    public boolean contains(float entry)
    {
      return TFloatFloatHashMap.this.containsValue(entry);
    }
    
    public float[] toArray()
    {
      return TFloatFloatHashMap.this.values();
    }
    
    public float[] toArray(float[] dest)
    {
      return TFloatFloatHashMap.this.values(dest);
    }
    
    public boolean add(float entry)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean remove(float entry)
    {
      float[] values = TFloatFloatHashMap.this._values;
      float[] set = TFloatFloatHashMap.this._set;
      for (int i = values.length; i-- > 0;) {
        if ((set[i] != 0.0F) && (set[i] != 2.0F) && (entry == values[i]))
        {
          TFloatFloatHashMap.this.removeAt(i);
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
          if (!TFloatFloatHashMap.this.containsValue(ele)) {
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
        if (!TFloatFloatHashMap.this.containsValue(iter.next())) {
          return false;
        }
      }
      return true;
    }
    
    public boolean containsAll(float[] array)
    {
      for (float element : array) {
        if (!TFloatFloatHashMap.this.containsValue(element)) {
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
      float[] values = TFloatFloatHashMap.this._values;
      byte[] states = TFloatFloatHashMap.this._states;
      for (int i = values.length; i-- > 0;) {
        if ((states[i] == 1) && (Arrays.binarySearch(array, values[i]) < 0))
        {
          TFloatFloatHashMap.this.removeAt(i);
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
      TFloatFloatHashMap.this.clear();
    }
    
    public boolean forEach(TFloatProcedure procedure)
    {
      return TFloatFloatHashMap.this.forEachValue(procedure);
    }
    
    public String toString()
    {
      final StringBuilder buf = new StringBuilder("{");
      TFloatFloatHashMap.this.forEachValue(new TFloatProcedure()
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
  }
  
  class TFloatFloatKeyHashIterator
    extends THashPrimitiveIterator
    implements TFloatIterator
  {
    TFloatFloatKeyHashIterator(TPrimitiveHash hash)
    {
      super();
    }
    
    public float next()
    {
      moveToNextIndex();
      return TFloatFloatHashMap.this._set[this._index];
    }
    
    public void remove()
    {
      if (this._expectedSize != this._hash.size()) {
        throw new ConcurrentModificationException();
      }
      try
      {
        this._hash.tempDisableAutoCompaction();
        TFloatFloatHashMap.this.removeAt(this._index);
      }
      finally
      {
        this._hash.reenableAutoCompaction(false);
      }
      this._expectedSize -= 1;
    }
  }
  
  class TFloatFloatValueHashIterator
    extends THashPrimitiveIterator
    implements TFloatIterator
  {
    TFloatFloatValueHashIterator(TPrimitiveHash hash)
    {
      super();
    }
    
    public float next()
    {
      moveToNextIndex();
      return TFloatFloatHashMap.this._values[this._index];
    }
    
    public void remove()
    {
      if (this._expectedSize != this._hash.size()) {
        throw new ConcurrentModificationException();
      }
      try
      {
        this._hash.tempDisableAutoCompaction();
        TFloatFloatHashMap.this.removeAt(this._index);
      }
      finally
      {
        this._hash.reenableAutoCompaction(false);
      }
      this._expectedSize -= 1;
    }
  }
  
  class TFloatFloatHashIterator
    extends THashPrimitiveIterator
    implements TFloatFloatIterator
  {
    TFloatFloatHashIterator(TFloatFloatHashMap map)
    {
      super();
    }
    
    public void advance()
    {
      moveToNextIndex();
    }
    
    public float key()
    {
      return TFloatFloatHashMap.this._set[this._index];
    }
    
    public float value()
    {
      return TFloatFloatHashMap.this._values[this._index];
    }
    
    public float setValue(float val)
    {
      float old = value();
      TFloatFloatHashMap.this._values[this._index] = val;
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
        TFloatFloatHashMap.this.removeAt(this._index);
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
    if (!(other instanceof TFloatFloatMap)) {
      return false;
    }
    TFloatFloatMap that = (TFloatFloatMap)other;
    if (that.size() != size()) {
      return false;
    }
    float[] values = this._values;
    byte[] states = this._states;
    float this_no_entry_value = getNoEntryValue();
    float that_no_entry_value = that.getNoEntryValue();
    for (int i = values.length; i-- > 0;) {
      if (states[i] == 1)
      {
        float key = this._set[i];
        float that_value = that.get(key);
        float this_value = values[i];
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
    forEachEntry(new TFloatFloatProcedure()
    {
      private boolean first = true;
      
      public boolean execute(float key, float value)
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
        out.writeFloat(this._values[i]);
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
      float val = in.readFloat();
      put(key, val);
    }
  }
}
