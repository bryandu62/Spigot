package gnu.trove.impl.hash;

import gnu.trove.impl.HashFunctions;
import gnu.trove.procedure.TCharProcedure;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public abstract class TCharByteHash
  extends TPrimitiveHash
{
  static final long serialVersionUID = 1L;
  public transient char[] _set;
  protected char no_entry_key;
  protected byte no_entry_value;
  protected boolean consumeFreeSlot;
  
  public TCharByteHash()
  {
    this.no_entry_key = '\000';
    this.no_entry_value = 0;
  }
  
  public TCharByteHash(int initialCapacity)
  {
    super(initialCapacity);
    this.no_entry_key = '\000';
    this.no_entry_value = 0;
  }
  
  public TCharByteHash(int initialCapacity, float loadFactor)
  {
    super(initialCapacity, loadFactor);
    this.no_entry_key = '\000';
    this.no_entry_value = 0;
  }
  
  public TCharByteHash(int initialCapacity, float loadFactor, char no_entry_key, byte no_entry_value)
  {
    super(initialCapacity, loadFactor);
    this.no_entry_key = no_entry_key;
    this.no_entry_value = no_entry_value;
  }
  
  public char getNoEntryKey()
  {
    return this.no_entry_key;
  }
  
  public byte getNoEntryValue()
  {
    return this.no_entry_value;
  }
  
  protected int setUp(int initialCapacity)
  {
    int capacity = super.setUp(initialCapacity);
    this._set = new char[capacity];
    return capacity;
  }
  
  public boolean contains(char val)
  {
    return index(val) >= 0;
  }
  
  public boolean forEach(TCharProcedure procedure)
  {
    byte[] states = this._states;
    char[] set = this._set;
    for (int i = set.length; i-- > 0;) {
      if ((states[i] == 1) && (!procedure.execute(set[i]))) {
        return false;
      }
    }
    return true;
  }
  
  protected void removeAt(int index)
  {
    this._set[index] = this.no_entry_key;
    super.removeAt(index);
  }
  
  protected int index(char key)
  {
    byte[] states = this._states;
    char[] set = this._set;
    int length = states.length;
    int hash = HashFunctions.hash(key) & 0x7FFFFFFF;
    int index = hash % length;
    byte state = states[index];
    if (state == 0) {
      return -1;
    }
    if ((state == 1) && (set[index] == key)) {
      return index;
    }
    return indexRehashed(key, index, hash, state);
  }
  
  int indexRehashed(char key, int index, int hash, byte state)
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
  
  protected int insertKey(char val)
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
  
  int insertKeyRehash(char val, int index, int hash, byte state)
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
  
  void insertKeyAt(int index, char val)
  {
    this._set[index] = val;
    this._states[index] = 1;
  }
  
  protected int XinsertKey(char key)
  {
    byte[] states = this._states;
    char[] set = this._set;
    int length = states.length;
    int hash = HashFunctions.hash(key) & 0x7FFFFFFF;
    int index = hash % length;
    byte state = states[index];
    
    this.consumeFreeSlot = false;
    if (state == 0)
    {
      this.consumeFreeSlot = true;
      set[index] = key;
      states[index] = 1;
      
      return index;
    }
    if ((state == 1) && (set[index] == key)) {
      return -index - 1;
    }
    int probe = 1 + hash % (length - 2);
    if (state != 2) {
      do
      {
        index -= probe;
        if (index < 0) {
          index += length;
        }
        state = states[index];
      } while ((state == 1) && (set[index] != key));
    }
    if (state == 2)
    {
      int firstRemoved = index;
      while ((state != 0) && ((state == 2) || (set[index] != key)))
      {
        index -= probe;
        if (index < 0) {
          index += length;
        }
        state = states[index];
      }
      if (state == 1) {
        return -index - 1;
      }
      set[index] = key;
      states[index] = 1;
      
      return firstRemoved;
    }
    if (state == 1) {
      return -index - 1;
    }
    this.consumeFreeSlot = true;
    set[index] = key;
    states[index] = 1;
    
    return index;
  }
  
  public void writeExternal(ObjectOutput out)
    throws IOException
  {
    out.writeByte(0);
    
    super.writeExternal(out);
    
    out.writeChar(this.no_entry_key);
    
    out.writeByte(this.no_entry_value);
  }
  
  public void readExternal(ObjectInput in)
    throws IOException, ClassNotFoundException
  {
    in.readByte();
    
    super.readExternal(in);
    
    this.no_entry_key = in.readChar();
    
    this.no_entry_value = in.readByte();
  }
}
