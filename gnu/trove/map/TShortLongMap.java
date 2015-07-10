package gnu.trove.map;

import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.iterator.TShortLongIterator;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.procedure.TShortLongProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TShortSet;
import java.util.Map;

public abstract interface TShortLongMap
{
  public abstract short getNoEntryKey();
  
  public abstract long getNoEntryValue();
  
  public abstract long put(short paramShort, long paramLong);
  
  public abstract long putIfAbsent(short paramShort, long paramLong);
  
  public abstract void putAll(Map<? extends Short, ? extends Long> paramMap);
  
  public abstract void putAll(TShortLongMap paramTShortLongMap);
  
  public abstract long get(short paramShort);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract long remove(short paramShort);
  
  public abstract int size();
  
  public abstract TShortSet keySet();
  
  public abstract short[] keys();
  
  public abstract short[] keys(short[] paramArrayOfShort);
  
  public abstract TLongCollection valueCollection();
  
  public abstract long[] values();
  
  public abstract long[] values(long[] paramArrayOfLong);
  
  public abstract boolean containsValue(long paramLong);
  
  public abstract boolean containsKey(short paramShort);
  
  public abstract TShortLongIterator iterator();
  
  public abstract boolean forEachKey(TShortProcedure paramTShortProcedure);
  
  public abstract boolean forEachValue(TLongProcedure paramTLongProcedure);
  
  public abstract boolean forEachEntry(TShortLongProcedure paramTShortLongProcedure);
  
  public abstract void transformValues(TLongFunction paramTLongFunction);
  
  public abstract boolean retainEntries(TShortLongProcedure paramTShortLongProcedure);
  
  public abstract boolean increment(short paramShort);
  
  public abstract boolean adjustValue(short paramShort, long paramLong);
  
  public abstract long adjustOrPutValue(short paramShort, long paramLong1, long paramLong2);
}
