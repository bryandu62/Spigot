package gnu.trove.map.hash;

import gnu.trove.TCharCollection;
import gnu.trove.function.TCharFunction;
import gnu.trove.impl.Constants;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.THash;
import gnu.trove.impl.hash.TObjectHash;
import gnu.trove.iterator.TCharIterator;
import gnu.trove.iterator.TObjectCharIterator;
import gnu.trove.iterator.hash.TObjectHashIterator;
import gnu.trove.map.TObjectCharMap;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TObjectCharProcedure;
import gnu.trove.procedure.TObjectProcedure;
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

public class TObjectCharHashMap<K>
  extends TObjectHash<K>
  implements TObjectCharMap<K>, Externalizable
{
  static final long serialVersionUID = 1L;
  private final TObjectCharProcedure<K> PUT_ALL_PROC = new TObjectCharProcedure()
  {
    public boolean execute(K key, char value)
    {
      TObjectCharHashMap.this.put(key, value);
      return true;
    }
  };
  protected transient char[] _values;
  protected char no_entry_value;
  
  public TObjectCharHashMap()
  {
    this.no_entry_value = Constants.DEFAULT_CHAR_NO_ENTRY_VALUE;
  }
  
  public TObjectCharHashMap(int initialCapacity)
  {
    super(initialCapacity);
    this.no_entry_value = Constants.DEFAULT_CHAR_NO_ENTRY_VALUE;
  }
  
  public TObjectCharHashMap(int initialCapacity, float loadFactor)
  {
    super(initialCapacity, loadFactor);
    this.no_entry_value = Constants.DEFAULT_CHAR_NO_ENTRY_VALUE;
  }
  
  public TObjectCharHashMap(int initialCapacity, float loadFactor, char noEntryValue)
  {
    super(initialCapacity, loadFactor);
    this.no_entry_value = noEntryValue;
    if (this.no_entry_value != 0) {
      Arrays.fill(this._values, this.no_entry_value);
    }
  }
  
  public TObjectCharHashMap(TObjectCharMap<? extends K> map)
  {
    this(map.size(), 0.5F, map.getNoEntryValue());
    if ((map instanceof TObjectCharHashMap))
    {
      TObjectCharHashMap hashmap = (TObjectCharHashMap)map;
      this._loadFactor = hashmap._loadFactor;
      this.no_entry_value = hashmap.no_entry_value;
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
    this._values = new char[capacity];
    return capacity;
  }
  
  protected void rehash(int newCapacity)
  {
    int oldCapacity = this._set.length;
    
    K[] oldKeys = (Object[])this._set;
    char[] oldVals = this._values;
    
    this._set = new Object[newCapacity];
    Arrays.fill(this._set, FREE);
    this._values = new char[newCapacity];
    Arrays.fill(this._values, this.no_entry_value);
    for (int i = oldCapacity; i-- > 0;) {
      if ((oldKeys[i] != FREE) && (oldKeys[i] != REMOVED))
      {
        K o = oldKeys[i];
        int index = insertKey(o);
        if (index < 0) {
          throwObjectContractViolation(this._set[(-index - 1)], o);
        }
        this._set[index] = o;
        this._values[index] = oldVals[i];
      }
    }
  }
  
  public char getNoEntryValue()
  {
    return this.no_entry_value;
  }
  
  public boolean containsKey(Object key)
  {
    return contains(key);
  }
  
  public boolean containsValue(char val)
  {
    Object[] keys = this._set;
    char[] vals = this._values;
    for (int i = vals.length; i-- > 0;) {
      if ((keys[i] != FREE) && (keys[i] != REMOVED) && (val == vals[i])) {
        return true;
      }
    }
    return false;
  }
  
  public char get(Object key)
  {
    int index = index(key);
    return index < 0 ? this.no_entry_value : this._values[index];
  }
  
  public char put(K key, char value)
  {
    int index = insertKey(key);
    return doPut(value, index);
  }
  
  public char putIfAbsent(K key, char value)
  {
    int index = insertKey(key);
    if (index < 0) {
      return this._values[(-index - 1)];
    }
    return doPut(value, index);
  }
  
  private char doPut(char value, int index)
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
  
  public char remove(Object key)
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
  
  public void putAll(Map<? extends K, ? extends Character> map)
  {
    Set<? extends Map.Entry<? extends K, ? extends Character>> set = map.entrySet();
    for (Map.Entry<? extends K, ? extends Character> entry : set) {
      put(entry.getKey(), ((Character)entry.getValue()).charValue());
    }
  }
  
  public void putAll(TObjectCharMap<? extends K> map)
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
  
  public TCharCollection valueCollection()
  {
    return new TCharValueCollection();
  }
  
  public char[] values()
  {
    char[] vals = new char[size()];
    char[] v = this._values;
    Object[] keys = this._set;
    
    int i = v.length;
    for (int j = 0; i-- > 0;) {
      if ((keys[i] != FREE) && (keys[i] != REMOVED)) {
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
  
  public TObjectCharIterator<K> iterator()
  {
    return new TObjectCharHashIterator(this);
  }
  
  public boolean increment(K key)
  {
    return adjustValue(key, '\001');
  }
  
  public boolean adjustValue(K key, char amount)
  {
    int index = index(key);
    if (index < 0) {
      return false;
    }
    int tmp17_16 = index; char[] tmp17_13 = this._values;tmp17_13[tmp17_16] = ((char)(tmp17_13[tmp17_16] + amount));
    return true;
  }
  
  public char adjustOrPutValue(K key, char adjust_amount, char put_amount)
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
    if (isNewMapping) {
      postInsertHook(this.consumeFreeSlot);
    }
    return newValue;
  }
  
  public boolean forEachKey(TObjectProcedure<? super K> procedure)
  {
    return forEach(procedure);
  }
  
  public boolean forEachValue(TCharProcedure procedure)
  {
    Object[] keys = this._set;
    char[] values = this._values;
    for (int i = values.length; i-- > 0;) {
      if ((keys[i] != FREE) && (keys[i] != REMOVED) && (!procedure.execute(values[i]))) {
        return false;
      }
    }
    return true;
  }
  
  public boolean forEachEntry(TObjectCharProcedure<? super K> procedure)
  {
    Object[] keys = this._set;
    char[] values = this._values;
    for (int i = keys.length; i-- > 0;) {
      if ((keys[i] != FREE) && (keys[i] != REMOVED) && (!procedure.execute(keys[i], values[i]))) {
        return false;
      }
    }
    return true;
  }
  
  public boolean retainEntries(TObjectCharProcedure<? super K> procedure)
  {
    boolean modified = false;
    
    K[] keys = (Object[])this._set;
    char[] values = this._values;
    
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
  
  public void transformValues(TCharFunction function)
  {
    Object[] keys = this._set;
    char[] values = this._values;
    for (int i = values.length; i-- > 0;) {
      if ((keys[i] != null) && (keys[i] != REMOVED)) {
        values[i] = function.execute(values[i]);
      }
    }
  }
  
  public boolean equals(Object other)
  {
    if (!(other instanceof TObjectCharMap)) {
      return false;
    }
    TObjectCharMap that = (TObjectCharMap)other;
    if (that.size() != size()) {
      return false;
    }
    try
    {
      TObjectCharIterator iter = iterator();
      while (iter.hasNext())
      {
        iter.advance();
        Object key = iter.key();
        char value = iter.value();
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
    char[] values = this._values;
    for (int i = values.length; i-- > 0;) {
      if ((keys[i] != FREE) && (keys[i] != REMOVED)) {
        hashcode += (HashFunctions.hash(values[i]) ^ (keys[i] == null ? 0 : keys[i].hashCode()));
      }
    }
    return hashcode;
  }
  
  protected class KeyView
    extends TObjectCharHashMap<K>.MapBackedView<K>
  {
    protected KeyView()
    {
      super(null);
    }
    
    public Iterator<K> iterator()
    {
      return new TObjectHashIterator(TObjectCharHashMap.this);
    }
    
    public boolean removeElement(K key)
    {
      return TObjectCharHashMap.this.no_entry_value != TObjectCharHashMap.this.remove(key);
    }
    
    public boolean containsElement(K key)
    {
      return TObjectCharHashMap.this.contains(key);
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
      TObjectCharHashMap.this.clear();
    }
    
    public boolean add(E obj)
    {
      throw new UnsupportedOperationException();
    }
    
    public int size()
    {
      return TObjectCharHashMap.this.size();
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
      return TObjectCharHashMap.this.isEmpty();
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
  
  class TCharValueCollection
    implements TCharCollection
  {
    TCharValueCollection() {}
    
    public TCharIterator iterator()
    {
      return new TObjectCharValueHashIterator();
    }
    
    public char getNoEntryValue()
    {
      return TObjectCharHashMap.this.no_entry_value;
    }
    
    public int size()
    {
      return TObjectCharHashMap.this._size;
    }
    
    public boolean isEmpty()
    {
      return 0 == TObjectCharHashMap.this._size;
    }
    
    public boolean contains(char entry)
    {
      return TObjectCharHashMap.this.containsValue(entry);
    }
    
    public char[] toArray()
    {
      return TObjectCharHashMap.this.values();
    }
    
    public char[] toArray(char[] dest)
    {
      return TObjectCharHashMap.this.values(dest);
    }
    
    public boolean add(char entry)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean remove(char entry)
    {
      char[] values = TObjectCharHashMap.this._values;
      Object[] set = TObjectCharHashMap.this._set;
      for (int i = values.length; i-- > 0;) {
        if ((set[i] != TObjectHash.FREE) && (set[i] != TObjectHash.REMOVED) && (entry == values[i]))
        {
          TObjectCharHashMap.this.removeAt(i);
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
          if (!TObjectCharHashMap.this.containsValue(ele)) {
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
        if (!TObjectCharHashMap.this.containsValue(iter.next())) {
          return false;
        }
      }
      return true;
    }
    
    public boolean containsAll(char[] array)
    {
      for (char element : array) {
        if (!TObjectCharHashMap.this.containsValue(element)) {
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
      char[] values = TObjectCharHashMap.this._values;
      
      Object[] set = TObjectCharHashMap.this._set;
      for (int i = set.length; i-- > 0;) {
        if ((set[i] != TObjectHash.FREE) && (set[i] != TObjectHash.REMOVED) && (Arrays.binarySearch(array, values[i]) < 0))
        {
          TObjectCharHashMap.this.removeAt(i);
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
      TObjectCharHashMap.this.clear();
    }
    
    public boolean forEach(TCharProcedure procedure)
    {
      return TObjectCharHashMap.this.forEachValue(procedure);
    }
    
    public String toString()
    {
      final StringBuilder buf = new StringBuilder("{");
      TObjectCharHashMap.this.forEachValue(new TCharProcedure()
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
    
    class TObjectCharValueHashIterator
      implements TCharIterator
    {
      protected THash _hash = TObjectCharHashMap.this;
      protected int _expectedSize;
      protected int _index;
      
      TObjectCharValueHashIterator()
      {
        this._expectedSize = this._hash.size();
        this._index = this._hash.capacity();
      }
      
      public boolean hasNext()
      {
        return nextIndex() >= 0;
      }
      
      public char next()
      {
        moveToNextIndex();
        return TObjectCharHashMap.this._values[this._index];
      }
      
      public void remove()
      {
        if (this._expectedSize != this._hash.size()) {
          throw new ConcurrentModificationException();
        }
        try
        {
          this._hash.tempDisableAutoCompaction();
          TObjectCharHashMap.this.removeAt(this._index);
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
        Object[] set = TObjectCharHashMap.this._set;
        int i = this._index;
        while ((i-- > 0) && ((set[i] == TObjectHash.FREE) || (set[i] == TObjectHash.REMOVED))) {}
        return i;
      }
    }
  }
  
  class TObjectCharHashIterator<K>
    extends TObjectHashIterator<K>
    implements TObjectCharIterator<K>
  {
    private final TObjectCharHashMap<K> _map;
    
    public TObjectCharHashIterator()
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
    
    public char value()
    {
      return this._map._values[this._index];
    }
    
    public char setValue(char val)
    {
      char old = value();
      this._map._values[this._index] = val;
      return old;
    }
  }
  
  public void writeExternal(ObjectOutput out)
    throws IOException
  {
    out.writeByte(0);
    
    super.writeExternal(out);
    
    out.writeChar(this.no_entry_value);
    
    out.writeInt(this._size);
    for (int i = this._set.length; i-- > 0;) {
      if ((this._set[i] != REMOVED) && (this._set[i] != FREE))
      {
        out.writeObject(this._set[i]);
        out.writeChar(this._values[i]);
      }
    }
  }
  
  public void readExternal(ObjectInput in)
    throws IOException, ClassNotFoundException
  {
    in.readByte();
    
    super.readExternal(in);
    
    this.no_entry_value = in.readChar();
    
    int size = in.readInt();
    setUp(size);
    while (size-- > 0)
    {
      K key = in.readObject();
      char val = in.readChar();
      put(key, val);
    }
  }
  
  public String toString()
  {
    final StringBuilder buf = new StringBuilder("{");
    forEachEntry(new TObjectCharProcedure()
    {
      private boolean first = true;
      
      public boolean execute(K key, char value)
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
