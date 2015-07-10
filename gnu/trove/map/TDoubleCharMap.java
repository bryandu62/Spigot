package gnu.trove.map;

import gnu.trove.TCharCollection;
import gnu.trove.function.TCharFunction;
import gnu.trove.iterator.TDoubleCharIterator;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TDoubleCharProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.set.TDoubleSet;
import java.util.Map;

public abstract interface TDoubleCharMap
{
  public abstract double getNoEntryKey();
  
  public abstract char getNoEntryValue();
  
  public abstract char put(double paramDouble, char paramChar);
  
  public abstract char putIfAbsent(double paramDouble, char paramChar);
  
  public abstract void putAll(Map<? extends Double, ? extends Character> paramMap);
  
  public abstract void putAll(TDoubleCharMap paramTDoubleCharMap);
  
  public abstract char get(double paramDouble);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract char remove(double paramDouble);
  
  public abstract int size();
  
  public abstract TDoubleSet keySet();
  
  public abstract double[] keys();
  
  public abstract double[] keys(double[] paramArrayOfDouble);
  
  public abstract TCharCollection valueCollection();
  
  public abstract char[] values();
  
  public abstract char[] values(char[] paramArrayOfChar);
  
  public abstract boolean containsValue(char paramChar);
  
  public abstract boolean containsKey(double paramDouble);
  
  public abstract TDoubleCharIterator iterator();
  
  public abstract boolean forEachKey(TDoubleProcedure paramTDoubleProcedure);
  
  public abstract boolean forEachValue(TCharProcedure paramTCharProcedure);
  
  public abstract boolean forEachEntry(TDoubleCharProcedure paramTDoubleCharProcedure);
  
  public abstract void transformValues(TCharFunction paramTCharFunction);
  
  public abstract boolean retainEntries(TDoubleCharProcedure paramTDoubleCharProcedure);
  
  public abstract boolean increment(double paramDouble);
  
  public abstract boolean adjustValue(double paramDouble, char paramChar);
  
  public abstract char adjustOrPutValue(double paramDouble, char paramChar1, char paramChar2);
}
