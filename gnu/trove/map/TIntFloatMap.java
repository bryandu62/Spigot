package gnu.trove.map;

import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.iterator.TIntFloatIterator;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.procedure.TIntFloatProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.TIntSet;
import java.util.Map;

public abstract interface TIntFloatMap
{
  public abstract int getNoEntryKey();
  
  public abstract float getNoEntryValue();
  
  public abstract float put(int paramInt, float paramFloat);
  
  public abstract float putIfAbsent(int paramInt, float paramFloat);
  
  public abstract void putAll(Map<? extends Integer, ? extends Float> paramMap);
  
  public abstract void putAll(TIntFloatMap paramTIntFloatMap);
  
  public abstract float get(int paramInt);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract float remove(int paramInt);
  
  public abstract int size();
  
  public abstract TIntSet keySet();
  
  public abstract int[] keys();
  
  public abstract int[] keys(int[] paramArrayOfInt);
  
  public abstract TFloatCollection valueCollection();
  
  public abstract float[] values();
  
  public abstract float[] values(float[] paramArrayOfFloat);
  
  public abstract boolean containsValue(float paramFloat);
  
  public abstract boolean containsKey(int paramInt);
  
  public abstract TIntFloatIterator iterator();
  
  public abstract boolean forEachKey(TIntProcedure paramTIntProcedure);
  
  public abstract boolean forEachValue(TFloatProcedure paramTFloatProcedure);
  
  public abstract boolean forEachEntry(TIntFloatProcedure paramTIntFloatProcedure);
  
  public abstract void transformValues(TFloatFunction paramTFloatFunction);
  
  public abstract boolean retainEntries(TIntFloatProcedure paramTIntFloatProcedure);
  
  public abstract boolean increment(int paramInt);
  
  public abstract boolean adjustValue(int paramInt, float paramFloat);
  
  public abstract float adjustOrPutValue(int paramInt, float paramFloat1, float paramFloat2);
}
