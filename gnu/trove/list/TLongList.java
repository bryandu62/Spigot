package gnu.trove.list;

import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.procedure.TLongProcedure;
import java.util.Random;

public abstract interface TLongList
  extends TLongCollection
{
  public abstract long getNoEntryValue();
  
  public abstract int size();
  
  public abstract boolean isEmpty();
  
  public abstract boolean add(long paramLong);
  
  public abstract void add(long[] paramArrayOfLong);
  
  public abstract void add(long[] paramArrayOfLong, int paramInt1, int paramInt2);
  
  public abstract void insert(int paramInt, long paramLong);
  
  public abstract void insert(int paramInt, long[] paramArrayOfLong);
  
  public abstract void insert(int paramInt1, long[] paramArrayOfLong, int paramInt2, int paramInt3);
  
  public abstract long get(int paramInt);
  
  public abstract long set(int paramInt, long paramLong);
  
  public abstract void set(int paramInt, long[] paramArrayOfLong);
  
  public abstract void set(int paramInt1, long[] paramArrayOfLong, int paramInt2, int paramInt3);
  
  public abstract long replace(int paramInt, long paramLong);
  
  public abstract void clear();
  
  public abstract boolean remove(long paramLong);
  
  public abstract long removeAt(int paramInt);
  
  public abstract void remove(int paramInt1, int paramInt2);
  
  public abstract void transformValues(TLongFunction paramTLongFunction);
  
  public abstract void reverse();
  
  public abstract void reverse(int paramInt1, int paramInt2);
  
  public abstract void shuffle(Random paramRandom);
  
  public abstract TLongList subList(int paramInt1, int paramInt2);
  
  public abstract long[] toArray();
  
  public abstract long[] toArray(int paramInt1, int paramInt2);
  
  public abstract long[] toArray(long[] paramArrayOfLong);
  
  public abstract long[] toArray(long[] paramArrayOfLong, int paramInt1, int paramInt2);
  
  public abstract long[] toArray(long[] paramArrayOfLong, int paramInt1, int paramInt2, int paramInt3);
  
  public abstract boolean forEach(TLongProcedure paramTLongProcedure);
  
  public abstract boolean forEachDescending(TLongProcedure paramTLongProcedure);
  
  public abstract void sort();
  
  public abstract void sort(int paramInt1, int paramInt2);
  
  public abstract void fill(long paramLong);
  
  public abstract void fill(int paramInt1, int paramInt2, long paramLong);
  
  public abstract int binarySearch(long paramLong);
  
  public abstract int binarySearch(long paramLong, int paramInt1, int paramInt2);
  
  public abstract int indexOf(long paramLong);
  
  public abstract int indexOf(int paramInt, long paramLong);
  
  public abstract int lastIndexOf(long paramLong);
  
  public abstract int lastIndexOf(int paramInt, long paramLong);
  
  public abstract boolean contains(long paramLong);
  
  public abstract TLongList grep(TLongProcedure paramTLongProcedure);
  
  public abstract TLongList inverseGrep(TLongProcedure paramTLongProcedure);
  
  public abstract long max();
  
  public abstract long min();
  
  public abstract long sum();
}
