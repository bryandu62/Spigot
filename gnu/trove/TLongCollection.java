package gnu.trove;

import gnu.trove.iterator.TLongIterator;
import gnu.trove.procedure.TLongProcedure;
import java.util.Collection;

public abstract interface TLongCollection
{
  public static final long serialVersionUID = 1L;
  
  public abstract long getNoEntryValue();
  
  public abstract int size();
  
  public abstract boolean isEmpty();
  
  public abstract boolean contains(long paramLong);
  
  public abstract TLongIterator iterator();
  
  public abstract long[] toArray();
  
  public abstract long[] toArray(long[] paramArrayOfLong);
  
  public abstract boolean add(long paramLong);
  
  public abstract boolean remove(long paramLong);
  
  public abstract boolean containsAll(Collection<?> paramCollection);
  
  public abstract boolean containsAll(TLongCollection paramTLongCollection);
  
  public abstract boolean containsAll(long[] paramArrayOfLong);
  
  public abstract boolean addAll(Collection<? extends Long> paramCollection);
  
  public abstract boolean addAll(TLongCollection paramTLongCollection);
  
  public abstract boolean addAll(long[] paramArrayOfLong);
  
  public abstract boolean retainAll(Collection<?> paramCollection);
  
  public abstract boolean retainAll(TLongCollection paramTLongCollection);
  
  public abstract boolean retainAll(long[] paramArrayOfLong);
  
  public abstract boolean removeAll(Collection<?> paramCollection);
  
  public abstract boolean removeAll(TLongCollection paramTLongCollection);
  
  public abstract boolean removeAll(long[] paramArrayOfLong);
  
  public abstract void clear();
  
  public abstract boolean forEach(TLongProcedure paramTLongProcedure);
  
  public abstract boolean equals(Object paramObject);
  
  public abstract int hashCode();
}
