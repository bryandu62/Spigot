package gnu.trove.list;

import gnu.trove.TCharCollection;
import gnu.trove.function.TCharFunction;
import gnu.trove.procedure.TCharProcedure;
import java.util.Random;

public abstract interface TCharList
  extends TCharCollection
{
  public abstract char getNoEntryValue();
  
  public abstract int size();
  
  public abstract boolean isEmpty();
  
  public abstract boolean add(char paramChar);
  
  public abstract void add(char[] paramArrayOfChar);
  
  public abstract void add(char[] paramArrayOfChar, int paramInt1, int paramInt2);
  
  public abstract void insert(int paramInt, char paramChar);
  
  public abstract void insert(int paramInt, char[] paramArrayOfChar);
  
  public abstract void insert(int paramInt1, char[] paramArrayOfChar, int paramInt2, int paramInt3);
  
  public abstract char get(int paramInt);
  
  public abstract char set(int paramInt, char paramChar);
  
  public abstract void set(int paramInt, char[] paramArrayOfChar);
  
  public abstract void set(int paramInt1, char[] paramArrayOfChar, int paramInt2, int paramInt3);
  
  public abstract char replace(int paramInt, char paramChar);
  
  public abstract void clear();
  
  public abstract boolean remove(char paramChar);
  
  public abstract char removeAt(int paramInt);
  
  public abstract void remove(int paramInt1, int paramInt2);
  
  public abstract void transformValues(TCharFunction paramTCharFunction);
  
  public abstract void reverse();
  
  public abstract void reverse(int paramInt1, int paramInt2);
  
  public abstract void shuffle(Random paramRandom);
  
  public abstract TCharList subList(int paramInt1, int paramInt2);
  
  public abstract char[] toArray();
  
  public abstract char[] toArray(int paramInt1, int paramInt2);
  
  public abstract char[] toArray(char[] paramArrayOfChar);
  
  public abstract char[] toArray(char[] paramArrayOfChar, int paramInt1, int paramInt2);
  
  public abstract char[] toArray(char[] paramArrayOfChar, int paramInt1, int paramInt2, int paramInt3);
  
  public abstract boolean forEach(TCharProcedure paramTCharProcedure);
  
  public abstract boolean forEachDescending(TCharProcedure paramTCharProcedure);
  
  public abstract void sort();
  
  public abstract void sort(int paramInt1, int paramInt2);
  
  public abstract void fill(char paramChar);
  
  public abstract void fill(int paramInt1, int paramInt2, char paramChar);
  
  public abstract int binarySearch(char paramChar);
  
  public abstract int binarySearch(char paramChar, int paramInt1, int paramInt2);
  
  public abstract int indexOf(char paramChar);
  
  public abstract int indexOf(int paramInt, char paramChar);
  
  public abstract int lastIndexOf(char paramChar);
  
  public abstract int lastIndexOf(int paramInt, char paramChar);
  
  public abstract boolean contains(char paramChar);
  
  public abstract TCharList grep(TCharProcedure paramTCharProcedure);
  
  public abstract TCharList inverseGrep(TCharProcedure paramTCharProcedure);
  
  public abstract char max();
  
  public abstract char min();
  
  public abstract char sum();
}
