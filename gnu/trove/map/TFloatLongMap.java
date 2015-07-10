package gnu.trove.map;

import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.iterator.TFloatLongIterator;
import gnu.trove.procedure.TFloatLongProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TFloatSet;
import java.util.Map;

public abstract interface TFloatLongMap
{
  public abstract float getNoEntryKey();
  
  public abstract long getNoEntryValue();
  
  public abstract long put(float paramFloat, long paramLong);
  
  public abstract long putIfAbsent(float paramFloat, long paramLong);
  
  public abstract void putAll(Map<? extends Float, ? extends Long> paramMap);
  
  public abstract void putAll(TFloatLongMap paramTFloatLongMap);
  
  public abstract long get(float paramFloat);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract long remove(float paramFloat);
  
  public abstract int size();
  
  public abstract TFloatSet keySet();
  
  public abstract float[] keys();
  
  public abstract float[] keys(float[] paramArrayOfFloat);
  
  public abstract TLongCollection valueCollection();
  
  public abstract long[] values();
  
  public abstract long[] values(long[] paramArrayOfLong);
  
  public abstract boolean containsValue(long paramLong);
  
  public abstract boolean containsKey(float paramFloat);
  
  public abstract TFloatLongIterator iterator();
  
  public abstract boolean forEachKey(TFloatProcedure paramTFloatProcedure);
  
  public abstract boolean forEachValue(TLongProcedure paramTLongProcedure);
  
  public abstract boolean forEachEntry(TFloatLongProcedure paramTFloatLongProcedure);
  
  public abstract void transformValues(TLongFunction paramTLongFunction);
  
  public abstract boolean retainEntries(TFloatLongProcedure paramTFloatLongProcedure);
  
  public abstract boolean increment(float paramFloat);
  
  public abstract boolean adjustValue(float paramFloat, long paramLong);
  
  public abstract long adjustOrPutValue(float paramFloat, long paramLong1, long paramLong2);
}
