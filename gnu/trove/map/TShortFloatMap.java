package gnu.trove.map;

import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.iterator.TShortFloatIterator;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.procedure.TShortFloatProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TShortSet;
import java.util.Map;

public abstract interface TShortFloatMap
{
  public abstract short getNoEntryKey();
  
  public abstract float getNoEntryValue();
  
  public abstract float put(short paramShort, float paramFloat);
  
  public abstract float putIfAbsent(short paramShort, float paramFloat);
  
  public abstract void putAll(Map<? extends Short, ? extends Float> paramMap);
  
  public abstract void putAll(TShortFloatMap paramTShortFloatMap);
  
  public abstract float get(short paramShort);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract float remove(short paramShort);
  
  public abstract int size();
  
  public abstract TShortSet keySet();
  
  public abstract short[] keys();
  
  public abstract short[] keys(short[] paramArrayOfShort);
  
  public abstract TFloatCollection valueCollection();
  
  public abstract float[] values();
  
  public abstract float[] values(float[] paramArrayOfFloat);
  
  public abstract boolean containsValue(float paramFloat);
  
  public abstract boolean containsKey(short paramShort);
  
  public abstract TShortFloatIterator iterator();
  
  public abstract boolean forEachKey(TShortProcedure paramTShortProcedure);
  
  public abstract boolean forEachValue(TFloatProcedure paramTFloatProcedure);
  
  public abstract boolean forEachEntry(TShortFloatProcedure paramTShortFloatProcedure);
  
  public abstract void transformValues(TFloatFunction paramTFloatFunction);
  
  public abstract boolean retainEntries(TShortFloatProcedure paramTShortFloatProcedure);
  
  public abstract boolean increment(short paramShort);
  
  public abstract boolean adjustValue(short paramShort, float paramFloat);
  
  public abstract float adjustOrPutValue(short paramShort, float paramFloat1, float paramFloat2);
}
