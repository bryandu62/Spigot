package gnu.trove.list;

import gnu.trove.TByteCollection;
import gnu.trove.function.TByteFunction;
import gnu.trove.procedure.TByteProcedure;
import java.util.Random;

public abstract interface TByteList
  extends TByteCollection
{
  public abstract byte getNoEntryValue();
  
  public abstract int size();
  
  public abstract boolean isEmpty();
  
  public abstract boolean add(byte paramByte);
  
  public abstract void add(byte[] paramArrayOfByte);
  
  public abstract void add(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
  
  public abstract void insert(int paramInt, byte paramByte);
  
  public abstract void insert(int paramInt, byte[] paramArrayOfByte);
  
  public abstract void insert(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3);
  
  public abstract byte get(int paramInt);
  
  public abstract byte set(int paramInt, byte paramByte);
  
  public abstract void set(int paramInt, byte[] paramArrayOfByte);
  
  public abstract void set(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3);
  
  public abstract byte replace(int paramInt, byte paramByte);
  
  public abstract void clear();
  
  public abstract boolean remove(byte paramByte);
  
  public abstract byte removeAt(int paramInt);
  
  public abstract void remove(int paramInt1, int paramInt2);
  
  public abstract void transformValues(TByteFunction paramTByteFunction);
  
  public abstract void reverse();
  
  public abstract void reverse(int paramInt1, int paramInt2);
  
  public abstract void shuffle(Random paramRandom);
  
  public abstract TByteList subList(int paramInt1, int paramInt2);
  
  public abstract byte[] toArray();
  
  public abstract byte[] toArray(int paramInt1, int paramInt2);
  
  public abstract byte[] toArray(byte[] paramArrayOfByte);
  
  public abstract byte[] toArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
  
  public abstract byte[] toArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3);
  
  public abstract boolean forEach(TByteProcedure paramTByteProcedure);
  
  public abstract boolean forEachDescending(TByteProcedure paramTByteProcedure);
  
  public abstract void sort();
  
  public abstract void sort(int paramInt1, int paramInt2);
  
  public abstract void fill(byte paramByte);
  
  public abstract void fill(int paramInt1, int paramInt2, byte paramByte);
  
  public abstract int binarySearch(byte paramByte);
  
  public abstract int binarySearch(byte paramByte, int paramInt1, int paramInt2);
  
  public abstract int indexOf(byte paramByte);
  
  public abstract int indexOf(int paramInt, byte paramByte);
  
  public abstract int lastIndexOf(byte paramByte);
  
  public abstract int lastIndexOf(int paramInt, byte paramByte);
  
  public abstract boolean contains(byte paramByte);
  
  public abstract TByteList grep(TByteProcedure paramTByteProcedure);
  
  public abstract TByteList inverseGrep(TByteProcedure paramTByteProcedure);
  
  public abstract byte max();
  
  public abstract byte min();
  
  public abstract byte sum();
}
