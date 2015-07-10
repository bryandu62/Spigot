package gnu.trove.map.hash;

import gnu.trove.TCharCollection;
import gnu.trove.TShortCollection;
import gnu.trove.function.TCharFunction;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.THashPrimitiveIterator;
import gnu.trove.impl.hash.TPrimitiveHash;
import gnu.trove.impl.hash.TShortCharHash;
import gnu.trove.iterator.TCharIterator;
import gnu.trove.iterator.TShortCharIterator;
import gnu.trove.iterator.TShortIterator;
import gnu.trove.map.TShortCharMap;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TShortCharProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TShortSet;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.Map.Entry;

public class TShortCharHashMap
  extends TShortCharHash
  implements TShortCharMap, Externalizable
{
  static final long serialVersionUID = 1L;
  protected transient char[] _values;
  
  public TShortCharHashMap() {}
  
  public TShortCharHashMap(int initialCapacity)
  {
    super(initialCapacity);
  }
  
  public TShortCharHashMap(int initialCapacity, float loadFactor)
  {
    super(initialCapacity, loadFactor);
  }
  
  public TShortCharHashMap(int initialCapacity, float loadFactor, short noEntryKey, char noEntryValue)
  {
    super(initialCapacity, loadFactor, noEntryKey, noEntryValue);
  }
  
  public TShortCharHashMap(short[] keys, char[] values)
  {
    super(Math.max(keys.length, values.length));
    
    int size = Math.min(keys.length, values.length);
    for (int i = 0; i < size; i++) {
      put(keys[i], values[i]);
    }
  }
  
  public TShortCharHashMap(TShortCharMap map)
  {
    super(map.size());
    if ((map instanceof TShortCharHashMap))
    {
      TShortCharHashMap hashmap = (TShortCharHashMap)map;
      this._loadFactor = hashmap._loadFactor;
      this.no_entry_key = hashmap.no_entry_key;
      this.no_entry_value = hashmap.no_entry_value;
      if (this.no_entry_key != 0) {
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
    this._values = new char[capacity];
    return capacity;
  }
  
  protected void rehash(int newCapacity)
  {
    int oldCapacity = this._set.length;
    
    short[] oldKeys = this._set;
    char[] oldVals = this._values;
    byte[] oldStates = this._states;
    
    this._set = new short[newCapacity];
    this._values = new char[newCapacity];
    this._states = new byte[newCapacity];
    for (int i = oldCapacity; i-- > 0;) {
      if (oldStates[i] == 1)
      {
        short o = oldKeys[i];
        int index = insertKey(o);
        this._values[index] = oldVals[i];
      }
    }
  }
  
  public char put(short key, char value)
  {
    int index = insertKey(key);
    return doPut(key, value, index);
  }
  
  public char putIfAbsent(short key, char value)
  {
    int index = insertKey(key);
    if (index < 0) {
      return this._values[(-index - 1)];
    }
    return doPut(key, value, index);
  }
  
  private char doPut(short key, char value, int index)
  {
    char previous = this.no_entry_value;
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
  
  public void putAll(Map<? extends Short, ? extends Character> map)
  {
    ensureCapacity(map.size());
    for (Map.Entry<? extends Short, ? extends Character> entry : map.entrySet()) {
      put(((Short)entry.getKey()).shortValue(), ((Character)entry.getValue()).charValue());
    }
  }
  
  public void putAll(TShortCharMap map)
  {
    ensureCapacity(map.size());
    TShortCharIterator iter = map.iterator();
    while (iter.hasNext())
    {
      iter.advance();
      put(iter.key(), iter.value());
    }
  }
  
  public char get(short key)
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
  
  public char remove(short key)
  {
    char prev = this.no_entry_value;
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
  
  public TShortSet keySet()
  {
    return new TKeyView();
  }
  
  public short[] keys()
  {
    short[] keys = new short[size()];
    short[] k = this._set;
    byte[] states = this._states;
    
    int i = k.length;
    for (int j = 0; i-- > 0;) {
      if (states[i] == 1) {
        keys[(j++)] = k[i];
      }
    }
    return keys;
  }
  
  public short[] keys(short[] array)
  {
    int size = size();
    if (array.length < size) {
      array = new short[size];
    }
    short[] keys = this._set;
    byte[] states = this._states;
    
    int i = keys.length;
    for (int j = 0; i-- > 0;) {
      if (states[i] == 1) {
        array[(j++)] = keys[i];
      }
    }
    return array;
  }
  
  public TCharCollection valueCollection()
  {
    return new TValueView();
  }
  
  public char[] values()
  {
    char[] vals = new char[size()];
    char[] v = this._values;
    byte[] states = this._states;
    
    int i = v.length;
    for (int j = 0; i-- > 0;) {
      if (states[i] == 1) {
        vals[(j++)] = v[i];
      }
    }
    return vals;
  }
  
  public char[] values(char[] array)
  {
    int size = size();
    if (array.length < size) {
      array = new char[size];
    }
    char[] v = this._values;
    byte[] states = this._states;
    
    int i = v.length;
    for (int j = 0; i-- > 0;) {
      if (states[i] == 1) {
        array[(j++)] = v[i];
      }
    }
    return array;
  }
  
  public boolean containsValue(char val)
  {
    byte[] states = this._states;
    char[] vals = this._values;
    for (int i = vals.length; i-- > 0;) {
      if ((states[i] == 1) && (val == vals[i])) {
        return true;
      }
    }
    return false;
  }
  
  public boolean containsKey(short key)
  {
    return contains(key);
  }
  
  public TShortCharIterator iterator()
  {
    return new TShortCharHashIterator(this);
  }
  
  public boolean forEachKey(TShortProcedure procedure)
  {
    return forEach(procedure);
  }
  
  public boolean forEachValue(TCharProcedure procedure)
  {
    byte[] states = this._states;
    char[] values = this._values;
    for (int i = values.length; i-- > 0;) {
      if ((states[i] == 1) && (!procedure.execute(values[i]))) {
        return false;
      }
    }
    return true;
  }
  
  public boolean forEachEntry(TShortCharProcedure procedure)
  {
    byte[] states = this._states;
    short[] keys = this._set;
    char[] values = this._values;
    for (int i = keys.length; i-- > 0;) {
      if ((states[i] == 1) && (!procedure.execute(keys[i], values[i]))) {
        return false;
      }
    }
    return true;
  }
  
  public void transformValues(TCharFunction function)
  {
    byte[] states = this._states;
    char[] values = this._values;
    for (int i = values.length; i-- > 0;) {
      if (states[i] == 1) {
        values[i] = function.execute(values[i]);
      }
    }
  }
  
  public boolean retainEntries(TShortCharProcedure procedure)
  {
    boolean modified = false;
    byte[] states = this._states;
    short[] keys = this._set;
    char[] values = this._values;
    
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
  
  public boolean increment(short key)
  {
    return adjustValue(key, '\001');
  }
  
  public boolean adjustValue(short key, char amount)
  {
    int index = index(key);
    if (index < 0) {
      return false;
    }
    int tmp17_16 = index; char[] tmp17_13 = this._values;tmp17_13[tmp17_16] = ((char)(tmp17_13[tmp17_16] + amount));
    return true;
  }
  
  public char adjustOrPutValue(short key, char adjust_amount, char put_amount)
  {
    int index = insertKey(key);
    boolean isNewMapping;
    char newValue;
    boolean isNewMapping;
    if (index < 0)
    {
      index = -index - 1; int 
        tmp25_23 = index; char[] tmp25_20 = this._values;char newValue = tmp25_20[tmp25_23] = (char)(tmp25_20[tmp25_23] + adjust_amount);
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
    implements TShortSet
  {
    protected TKeyView() {}
    
    public TShortIterator iterator()
    {
      return new TShortCharHashMap.TShortCharKeyHashIterator(TShortCharHashMap.this, TShortCharHashMap.this);
    }
    
    public short getNoEntryValue()
    {
      return TShortCharHashMap.this.no_entry_key;
    }
    
    public int size()
    {
      return TShortCharHashMap.this._size;
    }
    
    public boolean isEmpty()
    {
      return 0 == TShortCharHashMap.this._size;
    }
    
    public boolean contains(short entry)
    {
      return TShortCharHashMap.this.contains(entry);
    }
    
    public short[] toArray()
    {
      return TShortCharHashMap.this.keys();
    }
    
    public short[] toArray(short[] dest)
    {
      return TShortCharHashMap.this.keys(dest);
    }
    
    public boolean add(short entry)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean remove(short entry)
    {
      return TShortCharHashMap.this.no_entry_value != TShortCharHashMap.this.remove(entry);
    }
    
    public boolean containsAll(Collection<?> collection)
    {
      for (Object element : collection) {
        if ((element instanceof Short))
        {
          short ele = ((Short)element).shortValue();
          if (!TShortCharHashMap.this.containsKey(ele)) {
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
        if (!TShortCharHashMap.this.containsKey(iter.next())) {
          return false;
        }
      }
      return true;
    }
    
    public boolean containsAll(short[] array)
    {
      for (short element : array) {
        if (!TShortCharHashMap.this.contains(element)) {
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
      short[] set = TShortCharHashMap.this._set;
      byte[] states = TShortCharHashMap.this._states;
      for (int i = set.length; i-- > 0;) {
        if ((states[i] == 1) && (Arrays.binarySearch(array, set[i]) < 0))
        {
          TShortCharHashMap.this.removeAt(i);
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
      TShortCharHashMap.this.clear();
    }
    
    public boolean forEach(TShortProcedure procedure)
    {
      return TShortCharHashMap.this.forEachKey(procedure);
    }
    
    public boolean equals(Object other)
    {
      if (!(other instanceof TShortSet)) {
        return false;
      }
      TShortSet that = (TShortSet)other;
      if (that.size() != size()) {
        return false;
      }
      for (int i = TShortCharHashMap.this._states.length; i-- > 0;) {
        if ((TShortCharHashMap.this._states[i] == 1) && 
          (!that.contains(TShortCharHashMap.this._set[i]))) {
          return false;
        }
      }
      return true;
    }
    
    public int hashCode()
    {
      int hashcode = 0;
      for (int i = TShortCharHashMap.this._states.length; i-- > 0;) {
        if (TShortCharHashMap.this._states[i] == 1) {
          hashcode += HashFunctions.hash(TShortCharHashMap.this._set[i]);
        }
      }
      return hashcode;
    }
    
    public String toString()
    {
      final StringBuilder buf = new StringBuilder("{");
      TShortCharHashMap.this.forEachKey(new TShortProcedure()
      {
        private boolean first = true;
        
        public boolean execute(short key)
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
    implements TCharCollection
  {
    protected TValueView() {}
    
    public TCharIterator iterator()
    {
      return new TShortCharHashMap.TShortCharValueHashIterator(TShortCharHashMap.this, TShortCharHashMap.this);
    }
    
    public char getNoEntryValue()
    {
      return TShortCharHashMap.this.no_entry_value;
    }
    
    public int size()
    {
      return TShortCharHashMap.this._size;
    }
    
    public boolean isEmpty()
    {
      return 0 == TShortCharHashMap.this._size;
    }
    
    public boolean contains(char entry)
    {
      return TShortCharHashMap.this.containsValue(entry);
    }
    
    public char[] toArray()
    {
      return TShortCharHashMap.this.values();
    }
    
    public char[] toArray(char[] dest)
    {
      return TShortCharHashMap.this.values(dest);
    }
    
    public boolean add(char entry)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean remove(char entry)
    {
      char[] values = TShortCharHashMap.this._values;
      short[] set = TShortCharHashMap.this._set;
      for (int i = values.length; i-- > 0;) {
        if ((set[i] != 0) && (set[i] != 2) && (entry == values[i]))
        {
          TShortCharHashMap.this.removeAt(i);
          return true;
        }
      }
      return false;
    }
    
    public boolean containsAll(Collection<?> collection)
    {
      for (Object element : collection) {
        if ((element instanceof Character))
        {
          char ele = ((Character)element).charValue();
          if (!TShortCharHashMap.this.containsValue(ele)) {
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
    
    public boolean containsAll(TCharCollection collection)
    {
      TCharIterator iter = collection.iterator();
      while (iter.hasNext()) {
        if (!TShortCharHashMap.this.containsValue(iter.next())) {
          return false;
        }
      }
      return true;
    }
    
    public boolean containsAll(char[] array)
    {
      for (char element : array) {
        if (!TShortCharHashMap.this.containsValue(element)) {
          return false;
        }
      }
      return true;
    }
    
    public boolean addAll(Collection<? extends Character> collection)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean addAll(TCharCollection collection)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean addAll(char[] array)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean retainAll(Collection<?> collection)
    {
      boolean modified = false;
      TCharIterator iter = iterator();
      while (iter.hasNext()) {
        if (!collection.contains(Character.valueOf(iter.next())))
        {
          iter.remove();
          modified = true;
        }
      }
      return modified;
    }
    
    public boolean retainAll(TCharCollection collection)
    {
      if (this == collection) {
        return false;
      }
      boolean modified = false;
      TCharIterator iter = iterator();
      while (iter.hasNext()) {
        if (!collection.contains(iter.next()))
        {
          iter.remove();
          modified = true;
        }
      }
      return modified;
    }
    
    public boolean retainAll(char[] array)
    {
      boolean changed = false;
      Arrays.sort(array);
      char[] values = TShortCharHashMap.this._values;
      byte[] states = TShortCharHashMap.this._states;
      for (int i = values.length; i-- > 0;) {
        if ((states[i] == 1) && (Arrays.binarySearch(array, values[i]) < 0))
        {
          TShortCharHashMap.this.removeAt(i);
          changed = true;
        }
      }
      return changed;
    }
    
    public boolean removeAll(Collection<?> collection)
    {
      boolean changed = false;
      for (Object element : collection) {
        if ((element instanceof Character))
        {
          char c = ((Character)element).charValue();
          if (remove(c)) {
            changed = true;
          }
        }
      }
      return changed;
    }
    
    public boolean removeAll(TCharCollection collection)
    {
      if (this == collection)
      {
        clear();
        return true;
      }
      boolean changed = false;
      TCharIterator iter = collection.iterator();
      while (iter.hasNext())
      {
        char element = iter.next();
        if (remove(element)) {
          changed = true;
        }
      }
      return changed;
    }
    
    public boolean removeAll(char[] array)
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
      TShortCharHashMap.this.clear();
    }
    
    public boolean forEach(TCharProcedure procedure)
    {
      return TShortCharHashMap.this.forEachValue(procedure);
    }
    
    public String toString()
    {
      final StringBuilder buf = new StringBuilder("{");
      TShortCharHashMap.this.forEachValue(new TCharProcedure()
      {
        private boolean first = true;
        
        public boolean execute(char value)
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
  
  class TShortCharKeyHashIterator
    extends THashPrimitiveIterator
    implements TShortIterator
  {
    TShortCharKeyHashIterator(TPrimitiveHash hash)
    {
      super();
    }
    
    public short next()
    {
      moveToNextIndex();
      return TShortCharHashMap.this._set[this._index];
    }
    
    public void remove()
    {
      if (this._expectedSize != this._hash.size()) {
        throw new ConcurrentModificationException();
      }
      try
      {
        this._hash.tempDisableAutoCompaction();
        TShortCharHashMap.this.removeAt(this._index);
      }
      finally
      {
        this._hash.reenableAutoCompaction(false);
      }
      this._expectedSize -= 1;
    }
  }
  
  class TShortCharValueHashIterator
    extends THashPrimitiveIterator
    implements TCharIterator
  {
    TShortCharValueHashIterator(TPrimitiveHash hash)
    {
      super();
    }
    
    public char next()
    {
      moveToNextIndex();
      return TShortCharHashMap.this._values[this._index];
    }
    
    public void remove()
    {
      if (this._expectedSize != this._hash.size()) {
        throw new ConcurrentModificationException();
      }
      try
      {
        this._hash.tempDisableAutoCompaction();
        TShortCharHashMap.this.removeAt(this._index);
      }
      finally
      {
        this._hash.reenableAutoCompaction(false);
      }
      this._expectedSize -= 1;
    }
  }
  
  class TShortCharHashIterator
    extends THashPrimitiveIterator
    implements TShortCharIterator
  {
    TShortCharHashIterator(TShortCharHashMap map)
    {
      super();
    }
    
    public void advance()
    {
      moveToNextIndex();
    }
    
    public short key()
    {
      return TShortCharHashMap.this._set[this._index];
    }
    
    public char value()
    {
      return TShortCharHashMap.this._values[this._index];
    }
    
    public char setValue(char val)
    {
      char old = value();
      TShortCharHashMap.this._values[this._index] = val;
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
        TShortCharHashMap.this.removeAt(this._index);
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
    if (!(other instanceof TShortCharMap)) {
      return false;
    }
    TShortCharMap that = (TShortCharMap)other;
    if (that.size() != size()) {
      return false;
    }
    char[] values = this._values;
    byte[] states = this._states;
    char this_no_entry_value = getNoEntryValue();
    char that_no_entry_value = that.getNoEntryValue();
    for (int i = values.length; i-- > 0;) {
      if (states[i] == 1)
      {
        short key = this._set[i];
        char that_value = that.get(key);
        char this_value = values[i];
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
    forEachEntry(new TShortCharProcedure()
    {
      private boolean first = true;
      
      public boolean execute(short key, char value)
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
        out.writeShort(this._set[i]);
        out.writeChar(this._values[i]);
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
      short key = in.readShort();
      char val = in.readChar();
      put(key, val);
    }
  }
}
