package gnu.trove;

import gnu.trove.iterator.TShortIterator;
import gnu.trove.procedure.TShortProcedure;
import java.util.Collection;

public abstract interface TShortCollection
{
  public static final long serialVersionUID = 1L;
  
  public abstract short getNoEntryValue();
  
  public abstract int size();
  
  public abstract boolean isEmpty();
  
  public abstract boolean contains(short paramShort);
  
  public abstract TShortIterator iterator();
  
  public abstract short[] toArray();
  
  public abstract short[] toArray(short[] paramArrayOfShort);
  
  public abstract boolean add(short paramShort);
  
  public abstract boolean remove(short paramShort);
  
  public abstract boolean containsAll(Collection<?> paramCollection);
  
  public abstract boolean containsAll(TShortCollection paramTShortCollection);
  
  public abstract boolean containsAll(short[] paramArrayOfShort);
  
  public abstract boolean addAll(Collection<? extends Short> paramCollection);
  
  public abstract boolean addAll(TShortCollection paramTShortCollection);
  
  public abstract boolean addAll(short[] paramArrayOfShort);
  
  public abstract boolean retainAll(Collection<?> paramCollection);
  
  public abstract boolean retainAll(TShortCollection paramTShortCollection);
  
  public abstract boolean retainAll(short[] paramArrayOfShort);
  
  public abstract boolean removeAll(Collection<?> paramCollection);
  
  public abstract boolean removeAll(TShortCollection paramTShortCollection);
  
  public abstract boolean removeAll(short[] paramArrayOfShort);
  
  public abstract void clear();
  
  public abstract boolean forEach(TShortProcedure paramTShortProcedure);
  
  public abstract boolean equals(Object paramObject);
  
  public abstract int hashCode();
}
