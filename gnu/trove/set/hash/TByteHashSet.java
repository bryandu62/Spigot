package gnu.trove.set.hash;

import gnu.trove.TByteCollection;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.TByteHash;
import gnu.trove.impl.hash.THashPrimitiveIterator;
import gnu.trove.iterator.TByteIterator;
import gnu.trove.set.TByteSet;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;

public class TByteHashSet
  extends TByteHash
  implements TByteSet, Externalizable
{
  static final long serialVersionUID = 1L;
  
  public TByteHashSet() {}
  
  public TByteHashSet(int initialCapacity)
  {
    super(initialCapacity);
  }
  
  public TByteHashSet(int initialCapacity, float load_factor)
  {
    super(initialCapacity, load_factor);
  }
  
  public TByteHashSet(int initial_capacity, float load_factor, byte no_entry_value)
  {
    super(initial_capacity, load_factor, no_entry_value);
    if (no_entry_value != 0) {
      Arrays.fill(this._set, no_entry_value);
    }
  }
  
  public TByteHashSet(Collection<? extends Byte> collection)
  {
    this(Math.max(collection.size(), 10));
    addAll(collection);
  }
  
  public TByteHashSet(TByteCollection collection)
  {
    this(Math.max(collection.size(), 10));
    if ((collection instanceof TByteHashSet))
    {
      TByteHashSet hashset = (TByteHashSet)collection;
      this._loadFactor = hashset._loadFactor;
      this.no_entry_value = hashset.no_entry_value;
      if (this.no_entry_value != 0) {
        Arrays.fill(this._set, this.no_entry_value);
      }
      setUp((int)Math.ceil(10.0F / this._loadFactor));
    }
    addAll(collection);
  }
  
  public TByteHashSet(byte[] array)
  {
    this(Math.max(array.length, 10));
    addAll(array);
  }
  
  public TByteIterator iterator()
  {
    return new TByteHashIterator(this);
  }
  
  public byte[] toArray()
  {
    byte[] result = new byte[size()];
    byte[] set = this._set;
    byte[] states = this._states;
    
    int i = states.length;
    for (int j = 0; i-- > 0;) {
      if (states[i] == 1) {
        result[(j++)] = set[i];
      }
    }
    return result;
  }
  
  public byte[] toArray(byte[] dest)
  {
    byte[] set = this._set;
    byte[] states = this._states;
    
    int i = states.length;
    for (int j = 0; i-- > 0;) {
      if (states[i] == 1) {
        dest[(j++)] = set[i];
      }
    }
    if (dest.length > this._size) {
      dest[this._size] = this.no_entry_value;
    }
    return dest;
  }
  
  public boolean add(byte val)
  {
    int index = insertKey(val);
    if (index < 0) {
      return false;
    }
    postInsertHook(this.consumeFreeSlot);
    
    return true;
  }
  
  public boolean remove(byte val)
  {
    int index = index(val);
    if (index >= 0)
    {
      removeAt(index);
      return true;
    }
    return false;
  }
  
  public boolean containsAll(Collection<?> collection)
  {
    for (Object element : collection) {
      if ((element instanceof Byte))
      {
        byte c = ((Byte)element).byteValue();
        if (!contains(c)) {
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
    while (iter.hasNext())
    {
      byte element = iter.next();
      if (!contains(element)) {
        return false;
      }
    }
    return true;
  }
  
  public boolean containsAll(byte[] array)
  {
    for (int i = array.length; i-- > 0;) {
      if (!contains(array[i])) {
        return false;
      }
    }
    return true;
  }
  
  public boolean addAll(Collection<? extends Byte> collection)
  {
    boolean changed = false;
    for (Byte element : collection)
    {
      byte e = element.byteValue();
      if (add(e)) {
        changed = true;
      }
    }
    return changed;
  }
  
  public boolean addAll(TByteCollection collection)
  {
    boolean changed = false;
    TByteIterator iter = collection.iterator();
    while (iter.hasNext())
    {
      byte element = iter.next();
      if (add(element)) {
        changed = true;
      }
    }
    return changed;
  }
  
  public boolean addAll(byte[] array)
  {
    boolean changed = false;
    for (int i = array.length; i-- > 0;) {
      if (add(array[i])) {
        changed = true;
      }
    }
    return changed;
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
    byte[] set = this._set;
    byte[] states = this._states;
    
    this._autoCompactTemporaryDisable = true;
    for (int i = set.length; i-- > 0;) {
      if ((states[i] == 1) && (Arrays.binarySearch(array, set[i]) < 0))
      {
        removeAt(i);
        changed = true;
      }
    }
    this._autoCompactTemporaryDisable = false;
    
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
    super.clear();
    byte[] set = this._set;
    byte[] states = this._states;
    for (int i = set.length; i-- > 0;)
    {
      set[i] = this.no_entry_value;
      states[i] = 0;
    }
  }
  
  protected void rehash(int newCapacity)
  {
    int oldCapacity = this._set.length;
    
    byte[] oldSet = this._set;
    byte[] oldStates = this._states;
    
    this._set = new byte[newCapacity];
    this._states = new byte[newCapacity];
    for (int i = oldCapacity; i-- > 0;) {
      if (oldStates[i] == 1)
      {
        byte o = oldSet[i];
        index = insertKey(o);
      }
    }
    int index;
  }
  
  public boolean equals(Object other)
  {
    if (!(other instanceof TByteSet)) {
      return false;
    }
    TByteSet that = (TByteSet)other;
    if (that.size() != size()) {
      return false;
    }
    for (int i = this._states.length; i-- > 0;) {
      if ((this._states[i] == 1) && 
        (!that.contains(this._set[i]))) {
        return false;
      }
    }
    return true;
  }
  
  public int hashCode()
  {
    int hashcode = 0;
    for (int i = this._states.length; i-- > 0;) {
      if (this._states[i] == 1) {
        hashcode += HashFunctions.hash(this._set[i]);
      }
    }
    return hashcode;
  }
  
  public String toString()
  {
    StringBuilder buffy = new StringBuilder(this._size * 2 + 2);
    buffy.append("{");
    int i = this._states.length;
    for (int j = 1; i-- > 0;) {
      if (this._states[i] == 1)
      {
        buffy.append(this._set[i]);
        if (j++ < this._size) {
          buffy.append(",");
        }
      }
    }
    buffy.append("}");
    return buffy.toString();
  }
  
  class TByteHashIterator
    extends THashPrimitiveIterator
    implements TByteIterator
  {
    private final TByteHash _hash;
    
    public TByteHashIterator(TByteHash hash)
    {
      super();
      this._hash = hash;
    }
    
    public byte next()
    {
      moveToNextIndex();
      return this._hash._set[this._index];
    }
  }
  
  public void writeExternal(ObjectOutput out)
    throws IOException
  {
    out.writeByte(1);
    
    super.writeExternal(out);
    
    out.writeInt(this._size);
    
    out.writeFloat(this._loadFactor);
    
    out.writeByte(this.no_entry_value);
    for (int i = this._states.length; i-- > 0;) {
      if (this._states[i] == 1) {
        out.writeByte(this._set[i]);
      }
    }
  }
  
  public void readExternal(ObjectInput in)
    throws IOException, ClassNotFoundException
  {
    int version = in.readByte();
    
    super.readExternal(in);
    
    int size = in.readInt();
    if (version >= 1)
    {
      this._loadFactor = in.readFloat();
      
      this.no_entry_value = in.readByte();
      if (this.no_entry_value != 0) {
        Arrays.fill(this._set, this.no_entry_value);
      }
    }
    setUp(size);
    while (size-- > 0)
    {
      byte val = in.readByte();
      add(val);
    }
  }
}
