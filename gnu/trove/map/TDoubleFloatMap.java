package gnu.trove.map;

import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.iterator.TDoubleFloatIterator;
import gnu.trove.procedure.TDoubleFloatProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.set.TDoubleSet;
import java.util.Map;

public abstract interface TDoubleFloatMap
{
  public abstract double getNoEntryKey();
  
  public abstract float getNoEntryValue();
  
  public abstract float put(double paramDouble, float paramFloat);
  
  public abstract float putIfAbsent(double paramDouble, float paramFloat);
  
  public abstract void putAll(Map<? extends Double, ? extends Float> paramMap);
  
  public abstract void putAll(TDoubleFloatMap paramTDoubleFloatMap);
  
  public abstract float get(double paramDouble);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract float remove(double paramDouble);
  
  public abstract int size();
  
  public abstract TDoubleSet keySet();
  
  public abstract double[] keys();
  
  public abstract double[] keys(double[] paramArrayOfDouble);
  
  public abstract TFloatCollection valueCollection();
  
  public abstract float[] values();
  
  public abstract float[] values(float[] paramArrayOfFloat);
  
  public abstract boolean containsValue(float paramFloat);
  
  public abstract boolean containsKey(double paramDouble);
  
  public abstract TDoubleFloatIterator iterator();
  
  public abstract boolean forEachKey(TDoubleProcedure paramTDoubleProcedure);
  
  public abstract boolean forEachValue(TFloatProcedure paramTFloatProcedure);
  
  public abstract boolean forEachEntry(TDoubleFloatProcedure paramTDoubleFloatProcedure);
  
  public abstract void transformValues(TFloatFunction paramTFloatFunction);
  
  public abstract boolean retainEntries(TDoubleFloatProcedure paramTDoubleFloatProcedure);
  
  public abstract boolean increment(double paramDouble);
  
  public abstract boolean adjustValue(double paramDouble, float paramFloat);
  
  public abstract float adjustOrPutValue(double paramDouble, float paramFloat1, float paramFloat2);
}
