package gnu.trove.set.hash;

import gnu.trove.impl.hash.THash;
import gnu.trove.impl.hash.TObjectHash;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.iterator.hash.TObjectHashIterator;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.list.linked.TIntLinkedList;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.procedure.TObjectProcedure;
import java.io.IOException;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class TLinkedHashSet<E>
  extends THashSet<E>
{
  TIntList order;
  
  public TLinkedHashSet() {}
  
  public TLinkedHashSet(int initialCapacity)
  {
    super(initialCapacity);
  }
  
  public TLinkedHashSet(int initialCapacity, float loadFactor)
  {
    super(initialCapacity, loadFactor);
  }
  
  public TLinkedHashSet(Collection<? extends E> es)
  {
    super(es);
  }
  
  public int setUp(int initialCapacity)
  {
    this.order = new TIntArrayList(initialCapacity)
    {
      public void ensureCapacity(int capacity)
      {
        if (capacity > this._data.length)
        {
          int newCap = Math.max(TLinkedHashSet.this._set.length, capacity);
          int[] tmp = new int[newCap];
          System.arraycopy(this._data, 0, tmp, 0, this._data.length);
          this._data = tmp;
        }
      }
    };
    return super.setUp(initialCapacity);
  }
  
  public void clear()
  {
    super.clear();
    this.order.clear();
  }
  
  public String toString()
  {
    StringBuilder buf = new StringBuilder("{");
    boolean first = true;
    for (Iterator<E> it = iterator(); it.hasNext();)
    {
      if (first) {
        first = false;
      } else {
        buf.append(", ");
      }
      buf.append(it.next());
    }
    buf.append("}");
    return buf.toString();
  }
  
  public boolean add(E obj)
  {
    int index = insertKey(obj);
    if (index < 0) {
      return false;
    }
    if (!this.order.add(index)) {
      throw new IllegalStateException("Order not changed after insert");
    }
    postInsertHook(this.consumeFreeSlot);
    return true;
  }
  
  protected void removeAt(int index)
  {
    this.order.remove(index);
    super.removeAt(index);
  }
  
  protected void rehash(int newCapacity)
  {
    TIntLinkedList oldOrder = new TIntLinkedList(this.order);
    int oldSize = size();
    
    Object[] oldSet = this._set;
    
    this.order.clear();
    this._set = new Object[newCapacity];
    Arrays.fill(this._set, FREE);
    for (TIntIterator iterator = oldOrder.iterator(); iterator.hasNext();)
    {
      int i = iterator.next();
      E o = oldSet[i];
      if ((o == FREE) || (o == REMOVED)) {
        throw new IllegalStateException("Iterating over empty location while rehashing");
      }
      if ((o != FREE) && (o != REMOVED))
      {
        int index = insertKey(o);
        if (index < 0) {
          throwObjectContractViolation(this._set[(-index - 1)], o, size(), oldSize, oldSet);
        }
        if (!this.order.add(index)) {
          throw new IllegalStateException("Order not changed after insert");
        }
      }
    }
  }
  
  class WriteProcedure
    implements TIntProcedure
  {
    final ObjectOutput output;
    IOException ioException;
    
    WriteProcedure(ObjectOutput output)
    {
      this.output = output;
    }
    
    public IOException getIoException()
    {
      return this.ioException;
    }
    
    public boolean execute(int value)
    {
      try
      {
        this.output.writeObject(TLinkedHashSet.this._set[value]);
      }
      catch (IOException e)
      {
        this.ioException = e;
        return false;
      }
      return true;
    }
  }
  
  protected void writeEntries(ObjectOutput out)
    throws IOException
  {
    TLinkedHashSet<E>.WriteProcedure writeProcedure = new WriteProcedure(out);
    if (!this.order.forEach(writeProcedure)) {
      throw writeProcedure.getIoException();
    }
  }
  
  public TObjectHashIterator<E> iterator()
  {
    new TObjectHashIterator(this)
    {
      TIntIterator localIterator = TLinkedHashSet.this.order.iterator();
      int lastIndex;
      
      public E next()
      {
        this.lastIndex = this.localIterator.next();
        return (E)objectAtIndex(this.lastIndex);
      }
      
      public boolean hasNext()
      {
        return this.localIterator.hasNext();
      }
      
      public void remove()
      {
        this.localIterator.remove();
        try
        {
          this._hash.tempDisableAutoCompaction();
          TLinkedHashSet.this.removeAt(this.lastIndex);
        }
        finally
        {
          this._hash.reenableAutoCompaction(false);
        }
      }
    };
  }
  
  class ForEachProcedure
    implements TIntProcedure
  {
    boolean changed = false;
    final Object[] set;
    final TObjectProcedure<? super E> procedure;
    
    public ForEachProcedure(TObjectProcedure<? super E> set)
    {
      this.set = set;
      this.procedure = procedure;
    }
    
    public boolean execute(int value)
    {
      return this.procedure.execute(this.set[value]);
    }
  }
  
  public boolean forEach(TObjectProcedure<? super E> procedure)
  {
    TLinkedHashSet<E>.ForEachProcedure forEachProcedure = new ForEachProcedure(this._set, procedure);
    return this.order.forEach(forEachProcedure);
  }
}
