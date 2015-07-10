package gnu.trove;

import gnu.trove.iterator.TDoubleIterator;
import gnu.trove.procedure.TDoubleProcedure;
import java.util.Collection;

public abstract interface TDoubleCollection
{
  public static final long serialVersionUID = 1L;
  
  public abstract double getNoEntryValue();
  
  public abstract int size();
  
  public abstract boolean isEmpty();
  
  public abstract boolean contains(double paramDouble);
  
  public abstract TDoubleIterator iterator();
  
  public abstract double[] toArray();
  
  public abstract double[] toArray(double[] paramArrayOfDouble);
  
  public abstract boolean add(double paramDouble);
  
  public abstract boolean remove(double paramDouble);
  
  public abstract boolean containsAll(Collection<?> paramCollection);
  
  public abstract boolean containsAll(TDoubleCollection paramTDoubleCollection);
  
  public abstract boolean containsAll(double[] paramArrayOfDouble);
  
  public abstract boolean addAll(Collection<? extends Double> paramCollection);
  
  public abstract boolean addAll(TDoubleCollection paramTDoubleCollection);
  
  public abstract boolean addAll(double[] paramArrayOfDouble);
  
  public abstract boolean retainAll(Collection<?> paramCollection);
  
  public abstract boolean retainAll(TDoubleCollection paramTDoubleCollection);
  
  public abstract boolean retainAll(double[] paramArrayOfDouble);
  
  public abstract boolean removeAll(Collection<?> paramCollection);
  
  public abstract boolean removeAll(TDoubleCollection paramTDoubleCollection);
  
  public abstract boolean removeAll(double[] paramArrayOfDouble);
  
  public abstract void clear();
  
  public abstract boolean forEach(TDoubleProcedure paramTDoubleProcedure);
  
  public abstract boolean equals(Object paramObject);
  
  public abstract int hashCode();
}
