package gnu.trove.list;

import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.procedure.TShortProcedure;
import java.util.Random;

public abstract interface TShortList
  extends TShortCollection
{
  public abstract short getNoEntryValue();
  
  public abstract int size();
  
  public abstract boolean isEmpty();
  
  public abstract boolean add(short paramShort);
  
  public abstract void add(short[] paramArrayOfShort);
  
  public abstract void add(short[] paramArrayOfShort, int paramInt1, int paramInt2);
  
  public abstract void insert(int paramInt, short paramShort);
  
  public abstract void insert(int paramInt, short[] paramArrayOfShort);
  
  public abstract void insert(int paramInt1, short[] paramArrayOfShort, int paramInt2, int paramInt3);
  
  public abstract short get(int paramInt);
  
  public abstract short set(int paramInt, short paramShort);
  
  public abstract void set(int paramInt, short[] paramArrayOfShort);
  
  public abstract void set(int paramInt1, short[] paramArrayOfShort, int paramInt2, int paramInt3);
  
  public abstract short replace(int paramInt, short paramShort);
  
  public abstract void clear();
  
  public abstract boolean remove(short paramShort);
  
  public abstract short removeAt(int paramInt);
  
  public abstract void remove(int paramInt1, int paramInt2);
  
  public abstract void transformValues(TShortFunction paramTShortFunction);
  
  public abstract void reverse();
  
  public abstract void reverse(int paramInt1, int paramInt2);
  
  public abstract void shuffle(Random paramRandom);
  
  public abstract TShortList subList(int paramInt1, int paramInt2);
  
  public abstract short[] toArray();
  
  public abstract short[] toArray(int paramInt1, int paramInt2);
  
  public abstract short[] toArray(short[] paramArrayOfShort);
  
  public abstract short[] toArray(short[] paramArrayOfShort, int paramInt1, int paramInt2);
  
  public abstract short[] toArray(short[] paramArrayOfShort, int paramInt1, int paramInt2, int paramInt3);
  
  public abstract boolean forEach(TShortProcedure paramTShortProcedure);
  
  public abstract boolean forEachDescending(TShortProcedure paramTShortProcedure);
  
  public abstract void sort();
  
  public abstract void sort(int paramInt1, int paramInt2);
  
  public abstract void fill(short paramShort);
  
  public abstract void fill(int paramInt1, int paramInt2, short paramShort);
  
  public abstract int binarySearch(short paramShort);
  
  public abstract int binarySearch(short paramShort, int paramInt1, int paramInt2);
  
  public abstract int indexOf(short paramShort);
  
  public abstract int indexOf(int paramInt, short paramShort);
  
  public abstract int lastIndexOf(short paramShort);
  
  public abstract int lastIndexOf(int paramInt, short paramShort);
  
  public abstract boolean contains(short paramShort);
  
  public abstract TShortList grep(TShortProcedure paramTShortProcedure);
  
  public abstract TShortList inverseGrep(TShortProcedure paramTShortProcedure);
  
  public abstract short max();
  
  public abstract short min();
  
  public abstract short sum();
}
