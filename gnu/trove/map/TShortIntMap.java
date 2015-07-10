package gnu.trove.map;

import gnu.trove.TIntCollection;
import gnu.trove.function.TIntFunction;
import gnu.trove.iterator.TShortIntIterator;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.procedure.TShortIntProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TShortSet;
import java.util.Map;

public abstract interface TShortIntMap
{
  public abstract short getNoEntryKey();
  
  public abstract int getNoEntryValue();
  
  public abstract int put(short paramShort, int paramInt);
  
  public abstract int putIfAbsent(short paramShort, int paramInt);
  
  public abstract void putAll(Map<? extends Short, ? extends Integer> paramMap);
  
  public abstract void putAll(TShortIntMap paramTShortIntMap);
  
  public abstract int get(short paramShort);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract int remove(short paramShort);
  
  public abstract int size();
  
  public abstract TShortSet keySet();
  
  public abstract short[] keys();
  
  public abstract short[] keys(short[] paramArrayOfShort);
  
  public abstract TIntCollection valueCollection();
  
  public abstract int[] values();
  
  public abstract int[] values(int[] paramArrayOfInt);
  
  public abstract boolean containsValue(int paramInt);
  
  public abstract boolean containsKey(short paramShort);
  
  public abstract TShortIntIterator iterator();
  
  public abstract boolean forEachKey(TShortProcedure paramTShortProcedure);
  
  public abstract boolean forEachValue(TIntProcedure paramTIntProcedure);
  
  public abstract boolean forEachEntry(TShortIntProcedure paramTShortIntProcedure);
  
  public abstract void transformValues(TIntFunction paramTIntFunction);
  
  public abstract boolean retainEntries(TShortIntProcedure paramTShortIntProcedure);
  
  public abstract boolean increment(short paramShort);
  
  public abstract boolean adjustValue(short paramShort, int paramInt);
  
  public abstract int adjustOrPutValue(short paramShort, int paramInt1, int paramInt2);
}
