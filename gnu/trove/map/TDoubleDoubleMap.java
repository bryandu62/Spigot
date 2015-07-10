package gnu.trove.map;

import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.iterator.TDoubleDoubleIterator;
import gnu.trove.procedure.TDoubleDoubleProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.set.TDoubleSet;
import java.util.Map;

public abstract interface TDoubleDoubleMap
{
  public abstract double getNoEntryKey();
  
  public abstract double getNoEntryValue();
  
  public abstract double put(double paramDouble1, double paramDouble2);
  
  public abstract double putIfAbsent(double paramDouble1, double paramDouble2);
  
  public abstract void putAll(Map<? extends Double, ? extends Double> paramMap);
  
  public abstract void putAll(TDoubleDoubleMap paramTDoubleDoubleMap);
  
  public abstract double get(double paramDouble);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract double remove(double paramDouble);
  
  public abstract int size();
  
  public abstract TDoubleSet keySet();
  
  public abstract double[] keys();
  
  public abstract double[] keys(double[] paramArrayOfDouble);
  
  public abstract TDoubleCollection valueCollection();
  
  public abstract double[] values();
  
  public abstract double[] values(double[] paramArrayOfDouble);
  
  public abstract boolean containsValue(double paramDouble);
  
  public abstract boolean containsKey(double paramDouble);
  
  public abstract TDoubleDoubleIterator iterator();
  
  public abstract boolean forEachKey(TDoubleProcedure paramTDoubleProcedure);
  
  public abstract boolean forEachValue(TDoubleProcedure paramTDoubleProcedure);
  
  public abstract boolean forEachEntry(TDoubleDoubleProcedure paramTDoubleDoubleProcedure);
  
  public abstract void transformValues(TDoubleFunction paramTDoubleFunction);
  
  public abstract boolean retainEntries(TDoubleDoubleProcedure paramTDoubleDoubleProcedure);
  
  public abstract boolean increment(double paramDouble);
  
  public abstract boolean adjustValue(double paramDouble1, double paramDouble2);
  
  public abstract double adjustOrPutValue(double paramDouble1, double paramDouble2, double paramDouble3);
}
