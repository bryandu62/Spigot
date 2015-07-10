package gnu.trove.map;

import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.iterator.TLongDoubleIterator;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TLongDoubleProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TLongSet;
import java.util.Map;

public abstract interface TLongDoubleMap
{
  public abstract long getNoEntryKey();
  
  public abstract double getNoEntryValue();
  
  public abstract double put(long paramLong, double paramDouble);
  
  public abstract double putIfAbsent(long paramLong, double paramDouble);
  
  public abstract void putAll(Map<? extends Long, ? extends Double> paramMap);
  
  public abstract void putAll(TLongDoubleMap paramTLongDoubleMap);
  
  public abstract double get(long paramLong);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract double remove(long paramLong);
  
  public abstract int size();
  
  public abstract TLongSet keySet();
  
  public abstract long[] keys();
  
  public abstract long[] keys(long[] paramArrayOfLong);
  
  public abstract TDoubleCollection valueCollection();
  
  public abstract double[] values();
  
  public abstract double[] values(double[] paramArrayOfDouble);
  
  public abstract boolean containsValue(double paramDouble);
  
  public abstract boolean containsKey(long paramLong);
  
  public abstract TLongDoubleIterator iterator();
  
  public abstract boolean forEachKey(TLongProcedure paramTLongProcedure);
  
  public abstract boolean forEachValue(TDoubleProcedure paramTDoubleProcedure);
  
  public abstract boolean forEachEntry(TLongDoubleProcedure paramTLongDoubleProcedure);
  
  public abstract void transformValues(TDoubleFunction paramTDoubleFunction);
  
  public abstract boolean retainEntries(TLongDoubleProcedure paramTLongDoubleProcedure);
  
  public abstract boolean increment(long paramLong);
  
  public abstract boolean adjustValue(long paramLong, double paramDouble);
  
  public abstract double adjustOrPutValue(long paramLong, double paramDouble1, double paramDouble2);
}
