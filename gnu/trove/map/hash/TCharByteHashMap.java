package gnu.trove.map.hash;

import gnu.trove.TByteCollection;
import gnu.trove.TCharCollection;
import gnu.trove.function.TByteFunction;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.TCharByteHash;
import gnu.trove.impl.hash.THashPrimitiveIterator;
import gnu.trove.impl.hash.TPrimitiveHash;
import gnu.trove.iterator.TByteIterator;
import gnu.trove.iterator.TCharByteIterator;
import gnu.trove.iterator.TCharIterator;
import gnu.trove.map.TCharByteMap;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TCharByteProcedure;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.set.TCharSet;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.Map.Entry;

public class TCharByteHashMap
  extends TCharByteHash
  implements TCharByteMap, Externalizable
{
  static final long serialVersionUID = 1L;
  protected transient byte[] _values;
  
  public TCharByteHashMap() {}
  
  public TCharByteHashMap(int initialCapacity)
  {
    super(initialCapacity);
  }
  
  public TCharByteHashMap(int initialCapacity, float loadFactor)
  {
    super(initialCapacity, loadFactor);
  }
  
  public TCharByteHashMap(int initialCapacity, float loadFactor, char noEntryKey, byte noEntryValue)
  {
    super(initialCapacity, loadFactor, noEntryKey, noEntryValue);
  }
  
  public TCharByteHashMap(char[] keys, byte[] values)
  {
    super(Math.max(keys.length, values.length));
    
    int size = Math.min(keys.length, values.length);
    for (int i = 0; i < size; i++) {
      put(keys[i], values[i]);
    }
  }
  
  public TCharByteHashMap(TCharByteMap map)
  {
    super(map.size());
    if ((map instanceof TCharByteHashMap))
    {
      TCharByteHashMap hashmap = (TCharByteHashMap)map;
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
    this._values = new byte[capacity];
    return capacity;
  }
  
  protected void rehash(int newCapacity)
  {
    int oldCapacity = this._set.length;
    
    char[] oldKeys = this._set;
    byte[] oldVals = this._values;
    byte[] oldStates = this._states;
    
    this._set = new char[newCapacity];
    this._values = new byte[newCapacity];
    this._states = new byte[newCapacity];
    for (int i = oldCapacity; i-- > 0;) {
      if (oldStates[i] == 1)
      {
        char o = oldKeys[i];
        int index = insertKey(o);
        this._values[index] = oldVals[i];
      }
    }
  }
  
  public byte put(char key, byte value)
  {
    int index = insertKey(key);
    return doPut(key, value, index);
  }
  
  public byte putIfAbsent(char key, byte value)
  {
    int index = insertKey(key);
    if (index < 0) {
      return this._values[(-index - 1)];
    }
    return doPut(key, value, index);
  }
  
  private byte doPut(char key, byte value, int index)
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
  
  public void putAll(Map<? extends Character, ? extends Byte> map)
  {
    ensureCapacity(map.size());
    for (Map.Entry<? extends Character, ? extends Byte> entry : map.entrySet()) {
      put(((Character)entry.getKey()).charValue(), ((Byte)entry.getValue()).byteValue());
    }
  }
  
  public void putAll(TCharByteMap map)
  {
    ensureCapacity(map.size());
    TCharByteIterator iter = map.iterator();
    while (iter.hasNext())
    {
      iter.advance();
      put(iter.key(), iter.value());
    }
  }
  
  public byte get(char key)
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
  
  public byte remove(char key)
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
  
  public TCharSet keySet()
  {
    return new TKeyView();
  }
  
  public char[] keys()
  {
    char[] keys = new char[size()];
    char[] k = this._set;
    byte[] states = this._states;
    
    int i = k.length;
    for (int j = 0; i-- > 0;) {
      if (states[i] == 1) {
        keys[(j++)] = k[i];
      }
    }
    return keys;
  }
  
  public char[] keys(char[] array)
  {
    int size = size();
    if (array.length < size) {
      array = new char[size];
    }
    char[] keys = this._set;
    byte[] states = this._states;
    
    int i = keys.length;
    for (int j = 0; i-- > 0;) {
      if (states[i] == 1) {
        array[(j++)] = keys[i];
      }
    }
    return array;
  }
  
  public TByteCollection valueCollection()
  {
    return new TValueView();
  }
  
  public byte[] values()
  {
    byte[] vals = new byte[size()];
    byte[] v = this._values;
    byte[] states = this._states;
    
    int i = v.length;
    for (int j = 0; i-- > 0;) {
      if (states[i] == 1) {
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
    byte[] states = this._states;
    
    int i = v.length;
    for (int j = 0; i-- > 0;) {
      if (states[i] == 1) {
        array[(j++)] = v[i];
      }
    }
    return array;
  }
  
  public boolean containsValue(byte val)
  {
    byte[] states = this._states;
    byte[] vals = this._values;
    for (int i = vals.length; i-- > 0;) {
      if ((states[i] == 1) && (val == vals[i])) {
        return true;
      }
    }
    return false;
  }
  
  public boolean containsKey(char key)
  {
    return contains(key);
  }
  
  public TCharByteIterator iterator()
  {
    return new TCharByteHashIterator(this);
  }
  
  public boolean forEachKey(TCharProcedure procedure)
  {
    return forEach(procedure);
  }
  
  public boolean forEachValue(TByteProcedure procedure)
  {
    byte[] states = this._states;
    byte[] values = this._values;
    for (int i = values.length; i-- > 0;) {
      if ((states[i] == 1) && (!procedure.execute(values[i]))) {
        return false;
      }
    }
    return true;
  }
  
  public boolean forEachEntry(TCharByteProcedure procedure)
  {
    byte[] states = this._states;
    char[] keys = this._set;
    byte[] values = this._values;
    for (int i = keys.length; i-- > 0;) {
      if ((states[i] == 1) && (!procedure.execute(keys[i], values[i]))) {
        return false;
      }
    }
    return true;
  }
  
  public void transformValues(TByteFunction function)
  {
    byte[] states = this._states;
    byte[] values = this._values;
    for (int i = values.length; i-- > 0;) {
      if (states[i] == 1) {
        values[i] = function.execute(values[i]);
      }
    }
  }
  
  public boolean retainEntries(TCharByteProcedure procedure)
  {
    boolean modified = false;
    byte[] states = this._states;
    char[] keys = this._set;
    byte[] values = this._values;
    
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
  
  public boolean increment(char key)
  {
    return adjustValue(key, (byte)1);
  }
  
  public boolean adjustValue(char key, byte amount)
  {
    int index = index(key);
    if (index < 0) {
      return false;
    }
    int tmp17_16 = index; byte[] tmp17_13 = this._values;tmp17_13[tmp17_16] = ((byte)(tmp17_13[tmp17_16] + amount));
    return true;
  }
  
  public byte adjustOrPutValue(char key, byte adjust_amount, byte put_amount)
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
    byte previousState = this._states[index];
    if (isNewMapping) {
      postInsertHook(this.consumeFreeSlot);
    }
    return newValue;
  }
  
  protected class TKeyView
    implements TCharSet
  {
    protected TKeyView() {}
    
    public TCharIterator iterator()
    {
      return new TCharByteHashMap.TCharByteKeyHashIterator(TCharByteHashMap.this, TCharByteHashMap.this);
    }
    
    public char getNoEntryValue()
    {
      return TCharByteHashMap.this.no_entry_key;
    }
    
    public int size()
    {
      return TCharByteHashMap.this._size;
    }
    
    public boolean isEmpty()
    {
      return 0 == TCharByteHashMap.this._size;
    }
    
    public boolean contains(char entry)
    {
      return TCharByteHashMap.this.contains(entry);
    }
    
    public char[] toArray()
    {
      return TCharByteHashMap.this.keys();
    }
    
    public char[] toArray(char[] dest)
    {
      return TCharByteHashMap.this.keys(dest);
    }
    
    public boolean add(char entry)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean remove(char entry)
    {
      return TCharByteHashMap.this.no_entry_value != TCharByteHashMap.this.remove(entry);
    }
    
    public boolean containsAll(Collection<?> collection)
    {
      for (Object element : collection) {
        if ((element instanceof Character))
        {
          char ele = ((Character)element).charValue();
          if (!TCharByteHashMap.this.containsKey(ele)) {
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
        if (!TCharByteHashMap.this.containsKey(iter.next())) {
          return false;
        }
      }
      return true;
    }
    
    public boolean containsAll(char[] array)
    {
      for (char element : array) {
        if (!TCharByteHashMap.this.contains(element)) {
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
      char[] set = TCharByteHashMap.this._set;
      byte[] states = TCharByteHashMap.this._states;
      for (int i = set.length; i-- > 0;) {
        if ((states[i] == 1) && (Arrays.binarySearch(array, set[i]) < 0))
        {
          TCharByteHashMap.this.removeAt(i);
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
      TCharByteHashMap.this.clear();
    }
    
    public boolean forEach(TCharProcedure procedure)
    {
      return TCharByteHashMap.this.forEachKey(procedure);
    }
    
    public boolean equals(Object other)
    {
      if (!(other instanceof TCharSet)) {
        return false;
      }
      TCharSet that = (TCharSet)other;
      if (that.size() != size()) {
        return false;
      }
      for (int i = TCharByteHashMap.this._states.length; i-- > 0;) {
        if ((TCharByteHashMap.this._states[i] == 1) && 
          (!that.contains(TCharByteHashMap.this._set[i]))) {
          return false;
        }
      }
      return true;
    }
    
    public int hashCode()
    {
      int hashcode = 0;
      for (int i = TCharByteHashMap.this._states.length; i-- > 0;) {
        if (TCharByteHashMap.this._states[i] == 1) {
          hashcode += HashFunctions.hash(TCharByteHashMap.this._set[i]);
        }
      }
      return hashcode;
    }
    
    public String toString()
    {
      final StringBuilder buf = new StringBuilder("{");
      TCharByteHashMap.this.forEachKey(new TCharProcedure()
      {
        private boolean first = true;
        
        public boolean execute(char key)
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
    implements TByteCollection
  {
    protected TValueView() {}
    
    public TByteIterator iterator()
    {
      return new TCharByteHashMap.TCharByteValueHashIterator(TCharByteHashMap.this, TCharByteHashMap.this);
    }
    
    public byte getNoEntryValue()
    {
      return TCharByteHashMap.this.no_entry_value;
    }
    
    public int size()
    {
      return TCharByteHashMap.this._size;
    }
    
    public boolean isEmpty()
    {
      return 0 == TCharByteHashMap.this._size;
    }
    
    public boolean contains(byte entry)
    {
      return TCharByteHashMap.this.containsValue(entry);
    }
    
    public byte[] toArray()
    {
      return TCharByteHashMap.this.values();
    }
    
    public byte[] toArray(byte[] dest)
    {
      return TCharByteHashMap.this.values(dest);
    }
    
    public boolean add(byte entry)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean remove(byte entry)
    {
      byte[] values = TCharByteHashMap.this._values;
      char[] set = TCharByteHashMap.this._set;
      for (int i = values.length; i-- > 0;) {
        if ((set[i] != 0) && (set[i] != '\002') && (entry == values[i]))
        {
          TCharByteHashMap.this.removeAt(i);
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
          if (!TCharByteHashMap.this.containsValue(ele)) {
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
        if (!TCharByteHashMap.this.containsValue(iter.next())) {
          return false;
        }
      }
      return true;
    }
    
    public boolean containsAll(byte[] array)
    {
      for (byte element : array) {
        if (!TCharByteHashMap.this.containsValue(element)) {
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
      byte[] values = TCharByteHashMap.this._values;
      byte[] states = TCharByteHashMap.this._states;
      for (int i = values.length; i-- > 0;) {
        if ((states[i] == 1) && (Arrays.binarySearch(array, values[i]) < 0))
        {
          TCharByteHashMap.this.removeAt(i);
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
      TCharByteHashMap.this.clear();
    }
    
    public boolean forEach(TByteProcedure procedure)
    {
      return TCharByteHashMap.this.forEachValue(procedure);
    }
    
    public String toString()
    {
      final StringBuilder buf = new StringBuilder("{");
      TCharByteHashMap.this.forEachValue(new TByteProcedure()
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
  }
  
  class TCharByteKeyHashIterator
    extends THashPrimitiveIterator
    implements TCharIterator
  {
    TCharByteKeyHashIterator(TPrimitiveHash hash)
    {
      super();
    }
    
    public char next()
    {
      moveToNextIndex();
      return TCharByteHashMap.this._set[this._index];
    }
    
    public void remove()
    {
      if (this._expectedSize != this._hash.size()) {
        throw new ConcurrentModificationException();
      }
      try
      {
        this._hash.tempDisableAutoCompaction();
        TCharByteHashMap.this.removeAt(this._index);
      }
      finally
      {
        this._hash.reenableAutoCompaction(false);
      }
      this._expectedSize -= 1;
    }
  }
  
  class TCharByteValueHashIterator
    extends THashPrimitiveIterator
    implements TByteIterator
  {
    TCharByteValueHashIterator(TPrimitiveHash hash)
    {
      super();
    }
    
    public byte next()
    {
      moveToNextIndex();
      return TCharByteHashMap.this._values[this._index];
    }
    
    public void remove()
    {
      if (this._expectedSize != this._hash.size()) {
        throw new ConcurrentModificationException();
      }
      try
      {
        this._hash.tempDisableAutoCompaction();
        TCharByteHashMap.this.removeAt(this._index);
      }
      finally
      {
        this._hash.reenableAutoCompaction(false);
      }
      this._expectedSize -= 1;
    }
  }
  
  class TCharByteHashIterator
    extends THashPrimitiveIterator
    implements TCharByteIterator
  {
    TCharByteHashIterator(TCharByteHashMap map)
    {
      super();
    }
    
    public void advance()
    {
      moveToNextIndex();
    }
    
    public char key()
    {
      return TCharByteHashMap.this._set[this._index];
    }
    
    public byte value()
    {
      return TCharByteHashMap.this._values[this._index];
    }
    
    public byte setValue(byte val)
    {
      byte old = value();
      TCharByteHashMap.this._values[this._index] = val;
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
        TCharByteHashMap.this.removeAt(this._index);
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
    if (!(other instanceof TCharByteMap)) {
      return false;
    }
    TCharByteMap that = (TCharByteMap)other;
    if (that.size() != size()) {
      return false;
    }
    byte[] values = this._values;
    byte[] states = this._states;
    byte this_no_entry_value = getNoEntryValue();
    byte that_no_entry_value = that.getNoEntryValue();
    for (int i = values.length; i-- > 0;) {
      if (states[i] == 1)
      {
        char key = this._set[i];
        byte that_value = that.get(key);
        byte this_value = values[i];
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
    forEachEntry(new TCharByteProcedure()
    {
      private boolean first = true;
      
      public boolean execute(char key, byte value)
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
        out.writeChar(this._set[i]);
        out.writeByte(this._values[i]);
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
      char key = in.readChar();
      byte val = in.readByte();
      put(key, val);
    }
  }
}
