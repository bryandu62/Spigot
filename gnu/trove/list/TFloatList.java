package gnu.trove.list;

import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.procedure.TFloatProcedure;
import java.util.Random;

public abstract interface TFloatList
  extends TFloatCollection
{
  public abstract float getNoEntryValue();
  
  public abstract int size();
  
  public abstract boolean isEmpty();
  
  public abstract boolean add(float paramFloat);
  
  public abstract void add(float[] paramArrayOfFloat);
  
  public abstract void add(float[] paramArrayOfFloat, int paramInt1, int paramInt2);
  
  public abstract void insert(int paramInt, float paramFloat);
  
  public abstract void insert(int paramInt, float[] paramArrayOfFloat);
  
  public abstract void insert(int paramInt1, float[] paramArrayOfFloat, int paramInt2, int paramInt3);
  
  public abstract float get(int paramInt);
  
  public abstract float set(int paramInt, float paramFloat);
  
  public abstract void set(int paramInt, float[] paramArrayOfFloat);
  
  public abstract void set(int paramInt1, float[] paramArrayOfFloat, int paramInt2, int paramInt3);
  
  public abstract float replace(int paramInt, float paramFloat);
  
  public abstract void clear();
  
  public abstract boolean remove(float paramFloat);
  
  public abstract float removeAt(int paramInt);
  
  public abstract void remove(int paramInt1, int paramInt2);
  
  public abstract void transformValues(TFloatFunction paramTFloatFunction);
  
  public abstract void reverse();
  
  public abstract void reverse(int paramInt1, int paramInt2);
  
  public abstract void shuffle(Random paramRandom);
  
  public abstract TFloatList subList(int paramInt1, int paramInt2);
  
  public abstract float[] toArray();
  
  public abstract float[] toArray(int paramInt1, int paramInt2);
  
  public abstract float[] toArray(float[] paramArrayOfFloat);
  
  public abstract float[] toArray(float[] paramArrayOfFloat, int paramInt1, int paramInt2);
  
  public abstract float[] toArray(float[] paramArrayOfFloat, int paramInt1, int paramInt2, int paramInt3);
  
  public abstract boolean forEach(TFloatProcedure paramTFloatProcedure);
  
  public abstract boolean forEachDescending(TFloatProcedure paramTFloatProcedure);
  
  public abstract void sort();
  
  public abstract void sort(int paramInt1, int paramInt2);
  
  public abstract void fill(float paramFloat);
  
  public abstract void fill(int paramInt1, int paramInt2, float paramFloat);
  
  public abstract int binarySearch(float paramFloat);
  
  public abstract int binarySearch(float paramFloat, int paramInt1, int paramInt2);
  
  public abstract int indexOf(float paramFloat);
  
  public abstract int indexOf(int paramInt, float paramFloat);
  
  public abstract int lastIndexOf(float paramFloat);
  
  public abstract int lastIndexOf(int paramInt, float paramFloat);
  
  public abstract boolean contains(float paramFloat);
  
  public abstract TFloatList grep(TFloatProcedure paramTFloatProcedure);
  
  public abstract TFloatList inverseGrep(TFloatProcedure paramTFloatProcedure);
  
  public abstract float max();
  
  public abstract float min();
  
  public abstract float sum();
}
