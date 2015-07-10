package gnu.trove.list;

import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.procedure.TDoubleProcedure;
import java.util.Random;

public abstract interface TDoubleList
  extends TDoubleCollection
{
  public abstract double getNoEntryValue();
  
  public abstract int size();
  
  public abstract boolean isEmpty();
  
  public abstract boolean add(double paramDouble);
  
  public abstract void add(double[] paramArrayOfDouble);
  
  public abstract void add(double[] paramArrayOfDouble, int paramInt1, int paramInt2);
  
  public abstract void insert(int paramInt, double paramDouble);
  
  public abstract void insert(int paramInt, double[] paramArrayOfDouble);
  
  public abstract void insert(int paramInt1, double[] paramArrayOfDouble, int paramInt2, int paramInt3);
  
  public abstract double get(int paramInt);
  
  public abstract double set(int paramInt, double paramDouble);
  
  public abstract void set(int paramInt, double[] paramArrayOfDouble);
  
  public abstract void set(int paramInt1, double[] paramArrayOfDouble, int paramInt2, int paramInt3);
  
  public abstract double replace(int paramInt, double paramDouble);
  
  public abstract void clear();
  
  public abstract boolean remove(double paramDouble);
  
  public abstract double removeAt(int paramInt);
  
  public abstract void remove(int paramInt1, int paramInt2);
  
  public abstract void transformValues(TDoubleFunction paramTDoubleFunction);
  
  public abstract void reverse();
  
  public abstract void reverse(int paramInt1, int paramInt2);
  
  public abstract void shuffle(Random paramRandom);
  
  public abstract TDoubleList subList(int paramInt1, int paramInt2);
  
  public abstract double[] toArray();
  
  public abstract double[] toArray(int paramInt1, int paramInt2);
  
  public abstract double[] toArray(double[] paramArrayOfDouble);
  
  public abstract double[] toArray(double[] paramArrayOfDouble, int paramInt1, int paramInt2);
  
  public abstract double[] toArray(double[] paramArrayOfDouble, int paramInt1, int paramInt2, int paramInt3);
  
  public abstract boolean forEach(TDoubleProcedure paramTDoubleProcedure);
  
  public abstract boolean forEachDescending(TDoubleProcedure paramTDoubleProcedure);
  
  public abstract void sort();
  
  public abstract void sort(int paramInt1, int paramInt2);
  
  public abstract void fill(double paramDouble);
  
  public abstract void fill(int paramInt1, int paramInt2, double paramDouble);
  
  public abstract int binarySearch(double paramDouble);
  
  public abstract int binarySearch(double paramDouble, int paramInt1, int paramInt2);
  
  public abstract int indexOf(double paramDouble);
  
  public abstract int indexOf(int paramInt, double paramDouble);
  
  public abstract int lastIndexOf(double paramDouble);
  
  public abstract int lastIndexOf(int paramInt, double paramDouble);
  
  public abstract boolean contains(double paramDouble);
  
  public abstract TDoubleList grep(TDoubleProcedure paramTDoubleProcedure);
  
  public abstract TDoubleList inverseGrep(TDoubleProcedure paramTDoubleProcedure);
  
  public abstract double max();
  
  public abstract double min();
  
  public abstract double sum();
}
