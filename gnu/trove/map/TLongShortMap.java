package gnu.trove.map;

import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.iterator.TLongShortIterator;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.procedure.TLongShortProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TLongSet;
import java.util.Map;

public abstract interface TLongShortMap
{
  public abstract long getNoEntryKey();
  
  public abstract short getNoEntryValue();
  
  public abstract short put(long paramLong, short paramShort);
  
  public abstract short putIfAbsent(long paramLong, short paramShort);
  
  public abstract void putAll(Map<? extends Long, ? extends Short> paramMap);
  
  public abstract void putAll(TLongShortMap paramTLongShortMap);
  
  public abstract short get(long paramLong);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract short remove(long paramLong);
  
  public abstract int size();
  
  public abstract TLongSet keySet();
  
  public abstract long[] keys();
  
  public abstract long[] keys(long[] paramArrayOfLong);
  
  public abstract TShortCollection valueCollection();
  
  public abstract short[] values();
  
  public abstract short[] values(short[] paramArrayOfShort);
  
  public abstract boolean containsValue(short paramShort);
  
  public abstract boolean containsKey(long paramLong);
  
  public abstract TLongShortIterator iterator();
  
  public abstract boolean forEachKey(TLongProcedure paramTLongProcedure);
  
  public abstract boolean forEachValue(TShortProcedure paramTShortProcedure);
  
  public abstract boolean forEachEntry(TLongShortProcedure paramTLongShortProcedure);
  
  public abstract void transformValues(TShortFunction paramTShortFunction);
  
  public abstract boolean retainEntries(TLongShortProcedure paramTLongShortProcedure);
  
  public abstract boolean increment(long paramLong);
  
  public abstract boolean adjustValue(long paramLong, short paramShort);
  
  public abstract short adjustOrPutValue(long paramLong, short paramShort1, short paramShort2);
}
