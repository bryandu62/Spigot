package gnu.trove.map;

import gnu.trove.TIntCollection;
import gnu.trove.function.TIntFunction;
import gnu.trove.iterator.TFloatIntIterator;
import gnu.trove.procedure.TFloatIntProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.TFloatSet;
import java.util.Map;

public abstract interface TFloatIntMap
{
  public abstract float getNoEntryKey();
  
  public abstract int getNoEntryValue();
  
  public abstract int put(float paramFloat, int paramInt);
  
  public abstract int putIfAbsent(float paramFloat, int paramInt);
  
  public abstract void putAll(Map<? extends Float, ? extends Integer> paramMap);
  
  public abstract void putAll(TFloatIntMap paramTFloatIntMap);
  
  public abstract int get(float paramFloat);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract int remove(float paramFloat);
  
  public abstract int size();
  
  public abstract TFloatSet keySet();
  
  public abstract float[] keys();
  
  public abstract float[] keys(float[] paramArrayOfFloat);
  
  public abstract TIntCollection valueCollection();
  
  public abstract int[] values();
  
  public abstract int[] values(int[] paramArrayOfInt);
  
  public abstract boolean containsValue(int paramInt);
  
  public abstract boolean containsKey(float paramFloat);
  
  public abstract TFloatIntIterator iterator();
  
  public abstract boolean forEachKey(TFloatProcedure paramTFloatProcedure);
  
  public abstract boolean forEachValue(TIntProcedure paramTIntProcedure);
  
  public abstract boolean forEachEntry(TFloatIntProcedure paramTFloatIntProcedure);
  
  public abstract void transformValues(TIntFunction paramTIntFunction);
  
  public abstract boolean retainEntries(TFloatIntProcedure paramTFloatIntProcedure);
  
  public abstract boolean increment(float paramFloat);
  
  public abstract boolean adjustValue(float paramFloat, int paramInt);
  
  public abstract int adjustOrPutValue(float paramFloat, int paramInt1, int paramInt2);
}
