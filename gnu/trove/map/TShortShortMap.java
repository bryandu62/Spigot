package gnu.trove.map;

import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.iterator.TShortShortIterator;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.procedure.TShortShortProcedure;
import gnu.trove.set.TShortSet;
import java.util.Map;

public abstract interface TShortShortMap
{
  public abstract short getNoEntryKey();
  
  public abstract short getNoEntryValue();
  
  public abstract short put(short paramShort1, short paramShort2);
  
  public abstract short putIfAbsent(short paramShort1, short paramShort2);
  
  public abstract void putAll(Map<? extends Short, ? extends Short> paramMap);
  
  public abstract void putAll(TShortShortMap paramTShortShortMap);
  
  public abstract short get(short paramShort);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract short remove(short paramShort);
  
  public abstract int size();
  
  public abstract TShortSet keySet();
  
  public abstract short[] keys();
  
  public abstract short[] keys(short[] paramArrayOfShort);
  
  public abstract TShortCollection valueCollection();
  
  public abstract short[] values();
  
  public abstract short[] values(short[] paramArrayOfShort);
  
  public abstract boolean containsValue(short paramShort);
  
  public abstract boolean containsKey(short paramShort);
  
  public abstract TShortShortIterator iterator();
  
  public abstract boolean forEachKey(TShortProcedure paramTShortProcedure);
  
  public abstract boolean forEachValue(TShortProcedure paramTShortProcedure);
  
  public abstract boolean forEachEntry(TShortShortProcedure paramTShortShortProcedure);
  
  public abstract void transformValues(TShortFunction paramTShortFunction);
  
  public abstract boolean retainEntries(TShortShortProcedure paramTShortShortProcedure);
  
  public abstract boolean increment(short paramShort);
  
  public abstract boolean adjustValue(short paramShort1, short paramShort2);
  
  public abstract short adjustOrPutValue(short paramShort1, short paramShort2, short paramShort3);
}
