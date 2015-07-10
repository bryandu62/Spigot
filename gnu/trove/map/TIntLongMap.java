package gnu.trove.map;

import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.iterator.TIntLongIterator;
import gnu.trove.procedure.TIntLongProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TIntSet;
import java.util.Map;

public abstract interface TIntLongMap
{
  public abstract int getNoEntryKey();
  
  public abstract long getNoEntryValue();
  
  public abstract long put(int paramInt, long paramLong);
  
  public abstract long putIfAbsent(int paramInt, long paramLong);
  
  public abstract void putAll(Map<? extends Integer, ? extends Long> paramMap);
  
  public abstract void putAll(TIntLongMap paramTIntLongMap);
  
  public abstract long get(int paramInt);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract long remove(int paramInt);
  
  public abstract int size();
  
  public abstract TIntSet keySet();
  
  public abstract int[] keys();
  
  public abstract int[] keys(int[] paramArrayOfInt);
  
  public abstract TLongCollection valueCollection();
  
  public abstract long[] values();
  
  public abstract long[] values(long[] paramArrayOfLong);
  
  public abstract boolean containsValue(long paramLong);
  
  public abstract boolean containsKey(int paramInt);
  
  public abstract TIntLongIterator iterator();
  
  public abstract boolean forEachKey(TIntProcedure paramTIntProcedure);
  
  public abstract boolean forEachValue(TLongProcedure paramTLongProcedure);
  
  public abstract boolean forEachEntry(TIntLongProcedure paramTIntLongProcedure);
  
  public abstract void transformValues(TLongFunction paramTLongFunction);
  
  public abstract boolean retainEntries(TIntLongProcedure paramTIntLongProcedure);
  
  public abstract boolean increment(int paramInt);
  
  public abstract boolean adjustValue(int paramInt, long paramLong);
  
  public abstract long adjustOrPutValue(int paramInt, long paramLong1, long paramLong2);
}
