package gnu.trove.map;

import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.iterator.TCharDoubleIterator;
import gnu.trove.procedure.TCharDoubleProcedure;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.set.TCharSet;
import java.util.Map;

public abstract interface TCharDoubleMap
{
  public abstract char getNoEntryKey();
  
  public abstract double getNoEntryValue();
  
  public abstract double put(char paramChar, double paramDouble);
  
  public abstract double putIfAbsent(char paramChar, double paramDouble);
  
  public abstract void putAll(Map<? extends Character, ? extends Double> paramMap);
  
  public abstract void putAll(TCharDoubleMap paramTCharDoubleMap);
  
  public abstract double get(char paramChar);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract double remove(char paramChar);
  
  public abstract int size();
  
  public abstract TCharSet keySet();
  
  public abstract char[] keys();
  
  public abstract char[] keys(char[] paramArrayOfChar);
  
  public abstract TDoubleCollection valueCollection();
  
  public abstract double[] values();
  
  public abstract double[] values(double[] paramArrayOfDouble);
  
  public abstract boolean containsValue(double paramDouble);
  
  public abstract boolean containsKey(char paramChar);
  
  public abstract TCharDoubleIterator iterator();
  
  public abstract boolean forEachKey(TCharProcedure paramTCharProcedure);
  
  public abstract boolean forEachValue(TDoubleProcedure paramTDoubleProcedure);
  
  public abstract boolean forEachEntry(TCharDoubleProcedure paramTCharDoubleProcedure);
  
  public abstract void transformValues(TDoubleFunction paramTDoubleFunction);
  
  public abstract boolean retainEntries(TCharDoubleProcedure paramTCharDoubleProcedure);
  
  public abstract boolean increment(char paramChar);
  
  public abstract boolean adjustValue(char paramChar, double paramDouble);
  
  public abstract double adjustOrPutValue(char paramChar, double paramDouble1, double paramDouble2);
}
