package gnu.trove.map;

import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.iterator.TFloatDoubleIterator;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TFloatDoubleProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.set.TFloatSet;
import java.util.Map;

public abstract interface TFloatDoubleMap
{
  public abstract float getNoEntryKey();
  
  public abstract double getNoEntryValue();
  
  public abstract double put(float paramFloat, double paramDouble);
  
  public abstract double putIfAbsent(float paramFloat, double paramDouble);
  
  public abstract void putAll(Map<? extends Float, ? extends Double> paramMap);
  
  public abstract void putAll(TFloatDoubleMap paramTFloatDoubleMap);
  
  public abstract double get(float paramFloat);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract double remove(float paramFloat);
  
  public abstract int size();
  
  public abstract TFloatSet keySet();
  
  public abstract float[] keys();
  
  public abstract float[] keys(float[] paramArrayOfFloat);
  
  public abstract TDoubleCollection valueCollection();
  
  public abstract double[] values();
  
  public abstract double[] values(double[] paramArrayOfDouble);
  
  public abstract boolean containsValue(double paramDouble);
  
  public abstract boolean containsKey(float paramFloat);
  
  public abstract TFloatDoubleIterator iterator();
  
  public abstract boolean forEachKey(TFloatProcedure paramTFloatProcedure);
  
  public abstract boolean forEachValue(TDoubleProcedure paramTDoubleProcedure);
  
  public abstract boolean forEachEntry(TFloatDoubleProcedure paramTFloatDoubleProcedure);
  
  public abstract void transformValues(TDoubleFunction paramTDoubleFunction);
  
  public abstract boolean retainEntries(TFloatDoubleProcedure paramTFloatDoubleProcedure);
  
  public abstract boolean increment(float paramFloat);
  
  public abstract boolean adjustValue(float paramFloat, double paramDouble);
  
  public abstract double adjustOrPutValue(float paramFloat, double paramDouble1, double paramDouble2);
}
