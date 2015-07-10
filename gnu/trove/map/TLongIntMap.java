package gnu.trove.map;

import gnu.trove.TIntCollection;
import gnu.trove.function.TIntFunction;
import gnu.trove.iterator.TLongIntIterator;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.procedure.TLongIntProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TLongSet;
import java.util.Map;

public abstract interface TLongIntMap
{
  public abstract long getNoEntryKey();
  
  public abstract int getNoEntryValue();
  
  public abstract int put(long paramLong, int paramInt);
  
  public abstract int putIfAbsent(long paramLong, int paramInt);
  
  public abstract void putAll(Map<? extends Long, ? extends Integer> paramMap);
  
  public abstract void putAll(TLongIntMap paramTLongIntMap);
  
  public abstract int get(long paramLong);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract int remove(long paramLong);
  
  public abstract int size();
  
  public abstract TLongSet keySet();
  
  public abstract long[] keys();
  
  public abstract long[] keys(long[] paramArrayOfLong);
  
  public abstract TIntCollection valueCollection();
  
  public abstract int[] values();
  
  public abstract int[] values(int[] paramArrayOfInt);
  
  public abstract boolean containsValue(int paramInt);
  
  public abstract boolean containsKey(long paramLong);
  
  public abstract TLongIntIterator iterator();
  
  public abstract boolean forEachKey(TLongProcedure paramTLongProcedure);
  
  public abstract boolean forEachValue(TIntProcedure paramTIntProcedure);
  
  public abstract boolean forEachEntry(TLongIntProcedure paramTLongIntProcedure);
  
  public abstract void transformValues(TIntFunction paramTIntFunction);
  
  public abstract boolean retainEntries(TLongIntProcedure paramTLongIntProcedure);
  
  public abstract boolean increment(long paramLong);
  
  public abstract boolean adjustValue(long paramLong, int paramInt);
  
  public abstract int adjustOrPutValue(long paramLong, int paramInt1, int paramInt2);
}
