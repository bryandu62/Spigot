package gnu.trove.map;

import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.iterator.TDoubleLongIterator;
import gnu.trove.procedure.TDoubleLongProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TDoubleSet;
import java.util.Map;

public abstract interface TDoubleLongMap
{
  public abstract double getNoEntryKey();
  
  public abstract long getNoEntryValue();
  
  public abstract long put(double paramDouble, long paramLong);
  
  public abstract long putIfAbsent(double paramDouble, long paramLong);
  
  public abstract void putAll(Map<? extends Double, ? extends Long> paramMap);
  
  public abstract void putAll(TDoubleLongMap paramTDoubleLongMap);
  
  public abstract long get(double paramDouble);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract long remove(double paramDouble);
  
  public abstract int size();
  
  public abstract TDoubleSet keySet();
  
  public abstract double[] keys();
  
  public abstract double[] keys(double[] paramArrayOfDouble);
  
  public abstract TLongCollection valueCollection();
  
  public abstract long[] values();
  
  public abstract long[] values(long[] paramArrayOfLong);
  
  public abstract boolean containsValue(long paramLong);
  
  public abstract boolean containsKey(double paramDouble);
  
  public abstract TDoubleLongIterator iterator();
  
  public abstract boolean forEachKey(TDoubleProcedure paramTDoubleProcedure);
  
  public abstract boolean forEachValue(TLongProcedure paramTLongProcedure);
  
  public abstract boolean forEachEntry(TDoubleLongProcedure paramTDoubleLongProcedure);
  
  public abstract void transformValues(TLongFunction paramTLongFunction);
  
  public abstract boolean retainEntries(TDoubleLongProcedure paramTDoubleLongProcedure);
  
  public abstract boolean increment(double paramDouble);
  
  public abstract boolean adjustValue(double paramDouble, long paramLong);
  
  public abstract long adjustOrPutValue(double paramDouble, long paramLong1, long paramLong2);
}
