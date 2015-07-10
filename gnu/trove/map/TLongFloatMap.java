package gnu.trove.map;

import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.iterator.TLongFloatIterator;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.procedure.TLongFloatProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TLongSet;
import java.util.Map;

public abstract interface TLongFloatMap
{
  public abstract long getNoEntryKey();
  
  public abstract float getNoEntryValue();
  
  public abstract float put(long paramLong, float paramFloat);
  
  public abstract float putIfAbsent(long paramLong, float paramFloat);
  
  public abstract void putAll(Map<? extends Long, ? extends Float> paramMap);
  
  public abstract void putAll(TLongFloatMap paramTLongFloatMap);
  
  public abstract float get(long paramLong);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract float remove(long paramLong);
  
  public abstract int size();
  
  public abstract TLongSet keySet();
  
  public abstract long[] keys();
  
  public abstract long[] keys(long[] paramArrayOfLong);
  
  public abstract TFloatCollection valueCollection();
  
  public abstract float[] values();
  
  public abstract float[] values(float[] paramArrayOfFloat);
  
  public abstract boolean containsValue(float paramFloat);
  
  public abstract boolean containsKey(long paramLong);
  
  public abstract TLongFloatIterator iterator();
  
  public abstract boolean forEachKey(TLongProcedure paramTLongProcedure);
  
  public abstract boolean forEachValue(TFloatProcedure paramTFloatProcedure);
  
  public abstract boolean forEachEntry(TLongFloatProcedure paramTLongFloatProcedure);
  
  public abstract void transformValues(TFloatFunction paramTFloatFunction);
  
  public abstract boolean retainEntries(TLongFloatProcedure paramTLongFloatProcedure);
  
  public abstract boolean increment(long paramLong);
  
  public abstract boolean adjustValue(long paramLong, float paramFloat);
  
  public abstract float adjustOrPutValue(long paramLong, float paramFloat1, float paramFloat2);
}
