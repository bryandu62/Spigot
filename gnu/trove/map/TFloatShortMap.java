package gnu.trove.map;

import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.iterator.TFloatShortIterator;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.procedure.TFloatShortProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TFloatSet;
import java.util.Map;

public abstract interface TFloatShortMap
{
  public abstract float getNoEntryKey();
  
  public abstract short getNoEntryValue();
  
  public abstract short put(float paramFloat, short paramShort);
  
  public abstract short putIfAbsent(float paramFloat, short paramShort);
  
  public abstract void putAll(Map<? extends Float, ? extends Short> paramMap);
  
  public abstract void putAll(TFloatShortMap paramTFloatShortMap);
  
  public abstract short get(float paramFloat);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract short remove(float paramFloat);
  
  public abstract int size();
  
  public abstract TFloatSet keySet();
  
  public abstract float[] keys();
  
  public abstract float[] keys(float[] paramArrayOfFloat);
  
  public abstract TShortCollection valueCollection();
  
  public abstract short[] values();
  
  public abstract short[] values(short[] paramArrayOfShort);
  
  public abstract boolean containsValue(short paramShort);
  
  public abstract boolean containsKey(float paramFloat);
  
  public abstract TFloatShortIterator iterator();
  
  public abstract boolean forEachKey(TFloatProcedure paramTFloatProcedure);
  
  public abstract boolean forEachValue(TShortProcedure paramTShortProcedure);
  
  public abstract boolean forEachEntry(TFloatShortProcedure paramTFloatShortProcedure);
  
  public abstract void transformValues(TShortFunction paramTShortFunction);
  
  public abstract boolean retainEntries(TFloatShortProcedure paramTFloatShortProcedure);
  
  public abstract boolean increment(float paramFloat);
  
  public abstract boolean adjustValue(float paramFloat, short paramShort);
  
  public abstract short adjustOrPutValue(float paramFloat, short paramShort1, short paramShort2);
}
