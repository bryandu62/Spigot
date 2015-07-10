package gnu.trove.set;

import gnu.trove.TCharCollection;
import gnu.trove.iterator.TCharIterator;
import gnu.trove.procedure.TCharProcedure;
import java.util.Collection;

public abstract interface TCharSet
  extends TCharCollection
{
  public abstract char getNoEntryValue();
  
  public abstract int size();
  
  public abstract boolean isEmpty();
  
  public abstract boolean contains(char paramChar);
  
  public abstract TCharIterator iterator();
  
  public abstract char[] toArray();
  
  public abstract char[] toArray(char[] paramArrayOfChar);
  
  public abstract boolean add(char paramChar);
  
  public abstract boolean remove(char paramChar);
  
  public abstract boolean containsAll(Collection<?> paramCollection);
  
  public abstract boolean containsAll(TCharCollection paramTCharCollection);
  
  public abstract boolean containsAll(char[] paramArrayOfChar);
  
  public abstract boolean addAll(Collection<? extends Character> paramCollection);
  
  public abstract boolean addAll(TCharCollection paramTCharCollection);
  
  public abstract boolean addAll(char[] paramArrayOfChar);
  
  public abstract boolean retainAll(Collection<?> paramCollection);
  
  public abstract boolean retainAll(TCharCollection paramTCharCollection);
  
  public abstract boolean retainAll(char[] paramArrayOfChar);
  
  public abstract boolean removeAll(Collection<?> paramCollection);
  
  public abstract boolean removeAll(TCharCollection paramTCharCollection);
  
  public abstract boolean removeAll(char[] paramArrayOfChar);
  
  public abstract void clear();
  
  public abstract boolean forEach(TCharProcedure paramTCharProcedure);
  
  public abstract boolean equals(Object paramObject);
  
  public abstract int hashCode();
}
