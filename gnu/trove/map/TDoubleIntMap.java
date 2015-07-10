package gnu.trove.map;

import gnu.trove.TIntCollection;
import gnu.trove.function.TIntFunction;
import gnu.trove.iterator.TDoubleIntIterator;
import gnu.trove.procedure.TDoubleIntProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.TDoubleSet;
import java.util.Map;

public abstract interface TDoubleIntMap
{
  public abstract double getNoEntryKey();
  
  public abstract int getNoEntryValue();
  
  public abstract int put(double paramDouble, int paramInt);
  
  public abstract int putIfAbsent(double paramDouble, int paramInt);
  
  public abstract void putAll(Map<? extends Double, ? extends Integer> paramMap);
  
  public abstract void putAll(TDoubleIntMap paramTDoubleIntMap);
  
  public abstract int get(double paramDouble);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract int remove(double paramDouble);
  
  public abstract int size();
  
  public abstract TDoubleSet keySet();
  
  public abstract double[] keys();
  
  public abstract double[] keys(double[] paramArrayOfDouble);
  
  public abstract TIntCollection valueCollection();
  
  public abstract int[] values();
  
  public abstract int[] values(int[] paramArrayOfInt);
  
  public abstract boolean containsValue(int paramInt);
  
  public abstract boolean containsKey(double paramDouble);
  
  public abstract TDoubleIntIterator iterator();
  
  public abstract boolean forEachKey(TDoubleProcedure paramTDoubleProcedure);
  
  public abstract boolean forEachValue(TIntProcedure paramTIntProcedure);
  
  public abstract boolean forEachEntry(TDoubleIntProcedure paramTDoubleIntProcedure);
  
  public abstract void transformValues(TIntFunction paramTIntFunction);
  
  public abstract boolean retainEntries(TDoubleIntProcedure paramTDoubleIntProcedure);
  
  public abstract boolean increment(double paramDouble);
  
  public abstract boolean adjustValue(double paramDouble, int paramInt);
  
  public abstract int adjustOrPutValue(double paramDouble, int paramInt1, int paramInt2);
}
