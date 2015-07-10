package gnu.trove.map;

import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.iterator.TIntDoubleIterator;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TIntDoubleProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.TIntSet;
import java.util.Map;

public abstract interface TIntDoubleMap
{
  public abstract int getNoEntryKey();
  
  public abstract double getNoEntryValue();
  
  public abstract double put(int paramInt, double paramDouble);
  
  public abstract double putIfAbsent(int paramInt, double paramDouble);
  
  public abstract void putAll(Map<? extends Integer, ? extends Double> paramMap);
  
  public abstract void putAll(TIntDoubleMap paramTIntDoubleMap);
  
  public abstract double get(int paramInt);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract double remove(int paramInt);
  
  public abstract int size();
  
  public abstract TIntSet keySet();
  
  public abstract int[] keys();
  
  public abstract int[] keys(int[] paramArrayOfInt);
  
  public abstract TDoubleCollection valueCollection();
  
  public abstract double[] values();
  
  public abstract double[] values(double[] paramArrayOfDouble);
  
  public abstract boolean containsValue(double paramDouble);
  
  public abstract boolean containsKey(int paramInt);
  
  public abstract TIntDoubleIterator iterator();
  
  public abstract boolean forEachKey(TIntProcedure paramTIntProcedure);
  
  public abstract boolean forEachValue(TDoubleProcedure paramTDoubleProcedure);
  
  public abstract boolean forEachEntry(TIntDoubleProcedure paramTIntDoubleProcedure);
  
  public abstract void transformValues(TDoubleFunction paramTDoubleFunction);
  
  public abstract boolean retainEntries(TIntDoubleProcedure paramTIntDoubleProcedure);
  
  public abstract boolean increment(int paramInt);
  
  public abstract boolean adjustValue(int paramInt, double paramDouble);
  
  public abstract double adjustOrPutValue(int paramInt, double paramDouble1, double paramDouble2);
}
