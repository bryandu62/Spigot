package gnu.trove.map;

import gnu.trove.TIntCollection;
import gnu.trove.function.TIntFunction;
import gnu.trove.iterator.TIntIntIterator;
import gnu.trove.procedure.TIntIntProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.TIntSet;
import java.util.Map;

public abstract interface TIntIntMap
{
  public abstract int getNoEntryKey();
  
  public abstract int getNoEntryValue();
  
  public abstract int put(int paramInt1, int paramInt2);
  
  public abstract int putIfAbsent(int paramInt1, int paramInt2);
  
  public abstract void putAll(Map<? extends Integer, ? extends Integer> paramMap);
  
  public abstract void putAll(TIntIntMap paramTIntIntMap);
  
  public abstract int get(int paramInt);
  
  public abstract void clear();
  
  public abstract boolean isEmpty();
  
  public abstract int remove(int paramInt);
  
  public abstract int size();
  
  public abstract TIntSet keySet();
  
  public abstract int[] keys();
  
  public abstract int[] keys(int[] paramArrayOfInt);
  
  public abstract TIntCollection valueCollection();
  
  public abstract int[] values();
  
  public abstract int[] values(int[] paramArrayOfInt);
  
  public abstract boolean containsValue(int paramInt);
  
  public abstract boolean containsKey(int paramInt);
  
  public abstract TIntIntIterator iterator();
  
  public abstract boolean forEachKey(TIntProcedure paramTIntProcedure);
  
  public abstract boolean forEachValue(TIntProcedure paramTIntProcedure);
  
  public abstract boolean forEachEntry(TIntIntProcedure paramTIntIntProcedure);
  
  public abstract void transformValues(TIntFunction paramTIntFunction);
  
  public abstract boolean retainEntries(TIntIntProcedure paramTIntIntProcedure);
  
  public abstract boolean increment(int paramInt);
  
  public abstract boolean adjustValue(int paramInt1, int paramInt2);
  
  public abstract int adjustOrPutValue(int paramInt1, int paramInt2, int paramInt3);
}
