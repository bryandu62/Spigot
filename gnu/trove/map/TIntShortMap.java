package gnu.trove.map;

import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.iterator.TIntShortIterator;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.procedure.TIntShortProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TIntSet;
import java.util.Map;

public abstract interface TIntShortMap
{
  public abstract int getNoEntryKey();
  
  public abstract short getNoEntryValue();
  
  public abstract short put(int paramInt, short paramShort);
  
  public abstract short putIfAbsent(int paramInt, short paramShort);
  
  public abstract void putAll(Map<? extends Integer, ? extends Short> paramMap);
  
  public abstract void putAll(TIntShortMap paramTIntShortMap);
  
  public abstract short get(int paramInt);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract short remove(int paramInt);
  
  public abstract int size();
  
  public abstract TIntSet keySet();
  
  public abstract int[] keys();
  
  public abstract int[] keys(int[] paramArrayOfInt);
  
  public abstract TShortCollection valueCollection();
  
  public abstract short[] values();
  
  public abstract short[] values(short[] paramArrayOfShort);
  
  public abstract boolean containsValue(short paramShort);
  
  public abstract boolean containsKey(int paramInt);
  
  public abstract TIntShortIterator iterator();
  
  public abstract boolean forEachKey(TIntProcedure paramTIntProcedure);
  
  public abstract boolean forEachValue(TShortProcedure paramTShortProcedure);
  
  public abstract boolean forEachEntry(TIntShortProcedure paramTIntShortProcedure);
  
  public abstract void transformValues(TShortFunction paramTShortFunction);
  
  public abstract boolean retainEntries(TIntShortProcedure paramTIntShortProcedure);
  
  public abstract boolean increment(int paramInt);
  
  public abstract boolean adjustValue(int paramInt, short paramShort);
  
  public abstract short adjustOrPutValue(int paramInt, short paramShort1, short paramShort2);
}
