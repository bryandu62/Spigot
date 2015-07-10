package gnu.trove.map;

import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TDoubleShortProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TDoubleSet;
import java.util.Map;

public abstract interface TDoubleShortMap
{
  public abstract double getNoEntryKey();
  
  public abstract short getNoEntryValue();
  
  public abstract short put(double paramDouble, short paramShort);
  
  public abstract short putIfAbsent(double paramDouble, short paramShort);
  
  public abstract void putAll(Map<? extends Double, ? extends Short> paramMap);
  
  public abstract void putAll(TDoubleShortMap paramTDoubleShortMap);
  
  public abstract short get(double paramDouble);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract short remove(double paramDouble);
  
  public abstract int size();
  
  public abstract TDoubleSet keySet();
  
  public abstract double[] keys();
  
  public abstract double[] keys(double[] paramArrayOfDouble);
  
  public abstract TShortCollection valueCollection();
  
  public abstract short[] values();
  
  public abstract short[] values(short[] paramArrayOfShort);
  
  public abstract boolean containsValue(short paramShort);
  
  public abstract boolean containsKey(double paramDouble);
  
  public abstract TDoubleShortIterator iterator();
  
  public abstract boolean forEachKey(TDoubleProcedure paramTDoubleProcedure);
  
  public abstract boolean forEachValue(TShortProcedure paramTShortProcedure);
  
  public abstract boolean forEachEntry(TDoubleShortProcedure paramTDoubleShortProcedure);
  
  public abstract void transformValues(TShortFunction paramTShortFunction);
  
  public abstract boolean retainEntries(TDoubleShortProcedure paramTDoubleShortProcedure);
  
  public abstract boolean increment(double paramDouble);
  
  public abstract boolean adjustValue(double paramDouble, short paramShort);
  
  public abstract short adjustOrPutValue(double paramDouble, short paramShort1, short paramShort2);
}
