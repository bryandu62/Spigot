package gnu.trove.set;

import gnu.trove.TFloatCollection;
import gnu.trove.iterator.TFloatIterator;
import gnu.trove.procedure.TFloatProcedure;
import java.util.Collection;

public abstract interface TFloatSet
  extends TFloatCollection
{
  public abstract float getNoEntryValue();
  
  public abstract int size();
  
  public abstract boolean isEmpty();
  
  public abstract boolean contains(float paramFloat);
  
  public abstract TFloatIterator iterator();
  
  public abstract float[] toArray();
  
  public abstract float[] toArray(float[] paramArrayOfFloat);
  
  public abstract boolean add(float paramFloat);
  
  public abstract boolean remove(float paramFloat);
  
  public abstract boolean containsAll(Collection<?> paramCollection);
  
  public abstract boolean containsAll(TFloatCollection paramTFloatCollection);
  
  public abstract boolean containsAll(float[] paramArrayOfFloat);
  
  public abstract boolean addAll(Collection<? extends Float> paramCollection);
  
  public abstract boolean addAll(TFloatCollection paramTFloatCollection);
  
  public abstract boolean addAll(float[] paramArrayOfFloat);
  
  public abstract boolean retainAll(Collection<?> paramCollection);
  
  public abstract boolean retainAll(TFloatCollection paramTFloatCollection);
  
  public abstract boolean retainAll(float[] paramArrayOfFloat);
  
  public abstract boolean removeAll(Collection<?> paramCollection);
  
  public abstract boolean removeAll(TFloatCollection paramTFloatCollection);
  
  public abstract boolean removeAll(float[] paramArrayOfFloat);
  
  public abstract void clear();
  
  public abstract boolean forEach(TFloatProcedure paramTFloatProcedure);
  
  public abstract boolean equals(Object paramObject);
  
  public abstract int hashCode();
}
