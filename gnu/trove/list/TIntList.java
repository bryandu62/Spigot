package gnu.trove.list;

import gnu.trove.TIntCollection;
import gnu.trove.function.TIntFunction;
import gnu.trove.procedure.TIntProcedure;
import java.util.Random;

public abstract interface TIntList
  extends TIntCollection
{
  public abstract int getNoEntryValue();
  
  public abstract int size();
  
  public abstract boolean isEmpty();
  
  public abstract boolean add(int paramInt);
  
  public abstract void add(int[] paramArrayOfInt);
  
  public abstract void add(int[] paramArrayOfInt, int paramInt1, int paramInt2);
  
  public abstract void insert(int paramInt1, int paramInt2);
  
  public abstract void insert(int paramInt, int[] paramArrayOfInt);
  
  public abstract void insert(int paramInt1, int[] paramArrayOfInt, int paramInt2, int paramInt3);
  
  public abstract int get(int paramInt);
  
  public abstract int set(int paramInt1, int paramInt2);
  
  public abstract void set(int paramInt, int[] paramArrayOfInt);
  
  public abstract void set(int paramInt1, int[] paramArrayOfInt, int paramInt2, int paramInt3);
  
  public abstract int replace(int paramInt1, int paramInt2);
  
  public abstract void clear();
  
  public abstract boolean remove(int paramInt);
  
  public abstract int removeAt(int paramInt);
  
  public abstract void remove(int paramInt1, int paramInt2);
  
  public abstract void transformValues(TIntFunction paramTIntFunction);
  
  public abstract void reverse();
  
  public abstract void reverse(int paramInt1, int paramInt2);
  
  public abstract void shuffle(Random paramRandom);
  
  public abstract TIntList subList(int paramInt1, int paramInt2);
  
  public abstract int[] toArray();
  
  public abstract int[] toArray(int paramInt1, int paramInt2);
  
  public abstract int[] toArray(int[] paramArrayOfInt);
  
  public abstract int[] toArray(int[] paramArrayOfInt, int paramInt1, int paramInt2);
  
  public abstract int[] toArray(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3);
  
  public abstract boolean forEach(TIntProcedure paramTIntProcedure);
  
  public abstract boolean forEachDescending(TIntProcedure paramTIntProcedure);
  
  public abstract void sort();
  
  public abstract void sort(int paramInt1, int paramInt2);
  
  public abstract void fill(int paramInt);
  
  public abstract void fill(int paramInt1, int paramInt2, int paramInt3);
  
  public abstract int binarySearch(int paramInt);
  
  public abstract int binarySearch(int paramInt1, int paramInt2, int paramInt3);
  
  public abstract int indexOf(int paramInt);
  
  public abstract int indexOf(int paramInt1, int paramInt2);
  
  public abstract int lastIndexOf(int paramInt);
  
  public abstract int lastIndexOf(int paramInt1, int paramInt2);
  
  public abstract boolean contains(int paramInt);
  
  public abstract TIntList grep(TIntProcedure paramTIntProcedure);
  
  public abstract TIntList inverseGrep(TIntProcedure paramTIntProcedure);
  
  public abstract int max();
  
  public abstract int min();
  
  public abstract int sum();
}
