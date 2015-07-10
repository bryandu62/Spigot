package gnu.trove.map.hash;

import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.impl.Constants;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.THash;
import gnu.trove.impl.hash.TObjectHash;
import gnu.trove.iterator.TDoubleIterator;
import gnu.trove.iterator.TObjectDoubleIterator;
import gnu.trove.iterator.hash.TObjectHashIterator;
import gnu.trove.map.TObjectDoubleMap;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TObjectDoubleProcedure;
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

public class TObjectDoubleHashMap<K>
  extends TObjectHash<K>
  implements TObjectDoubleMap<K>, Externalizable
{
  static final long serialVersionUID = 1L;
  private final TObjectDoubleProcedure<K> PUT_ALL_PROC = new TObjectDoubleProcedure()
  {
    public boolean execute(K key, double value)
    {
      TObjectDoubleHashMap.this.put(key, value);
      return true;
    }
  };
  protected transient double[] _values;
  protected double no_entry_value;
  
  public TObjectDoubleHashMap()
  {
    this.no_entry_value = Constants.DEFAULT_DOUBLE_NO_ENTRY_VALUE;
  }
  
  public TObjectDoubleHashMap(int initialCapacity)
  {
    super(initialCapacity);
    this.no_entry_value = Constants.DEFAULT_DOUBLE_NO_ENTRY_VALUE;
  }
  
  public TObjectDoubleHashMap(int initialCapacity, float loadFactor)
  {
    super(initialCapacity, loadFactor);
    this.no_entry_value = Constants.DEFAULT_DOUBLE_NO_ENTRY_VALUE;
  }
  
  public TObjectDoubleHashMap(int initialCapacity, float loadFactor, double noEntryValue)
  {
    super(initialCapacity, loadFactor);
    this.no_entry_value = noEntryValue;
    if (this.no_entry_value != 0.0D) {
      Arrays.fill(this._values, this.no_entry_value);
    }
  }
  
  public TObjectDoubleHashMap(TObjectDoubleMap<? extends K> map)
  {
    this(map.size(), 0.5F, map.getNoEntryValue());
    if ((map instanceof TObjectDoubleHashMap))
    {
      TObjectDoubleHashMap hashmap = (TObjectDoubleHashMap)map;
      this._loadFactor = hashmap._loadFactor;
      this.no_entry_value = hashmap.no_entry_value;
      if (this.no_entry_value != 0.0D) {
        Arrays.fill(this._values, this.no_entry_value);
      }
      setUp((int)Math.ceil(10.0F / this._loadFactor));
    }
    putAll(map);
  }
  
  public int setUp(int initialCapacity)
  {
    int capacity = super.setUp(initialCapacity);
    this._values = new double[capacity];
    return capacity;
  }
  
  protected void rehash(int newCapacity)
  {
    int oldCapacity = this._set.length;
    
    K[] oldKeys = (Object[])this._set;
    double[] oldVals = this._values;
    
    this._set = new Object[newCapacity];
    Arrays.fill(this._set, FREE);
    this._values = new double[newCapacity];
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
  
  public double getNoEntryValue()
  {
    return this.no_entry_value;
  }
  
  public boolean containsKey(Object key)
  {
    return contains(key);
  }
  
  public boolean containsValue(double val)
  {
    Object[] keys = this._set;
    double[] vals = this._values;
    for (int i = vals.length; i-- > 0;) {
      if ((keys[i] != FREE) && (keys[i] != REMOVED) && (val == vals[i])) {
        return true;
      }
    }
    return false;
  }
  
  public double get(Object key)
  {
    int index = index(key);
    return index < 0 ? this.no_entry_value : this._values[index];
  }
  
  public double put(K key, double value)
  {
    int index = insertKey(key);
    return doPut(value, index);
  }
  
  public double putIfAbsent(K key, double value)
  {
    int index = insertKey(key);
    if (index < 0) {
      return this._values[(-index - 1)];
    }
    return doPut(value, index);
  }
  
  private double doPut(double value, int index)
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
  
  public double remove(Object key)
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
  
  public void putAll(Map<? extends K, ? extends Double> map)
  {
    Set<? extends Map.Entry<? extends K, ? extends Double>> set = map.entrySet();
    for (Map.Entry<? extends K, ? extends Double> entry : set) {
      put(entry.getKey(), ((Double)entry.getValue()).doubleValue());
    }
  }
  
  public void putAll(TObjectDoubleMap<? extends K> map)
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
  
  public TDoubleCollection valueCollection()
  {
    return new TDoubleValueCollection();
  }
  
  public double[] values()
  {
    double[] vals = new double[size()];
    double[] v = this._values;
    Object[] keys = this._set;
    
    int i = v.length;
    for (int j = 0; i-- > 0;) {
      if ((keys[i] != FREE) && (keys[i] != REMOVED)) {
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
  
  public TObjectDoubleIterator<K> iterator()
  {
    return new TObjectDoubleHashIterator(this);
  }
  
  public boolean increment(K key)
  {
    return adjustValue(key, 1.0D);
  }
  
  public boolean adjustValue(K key, double amount)
  {
    int index = index(key);
    if (index < 0) {
      return false;
    }
    this._values[index] += amount;
    return true;
  }
  
  public double adjustOrPutValue(K key, double adjust_amount, double put_amount)
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
    if (isNewMapping) {
      postInsertHook(this.consumeFreeSlot);
    }
    return newValue;
  }
  
  public boolean forEachKey(TObjectProcedure<? super K> procedure)
  {
    return forEach(procedure);
  }
  
  public boolean forEachValue(TDoubleProcedure procedure)
  {
    Object[] keys = this._set;
    double[] values = this._values;
    for (int i = values.length; i-- > 0;) {
      if ((keys[i] != FREE) && (keys[i] != REMOVED) && (!procedure.execute(values[i]))) {
        return false;
      }
    }
    return true;
  }
  
  public boolean forEachEntry(TObjectDoubleProcedure<? super K> procedure)
  {
    Object[] keys = this._set;
    double[] values = this._values;
    for (int i = keys.length; i-- > 0;) {
      if ((keys[i] != FREE) && (keys[i] != REMOVED) && (!procedure.execute(keys[i], values[i]))) {
        return false;
      }
    }
    return true;
  }
  
  public boolean retainEntries(TObjectDoubleProcedure<? super K> procedure)
  {
    boolean modified = false;
    
    K[] keys = (Object[])this._set;
    double[] values = this._values;
    
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
  
  public void transformValues(TDoubleFunction function)
  {
    Object[] keys = this._set;
    double[] values = this._values;
    for (int i = values.length; i-- > 0;) {
      if ((keys[i] != null) && (keys[i] != REMOVED)) {
        values[i] = function.execute(values[i]);
      }
    }
  }
  
  public boolean equals(Object other)
  {
    if (!(other instanceof TObjectDoubleMap)) {
      return false;
    }
    TObjectDoubleMap that = (TObjectDoubleMap)other;
    if (that.size() != size()) {
      return false;
    }
    try
    {
      TObjectDoubleIterator iter = iterator();
      while (iter.hasNext())
      {
        iter.advance();
        Object key = iter.key();
        double value = iter.value();
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
    double[] values = this._values;
    for (int i = values.length; i-- > 0;) {
      if ((keys[i] != FREE) && (keys[i] != REMOVED)) {
        hashcode += (HashFunctions.hash(values[i]) ^ (keys[i] == null ? 0 : keys[i].hashCode()));
      }
    }
    return hashcode;
  }
  
  protected class KeyView
    extends TObjectDoubleHashMap<K>.MapBackedView<K>
  {
    protected KeyView()
    {
      super(null);
    }
    
    public Iterator<K> iterator()
    {
      return new TObjectHashIterator(TObjectDoubleHashMap.this);
    }
    
    public boolean removeElement(K key)
    {
      return TObjectDoubleHashMap.this.no_entry_value != TObjectDoubleHashMap.this.remove(key);
    }
    
    public boolean containsElement(K key)
    {
      return TObjectDoubleHashMap.this.contains(key);
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
      TObjectDoubleHashMap.this.clear();
    }
    
    public boolean add(E obj)
    {
      throw new UnsupportedOperationException();
    }
    
    public int size()
    {
      return TObjectDoubleHashMap.this.size();
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
      return TObjectDoubleHashMap.this.isEmpty();
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
  
  class TDoubleValueCollection
    implements TDoubleCollection
  {
    TDoubleValueCollection() {}
    
    public TDoubleIterator iterator()
    {
      return new TObjectDoubleValueHashIterator();
    }
    
    public double getNoEntryValue()
    {
      return TObjectDoubleHashMap.this.no_entry_value;
    }
    
    public int size()
    {
      return TObjectDoubleHashMap.this._size;
    }
    
    public boolean isEmpty()
    {
      return 0 == TObjectDoubleHashMap.this._size;
    }
    
    public boolean contains(double entry)
    {
      return TObjectDoubleHashMap.this.containsValue(entry);
    }
    
    public double[] toArray()
    {
      return TObjectDoubleHashMap.this.values();
    }
    
    public double[] toArray(double[] dest)
    {
      return TObjectDoubleHashMap.this.values(dest);
    }
    
    public boolean add(double entry)
    {
      throw new UnsupportedOperationException();
    }
    
    public boolean remove(double entry)
    {
      double[] values = TObjectDoubleHashMap.this._values;
      Object[] set = TObjectDoubleHashMap.this._set;
      for (int i = values.length; i-- > 0;) {
        if ((set[i] != TObjectHash.FREE) && (set[i] != TObjectHash.REMOVED) && (entry == values[i]))
        {
          TObjectDoubleHashMap.this.removeAt(i);
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
          if (!TObjectDoubleHashMap.this.containsValue(ele)) {
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
        if (!TObjectDoubleHashMap.this.containsValue(iter.next())) {
          return false;
        }
      }
      return true;
    }
    
    public boolean containsAll(double[] array)
    {
      for (double element : array) {
        if (!TObjectDoubleHashMap.this.containsValue(element)) {
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
      double[] values = TObjectDoubleHashMap.this._values;
      
      Object[] set = TObjectDoubleHashMap.this._set;
      for (int i = set.length; i-- > 0;) {
        if ((set[i] != TObjectHash.FREE) && (set[i] != TObjectHash.REMOVED) && (Arrays.binarySearch(array, values[i]) < 0))
        {
          TObjectDoubleHashMap.this.removeAt(i);
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
      TObjectDoubleHashMap.this.clear();
    }
    
    public boolean forEach(TDoubleProcedure procedure)
    {
      return TObjectDoubleHashMap.this.forEachValue(procedure);
    }
    
    public String toString()
    {
      final StringBuilder buf = new StringBuilder("{");
      TObjectDoubleHashMap.this.forEachValue(new TDoubleProcedure()
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
    
    class TObjectDoubleValueHashIterator
      implements TDoubleIterator
    {
      protected THash _hash = TObjectDoubleHashMap.this;
      protected int _expectedSize;
      protected int _index;
      
      TObjectDoubleValueHashIterator()
      {
        this._expectedSize = this._hash.size();
        this._index = this._hash.capacity();
      }
      
      public boolean hasNext()
      {
        return nextIndex() >= 0;
      }
      
      public double next()
      {
        moveToNextIndex();
        return TObjectDoubleHashMap.this._values[this._index];
      }
      
      public void remove()
      {
        if (this._expectedSize != this._hash.size()) {
          throw new ConcurrentModificationException();
        }
        try
        {
          this._hash.tempDisableAutoCompaction();
          TObjectDoubleHashMap.this.removeAt(this._index);
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
        Object[] set = TObjectDoubleHashMap.this._set;
        int i = this._index;
        while ((i-- > 0) && ((set[i] == TObjectHash.FREE) || (set[i] == TObjectHash.REMOVED))) {}
        return i;
      }
    }
  }
  
  class TObjectDoubleHashIterator<K>
    extends TObjectHashIterator<K>
    implements TObjectDoubleIterator<K>
  {
    private final TObjectDoubleHashMap<K> _map;
    
    public TObjectDoubleHashIterator()
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
    
    public double value()
    {
      return this._map._values[this._index];
    }
    
    public double setValue(double val)
    {
      double old = value();
      this._map._values[this._index] = val;
      return old;
    }
  }
  
  public void writeExternal(ObjectOutput out)
    throws IOException
  {
    out.writeByte(0);
    
    super.writeExternal(out);
    
    out.writeDouble(this.no_entry_value);
    
    out.writeInt(this._size);
    for (int i = this._set.length; i-- > 0;) {
      if ((this._set[i] != REMOVED) && (this._set[i] != FREE))
      {
        out.writeObject(this._set[i]);
        out.writeDouble(this._values[i]);
      }
    }
  }
  
  public void readExternal(ObjectInput in)
    throws IOException, ClassNotFoundException
  {
    in.readByte();
    
    super.readExternal(in);
    
    this.no_entry_value = in.readDouble();
    
    int size = in.readInt();
    setUp(size);
    while (size-- > 0)
    {
      K key = in.readObject();
      double val = in.readDouble();
      put(key, val);
    }
  }
  
  public String toString()
  {
    final StringBuilder buf = new StringBuilder("{");
    forEachEntry(new TObjectDoubleProcedure()
    {
      private boolean first = true;
      
      public boolean execute(K key, double value)
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
