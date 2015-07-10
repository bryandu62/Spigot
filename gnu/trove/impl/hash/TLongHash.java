package gnu.trove.impl.hash;

import gnu.trove.impl.Constants;
import gnu.trove.impl.HashFunctions;
import gnu.trove.procedure.TLongProcedure;
import java.util.Arrays;

public abstract class TLongHash
  extends TPrimitiveHash
{
  static final long serialVersionUID = 1L;
  public transient long[] _set;
  protected long no_entry_value;
  protected boolean consumeFreeSlot;
  
  public TLongHash()
  {
    this.no_entry_value = Constants.DEFAULT_LONG_NO_ENTRY_VALUE;
    if (this.no_entry_value != 0L) {
      Arrays.fill(this._set, this.no_entry_value);
    }
  }
  
  public TLongHash(int initialCapacity)
  {
    super(initialCapacity);
    this.no_entry_value = Constants.DEFAULT_LONG_NO_ENTRY_VALUE;
    if (this.no_entry_value != 0L) {
      Arrays.fill(this._set, this.no_entry_value);
    }
  }
  
  public TLongHash(int initialCapacity, float loadFactor)
  {
    super(initialCapacity, loadFactor);
    this.no_entry_value = Constants.DEFAULT_LONG_NO_ENTRY_VALUE;
    if (this.no_entry_value != 0L) {
      Arrays.fill(this._set, this.no_entry_value);
    }
  }
  
  public TLongHash(int initialCapacity, float loadFactor, long no_entry_value)
  {
    super(initialCapacity, loadFactor);
    this.no_entry_value = no_entry_value;
    if (no_entry_value != 0L) {
      Arrays.fill(this._set, no_entry_value);
    }
  }
  
  public long getNoEntryValue()
  {
    return this.no_entry_value;
  }
  
  protected int setUp(int initialCapacity)
  {
    int capacity = super.setUp(initialCapacity);
    this._set = new long[capacity];
    return capacity;
  }
  
  public boolean contains(long val)
  {
    return index(val) >= 0;
  }
  
  public boolean forEach(TLongProcedure procedure)
  {
    byte[] states = this._states;
    long[] set = this._set;
    for (int i = set.length; i-- > 0;) {
      if ((states[i] == 1) && (!procedure.execute(set[i]))) {
        return false;
      }
    }
    return true;
  }
  
  protected void removeAt(int index)
  {
    this._set[index] = this.no_entry_value;
    super.removeAt(index);
  }
  
  protected int index(long val)
  {
    byte[] states = this._states;
    long[] set = this._set;
    int length = states.length;
    int hash = HashFunctions.hash(val) & 0x7FFFFFFF;
    int index = hash % length;
    byte state = states[index];
    if (state == 0) {
      return -1;
    }
    if ((state == 1) && (set[index] == val)) {
      return index;
    }
    return indexRehashed(val, index, hash, state);
  }
  
  int indexRehashed(long key, int index, int hash, byte state)
  {
    int length = this._set.length;
    int probe = 1 + hash % (length - 2);
    int loopIndex = index;
    do
    {
      index -= probe;
      if (index < 0) {
        index += length;
      }
      state = this._states[index];
      if (state == 0) {
        return -1;
      }
      if ((key == this._set[index]) && (state != 2)) {
        return index;
      }
    } while (index != loopIndex);
    return -1;
  }
  
  protected int insertKey(long val)
  {
    int hash = HashFunctions.hash(val) & 0x7FFFFFFF;
    int index = hash % this._states.length;
    byte state = this._states[index];
    
    this.consumeFreeSlot = false;
    if (state == 0)
    {
      this.consumeFreeSlot = true;
      insertKeyAt(index, val);
      
      return index;
    }
    if ((state == 1) && (this._set[index] == val)) {
      return -index - 1;
    }
    return insertKeyRehash(val, index, hash, state);
  }
  
  int insertKeyRehash(long val, int index, int hash, byte state)
  {
    int length = this._set.length;
    int probe = 1 + hash % (length - 2);
    int loopIndex = index;
    int firstRemoved = -1;
    do
    {
      if ((state == 2) && (firstRemoved == -1)) {
        firstRemoved = index;
      }
      index -= probe;
      if (index < 0) {
        index += length;
      }
      state = this._states[index];
      if (state == 0)
      {
        if (firstRemoved != -1)
        {
          insertKeyAt(firstRemoved, val);
          return firstRemoved;
        }
        this.consumeFreeSlot = true;
        insertKeyAt(index, val);
        return index;
      }
      if ((state == 1) && (this._set[index] == val)) {
        return -index - 1;
      }
    } while (index != loopIndex);
    if (firstRemoved != -1)
    {
      insertKeyAt(firstRemoved, val);
      return firstRemoved;
    }
    throw new IllegalStateException("No free or removed slots available. Key set full?!!");
  }
  
  void insertKeyAt(int index, long val)
  {
    this._set[index] = val;
    this._states[index] = 1;
  }
}
