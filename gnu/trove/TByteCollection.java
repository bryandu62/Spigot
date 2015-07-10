package gnu.trove;

import gnu.trove.iterator.TByteIterator;
import gnu.trove.procedure.TByteProcedure;
import java.util.Collection;

public abstract interface TByteCollection
{
  public static final long serialVersionUID = 1L;
  
  public abstract byte getNoEntryValue();
  
  public abstract int size();
  
  public abstract boolean isEmpty();
  
  public abstract boolean contains(byte paramByte);
  
  public abstract TByteIterator iterator();
  
  public abstract byte[] toArray();
  
  public abstract byte[] toArray(byte[] paramArrayOfByte);
  
  public abstract boolean add(byte paramByte);
  
  public abstract boolean remove(byte paramByte);
  
  public abstract boolean containsAll(Collection<?> paramCollection);
  
  public abstract boolean containsAll(TByteCollection paramTByteCollection);
  
  public abstract boolean containsAll(byte[] paramArrayOfByte);
  
  public abstract boolean addAll(Collection<? extends Byte> paramCollection);
  
  public abstract boolean addAll(TByteCollection paramTByteCollection);
  
  public abstract boolean addAll(byte[] paramArrayOfByte);
  
  public abstract boolean retainAll(Collection<?> paramCollection);
  
  public abstract boolean retainAll(TByteCollection paramTByteCollection);
  
  public abstract boolean retainAll(byte[] paramArrayOfByte);
  
  public abstract boolean removeAll(Collection<?> paramCollection);
  
  public abstract boolean removeAll(TByteCollection paramTByteCollection);
  
  public abstract boolean removeAll(byte[] paramArrayOfByte);
  
  public abstract void clear();
  
  public abstract boolean forEach(TByteProcedure paramTByteProcedure);
  
  public abstract boolean equals(Object paramObject);
  
  public abstract int hashCode();
}
