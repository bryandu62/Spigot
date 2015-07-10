package gnu.trove.map;

import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.iterator.TLongLongIterator;
import gnu.trove.procedure.TLongLongProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TLongSet;
import java.util.Map;

public abstract interface TLongLongMap
{
  public abstract long getNoEntryKey();
  
  public abstract long getNoEntryValue();
  
  public abstract long put(long paramLong1, long paramLong2);
  
  public abstract long putIfAbsent(long paramLong1, long paramLong2);
  
  public abstract void putAll(Map<? extends Long, ? extends Long> paramMap);
  
  public abstract void putAll(TLongLongMap paramTLongLongMap);
  
  public abstract long get(long paramLong);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract long remove(long paramLong);
  
  public abstract int size();
  
  public abstract TLongSet keySet();
  
  public abstract long[] keys();
  
  public abstract long[] keys(long[] paramArrayOfLong);
  
  public abstract TLongCollection valueCollection();
  
  public abstract long[] values();
  
  public abstract long[] values(long[] paramArrayOfLong);
  
  public abstract boolean containsValue(long paramLong);
  
  public abstract boolean containsKey(long paramLong);
  
  public abstract TLongLongIterator iterator();
  
  public abstract boolean forEachKey(TLongProcedure paramTLongProcedure);
  
  public abstract boolean forEachValue(TLongProcedure paramTLongProcedure);
  
  public abstract boolean forEachEntry(TLongLongProcedure paramTLongLongProcedure);
  
  public abstract void transformValues(TLongFunction paramTLongFunction);
  
  public abstract boolean retainEntries(TLongLongProcedure paramTLongLongProcedure);
  
  public abstract boolean increment(long paramLong);
  
  public abstract boolean adjustValue(long paramLong1, long paramLong2);
  
  public abstract long adjustOrPutValue(long paramLong1, long paramLong2, long paramLong3);
}
