package gnu.trove.map;

import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.iterator.TShortDoubleIterator;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TShortDoubleProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TShortSet;
import java.util.Map;

public abstract interface TShortDoubleMap
{
  public abstract short getNoEntryKey();
  
  public abstract double getNoEntryValue();
  
  public abstract double put(short paramShort, double paramDouble);
  
  public abstract double putIfAbsent(short paramShort, double paramDouble);
  
  public abstract void putAll(Map<? extends Short, ? extends Double> paramMap);
  
  public abstract void putAll(TShortDoubleMap paramTShortDoubleMap);
  
  public abstract double get(short paramShort);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract double remove(short paramShort);
  
  public abstract int size();
  
  public abstract TShortSet keySet();
  
  public abstract short[] keys();
  
  public abstract short[] keys(short[] paramArrayOfShort);
  
  public abstract TDoubleCollection valueCollection();
  
  public abstract double[] values();
  
  public abstract double[] values(double[] paramArrayOfDouble);
  
  public abstract boolean containsValue(double paramDouble);
  
  public abstract boolean containsKey(short paramShort);
  
  public abstract TShortDoubleIterator iterator();
  
  public abstract boolean forEachKey(TShortProcedure paramTShortProcedure);
  
  public abstract boolean forEachValue(TDoubleProcedure paramTDoubleProcedure);
  
  public abstract boolean forEachEntry(TShortDoubleProcedure paramTShortDoubleProcedure);
  
  public abstract void transformValues(TDoubleFunction paramTDoubleFunction);
  
  public abstract boolean retainEntries(TShortDoubleProcedure paramTShortDoubleProcedure);
  
  public abstract boolean increment(short paramShort);
  
  public abstract boolean adjustValue(short paramShort, double paramDouble);
  
  public abstract double adjustOrPutValue(short paramShort, double paramDouble1, double paramDouble2);
}
