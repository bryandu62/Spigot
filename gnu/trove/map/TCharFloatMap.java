package gnu.trove.map;

import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.iterator.TCharFloatIterator;
import gnu.trove.procedure.TCharFloatProcedure;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.set.TCharSet;
import java.util.Map;

public abstract interface TCharFloatMap
{
  public abstract char getNoEntryKey();
  
  public abstract float getNoEntryValue();
  
  public abstract float put(char paramChar, float paramFloat);
  
  public abstract float putIfAbsent(char paramChar, float paramFloat);
  
  public abstract void putAll(Map<? extends Character, ? extends Float> paramMap);
  
  public abstract void putAll(TCharFloatMap paramTCharFloatMap);
  
  public abstract float get(char paramChar);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract float remove(char paramChar);
  
  public abstract int size();
  
  public abstract TCharSet keySet();
  
  public abstract char[] keys();
  
  public abstract char[] keys(char[] paramArrayOfChar);
  
  public abstract TFloatCollection valueCollection();
  
  public abstract float[] values();
  
  public abstract float[] values(float[] paramArrayOfFloat);
  
  public abstract boolean containsValue(float paramFloat);
  
  public abstract boolean containsKey(char paramChar);
  
  public abstract TCharFloatIterator iterator();
  
  public abstract boolean forEachKey(TCharProcedure paramTCharProcedure);
  
  public abstract boolean forEachValue(TFloatProcedure paramTFloatProcedure);
  
  public abstract boolean forEachEntry(TCharFloatProcedure paramTCharFloatProcedure);
  
  public abstract void transformValues(TFloatFunction paramTFloatFunction);
  
  public abstract boolean retainEntries(TCharFloatProcedure paramTCharFloatProcedure);
  
  public abstract boolean increment(char paramChar);
  
  public abstract boolean adjustValue(char paramChar, float paramFloat);
  
  public abstract float adjustOrPutValue(char paramChar, float paramFloat1, float paramFloat2);
}
