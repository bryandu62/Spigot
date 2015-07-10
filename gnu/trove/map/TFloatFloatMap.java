package gnu.trove.map;

import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.iterator.TFloatFloatIterator;
import gnu.trove.procedure.TFloatFloatProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.set.TFloatSet;
import java.util.Map;

public abstract interface TFloatFloatMap
{
  public abstract float getNoEntryKey();
  
  public abstract float getNoEntryValue();
  
  public abstract float put(float paramFloat1, float paramFloat2);
  
  public abstract float putIfAbsent(float paramFloat1, float paramFloat2);
  
  public abstract void putAll(Map<? extends Float, ? extends Float> paramMap);
  
  public abstract void putAll(TFloatFloatMap paramTFloatFloatMap);
  
  public abstract float get(float paramFloat);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract float remove(float paramFloat);
  
  public abstract int size();
  
  public abstract TFloatSet keySet();
  
  public abstract float[] keys();
  
  public abstract float[] keys(float[] paramArrayOfFloat);
  
  public abstract TFloatCollection valueCollection();
  
  public abstract float[] values();
  
  public abstract float[] values(float[] paramArrayOfFloat);
  
  public abstract boolean containsValue(float paramFloat);
  
  public abstract boolean containsKey(float paramFloat);
  
  public abstract TFloatFloatIterator iterator();
  
  public abstract boolean forEachKey(TFloatProcedure paramTFloatProcedure);
  
  public abstract boolean forEachValue(TFloatProcedure paramTFloatProcedure);
  
  public abstract boolean forEachEntry(TFloatFloatProcedure paramTFloatFloatProcedure);
  
  public abstract void transformValues(TFloatFunction paramTFloatFunction);
  
  public abstract boolean retainEntries(TFloatFloatProcedure paramTFloatFloatProcedure);
  
  public abstract boolean increment(float paramFloat);
  
  public abstract boolean adjustValue(float paramFloat1, float paramFloat2);
  
  public abstract float adjustOrPutValue(float paramFloat1, float paramFloat2, float paramFloat3);
}
