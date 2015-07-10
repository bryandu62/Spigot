package gnu.trove.map;

import gnu.trove.TCharCollection;
import gnu.trove.function.TCharFunction;
import gnu.trove.iterator.TFloatCharIterator;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TFloatCharProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.set.TFloatSet;
import java.util.Map;

public abstract interface TFloatCharMap
{
  public abstract float getNoEntryKey();
  
  public abstract char getNoEntryValue();
  
  public abstract char put(float paramFloat, char paramChar);
  
  public abstract char putIfAbsent(float paramFloat, char paramChar);
  
  public abstract void putAll(Map<? extends Float, ? extends Character> paramMap);
  
  public abstract void putAll(TFloatCharMap paramTFloatCharMap);
  
  public abstract char get(float paramFloat);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract char remove(float paramFloat);
  
  public abstract int size();
  
  public abstract TFloatSet keySet();
  
  public abstract float[] keys();
  
  public abstract float[] keys(float[] paramArrayOfFloat);
  
  public abstract TCharCollection valueCollection();
  
  public abstract char[] values();
  
  public abstract char[] values(char[] paramArrayOfChar);
  
  public abstract boolean containsValue(char paramChar);
  
  public abstract boolean containsKey(float paramFloat);
  
  public abstract TFloatCharIterator iterator();
  
  public abstract boolean forEachKey(TFloatProcedure paramTFloatProcedure);
  
  public abstract boolean forEachValue(TCharProcedure paramTCharProcedure);
  
  public abstract boolean forEachEntry(TFloatCharProcedure paramTFloatCharProcedure);
  
  public abstract void transformValues(TCharFunction paramTCharFunction);
  
  public abstract boolean retainEntries(TFloatCharProcedure paramTFloatCharProcedure);
  
  public abstract boolean increment(float paramFloat);
  
  public abstract boolean adjustValue(float paramFloat, char paramChar);
  
  public abstract char adjustOrPutValue(float paramFloat, char paramChar1, char paramChar2);
}
