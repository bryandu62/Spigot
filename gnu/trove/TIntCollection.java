package gnu.trove;

import gnu.trove.iterator.TIntIterator;
import gnu.trove.procedure.TIntProcedure;
import java.util.Collection;

public abstract interface TIntCollection
{
  public static final long serialVersionUID = 1L;
  
  public abstract int getNoEntryValue();
  
  public abstract int size();
  
  public abstract boolean isEmpty();
  
  public abstract boolean contains(int paramInt);
  
  public abstract TIntIterator iterator();
  
  public abstract int[] toArray();
  
  public abstract int[] toArray(int[] paramArrayOfInt);
  
  public abstract boolean add(int paramInt);
  
  public abstract boolean remove(int paramInt);
  
  public abstract boolean containsAll(Collection<?> paramCollection);
  
  public abstract boolean containsAll(TIntCollection paramTIntCollection);
  
  public abstract boolean containsAll(int[] paramArrayOfInt);
  
  public abstract boolean addAll(Collection<? extends Integer> paramCollection);
  
  public abstract boolean addAll(TIntCollection paramTIntCollection);
  
  public abstract boolean addAll(int[] paramArrayOfInt);
  
  public abstract boolean retainAll(Collection<?> paramCollection);
  
  public abstract boolean retainAll(TIntCollection paramTIntCollection);
  
  public abstract boolean retainAll(int[] paramArrayOfInt);
  
  public abstract boolean removeAll(Collection<?> paramCollection);
  
  public abstract boolean removeAll(TIntCollection paramTIntCollection);
  
  public abstract boolean removeAll(int[] paramArrayOfInt);
  
  public abstract void clear();
  
  public abstract boolean forEach(TIntProcedure paramTIntProcedure);
  
  public abstract boolean equals(Object paramObject);
  
  public abstract int hashCode();
}
